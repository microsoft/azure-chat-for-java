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

/**
 * This interface defines contract methods for the profile image management.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface ProfileImageRequestDAO {
	/**
	 * Save/Add profile image by given file object and file name to azure
	 * storage.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String saveProfileImage(MultipartFile file, String fileName)
			throws Exception;

	/**
	 * Get signature to access the private containers.
	 * 
	 * @param containerName
	 * @return
	 * @throws Exception
	 */
	public String getSignatureForPrivateAccess() throws Exception;

	/**
	 * Save/Add profile image by given file object and file name to azure
	 * storage.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String savePhoto(MultipartFile file, String fileName)
			throws Exception;

	/**
	 * Save/Add profile image by given file object and file name to the azure
	 * storage.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String saveTempVideo(MultipartFile file, String fileName)
			throws Exception;

	/**
	 * Save/Ad profile image by given file object and file name to the azure
	 * storage.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public void deletePhoto(String fileName) throws Exception;
}
