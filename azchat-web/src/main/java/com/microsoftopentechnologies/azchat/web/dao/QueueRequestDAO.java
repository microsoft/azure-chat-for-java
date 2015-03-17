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

import java.util.Set;

import com.microsoft.azure.storage.queue.CloudQueueMessage;

/**
 * This interface defines contract methods for azure queue operations for
 * messages.
 * 
 * @author Rupesh_Shirude
 */
public interface QueueRequestDAO {
	/**
	 * This method is used to post message to queue.
	 * 
	 * @param message
	 * @param expiryTimeInSeconds
	 * @throws Exception
	 */
	public void postMessage(String message, int expiryTimeInSeconds)
			throws Exception;

	/**
	 * This method is used to get the messages from queue.
	 * 
	 * @return
	 * @throws Exception
	 */
	public CloudQueueMessage getMessageFromQueue() throws Exception;

	/**
	 * This method is used to delete the messages from queue.
	 * 
	 * @param queueMessage
	 * @throws Exception
	 */
	public void deleteMessageFromQueue(CloudQueueMessage queueMessage)
			throws Exception;

	/**
	 * This method is used to get all messages from queue.
	 * 
	 * @param queueMessage
	 * @throws Exception
	 */
	public Set<String> getAllMessagesFromQueue(String queueName)
			throws Exception;

	/**
	 * This method is used to post message to queue.
	 * 
	 * @param message
	 * @param expiryTimeInSeconds
	 * @throws Exception
	 */
	public void postFriendRequestNotification(String message,
			int expiryTimeInSeconds) throws Exception;
}
