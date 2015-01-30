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
package com.microsoftopentechnologies.azchat.web.mediaservice;

/**
 * This class provides different utilities for media service.
 * 
 * @author prajakta_pednekar
 *
 */
public class AzureChatMediaUtils {

	/**
	 * Method returns service endpoint URL from blob URL.
	 * 
	 * @param url
	 * @return http[s]://<storage-account-name>.<blob-service-endpoint>/
	 * @author prajakta_pednekar
	 */
	public static String getServiceEndpointUrl(String url) {
		String serviceUrl = url.substring(0,
				url.indexOf('/', url.indexOf('.')) + 1);
		return serviceUrl;
	}

	/**
	 * This method will return container name using given URL.
	 * 
	 * @param url
	 * @return
	 * @author prajakta_pednekar
	 */
	public static String getContainerName(String url) {
		int startIndex = url.indexOf('/', url.indexOf('.')) + 1;
		int endIndex = url.indexOf('/', startIndex + 1);
		String containerName = url.substring(startIndex, endIndex);
		return containerName;
	}

	/**
	 * This method will find filename using url and container name.
	 * 
	 * @param url
	 * @param containerName
	 * @return
	 * @author prajakta_pednekar
	 */
	public static String getFileName(String url, String containerName) {
		int startIndex = url.indexOf(containerName + "/")
				+ containerName.length() + 1;
		int endIndex = url.indexOf('?', startIndex + 1);
		String fileName = url.substring(startIndex, endIndex);
		return fileName;
	}
}
