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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.MessageComments;

/**
 * This interface defines contract methods for user message comment DAO object.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface MessageCommentsDAO {
	/**
	 * Add comments for input message.
	 * 
	 * @param messageComments
	 * @return
	 * @throws Exception
	 */
	public MessageComments addMessageComments(MessageComments messageComments)
			throws Exception;

	/**
	 * Get comments over message by message id.
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
	 * Get count of comments for the corresponding message by message id &
	 * friend id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public Integer getMessageCommentsCount(String messageId, String friendId)
			throws Exception;

	/**
	 * Delete all comments for input message id.
	 * 
	 * @param messageId
	 * @return
	 * @throws Exception
	 */
	public void deleteAllMessageComments(String messageId) throws Exception;
}