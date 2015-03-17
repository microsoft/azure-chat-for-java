/*
Copyright (c) Microsoft Open Technologies, Inc.  All rights reserved.
 
The MIT License (MIT)
 
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package com.microsoftopentechnologies.azchat.web.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStartupUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.FriendRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.PreferenceMetadataDAO;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserPreferenceDAO;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.FriendRequestBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;

/**
 * Service class provides the friend request management functionality.It
 * supports the search friend,update friend status and add friend functionality
 * by calling corresponding DAO class methods.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Service
@Qualifier("friendRequestService")
public class FriendRequestService extends BaseServiceImpl {

	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestService.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FriendRequestDAO friendRequestDAO;

	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;

	@Autowired
	private PreferenceMetadataDAO preferenceMetadataDAO;

	@Autowired
	private UserPreferenceDAO userPreferenceDAO;

	@Autowired
	private QueueRequestDAO queueRequestDAO;

	@Autowired
	private AzureChatStartupUtils azureChatStartupUtils;

	/**
	 * BeanPostProcessor initialization code to create and get reference to the
	 * AZURE TABLE from azure storage.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("[FriendRequestService][init] start");
		String excpMsg = null;
		try {
			LOGGER.debug("Creating Friend Request Management tables.");
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_FRINED_REQ);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_FRIEND_REQ);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_USER_MESSAGE);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating azure storage tables for the friend request management. Exception Message : "
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}

		try {
			LOGGER.debug("Creating user profile management tables.");
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_USER_SQL_TABLES);
			userDAO.createUserTable();
			preferenceMetadataDAO.createPreferenceMatedateTable();
			userPreferenceDAO.createUserPreferenceTable();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating SQL tables for user profile management. Exception Message :"
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}

		try {
			LOGGER.debug(" Preparing preference mata data.");
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_USER_PREF);
			List<PreferenceMetadataEntity> list = preferenceMetadataDAO
					.getPreferenceMetadataEntities();
			if (AzureChatUtils.isEmpty(list)) {
				List<String> listOfPreferences = AzureChatUtils
						.getPreferences();
				for (String preference : listOfPreferences) {
					PreferenceMetadataEntity preferenceMetadataEntity = buildPreferenceMetadata(preference);
					preferenceMetadataDAO
							.addPreferenceMetadataEntity(preferenceMetadataEntity);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while preparing user preference meta data in azure SQL. Exception Message : "
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));

		}

		try {
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_EMAIL_NOTIFICATION_QUEUE);
			LOGGER.debug(" Creating email notification queue.");
			AzureChatStorageUtils
					.createQueue(AzureChatConstants.QUEUE_NAME_EMAIL_NOTIFICATION);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating email notification queue . Exception Message :"
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}
		LOGGER.info("[FriendRequestService][init] end");
	}

	/**
	 * Execute Service implementation for the Friend Request service.The switch
	 * takes action parameter coming from controller which is instance of
	 * ServiceActionEnum.The actions in this method are mapped conventionally by
	 * ServiceActionEnum to execute the operation and modify the model bean.
	 */
	@Override
	public BaseBean executeService(BaseBean baseBean) throws AzureChatException {
		LOGGER.info("[FriendRequestService][executeService] start");
		BaseBean baseBeanObj = null;
		switch (baseBean.getServiceAction()) {
		case FIND_FRIENDS:
			baseBeanObj = searchFriends((UserBean) baseBean);
			break;
		case FRIEND_STATUS:
			baseBeanObj = getFriendStatus((FriendRequestBean) baseBean);
			break;
		case ADD_FRIEND:
			baseBeanObj = addFriend((FriendRequestBean) baseBean);
			break;
		case GET_PENDING_FRIENDS:
			baseBeanObj = getPendingFriends((UserBean) baseBean);
			break;
		case UPDATE_FRIENDREQ_STATUS:
			baseBeanObj = updateFriendReqStatus((FriendRequestBean) baseBean);
			break;
		case GET_PENDING_FRIEND_COUNT:
			baseBeanObj = getPendingFriendRequestCount((UserBean) baseBean);
			break;
		default:
			break;
		}
		LOGGER.info("[FriendRequestService][executeService] end");
		return baseBeanObj;
	}

	/**
	 * This method fetch the pending friend list for input user by calling the
	 * friendRequestDAO.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private BaseBean getPendingFriends(UserBean userBean)
			throws AzureChatBusinessException {
		LOGGER.info("[FriendRequestService][getPendingFriends] start");
		try {
			if (AzureChatUtils.isEmptyOrNull(userBean.getUserID())) {
				LOGGER.error("User ID is null.Can not fetch Pending friend requests.");
				throw new AzureChatBusinessException(
						"User ID is null.Can not fetch Pending Friend Request");
			}
			List<FriendRequestEntity> pendingFriendList = friendRequestDAO
					.getPendingFriendRequestsForUser(userBean.getUserID());
			populateFriendBeanList(userBean, pendingFriendList);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching pending request for user."
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while fetching pending request for user."
							+ e.getMessage());
		}
		LOGGER.info("[FriendRequestService][getPendingFriends] end");
		return userBean;
	}

	/**
	 * This method populates the FriendRequestBean list from FriendRequestEntity
	 * list.
	 * 
	 * @param userBean
	 * @param pendingFriendList
	 */
	private void populateFriendBeanList(UserBean userBean,
			List<FriendRequestEntity> pendingFriendList) {
		List<FriendRequestBean> pendingfriendBeanList = new ArrayList<FriendRequestBean>();
		if (!AzureChatUtils.isEmpty(pendingFriendList)) {
			for (FriendRequestEntity entity : pendingFriendList) {
				FriendRequestBean friendBean = new FriendRequestBean();
				// Fill Up Bean
				friendBean.setFriendID(entity.getFriendID());
				friendBean.setFriendName(entity.getFriendName());
				friendBean.setFriendPhotoUrl(entity.getFriendProfileBlobURL());
				friendBean.setStatus(entity.getStatus());
				friendBean.setUserID(entity.getUserID());
				pendingfriendBeanList.add(friendBean);
			}
			userBean.setFriendList(pendingfriendBeanList);
			userBean.setPendingFriendReq(pendingfriendBeanList.size());
		}

	}

	/**
	 * This method calls add friend storage service and establish the friend
	 * relationship between logged in user and selected user.
	 * 
	 * @param friendRequestBean
	 * @return
	 * @throws Exception
	 */
	private BaseBean addFriend(FriendRequestBean friendRequestBean)
			throws AzureChatException {
		LOGGER.info("[FriendRequestService][addFriend] start");
		try {
			friendRequestDAO.addFriendRequest(friendRequestBean.getUserID(),
					friendRequestBean.getFriendID(),
					friendRequestBean.getFriendName(),
					friendRequestBean.getFriendPhotoUrl(),
					AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL);

			String jsonString = convertToJSONString(friendRequestBean);
			queueRequestDAO.postFriendRequestNotification(jsonString, 300);
			LOGGER.debug("Friend Added Successfully.");
		} catch (Exception e) {
			LOGGER.error("Exception occurred while adding friend in insertOrReplaceEntity storage method : "
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while adding friend in insertOrReplaceEntity storage method. : "
							+ e.getMessage());
		}
		LOGGER.info("[FriendRequestService][addFriend] end");
		return friendRequestBean;
	}

	/**
	 * This service method search the friend for input first name by calling
	 * friend request DAO.
	 * 
	 * @param userBean
	 * @return
	 * @throws AzureChatException
	 */
	private UserBean searchFriends(UserBean userBean) throws AzureChatException {
		LOGGER.info("[FriendRequestService][searchFriends] start");
		userBean.setFriendList(searchFriends(userBean.getFirstName()));
		LOGGER.info("[FriendRequestService][searchFriends] end");
		return userBean;
	}

	/**
	 * This method search friends by input first name by calling friend request
	 * DAO.
	 * 
	 * @param firstName
	 * @return
	 * @throws AzureChatException
	 */
	private List<FriendRequestBean> searchFriends(String firstName)
			throws AzureChatException {
		LOGGER.info("[FriendRequestService][searchFriends] start");
		LOGGER.debug("Input : First Name : " + firstName);
		List<FriendRequestBean> frndList = new ArrayList<FriendRequestBean>();
		List<UserEntity> userEntities = null;
		try {
			userEntities = userDAO
					.getUserDetailsByFirstNameOrLastName(firstName);
			if (null != userEntities) {
				for (UserEntity userEntity : userEntities) {
					FriendRequestBean friendRequestBean = new FriendRequestBean();
					friendRequestBean = buildFriendRequestBean(userEntity,
							friendRequestBean);
					frndList.add(friendRequestBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing azure sql query for search friend. : "
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while executing azure sql query for search friend. : "
							+ e.getMessage());
		}
		LOGGER.info("[FriendRequestService][searchFriens] end");
		return frndList;
	}

	/**
	 * This method fetch the friend request status by calling friend request
	 * DAO.
	 * 
	 * @param firstName
	 * @return
	 * @throws AzureChatException
	 */
	private FriendRequestBean getFriendStatus(FriendRequestBean friendRequestBean)
			throws AzureChatException {
		LOGGER.info("[FriendRequestService][getFriendStatus] start");
		LOGGER.debug("Input : logInUserID : " + friendRequestBean.getUserID()
				+ " : " + friendRequestBean.getFriendID());
		try {
			FriendRequestEntity friendRequestEntity = friendRequestDAO
					.getFriendStatusForUserWithFriend(
							friendRequestBean.getUserID(),
							friendRequestBean.getFriendID());
			if (friendRequestEntity != null) {
				friendRequestBean.setUserID(friendRequestEntity.getUserID());
				friendRequestBean.setStatus(friendRequestEntity.getStatus());
				friendRequestBean
						.setFriendPhotoUrl(friendRequestEntity
								.getFriendProfileBlobURL()
								+ AzureChatConstants.CONSTANT_QUESTION_MARK
								+ AzureChatUtils
										.getSASUrl(AzureChatConstants.PROFILE_IMAGE_CONTAINER));
				friendRequestBean.setFriendName(friendRequestEntity
						.getFriendName());
				friendRequestBean
						.setFriendID(friendRequestEntity.getFriendID());
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in Azure storage while executing  get friend status query."
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred in Azure storage while executing  get friend status query. "
							+ e.getMessage());
		}
		LOGGER.info("[FriendRequestService][getFriendInfo] end");
		return friendRequestBean;
	}

	/**
	 * This method is used to populate the friend request bean values from
	 * userEntity.
	 * 
	 * @param userEntity
	 * @param friendRequestBean
	 * @return
	 */
	private FriendRequestBean buildFriendRequestBean(UserEntity userEntity,
			FriendRequestBean friendRequestBean) {
		if (null != userEntity.getUserID()) {
			friendRequestBean
					.setFriendID(String.valueOf(userEntity.getUserID()));
		}
		friendRequestBean.setFriendPhotoUrl(userEntity.getPhotoBlobUrl());
		friendRequestBean.setFriendName(userEntity.getFirstName()
				+ AzureChatConstants.CONSTANT_SPACE + userEntity.getLastName());
		return friendRequestBean;
	}

	/**
	 * This method update the friend request status by calling friend request
	 * DAO.
	 * 
	 * @param firstName
	 * @return
	 * @throws AzureChatException
	 */
	private FriendRequestBean updateFriendReqStatus(
			FriendRequestBean friendRequestBean) throws AzureChatException {
		LOGGER.info("[FriendRequestService][updateFriendReqStatus] start");
		try {
			if (null != friendRequestBean
					&& !AzureChatUtils.isEmptyOrNull(friendRequestBean
							.getStatus()))
				LOGGER.debug("Input FriendRequestBean : "
						+ friendRequestBean.toString());
			if (AzureChatConstants.FRIEND_REQUEST_APPROVE
					.equalsIgnoreCase(trim(friendRequestBean.getStatus()))) {
				friendRequestDAO.acceptFriendRequest(
						friendRequestBean.getUserID(),
						friendRequestBean.getFriendID(),
						friendRequestBean.getFriendName(),
						friendRequestBean.getFriendPhotoUrl());
				friendRequestBean
						.setMsg(AzureChatConstants.SUCCESS_MSG_APPROVE_FRND_REQ);
			} else if (AzureChatConstants.FRIEND_REQUEST_REJECT
					.equalsIgnoreCase(trim(friendRequestBean.getStatus()))) {
				friendRequestDAO.rejectFriendRequest(
						friendRequestBean.getUserID(),
						friendRequestBean.getFriendID(),
						friendRequestBean.getFriendID(),
						friendRequestBean.getFriendPhotoUrl());
				friendRequestBean
						.setMsg(AzureChatConstants.FAILURE_MSG_REJECT_FRND_REQ);
			}
			int pendFrndCnt = friendRequestDAO
					.getPendingFriendRequestsCountForUser(friendRequestBean
							.getUserID());
			LOGGER.debug("Pending Friend Updated Count : " + pendFrndCnt);
			friendRequestBean.setPendFrndReqCnt(pendFrndCnt);

		} catch (Exception e) {
			LOGGER.error("Exception while  updating "
					+ friendRequestBean.getStatus()
					+ " status for friend name "
					+ friendRequestBean.getFriendName() + " " + e.getMessage());
			throw new AzureChatBusinessException("Exception while  updating "
					+ friendRequestBean.getStatus()
					+ " status for friend name "
					+ friendRequestBean.getFriendName() + " " + e.getMessage());
		}
		LOGGER.info("[FriendRequestService][getFriendInfo] end");
		return friendRequestBean;
	}

	/**
	 * This method convert the FriendRequestEntity to the corresponding JSON
	 * representation.
	 * 
	 * @param entity
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private String convertToJSONString(FriendRequestBean entity)
			throws AzureChatBusinessException {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(AzureChatConstants.FRND_REQ_JSON_KEY_USERID,
					entity.getUserID());
			jsonObject.put(AzureChatConstants.FRND_REQ_JSON_KEY_FRNDID,
					entity.getFriendID());
			jsonObject.put(AzureChatConstants.FRND_REQ_JSON_KEY_FRNDNM,
					entity.getFriendName());
			jsonObject.put(AzureChatConstants.FRND_REQ_JSON_KEY_STATUS,
					entity.getStatus());
			jsonObject.put(AzureChatConstants.FRND_REQ_JSON_KEY_FRNDURL,
					entity.getFriendPhotoUrl());
		} catch (JSONException e) {
			LOGGER.error("Exception while converting to JSONObject. "
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception while converting to JSONObject. "
							+ e.getMessage());
		}
		return jsonObject.toString();
	}

	/**
	 * This method fetch the pending friend list for the user and calculate the
	 * count.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private BaseBean getPendingFriendRequestCount(UserBean userBean)
			throws AzureChatBusinessException {
		LOGGER.info("[FriendRequestService][getPendingFriendRequestCount] start");
		try {
			if (AzureChatUtils.isEmptyOrNull(userBean.getUserID())) {
				LOGGER.error("User ID is null.Can not fetch Pending friend requests count.");
				throw new AzureChatBusinessException(
						"User ID is null.Can not fetch Pending Friend Request count.");
			}
			List<FriendRequestEntity> pendingFriendList = friendRequestDAO
					.getPendingFriendRequestsForUser(userBean.getUserID());
			if (null != pendingFriendList) {
				userBean.setPendingFriendReq(pendingFriendList.size());
				LOGGER.debug("Pending Friend Count Set  : "
						+ pendingFriendList.size());
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching pending request count for user."
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while fetching pending request count for user."
							+ e.getMessage());
		}
		LOGGER.info("[FriendRequestService][getPendingFriendRequestCount] end");
		return userBean;
	}

	/**
	 * This method is used to create preference meta-data object .
	 * 
	 * @param preference
	 * @return
	 */
	private PreferenceMetadataEntity buildPreferenceMetadata(String preference) {
		PreferenceMetadataEntity entity = new PreferenceMetadataEntity();
		Date date = new Date();
		entity.setCreatedBy(date);
		entity.setDateCreated(date);
		entity.setDateModified(date);
		entity.setModifiedBy(date);
		entity.setPreferenceDesc(preference);
		return entity;
	}

}
