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
package com.microsoftopentechnologies.azchat.web.timertasks;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.QueueRequestDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.UserDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.UserPreferenceDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserPreferenceDAOImpl;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserPreferenceEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.FriendRequestBean;

/**
 * This class handles email notifications for new friend requests by scanning
 * email notification queue periodically.
 * 
 * @author Rupesh_Shirude
 *
 */
@Component
public class EmailNotificationHandler {
	private static final Logger LOGGER = LogManager
			.getLogger(EmailNotificationHandler.class);

	/**
	 * This method is used to send mail about friend request notification. 
	 * TODO : Currently this method does not support email sending it just logs the
	 * message using log4j implementation.If you want to send the email's you
	 * need to hook corresponding code.
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 180000)
	private void processFriendRequestNotifications() throws Exception {
		LOGGER.info("[EmailNotificationHandler][sendMailFriendRequestNotifications] start");
		Set<String> friendRequests = null;
		QueueRequestDAO queueRequestDAO = new QueueRequestDAOImpl();
		UserPreferenceDAO userPreferenceDAO = new UserPreferenceDAOImpl();
		try {
			friendRequests = queueRequestDAO
					.getAllMessagesFromQueue(AzureChatConstants.QUEUE_NAME_EMAIL_NOTIFICATION);
		} catch (Exception e1) {
			LOGGER.error("Exception occurred while retrieving email notification information from queue. Exception Messages :  "
					+ e1.getMessage());
		}
		for (String friendReqNotfcnInQue : friendRequests) {
			JSONObject jsonObject = new JSONObject(friendReqNotfcnInQue);
			FriendRequestBean friendRequestBean = convertToObject(jsonObject);
			List<UserPreferenceEntity> userPreferenceEntities = userPreferenceDAO
					.getUserPreferenceEntity(Integer.parseInt(friendRequestBean
							.getFriendID()));
			for (UserPreferenceEntity entity : userPreferenceEntities) {
				if (entity.getPreferenceId() == 1) {
					UserDAO userDAO = new UserDAOImpl();
					UserEntity user = userDAO.getUserDetailsByUserId(Integer
							.parseInt(friendRequestBean.getUserID()));
					// Send mail notification here to friend
					StringBuilder logstat = new StringBuilder(
							"Friend request sent by user id: ");
					logstat.append(user.getUserID());
					logstat.append("(").append(user.getFirstName())
							.append(") TO ");
					logstat.append(friendRequestBean.getFriendName()).append(
							"0.");

					LOGGER.info(logstat);

				}
			}

		}
		LOGGER.info("[EmailNotificationHandler][sendMailFriendRequestNotifications] end");
	}

	/**
	 * This method converts JSON object to the FriendRequestBean object.
	 * 
	 * @param jsonObject
	 * @return
	 */
	private FriendRequestBean convertToObject(JSONObject jsonObject) {
		FriendRequestBean friendRequestBean = new FriendRequestBean();
		try {
			friendRequestBean.setUserID(jsonObject.getString("userID"));
			friendRequestBean.setFriendID(jsonObject.getString("friendID"));
			friendRequestBean.setFriendName(jsonObject.getString("friendName"));
			friendRequestBean.setFriendPhotoUrl(jsonObject
					.getString("friendPhotoUrl"));
			friendRequestBean.setStatus(jsonObject.getString("status"));
		} catch (JSONException e) {
			LOGGER.error("Exception while converting to Object. "
					+ e.getMessage());
		}
		return friendRequestBean;
	}

}
