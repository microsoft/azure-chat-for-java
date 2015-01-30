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
 * This entity holds data for user details.
 * 
 * @author Rupesh_Shirude
 *
 */
public class UserEntity {

	private Integer userID;

	private String nameId;

	private String identityProvider;

	private String firstName;

	private String lastName;

	private String photoBlobUrl;

	private String emailAddress;

	private Integer phoneCountryCode;

	private Long phoneNumber;

	private Date dateCreated;

	private Date createdBy;

	private Date dateModified;

	private Date modifiedBy;

	public UserEntity() {

	}

	public UserEntity(String nameId, String identityProvider, String firstName,
			String lastName, String photoBlobUrl, String emailAddress,
			Integer phoneCountryCode, Long phoneNumber, Date dateCreated,
			Date createdBy, Date dateModified, Date modifiedBy) {
		this.nameId = nameId;
		this.identityProvider = identityProvider;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photoBlobUrl = photoBlobUrl;
		this.emailAddress = emailAddress;
		this.phoneCountryCode = phoneCountryCode;
		this.phoneNumber = phoneNumber;
		this.dateCreated = dateCreated;
		this.createdBy = createdBy;
		this.dateModified = dateModified;
		this.modifiedBy = modifiedBy;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public String getIdentityProvider() {
		return identityProvider;
	}

	public void setIdentityProvider(String identityProvider) {
		this.identityProvider = identityProvider;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotoBlobUrl() {
		return photoBlobUrl;
	}

	public void setPhotoBlobUrl(String photoBlobUrl) {
		this.photoBlobUrl = photoBlobUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(Integer phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
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