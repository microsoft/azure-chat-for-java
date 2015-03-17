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
 * Interface to hold the common SQL queries that are used throughout the
 * application.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface AzureChatSQLConstants {

	String CREATE_USER_TABLE = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name='User') "
			+ "CREATE TABLE [User] ("
			+ "[USER_ID] [int] IDENTITY(1,1) NOT NULL,"
			+ "[NAME_ID] [nvarchar](250) NOT NULL,"
			+ "[IDENTITY_PROVIDER] [nvarchar](100) NOT NULL,"
			+ "[FIRST_NAME] [nvarchar](50) ,"
			+ "[LAST_NAME] [nvarchar](50) ,"
			+ "[PHOTO_BLOB_URL] [nvarchar](1024),"
			+ "[EMAIL_ADDRESS] [nvarchar](50) ,"
			+ "[PHONE_COUNTRY_CODE] [int] ,"
			+ "[PHONE_NUMBER] [bigint] ,"
			+ "[DATE_CREATED] [date]  ,"
			+ "[CREATED_BY] [date] ,"
			+ "[DATE_MODIFIED] [date]  ," + "[MODIFIED_BY] [date]   " + ")";

	String CREATE_USER_TABLE_INDEX = "IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name like 'userTableIndex') "
			+ "CREATE CLUSTERED INDEX [userTableIndex] ON [User] ([USER_ID])";

	String CREATE_PREFERENCE_METADATA_TABLE = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name='preference_metadata') "
			+ "CREATE TABLE [preference_metadata]("
			+ "[preference_metadata_id] [int] IDENTITY(1,1) NOT NULL,"
			+ "[PREFERENCE_DESCRIPTION] [nvarchar](50) NOT NULL,"
			+ "[DATE_CREATED] [date]  ,"
			+ "[CREATED_BY] [date] ,"
			+ "[DATE_MODIFIED] [date]  ," + "[MODIFIED_BY] [date]   " + ")";

	String CREATE_PREFERENCE_METADATA_TABLE_INDEX = "IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name like 'prefMetaDataIndex') "
			+ "CREATE CLUSTERED INDEX [prefMetaDataIndex] ON [preference_metadata] ([preference_metadata_id])";

	String CREATE_USER_PREFERENCES_TABLE = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name='user_preferences') "
			+ "CREATE TABLE [user_preferences]("
			+ "[user_preferences_id] [int] IDENTITY(1,1) NOT NULL,"
			+ "[USER_ID] [int] NOT NULL,"
			+ "[PREFERENCE_ID] [int] NOT NULL,"
			+ "[DATE_CREATED] [date]  ,"
			+ "[CREATED_BY] [date]  ,"
			+ "[DATE_MODIFIED] [date]  ," + "[MODIFIED_BY] [date]  " + ")";

	String CREATE_USER_PREFERENCES_TABLE_INDEX = "IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name like 'userprefTableIndex') "
			+ "CREATE CLUSTERED INDEX [userprefTableIndex] ON [user_preferences] ([user_preferences_id])";

	String SAVE_NEW_USER = "INSERT INTO [user] (NAME_ID, IDENTITY_PROVIDER, FIRST_NAME, LAST_NAME,"
			+ "PHOTO_BLOB_URL, EMAIL_ADDRESS, PHONE_COUNTRY_CODE, PHONE_NUMBER,"
			+ "DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY"
			+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	String UPDATE_NEW_USER = "UPDATE [user] SET FIRST_NAME = ? , LAST_NAME = ? , PHOTO_BLOB_URL = ?, EMAIL_ADDRESS = ? , "
			+ "PHONE_COUNTRY_CODE = ?, PHONE_NUMBER = ? where NAME_ID = ? ";

	String GET_USER_BY_USERID = "SELECT * FROM [user] WHERE user_id = ? ";

	String GET_USER_PROFILE_URL_BY_USERID = "SELECT PHOTO_BLOB_URL FROM [user] WHERE user_id = ? ";

	String GET_USER_BY_NAMEID = "SELECT * FROM [user] WHERE name_id = ? ";

	String GET_USER_BY_NAMEID_IDNTITY_PROVIDR = "SELECT * FROM [user] WHERE name_id = ? and identity_provider = ?";

	String GET_USER_BY_FIRST_NAME = "SELECT * FROM [user] WHERE first_name like ?";

	String GET_USER_BY_LAST_NAME = "SELECT * FROM [user] WHERE last_name like ?";

	String GET_USER_BY_FIRST_LAST_NAME = "SELECT * FROM [user] WHERE last_name like ? or first_name like ?";

	String ADD_PREFERENCE_METADATA = "INSERT INTO [preference_metadata] (PREFERENCE_DESCRIPTION,"
			+ "DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY) VALUES (?,?,?,?,?)";

	String GET_PREFERENCE_METADATA_BY_ID = "SELECT * FROM [preference_metadata] WHERE preference_metadata_id = ? ";

	String GET_PREFERENCE_METADATA_ALL = "SELECT * FROM [preference_metadata] ";

	String GET_PREFERENCE_METADATA_BY_DESC = "SELECT preference_metadata_id FROM [preference_metadata] WHERE PREFERENCE_DESCRIPTION like ?  ";

	String ADD_USER_PREFERENCE = "INSERT INTO [user_preferences]  (USER_ID, PREFERENCE_ID, DATE_CREATED, CREATED_BY,"
			+ "DATE_MODIFIED, MODIFIED_BY) VALUES (?,?,?,?,?,?)";

	String GET_USER_PREFERENCE_BY_USERID = "SELECT a.*, b.PREFERENCE_DESCRIPTION FROM [user_preferences] a join [preference_metadata] b "
			+ "ON a.PREFERENCE_ID=b.preference_metadata_id WHERE a.USER_ID = ? ";

	String GET_USER_PREFERENCE_BY_USERID_PREFERENCEID = "SELECT a.*, b.PREFERENCE_DESCRIPTION FROM [user_preferences] a join [preference_metadata] b "
			+ "ON a.PREFERENCE_ID=b.preference_metadata_id WHERE a.USER_ID = ? AND a.PREFERENCE_ID = ? ";
}
