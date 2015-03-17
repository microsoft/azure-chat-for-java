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
