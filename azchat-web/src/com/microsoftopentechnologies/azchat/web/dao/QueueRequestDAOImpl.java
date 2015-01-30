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
 * This class used to do azure queue operations for messages.
 * 
 * @author Rupesh_Shirude
 */
@Service("queueRequestDAO")
public class QueueRequestDAOImpl implements QueueRequestDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(QueueRequestDAOImpl.class);

	/**
	 * This method is used to post message to queue.
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
	 * This method is used to get the messages from queue.
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
	 * This method is used to delete the messages from queue.
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
	 * This method is used to get all messages from queue.
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
		// int messageCount = (int) queue.getApproximateMessageCount();
		int messageCount = 32;
		for (CloudQueueMessage message : queue.retrieveMessages(messageCount,
				1, null, null)) {
			setStrings.add(message.getMessageContentAsString());
		}
		LOGGER.info("[QueueRequestDAOImpl][checkMessageExpiryFromQueue] end ");
		return setStrings;
	}

	/**
	 * This method is used to post message to queue.
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