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
