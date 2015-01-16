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

public interface AzureChatSQLConstants {

	public static final String SAVE_NEW_USER =
			"INSERT INTO [user] (NAME_ID, IDENTITY_PROVIDER, FIRST_NAME, LAST_NAME,"+
					"PHOTO_BLOB_URL, EMAIL_ADDRESS, PHONE_COUNTRY_CODE, PHONE_NUMBER,"+
					"DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY" +
					") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_NEW_USER = "UPDATE [user] SET FIRST_NAME = ? , LAST_NAME = ? , PHOTO_BLOB_URL = ?, EMAIL_ADDRESS = ? , "
			+ "PHONE_COUNTRY_CODE = ?, PHONE_NUMBER = ? where NAME_ID = ? ";

	public static final String GET_USER_BY_USERID = "SELECT * FROM [user] WHERE user_id = ? ";
	
	public static final String GET_USER_PROFILE_URL_BY_USERID = "SELECT PHOTO_BLOB_URL FROM [user] WHERE user_id = ? ";

	public static final String GET_USER_BY_NAMEID = "SELECT * FROM [user] WHERE name_id = ? ";

	public static final String GET_USER_BY_NAMEID_IDNTITY_PROVIDR = "SELECT * FROM [user] WHERE name_id = ? and identity_provider = ?";

	public static final String GET_USER_BY_FIRST_NAME = "SELECT * FROM [user] WHERE first_name like ?";

	public static final String GET_USER_BY_LAST_NAME = "SELECT * FROM [user] WHERE last_name like ?";

	public static final String GET_USER_BY_FIRST_LAST_NAME = "SELECT * FROM [user] WHERE last_name like ? or first_name like ?";

	public static final String ADD_PREFERENCE_METADATA = "INSERT INTO [preference_metadata] (PREFERENCE_DESCRIPTION,"
			+ "DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY) VALUES (?,?,?,?,?)";

	public static final String GET_PREFERENCE_METADATA_BY_ID = "SELECT * FROM [preference_metadata] WHERE preference_metadata_id = ? ";

	public static final String GET_PREFERENCE_METADATA_ALL = "SELECT * FROM [preference_metadata] ";
	
	public static final String GET_PREFERENCE_METADATA_BY_DESC = "SELECT preference_metadata_id FROM [preference_metadata] WHERE PREFERENCE_DESCRIPTION like ?  ";
	
	public static final String ADD_USER_PREFERENCE = "INSERT INTO [user_preferences]  (USER_ID, PREFERENCE_ID, DATE_CREATED, CREATED_BY,"
			+ "DATE_MODIFIED, MODIFIED_BY) VALUES (?,?,?,?,?,?)";

	public static final String GET_USER_PREFERENCE_BY_USERID = "SELECT a.*, b.PREFERENCE_DESCRIPTION FROM [user_preferences] a join [preference_metadata] b "
			+ "ON a.PREFERENCE_ID=b.preference_metadata_id WHERE a.USER_ID = ? ";

	public static final String GET_USER_PREFERENCE_BY_USERID_PREFERENCEID = "SELECT a.*, b.PREFERENCE_DESCRIPTION FROM [user_preferences] a join [preference_metadata] b "
			+ "ON a.PREFERENCE_ID=b.preference_metadata_id WHERE a.USER_ID = ? AND a.PREFERENCE_ID = ? ";
}
