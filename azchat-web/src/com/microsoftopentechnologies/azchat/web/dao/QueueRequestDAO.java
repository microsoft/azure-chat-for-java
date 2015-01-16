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

import com.microsoft.azure.storage.queue.CloudQueueMessage;
/**
 * This interface used to provide methods for azure queue operations for messages.
 * 
 * @author
 */
public interface QueueRequestDAO {
	/**
	 * This method is used to post message to queue.
	 * 
	 * @param message
	 * @param expiryTimeInSeconds
	 * @throws Exception
	 */
	public void postMessage(String message, int expiryTimeInSeconds) throws Exception ;
	/**
	 * This method is used to get the messages from queue.
	 * 
	 * @return
	 * @throws Exception
	 */
	public CloudQueueMessage getMessageFromQueue() throws Exception ;
	/**
	 * This method is used to delete the messages from queue. 
	 * 
	 * @param queueMessage
	 * @throws Exception
	 */
	public void deleteMessageFromQueue(CloudQueueMessage queueMessage) throws Exception ;
}
