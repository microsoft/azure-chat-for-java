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