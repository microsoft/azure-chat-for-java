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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStartupUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.MessageCommentsDAO;
import com.microsoftopentechnologies.azchat.web.dao.MessageLikeEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageComments;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageLikesEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.MessageCommentBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserMessageBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserMessageListBean;
import com.microsoftopentechnologies.azchat.web.servicesbus.AzureChatServiceBus;

/**
 * This service class serves the user content share requests.It calls the
 * corresponding DAO methods to upload text,photo and videos and also to
 * retrieves the user contents.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Service
@Qualifier("contentShareService")
public class ContentShareService extends BaseServiceImpl {
	private static final Logger LOGGER = LogManager
			.getLogger(ContentShareService.class);
	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;

	@Autowired
	private UserMessageEntityDAO userMessageEntityDAO;

	@Autowired
	private MessageCommentsDAO messageCommentsDAO;

	@Autowired
	private MessageLikeEntityDAO messageLikeEntityDAO;

	@Autowired
	private UserDAO userDao;

	@Autowired
	private QueueRequestDAO queueRequestDAO;

	@Autowired
	private AzureChatStartupUtils azureChatStartupUtils;

	/**
	 * BeanPostProcessor initialization code to create and get reference to the
	 * MessageCommemnt,MessageLike and UserMessage table from azure storage.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("[FriendRequestService][init] start");
		LOGGER.debug("Creating Friend Request related tables and queues.");
		String excpMsg = null;
		try {
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_CONTENT_SHARE);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES);
			AzureChatStorageUtils
					.createTable(AzureChatConstants.TABLE_NAME_USER_MESSAGE);
			AzureChatStorageUtils
					.createQueue(AzureChatConstants.QUEUE_NAME_MESSAGE);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating azure storage tables and message expiry queue for the content share services. Exception Message : "
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg+e.getMessage()));
		}
		LOGGER.info("[FriendRequestService][init] end");
	}

	/**
	 * Execute Service implementation for the Content Share service.The switch
	 * takes action parameter coming from controller which is instance of
	 * ServiceActionEnum.The actions in this method are mapped conventionally by
	 * ServiceActionEnum to execute the operation and modify the model bean.
	 */
	@Override
	public BaseBean executeService(BaseBean baseBean) throws AzureChatException {
		LOGGER.info("[ContentShareService][executeService] start");
		BaseBean baseBeanObj = null;
		switch (baseBean.getServiceAction()) {
		case CONTENT_SHARE:
			baseBeanObj = shareContent((UserBean) baseBean);
			break;
		case GET_USER_CONTENT:
			baseBeanObj = getUserContent((UserBean) baseBean);
			break;
		case UPDATE_MSG_COMMENT:
			baseBeanObj = updateMessageComment((MessageCommentBean) baseBean);
			break;
		case UPDATE_USER_LIKE_STATUS:
			baseBeanObj = updateUserLikeStatus((UserMessageBean) baseBean);
			break;
		case GET_MSG_COMMENTS:
			baseBeanObj = getUserMsgComments((UserMessageBean) baseBean);
			break;
		default:
			break;
		}
		LOGGER.info("[ContentShareService][executeService] end");
		return baseBeanObj;
	}

	/**
	 * This method fetch the user message comment for the single message.
	 * 
	 * @param usreMessageBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private UserMessageBean getUserMsgComments(UserMessageBean userMessageBean)
			throws AzureChatBusinessException {
		LOGGER.info("[ContentShareService][getUserMsgComments] start");
		if (null != userMessageBean.getMsgID()) {
			try {
				List<MessageComments> messageComments = messageCommentsDAO
						.getMessageComments(userMessageBean.getMsgID());
				List<MessageCommentBean> msgCommentList = new ArrayList<MessageCommentBean>();
				if (!AzureChatUtils.isEmpty(messageComments)) {
					userMessageBean.setCommentCount(messageComments.size());
					for (MessageComments comment : messageComments) {
						MessageCommentBean commentBean = new MessageCommentBean();
						commentBean.setComment(comment.getComments());
						commentBean.setFriendID(comment.getFriendID());
						commentBean.setMsgID(comment.getMessageID());
						commentBean.setFriendName(comment.getFriendName());
						commentBean.setPhotoUrl(comment
								.getFriendProfileBlobURL());
						msgCommentList.add(commentBean);
					}
					userMessageBean.setMsgCommentList(msgCommentList);
				}
			} catch (Exception e) {
				LOGGER.error("Exception occurred while fetching user message comments for msg id : "
						+ userMessageBean.getMsgID()
						+ " Exception Message : "
						+ e.getMessage());
				throw new AzureChatBusinessException(
						"Exception occurred while fetching user mesage comments for msg id : "
								+ userMessageBean.getMsgID()
								+ " Exception Message : " + e.getMessage());
			}

		} else {
			LOGGER.error("Can not fetch the user message comment operation.Message ID is null.");
			throw new AzureChatBusinessException(
					"Can not fetch the user message comment operation.Message ID is null.");
		}
		LOGGER.info("[ContentShareService][getUserMsgComments] end");
		return userMessageBean;
	}

	/**
	 * This service method add and removes the user like depending on the user
	 * operation on the UI.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private UserMessageBean updateUserLikeStatus(UserMessageBean userMessageBean)
			throws AzureChatBusinessException {
		LOGGER.info("[ContentShareService][updateUserLikeStatus] start");
		MessageLikesEntity msgLikeEntity = new MessageLikesEntity(
				userMessageBean.getMsgID(), userMessageBean.getOwnerID());
		msgLikeEntity.setFriendName(userMessageBean.getOwnerName());
		msgLikeEntity.setFriendProfileBlobURL(userMessageBean.getPhotoUrl());
		try {
			if (userMessageBean.getIsLike()) {
				LOGGER.debug("Adding the user like ");
				messageLikeEntityDAO.addMessageLikesEntity(msgLikeEntity);
			} else {
				LOGGER.debug("Removing the user like ");
				messageLikeEntityDAO.deleteMessageLikeEntity(msgLikeEntity);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while performing user like/unlike operation for user id "
					+ userMessageBean.getOwnerID()
					+ " and Mesage ID :"
					+ userMessageBean.getMsgID()
					+ " Exception Message :"
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while performing user like/unlike operation for user "
							+ userMessageBean.getOwnerName()
							+ " Exception Message :" + e.getMessage());
		}

		LOGGER.info("[ContentShareService][updateUserLikeStatus] end");
		return userMessageBean;
	}

	/**
	 * This method take user message comment related information and calls the
	 * messageCommentDAO to store the message comment in the Azure SQL database.
	 * 
	 * @param messageCommentBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	private MessageCommentBean updateMessageComment(
			MessageCommentBean messageCommentBean)
			throws AzureChatBusinessException {
		LOGGER.info("[ContentShareService][updateMessageComment] start");

		if (!AzureChatUtils.isEmptyOrNull(messageCommentBean.getMsgID())) {
			LOGGER.debug("Message ID : " + messageCommentBean.getMsgID()
					+ " User ID" + messageCommentBean.getFriendID());
			MessageComments commentEntity = new MessageComments(
					messageCommentBean.getMsgID(), AzureChatUtils.getGUID());
			commentEntity.setComments(messageCommentBean.getComment());
			commentEntity.setFriendID(messageCommentBean.getFriendID());
			commentEntity.setFriendName(messageCommentBean.getFriendName());
			commentEntity.setFriendProfileBlobURL(messageCommentBean
					.getPhotoUrl());
			commentEntity.setTimestamp(new Date());
			try {
				messageCommentsDAO.addMessageComments(commentEntity);
			} catch (Exception e) {
				LOGGER.error("Exception occurred while adding comments for message id : "
						+ commentEntity.getMessageID()
						+ " for user :"
						+ commentEntity.getFriendName()
						+ " Exception Message :" + e.getMessage());
				throw new AzureChatBusinessException(
						"Exception occurred while adding comments for message id : "
								+ commentEntity.getMessageID() + " for user :"
								+ commentEntity.getFriendName()
								+ " Exception Message :" + e.getMessage());
			}

		} else {
			LOGGER.error("Can not update user message comment.Message id is null.");
			throw new AzureChatBusinessException(
					"Can not update user message comment.Message id is null");
		}

		LOGGER.info("[ContentShareService][updateMessageComment] end");
		return messageCommentBean;
	}

	/**
	 * This method retrieve the user content i.e. messages and phoho's/video's
	 * shared by user and his direct friends.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	public UserBean getUserContent(UserBean userBean)
			throws AzureChatBusinessException {
		LOGGER.info("[ContentShareService][getUserContent] start");
		try {
			List<UserMessageBean> userMessageBeans = new ArrayList<UserMessageBean>();
			List<UserMessageEntity> userMessageEntities = null;
			if (AzureChatConstants.GET_USER_ONLY_CONTENTS
					.equalsIgnoreCase(userBean.getContentLevel())) {
				userMessageEntities = userMessageEntityDAO
						.getUserMessageEntities(userBean.getUserID());
			} else {
				userMessageEntities = userMessageEntityDAO
						.getUserAndFriendsMessages(userBean.getUserID());
			}
			if (null != userMessageEntities) {
				for (UserMessageEntity userMessageEntity : userMessageEntities) {
					userMessageBeans
							.add(populateUserBeanWithUserMessageContent(
									userBean, userMessageEntity));
				}

				UserMessageListBean userMessageListBean = new UserMessageListBean();
				userMessageListBean.setUserMsgList(userMessageBeans);
				userBean.setUserMessageListBean(userMessageListBean);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching user content for user : "
					+ userBean.getFirstName()
					+ " Exception message : "
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while fetching user content for user : "
							+ userBean.getFirstName() + " Exception message : "
							+ e.getMessage());
		}
		LOGGER.info("[ContentShareService][getUserContent] end");
		return userBean;
	}

	/**
	 * This method get input user text messages,photo's and video's.Update the
	 * same using respective DAO object.
	 * 
	 * @param userBean
	 * @return
	 * @throws AzureChatException
	 */
	private BaseBean shareContent(UserBean userBean) throws AzureChatException {
		LOGGER.info("[ContentShareService][shareContent] start");
		UserMessageEntity userMessageEntity = buildUserMessageEntity(userBean);
		String expiryTime = userBean.getUserMessageListBean().getExpiryTime();
		try {
			if (null != userMessageEntity.getMediaType()
					&& userMessageEntity.getMediaType().contains(
							AzureChatConstants.UI_MEDIA_TYPE_VIDEO)) {
				BrokeredMessage message = new BrokeredMessage(
						AzureChatConstants.CONSTANT_EMPTY_STRING);
				message.setProperty("msg", userMessageEntity.getTextContent());
				message.setProperty("url", (userMessageEntity.getMediaURL()
						+ "?" + AzureChatUtils
						.getSASUrl(AzureChatConstants.TEMP_UPLOAD_CONTAINER)));
				message.setProperty("uname", userMessageEntity.getUserName());
				message.setProperty("photoblob",
						userMessageEntity.getUserPhotoBlobURL());
				message.setProperty("exp",
						AzureChatUtils.covertMinToSec(expiryTime));
				message.setProperty("uid", userMessageEntity.getUserID());
				message.setProperty("mid", userMessageEntity.getMessageID());
				AzureChatServiceBus.getServicebus().sendQueueMessage(
						AzureChatConstants.SERVICE_BUS_QUEUENAME, message);
			} else {
				queueRequestDAO.postMessage(userMessageEntity.getMessageID(),
						AzureChatUtils.covertMinToSec(expiryTime));
				userMessageEntityDAO.addUserMessageEntity(userMessageEntity);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred while uploading user message content  for user id : "
					+ userBean.getUserID()
					+ " and Name : "
					+ userBean.getFirstName()
					+ " Exception Message :"
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occurred while uploading user message content. For user "
							+ userBean.getFirstName()
							+ " .Exception Message :  " + e.getMessage());
		}

		LOGGER.info("[ContentShareService][shareContent] end");
		return userBean;
	}

	/**
	 * This method populates the user message related details into the user.
	 * message bean.
	 * 
	 * @param userMessageEntity
	 * @return
	 * @throws Exception
	 */
	private UserMessageBean populateUserBeanWithUserMessageContent(
			UserBean userBean, UserMessageEntity userMessageEntity)
			throws Exception {
		LOGGER.info("[ContentShareService][populateUserBeanWithUserMessageContent] start");
		UserMessageBean userMessageBean = new UserMessageBean();

		userMessageBean.setMediaType(getUIMediaType(userMessageEntity
				.getMediaType()));
		LOGGER.debug("Set media type : " + userMessageBean.getMediaType());
		userMessageBean.setMediaUrl(userMessageEntity.getMediaURL()
				+ "?"
				+ AzureChatUtils
						.getSASUrl(AzureChatConstants.PHOTO_UPLOAD_CONTAINER));
		LOGGER.debug("Media URL : " + userMessageBean.getMediaUrl());
		userMessageBean.setMsgID(userMessageEntity.getMessageID());

		userMessageBean.setMsgText(userMessageEntity.getTextContent());
		userMessageBean.setOwnerID(userMessageEntity.getUserID());
		userMessageBean.setOwnerName(userMessageEntity.getUserName());
		userMessageBean.setPhotoUrl(userMessageEntity.getUserPhotoBlobURL()
				+ "?"
				+ AzureChatUtils
						.getSASUrl(AzureChatConstants.PROFILE_IMAGE_CONTAINER));
		LOGGER.debug("Message ID : " + userMessageBean.getMsgID()
				+ " Message User ID : " + userMessageBean.getOwnerID());
		// Fetch the message comment and set the list
		List<MessageComments> comments = messageCommentsDAO
				.getMessageComments(userMessageEntity.getMessageID());
		List<MessageCommentBean> msgCommentList = new ArrayList<MessageCommentBean>();
		if (!AzureChatUtils.isEmpty(comments)) {
			userMessageBean.setCommentCount(comments.size());
			for (MessageComments comment : comments) {
				MessageCommentBean commentBean = new MessageCommentBean();
				commentBean.setComment(comment.getComments());
				commentBean.setFriendID(comment.getFriendID());
				commentBean.setMsgID(comment.getMessageID());
				commentBean.setFriendName(comment.getFriendName());
				commentBean.setPhotoUrl(comment.getFriendProfileBlobURL());
				msgCommentList.add(commentBean);
			}
			userMessageBean.setMsgCommentList(msgCommentList);
		}
		// Fetch the message like and set the like count.
		Map<String, MessageLikesEntity> msgLikeMap = messageLikeEntityDAO
				.getMessageLikeEntity(userMessageEntity.getMessageID());
		if (!AzureChatUtils.isEmpty(msgLikeMap)) {
			MessageLikesEntity msgLikeEntity = msgLikeMap.get(userBean
					.getUserID() + userMessageBean.getMsgID());
			if (null != msgLikeEntity) {
				userMessageBean.setIsLike(true);
			}
			userMessageBean.setLikeCount(msgLikeMap.size());
		}
		LOGGER.info("[ContentShareService][populateUserBeanWithUserMessageContent] end");
		return userMessageBean;
	}

	/**
	 * This method parse the media type coming from the storage and divide them
	 * into the image and video category.
	 * 
	 * @param mediaType
	 * @return
	 */
	private String getUIMediaType(String mediaType) {
		if (null != mediaType) {
			if (mediaType.contains(AzureChatConstants.UI_MEDIA_TYPE_IMAGE)) {
				return AzureChatConstants.UI_MEDIA_TYPE_IMAGE;
			} else if (mediaType
					.contains(AzureChatConstants.UI_MEDIA_TYPE_VIDEO)) {
				return AzureChatConstants.UI_MEDIA_TYPE_VIDEO;
			}
		}
		return AzureChatConstants.CONSTANT_EMPTY_STRING;

	}

	/**
	 * This method generate the UserMessageEntity object from given UserBean
	 * object.
	 * 
	 * @param userBean
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws AzureChatBusinessException
	 */
	private UserMessageEntity buildUserMessageEntity(UserBean userBean)
			throws NumberFormatException, AzureChatBusinessException {
		LOGGER.info("[ContentShareService][buildUserMessageEntity] start");
		UserEntity userEntity;
		try {
			userEntity = userDao.getUserDetailsByUserId(Integer
					.parseInt(userBean.getUserID()));
		} catch (Exception e1) {
			LOGGER.error("Exception ocured in postng user message.Unable to fetch user details for user id : "
					+ userBean.getUserID()
					+ " and Name : "
					+ userBean.getFirstName());
			throw new AzureChatBusinessException(
					"Exception ocured in postng user message.Unable to fetch user details for user id : "
							+ userBean.getUserID()
							+ " and Name : "
							+ userBean.getFirstName() + " " + e1.getMessage());
		}
		UserMessageListBean userMessageBean = userBean.getUserMessageListBean();
		if (userMessageBean != null) {
			LOGGER.debug("Message ID : " + userMessageBean.getMsgID());
			UserMessageEntity userMessageEntity = new UserMessageEntity(
					userBean.getUserID(), AzureChatUtils.getGUID());
			userMessageEntity.setMediaType(userMessageBean.getMediaType());
			String uri = null;
			try {
				if (null != userMessageBean.getPhotoVideoFile()
						&& userMessageBean.getPhotoVideoFile().getSize() > 0) {
					if (null != userMessageBean.getMediaType()
							&& userMessageBean.getMediaType().contains(
									AzureChatConstants.UI_MEDIA_TYPE_IMAGE)) {
						uri = profileImageRequestDAO.savePhoto(userMessageBean
								.getPhotoVideoFile(), (Calendar.getInstance()
								.getTimeInMillis() + userMessageBean
								.getPhotoVideoFile().getOriginalFilename()));
						userBean.getUserMessageListBean()
								.setMediaUrl(
										uri
												+ "?"
												+ AzureChatUtils
														.getSASUrl(AzureChatConstants.PHOTO_UPLOAD_CONTAINER));
						LOGGER.debug("Photo URI : " + uri);
					} else if (null != userMessageBean.getMediaType()
							&& userMessageBean.getMediaType().contains(
									AzureChatConstants.UI_MEDIA_TYPE_VIDEO)) {
						uri = profileImageRequestDAO
								.saveTempVideo(
										userMessageBean.getPhotoVideoFile(),
										(Calendar.getInstance()
												.getTimeInMillis() + userMessageBean
												.getPhotoVideoFile()
												.getOriginalFilename()));
						LOGGER.debug("Raw Video URI : " + uri);
					}

				}

			} catch (Exception e) {
				LOGGER.error("Exception occured while uploading user mesage image in blob storage.User id :"
						+ userBean.getUserID()
						+ " and Name : "
						+ userBean.getFirstName()
						+ " Exception Message :"
						+ e.getMessage());
				throw new AzureChatBusinessException(
						"Exception occurred while uploading user mesage image in blob storage for user "
								+ userBean.getFirstName()
								+ " Exception Message :" + e.getMessage());
			}
			userMessageEntity.setMediaURL(uri != null ? uri
					: AzureChatConstants.CONSTANT_EMPTY_STRING);
			userMessageEntity.setTextContent(userMessageBean.getMsgText());
			userMessageEntity.setUserName(userEntity.getFirstName() + " "
					+ userEntity.getLastName());
			userMessageEntity.setUserPhotoBlobURL(getUserPhotoBlobUrl(userBean
					.getPhotoUrl()));
			LOGGER.debug("Photo Blob Url set : "
					+ userMessageEntity.getUserPhotoBlobURL());
			LOGGER.info("[ContentShareService][buildUserMessageEntity] end");
			return userMessageEntity;
		}
		LOGGER.info("[ContentShareService][buildUserMessageEntity] end");
		return null;
	}

	/**
	 * This method parse the user photo blob url to remove the private
	 * signature.If private signature present remove and returns the url.
	 * 
	 * @param photoUrl
	 * @return
	 */
	private String getUserPhotoBlobUrl(String photoUrl) {
		if (photoUrl != null && photoUrl.length() > 0 && photoUrl.contains("?")) {
			return photoUrl.split("\\?")[0];
		}
		return photoUrl;
	}
}
