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
package com.microsoftopentechnologies.azchat.web.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableEntity;
import com.microsoft.azure.storage.table.TableOperation;

/**
 * This class contains common methods to interact with the azure storage
 * services.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Component
public class AzureChatStorageUtils {

	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatStorageUtils.class);

	/**
	 * Method creates azure cloud table.
	 * 
	 * @param tableName
	 * @throws Exception
	 */
	public static void createTable(String tableName) throws Exception {
		getTableReference(tableName).createIfNotExists();
		LOGGER.debug("Table Created Successfully.");
	}

	/**
	 * Method returns the reference to the input table name.
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static CloudTable getTableReference(String tableName)
			throws Exception {
		LOGGER.info("[AzureStorageUtils][getTableReference] start");
		// Get storage connection string
		String storageConnectionString = getStorageConnectionString();
		CloudStorageAccount storageAccount = CloudStorageAccount
				.parse(storageConnectionString);
		
		// Create the table client.
		CloudTableClient tableClient = storageAccount.createCloudTableClient();

		LOGGER.info("[AzureStorageUtils][getTableReference] end.");
		return tableClient.getTableReference(tableName);

	}

	/**
	 * Method deletes the existing cloud table from azure storage.
	 * 
	 * @param tableName
	 * @throws Exception
	 */
	public static void deleteTable(String tableName) throws Exception {
		getTableReference(tableName).deleteIfExists();
		LOGGER.debug("Table deleted Successfully.");
	}

	/**
	 * Method inserts or replace the entity on azure cloud.
	 * 
	 * @param tableName
	 * @param tableEntity
	 * @throws Exception
	 */
	public static void insertOrReplaceEntity(String tableName,
			TableEntity tableEntity) throws Exception {
		// Create an operation to add the new customer to the people table.
		TableOperation tableOperation = TableOperation
				.insertOrReplace(tableEntity);
		
		// Submit the operation to the table service.
		getTableReference(tableName).execute(tableOperation);
		LOGGER.debug("Entity added to table " + tableName);
	}

	/**
	 * This method returns the cloud blob block.
	 * 
	 * @param containerName
	 * @param blobName
	 * @return
	 * @throws Exception
	 */
	public static CloudBlockBlob getBLOBReference(String containerName,
			String blobName) throws Exception {
		CloudBlockBlob blob = getCloudBlobContainer(containerName)
				.getBlockBlobReference(blobName);
		return blob;
	}

	/**
	 * This method gets reference to the cloud container corresponding to the
	 * application configuration paramerters.
	 * 
	 * @param containerName
	 * @return
	 * @throws Exception
	 */
	public static CloudBlobContainer getCloudBlobContainer(String containerName)
			throws Exception {
		
		String storageConnectionString = getStorageConnectionString();
		CloudStorageAccount storageAccount = CloudStorageAccount
				.parse(storageConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient
				.getContainerReference(containerName);
		container.createIfNotExists();

		return container;
	}

	/**
	 * This method will assign public access to the blob container.
	 * 
	 * @param container
	 * @throws Exception
	 */
	public static void assignPublicAccessToContainer(
			CloudBlobContainer container) throws Exception {
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
		containerPermissions
				.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
		container.uploadPermissions(containerPermissions);
	}


	/**
	 * This method will assign private access to the blob container.
	 * 
	 * @param container
	 * @return
	 * @throws Exception
	 */
	public static String assignPrivateAccess(CloudBlobContainer container)
			throws Exception {
		String signature = AzureChatConstants.CONSTANT_EMPTY_STRING;
		SharedAccessBlobPolicy sharedAccessBlobPolicy = new SharedAccessBlobPolicy();
		GregorianCalendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone(AzureChatConstants.TIMEZONE_UTC));
		calendar.setTime(new Date());
		sharedAccessBlobPolicy.setSharedAccessStartTime(calendar.getTime());
		calendar.add(Calendar.HOUR, 23);
		sharedAccessBlobPolicy.setSharedAccessExpiryTime(calendar.getTime());
		sharedAccessBlobPolicy.setPermissions(EnumSet.of(
				SharedAccessBlobPermissions.READ,
				SharedAccessBlobPermissions.WRITE,
				SharedAccessBlobPermissions.DELETE,
				SharedAccessBlobPermissions.LIST));
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
		containerPermissions.setPublicAccess(BlobContainerPublicAccessType.OFF);
		container.uploadPermissions(containerPermissions);
		signature = container.generateSharedAccessSignature(
				sharedAccessBlobPolicy, null);
		return signature;
	}
	
	/**
	 * This method is used to create queue with the given name.
	 * @param queueName
	 * @throws Exception
	 */
	public static void createQueue(String queueName) throws Exception {
		getQueueReference(queueName).createIfNotExists();
	}
	
	/**
	 * This method is used to get queue by given queue name.	
	 * 
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public static CloudQueue getQueueReference(String queueName) throws Exception {
		String storageConnectionString = getStorageConnectionString();
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudQueueClient queueClient = storageAccount.createCloudQueueClient();
		return queueClient.getQueueReference(queueName);
	}
	
	/**
	 * This method is used to post the message to queue by queue name, message and it's visible time.
	 * 	
	 * @param queueName
	 * @param message
	 * @param invisibleTimeInSeconds
	 * @throws Exception
	 */
	public static void postMessageToQueue(String queueName, String message, int invisibleTimeInSeconds) throws Exception {
		CloudQueue cloudQueue = getQueueReference(queueName);
		CloudQueueMessage queueMessage = new CloudQueueMessage(message);

		cloudQueue.addMessage(queueMessage, invisibleTimeInSeconds, 0, null, null); 
	}
	
	/**
	 * This method is used to fetch the messages from queue. 
	 * 
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public static CloudQueueMessage getMessageFromQueue(String queueName) throws Exception {
		CloudQueue cloudQueue = getQueueReference(queueName);
		return cloudQueue.retrieveMessage();
	}
	
	/**
	 * This method is used to delete the messages from the queue.
	 * 
	 * @param queueName
	 * @param queueMessage
	 * @throws Exception
	 */
	public static void deleteMessageFromQueue(String queueName, CloudQueueMessage queueMessage) throws Exception {
		CloudQueue cloudQueue = getQueueReference(queueName);
		cloudQueue.deleteMessage(queueMessage);
	}
	
	/**
	 * This method forms storage connection string
	 */
	public static String getStorageConnectionString() throws Exception {
		String accName = AzureChatConstants.KEY_ACOUNT_NAME
				+ AzureChatConstants.OPERATOR_EQUALS
				+ AzureChatUtils.getProperty(AzureChatConstants.ACCOUNT_NAME)
				+ AzureChatConstants.CONSTANT_SEMICOLON;
		String accKey = AzureChatConstants.KEY_ACOUNT_KEY
				+ AzureChatConstants.OPERATOR_EQUALS
				+ AzureChatUtils.getProperty(AzureChatConstants.ACCOUNT_KEY);
		String defEndPointProtocol = AzureChatUtils
				.getProperty(AzureChatConstants.DEF_ENDPOINT_PROTOCOL);
		
		if (AzureChatUtils.isEmptyOrNull(defEndPointProtocol)) {
			defEndPointProtocol = AzureChatConstants.DEF_ENDPOINT_PROTOCOL_DEFVAL;
		}
		
		return defEndPointProtocol + accName + accKey;
	}
}
