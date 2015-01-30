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

import java.sql.Connection;
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
	 * Used to add User preference entity object in azure sql.
	 * 
	 * @param userPreferenceEntity
	 * @return
	 * @throws Exception
	 */
	public UserPreferenceEntity addUserPreferenceEntity(
			UserPreferenceEntity userPreferenceEntity) throws Exception;

	/**
	 * Used to get user preference entity object by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId)
			throws Exception;

	/**
	 * Used to get user preference entity object by userid & prefMetadataId.
	 * 
	 * @param userId
	 * @param prefMetadataId
	 * @return
	 * @throws Exception
	 */
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId,
			Integer prefMetadataId) throws Exception;

	/**
	 * Used to create User Preference table.
	 * 
	 * @throws Exception
	 */
	public void createUserPreferenceTable(Connection connection) throws Exception;
}
