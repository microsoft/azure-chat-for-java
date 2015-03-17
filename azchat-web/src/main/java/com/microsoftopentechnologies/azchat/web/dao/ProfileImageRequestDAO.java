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
