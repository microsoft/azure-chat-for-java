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
