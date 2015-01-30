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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageComments;

/**
 * This interface used to do add, get message comments.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface MessageCommentsDAO {
	/**
	 * This method is used to add comments over message.
	 * 
	 * @param messageComments
	 * @return
	 * @throws Exception
	 */
	public MessageComments addMessageComments(MessageComments messageComments)
			throws Exception;

	/**
	 * This method is used to get comments over message by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public List<MessageComments> getMessageComments(String messageId)
			throws Exception;

	/**
	 * This method is used to get comments over message by message id & friend
	 * id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public List<MessageComments> getMessageComments(String messageId,
			String friendId) throws Exception;

	/**
	 * This method is used to get count of comments over message by message id &
	 * friend id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageCommentsCount(String messageId) throws Exception;

	/**
	 * This method is used to get count of comments over message by message id &
	 * friend id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageCommentsCount(String messageId, String friendId)
			throws Exception;

	/**
	 * This method is used to delete all comments over message by message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public void deleteAllMessageComments(String messageId) throws Exception;
}