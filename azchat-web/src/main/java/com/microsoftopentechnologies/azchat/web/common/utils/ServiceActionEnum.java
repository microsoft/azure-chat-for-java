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
package com.microsoftopentechnologies.azchat.web.common.utils;

/**
 * Enum class to hold the service action constant.These constants can be used to
 * identify the AzChat services uniquely during the server call.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public enum ServiceActionEnum {

	LOGIN("processUserLogin"),
	REGISTRATION("userRegistration"),
	FIND_FRIENDS("FindFriends"),
	MEDIA_SERVICE("mediaService"),
	FRIEND_STATUS("findFriendStatus"),
	ADD_FRIEND("addFriend"),
	UPDATE_USER_PROFILE("updateUserProfile"),
	GET_PENDING_FRIENDS("getPendingFriendList"),
	UPDATE_FRIENDREQ_STATUS("updateFrndReqstatus"),
	CONTENT_SHARE("shareContent"),
	GET_USER_CONTENT("getUserContent"),
	UPDATE_MSG_COMMENT("updateMessageComment"),
	UPDATE_USER_LIKE_STATUS("updateUserLikeStatus"),
	GET_MSG_COMMENTS("getMsgCommets"),
	GET_PENDING_FRIEND_COUNT("getPendingFriendRequestCount");

	private String serviceAction;

	/**
	 * Enum constructor.
	 * @param serviceAction
	 */
	private ServiceActionEnum(String serviceAction) {
		this.serviceAction = serviceAction;
	}

	/**
	 * @return the serviceAction
	 */
	public String getServiceAction() {
		return serviceAction;
	}

	/**
	 * @param serviceAction
	 *            the serviceAction to set
	 */
	public void setServiceAction(String serviceAction) {
		this.serviceAction = serviceAction;
	}

}
