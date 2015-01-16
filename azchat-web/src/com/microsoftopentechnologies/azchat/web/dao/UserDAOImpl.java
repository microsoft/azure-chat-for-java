/* Copyright 2015 Microsoft Open Technologies, Inc.

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatSQLConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;

/**
 * DAO impl class for User data-store table.
 * 
 * @author rupesh_shirude
 *
 */
@Service("userDao")
public class UserDAOImpl implements UserDAO {

	static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String connectionString = null;
	private String sqlString = null;

	public UserEntity saveNewUser(UserEntity user) throws Exception {
		LOGGER.info("[UserDAOImpl][saveNewUser]         start ");

		int userId = 0;
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {

			LOGGER.info("Exception while saveNewUser_loading sql driver class  : "
					+ e.getMessage());

			LOGGER.info("Exception while saveNewUser_loading sql driver class  : "
					+ e.getMessage());

			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {

			LOGGER.info("Exception while saveNewUser_getting connection  : "
					+ e.getMessage());

			LOGGER.info("Exception while saveNewUser_getting connection  : "
					+ e.getMessage());

			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.SAVE_NEW_USER);
			preparedStatement = connection.prepareStatement(sqlString,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					user);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				userId = resultSet.getInt(1);
			}
		} catch (SQLException e) {

			LOGGER.info("Exception while saveNewUser : " + e.getMessage());

			LOGGER.info("Exception while saveNewUser : " + e.getMessage());

			throw new AzureChatSystemException(
					"Exception executing azure sql for save new user : "
							+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {

				LOGGER.info("Exception while saveNewUser_closing DB resources : "
						+ e.getMessage());

				LOGGER.info("Exception while saveNewUser_closing DB resources : "
						+ e.getMessage());

				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		user.setUserID(userId);

		LOGGER.info("[UserDAOImpl][saveNewUser] end ");
		return user;
	}

	@Override
	public UserEntity getUserDetailsByUserId(Integer userId) throws Exception {

		LOGGER.info("[UserDAOImpl][getUserDetailsByUserId] start ");

		LOGGER.info("[UserDAOImpl][getUserDetailsByUserId]         start ");

		UserEntity user = null;
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {

			LOGGER.info("Exception while getUserDetailsByUserId_loading sql driver class  : "
					+ e.getMessage());

			LOGGER.info("Exception while getUserDetailsByUserId_loading sql driver class  : "
					+ e.getMessage());

			throw new AzureChatSystemException(
					"Exception occured loading azure sql driver class : "
							+ e.getMessage());
		} catch (SQLException e) {

			LOGGER.info("Exception while getUserDetailsByUserId_getting connection  : "
					+ e.getMessage());

			LOGGER.info("Exception while getUserDetailsByUserId_getting connection  : "
					+ e.getMessage());

			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.GET_USER_BY_USERID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				user = generateUserObject(resultSet);
			}
		} catch (SQLException e) {

			LOGGER.info("Exception while getUserDetailsByUserId : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {

				LOGGER.info("Exception while getUserDetailsByUserId_closing DB resources : "
						+ e.getMessage());

				LOGGER.info("Exception while getUserDetailsByUserId_closing DB resources : "
						+ e.getMessage());

				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByUserId]         end ");
		return user;
	}

	@Override
	public List<UserEntity> getUserDetailsByNameID(String nameId)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameID] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserDetailsByNameID_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserDetailsByNameID_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.GET_USER_BY_NAMEID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, nameId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {

			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserDetailsByNameID_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameID]         end ");
		return userEntities;
	}

