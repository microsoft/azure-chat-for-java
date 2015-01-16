package com.microsoftopentechnologies.azchat.web.dao.data.entities.storage;

import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;
/**
 * This class represents the association between user & message entity with message as textContent, 
 * uploaded photoURL & username.
 * 
 * @author
 *
 */
public class MessageLikesEntity extends TableServiceEntity
implements Comparable<MessageLikesEntity> {

	String friendName;
	String friendProfileBlobURL;
	
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