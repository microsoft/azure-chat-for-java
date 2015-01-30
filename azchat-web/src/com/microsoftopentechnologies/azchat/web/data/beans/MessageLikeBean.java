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

import java.io.Serializable;

/**
 * This data bean holds the data for user message like option.
 * 
 * @author Dnyaneshwar_Pawar
 */
public class MessageLikeBean implements Serializable {

	/**
	 * Generated serial version id.
	 */
	private static final long serialVersionUID = 2188156022027056392L;
	private String msgID;
	private String friendID;
	private String friendName;
	private String photoUrl;

	/**
	 * @return the msgID
	 */
	public String getMsgID() {
		return msgID;
	}

	/**
	 * @param msgID
	 *            the msgID to set
	 */
	public void setMsgID(String msgID) {
		this.msgID = msgID;
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
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * @param photoUrl
	 *            the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
