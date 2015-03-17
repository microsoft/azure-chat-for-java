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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserPreferenceEntity;

/**
 * Interface defines contract methods for user preference functionality.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface UserPreferenceDAO {
	/**
	 * Add User preference entity object in azure SQL user preference table.
	 * 
	 * @param userPreferenceEntity
	 * @return
	 * @throws Exception
	 */
	public UserPreferenceEntity addUserPreferenceEntity(
			UserPreferenceEntity userPreferenceEntity) throws Exception;

	/**
	 * Get user preference entity object by user id from azure SQL user
	 * preference table.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId)
			throws Exception;

	/**
	 * Get user preference entity object by userId & prefMetadataId from azure
	 * SQL user preference table.
	 * 
	 * @param userId
	 * @param prefMetadataId
	 * @return
	 * @throws Exception
	 */
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId,
			Integer prefMetadataId) throws Exception;

	/**
	 * Create User Preference table in azure SQL database.
	 * 
	 * @throws Exception
	 */
	public void createUserPreferenceTable() throws Exception;
}
