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

import java.util.List;
import java.util.Set;

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;

/**
 * This interface defines contract methods to add, get messages to/from azure
 * table.
 * 
 * @author Rupesh_Shirude
 */
public interface UserMessageEntityDAO {
	/**
	 * add user message.
	 * 
	 * @param userMessageEntity
	 * @return
	 * @throws Exception
	 */
	public UserMessageEntity addUserMessageEntity(
			UserMessageEntity userMessageEntity) throws Exception;

	/**
	 * get user message by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserMessageEntity> getUserMessageEntities(String userId)
			throws Exception;

	/**
	 * get user messages by userId & messageId.
	 * 
	 * @param userId
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public UserMessageEntity getUserMessageEntities(String userId,
			String messageId) throws Exception;

	/**
	 * get user message by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserMessageEntity> getUserAndFriendsMessages(String userId)
			throws Exception;

	/**
	 * get all user message ID's.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Set<String> getAllUserMessageIds() throws Exception;

	/**
	 * get all user message ID's.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public void deleteMessageById(String messageId) throws Exception;

	/**
	 * get user message by ID's.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public UserMessageEntity getMessageById(String messageId) throws Exception;
}
