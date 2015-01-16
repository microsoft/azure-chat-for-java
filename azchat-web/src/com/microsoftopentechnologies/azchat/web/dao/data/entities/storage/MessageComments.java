package com.microsoftopentechnologies.azchat.web.dao.data.entities.storage;

import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;
/**
 * This class represents the textual comments over the message with their status. 
 * 
 * @author 
 *
 */

public class MessageComments extends TableServiceEntity 
implements Comparable<MessageComments>{

	String friendID;
	String friendName;
	String friendProfileBlobURL;
	String comments;
	
	public MessageComments(String messageID, String guid) {
	    this.partitionKey = messageID;
	    this.rowKey = guid;
	}
	
	public MessageComments() { }
	
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