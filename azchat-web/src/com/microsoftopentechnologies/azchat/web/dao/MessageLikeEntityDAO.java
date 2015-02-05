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
