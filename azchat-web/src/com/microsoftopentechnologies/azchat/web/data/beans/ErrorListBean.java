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

import java.io.Serializable;
import java.util.List;

/**
 * This bean contains list of errorBean categorized by excpCode.Exception code
 * can be set to differentiate the exception to be useful in redirecting user to
 * the different view based on the exception severity.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class ErrorListBean implements Serializable {
	/**
	 * Generated serial version id for ErrorListBean
	 */
	private static final long serialVersionUID = 920290905626105943L;
	private String excpCode;
	private List<ErrorBean> errorList;

	/**
	 * @return the excpCode
	 */
	public String getExcpCode() {
		return excpCode;
	}

	/**
	 * @param excpCode
	 *            the excpCode to set
	 */
	public void setExcpCode(String excpCode) {
		this.excpCode = excpCode;
	}

	/**
	 * @return the errorList
	 */
	public List<ErrorBean> getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList
	 *            the errorList to set
	 */
	public void setErrorList(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

}
