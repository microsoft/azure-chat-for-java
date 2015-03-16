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

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * This class represents the association between user and friend by user id and
 * friend id with their status.
 * 
 *
 */
public class FriendRequestEntity extends TableServiceEntity {

	private String friendName;
	private String friendProfileBlobURL;
	private String status;

	/**
	 * @param userID
	 * @param friendID
	 */
	public FriendRequestEntity(String userID, String friendID) {
		this.partitionKey = userID;
		this.rowKey = friendID;
	}

	public FriendRequestEntity() {
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendProfileBlobURL() {
		return friendProfileBlobURL;
	}

	public void setFriendProfileBlobURL(String friendProfileBlobURL) {
		this.friendProfileBlobURL = friendProfileBlobURL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserID() {
		return partitionKey;
	}

	public String getFriendID() {
		return rowKey;
	}

	@Override
	public String toString() {
		return "FriendRequestEntity [friendName=" + friendName
				+ ", friendProfileBlobURL=" + friendProfileBlobURL
				+ ", status=" + status + ", partitionKey=" + partitionKey
				+ ", rowKey=" + rowKey + ", timeStamp=" + timeStamp + "]";
	}
}