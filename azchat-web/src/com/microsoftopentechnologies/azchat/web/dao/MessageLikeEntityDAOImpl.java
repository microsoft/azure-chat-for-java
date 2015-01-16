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

import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageLikesEntity;

@Service("messageLikeEntityDAO")
public class MessageLikeEntityDAOImpl implements MessageLikeEntityDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);
	
	@Override
	public MessageLikesEntity addMessageLikesEntity(MessageLikesEntity messageLikesEntity)throws Exception{
		LOGGER.info("[MessageLikeEntityDAOImpl][addMessageLikesEntity]         start ");
		AzureChatStorageUtils.insertOrReplaceEntity(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES, messageLikesEntity);
		LOGGER.info("[MessageLikeEntityDAOImpl][addMessageLikesEntity]         end ");
		return messageLikesEntity;
	}
	
	@Override
	public List<MessageLikesEntity> getMessageLikeEntity(String messageId)
			throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         start ");
		 List<MessageLikesEntity> messageLikesEntities = new ArrayList<MessageLikesEntity>();
			String partitionFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
					messageId);
			TableQuery<MessageLikesEntity> query = TableQuery.from(
					MessageLikesEntity.class).where(partitionFilter);
			for (MessageLikesEntity entity : AzureChatStorageUtils
					.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES).execute(query)) {
				messageLikesEntities.add(entity);
			}
			Collections.sort(messageLikesEntities);
			LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         end ");
			return messageLikesEntities;
	}
	
	@Override
	public MessageLikesEntity getMessageLikeEntity(String messageId,
			String friendId) throws Exception {
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         start ");
		List<MessageLikesEntity> messageLikesEntities = new ArrayList<MessageLikesEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
				friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageLikesEntity> friendListQuery = TableQuery.from(
				MessageLikesEntity.class).where(combinedFilter);
		for (MessageLikesEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES).execute(
						friendListQuery)) {
			messageLikesEntities.add(entity);
		}
		LOGGER.info("[MessageLikeEntityDAOImpl][getMessageLikeEntity]         end ");
		return messageLikesEntities.get(0);
	}
}
