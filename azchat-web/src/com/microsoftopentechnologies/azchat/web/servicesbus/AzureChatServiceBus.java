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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStartupUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.MediaServiceOutputBean;
import com.microsoftopentechnologies.azchat.web.mediaservice.AzureChatMediaService;

/**
 * This class handles azure service bus related functionality.This class
 * provides the operations to read the video messages from the temporary storage
 * and process using AzChat media service.The raw video messages deleted from
 * the temporary storage post processing from the media services and stored in
 * to the azure storage.
 *
 */
@Component
public class AzureChatServiceBus {

	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatServiceBus.class);

	private static ServiceBusContract service = null;

	@Autowired
	private AzureChatStartupUtils azureChatStartupUtils;

	@Autowired
	AzureChatMediaService azureChatMediaService;

	/**
	 * BeanPostProcessor initialization method to create azure service bus and
	 * queue.This method consumes the exceptions and store it in application
	 * context using azure startup utils.
	 * 
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("[AzureChatServiceBus][init] start");
		String excpMsg = null;
		try {

			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_SERVICE_BUS);
			LOGGER.debug("Service Bus initialization started.");
			getServicebus();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating azure service bus object from the existing configuration. Exception Message : "
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}
		try {
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_SERVICE_QUEUE);
			LOGGER.debug("creating Service Bus queue.");
			createQueue();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating azure service bus queue. Exception Message :"
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}
		LOGGER.info("[AzureChatServiceBus][init] end");
	}

	/**
	 * This method create and assign the static reference to the azure service
	 * bus object.
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
	 * This method reads video messages from the service bus queue and perform
	 * the media service operations on the video message.Finally it deletes the
	 * blob from temporary storage.
	 * 
	 * @param
	 * @return
	 */
	@Scheduled(fixedRate = 36000)
	public void readmessage() {
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
					String url = (String) message
							.getProperty(AzureChatConstants.BROK_MSG_KEY_URL);
					url = StringEscapeUtils.unescapeJava(url);
					String expiryTime = String
							.valueOf(message
									.getProperty(AzureChatConstants.BROK_MSG_KEY_EXPIRY_TIME));
					LOGGER.debug("Broker Message : URL : " + url
							+ " Expiry time : " + expiryTime + " seconds.");
					// Media service call
					LOGGER.debug("Media service call start time : "
							+ AzureChatUtils.getCurrentTimestamp());
					MediaServiceOutputBean outputObject = azureChatMediaService
							.performMediaServicesOperations(url,
									Double.parseDouble(expiryTime));
					LOGGER.debug("Media service call end time : "
							+ AzureChatUtils.getCurrentTimestamp());
					if (outputObject != null) {
						LOGGER.debug("Asset ID : " + outputObject.getAssetID());
						UserMessageEntity userMessageEntity = prepareUserMessageEntity(
								message, outputObject);
						UserMessageEntityDAO dao = new UserMessageEntityDAOImpl();
						dao.addUserMessageEntity(userMessageEntity);
						LOGGER.debug("Video message added the user message table. Message id : "
								+ userMessageEntity.getMessageID());
						QueueRequestDAO queueRequestDAO = new QueueRequestDAOImpl();
						queueRequestDAO.postMessage(
								userMessageEntity.getMessageID(),
								Integer.parseInt(expiryTime));
						LOGGER.debug("Video message added the user message expiry queue. Message id : "
								+ userMessageEntity.getMessageID());
						// Remove temporary stored non encoded video from blob
						String filename = AzureChatUtils.getFileName(url,
								AzureChatConstants.TEMP_UPLOAD_CONTAINER);
						deleteblob(filename);
						LOGGER.debug("Deleted " + filename + " blob from the "
								+ AzureChatConstants.TEMP_UPLOAD_CONTAINER
								+ " storage.");
					}
				} else {
					LOGGER.debug("No more messages");
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
	 * This method creates service bus queue.
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
				LOGGER.debug("Not able to find the queue "
						+ AzureChatConstants.SERVICE_BUS_QUEUENAME
						+ " for service bus.Creating new queue.");
				result = service.createQueue(queueInfo);
			}
		}
		LOGGER.info("[AzureChatServiceBus][createQueue] end");
		return result;
	}

	/**
	 * This method deletes temporary non-encoded video from azure storage.
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
			throw new AzureChatBusinessException(
					"Exception occurred while deleting blob " + filename
							+ " from the  "
							+ AzureChatConstants.TEMP_UPLOAD_CONTAINER
							+ ". Exception Message : " + e.getMessage());
		}
		LOGGER.info("[AzureChatServiceBus][createQueue] end");
	}

	/**
	 * This method populates the userMessageEntity values from the broker
	 * message and MediaServiceOutputBean.
	 * 
	 * @param message
	 * @param outputObject
	 * @return userMessageEntity
	 */
	private UserMessageEntity prepareUserMessageEntity(BrokeredMessage message,
			MediaServiceOutputBean outputObject) {
		UserMessageEntity userMessageEntity = new UserMessageEntity(
				AzureChatUtils.toString(message
						.getProperty(AzureChatConstants.BROK_MSG_KEY_UID)),
				AzureChatUtils.toString(message
						.getProperty(AzureChatConstants.BROK_MSG_KEY_MSG_ID)));

		userMessageEntity.setTextContent(AzureChatUtils.toString(message
				.getProperty(AzureChatConstants.BROK_MSG_KEY_TEXT_MSG)));
		userMessageEntity.setMediaURL(outputObject.getStreamingUrl());
		userMessageEntity.setUserName(AzureChatUtils.toString(message
				.getProperty(AzureChatConstants.BROK_MSG_KEY_USER_NAME)));
		userMessageEntity
				.setUserPhotoBlobURL(StringEscapeUtils.unescapeJava(AzureChatUtils.toString(message
						.getProperty(AzureChatConstants.BROK_MSG_KEY_PHOTO_BLOB))));
		userMessageEntity
				.setMediaType(AzureChatConstants.BROK_MSG_KEY_MED_TYPE_VEDIO);
		userMessageEntity.setAssetID(outputObject.getAssetID());
		return userMessageEntity;
	}

}
