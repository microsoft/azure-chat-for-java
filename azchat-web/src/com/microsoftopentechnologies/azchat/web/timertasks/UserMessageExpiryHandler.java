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

package com.microsoftopentechnologies.azchat.web.timertasks;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.MessageCommentsDAO;
import com.microsoftopentechnologies.azchat.web.dao.MessageCommentsDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.MessageLikeEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.MessageLikeEntityDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
import com.microsoftopentechnologies.azchat.web.mediaservice.AzureChatMediaService;

/**
 * This class runs as a thread to get the set of messages from user message
 * table & check whether messages message is expired & deleted or still present
 * in queue. If present then, it will keep it in table or delete from table
 * also.
 * 
 * @author Rupesh_Shirude
 *
 */
@Component
public class UserMessageExpiryHandler {

	private static final Logger LOGGER = LogManager
			.getLogger(UserMessageExpiryHandler.class);

	/**
	 * This is Spring Scheduled Fixed Task.This method will delete the messages
	 * from UserMessage table which are expired and removed from queue.
	 * 
	 * @param setOfMessageIdsInQueue
	 */
	@Scheduled(fixedRate = 180000)
	public void deleteExpiredMessages() {
		LOGGER.info("[UserMessageExpiryHandler][deleteExpiredMessages]         start ");
		LOGGER.info("THREAD UserMessageExpiryHandler start time : "
				+ AzureChatUtils.getCurrentTimestamp());
		Set<String> setOfMsgIdsInQue = null;
		Set<String> setOfMsgIdsUserMsgTbl = null;
		QueueRequestDAO queueRequestDAO = new QueueRequestDAOImpl();
		UserMessageEntityDAO userMessageEntityDAO = new UserMessageEntityDAOImpl();
		try {
			setOfMsgIdsUserMsgTbl = userMessageEntityDAO.getAllUserMessageIds();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while retrieving the user messages from the table. Exception Message :  "
					+ e.getMessage());
		}
		try {
			setOfMsgIdsInQue = queueRequestDAO
					.getAllMessagesFromQueue(AzureChatConstants.QUEUE_NAME_MESSAGE);
		} catch (Exception e1) {
			LOGGER.error("Exception occurred while retrieving the user messages from the queue. Exception Message :  "
					+ e1.getMessage());
		}
		for (String msgIdUserTbl : setOfMsgIdsUserMsgTbl) {
			if (!setOfMsgIdsInQue.contains(msgIdUserTbl)) {
				try {
					UserMessageEntity messageEntity = userMessageEntityDAO
							.getMessageById(msgIdUserTbl);
					deleteMessage(messageEntity);
				} catch (Exception e) {
					LOGGER.error("Exception occurred while deleting the message from the azure storage. Exception message : "
							+ e.getMessage());
				}
			}
		}
		LOGGER.debug("THREAD UserMessageExpiryHandler end time : "
				+ AzureChatUtils.getCurrentTimestamp());
		LOGGER.info("[UserMessageExpiryHandler][deleteExpiredMessages]         end ");
	}

	/**
	 * This method deletes the message and corresponding media,comments and text
	 * from azure storage.
	 * 
	 * @param messageEntity
	 * @throws Exception
	 */
	public void deleteMessage(UserMessageEntity messageEntity) throws Exception {
		LOGGER.info("[UserMessageExpiryHandler][deleteMessage]         start ");
		// 1 : delete associated media photo from photo table.
		if (messageEntity.getMediaURL() != null
				&& messageEntity.getMediaURL().length() > 0) {
			int startIndex = messageEntity.getMediaURL().indexOf(
					AzureChatConstants.PHOTO_UPLOAD_CONTAINER + "/")
					+ AzureChatConstants.PHOTO_UPLOAD_CONTAINER.length() + 1;
			String fileName = messageEntity.getMediaURL().substring(startIndex);
			ProfileImageRequestDAO profileImageRequestDAO = new ProfileImageRequestDAOImpl();
			profileImageRequestDAO.deletePhoto(fileName);
		}
		// 2 : delete associated comments for this message.
		MessageCommentsDAO messageCommentsDAO = new MessageCommentsDAOImpl();
		messageCommentsDAO.deleteAllMessageComments(messageEntity
				.getMessageID());
		// 3 :  delete associated likes for this message.
		MessageLikeEntityDAO messageLikeEntityDAO = new MessageLikeEntityDAOImpl();
		messageLikeEntityDAO.deleteMessageLikeByMessageId(messageEntity
				.getMessageID());
		// 4 : delete the actual message from user message table.
		CloudTable cloudTable = AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
				messageEntity.getMessageID());
		TableQuery<UserMessageEntity> userMessages = TableQuery.from(
				UserMessageEntity.class).where(rowFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE)
				.execute(userMessages)) {
			TableOperation deleteOperation = TableOperation.delete(entity);
			cloudTable.execute(deleteOperation);
		}
		// 5 :  delete video asset from media service account after expiry
		// time.
		if (null != messageEntity.getMediaType()
				&& messageEntity.getMediaType().contains(
						AzureChatConstants.UI_MEDIA_TYPE_VIDEO)) {
			AzureChatMediaService.deleteAsset(messageEntity.getAssetID());
		}
		LOGGER.info("[UserMessageExpiryHandler][deleteMessage] end");
	}
}