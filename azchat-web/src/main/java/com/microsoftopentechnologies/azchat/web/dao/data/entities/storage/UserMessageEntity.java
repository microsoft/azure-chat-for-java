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
package com.microsoftopentechnologies.azchat.web.dao.data.entities.storage;

import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * This class represents the association between user & message entity with
 * message as textContent, uploaded photoURL & user name.
 * 
 * @author Rupesh_Shirude
 */
public class UserMessageEntity extends TableServiceEntity implements
		Comparable<UserMessageEntity> {

	private String userName;
	private String userPhotoBlobURL;
	private String textContent;
	private String mediaURL;
	private String mediaType;
	private String assetID;

	public UserMessageEntity(String userID, String messageID) {
		this.partitionKey = userID;
		this.rowKey = messageID;
	}

	public UserMessageEntity() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhotoBlobURL() {
		return userPhotoBlobURL;
	}

	public void setUserPhotoBlobURL(String userPhotoBlobURL) {
		this.userPhotoBlobURL = userPhotoBlobURL;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getUserID() {
		return partitionKey;
	}

	public String getMessageID() {
		return rowKey;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public int compareTo(UserMessageEntity userMessageEntity) {
		return userMessageEntity.getTimeStamp().compareTo(getTimeStamp());
	}

	@Override
	public String toString() {
		return "UserMessageEntity [userName=" + userName
				+ ", userPhotoBlobURL=" + userPhotoBlobURL + ", textContent="
				+ textContent + ", mediaURL=" + mediaURL + ", mediaType="
				+ mediaType + ", partitionKey=" + partitionKey + ", rowKey="
				+ rowKey + ", timeStamp=" + timeStamp + "]";
	}

	/**
	 * @return the assetID
	 */
	public String getAssetID() {
		return assetID;
	}

	/**
	 * @param assetID
	 *            the assetID to set
	 */
	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}
}