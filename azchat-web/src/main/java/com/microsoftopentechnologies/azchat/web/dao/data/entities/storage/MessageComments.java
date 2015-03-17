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