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

import java.util.Map;

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageLikesEntity;

/**
 * This interface defines contract methods for the user message like DAO object.
 * 
 *
 */
public interface MessageLikeEntityDAO {
	/**
	 * Add like to the message.
	 * 
	 * @param messageLikesEntity
	 * @return
	 * @throws Exception
	 */
	public MessageLikesEntity addMessageLikesEntity(
			MessageLikesEntity messageLikesEntity) throws Exception;

	/**
	 * Get message likes entity by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Map<String, MessageLikesEntity> getMessageLikeEntity(String messageId)
			throws Exception;

	/**
	 * Get message like entity by message id & friend id.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public MessageLikesEntity getMessageLikeEntity(String messageId,
			String friendId) throws Exception;

	/**
	 * Get message likes count by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageLikeEntityCount(String messageId) throws Exception;

	/**
	 * Get message like count by message id & friend id.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageLikeEntityCount(String messageId, String friendId)
			throws Exception;

	/**
	 * Delete message like entities by message id & friend id.
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public void deleteMessageLikeEntity(MessageLikesEntity messageLikesEntity)
			throws Exception;

	/**
	 * Delete message like entities by message id.
	 * 
	 * @param messageId
	 * @throws Exception
	 */
	public void deleteMessageLikeByMessageId(String messageId) throws Exception;
}
