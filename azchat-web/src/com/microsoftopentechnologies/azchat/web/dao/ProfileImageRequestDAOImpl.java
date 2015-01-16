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

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;

@Service("profileImageRequestDAO")
public class ProfileImageRequestDAOImpl implements ProfileImageRequestDAO{
	
	private static final String PROFILE_IMAGE_CONTAINER = "userprofilepics";
	
	@Override
	public  String saveProfileImage(MultipartFile file, String fileName ) throws Exception{
		
		InputStream inputStream = null;
		URI profileImageBLOBUrl = null;
		byte[] byteArr = null;
		byteArr = file.getBytes();
		inputStream = new ByteArrayInputStream(byteArr);
		
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils.getCloudBlobContainer(PROFILE_IMAGE_CONTAINER);
		
		AzureChatStorageUtils.assignPrivateAccess(cloudBlobContainer);
		
	    CloudBlockBlob blob = cloudBlobContainer.getBlockBlobReference(fileName);
	    
    	// CloudBlockBlob blob = AzureStorageUtils.assignPublicAccessToContainer(container);
	    blob.getProperties().setContentType(file.getContentType());

		blob.upload(inputStream, byteArr.length);

		profileImageBLOBUrl = blob.getUri();
		
		return profileImageBLOBUrl +"" ;

	}

	@Override
	public String getSignatureForPrivateAccess()
			throws Exception {
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils.getCloudBlobContainer(PROFILE_IMAGE_CONTAINER);
		String signature =  AzureChatStorageUtils.assignPrivateAccess(cloudBlobContainer);
		return signature;
	}
	
	
	
}