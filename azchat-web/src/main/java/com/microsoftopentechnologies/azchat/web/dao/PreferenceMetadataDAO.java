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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;

/**
 * This interface defines contract methods to be implemented by user preference
 * DAO object to add user preference functionality.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface PreferenceMetadataDAO {
	/**
	 * Used to add preference meta data entity.
	 * 
	 * @param preferenceMetadataEntity
	 * @return
	 * @throws Exception
	 */
	public PreferenceMetadataEntity addPreferenceMetadataEntity(
			PreferenceMetadataEntity preferenceMetadataEntity) throws Exception;

	/**
	 * Used to get preference meta data entity by its id.
	 * 
	 * @param preferenceMetadataEntityId
	 * @return
	 * @throws Exception
	 */
	public PreferenceMetadataEntity getPreferenceMetadataEntityById(
			Integer preferenceMetadataEntityId) throws Exception;

	/**
	 * Used to get all preference Meta data Entities.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PreferenceMetadataEntity> getPreferenceMetadataEntities()
			throws Exception;

	/**
	 * Get to Preference Meta data ID using description
	 * 
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	public Integer getPreferenceMetedataIdByDescription(String desc)
			throws Exception;

	/**
	 * 
	 * Used to create preference meta data table.
	 * 
	 * @throws Exception
	 */
	public void createPreferenceMatedateTable() throws Exception;
}
