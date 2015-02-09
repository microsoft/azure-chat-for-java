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
package com.microsoftopentechnologies.azchat.web.common.exceptions;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;

/**
 * 
 * This class can be used to raise the application specific exceptions.Single
 * parameter constructor create the exception instance with customized message
 * which in turn appends the more information like exception code and exception
 * type for system category exception by calling super class constructor.
 * Customized exception code and type can also be provided by using
 * parameterized constructor.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatSystemException extends AzureChatException {

	/**
	 * Generated serial version id for AZChatSystemException.
	 */
	private static final long serialVersionUID = 345660950532946168L;

	/**
	 * Parameterized constructor to generate custom azchat system exceptions.
	 * 
	 * @param excpMsg
	 */
	public AzureChatSystemException(String excpMsg) {
		super(AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
				AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg);
	}

	/**
	 * Parameterized constructor to generate custom azchat business exceptions.
	 * Exception code is the exception severity level mainly Business and System
	 * exception and exception type is exception class identifies exception
	 * area.
	 * 
	 * @param excpCode
	 * @param excpType
	 * @param excpMsg
	 */
	public AzureChatSystemException(String excpCode, String excpType,
			String excpMsg) {
		super(excpCode, excpType, excpMsg);
	}

}
