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
 * 
 * This bean represents the data elements required to store the User Text
 * Messages and Media URL.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class UserMessageBean extends BaseBean implements Serializable {

	/**
	 * Generated serial version id for UserMessagesBean
	 */
	private static final long serialVersionUID = 8818253762673691119L;
	private String msgText;
	private String mediaUrl;
	private String mediaType;
	private MultipartFile photoVideoFile;
	private String photoUrl;
	private String msgID;
	private Boolean isLike = false;
	private int likeCount;
	private String ownerName;
	private String ownerID;
	private List<MessageCommentBean> msgCommentList;
	private int commentCount;

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
	 * @return the likeCount
	 */
	public int getLikeCount() {
		return likeCount;
	}

	/**
	 * @param likeCount
	 *            the likeCount to set
	 */
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
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

	/**
	 * @return the msgCommentList
	 */
	public List<MessageCommentBean> getMsgCommentList() {
		return msgCommentList;
	}

	/**
	 * @param msgCommentList
	 *            the msgCommentList to set
	 */
	public void setMsgCommentList(List<MessageCommentBean> msgCommentList) {
		this.msgCommentList = msgCommentList;
	}

	/**
	 * @return the commentCount
	 */
	public int getCommentCount() {
		return commentCount;
	}

	/**
	 * @param commentCount
	 *            the commentCount to set
	 */
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

}
