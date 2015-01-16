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

@Service("userPreferenceDAO")
public class UserPreferenceDAOImpl implements UserPreferenceDAO {

	static final Logger LOGGER = LogManager
			.getLogger(UserPreferenceDAOImpl.class);
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String connectionString = null;
	String sqlString = null;

	@Override
	public UserPreferenceEntity addUserPreferenceEntity(
			UserPreferenceEntity userPreferenceEntity) throws Exception {
		LOGGER.info("[UserPreferenceEntity][addUserPreferenceEntity]         start ");
		int userPreferenceId = 0;
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while addUserPreferenceEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while addUserPreferenceEntity_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.ADD_USER_PREFERENCE);
			preparedStatement = connection.prepareStatement(sqlString,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					userPreferenceEntity);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				userPreferenceId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.info("Exception while addUserPreferenceEntity : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while addUserPreferenceEntity_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		userPreferenceEntity.setUserPreferenceID(userPreferenceId);
		LOGGER.info("[UserPreferenceEntity][addUserPreferenceEntity]         end ");
		return userPreferenceEntity;
	}

	@Override
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId)
			throws Exception {
		LOGGER.info("[UserPreferenceDAOImpl][getUserPreferenceEntity]         start ");
		List<UserPreferenceEntity> userPreferenceEntities = new ArrayList<UserPreferenceEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserPreferenceEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserPreferenceEntity_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_USER_PREFERENCE_BY_USERID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userPreferenceEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserPreferenceEntity : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserPreferenceEntity_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserPreferenceEntity][getUserPreferenceEntity]         end ");
		return userPreferenceEntities;
	}

	@Override
	public List<UserPreferenceEntity> getUserPreferenceEntity(Integer userId,
			Integer preferenceMetadataId) throws Exception {
		LOGGER.info("[UserPreferenceDAOImpl][getUserPreferenceEntity]         start ");
		List<UserPreferenceEntity> userPreferenceEntities = new ArrayList<UserPreferenceEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserPreferenceEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserPreferenceEntity_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_USER_PREFERENCE_BY_USERID_PREFERENCEID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, preferenceMetadataId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userPreferenceEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserPreferenceEntity : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserPreferenceEntity_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserPreferenceEntity][getUserPreferenceEntity]         end ");
		return userPreferenceEntities;
	}

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
}
