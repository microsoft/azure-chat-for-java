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
package com.microsoftopentechnologies.azchat.web.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorListBean;

/**
 * This class stores the application startup data and errors in application
 * context. This class provides the operations to parse the input exception
 * object to the error bean and add the error bean to the error list.errorList
 * is set to the errorListBean and finally errorListBean is set to the base
 * bean.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Component
public class AzureChatStartupUtils {

	/**
	 * This method add the startup errors to the application context.This method
	 * takes exception object as a input parse it to error bean,add errorBean to
	 * the errorList.Set errorList to the errorListBean and finally set
	 * errorListBean to the baseBean and store the baseBean to the application
	 * context.
	 * 
	 * @param e
	 */
	public void populateStartupErrors(AzureChatException e) {
		BaseBean startupErrors = (BaseBean) AzureChatAppCtxUtils
				.getApplicationContext().getAttribute(
						AzureChatConstants.STARTUP_ERRORS);
		if (null != startupErrors) {
			populateErrors(startupErrors, e);
		} else {
			startupErrors = new BaseBean();
			populateErrors(startupErrors, e);
		}
		setStartupErrors(startupErrors);
	}

	/**
	 * This method retrieve the errorListBean and errorList from the input
	 * baseBean and parse and populate the exceptions into the base bean.
	 * 
	 * @param startupErrors
	 * @param e
	 */
	private void populateErrors(BaseBean startupErrors, AzureChatException e) {
		startupErrors.setHasErrors(true);
		ErrorListBean errorListBean = startupErrors.getErrorList() != null ? startupErrors
				.getErrorList() : new ErrorListBean();
		List<ErrorBean> errorList = errorListBean.getErrorList() != null ? errorListBean
				.getErrorList() : new ArrayList<ErrorBean>();
		errorListBean
				.setExcpCode(AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION);
		addErrors(errorList, e);
		errorListBean.setErrorList(errorList);
		startupErrors.setErrorList(errorListBean);
	}

	/**
	 * This method parse the exception object to the errorBean and add the
	 * errorBean to the input errorList.
	 * 
	 * @param errorList
	 * @param e
	 */
	private void addErrors(List<ErrorBean> errorList, AzureChatException e) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setExcpMsg(e.getExcpMsg());
		errorBean.setExcpType(e.getExcpType());
		errorList.add(errorBean);
	}

	/**
	 * This method add the application startup errors to the application
	 * context.
	 * 
	 * @return baseBean
	 */
	public void setStartupErrors(BaseBean startupErrors) {
		AzureChatAppCtxUtils.getApplicationContext().setAttribute(
				AzureChatConstants.STARTUP_ERRORS, startupErrors);
	}

	/**
	 * This method retrieve the application startup errors from application
	 * context.
	 * 
	 * @return baseBean
	 */
	public BaseBean getStartupErrors() {
		if (null != AzureChatAppCtxUtils.getApplicationContext().getAttribute(
				AzureChatConstants.STARTUP_ERRORS)) {
			return (BaseBean) AzureChatAppCtxUtils.getApplicationContext()
					.getAttribute(AzureChatConstants.STARTUP_ERRORS);
		}
		return null;
	}
}
