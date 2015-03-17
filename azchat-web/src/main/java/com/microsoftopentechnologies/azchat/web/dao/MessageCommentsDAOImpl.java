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
	 * the azure storage.Also sorts the comments according to the ascending
	 * order of time-stamp.
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
	 * id combination.Also sorts the comments according to the ascending order
	 * of time-stamp.
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
	 * This method retrieve the message comment count for input message id.
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
	 * This method deletes all the comments for corresponding message id.
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
