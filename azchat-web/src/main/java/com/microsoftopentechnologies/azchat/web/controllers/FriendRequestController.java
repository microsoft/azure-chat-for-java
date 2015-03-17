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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.ServiceActionEnum;
import com.microsoftopentechnologies.azchat.web.data.beans.FriendRequestBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.services.BaseService;

/**
 * This controller handles the Friend Request Add/Delete,Search Friends and
 * Retrieve and Update friend status requests.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
@Controller
public class FriendRequestController extends BaseController {

	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestController.class);

	@Autowired
	@Qualifier("friendRequestService")
	private BaseService friendRequestService;

	/**
	 * This method handles the get friend list request and response the JSON.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_FINDFRIENDS, method = RequestMethod.GET)
	public @ResponseBody UserBean getFriendList(@RequestParam String firstName) {
		LOGGER.info("[FriendRequestController][getFriendList] start");
		UserBean userBean = new UserBean();
		userBean.setFirstName(firstName);
		userBean.setServiceAction(ServiceActionEnum.FIND_FRIENDS);
		userBean = (UserBean) friendRequestService.invokeService(userBean);
		LOGGER.info("[FriendRequestController][getFriendList] end");
		return userBean;
	}

	/**
	 * This method handles the friend request status request and response.This
	 * method also parse the response to the JSON.
	 * 
	 * @param request
	 * @return JSON friendRequestBean
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_FRIND_STATUS, method = RequestMethod.GET)
	public @ResponseBody FriendRequestBean getFriendStatus(
			HttpServletRequest request) {
		LOGGER.info("[FriendRequestController][getFriendStatuRs] start");
		FriendRequestBean friendRequestBean = new FriendRequestBean();
		friendRequestBean.setFriendID(request.getParameter("friendID"));
		friendRequestBean.setUserID(request.getParameter("loggedInUserID"));
		friendRequestBean.setServiceAction(ServiceActionEnum.FRIEND_STATUS);
		friendRequestBean = (FriendRequestBean) friendRequestService
				.invokeService(friendRequestBean);
		LOGGER.info("[FriendRequestController][getFriendStatus] end");
		return friendRequestBean;
	}

	/**
	 * This method handles the add friend request and response.This method also
	 * parse the response to the JSON.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_ADD_FRIEND, method = RequestMethod.GET)
	public @ResponseBody FriendRequestBean addFriend(HttpServletRequest request) {
		LOGGER.info("[FriendRequestController][addFriend] start");
		FriendRequestBean friendRequestBean = new FriendRequestBean();
		friendRequestBean.setFriendID(request.getParameter("friendID"));
		friendRequestBean.setUserID(request.getParameter("loggedInUserID"));
		friendRequestBean.setFriendName(request.getParameter("friendName"));
		friendRequestBean.setFriendPhotoUrl(request.getParameter("photoUrl"));
		friendRequestBean.setServiceAction(ServiceActionEnum.ADD_FRIEND);
		friendRequestBean = (FriendRequestBean) friendRequestService
				.invokeService(friendRequestBean);
		if (!friendRequestBean.hasErrors()) {
			friendRequestBean.setMsg(AzureChatConstants.SUCCESS_MSG_ADD_FRIEND);
		}
		LOGGER.info("[FriendRequestController][addFriend] end");
		return friendRequestBean;
	}

	/**
	 * This method handles the pending friend request and response.This method
	 * also parse the response to the JSON.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_GET_PENDING_FRND, method = RequestMethod.GET)
	public @ResponseBody UserBean getPendingFriendList(
			HttpServletRequest request) {
		LOGGER.info("[FriendRequestController][getPendingFriendList] start");
		UserBean userBean = new UserBean();
		userBean.setUserID(request.getParameter("loggedInUserID"));
		userBean.setServiceAction(ServiceActionEnum.GET_PENDING_FRIENDS);
		userBean = (UserBean) friendRequestService.invokeService(userBean);
		LOGGER.info("[FriendRequestController][getPendingFriendList] end");
		return userBean;
	}

	/**
	 * This method handles the update friend request status request and
	 * response.This method also parse the response to the JSON.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_UPDATE_PENDING_FRNDREQ, method = RequestMethod.POST)
	public @ResponseBody FriendRequestBean updateFriendReqStatus(
			HttpServletRequest request) {
		LOGGER.info("[FriendRequestController][updateFriendReqStatus] start");
		FriendRequestBean frndBean = new FriendRequestBean();
		frndBean.setUserID(request.getParameter("loggedInUserID"));
		frndBean.setStatus(request.getParameter("status"));
		frndBean.setFriendID(request.getParameter("friendID"));
		frndBean.setFriendName(request.getParameter("friendName"));
		frndBean.setFriendPhotoUrl(request.getParameter("photoUrl"));
		frndBean.setServiceAction(ServiceActionEnum.UPDATE_FRIENDREQ_STATUS);
		frndBean = (FriendRequestBean) friendRequestService
				.invokeService(frndBean);
		LOGGER.info("[FriendRequestController][updateFriendReqStatus] end");
		return frndBean;
	}

	/**
	 * This method handles the pending friend request count request and
	 * response.This method also parse the response to the JSON.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AzureChatConstants.FROM_PAGE_PENDING_FRIEND_CNT, method = RequestMethod.POST)
	public @ResponseBody UserBean getPendingFriendRequestCount(
			HttpServletRequest request) {
		LOGGER.info("[FriendRequestController][updateFriendReqStatus] start");
		UserBean userBean = new UserBean();
		userBean.setUserID(request.getParameter("userID"));
		userBean.setServiceAction(ServiceActionEnum.GET_PENDING_FRIEND_COUNT);
		userBean = (UserBean) friendRequestService.invokeService(userBean);
		LOGGER.info("[FriendRequestController][updateFriendReqStatus] end");
		return userBean;
	}
}