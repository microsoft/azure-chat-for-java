package com.microsoftopentechnologies.azchat.web.dao.data.entities.storage;

import com.microsoft.azure.storage.table.TableServiceEntity;
/**
 * This class represents the association between user and friend by user id and 
 * friend id with their status. 
 * 
 * @author 
 *
 */
public class FriendRequestEntity extends TableServiceEntity {
	
	String friendName;
	String friendProfileBlobURL;
	String status;
    
	/**
	 * @param userID
	 * @param friendID
	 */
	public FriendRequestEntity(String userID, String friendID) {
        this.partitionKey = userID;
        this.rowKey = friendID;
    }

    public FriendRequestEntity() { }
    
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