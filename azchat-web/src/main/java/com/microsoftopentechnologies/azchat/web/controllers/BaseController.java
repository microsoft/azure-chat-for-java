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
 * BaseController for AzChat application includes operations common amongst the
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
