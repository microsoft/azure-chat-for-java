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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;

/**
 * Implementation class for the ProfileImageRequestDAO.This class provides
 * operations for the save,retrieve user profile image.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("profileImageRequestDAO")
public class ProfileImageRequestDAOImpl implements ProfileImageRequestDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(ProfileImageRequestDAOImpl.class);

	/**
	 * This method add the image to the profile image storage for the
	 * corresponding user.
	 */
	@Override
	public String saveProfileImage(MultipartFile file, String fileName)
			throws Exception {
		LOGGER.info("[ProfileImageRequestDAOImpl][saveProfileImage] start");
		LOGGER.debug("File Name : " + fileName);
		InputStream inputStream = null;
		URI profileImageBLOBUrl = null;
		byte[] byteArr = null;
		byteArr = file.getBytes();
		inputStream = new ByteArrayInputStream(byteArr);
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(AzureChatConstants.PROFILE_IMAGE_CONTAINER);
		AzureChatStorageUtils.generateSASURL(cloudBlobContainer);
		CloudBlockBlob blob = cloudBlobContainer
				.getBlockBlobReference(fileName);
		blob.getProperties().setContentType(file.getContentType());
		blob.upload(inputStream, byteArr.length);
		profileImageBLOBUrl = blob.getUri();
		LOGGER.debug("Profile Blob URL: " + profileImageBLOBUrl);
		LOGGER.info("[ProfileImageRequestDAOImpl][saveProfileImage] end");
		return profileImageBLOBUrl + AzureChatConstants.CONSTANT_EMPTY_STRING;

	}

	/**
	 * This method retrieves the private access signature.
	 */
	@Override
	public String getSignatureForPrivateAccess() throws Exception {
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(AzureChatConstants.PROFILE_IMAGE_CONTAINER);
		String signature = AzureChatStorageUtils
				.generateSASURL(cloudBlobContainer);
		return signature;
	}

	/**
	 * This method adds the image to the azure storage.
	 */
	@Override
	public String savePhoto(MultipartFile file, String fileName)
			throws Exception {
		LOGGER.info("[ProfileImageRequestDAOImpl][savePhoto] start");
		LOGGER.debug("File Name : " + fileName);
		InputStream inputStream = null;
		URI profileImageBLOBUrl = null;
		byte[] byteArr = null;
		byteArr = file.getBytes();
		inputStream = new ByteArrayInputStream(byteArr);
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(AzureChatConstants.PHOTO_UPLOAD_CONTAINER);
		AzureChatStorageUtils.generateSASURL(cloudBlobContainer);
		CloudBlockBlob blob = cloudBlobContainer
				.getBlockBlobReference(fileName);
		blob.getProperties().setContentType(file.getContentType());
		blob.upload(inputStream, byteArr.length);
		profileImageBLOBUrl = blob.getUri();
		LOGGER.debug("Profile Blob URL: " + profileImageBLOBUrl);
		LOGGER.info("[ProfileImageRequestDAOImpl][savePhoto] end");
		return profileImageBLOBUrl + AzureChatConstants.CONSTANT_EMPTY_STRING;
	}

	/**
	 * This method save/add the video to the temporary azure storage.
	 */
	@Override
	public String saveTempVideo(MultipartFile file, String fileName)
			throws Exception {
		LOGGER.info("[ProfileImageRequestDAOImpl][saveTempVideo] start");
		LOGGER.debug("File Name : " + fileName);
		InputStream inputStream = null;
		URI profileImageBLOBUrl = null;
		byte[] byteArr = null;
		byteArr = file.getBytes();
		inputStream = new ByteArrayInputStream(byteArr);

		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(AzureChatConstants.TEMP_UPLOAD_CONTAINER);
		AzureChatStorageUtils.generateSASURL(cloudBlobContainer);
		CloudBlockBlob blob = cloudBlobContainer
				.getBlockBlobReference(fileName);
		blob.getProperties().setContentType(file.getContentType());
		blob.upload(inputStream, byteArr.length);
		profileImageBLOBUrl = blob.getUri();
		LOGGER.debug("Profile Blob URL: " + profileImageBLOBUrl);
		LOGGER.info("[ProfileImageRequestDAOImpl][saveTempVideo] end");
		return profileImageBLOBUrl + AzureChatConstants.CONSTANT_EMPTY_STRING;
	}

	/**
	 * This method deletes the user profile image from azure storage.
	 */
	@Override
	public void deletePhoto(String fileName) throws Exception {
		LOGGER.info("[ProfileImageRequestDAoImpl][deletePhoto] start");
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(AzureChatConstants.PHOTO_UPLOAD_CONTAINER);

		CloudBlockBlob blob = cloudBlobContainer
				.getBlockBlobReference(fileName);
		if (blob != null) {
			blob.deleteIfExists();
		}
		LOGGER.info("[ProfileImageRequestDAoImpl][deletePhoto] end");
	}

}