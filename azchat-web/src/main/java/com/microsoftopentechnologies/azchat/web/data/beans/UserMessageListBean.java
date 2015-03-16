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
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * This bean holds the collection of the userMesagebean.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class UserMessageListBean implements Serializable {

	/**
	 * Generated serial version id for UserMessageListBean
	 */
	private static final long serialVersionUID = -5760903037015615684L;
	private String msgText;
	private String mediaUrl;
	private String mediaType;
	private List<UserMessageBean> userMsgList;
	private MultipartFile photoVideoFile;
	private String expiryTime;
	private Boolean isLike;
	private String msgID;
	private String ownerName;
	private String ownerID;

	/**
	 * @return the msgText
	 */
	public String getMsgText() {
		return msgText;
	}

	/**
	 * @param msgText
	 *            the msgText to set
	 */
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl() {
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *            the mediaUrl to set
	 */
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType
	 *            the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * @return the userMsgList
	 */
	public List<UserMessageBean> getUserMsgList() {
		return userMsgList;
	}

	/**
	 * @param userMsgList
	 *            the userMsgList to set
	 */
	public void setUserMsgList(List<UserMessageBean> userMsgList) {
		this.userMsgList = userMsgList;
	}

	/**
	 * @return the photoVideoFile
	 */
	public MultipartFile getPhotoVideoFile() {
		return photoVideoFile;
	}

	/**
	 * @param photoVideoFile
	 *            the photoVideoFile to set
	 */
	public void setPhotoVideoFile(MultipartFile photoVideoFile) {
		this.photoVideoFile = photoVideoFile;
	}

	/**
	 * @return the expiryTime
	 */
	public String getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param expiryTime
	 *            the expiryTime to set
	 */
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	/**
	 * @return the isLike
	 */
	public Boolean getIsLike() {
		return isLike;
	}

	/**
	 * @param isLike
	 *            the isLike to set
	 */
	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

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
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the ownerID
	 */
	public String getOwnerID() {
		return ownerID;
	}

	/**
	 * @param ownerID
	 *            the ownerID to set
	 */
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

}
