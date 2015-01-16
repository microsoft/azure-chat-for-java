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

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.dao.FriendRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.PreferenceMetadataDAO;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserPreferenceDAO;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserPreferenceEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserPrefBean;

/**
 * Registration service handle user registration tasks by providing operations
 * that take user details as a input and store into the the database.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Service
@Qualifier("registrationService")
public class RegistrationService extends BaseServiceImpl {

	private static final Logger LOGGER = LogManager
			.getLogger(RegistrationService.class);

	@Autowired
	private UserDAO userDao;

	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;

	@Autowired
	private FriendRequestDAO friendRequestDAOImpl;

	@Autowired
	private PreferenceMetadataDAO preferenceMetadataDAO;

	@Autowired
	private UserPreferenceDAO userPreferenceDAO;

	/**
	 * Execute Service implementation for the Registration service.The switch
	 * takes action parameter coming from controller which is instance of
	 * ServiceActionEnum.The actions in this method are mapped conventionally by
	 * ServiceActionEnum to execute the operation and modify the model bean.
	 */
	@Override
	public BaseBean executeService(BaseBean baseBean) throws AzureChatException {
		LOGGER.info("[RegistrationService][executeService]         start ");
		switch (baseBean.getServiceAction()) {
		case REGISTRATION:
			baseBean = doUserRegistration((UserBean) baseBean);
			break;
		case UPDATE_USER_PROFILE:
			baseBean = updateUserProfile((UserBean) baseBean);
			break;
		default:
			break;
		}
		LOGGER.info("[RegistrationService][executeService]         end ");
		return baseBean;
	}

	/**
	 * This method store the user details into the Azure SQL database.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatSystemException
	 */
	private UserBean doUserRegistration(UserBean userBean)
			throws AzureChatBusinessException, AzureChatSystemException {
		LOGGER.info("[RegistrationService][doUserRegistration]         start ");
		LOGGER.debug("User Details    " + userBean.toString());
		if (isNewUser(userBean)) {
			UserEntity userEntity = new UserEntity();
			String uri = null;
			try {
				if (null != userBean.getMultipartFile()
						&& userBean.getMultipartFile().getSize() > 0) {
					uri = profileImageRequestDAO.saveProfileImage(
							userBean.getMultipartFile(), userBean.getNameID());
				}

			} catch (Exception e) {
				LOGGER.error("Azure blob storage exception while storing profile image for user id : "
						+ userBean.getUserID()
						+ " and Name : "
						+ userBean.getFirstName());
				throw new AzureChatBusinessException(
						"Azure storage exception while storing profile image for user id : "
								+ userBean.getUserID() + " and Name : "
								+ userBean.getFirstName() + " "
								+ e.getMessage());
			}

			try {
				userBean.setPhotoUrl(uri);
				userEntity = buildUserEntity(userBean, userEntity);
				userDao.saveNewUser(userEntity);
				userBean.setNewUser(false);
				// Set user details to carry on home page.
				populateUserBean(userEntity, userBean);
				if (userBean.getUsrPrefList() != null
						&& userBean.getUsrPrefList().size() > 0) {
					for (UserPrefBean userPrefBean : userBean.getUsrPrefList()) {
						if (userPrefBean.getIsChecked()) {
							Integer preferenceMetadataId = preferenceMetadataDAO
									.getPreferenceMetedataIdByDescription(userPrefBean
											.getPrefDesc());
							UserPreferenceEntity userPreferenceEntity = buildUserPreferenceEntity(
									preferenceMetadataId, userBean.getUserID(),
									userPrefBean.getPrefDesc());
							userPreferenceDAO
									.addUserPreferenceEntity(userPreferenceEntity);
						}
					}
				}

			} catch (Exception e) {
				LOGGER.error("Azure storage exception while storing user registration details for user id : "
						+ userBean.getUserID()
						+ " and Name : "
						+ userBean.getFirstName());
				throw new AzureChatBusinessException(
						"Azure storage exception while storing user registration details for user id : "
								+ userBean.getUserID()
								+ " and Name : "
								+ userBean.getFirstName());
			}
		}
		LOGGER.info("[RegistrationService][doUserRegistration]         end ");
		return userBean;
	}

	/**
	 * This method store the updated user details into the Azure SQL database.
	 * 
	 * @param baseBean
	 * @return
	 */
	private UserBean updateUserProfile(UserBean userBean)
			throws AzureChatBusinessException {
		LOGGER.info("[RegistrationService][updateUserProfile]         start ");
		LOGGER.debug("User Details    " + userBean.toString());
		UserEntity userEntity = new UserEntity();
		String uri = null;
		try {
			if (null != userBean.getMultipartFile()
					&& userBean.getMultipartFile().getSize() > 0) {
				uri = profileImageRequestDAO.saveProfileImage(
						userBean.getMultipartFile(), userBean.getNameID());
			}
		} catch (Exception e) {
			LOGGER.error("Azure blob storage exception while updating profile image for user id : "
					+ userBean.getUserID()
					+ " and Name : "
					+ userBean.getFirstName());
			throw new AzureChatBusinessException(
					"Azure blob storage exception while updating profile image for user id : "
							+ userBean.getUserID() + " and Name : "
							+ userBean.getFirstName() + " :  " + e.getMessage());
		}

		try {
			if (uri == null) {
				uri = userDao.getUserPhotoBlobURL(Integer.parseInt(userBean
						.getUserID()));
			}
			userBean.setPhotoUrl(uri);
			userEntity = buildUserEntity(userBean, userEntity);
			userDao.updateNewUser(userEntity);

			userBean.setPhotoUrl(uri + "?"
					+ profileImageRequestDAO.getSignatureForPrivateAccess());
		} catch (Exception e) {
			LOGGER.error("Azure storage exception while updating user registration details for user id : "
					+ userBean.getUserID()
					+ " and Name : "
					+ userBean.getFirstName());
			userBean.setMsg(AzureChatConstants.FAILURE_MSG_USR_PROF_UPDT);
			throw new AzureChatBusinessException(
					"Azure storage exception while updating user registration details for user id : "
							+ userBean.getUserID()
							+ " and Name : "
							+ userBean.getFirstName() + " : " + e.getMessage());
		}
		userBean.setMsg(AzureChatConstants.SUCCESS_MSG_USR_PROF_UPDT);
		// Set Multipart null to avoid internal server error on json parsing
		userBean.setMultipartFile(null);
		LOGGER.info("[RegistrationService][updateUserProfile]         end ");
		return userBean;
	}

	/**
	 * User to create UserEntity object from UserBean object...
	 * 
	 * @param userBean
	 * @param userEntity
	 * @return
	 */
	private UserEntity buildUserEntity(UserBean userBean, UserEntity userEntity) {
		userEntity.setCreatedBy(new Date());
		userEntity.setDateCreated(new Date());
		userEntity.setDateModified(new Date());
		userEntity.setModifiedBy(new Date());
		userEntity.setEmailAddress(userBean.getEmail() != null ? userBean
				.getEmail() : AzureChatConstants.CONSTANT_EMPTY_STRING);
		userEntity.setFirstName(userBean.getFirstName() != null ? userBean
				.getFirstName() : AzureChatConstants.CONSTANT_EMPTY_STRING);
		userEntity
				.setIdentityProvider(userBean.getIdProvider() != null ? userBean
						.getIdProvider()
						: AzureChatConstants.CONSTANT_EMPTY_STRING);
		userEntity.setLastName(userBean.getLastName() != null ? userBean
				.getLastName() : AzureChatConstants.CONSTANT_EMPTY_STRING);
		userEntity.setNameId(userBean.getNameID() != null ? userBean
				.getNameID() : AzureChatConstants.CONSTANT_EMPTY_STRING);
		userEntity
				.setPhoneCountryCode(userBean.getCountryCD() != null ? Integer
						.parseInt(userBean.getCountryCD()) : 0);
		userEntity.setPhoneNumber(userBean.getPhoneNo() != null ? Long
				.parseLong(userBean.getPhoneNo()) : 0);
		userEntity.setPhotoBlobUrl(userBean.getPhotoUrl() != null ? userBean
				.getPhotoUrl() : AzureChatConstants.CONSTANT_EMPTY_STRING);
		return userEntity;
	}

	/**
	 * This method is a pre-check to avoid duplicate user entries into the SQL
	 * table.
	 * 
	 * @param userBean
	 * @return
	 * @throws AzureChatSystemException
	 */
	private boolean isNewUser(UserBean userBean)
			throws AzureChatSystemException {
		List<UserEntity> userEntities = null;
		boolean isNewUser = true;
		try {
			userEntities = userDao.getUserDetailsByNameIdAndIdentityProvider(
					userBean.getNameID(), userBean.getIdProvider());
			if (null != userEntities && userEntities.size() > 0) {
				isNewUser = false;
				UserEntity user = userEntities.get(0);
				userBean.setNewUser(false);
				populateUserBean(user, userBean);
				return isNewUser;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching user details from AZURE SQL DB."
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured while fetching user details from AZURE SQL DB."
							+ e.getMessage());
		}
		return isNewUser;
	}

	/**
	 * This method populates the value fromo userEntity to the user bean.
	 * 
	 * @param userEntity
	 * @param userBean
	 * @throws Exception
	 */
	private void populateUserBean(UserEntity userEntity, UserBean userBean)
			throws Exception {
		userBean.setUserID(String.valueOf(userEntity.getUserID()));
		userBean.setPendingFriendReq(friendRequestDAOImpl
				.getPendingFriendRequestsCountForUser(userBean.getUserID()));
		userBean.setFirstName(userEntity.getFirstName());
		userBean.setLastName(userEntity.getLastName());
		userBean.setEmail(userEntity.getEmailAddress());
		userBean.setCountryCD(String.valueOf(userEntity.getPhoneCountryCode()));
		userBean.setPhoneNo(String.valueOf(userEntity.getPhoneNumber()));
		userBean.setPhotoUrl(userEntity.getPhotoBlobUrl() + "?"
				+ profileImageRequestDAO.getSignatureForPrivateAccess());
	}

	private UserPreferenceEntity buildUserPreferenceEntity(
			Integer preferenceMetadataId, String userId, String desc) {
		UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity();
		userPreferenceEntity.setPreferenceId(preferenceMetadataId);
		userPreferenceEntity.setUserId(Integer.parseInt(userId));
		userPreferenceEntity.setPreferenceDesc(desc);
		Date date = new Date();
		userPreferenceEntity.setCreatedBy(date);
		userPreferenceEntity.setDateCreated(date);
		userPreferenceEntity.setDateModified(date);
		userPreferenceEntity.setModifiedBy(date);
		return userPreferenceEntity;
	}

}
