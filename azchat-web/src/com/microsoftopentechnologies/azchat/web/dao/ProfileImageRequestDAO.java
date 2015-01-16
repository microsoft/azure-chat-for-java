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

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageRequestDAO {
	/**
	 * Used to save profile image by given file object with file name over azure storage.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String saveProfileImage(MultipartFile file, String fileName) throws Exception;
	/**
	 * Used to get the signature to access the private containers.
	 * 
	 * @param containerName
	 * @return
	 * @throws Exception
	 */
	public String getSignatureForPrivateAccess()throws Exception;
}
