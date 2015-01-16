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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserMessageListBean;
import com.microsoftopentechnologies.azchat.web.services.BaseService;

/**
 * This controller handles the User Content share requests.
 *
 */
@Controller
public class ContentShareController extends BaseController {

	private static final Logger LOGGER = LogManager
			.getLogger(ContentShareController.class);

	@Autowired
	@Qualifier("contentShareService")
	private BaseService contentShareService;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AzureChatException 
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_CONTENT_SHARE, method = RequestMethod.POST)
	public @ResponseBody UserBean updateUserStatus(
			MultipartHttpServletRequest request) throws AzureChatException {
		LOGGER.info("[ContentShareController][updateUserStatus] start");
		UserBean userBean = new UserBean();
		populateUserBean(userBean, request);
		userBean.setServiceAction(ServiceActionEnum.CONTENT_SHARE);
		userBean = (UserBean) contentShareService.invokeService(userBean);
		if (!userBean.hasErrors()) {
			userBean.setMultipartFile(null);
			if (null != userBean.getUserMessageListBean()) {
				userBean.setUserMessageListBean(null);
			}
			userBean.setMsg(AzureChatConstants.SUCCESS_MSG_CONTENT_SHARE);
		}
		LOGGER.info("[ContentShareController][updateUserStatus] end");
		return userBean;

	}

	/**
	 * This method populates the user bean value from multi-part request
	 * 
	 * @param userBean
	 * @param request
	 * @throws AzureChatException
	 */
	private void populateUserBean(UserBean userBean,
			MultipartHttpServletRequest request) throws AzureChatException {
		UserMessageListBean userMessageListBean = new UserMessageListBean();
		MultipartFile photoVedioFile = request.getFile("mediaPhoto");
		if (null != photoVedioFile) {
			if (AzureChatUtils.getMegaBytes(photoVedioFile.getSize()) > AzureChatUtils
					.getNumbers(AzureChatUtils
							.getProperty(AzureChatConstants.MAX_UPLOAD_SIZE_KEY))) {
				AzureChatUtils.populateErrors(userBean,
						AzureChatConstants.EXCEP_CODE_BUSSINESS_EXCEPTION,
						AzureChatConstants.EXCEP_MSG_UPLOAD_SIZE_EXCEEDS);
			}
			userMessageListBean.setPhotoVedioFile(photoVedioFile);
			userMessageListBean.setMediaType(photoVedioFile.getContentType());
		}
		userMessageListBean.setExpiryTime(request.getParameter("expiryTime"));
		userMessageListBean.setMsgText(request.getParameter("msgText"));
		userBean.setUserMessageListBean(userMessageListBean);
		userBean.setUserID(request.getParameter("logedInUserID"));
	}	

}
