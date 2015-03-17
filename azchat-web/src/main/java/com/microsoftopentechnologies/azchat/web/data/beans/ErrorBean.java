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
