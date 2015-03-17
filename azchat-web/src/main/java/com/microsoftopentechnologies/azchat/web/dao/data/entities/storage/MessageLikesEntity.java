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
 * This class represents the association between user & message entity with message as textContent, 
 * uploaded photoURL & user name.
 * 
 * @author Rupesh_Shirude
 *
 */
public class MessageLikesEntity extends TableServiceEntity
implements Comparable<MessageLikesEntity> {

	private String friendName;
	private String friendProfileBlobURL;
	
	public MessageLikesEntity(String messageID, String friendID) {
	    this.partitionKey = messageID;
	    this.rowKey = friendID;
	}
	
	public MessageLikesEntity() { }
	
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
	
	public String getMessageID() {
		return partitionKey;
	}
	
	public String getFriendID() {
		return rowKey;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	public int compareTo(MessageLikesEntity messageLikesEntity) {
	    return messageLikesEntity.getTimeStamp().compareTo(getTimeStamp());
	}
	
	@Override
	public String toString() {
		return "MessageLikesEntity [friendName=" + friendName
				+ ", friendProfileBlobURL=" + friendProfileBlobURL
				+ ", partitionKey=" + partitionKey + ", rowKey=" + rowKey
				+ ", timeStamp=" + timeStamp + "]";
	}

}