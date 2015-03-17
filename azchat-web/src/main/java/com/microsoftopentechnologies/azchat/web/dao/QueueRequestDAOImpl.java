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

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;

/**
 * Implementation class for the QueueRequestDAO.This class provides operations
 * to interact with the azure queues.
 * 
 * @author Rupesh_Shirude
 */
@Service("queueRequestDAO")
public class QueueRequestDAOImpl implements QueueRequestDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(QueueRequestDAOImpl.class);

	/**
	 * This method is used to post the input message to azure queue.
	 * 
	 * @param message
	 * @param expiryTimeInSeconds
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public void postMessage(String message, int expiryTimeInSeconds)
			throws Exception {
		LOGGER.info("[QueueRequestDAOImpl][postMessage] start ");
		AzureChatStorageUtils.postMessageToQueue(
				AzureChatConstants.QUEUE_NAME_MESSAGE, message,
				expiryTimeInSeconds);
		LOGGER.info("[QueueRequestDAOImpl][postMessage] end ");
	}

	/**
	 * This method is used to get the messages from azure queue.
	 * 
	 * @return
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public CloudQueueMessage getMessageFromQueue() throws Exception {
		LOGGER.info("[QueueRequestDAOImpl][getMessageFromQueue] start ");
		CloudQueueMessage cloudQueueMessage = AzureChatStorageUtils
				.getMessageFromQueue(AzureChatConstants.QUEUE_NAME_MESSAGE);
		LOGGER.info("[QueueRequestDAOImpl][getMessageFromQueue] end ");
		return cloudQueueMessage;
	}

	/**
	 * This method is used to delete the input messages from azure queue.
	 * 
	 * @param queueMessage
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public void deleteMessageFromQueue(CloudQueueMessage queueMessage)
			throws Exception {
		LOGGER.info("[QueueRequestDAOImpl][deleteMessageFromQueue] start ");
		AzureChatStorageUtils.deleteMessageFromQueue(
				AzureChatConstants.QUEUE_NAME_MESSAGE, queueMessage);
		LOGGER.info("[QueueRequestDAOImpl][deleteMessageFromQueue] end ");
	}

	/**
	 * This method is used to get all messages from azure queue for the input
	 * queue name.
	 * 
	 * @param queueMessage
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public Set<String> getAllMessagesFromQueue(String queueName)
			throws Exception {
		LOGGER.info("[QueueRequestDAOImpl][checkMessageExpiryFromQueue] start ");
		Set<String> setStrings = new HashSet<String>();
		CloudQueue queue = AzureChatStorageUtils.getQueueReference(queueName);
		queue.downloadAttributes();
		int messageCount = 32;
		for (CloudQueueMessage message : queue.retrieveMessages(messageCount,
				1, null, null)) {
			setStrings.add(message.getMessageContentAsString());
		}
		LOGGER.info("[QueueRequestDAOImpl][checkMessageExpiryFromQueue] end ");
		return setStrings;
	}

	/**
	 * This method is used to post input message to azure queue with input
	 * expire time.
	 * 
	 * @param message
	 * @param expiryTimeInSeconds
	 * @throws Exception
	 * @author rupesh_shirude
	 */
	@Override
	public void postFriendRequestNotification(String message,
			int expiryTimeInSeconds) throws Exception {
		LOGGER.info("[QueueRequestDAOImpl][postMessage] start ");
		AzureChatStorageUtils.postMessageToQueue(
				AzureChatConstants.QUEUE_NAME_EMAIL_NOTIFICATION, message,
				expiryTimeInSeconds);
		LOGGER.info("[QueueRequestDAOImpl][postMessage] end ");

	}
}