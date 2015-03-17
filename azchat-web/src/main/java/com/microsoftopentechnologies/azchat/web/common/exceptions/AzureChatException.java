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

/**
 * Azure exception hierarchy parent class used to raise the application specific exception.This class
 * can hold the more information about exception like exception code and type
 * with customized exception message. 
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatException extends Exception {

	/**
	 * Generated Serial version id for AzureChatException
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
