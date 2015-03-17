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
package com.microsoftopentechnologies.azchat.web.dao;

import java.util.List;

import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;

/**
 * This interface defines contract methods needs to be implemented by the user
 * DAO implementation class.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface UserDAO {

	/**
	 * save the new user object into Azure SQL.
	 */
	public UserEntity saveNewUser(UserEntity user) throws Exception;

	/**
	 * get the user details from Azure SQL by user id
	 */
	UserEntity getUserDetailsByUserId(Integer userId) throws Exception;

	/**
	 * get the user details from Azure SQL by nameId
	 */
	List<UserEntity> getUserDetailsByNameID(String nameId) throws Exception;

	/**
	 * get the user details from Azure SQL by nameId and identityProvider
	 */
	List<UserEntity> getUserDetailsByNameIdAndIdentityProvider(String nameId,
			String identityProvider) throws Exception;

	/**
	 * get the user details from Azure SQL by first name
	 */
	List<UserEntity> getUserDetailsByFirstName(String firstName)
			throws Exception;

	/**
	 * get the user details from Azure SQL by last name
	 */
	List<UserEntity> getUserDetailsByLastName(String lastName) throws Exception;

	/**
	 * get the user details from Azure SQL by first name or last name
	 */
	List<UserEntity> getUserDetailsByFirstNameOrLastName(String name)
			throws Exception;

	/**
	 * save the update user object into Azure SQL
	 * 
	 */
	public UserEntity updateNewUser(UserEntity user) throws Exception;

	/**
	 * get user profile image URL by his userId.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getUserPhotoBlobURL(Integer userId) throws Exception;

	/**
	 * create User table.
	 * 
	 * @throws Exception
	 */
	public void createUserTable() throws Exception;
}