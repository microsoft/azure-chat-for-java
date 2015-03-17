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