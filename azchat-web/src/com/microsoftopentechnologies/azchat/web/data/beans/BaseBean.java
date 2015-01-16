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

import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;

/**
 * BaseBean for azchat.Holda the data common across the application.
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
