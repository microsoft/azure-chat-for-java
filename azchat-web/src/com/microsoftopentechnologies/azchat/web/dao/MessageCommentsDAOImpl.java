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
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageComments;

@Service("messageCommentsDAO")
public class MessageCommentsDAOImpl implements MessageCommentsDAO{
	private static final Logger LOGGER = LogManager.getLogger(FriendRequestDAOImpl.class);
	
	@Override
	public MessageComments addMessageComments(MessageComments messageComments)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][addMessageComments]         start ");
		AzureChatStorageUtils.insertOrReplaceEntity(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS, messageComments);
		LOGGER.info("[MessageCommentsDAOImpl][addMessageComments]         end ");
		return messageComments;
	}

	@Override
	public List<MessageComments> getMessageComments(String messageId)
			throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments]         start ");
		List<MessageComments> userMessageEntities = new ArrayList<MessageComments>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(partitionFilter);
		for (MessageComments entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
						friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments]         end ");
		return userMessageEntities;
	}

	@Override
	public List<MessageComments> getMessageComments(String messageId,
			String friendId) throws Exception {
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments]         start ");
		List<MessageComments> userMessageEntities = new ArrayList<MessageComments>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				messageId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
				friendId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<MessageComments> friendListQuery = TableQuery.from(
				MessageComments.class).where(combinedFilter);
		for (MessageComments entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS).execute(
						friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[MessageCommentsDAOImpl][getMessageComments]         end ");
		return userMessageEntities;
	}

}
