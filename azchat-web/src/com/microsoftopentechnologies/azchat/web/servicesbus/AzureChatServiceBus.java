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
package com.microsoftopentechnologies.azchat.web.servicesbus;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.GetQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.QueueInfo;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.MediaServiceOutputBean;
import com.microsoftopentechnologies.azchat.web.mediaservice.AzureChatMediaServices;
import com.microsoftopentechnologies.azchat.web.mediaservice.AzureChatMediaUtils;

/**
 * This class handles azure service bus related functionality.This class
 * provides the operations to read the video messages from the temporary storage
 * and process using azchat media service.The raw video messages deleted from
 * the temporary storage post processing from the media services and stored in
 * to the azure storage.
 *
 */
@Component
public class AzureChatServiceBus {

	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatServiceBus.class);

	private static ServiceBusContract service = null;

	/**
	 * BeanPostProcessor initialization method to create service bus and
	 * queue.Also start the timer task to read and process the video messages.
	 * 
	 * @throws AzureChatException
	 * @throws ServiceException
	 */
	@PostConstruct
	public void init() throws AzureChatException, ServiceException {
		getServicebus();
		createQueue();
	}

	/**
	 * This method will give the object of the service bus
	 * 
	 * @return
	 * @throws AzureChatException
	 */
	public static ServiceBusContract getServicebus() throws AzureChatException {
		LOGGER.info("[AzureChatServiceBus][getServicebus] start");
		if (service == null) {
			String namespace = AzureChatUtils
					.getProperty(AzureChatConstants.SERVICE_BUS_NAMESPACE);
			String saskeyname = AzureChatUtils
					.getProperty(AzureChatConstants.SERVICE_BUS_SASKEYNAME);
			String saskey = AzureChatUtils
					.getProperty(AzureChatConstants.SERVICE_BUS_SASKEY);
			String rootUri = AzureChatUtils
					.getProperty(AzureChatConstants.SERVICE_BUS_ROOT_URI);
			Configuration config = ServiceBusConfiguration
					.configureWithSASAuthentication(namespace, saskeyname,
							saskey, rootUri);
			LOGGER.debug("Namespace : " + namespace + " SAS Key Name"
					+ saskeyname + " SAS Key" + saskey);
			service = ServiceBusService.create(config);
		}
		LOGGER.info("[AzureChatServiceBus][getServicebus] end");
		return service;
	}

	/**
	 * This method will read the message from queue and pass it to media service
	 * call.
	 */
	@Scheduled(fixedRate = 36000)
	public static void readmessage() {
		LOGGER.info("[AzureChatServiceBus][readmessage] start");
		try {
			ReceiveMessageOptions options = ReceiveMessageOptions.DEFAULT;
			options.setReceiveMode(ReceiveMode.RECEIVE_AND_DELETE);

			while (true) {
				ReceiveQueueMessageResult resultQM = service
						.receiveQueueMessage(
								AzureChatConstants.SERVICE_BUS_QUEUENAME,
								options);
				BrokeredMessage message = resultQM.getValue();
				if (message != null && message.getMessageId() != null) {
					String url = (String) message.getProperty("url");
					url = StringEscapeUtils.unescapeJava(url);
					String expiryTime = String.valueOf(message
							.getProperty("exp"));
					// Media service call
					MediaServiceOutputBean outputObject = AzureChatMediaServices
							.performMediaServicesOperations(url,
									Double.parseDouble(expiryTime));
					if (outputObject != null) {
						UserMessageEntity userMessageEntity = new UserMessageEntity(
								message.getProperty("uid").toString(),
								outputObject.getAssetToDeleteId());
						userMessageEntity.setTextContent(message.getProperty(
								"msg").toString());
						userMessageEntity.setMediaURL(outputObject
								.getStreamingUrl());
						userMessageEntity.setUserName(message.getProperty(
								"uname").toString());
						userMessageEntity.setUserPhotoBlobURL(StringEscapeUtils
								.unescapeJava(message.getProperty("photoblob")
										.toString()));
						userMessageEntity.setMediaType("video");
						UserMessageEntityDAO dao = new UserMessageEntityDAOImpl();
						dao.addUserMessageEntity(userMessageEntity);

						QueueRequestDAO queueRequestDAO = new QueueRequestDAOImpl();
						queueRequestDAO.postMessage(
								outputObject.getAssetToDeleteId(),
								Integer.parseInt(expiryTime));

						// Remove temporary stored non encoded video from blob
						String filename = AzureChatMediaUtils.getFileName(url,
								AzureChatConstants.TEMP_UPLOAD_CONTAINER);
						deleteblob(filename);

						// //Remove message from queue.
						// service.deleteMessage(message);
					} else {
						LOGGER.info("[AzureChatServiceBus][readmessage] No output from media service.");
					}
				} else {
					LOGGER.info("[AzureChatServiceBus][readmessage] no more messages");
					break;
				}
			}
		} catch (ServiceException e) {
			LOGGER.error("Exception occurred while reading message from queue in AzureChatServiceBus Timer Task. Exception Message : "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception occurred while reading message from queue in AzureChatServiceBus Timer Task. Exception Message :  "
					+ e.getMessage());
		}
		LOGGER.info("[AzureChatServiceBus][readmessage] end");
	}

	/**
	 * This method is used to create queue.
	 * 
	 * @return
	 * @throws AzureChatException
	 * @throws ServiceException
	 */
	public static CreateQueueResult createQueue() throws AzureChatException,
			ServiceException {
		LOGGER.info("[AzureChatServiceBus][createQueue] start");
		long maxSizeInMegabytes = 5120;
		QueueInfo queueInfo = new QueueInfo(
				AzureChatConstants.SERVICE_BUS_QUEUENAME);
		GetQueueResult queue = null;
		queueInfo.setMaxSizeInMegabytes(maxSizeInMegabytes);
		CreateQueueResult result = null;
		try {
			queue = service.getQueue(AzureChatConstants.SERVICE_BUS_QUEUENAME);

		} catch (ServiceException e) {
			if (queue == null) {
				result = service.createQueue(queueInfo);
			}
		}
		LOGGER.info("[AzureChatServiceBus][createQueue] end");
		return result;
	}

	/**
	 * This method will delete the temporary non-encoded video stored on blob
	 * 
	 * @param filename
	 * @throws AzureChatException
	 */
	private static void deleteblob(String filename) throws AzureChatException {
		LOGGER.info("[AzureChatServiceBus][createQueue] start");
		try {
			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount
					.parse(AzureChatStorageUtils.getStorageConnectionString());
			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			// Retrieve reference to a previously created container.
			CloudBlobContainer container = blobClient
					.getContainerReference(AzureChatConstants.TEMP_UPLOAD_CONTAINER);
			// Retrieve reference to a blob named using filename
			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			// Delete the blob.
			blob.deleteIfExists();
		} catch (Exception e) {
			throw new AzureChatException("Unable to delete file from blob "
					+ filename + " " + e.getMessage());
		}
		LOGGER.info("[AzureChatServiceBus][createQueue] end");
	}

}
