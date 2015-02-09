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

/**
 * This bean represent the exception data.Excdeption type is a string may
 * represent the exception simple class name or any customized string.Excpetion
 * message can be in build java exception.getMessage() or custom message set by
 * application developer.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class ErrorBean implements Serializable {
	/**
	 *  Generated serial version id for the ErrorBean
	 */
	private static final long serialVersionUID = 8794950173873213053L;
	private String excpType;
	private String excpMsg;

	/**
	 * @return the excpType
	 */
	public String getExcpType() {
		return excpType;
	}

	/**
	 * @param excpType
	 *            the excpType to set
	 */
	public void setExcpType(String excpType) {
		this.excpType = excpType;
	}

	/**
	 * @return the excpMsg
	 */
	public String getExcpMsg() {
		return excpMsg;
	}

	/**
	 * @param excpMsg
	 *            the excpMsg to set
	 */
	public void setExcpMsg(String excpMsg) {
		this.excpMsg = excpMsg;
	}
}
