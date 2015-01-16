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

package com.microsoftopentechnologies.azchat.web.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;

@Service("friendRequestDAO")
public class FriendRequestDAOImpl implements FriendRequestDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);
	private static final String FRIEND_REQ_TBL_NAME = "FriendRequest";

	@Autowired
	private UserDAO userDao;

	public void addFriendRequest(String userID, String friendID,
			String friendName, String friendProfileBlobURL, String requestStatus)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][addFriendRequest]         start ");
		LOGGER.debug("User Details    " + userID);
		if (requestStatus
				.equalsIgnoreCase(AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL)) {
			FriendRequestEntity friendRequestSent = new FriendRequestEntity(
					userID, friendID);
			friendRequestSent.setFriendName(friendName);
			friendRequestSent.setFriendProfileBlobURL(friendProfileBlobURL);
			friendRequestSent.setStatus(AzureChatConstants.FRIEND_REQUEST_SENT);
			AzureChatStorageUtils.insertOrReplaceEntity(FRIEND_REQ_TBL_NAME,
					friendRequestSent);

			UserEntity entity = userDao.getUserDetailsByUserId(Integer
					.parseInt(userID));

			FriendRequestEntity friendRequestPending = new FriendRequestEntity(
					friendID, userID);
			friendRequestPending.setFriendName(entity.getFirstName() + " "
					+ entity.getLastName());
			friendRequestPending.setFriendProfileBlobURL(entity
					.getPhotoBlobUrl());
			friendRequestPending
					.setStatus(AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL);
			AzureChatStorageUtils.insertOrReplaceEntity(FRIEND_REQ_TBL_NAME,
					friendRequestPending);
		} else {
			FriendRequestEntity friendRequestSent = new FriendRequestEntity(
					userID, friendID);
			friendRequestSent.setFriendName(friendName);
			friendRequestSent.setFriendProfileBlobURL(friendProfileBlobURL);
			friendRequestSent.setStatus(requestStatus);
			AzureChatStorageUtils.insertOrReplaceEntity(FRIEND_REQ_TBL_NAME,
					friendRequestSent);
		}
		LOGGER.info("[FriendRequestDAOImpl][addFriendRequest]         end ");

	}

	public List<FriendRequestEntity> getFriendListForUser(String userID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getFriendListForUser]         start ");
		LOGGER.debug("User Details    " + userID);
		List<FriendRequestEntity> friendRequestEntities = new ArrayList<FriendRequestEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userID);
		String pendingApprovalFilter = TableQuery.generateFilterCondition(
				"Status", QueryComparisons.EQUAL,
				AzureChatConstants.FRIEND_REQUEST_APPROVED);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, pendingApprovalFilter);
		TableQuery<FriendRequestEntity> friendListQuery = TableQuery.from(
				FriendRequestEntity.class).where(combinedFilter);
		for (FriendRequestEntity entity : AzureChatStorageUtils
				.getTableReference(FRIEND_REQ_TBL_NAME)
				.execute(friendListQuery)) {
			friendRequestEntities.add(entity);
		}
		LOGGER.info("[FriendRequestDAOImpl][getFriendListForUser]         end ");
		return friendRequestEntities;

	}

	public List<FriendRequestEntity> getPendingFriendRequestsForUser(
			String userID) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsForUser]         start ");
		LOGGER.debug("User Details    " + userID);
		List<FriendRequestEntity> friendRequestEntities = new ArrayList<FriendRequestEntity>();
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userID);
		String pendingApprovalFilter = TableQuery.generateFilterCondition(
				"Status", QueryComparisons.EQUAL,
				AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, pendingApprovalFilter);
		TableQuery<FriendRequestEntity> pendingFriendRequestsQuery = TableQuery
				.from(FriendRequestEntity.class).where(combinedFilter);
		for (FriendRequestEntity entity : AzureChatStorageUtils
				.getTableReference(FRIEND_REQ_TBL_NAME).execute(
						pendingFriendRequestsQuery)) {
			friendRequestEntities.add(entity);
		}
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsForUser]         end ");
		return friendRequestEntities;
	}

	public boolean isFriendRequestApproved(String userID, String friendID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][isFriendRequestApproved]         start ");
		LOGGER.debug("User Details    " + userID);
		TableOperation tableOperation = TableOperation.retrieve(userID,
				friendID, FriendRequestEntity.class);
		FriendRequestEntity friendRequestEntity = AzureChatStorageUtils
				.getTableReference(FRIEND_REQ_TBL_NAME).execute(tableOperation)
				.getResultAsType();
		if (friendRequestEntity != null
				&& AzureChatConstants.FRIEND_REQUEST_APPROVED
						.equals(friendRequestEntity.getStatus())) {
			LOGGER.info("Both are friend..");
			LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest]          end ");
			return true;
		} else {
			LOGGER.info("Both are not friend..");
			LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest]          end ");
			return false;
		}
	}

	public void acceptFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest]         start ");
		LOGGER.debug("User Details    " + userID);
		addFriendRequest(userID, friendID, userName, userProfileBlobURL,
				AzureChatConstants.FRIEND_REQUEST_APPROVED);
		UserEntity entity = userDao.getUserDetailsByUserId(Integer
				.parseInt(userID));
		addFriendRequest(friendID, userID,
				entity.getFirstName() + " " + entity.getLastName(),
				entity.getPhotoBlobUrl(),
				AzureChatConstants.FRIEND_REQUEST_APPROVED);
		LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest]          end ");
	}

	public void rejectFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][rejectFriendRequest]         start ");
		LOGGER.debug("User Details    " + userID);
		addFriendRequest(userID, friendID, userName, userProfileBlobURL,
				AzureChatConstants.FRIEND_REQUEST_NO_FRIEND);
		UserEntity entity = userDao.getUserDetailsByUserId(Integer
				.parseInt(userID));
		addFriendRequest(friendID, userID,
				entity.getFirstName() + " " + entity.getLastName(),
				entity.getPhotoBlobUrl(),
				AzureChatConstants.FRIEND_REQUEST_REJECTED);
		LOGGER.info("[FriendRequestDAOImpl][rejectFriendRequest]          end ");
	}

	public FriendRequestEntity getFriendStatusForUserWithFriend(String userID,
			String friendId) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend]         start ");
		LOGGER.debug("User Details    " + userID);
		if (!userID.equalsIgnoreCase(friendId)) {

			List<FriendRequestEntity> friendRequestEntities = new ArrayList<FriendRequestEntity>();
			String partitionFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
					userID);
			String rowFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
					friendId);
			// String pendingApprovalFilter =
			// TableQuery.generateFilterCondition("Status",
			// QueryComparisons.EQUAL, AZChatConstants.FRIEND_REQUEST_APPROVED);
			// String combinedFilter =
			// TableQuery.combineFilters(partitionFilter, Operators.AND,
			// pendingApprovalFilter);
			String combinedFilter = TableQuery.combineFilters(partitionFilter,
					Operators.AND, rowFilter);
			TableQuery<FriendRequestEntity> friendListQuery = TableQuery.from(
					FriendRequestEntity.class).where(combinedFilter);
			for (FriendRequestEntity entity : AzureChatStorageUtils
					.getTableReference(FRIEND_REQ_TBL_NAME).execute(
							friendListQuery)) {
				friendRequestEntities.add(entity);
			}
			if (!AzureChatUtils.isEmpty(friendRequestEntities)) {
				LOGGER.info("friendRequestEntities not empty ...");
				LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend]          end ");
				return friendRequestEntities.get(0);
			} else {
				LOGGER.info("friendRequestEntities empty : building noFriend object...");
				FriendRequestEntity noFriendResponse = new FriendRequestEntity();
				noFriendResponse
						.setStatus(AzureChatConstants.FRIEND_REQUEST_NO_FRIEND);
				noFriendResponse.setPartitionKey(userID);
				noFriendResponse.setRowKey(friendId);
				UserEntity entity = userDao.getUserDetailsByUserId(Integer
						.parseInt(friendId));
				if (null != entity) {
					populateFriendPhotoUrl(noFriendResponse,entity);
				}
				LOGGER.info("[FriendRnoFriendResponseequestDAOImpl][getFriendStatusForUserWithFriend]          end ");
				return noFriendResponse;
			}
		} else {
			LOGGER.info("same user searched as login user...");
			FriendRequestEntity noFriendResponse = new FriendRequestEntity();
			noFriendResponse.setStatus(AzureChatConstants.FRIEND_REQUEST_YOU);
			noFriendResponse.setPartitionKey(userID);
			noFriendResponse.setRowKey(friendId);
			UserEntity entity = userDao.getUserDetailsByUserId(Integer
					.parseInt(friendId));
			if (null != entity) {
				populateFriendPhotoUrl(noFriendResponse,entity);
			}
			LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend]          end ");
			return noFriendResponse;
		}

	}

	@Override
	public Integer getPendingFriendRequestsCountForUser(String userID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsCountForUser]         start ");
		LOGGER.debug("User Details    " + userID);
		int count = 0;
		String partitionFilter = TableQuery.generateFilterCondition(
				AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
				userID);
		String pendingApprovalFilter = TableQuery.generateFilterCondition(
				"Status", QueryComparisons.EQUAL,
				AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL);
		String combinedFilter = TableQuery.combineFilters(partitionFilter,
				Operators.AND, pendingApprovalFilter);
		TableQuery<FriendRequestEntity> pendingFriendRequestsQuery = TableQuery
				.from(FriendRequestEntity.class).where(combinedFilter);
		for (FriendRequestEntity entity : AzureChatStorageUtils
				.getTableReference(FRIEND_REQ_TBL_NAME).execute(
						pendingFriendRequestsQuery)) {
			count++;
		}
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsCountForUser]          end ");
		return count;
	}

	/**
	 * This method populates the friend entity with user entities photo url and
	 * name is the first name + Last name.
	 * 
	 * @param firend
	 * @param user
	 */
	private void populateFriendPhotoUrl(FriendRequestEntity friend,
			UserEntity user) {
		friend.setFriendName(user.getFirstName() + " " + user.getLastName());
		friend.setFriendProfileBlobURL(user.getPhotoBlobUrl());
	}

}