/*
 Copyright 2015 Microsoft Open Technologies, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.microsoftopentechnologies.azchat.web.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.FriendRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.PreferenceMetadataDAO;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserDAO;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserPrefBean;

/**
 * @author Dnyaneshwar_Pawar
 *
 */
@Service
@Qualifier("loginService")
public class LoginService extends BaseServiceImpl {

	private static final Logger LOGGER = LogManager
			.getLogger(LoginService.class);

	@Autowired
	private UserDAO userDao;

	@Autowired
	private FriendRequestDAOImpl friendRequestDAOImpl;
	
	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;
	
	@Autowired
	private PreferenceMetadataDAO preferenceMetadataDAO;
	
	@Autowired
	@Qualifier("contentShareService")
	private ContentShareService contentShareService;

	/**
	 * Execute Service implementation for the Login service.The switch takes
	 * action parameter coming from service calling controller which is instance
	 * of ServiceActionEnum.The actions in this method are mapped conventionally
	 * by ServiceActionEnum to execute the operation and modify the model bean.
	 * 
	 * @throws Exception
	 */
	@Override
	public BaseBean executeService(BaseBean baseBean) throws AzureChatException {
		LOGGER.info("[LoginService][executeService]          start");
		switch (baseBean.getServiceAction()) {
		case LOGIN:
			baseBean = processUserLogin((UserBean) baseBean);
			break;
		default:
			break;
		}
		LOGGER.info("[LoginService][executeService]          end");
		return baseBean;
	}

	/**
	 * This method process the login user details.Fetch the logged in user
	 * information from the database.
	 * 
	 * @param loginBean
	 * @throws Exception
	 */
	private UserBean processUserLogin(UserBean userBean)
			throws AzureChatException {
		LOGGER.info("[LoginService][processLoginForm]          start");
		List<UserEntity> userEntities = null;
		String samlToken = userBean.getSamplToken();
		LOGGER.debug("saml token : " + samlToken);
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			Document assertionDoc = AzureChatUtils.getDocument(samlToken);
			String nameID = AzureChatUtils.getNameIDFromAssertion(xPath,
					assertionDoc);
			LOGGER.debug("	Name ID : " + nameID);

			userBean.setNameID(nameID);

			Map<String, String> claimMap = AzureChatUtils
					.getUserAttributeDetails(xPath, assertionDoc);
			if (claimMap != null && !claimMap.isEmpty()) {
				userBean.setIdProvider(claimMap
						.get(AzureChatConstants.ATTR_IDENTITY_PROVIDER));
				userBean.setEmail(claimMap
						.get(AzureChatConstants.ATTR_EMAIL_ADDRESS));
				if (claimMap.get(AzureChatConstants.ATTR_NAME) != null) {
					String[] userNameTokens = claimMap.get(
							AzureChatConstants.ATTR_NAME).split("\\s");
					if (userNameTokens != null) {
						if (userNameTokens.length >= 2) {
							userBean.setFirstName(userNameTokens[0]);
							userBean.setLastName(userNameTokens[1]);
						} else {
							userBean.setFirstName(userNameTokens[0]);
						}
					}

				}

			}
			LOGGER.debug("Claim Map : " + claimMap.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occured while parsing saml token : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured while parsing saml token : "
							+ e.getMessage());
		}

		try {
			userEntities = userDao.getUserDetailsByNameIdAndIdentityProvider(
					userBean.getNameID(), userBean.getIdProvider());
			if (userEntities.size() > 0) {
				UserEntity user = userEntities.get(0);
				userBean.setUserID(String.valueOf(user.getUserID()));
				userBean.setNewUser(false);
				// Set Necessary Properties in the Bean
				userBean.setPendingFriendReq(friendRequestDAOImpl
						.getPendingFriendRequestsCountForUser(userBean
								.getUserID()));
				userBean.setFirstName(user.getFirstName());
				userBean.setLastName(user.getLastName());
				userBean.setEmail(user.getEmailAddress());
				userBean.setCountryCD(String.valueOf(user.getPhoneCountryCode()));
				userBean.setPhoneNo(String.valueOf(user.getPhoneNumber()));
				userBean.setPhotoUrl(user.getPhotoBlobUrl()+"?"+profileImageRequestDAO.getSignatureForPrivateAccess());
				//Fetch the content for this user
				userBean=contentShareService.getUserContent(userBean);
				
				LOGGER.debug("User Details : " + userBean.toString());

			} else {
				userBean.setNewUser(true);
				userBean.setUsrPrefList(getUserPreferences());
			}
		} catch(AzureChatBusinessException e){
			LOGGER.error("Exception occured while fetching user details from Azure Storage and SQL."
					+ e.getMessage());
			throw new AzureChatBusinessException(e.getMessage());
		}catch(AzureChatSystemException e){
			LOGGER.error("Exception occured while fetching user details from Azure Storage and SQL ."
					+ e.getMessage());
			throw new AzureChatSystemException(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("Exception occured while fetching user details from Azure SQL DB."
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured while fetching user details from AZURE SQL DB."
							+ e.getMessage());
		}

		LOGGER.info("[LoginService][processLoginForm]          end");
		return userBean;
	}

	/**
	 * Used to get all user preferences to be selected on register page.
	 * 
	 * @return
	 * @throws AzureChatSystemException 
	 */
	public List<UserPrefBean> getUserPreferences() throws AzureChatSystemException {
		List<UserPrefBean> userList = new ArrayList<UserPrefBean>();
		try {
			List<PreferenceMetadataEntity> preferenceMetadataEntities = preferenceMetadataDAO.
																			getPreferenceMetadataEntities();
			for(PreferenceMetadataEntity preferenceMetadataEntity : preferenceMetadataEntities){
				userList.add(buildUserPrefBean(preferenceMetadataEntity));
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching user preference metadata from AZURE SQL DB."
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured while fetching user details from AZURE SQL DB."
							+ e.getMessage());
		}
		return userList;
	}
	
	/**
	 * Used to generate UserPrefBean object from PreferenceMetadataEntity object.
	 * 
	 * @param preferenceMetadataEntity
	 * @return
	 */
	private UserPrefBean buildUserPrefBean(PreferenceMetadataEntity preferenceMetadataEntity){
		UserPrefBean userPrefBean = new UserPrefBean();
		userPrefBean.setPrefDesc(preferenceMetadataEntity.getPreferenceDesc());
		return userPrefBean;
	}

}
