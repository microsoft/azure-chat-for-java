package com.microsoftopentechnologies.azchat.web.dao.data.entities.sql;

import java.util.Date;

public class UserPreferenceEntity {
	Integer userPreferenceID = null;
	Integer userId = null;
	Integer preferenceId = null;
	String preferenceDesc = null;
	Date dateCreated = null;
	Date createdBy = null;
	Date dateModified = null;
	Date modifiedBy = null;
	
	
	
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