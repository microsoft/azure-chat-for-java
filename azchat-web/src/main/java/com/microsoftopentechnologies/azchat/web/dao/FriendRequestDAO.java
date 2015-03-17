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
package com.microsoftopentechnologies.azchat.web.dao;

import java.util.List;

import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.FriendRequestEntity;

/**
 * This Interface defines contract methods for the Friend Request Management DAO
 * object.
 * 
 * @author Rupesh_Shirude
 *
 */
public interface FriendRequestDAO {

	/**
	 * Adds friend request entity in Azure table.
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
	 * Get friend status for logged in user with another user by his friend id.
	 * 
	 * @param userID
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	public FriendRequestEntity getFriendStatusForUserWithFriend(String userID,
			String friendId) throws Exception;

	/**
	 * Check whether friend request is approved or not.
	 * 
	 * @param userID
	 * @param friendID
	 * @return
	 * @throws Exception
	 */
	public boolean isFriendRequestApproved(String userID, String friendID)
			throws Exception;

	/**
	 * Accept friend request by logged in user.
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
	 * Reject friend request by logged in user.
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
	 * Get pending friend request count for logged is user by his user id.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public Integer getPendingFriendRequestsCountForUser(String userID)
			throws Exception;
}
