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

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStartupUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.services.BaseService;

/**
 * LoginController for AzChat.This controller redirect user to the login page
 * and also handle the login flow.On Successful login corresponding service logs
 * the user details in azure database.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Controller
public class LoginController extends BaseController {

	private static final Logger LOGGER = LogManager
			.getLogger(LoginController.class);

	@Autowired
	@Qualifier("loginService")
	private BaseService loginService;

	@Autowired
	AzureChatStartupUtils azureChatStartupUtils;

	/**
	 * Render Index page.
	 * 
	 * @return
	 * @throws AzureChatException
	 */
	@RequestMapping(AzureChatConstants.FROM_PAGE_INDEX)
	public ModelAndView goIndex() throws AzureChatException {
		LOGGER.info("[LoginController][goLogin] start");
		if (null != azureChatStartupUtils.getStartupErrors()) {
			return processResults(AzureChatConstants.VIEW_NAME_ERROR,
					new ModelMap(), azureChatStartupUtils.getStartupErrors());
		}
		LOGGER.info("[LoginController][goLogin] end");
		return new ModelAndView(AzureChatConstants.VIEW_NAME_INDEX);
	}

	/**
	 * This method process the login information of the user.On Successful
	 * verification from Azure ACS service user redirected to the Home/Landing
	 * page of the application.New or first time users are redirected to the
	 * Registration page for first time activity.
	 * 
	 * @param model
	 * @param loginBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(name = AzureChatConstants.FROM_PAGE_LOGIN, method = RequestMethod.GET)
	public ModelAndView doLogin(HttpServletRequest request, ModelMap model,
			UserBean userBean) throws Exception {
		LOGGER.info("[LoginController][goLogin] start");
		userBean.setSamplToken((String) request.getAttribute("ACSSAML"));
		userBean.setServiceAction(ServiceActionEnum.LOGIN);
		userBean = (UserBean) loginService.invokeService(userBean);
		model.put(AzureChatConstants.USER_BEAN, userBean);
		if (!userBean.hasErrors()) {
			if (!userBean.isNewUser()) {
				LOGGER.debug("New user : " + userBean.isNewUser());
				return processResults(AzureChatConstants.VIEW_NAME_HOME, model,
						userBean);
			} else {
				LOGGER.debug("New user : " + userBean.isNewUser());
				return processResults(AzureChatConstants.VIEW_NAME_REG, model,
						userBean);
			}
		}
		LOGGER.debug("Has errors : " + userBean.hasErrors());
		LOGGER.info("[LoginController][goLogin] end");
		return processResults(AzureChatConstants.VIEW_NAME_INDEX, model,
				userBean);
	}

}
