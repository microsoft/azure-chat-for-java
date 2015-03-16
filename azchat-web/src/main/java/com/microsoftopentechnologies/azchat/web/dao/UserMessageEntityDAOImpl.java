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
package com.microsoftopentechnologies.azchat.web.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;

/**
 * This class provides operations to add, get messages to/from azure table.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("userMessageEntityDAO")
public class UserMessageEntityDAOImpl implements UserMessageEntityDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);

	@Autowired
	private FriendRequestDAO friendRequestDAO;

	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;

	@Autowired
	private MessageCommentsDAO messageCommentsDAO;

	@Autowired
	private MessageLikeEntityDAO messageLikeEntityDAO;

	/**
	 * This method This method executes query on azure user message storage to
	 * add user message.
	 * 
	 * @param userMessageEntity
	 * @return
	 * @throws Exception
	 * @author Rupesh_Shirude
	 */
	@Override
	public UserMessageEntity addUserMessageEntity(
			UserMessageEntity userMessageEntity) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][addUserMessageEntity] start ");
		AzureChatStorageUtils.insertOrReplaceEntity(
				AzureChatConstants.TABLE_NAME_USER_MESSAGE, userMessageEntity);
		LOGGER.info("[UserMessageEntityDAOImpl][addUserMessageEntity] end ");
		return userMessageEntity;
	}

	/**
	 * This method executes query on azure user message storage to get user
	 * message by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<UserMessageEntity> getUserMessageEntities(String userId)
			throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities] start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userId);
		TableQuery<UserMessageEntity> friendListQuery = TableQuery.from(
				UserMessageEntity.class).where(partitionFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE)
				.execute(friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities] end ");
		return userMessageEntities;
	}

	/**
	 * This method executes query on azure user message storage to get user
	 * messages by user id & message id.
	 * 
	 * @param userId
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserMessageEntity getUserMessageEntities(String userId,
			String messageId) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities] start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, messageId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<UserMessageEntity> friendListQuery = TableQuery.from(
				UserMessageEntity.class).where(combinedFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE)
				.execute(friendListQuery)) {
			userMessageEntities.add(entity);
		}
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities] end ");
		return userMessageEntities.get(0);
	}

	/**
	 * This method executes query on azure user message storage to get user
	 * message by user id.
	 * 
	 * @param userId
	 * @return userMessageEntities
	 * @throws Exception
	 */
	@Override
	public List<UserMessageEntity> getUserAndFriendsMessages(String userId)
			throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserAndFriendsMessages] start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		userMessageEntities = getUserMessageEntities(userId);
		List<FriendRequestEntity> friendRequestEntities = friendRequestDAO
				.getFriendListForUser(userId);
		for (FriendRequestEntity friendRequestEntity : friendRequestEntities) {
			userMessageEntities
					.addAll(getUserMessageEntities(friendRequestEntity
							.getFriendID()));
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[UserMessageEntityDAOImpl][getUserAndFriendsMessages] end ");
		return userMessageEntities;
	}

	/**
	 * This method executes query on azure user message storage to get all user
	 * message ID's.
	 * 
	 * @param userId
	 * @return strings
	 * @throws Exception
	 */
	@Override
	public Set<String> getAllUserMessageIds() throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getAllUserMessageIds] start ");
		Set<String> strings = new HashSet<String>();
		TableQuery<UserMessageEntity> userMessages = TableQuery
				.from(UserMessageEntity.class);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE)
				.execute(userMessages)) {
			strings.add(entity.getMessageID());
		}
		LOGGER.debug("Message Count : " + strings != null ? strings.size()
				: null);
		LOGGER.info("[UserMessageEntityDAOImpl][getAllUserMessageIds] start ");
		return strings;
	}

	/**
	 * This method executes query on azure user message storage to delete user
	 * message by input ID's.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteMessageById(String messageId) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][deleteMessageById] start ");
		UserMessageEntity messageEntity = getMessageById(messageId);
		if (messageEntity != null) {
			int startIndex = messageEntity.getMediaURL().indexOf(
					AzureChatConstants.PHOTO_UPLOAD_CONTAINER + "/")
					+ AzureChatConstants.PHOTO_UPLOAD_CONTAINER.length() + 1;
			String fileName = messageEntity.getMediaURL().substring(startIndex);
			profileImageRequestDAO.deletePhoto(fileName);
			LOGGER.debug("Photo for message id : " + messageId + " is deleted.");
			messageCommentsDAO.deleteAllMessageComments(messageId);
			LOGGER.debug("Comments for message id : " + messageId
					+ " are deleted.");
			messageLikeEntityDAO.deleteMessageLikeByMessageId(messageId);
			LOGGER.debug("Likes for message id : " + messageId
					+ " are deleted.");
			CloudTable cloudTable = AzureChatStorageUtils
					.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE);
			String rowFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
					messageId);
			TableQuery<UserMessageEntity> userMessages = TableQuery.from(
					UserMessageEntity.class).where(rowFilter);
			for (UserMessageEntity entity : AzureChatStorageUtils
					.getTableReference(
							AzureChatConstants.TABLE_NAME_USER_MESSAGE)
					.execute(userMessages)) {
				TableOperation deleteOperation = TableOperation.delete(entity);
				cloudTable.execute(deleteOperation);
			}
		}
		LOGGER.info("[UserMessageEntityDAOImpl][deleteMessageById] end");
	}

	/**
	 * This method executes query on azure user message storage to get user
	 * message by id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public UserMessageEntity getMessageById(String messageId) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities] start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		UserMessageEntity userMessage = null;
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, messageId);
		TableQuery<UserMessageEntity> friendListQuery = TableQuery.from(
				UserMessageEntity.class).where(rowFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE)
				.execute(friendListQuery)) {
			userMessageEntities.add(entity);
		}
		if (userMessageEntities != null && userMessageEntities.size() > 0) {
			LOGGER.debug("User Message Entities Size : "
					+ userMessageEntities.size());
			userMessage = userMessageEntities.get(0);
		}
		LOGGER.info("[UserMessageEntityDAOImpl][getAllUserMessageIds] end ");
		return userMessage;

	}
}
