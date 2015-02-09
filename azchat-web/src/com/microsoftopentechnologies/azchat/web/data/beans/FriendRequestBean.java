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
package com.microsoftopentechnologies.azchat.web.data.beans;

/**
 * This bean holds FriendReuest Data.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class FriendRequestBean extends BaseBean {

	/**
	 * Generated serial version id for the FriendRequestBean
	 */
	private static final long serialVersionUID = -4547510058733330711L;
	private String userID;
	private String friendID;
	private String friendName;
	private String friendPhotoUrl;
	private String status;
	private int pendFrndReqCnt;

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the friendID
	 */
	public String getFriendID() {
		return friendID;
	}

	/**
	 * @param friendID
	 *            the friendID to set
	 */
	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}

	/**
	 * @return the friendName
	 */
	public String getFriendName() {
		return friendName;
	}

	/**
	 * @param friendName
	 *            the friendName to set
	 */
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	/**
	 * @return the friendPhotoUrl
	 */
	public String getFriendPhotoUrl() {
		return friendPhotoUrl;
	}

	/**
	 * @param friendPhotoUrl
	 *            the friendPhotoUrl to set
	 */
	public void setFriendPhotoUrl(String friendPhotoUrl) {
		this.friendPhotoUrl = friendPhotoUrl;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the pendFrndReqCnt
	 */
	public int getPendFrndReqCnt() {
		return pendFrndReqCnt;
	}

	/**
	 * @param pendFrndReqCnt
	 *            the pendFrndReqCnt to set
	 */
	public void setPendFrndReqCnt(int pendFrndReqCnt) {
		this.pendFrndReqCnt = pendFrndReqCnt;
	}

	/**
	 * toString implementation for the FriendRequestBean
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" user ID : " + this.userID + " Friend ID : " + this.friendID
				+ " Friend Name :" + this.friendName + " Status : "
				+ this.status);
		return sb.toString();
	}

}
