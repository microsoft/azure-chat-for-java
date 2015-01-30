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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageComments;

/**
 * Implementation class for UserCommentsDAO.This class provides the
 * implementation for the UserCommentsDAO methods that add and retrieves the
 * user comments from the azure storage.
 * 
 * @author Rupesh_Shirude
 */
@Service("messageCommentsDAO")
public class MessageCommentsDAOImpl implements MessageCommentsDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);

	/**
	 * This method is for adding message comments to the azure storage for
	 * corresponding message.
	 * 
	 * @param messageComments
	 * @return messageComments
	 */
	@Override
	public MessageComments addMessageComments(MessageComments messageComments)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][addMessageComments] start ");
		AzureChatStorageUtils
				.insertOrReplaceEntity(
						AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS,
						messageComments);
		LOGGER.info("[MessageCommentsDAOImpl][addMessageComments] end ");
		return messageComments;
	}

	/**
	 * This method fetch the list of message comments for input message id from
	 * the azure storage.
	 *
	 * @param messageId
	 * @return userMessageEntities
	 */
	@Override
	public List<MessageComments> getMessageComments(String messageId)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments] start ");
		List<MessageComments> userMessageEntities = new ArrayList<MessageComments>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(partitionFilter);
		for (MessageComments entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
				friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments] end ");
		return userMessageEntities;
	}

	/**
	 * This method fetch the message comments for the input message id and user
	 * id combination.
	 * 
	 * @param messageId
	 * @param friendId
	 * @returns List of messageComments
	 */
	@Override
	public List<MessageComments> getMessageComments(String messageId,
			String friendId) throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments] start ");
		List<MessageComments> userMessageEntities = new ArrayList<MessageComments>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(combinedFilter);
		for (MessageComments entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
				friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments]         end ");
		return userMessageEntities;
	}

	/**
	 * This method is used to get count of comments over message by message id &
	 * friend id.
	 * 
	 * @param messageId
	 * @return count
	 * @throws Exception
	 */
	@Override
	public Integer getMessageCommentsCount(String messageId) throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageCommentsCount]         start ");
		int count = 0;
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(partitionFilter);
		for (@SuppressWarnings("unused")
		MessageComments entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
				friendListQuery)) {
			count++;
		}
		LOGGER.info("[MessageCommentsDAOImpl][getMessageCommentsCount]         end ");
		return count;
	}

	/**
	 * This method fetch the message comments for the input user and calculate
	 * the comment count.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer getMessageCommentsCount(String messageId, String friendId)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageCommentsCount]         start ");
		LOGGER.debug(" Message ID : " + messageId + " Friend ID : " + friendId);
		int count = 0;
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(combinedFilter);
		for (@SuppressWarnings("unused")
		MessageComments entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
				friendListQuery)) {
			count++;
		}
		LOGGER.info("[MessageCommentsDAOImpl][getMessageCommentsCount]         end ");
		return count;
	}

	/**
	 * This method deletes the comments of corresponding message id..
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteAllMessageComments(String messageId) throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][deleteAllMessageComments] start ");
		LOGGER.debug("Message id : " + messageId);
		CloudTable cloudTable = AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS);
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageComments> messageListQuery = TableQuery.from(
				MessageComments.class).where(partitionFilter);
		for (MessageComments entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
				messageListQuery)) {
			TableOperation deleteOperation = TableOperation.delete(entity);
			cloudTable.execute(deleteOperation);
		}
		LOGGER.info("[MessageCommentsDAOImpl][deleteAllMessageComments] end ");

	}
}
