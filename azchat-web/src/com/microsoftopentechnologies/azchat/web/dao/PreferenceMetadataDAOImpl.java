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
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;

/**
 * Implementation class for the PreferenceMetadataDAO.This class provides the
 * operations for storing the user preference meta data.
 * 
 * @author Rupesh_Shirude
 */
@Service("preferenceMetadataDAO")
public class PreferenceMetadataDAOImpl implements PreferenceMetadataDAO {

	private static final Logger LOGGER = LogManager
			.getLogger(PreferenceMetadataDAOImpl.class);
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String connectionString = null;
	private String sqlString = null;

	/**
	 * This method adds the user preference data in the azure SQL table.
	 * 
	 * @return preferenceMetadataEntity
	 */
	@Override
	public PreferenceMetadataEntity addPreferenceMetadataEntity(
			PreferenceMetadataEntity preferenceMetadataEntity) throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][addPreferenceMetadataEntity]         start ");
		LOGGER.debug("PreferenceMetadataEntity    "
				+ preferenceMetadataEntity.getPreferenceDesc());
		connectionString = AzureChatUtils.buildConnectionString();
		int prefMetadataId = 0;
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while addPreferenceMetadataEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while addPreferenceMetadataEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.ADD_PREFERENCE_METADATA);
			preparedStatement = connection.prepareStatement(sqlString,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					preferenceMetadataEntity);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				prefMetadataId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.info("Exception while addPreferenceMetadataEntity : "
					+ e.getMessage());
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while addPreferenceMetadataEntity_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		preferenceMetadataEntity.setPreferenceId(prefMetadataId);
		LOGGER.info("[PreferenceMetadataEntity][addPreferenceMetadataEntity]         end ");
		return preferenceMetadataEntity;
	}

	/**
	 * This method fetch the user preferences from the azure SQL table.
	 * 
	 * @return preferenceMetadataEntity
	 */
	@Override
	public PreferenceMetadataEntity getPreferenceMetadataEntityById(
			Integer preferenceMetadataEntityId) throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		LOGGER.debug("PreferenceMetadataEntity   id "
				+ preferenceMetadataEntityId);
		PreferenceMetadataEntity preferenceMetadataEntity = null;
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred connecting with sql : " + e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_PREFERENCE_METADATA_BY_ID);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, preferenceMetadataEntityId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				preferenceMetadataEntity = generateUserObject(resultSet);
			}
		} catch (SQLException e) {
			LOGGER.error("SQL Exception executing prepfernce metadata sql. Exception Message :  "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"SQL Exception executing prepfernce metadata sql. Exception Message :  "
							+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("Exception while getPreferenceMetadataEntityById_closing DB resources. Exception Message :  "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while getPreferenceMetadataEntityById_closing DB resources. Exception Message :"
								+ e.getMessage());
			}
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return preferenceMetadataEntity;
	}

	/**
	 * This method fetch the user preferences from the azure SQL table.
	 * 
	 * @return preferenceMetadataEntities
	 */
	@Override
	public List<PreferenceMetadataEntity> getPreferenceMetadataEntities()
			throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		List<PreferenceMetadataEntity> preferenceMetadataEntities = new ArrayList<PreferenceMetadataEntity>();
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Exception occurred while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
							+ e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_PREFERENCE_METADATA_ALL);
			preparedStatement = connection.prepareStatement(sqlString);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				preferenceMetadataEntities.add(generateUserObject(resultSet));
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
				LOGGER.info("Exception occured while getPreferenceMetadataEntityById_closing DB resources. Exception Message :  "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occured while getPreferenceMetadataEntityById_closing DB resources. Exception Message :  "
								+ e.getMessage());
			}
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return preferenceMetadataEntities;
	}

	/**
	 * This method used to generate prepare statement object from given
	 * preferenceMetadataEntity object.
	 * 
	 * @param preparedStatement
	 * @param preferenceMetadataEntity
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement generatePreparedStatement(
			PreparedStatement preparedStatement,
			PreferenceMetadataEntity preferenceMetadataEntity)
			throws SQLException {
		preparedStatement.setString(1,
				preferenceMetadataEntity.getPreferenceDesc());
		preparedStatement.setDate(2, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(3, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(4, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(5, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		return preparedStatement;
	}

	/**
	 * this method used to set preferenceMetadataEntity object from returned
	 * result set object.
	 * 
	 * @param resutSet
	 * @return
	 * @throws SQLException
	 */
	public PreferenceMetadataEntity generateUserObject(ResultSet resutSet)
			throws SQLException {
		PreferenceMetadataEntity preferenceMetadataEntity = new PreferenceMetadataEntity();
		preferenceMetadataEntity.setPreferenceId(resultSet.getInt(1));
		preferenceMetadataEntity.setPreferenceDesc(resultSet.getString(2));
		preferenceMetadataEntity.setDateCreated(resultSet.getDate(3));
		preferenceMetadataEntity.setCreatedBy(resultSet.getDate(3));
		preferenceMetadataEntity.setDateModified(resultSet.getDate(3));
		preferenceMetadataEntity.setModifiedBy(resultSet.getDate(3));
		return preferenceMetadataEntity;

	}

	/**
	 * This method fetch the user preference id based on the input description.
	 * 
	 * @return id
	 */
	@Override
	public Integer getPreferenceMetedataIdByDescription(String desc)
			throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		Integer id = new Integer(0);
		connectionString = AzureChatUtils.buildConnectionString();
		try {
			connection = AzureChatUtils.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			LOGGER.error("ClassNotFoundException while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"ClassNotFoundException while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.error("SQLException while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :  "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"SQLException while getPreferenceMetadataEntityById_loading sql driver class. Exception Message :   "
							+ e.getMessage());
		}
		try {
			sqlString = new String(
					AzureChatSQLConstants.GET_PREFERENCE_METADATA_BY_DESC);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, desc);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error("SQLException occurred while retrieving the preference mata data entities. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"SQLException occurred while retrieving the preference mata data entities. Exception Message : "
							+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("Exception occurred while getPreferenceMetadataEntityById_closing DB resources. Exception Message :  "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occurred while getPreferenceMetadataEntityById_closing DB resources. Exception Message : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return id;
	}

	/**
	 * This method creates the preference meta data table.
	 */
	@Override
	public void createPreferenceMatedateTable(Connection connection) throws Exception {
		try {
			sqlString = new String(
					AzureChatSQLConstants.CREATE_PREFERENCE_METADATA_TABLE);
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.execute();
		} catch (SQLException e) {

			LOGGER.error("Exception occurred while executing createUserTable query on azure sql. Exception Mesage : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing createUserTable query on azure sql. Exception Mesage :  "
							+ e.getMessage());
		} finally {

			try {
				preparedStatement.close();
			} catch (SQLException e) {

				LOGGER.error("Exception occurred while createUserTable_closing DB resources. Exception Message :  "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occurred while createUserTable_closing DB resources. Exception Message :  "
								+ e.getMessage());
			}
		}

	}
}