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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;

@Service("userMessageEntityDAO")
public class UserMessageEntityDAOImpl implements UserMessageEntityDAO{
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);
	
	@Autowired
	private FriendRequestDAO friendRequestDAO;
	
	@Override
	public UserMessageEntity addUserMessageEntity(
			UserMessageEntity userMessageEntity) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][addUserMessageEntity]         start ");
		AzureChatStorageUtils.insertOrReplaceEntity(AzureChatConstants.TABLE_NAME_USER_MESSAGE, userMessageEntity);
		LOGGER.info("[UserMessageEntityDAOImpl][addUserMessageEntity]         end ");
		return userMessageEntity;
	}

	@Override
	public List<UserMessageEntity> getUserMessageEntities(String userId)
			throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities]         start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		
		//GET THE USER MESSAGES AND ADDED TO LIST.
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userId);
		TableQuery<UserMessageEntity> friendListQuery = TableQuery.from(
				UserMessageEntity.class).where(partitionFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE).execute(
						friendListQuery)) {
			userMessageEntities.add(entity);
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities]         end ");
		return userMessageEntities;
	}

	@Override
	public UserMessageEntity getUserMessageEntities(String userId,
			String messageId) throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities]         start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userId);
		String rowFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
				messageId);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, rowFilter);
		TableQuery<UserMessageEntity> friendListQuery = TableQuery.from(
				UserMessageEntity.class).where(combinedFilter);
		for (UserMessageEntity entity : AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_USER_MESSAGE).execute(
						friendListQuery)) {
			userMessageEntities.add(entity);
		}
		LOGGER.info("[UserMessageEntityDAOImpl][getUserMessageEntities]         end ");
		return userMessageEntities.get(0);
	}

	@Override
	public List<UserMessageEntity> getUserAndFriendsMessages(String userId)
			throws Exception {
		LOGGER.info("[UserMessageEntityDAOImpl][getUserAndFriendsMessages]         start ");
		List<UserMessageEntity> userMessageEntities = new ArrayList<UserMessageEntity>();
		// GET THE LIST OF USER MESSAGES AND ADD TO LIST.
		userMessageEntities = getUserMessageEntities(userId);
		
		//GET THE LIST OF USER'S FRIENDS.
		List<FriendRequestEntity> friendRequestEntities = friendRequestDAO.getFriendListForUser(userId);
		
		//FOR EACH FRIEND, GET THE MESSAGES UPLOADED BY THEM AND APPEND TO ABOVE LIST.
		for(FriendRequestEntity friendRequestEntity : friendRequestEntities){
			userMessageEntities.addAll(getUserMessageEntities(friendRequestEntity.getFriendID()));
		}
		Collections.sort(userMessageEntities);
		LOGGER.info("[UserMessageEntityDAOImpl][getUserAndFriendsMessages]         end ");
		return userMessageEntities;
	}
	
}
