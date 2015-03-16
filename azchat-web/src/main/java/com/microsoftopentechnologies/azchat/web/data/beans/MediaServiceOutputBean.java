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
package com.microsoftopentechnologies.azchat.web.data.beans;

/**
 * This class used as media service output bean. It will return streaming URL
 * and assetId. AssetId will be used to delete video asset from media service
 * account after expire time.
 * 
 * @author prajakta_pednekar
 *
 */
public class MediaServiceOutputBean {
	private String streamingUrl;
	private String assetID;

	public MediaServiceOutputBean(String streamingUrl, String assetToDeleteId) {
		this.streamingUrl = streamingUrl;
		this.assetID = assetToDeleteId;
	}

	public MediaServiceOutputBean() {
	}

	/**
	 * @return the streamingUrl
	 */
	public String getStreamingUrl() {
		return streamingUrl;
	}

	/**
	 * @param streamingUrl
	 *            the streamingUrl to set
	 */
	public void setStreamingUrl(String streamingUrl) {
		this.streamingUrl = streamingUrl;
	}

	/**
	 * @return the assetToDeleteId
	 */
	public String getAssetID() {
		return assetID;
	}

	/**
	 * @param assetToDeleteId
	 *            the assetToDeleteId to set
	 */
	public void setAssetID(String assetToDeleteId) {
		this.assetID = assetToDeleteId;
	}
}
