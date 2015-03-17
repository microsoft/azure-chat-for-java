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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageLikesEntity;

/**
 * This implementation class provides the operations for add,remove like status
 * for the message by adding the records into the azure storage.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("messageLikeEntityDAO")
public class MessageLikeEntityDAOImpl implements MessageLikeEntityDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);

	/**
	 * This method is used to add the likes by message id by adding the record
	 * to the azure storage.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	@Override
	public MessageLikesEntity addMessageLikesEntity(
			MessageLikesEntity messageLikesEntity) throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][addMessageLikesEntity] start ");
		AzureChatStorageUtils
				.insertOrReplaceEntity(
						AzureChatConstants.TABLE_NAME_MESSAGE_LIKES,
						messageLikesEntity);
		LOGGER.info("[MessageLikeEntityDAOImpl][addMessageLikesEntity] end ");
		return messageLikesEntity;
	}

	/**
	 * This method is used to get the likes by message id by fetching azure
	 * storage records.
	 * 
	 * @param messageId
	 * @return List of MessageLikeEntity
	 * @throws Exception
	 */
	@Override
	public Map<String, MessageLikesEntity> getMessageLikeEntity(String messageId)
			throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity] start ");
		Map<String, MessageLikesEntity> messageLikesEntities = new HashMap<String, MessageLikesEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageLikesEntity> query = TableQuery.from(
				MessageLikesEntity.class).where(partitionFilter);
		for (MessageLikesEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES)
				.execute(query)) {
			String mapKey = entity.getFriendID() + entity.getMessageID();
			messageLikesEntities.put(mapKey, entity);
		}
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity] end ");
		return messageLikesEntities;
	}

	/**
	 * This method is used to fetch the like status from azure storage by
	 * message id & friend id.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	@Override
	public MessageLikesEntity getMessageLikeEntity(String messageId,
			String friendId) throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity] start ");
		List<MessageLikesEntity> messageLikesEntities = new ArrayList<MessageLikesEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageLikesEntity> friendListQuery = TableQuery.from(
				MessageLikesEntity.class).where(combinedFilter);
		for (MessageLikesEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES)
				.execute(friendListQuery)) {
			messageLikesEntities.add(entity);
		}
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity] end ");
		return messageLikesEntities.get(0);
	}

	/**
	 * This method is used to fetch the likes status count by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer getMessageLikeEntityCount(String messageId) throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         start ");
		int count = 0;
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageLikesEntity> query = TableQuery.from(
				MessageLikesEntity.class).where(partitionFilter);
		for (@SuppressWarnings("unused")
		MessageLikesEntity entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_LIKES).execute(query)) {
			count++;
		}
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         end ");
		return count;
	}

	/**
	 * This method is used to fetch like status by message id & friend id and
	 * calculate the count.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer getMessageLikeEntityCount(String messageId, String friendId)
			throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntityCount]         start ");
		int count = 0;
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL, friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageLikesEntity> friendListQuery = TableQuery.from(
				MessageLikesEntity.class).where(combinedFilter);
		for (@SuppressWarnings("unused")
		MessageLikesEntity entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_MESSAGE_LIKES).execute(
				friendListQuery)) {
			count++;
		}
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntityCount]         end ");
		return count;
	}

	/**
	 * This method is used to delete like by message id & friend id by removing
	 * the record from the azure storage.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteMessageLikeEntity(MessageLikesEntity messageLikesEntity)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][deleteMessageLikeEntity] start ");
		CloudTable cloudTable = AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES);
		TableOperation tableOperation = TableOperation.retrieve(
				messageLikesEntity.getMessageID(),
				messageLikesEntity.getFriendID(), MessageLikesEntity.class);
		MessageLikesEntity messageLikesEntity1 = cloudTable.execute(
				tableOperation).getResultAsType();
		TableOperation deleteOperation = TableOperation
				.delete(messageLikesEntity1);
		cloudTable.execute(deleteOperation);
		LOGGER.info("[MessageCommentsDAOImpl][deleteMessageLikeEntity] end ");
	}

	/**
	 * This method is used to delete like by message id by deleting the record
	 * from the azure storage.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteMessageLikeByMessageId(String messageId) throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][deleteMessageLikeByMessageId] start ");
		CloudTable cloudTable = AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES);
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageLikesEntity> likeListQuery = TableQuery.from(
				MessageLikesEntity.class).where(partitionFilter);
		for (MessageLikesEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES)
				.execute(likeListQuery)) {
			TableOperation deleteOperation = TableOperation.delete(entity);
			cloudTable.execute(deleteOperation);
		}
		LOGGER.info("[MessageCommentsDAOImpl][deleteMessageLikeByMessageId] end ");
	}
}
