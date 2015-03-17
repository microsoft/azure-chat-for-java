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
