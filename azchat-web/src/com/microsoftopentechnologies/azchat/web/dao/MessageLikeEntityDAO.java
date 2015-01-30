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
 * This interface used to add, get likes over message.
 * 
 *
 */
public interface MessageLikeEntityDAO {
	/**
	 * this method is used to like the message.
	 * 
	 * @param messageLikesEntity
	 * @return
	 * @throws Exception
	 */
	public MessageLikesEntity addMessageLikesEntity(MessageLikesEntity messageLikesEntity)throws Exception;
	/**
	 * this method is used to get the likes by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Map<String,MessageLikesEntity> getMessageLikeEntity(String messageId) throws Exception;
	/**
	 * This method is used to get this like by message id & friend id. 
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public MessageLikesEntity getMessageLikeEntity(String messageId, String friendId) throws Exception;
	/**
	 * this method is used to get the likes count by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageLikeEntityCount(String messageId) throws Exception;
	/**
	 * This method is used to get like count by message id & friend id. 
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageLikeEntityCount(String messageId, String friendId) throws Exception;
	/**
	 * This method is used to delete like by message id & friend id. 
	 * 
	 * @param messageId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public void deleteMessageLikeEntity(MessageLikesEntity messageLikesEntity) throws Exception;
	/**
	 * This method is used to delete like by message id.
	 * @param messageId
	 * @throws Exception
	 */
	public void deleteMessageLikeByMessageId(String messageId) throws Exception;
}
