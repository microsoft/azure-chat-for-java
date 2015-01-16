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
package com.microsoftopentechnologies.azchat.web.dao;

import java.util.List;

import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
/**
 * Interface to interact with User table in Azure SQL.
 * 
 * @author rupesh_shirude
 *
 */
public interface UserDAO {
	
/**
 * Used to save the new user object into Azure SQL.
 */
	public UserEntity saveNewUser(UserEntity user) throws Exception;
/**
 * Used to get the user details from Azure SQL by userid ...
 */
	UserEntity getUserDetailsByUserId(Integer userId) throws Exception;
/**
 * Used to get the user details from Azure SQL by nameId ...
 */
	List<UserEntity> getUserDetailsByNameID(String nameId) throws Exception;
/**
 * Used to get the user details from Azure SQL by nameId and identityProvider ...
 */		
	List<UserEntity> getUserDetailsByNameIdAndIdentityProvider(String nameId, String identityProvider) throws Exception;
/**
 * Used to get the user details from Azure SQL by first name ...
 */		
	List<UserEntity> getUserDetailsByFirstName(String firstName) throws Exception;
/**
 * Used to get the user details from Azure SQL by last name ...
 */		
	List<UserEntity> getUserDetailsByLastName(String lastName) throws Exception;
/**
 * Used to get the user details from Azure SQL by first name or last name ...
 */		
	List<UserEntity> getUserDetailsByFirstNameOrLastName(String name) throws Exception;
/**
 * Used to save the update user object into Azure SQL ...
 * 
 */
	public UserEntity updateNewUser(UserEntity user) throws Exception;	
/**
 * Used to get user profile image url by his userId.	
 * @param userId
 * @return
 * @throws Exception
 */
	public String getUserPhotoBlobURL(Integer userId) throws Exception ;
}