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

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.services.BaseService;

/**
 * LoginController for azchat.This controller redirect user to the login page
 * and also handle the login flow.On Successful login corresponding service logs
 * the user details.
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

	/**
	 * Render Index page.
	 * 
	 * @return
	 */
	@RequestMapping(AzureChatConstants.FROM_PAGE_INDEX)
	public String goIndex() {
		LOGGER.info("[LoginController][goLogin] start");

		LOGGER.info("[LoginController][goLogin] end");
		return AzureChatConstants.VIEW_NAME_INDEX;
	}

	/**
	 * This method process the login information of the user.On Successful
	 * verification from Azure ACS service user redirected to the Home/Landing
	 * page of the application.
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
				LOGGER.debug("New user : "+userBean.isNewUser());
				return processResults(AzureChatConstants.VIEW_NAME_HOME, model,
						userBean);
			} else {
				LOGGER.debug("New user : "+userBean.isNewUser());
				return processResults(AzureChatConstants.VIEW_NAME_REG, model,
						userBean);
			}
		}
		LOGGER.debug("Has errors : "+userBean.hasErrors());
		LOGGER.info("[LoginController][goLogin] end");
		return processResults(AzureChatConstants.VIEW_NAME_INDEX, model,
				userBean);
	}

	/**
	 * This method invalidates the session and logout user to the index page.
	 * 
	 * @param request
	 * @return
	 */
/*	@RequestMapping(name = AzureChatConstants.FROM_PAGE_INDEX, params = "isLogout=true")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		for (Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		request.getSession().invalidate();
		return AzureChatConstants.VIEW_NAME_INDEX;
	}*/
}
