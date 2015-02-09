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
package com.microsoftopentechnologies.azchat.web.dao.data.entities.sql;

import java.util.Date;

/**
 * This entity holds the data for user preferences.
 * 
 * @author Rupesh_Shirude
 *
 */
public class UserPreferenceEntity {
	private Integer userPreferenceID = null;
	private Integer userId = null;
	private Integer preferenceId = null;
	private String preferenceDesc = null;
	private Date dateCreated = null;
	private Date createdBy = null;
	private Date dateModified = null;
	private Date modifiedBy = null;

	public String getPreferenceDesc() {
		return preferenceDesc;
	}

	public void setPreferenceDesc(String preferenceDesc) {
		this.preferenceDesc = preferenceDesc;
	}

	public Integer getUserPreferenceID() {
		return userPreferenceID;
	}

	public void setUserPreferenceID(Integer userPreferenceID) {
		this.userPreferenceID = userPreferenceID;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPreferenceId() {
		return preferenceId;
	}

	public void setPreferenceId(Integer preferenceId) {
		this.preferenceId = preferenceId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Date createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Date modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}