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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
/**
 * This interface used to add, get messages to/from azure table.
 * 
 * @author Administrator
 *
 */
public interface UserMessageEntityDAO {
	/**
	 * This method used to add user message.
	 * 
	 * @param userMessageEntity
	 * @return
	 * @throws Exception
	 */
	public UserMessageEntity addUserMessageEntity(UserMessageEntity userMessageEntity) throws Exception;
	/**
	 * this method used to get user mesage by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserMessageEntity> getUserMessageEntities(String userId)throws Exception;
	/**
	 * This method used to get user messages by userid & messageid.
	 * 
	 * @param userId
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public UserMessageEntity getUserMessageEntities(String userId, String messageId)throws Exception;
	/**
	 * this method used to get user mesage by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserMessageEntity> getUserAndFriendsMessages(String userId)throws Exception;
}
