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
package com.microsoftopentechnologies.azchat.web.common.utils;

/**
 * Enum class to hold the service action constant.These constants can be used to
 * identify the service.
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
