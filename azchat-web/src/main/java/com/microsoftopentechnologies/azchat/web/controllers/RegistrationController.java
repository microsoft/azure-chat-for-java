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

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.services.BaseService;

/**
 * This controller handle the registration and profile update request's from
 * user.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Controller
public class RegistrationController extends BaseController {

	private static final Logger LOGGER = LogManager
			.getLogger(RegistrationController.class);
	@Autowired
	@Qualifier("registrationService")
	private BaseService registrationService;

	/**
	 * This method handles the user registration request and
	 * response.Successfull registration of the user will be redirected to the
	 * home/landing page.
	 * 
	 * @param userBean
	 * @param model
	 * @return
	 */
	@RequestMapping(name = AzureChatConstants.FROM_PAGE_REG, method = RequestMethod.POST)
	public ModelAndView doRegistration(UserBean userBean, ModelMap model) {
		LOGGER.info("[RegistrationController][doRegistration] start");
		userBean.setServiceAction(ServiceActionEnum.REGISTRATION);
		userBean = (UserBean) registrationService.invokeService(userBean);
		model.put(AzureChatConstants.USER_BEAN, userBean);
		if (!userBean.hasErrors()) {
			return processResults(AzureChatConstants.VIEW_NAME_HOME, model,
					userBean);
		}
		LOGGER.info("[[RegistrationController][doRegistration] end");
		return processResults(AzureChatConstants.VIEW_NAME_REG, model, userBean);
	}

	/**
	 * This method handles the update user profile request and parse the
	 * response into JSON format.
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_UPDATE_USR_PROF, method = RequestMethod.POST)
	public @ResponseBody UserBean updateUserProfile(
			MultipartHttpServletRequest request) throws IOException {
		UserBean userBean = new UserBean();
		populateUserBean(userBean, request);
		userBean.setServiceAction(ServiceActionEnum.UPDATE_USER_PROFILE);
		userBean = (UserBean) registrationService.invokeService(userBean);
		return userBean;
	}

	/**
	 * This method populate the userBean from ajax request.
	 * 
	 * @param userBean
	 * @param request
	 */
	private void populateUserBean(UserBean userBean,
			MultipartHttpServletRequest request) {
		userBean.setUserID(request.getParameter("loggedInUserID"));
		userBean.setFirstName(request.getParameter("firstName"));
		userBean.setLastName(request.getParameter("lastName"));
		userBean.setEmail(request.getParameter("email"));
		userBean.setMultipartFile(request.getFile("userProfileImage"));
		userBean.setCountryCD(request.getParameter("country"));
		userBean.setPhoneNo(request.getParameter("phone"));
		userBean.setNameID(request.getParameter("nameID"));
	}

}
