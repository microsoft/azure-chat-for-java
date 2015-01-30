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

import java.util.List;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;

/**
 * Contract for the Friend Request DAO object.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface FriendRequestDAO {

	/**
	 * Adds friend request entity in Azure table
	 * 
	 * @param userID
	 * @param friendID
	 * @param friendName
	 * @param friendProfileBlobURL
	 * @param requestStatus
	 * @throws Exception
	 */
	public void addFriendRequest(String userID, String friendID,
			String friendName, String friendProfileBlobURL, String requestStatus)
			throws Exception;

	/**
	 * Lists user friends.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public List<FriendRequestEntity> getFriendListForUser(String userID)
			throws Exception;

	/**
	 * Retrieves pending friend requests for user by his userId.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public List<FriendRequestEntity> getPendingFriendRequestsForUser(
			String userID) throws Exception;

	/**
	 * Used to get friend status for logged in user with another user by his
	 * friend id.
	 * 
	 * @param userID
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public FriendRequestEntity getFriendStatusForUserWithFriend(String userID,
			String friendId) throws Exception;

	/**
	 * Used to check whether friend request is approved or not.
	 * 
	 * @param userID
	 * @param friendID
	 * @return
	 * @throws Exception
	 */
	public boolean isFriendRequestApproved(String userID, String friendID)
			throws Exception;

	/**
	 * Used to accept friend request by logged in user.
	 * 
	 * @param userID
	 * @param friendID
	 * @param userName
	 * @param userProfileBlobURL
	 * @throws Exception
	 */
	public void acceptFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception;

	/**
	 * Used to reject friend request by logged in user.
	 * 
	 * @param userID
	 * @param friendID
	 * @param userName
	 * @param userProfileBlobURL
	 * @throws Exception
	 */
	public void rejectFriendRequest(String userID, String friendID,
			String userName, String userProfileBlobURL) throws Exception;

	/**
	 * Used to get pending friend request count for logged is user by his user
	 * id.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public Integer getPendingFriendRequestsCountForUser(String userID)
			throws Exception;
}
