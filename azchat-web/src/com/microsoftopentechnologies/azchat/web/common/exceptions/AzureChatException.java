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

/**
 * Parent class used to raise the application specific exception.This class
 * can hold the more information about exception like exception code and type
 * with customized exception message. 
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatException extends Exception {

	/**
	 * Generated Serial version id for AZChatExceptions
	 */
	private static final long serialVersionUID = 8609243190793948020L;
	private String excpCode;
	private String excpType;
	private String excpMsg;

	/**
	 * Parameterized constructor calling superclass with the customized
	 * exception message.
	 * 
	 * @param msg
	 */
	public AzureChatException(String msg) {
		super(msg);
	}

	/**
	 * Parameterized constructor calling superclass with the customized message
	 * and having additional information about exception code and exception
	 * type. Exception code is the exception severity level mainly Business and
	 * System exception and exception type is exception class identifies
	 * exception area.
	 * 
	 * 
	 * @param excpCode
	 * @param excpType
	 * @param excpMsg
	 */
	public AzureChatException(String excpCode, String excpType, String excpMsg) {
		super(excpMsg);
		this.excpCode = excpCode;
		this.excpType = excpType;
		this.excpMsg = excpMsg;
	}

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
