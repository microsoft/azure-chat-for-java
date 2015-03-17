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

import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;

/**
 * BaseBean for all AzChat data beans.This bean holds the data common across the
 * application.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class BaseBean implements Serializable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -985948118280242986L;
	private ErrorListBean errorList;
	private boolean hasErrors = false;
	private ServiceActionEnum serviceAction;
	private String msg;

	/**
	 * @return the errorList
	 */
	public ErrorListBean getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList
	 *            the errorList to set
	 */
	public void setErrorList(ErrorListBean errorList) {
		this.errorList = errorList;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return this.hasErrors;
	}

	/**
	 * 
	 * @param hasErrors
	 */
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	/**
	 * @return the serviceAction
	 */
	public ServiceActionEnum getServiceAction() {
		return serviceAction;
	}

	/**
	 * @param serviceAction
	 *            the serviceAction to set
	 */
	public void setServiceAction(ServiceActionEnum serviceAction) {
		this.serviceAction = serviceAction;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
