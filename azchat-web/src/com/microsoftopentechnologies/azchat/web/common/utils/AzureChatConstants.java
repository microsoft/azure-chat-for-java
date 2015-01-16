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
 * Interface to hold the common constant values.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public interface AzureChatConstants {

	String EXCEP_CODE_BUSSINESS_EXCEPTION = "BE";
	String EXCEP_CODE_SYSTEM_EXCEPTION = "SE";
	String EXCEP_CODE_JAVA_EXCEPTION = "JE";
	String EXCEP_TYPE_BUSSINESS_EXCEPTION = "com.microsoftopentechnologies.azchat.web.common.exceptions.AZChatBusinessExcdeption";
	String EXCEP_TYPE_SYSTYEM_EXCEPTION = "com.microsoftopentechnologies.azchat.web.common.exceptions.AZChatSystemException";
	
	// Add Handler mapping Here
	String FROM_PAGE_INDEX = "index.htm";
	String FROM_PAGE_LOGIN = "login.htm";
	String FROM_PAGE_REG = "registration.htm";
	String FROM_PAGE_FINDFRIENDS = "searchFriends.htm";
	String FROM_PAGE_FRIND_STATUS = "friendStatus.htm";
	String FROM_PAGE_ADD_FRIEND = "addFriend.htm";

	String FROM_PAGE_GET_PENDING_FRND = "getPendingFriendList.htm";
	String FROM_PAGE_UPDATE_PENDING_FRNDREQ = "updateFrndReqstatus.htm";
	String FROM_PAGE_CONTENT_SHARE="contentShare.htm";

	String FROM_PAGE_UPDATE_USR_PROF = "updateUserProfile.htm";

	// Add View Names Here
	String VIEW_NAME_ERROR = "error";
	String VIEW_NAME_LOGIN = "login";
	String VIEW_NAME_INDEX = "index";
	String VIEW_NAME_HOME = "home";
	String VIEW_NAME_REG = "registration";

	// X-Path node expressions
	String NAME_ID_NODE = "/Assertion/Subject/NameID";
	String ATTRIBUTE_NODE = "/Assertion/AttributeStatement/Attribute";
	String NAME_ATTRIBUTE = "Name";

	// Identity Providers
	String GOOGLE = "Google";
	String YAHOO = "Yahoo!";
	String WINDOWS_LIVE = "uri:WindowsLiveID";

	// Attributes in assertion element returned by ACS
	String ATTR_IDENTITY_PROVIDER = "identityprovider";
	String ATTR_NAME = "name";
	String ATTR_EMAIL_ADDRESS = "emailaddress";

	// Bean name constants
	String USER_BEAN = "userBean";
	String BASE_BEAN = "baseBean";

	// Success Messages
	String SUCCESS_MSG_REG = "Registrered Successfully.Sign In to proceed.";
	String SUCCESS_MSG_ADD_FRIEND = "Friend Added Successfully.";
	String SUCCESS_MSG_USR_PROF_UPDT = "User profile updated successfully.";
	String SUCCESS_MSG_APPROVE_FRND_REQ = "Friend Added successfully.";
	String SUCCESS_MSG_CONTENT_SHARE="User status updated successfully.";
	
	// Failure Messages
	String FAILURE_MSG_REG = "Unable to register!";
	String FAILURE_MSG_USR_PROF_UPDT = "User profile update process failed.";
	String FAILURE_MSG_REJECT_FRND_REQ = "Friend request rejected successfully.";
	
	//Exception Messages
	String EXCEP_MSG_UPLOAD_SIZE_EXCEEDS="File upload size exceeds 200MB.File upload size should not exceeds 200MB.";

	// Property File Names
	String MSG_PROP_FILE = "azchatMessages.properties";
	String RESOURCE_PROP_FILE = "azchatResources.properties";

	// Azure Storage Constants
	String ACCOUNT_NAME = "storage.account.name";
	String ACCOUNT_KEY = "storage.account.key";
	String DEF_ENDPOINT_PROTOCOL = "default.endpoint.protocol";
	String KEY_ACOUNT_NAME = "AccountName";
	String KEY_ACOUNT_KEY = "AccountKey";
	String OPERATOR_EQUALS = "=";
	String CONSTANT_SEMICOLON = ";";
	String DEF_ENDPOINT_PROTOCOL_DEFVAL="DefaultEndpointsProtocol=http;";

	// Azure SQL Constants
	String DB_PROP_KEY_DATABASE = "db.name";
	String DB_PROP_KEY_USER = "db.username";
	String DB_PROP_KEY_PASSWORD = "db.password";
	String DB_PROP_KEY_URL = "db.url";
	String DB_PROP_KEY_HOST_NAME_IN_CERT = "db.hostNameInCertificate";
	String DB_PROP_KEY_LOG_IN_TIMEOUT = "db.loginTimeout";
	String DB_PROP_KEY_ENCRYPT = "db.encrypt";
	String DRIVER = "db.driver.classname";
	String DB_PROP_TIMEOUT_VAL="30";

	// Storage Table Names
	String TABLE_NAME_FRIEND_REQ = "FriendRequest";
	String TABLE_NAME_MESSAGE_COMMENTS = "MessageComments";
	String TABLE_NAME_MESSAGE_LIKES = "MessageLikes";
	String TABLE_NAME_USER_MESSAGE = "UserMessages";
	
	// Storage queue 
	String QUEUE_NAME_MESSAGE = "messageexpiryqueue";

	String FRIEND_REQUEST_APPROVED = "approved";
	String FRIEND_REQUEST_REJECTED = "rejected";
	String FRIEND_REQUEST_PENDING_APPROVAL = "pendingApproval";
	String FRIEND_REQUEST_NO_FRIEND = "noFriend";
	String FRIEND_REQUEST_YOU = "you";
	String FRIEND_REQUEST_SENT = "sent";
	String FRIEND_REQUEST_APPROVE = "approve";
	String FRIEND_REQUEST_REJECT = "reject";

	// Azure table constants
	String PARTITION_KEY = "PartitionKey";
	String ROW_KEY = "RowKey";
	String TIMESTAMP = "Timestamp";

	String CONSTANT_BACK_SLASH = "/";
	String CONSTANT_SPACE = " ";
	String CONSTANT_EMPTY_STRING = "";
	String TIMEZONE_UTC = "UTC";
	
	String MAX_UPLOAD_SIZE_KEY="max.upload.size";

	// Image Types
	String MEDIA_TYPE_IMAGE_JPEG = "image/jpeg";
	String MEDIA_TYPE_IMAGE_PNG = "image/png";
	String MEDIA_TYPE_VIDEO_MOV = "video/quicktime";
	String MEDIA_TYPE_VIDEO_MP4 = "video/mp4";
}
