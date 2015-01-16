package com.microsoftopentechnologies.azchat.web.dao.data.entities.storage;

import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;
/**
 * This class represents the association between user & message entity with message as textContent, 
 * uploaded photoURL & username. 
 * 
 * @author 
 */
public class UserMessageEntity extends TableServiceEntity
implements Comparable<UserMessageEntity> {
	
	String userName;
	String userPhotoBlobURL;
	String textContent;
	String mediaURL;
	String mediaType;
    
	public UserMessageEntity(String userID, String messageID) {
        this.partitionKey = userID;
        this.rowKey = messageID;
    }

    public UserMessageEntity() { }

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
				+ textContent + ", mediaURL=" + mediaURL
				+ ", mediaType=" + mediaType + ", partitionKey="
				+ partitionKey + ", rowKey=" + rowKey + ", timeStamp="
				+ timeStamp + "]";
	}
}