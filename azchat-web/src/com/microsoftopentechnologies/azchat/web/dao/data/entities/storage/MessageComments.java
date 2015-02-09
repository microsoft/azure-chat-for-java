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
 * This entity class holds data for User message comments.
 * 
 * @author Rupesh_Shirude
 *
 */
public class MessageComments extends TableServiceEntity implements
		Comparable<MessageComments> {

	private String friendID;
	private String friendName;
	private String friendProfileBlobURL;
	private String comments;

	public MessageComments(String messageID, String guid) {
		this.partitionKey = messageID;
		this.rowKey = guid;
	}

	public MessageComments() {
	}

	public String getFriendID() {
		return friendID;
	}

	public void setFriendID(String friendID) {
		this.friendID = friendID;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMessageID() {
		return partitionKey;
	}

	public String getGUID() {
		return rowKey;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public int compareTo(MessageComments messageCommentsEntity) {
		return messageCommentsEntity.getTimeStamp().compareTo(getTimeStamp());
	}

	@Override
	public String toString() {
		return "MessageComments [friendID=" + friendID + ", friendName="
				+ friendName + ", friendProfileBlobURL=" + friendProfileBlobURL
				+ ", comments=" + comments + ", partitionKey=" + partitionKey
				+ ", rowKey=" + rowKey + ", timeStamp=" + timeStamp + "]";
	}
}