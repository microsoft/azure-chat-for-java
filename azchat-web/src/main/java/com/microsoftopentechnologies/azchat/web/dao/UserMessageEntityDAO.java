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
