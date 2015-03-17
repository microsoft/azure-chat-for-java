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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatSQLConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserPreferenceEntity;

/**
 * This class provides operations to add,delete and update the user preferences
 * to the azure SQL user preference table.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("userPreferenceDAO")
public class UserPreferenceDAOImpl implements UserPreferenceDAO {

	private static final Logger LOGGER = LogManager
			.getLogger(UserPreferenceDAOImpl.class);

	/**
	 * This method executes add user preference query on azure SQL user
	 * preference table.
	 * 
	 * @param userPreferenceEntity
	 * @return
	 * @throws Exception
	 * @author Rupesh_Shirude
	 */
	@Override
	public UserPreferenceEntity addUserPreferenceEntity(
			UserPreferenceEntity userPreferenceEntity) throws Exception {
		LOGGER.info("[UserPreferenceEntity][addUserPreferenceEntity] start ");
		int userPreferenceId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection.prepareStatement(
					AzureChatSQLConstants.ADD_USER_PREFERENCE,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					userPreferenceEntity);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				userPreferenceId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing add user preference query on azure SQL user preference table. Exception Message  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing add user preference query on azure SQL user preference table. Exception Message  : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		userPreferenceEntity.setUserPreferenceID(userPreferenceId);
		LOGGER.info("[UserPreferenceEntity][addUserPreferenceEntity] end ");
		return userPreferenceEntity;
	}

	/**
	 * This method executes query on azure SQL user preference table to get user
	 * preference entity object by user id.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author Rupesh_shirude
	 */
	@Override
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId)
			throws Exception {
		LOGGER.info("[UserPreferenceDAOImpl][getUserPreferenceEntity]         start ");
		List<UserPreferenceEntity> userPreferenceEntities = new ArrayList<UserPreferenceEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_PREFERENCE_BY_USERID);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userPreferenceEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get user preferences query on azure SQL user preference table. Exception Message  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user preferences query on azure SQL user preference table. Exception Message  : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserPreferenceEntity][getUserPreferenceEntity]         end ");
		return userPreferenceEntities;
	}

	/**
	 * This method executes query on azure SQL user preference table to get user
	 * preferences by user id & prefMetadataId.
	 * 
	 * @param userId
	 * @param prefMetadataId
	 * @return
	 * @throws Exception
	 * @author Rupesh_Shirude
	 */
	@Override
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId,
			Integer preferenceMetadataId) throws Exception {
		LOGGER.info("[UserPreferenceDAOImpl][getUserPreferenceEntity] start ");
		List<UserPreferenceEntity> userPreferenceEntities = new ArrayList<UserPreferenceEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_PREFERENCE_BY_USERID_PREFERENCEID);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, preferenceMetadataId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userPreferenceEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get user preferences query on azure SQL user preference table. Exception Message  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user preferences query on azure SQL user preference table. Exception Message  : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserPreferenceEntity][getUserPreferenceEntity] end ");
		return userPreferenceEntities;
	}

	/**
	 * This method populates Prepared Statement from userPreferenceEntity
	 * object.
	 * 
	 * @param preparedStatement
	 * @param userPreferenceEntity
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement generatePreparedStatement(
			PreparedStatement preparedStatement,
			UserPreferenceEntity userPreferenceEntity) throws SQLException {
		preparedStatement.setInt(1, userPreferenceEntity.getUserId());
		preparedStatement.setInt(2, userPreferenceEntity.getPreferenceId());
		preparedStatement.setDate(3, new Date(userPreferenceEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(4, new Date(userPreferenceEntity
				.getCreatedBy().getTime()));
		preparedStatement.setDate(5, new Date(userPreferenceEntity
				.getDateModified().getTime()));
		preparedStatement.setDate(6, new Date(userPreferenceEntity
				.getModifiedBy().getTime()));
		return preparedStatement;
	}

	/**
	 * This method populates userPreferenceEntity object from input resultSet.
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public UserPreferenceEntity generateUserObject(ResultSet resultSet)
			throws SQLException {
		UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity();
		userPreferenceEntity.setUserPreferenceID(resultSet.getInt(1));
		userPreferenceEntity.setUserId(resultSet.getInt(2));
		userPreferenceEntity.setPreferenceId(resultSet.getInt(3));
		userPreferenceEntity.setDateCreated(resultSet.getDate(4));
		userPreferenceEntity.setCreatedBy(resultSet.getDate(5));
		userPreferenceEntity.setDateModified(resultSet.getDate(6));
		userPreferenceEntity.setModifiedBy(resultSet.getDate(7));
		userPreferenceEntity.setPreferenceDesc(resultSet.getString(8));
		return userPreferenceEntity;
	}

	/**
	 * This method executes create User Preference table query on the azure SQL
	 * database.
	 * 
	 * @throws Exception
	 * @author Rupesh_shirude
	 */
	@Override
	public void createUserPreferenceTable() throws Exception {
		Connection connection = null;
		Connection connection1 = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.CREATE_USER_PREFERENCES_TABLE);
			preparedStatement.execute();
			connection1 = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement1 = connection1
					.prepareStatement(AzureChatSQLConstants.CREATE_USER_PREFERENCES_TABLE_INDEX);
			preparedStatement1.execute();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing create user preference table in Azure SQL database. Exception Message  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing create user preference table in Azure SQL database. Exception Message  : "
							+ e.getMessage());
		} finally {
			AzureChatUtils
					.closeDatabaseResources(preparedStatement, connection);
			AzureChatUtils.closeDatabaseResources(preparedStatement1,
					connection1);
		}

	}
}
