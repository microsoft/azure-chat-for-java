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
package com.microsoftopentechnologies.azchat.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;

/**
 * BaseController for azchat application includes operations common amongst the
 * controllers.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Controller
public class BaseController {

	private static final Logger LOGGER = LogManager
			.getLogger(BaseController.class);

	/**
	 * This method take model bean,view name and model map as a input,check in
	 * the model bean for the errors. If errors are of type system errors , it
	 * modifies view name to redirect user to the error page otherwise view name
	 * remain intact and user redirected to the intended view.
	 * 
	 * @param viewName
	 * @param model
	 * @param baseBean
	 * @return
	 */
	public ModelAndView processResults(String viewName, ModelMap model,
			BaseBean baseBean) {
		ModelAndView modelView = null;
		if (baseBean.hasErrors() && null != baseBean.getErrorList()) {
			String excpCode = baseBean.getErrorList().getExcpCode();
			model.put(AzureChatConstants.BASE_BEAN, baseBean);
			if (AzureChatConstants.EXCEP_CODE_BUSSINESS_EXCEPTION
					.equalsIgnoreCase(excpCode)
					|| AzureChatConstants.EXCEP_CODE_JAVA_EXCEPTION
							.equalsIgnoreCase(excpCode)) {
				modelView = new ModelAndView(viewName, model);
			} else if (AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION
					.equalsIgnoreCase(excpCode)) {
				modelView = new ModelAndView(
						AzureChatConstants.VIEW_NAME_ERROR, model);
			}

		} else {
			modelView = new ModelAndView(viewName, model);
		}
		return modelView;

	}

	/**
	 * This is a default exception handler method.All the unhandled exceptions
	 * in the Azure chat application would be redirected and shown on the
	 * application error page with the default information available with the
	 * exception object.
	 * 
	 * @param e
	 * @return error view
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAzureChatExceptions(Exception e) {
		LOGGER.info("[BaseController][handleAzureChatExceptions] start");
		BaseBean baseBean = new BaseBean();
		baseBean.setHasErrors(true);
		String excpCode = AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION;
		String excpMsg = getExcpMsg(e);
		AzureChatUtils.populateErrors(baseBean, excpCode, excpMsg);
		LOGGER.info("[BaseController][handleAzureChatExceptions] end");
		return processResults(AzureChatConstants.VIEW_NAME_ERROR,
				new ModelMap(), baseBean);

	}

	/**
	 * This method extracts the exception message from the exception object.If
	 * not found, set the default exception message.
	 * 
	 * @param e
	 * @return
	 */
	private String getExcpMsg(Exception e) {
		String excpMsg = null;
		if (e instanceof AzureChatBusinessException) {
			excpMsg = ((AzureChatBusinessException) e).getExcpMsg();
			if (excpMsg == null) {
				excpMsg = e.getMessage();
			}
		} else if (e instanceof AzureChatSystemException) {
			excpMsg = ((AzureChatSystemException) e).getExcpMsg();
			if (excpMsg == null) {
				excpMsg = e.getMessage();
			}
		} else {
			excpMsg = e.getMessage();
		}
		return excpMsg;
	}

}
