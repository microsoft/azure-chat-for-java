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

/**
 * Implementation class for the FriendRequestDAO.This class provides operations
 * for AzChat friend management like add,search and approve/reject friend
 * requests.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("friendRequestDAO")
public class FriendRequestDAOImpl implements FriendRequestDAO {
	private static final Logger LOGGER = LogManager
			.getLogger(FriendRequestDAOImpl.class);

	@Autowired
	private UserDAO userDao;

	/**
	 * This method provides functionality to add the friend request to the azure
	 * storage account.
	 */
	public void addFriendRequest(String userID, String friendID,
			String friendName, String friendProfileBlobURL, String requestStatus)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][addFriendRequest] start ");
		LOGGER.debug("User Details    " + userID + " Request Status : "
				+ requestStatus);
		if (requestStatus
				.equalsIgnoreCase(AzureChatConstants.FRIEND_REQUEST_PENDING_APPROVAL)) {
			FriendRequestEntity friendRequestSent = new FriendRequestEntity(
					userID, friendID);
			friendRequestSent.setFriendName(friendName);
			friendRequestSent.setFriendProfileBlobURL(friendProfileBlobURL);
			friendRequestSent.setStatus(AzureChatConstants.FRIEND_REQUEST_SENT);
			AzureChatStorageUtils
					.insertOrReplaceEntity(
							AzureChatConstants.TABLE_NAME_FRIEND_REQ,
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
			AzureChatStorageUtils.insertOrReplaceEntity(
					AzureChatConstants.TABLE_NAME_FRIEND_REQ,
					friendRequestPending);
		} else {
			FriendRequestEntity friendRequestSent = new FriendRequestEntity(
					userID, friendID);
			friendRequestSent.setFriendName(friendName);
			friendRequestSent.setFriendProfileBlobURL(friendProfileBlobURL);
			friendRequestSent.setStatus(requestStatus);
			AzureChatStorageUtils
					.insertOrReplaceEntity(
							AzureChatConstants.TABLE_NAME_FRIEND_REQ,
							friendRequestSent);
		}
		LOGGER.info("[FriendRequestDAOImpl][addFriendRequest] end ");

	}

	/**
	 * This method call the azure storage and azure SQL to fetch the registered
	 * users.
	 * 
	 * @return friendRequestEntities
	 */
	public List<FriendRequestEntity> getFriendListForUser(String userID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getFriendListForUser] start ");
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
				.getTableReference(AzureChatConstants.TABLE_NAME_FRIEND_REQ)
				.execute(friendListQuery)) {
			friendRequestEntities.add(entity);
		}
		LOGGER.info("[FriendRequestDAOImpl][getFriendListForUser] end ");
		return friendRequestEntities;

	}

	/**
	 * This method calls the azure storage and fetch the pending friend requests
	 * for the user.
	 * 
	 * @return friendRequestEntities
	 */
	public List<FriendRequestEntity> getPendingFriendRequestsForUser(
			String userID) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsForUser] start ");
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
				.getTableReference(AzureChatConstants.TABLE_NAME_FRIEND_REQ)
				.execute(pendingFriendRequestsQuery)) {
			friendRequestEntities.add(entity);
		}
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsForUser] end ");
		return friendRequestEntities;
	}

	/**
	 * Gets the status of friend request is approved or not
	 * 
	 * @param userID
	 * @param friendID
	 * @return true\false
	 */
	public boolean isFriendRequestApproved(String userID, String friendID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][isFriendRequestApproved] start ");
		LOGGER.debug("User Details    " + userID);
		TableOperation tableOperation = TableOperation.retrieve(userID,
				friendID, FriendRequestEntity.class);
		FriendRequestEntity friendRequestEntity = AzureChatStorageUtils
				.getTableReference(AzureChatConstants.TABLE_NAME_FRIEND_REQ)
				.execute(tableOperation).getResultAsType();
		if (friendRequestEntity != null
				&& AzureChatConstants.FRIEND_REQUEST_APPROVED
						.equals(friendRequestEntity.getStatus())) {
			LOGGER.debug("[FriendRequestDAOImpl][acceptFriendRequest] end ");
			return true;
		}
		LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest] end ");
		return false;
	}

	/**
	 * This method is used to accept friend request and create the friend
	 * relationship by adding the entry to the azure storage.
	 * 
	 * @param userID
	 * @param friendID
	 * @param userName
	 * @param userProfileBlobURL
	 */
	public void acceptFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest] start ");
		LOGGER.debug("User Details    " + userID);
		addFriendRequest(userID, friendID, userName, userProfileBlobURL,
				AzureChatConstants.FRIEND_REQUEST_APPROVED);
		UserEntity entity = userDao.getUserDetailsByUserId(Integer
				.parseInt(userID));
		addFriendRequest(friendID, userID,
				entity.getFirstName() + " " + entity.getLastName(),
				entity.getPhotoBlobUrl(),
				AzureChatConstants.FRIEND_REQUEST_APPROVED);
		LOGGER.info("[FriendRequestDAOImpl][acceptFriendRequest] end ");
	}

	/**
	 * This method is used to reject friend request and add the entries to the
	 * azure table.
	 * 
	 * @param userID
	 * @param friendID
	 * @param userName
	 * @param userProfileBlobURL
	 */
	public void rejectFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][rejectFriendRequest] start ");
		LOGGER.debug("User Details    " + userID);
		addFriendRequest(userID, friendID, userName, userProfileBlobURL,
				AzureChatConstants.FRIEND_REQUEST_NO_FRIEND);
		UserEntity entity = userDao.getUserDetailsByUserId(Integer
				.parseInt(userID));
		addFriendRequest(friendID, userID,
				entity.getFirstName() + " " + entity.getLastName(),
				entity.getPhotoBlobUrl(),
				AzureChatConstants.FRIEND_REQUEST_REJECTED);
		LOGGER.info("[FriendRequestDAOImpl][rejectFriendRequest] end ");
	}

	/**
	 * This method used to find friend request status by querying the azure
	 * storage.
	 * 
	 * @param userID
	 * @param friendId
	 */
	public FriendRequestEntity getFriendStatusForUserWithFriend(String userID,
			String friendId) throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend] start ");
		LOGGER.debug("User ID  :   " + userID);
		if (!userID.equalsIgnoreCase(friendId)) {

			List<FriendRequestEntity> friendRequestEntities = new ArrayList<FriendRequestEntity>();
			String partitionFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.PARTITION_KEY, QueryComparisons.EQUAL,
					userID);
			String rowFilter = TableQuery.generateFilterCondition(
					AzureChatConstants.ROW_KEY, QueryComparisons.EQUAL,
					friendId);
			String combinedFilter = TableQuery.combineFilters(partitionFilter,
					Operators.AND, rowFilter);
			TableQuery<FriendRequestEntity> friendListQuery = TableQuery.from(
					FriendRequestEntity.class).where(combinedFilter);
			for (FriendRequestEntity entity : AzureChatStorageUtils
					.getTableReference(AzureChatConstants.TABLE_NAME_FRIEND_REQ)
					.execute(friendListQuery)) {
				friendRequestEntities.add(entity);
			}
			if (!AzureChatUtils.isEmpty(friendRequestEntities)) {
				LOGGER.debug("User ID and Frined ID relationship found in azure storage.");
				LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend] end ");
				return friendRequestEntities.get(0);
			} else {
				LOGGER.debug("User ID and Friend ID relationship not found.Returning noFriend status.");
				FriendRequestEntity noFriendResponse = new FriendRequestEntity();
				noFriendResponse
						.setStatus(AzureChatConstants.FRIEND_REQUEST_NO_FRIEND);
				noFriendResponse.setPartitionKey(userID);
				noFriendResponse.setRowKey(friendId);
				UserEntity entity = userDao.getUserDetailsByUserId(Integer
						.parseInt(friendId));
				if (null != entity) {
					populateFriendPhotoUrl(noFriendResponse, entity);
				}
				LOGGER.info("[FriendRnoFriendResponseequestDAOImpl][getFriendStatusForUserWithFriend] end ");
				return noFriendResponse;
			}
		} else {
			LOGGER.debug("User ID and Friend ID is same.User is queried self information.");
			FriendRequestEntity noFriendResponse = new FriendRequestEntity();
			noFriendResponse.setStatus(AzureChatConstants.FRIEND_REQUEST_YOU);
			noFriendResponse.setPartitionKey(userID);
			noFriendResponse.setRowKey(friendId);
			UserEntity entity = userDao.getUserDetailsByUserId(Integer
					.parseInt(friendId));
			if (null != entity) {
				populateFriendPhotoUrl(noFriendResponse, entity);
			}
			LOGGER.info("[FriendRequestDAOImpl][getFriendStatusForUserWithFriend] end ");
			return noFriendResponse;
		}

	}

	/**
	 * This method is used for getting pending friend request count for the
	 * logged in user by querying the azure storage.
	 * 
	 * @param userID
	 */
	@Override
	public Integer getPendingFriendRequestsCountForUser(String userID)
			throws Exception {
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsCountForUser] start ");
		LOGGER.debug("User ID :   " + userID);
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
		for (@SuppressWarnings("unused")
		FriendRequestEntity entity : AzureChatStorageUtils.getTableReference(
				AzureChatConstants.TABLE_NAME_FRIEND_REQ).execute(
				pendingFriendRequestsQuery)) {
			count++;
		}
		LOGGER.info("[FriendRequestDAOImpl][getPendingFriendRequestsCountForUser] end ");
		return count;
	}

	/**
	 * This method populates the friend entity with user entities photo URL and
	 * name is the first name + Last name.
	 * 
	 * @param firend
	 * @param user
	 */
	private void populateFriendPhotoUrl(FriendRequestEntity friend,
			UserEntity user) {
		friend.setFriendName(user.getFirstName()
				+ AzureChatConstants.CONSTANT_SPACE + user.getLastName());
		friend.setFriendProfileBlobURL(user.getPhotoBlobUrl());
	}

}