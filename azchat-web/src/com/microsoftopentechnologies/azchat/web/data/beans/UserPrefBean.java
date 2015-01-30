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
package com.microsoftopentechnologies.azchat.web.data.beans;

/**
 * This data bean hold the data for User preferences.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class UserPrefBean extends BaseBean {

	/**
	 * Generated Serial Version id for the UserPrefBean.
	 */
	private static final long serialVersionUID = -2828608180912890073L;
	private String prefDesc;
	private Boolean isChecked;

	/**
	 * @return the prefDesc
	 */
	public String getPrefDesc() {
		return prefDesc;
	}

	/**
	 * @param prefDesc
	 *            the prefDesc to set
	 */
	public void setPrefDesc(String prefDesc) {
		this.prefDesc = prefDesc;
	}

	/**
	 * @return the isChecked
	 */
	public Boolean getIsChecked() {
		return isChecked;
	}

	/**
	 * @param isChecked
	 *            the isChecked to set
	 */
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
}
