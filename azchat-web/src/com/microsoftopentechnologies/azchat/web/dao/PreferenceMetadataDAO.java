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

import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;

public interface PreferenceMetadataDAO {
	/**
	 * Used to add preference metadata entity.
	 * 
	 * @param preferenceMetadataEntity
	 * @return
	 * @throws Exception
	 */
	public PreferenceMetadataEntity addPreferenceMetadataEntity(PreferenceMetadataEntity preferenceMetadataEntity) throws Exception;
	/**
	 * Used to get preference metadata entity by its id. 
	 * 
	 * @param preferenceMetadataEntityId
	 * @return
	 * @throws Exception
	 */
	public PreferenceMetadataEntity getPreferenceMetadataEntityById(Integer preferenceMetadataEntityId) throws Exception;
	/**
	 * Used to get all preference Metadata Entities.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PreferenceMetadataEntity> getPreferenceMetadataEntities() throws Exception;
	
	public Integer getPreferenceMetedataIdByDescription(String desc)throws Exception;
}
