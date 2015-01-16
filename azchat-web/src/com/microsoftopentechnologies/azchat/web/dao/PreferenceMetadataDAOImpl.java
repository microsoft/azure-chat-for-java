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

@Service("preferenceMetadataDAO")
public class PreferenceMetadataDAOImpl implements PreferenceMetadataDAO {

	static final Logger LOGGER = LogManager
			.getLogger(PreferenceMetadataDAOImpl.class);
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String connectionString = null;
	private String sqlString = null;

	@Override
	public PreferenceMetadataEntity addPreferenceMetadataEntity(
			PreferenceMetadataEntity preferenceMetadataEntity) throws Exception {
		LOGGER.info("Execting SQL query for User : UserDAOImpl.java :: saveNewUser");
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
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while addPreferenceMetadataEntity_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
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
					"Exception occured loading driver class : "
							+ e.getMessage());
		} catch (SQLException e) {
			LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occured connecting with sql : " + e.getMessage());
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
			throw new AzureChatSystemException("Exception executing sql : "
					+ e.getMessage());
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Exception while getPreferenceMetadataEntityById_closing DB resources : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception while closing resources : resultSet & prepareStmt : "
								+ e.getMessage());
			}
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return preferenceMetadataEntity;
	}
	
	@Override
	public List<PreferenceMetadataEntity> getPreferenceMetadataEntities()
			throws Exception {
			LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
			List<PreferenceMetadataEntity> preferenceMetadataEntities = new ArrayList<PreferenceMetadataEntity>();
			connectionString = AzureChatUtils.buildConnectionString();
			try {
				connection = AzureChatUtils.getConnection(connectionString);
			} catch (ClassNotFoundException e) {
				LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occured loading driver class : "
								+ e.getMessage());
			} catch (SQLException e) {
				LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occured connecting with sql : " + e.getMessage());
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
					LOGGER.info("Exception while getPreferenceMetadataEntityById_closing DB resources : "
							+ e.getMessage());
					throw new AzureChatSystemException(
							"Exception while closing resources : resultSet & prepareStmt : "
									+ e.getMessage());
				}
			}
			LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
			return preferenceMetadataEntities;
	}

	/**
	 * Used to generate prepare statement object from given preferenceMetadataEntity objetc.
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
	 * Used to set preferenceMetadataEntity object from returned resultset object.
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
		
	@Override
	public Integer getPreferenceMetedataIdByDescription(String desc)
			throws Exception {
			LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
			Integer id = new Integer(0);
			connectionString = AzureChatUtils.buildConnectionString();
			try {
				connection = AzureChatUtils.getConnection(connectionString);
			} catch (ClassNotFoundException e) {
				LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occured loading driver class : "
								+ e.getMessage());
			} catch (SQLException e) {
				LOGGER.info("Exception while getPreferenceMetadataEntityById_loading sql driver class  : "
						+ e.getMessage());
				throw new AzureChatSystemException(
						"Exception occured connecting with sql : " + e.getMessage());
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
				throw new AzureChatSystemException("Exception executing sql : "
						+ e.getMessage());
			} finally {
				try {
					resultSet.close();
					preparedStatement.close();
					connection.close();
				} catch (SQLException e) {
					LOGGER.info("Exception while getPreferenceMetadataEntityById_closing DB resources : "
							+ e.getMessage());
					throw new AzureChatSystemException(
							"Exception while closing resources : resultSet & prepareStmt : "
									+ e.getMessage());
				}
			}
			LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
			return id;
	}
}