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
package com.microsoftopentechnologies.azchat.web.common.exceptions;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;

/**
 * This class can be used to raise the application specific exceptions.Single
 * parameter constructor create the exception instance with customized message
 * and call the super constructor with default information for exception code
 * and exception type.This class specifically set the exception code and type as
 * a system exception category. Customized exception code and type can also be
 * provided by using parameterized constructor.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatSystemException extends AzureChatException {

	/**
	 * Generated serial version id for AzureChatSystemException.
	 */
	private static final long serialVersionUID = 345660950532946168L;

	/**
	 * Parameterized constructor to generate custom AzChat system exceptions.
	 * 
	 * @param excpMsg
	 */
	public AzureChatSystemException(String excpMsg) {
		super(AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
				AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg);
	}

	/**
	 * Parameterized constructor to generate custom AzChat business exceptions.
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
