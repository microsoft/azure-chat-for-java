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
}