	@Override
	public List<UserEntity> getUserDetailsByNameIdAndIdentityProvider(
			String nameId, String identityProvider) throws AzureChatException {
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameIdAndIdentityProvider]         start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserDetailsByNameIdAndIdentityProvider_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserDetailsByNameIdAndIdentityProvider_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_USER_BY_NAMEID_IDNTITY_PROVIDR);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, nameId);
			preparedStatement.setString(2, identityProvider);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {

			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserDetailsByNameIdAndIdentityProvider_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameIdAndIdentityProvider]         end ");
		return userEntities;
	}

	@Override
	public List<UserEntity> getUserDetailsByFirstName(String firstName)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstName]         start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserDetailsByFirstName_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserDetailsByFirstName_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.GET_USER_BY_FIRST_NAME);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, firstName + "%");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {

			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserDetailsByFirstName_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstName]         end ");
		return userEntities;
	}

	@Override
	public List<UserEntity> getUserDetailsByLastName(String lastName)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByLastName]         start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserDetailsByLastName_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserDetailsByLastName_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.GET_USER_BY_LAST_NAME);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, lastName + "%");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {

			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserDetailsByLastName_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByLastName]         end ");
		return userEntities;
	}

	@Override
	public List<UserEntity> getUserDetailsByFirstNameOrLastName(String name)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstNameOrLastName]         start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getUserDetailsByFirstNameOrLastName_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getUserDetailsByFirstNameOrLastName_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_USER_BY_FIRST_LAST_NAME);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, name + "%");
			preparedStatement.setString(2, name + "%");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {

			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getUserDetailsByFirstNameOrLastName_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstNameOrLastName]         end ");
		return userEntities;
	}

	/**
	 * 
	 * @param preparedStatement
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement generatePreparedStatement(
			PreparedStatement preparedStatement, UserEntity user)
			throws SQLException {
		preparedStatement.setString(1, user.getNameId());
		preparedStatement.setString(2, user.getIdentityProvider());
		preparedStatement.setString(3, user.getFirstName());
		preparedStatement.setString(4, user.getLastName());
		preparedStatement.setString(5, user.getPhotoBlobUrl());
		preparedStatement.setString(6, user.getEmailAddress());
		preparedStatement.setInt(7, user.getPhoneCountryCode());
		preparedStatement.setLong(8, user.getPhoneNumber());
		preparedStatement.setDate(9, new java.sql.Date(user.getDateCreated()
				.getTime()));
		preparedStatement.setDate(10, new java.sql.Date(user.getCreatedBy()
				.getTime()));
		preparedStatement.setDate(11, new java.sql.Date(user.getDateModified()
				.getTime()));
		preparedStatement.setDate(12, new java.sql.Date(user.getModifiedBy()
				.getTime()));
		return preparedStatement;
	}

	public UserEntity generateUserObject(ResultSet resultSet)
			throws SQLException {
		UserEntity user = new UserEntity();
		user.setUserID(resultSet.getInt(1));
		user.setNameId(resultSet.getString(2));
		user.setIdentityProvider(resultSet.getString(3));
		user.setFirstName(resultSet.getString(4));
		user.setLastName(resultSet.getString(5));
		user.setPhotoBlobUrl(resultSet.getString(6));
		user.setEmailAddress(resultSet.getString(7));
		user.setPhoneCountryCode(resultSet.getInt(8));
		user.setPhoneNumber(resultSet.getLong(9));
		user.setDateCreated(resultSet.getDate(10));
		user.setCreatedBy(resultSet.getDate(11));
		user.setDateModified(resultSet.getDate(12));
		user.setModifiedBy(resultSet.getDate(13));
		return user;
	}

	@Override
	public UserEntity updateNewUser(UserEntity user) throws Exception {
		LOGGER.info("[UserDAOImpl][updateNewUser]         start ");
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while updateNewUser_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while updateNewUser_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(AzureChatSQLConstants.UPDATE_NEW_USER);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getPhotoBlobUrl());
			preparedStatement.setString(4, user.getEmailAddress());
			preparedStatement.setInt(5, user.getPhoneCountryCode());
			preparedStatement.setLong(6, user.getPhoneNumber());
			preparedStatement.setString(7, user.getNameId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {

			LOGGER.info("Exception while updateNewUser : " + e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {

				LOGGER.info("Exception while updateNewUser_closing DB resources : "
						+ e.getMessage());

				LOGGER.info("Exception while updateNewUser_closing DB resources : "
						+ e.getMessage());

				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[UserDAOImpl][updateNewUser] end ");
		return user;

	}

	@Override
	public String getUserPhotoBlobURL(Integer userId) throws Exception {
		String profileImageURL = "";
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while updateNewUser_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while updateNewUser_getting connection  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
		}
		
		try {
			sqlString = new String(AzureChatSQLConstants.GET_USER_PROFILE_URL_BY_USERID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				profileImageURL = resultSet.getString(1);
			}
		} catch (SQLException e) {

			LOGGER.info("Exception while getUserDetailsByUserId : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {

			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {

				LOGGER.info("Exception while getUserDetailsByUserId_closing DB resources : "
						+ e.getMessage());

				LOGGER.info("Exception while getUserDetailsByUserId_closing DB resources : "
						+ e.getMessage());

				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		return profileImageURL;
	}
}
