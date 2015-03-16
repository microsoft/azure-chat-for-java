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
package com.microsoftopentechnologies.azchat.web.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorListBean;

/**
 * This class contains common utility methods to be used in the application.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatUtils {

	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatUtils.class);
	private static final Properties PROPERTIES = new Properties();
	private static final String TOKEN_SEPARATOR = ";";

	/**
	 * Gets XML DOM object from string data.
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(String data) throws Exception {
		LOGGER.info("[AzureChatUtils][getDocument] start");
		Document document = null;
		DocumentBuilder documentBuilder;

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		byte[] samlToken = data.getBytes("UTF-8");

		ByteArrayInputStream bis = new ByteArrayInputStream(samlToken);
		document = documentBuilder.parse(bis);
		document.getDocumentElement().normalize();
		LOGGER.info("[AzureChatUtils][getDocument] end");
		return document;
	}

	/**
	 * Retrieves nameID assertion from ACS token.
	 * 
	 * @param xPath
	 * @param assertionDoc
	 * @return
	 * @throws Exception
	 */
	public static String getNameIDFromAssertion(XPath xPath,
			Document assertionDoc) throws Exception {
		LOGGER.info("[AzureChatUtils][getNameIDFromAssertion] start");
		Node nameIDNode = (Node) xPath.evaluate(
				AzureChatConstants.NAME_ID_NODE, assertionDoc,
				XPathConstants.NODE);
		LOGGER.info("[AzureChatUtils][getNameIDFromAssertion] end");
		return nameIDNode.getTextContent();
	}

	/**
	 * Retrieves user attribute details from ACS token.
	 * 
	 * @param xPath
	 * @param assertionDoc
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getUserAttributeDetails(XPath xPath,
			Document assertionDoc) throws Exception {
		LOGGER.info("[AzureChatUtils][getUserAttributeDetails] start");
		NodeList userAttributes = (NodeList) xPath.evaluate(
				AzureChatConstants.ATTRIBUTE_NODE, assertionDoc,
				XPathConstants.NODESET);
		Map<String, String> claimMap = new HashMap<String, String>();

		for (int i = 0; i < userAttributes.getLength(); i++) {
			Node attribute = userAttributes.item(i);
			String attributeName = attribute.getAttributes()
					.getNamedItem(AzureChatConstants.NAME_ATTRIBUTE)
					.getNodeValue();
			LOGGER.debug("Attribute Name :" + attributeName);
			String claimName = attributeName.substring(attributeName
					.lastIndexOf(AzureChatConstants.CONSTANT_BACK_SLASH) + 1);
			LOGGER.debug("Claim Name :" + claimName);

			if (AzureChatConstants.ATTR_IDENTITY_PROVIDER
					.equalsIgnoreCase(claimName)) {
				claimMap.put(AzureChatConstants.ATTR_IDENTITY_PROVIDER,
						attribute.getFirstChild().getTextContent());
			} else if (AzureChatConstants.ATTR_EMAIL_ADDRESS
					.equalsIgnoreCase(claimName)) {
				claimMap.put(AzureChatConstants.ATTR_EMAIL_ADDRESS, attribute
						.getFirstChild().getTextContent());
			} else if (AzureChatConstants.ATTR_NAME.equalsIgnoreCase(claimName)) {
				claimMap.put(AzureChatConstants.ATTR_NAME, attribute
						.getFirstChild().getTextContent());
			}

		}
		LOGGER.info("[AzureChatUtils][getUserAttributeDetails] start");
		return claimMap;
	}

	/**
	 * This method loads project properties.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Properties getProperties() throws AzureChatException {
		InputStream inputStream = AzureChatUtils.class.getClassLoader()
				.getResourceAsStream(AzureChatConstants.RESOURCE_PROP_FILE);
		InputStream inputStream1 = AzureChatUtils.class.getClassLoader()
				.getResourceAsStream(AzureChatConstants.MSG_PROP_FILE);
		if (inputStream != null) {
			try {
				PROPERTIES.load(inputStream);
				PROPERTIES.load(inputStream1);
			} catch (IOException e) {
				LOGGER.error("Exception Occurred while reading property file"
						+ AzureChatConstants.RESOURCE_PROP_FILE);
			} finally {
				try {
					inputStream.close();
					inputStream1.close();
				} catch (IOException e) {
					LOGGER.error("IOException Occurred while trying to close input stream after property load."
							+ e.getMessage());
					throw new AzureChatException(
							"IOException Occurred while trying to close input stream after property load."
									+ e.getMessage());
				}

			}
		}
		return PROPERTIES;
	}

	/**
	 * get property value for given property name.
	 * 
	 * @param propName
	 * @return
	 * @throws IOException
	 */
	public static String getProperty(String propName) throws AzureChatException {
		return null != getProperties().get(propName) ? (String) getProperties()
				.get(propName) : null;
	}

	/**
	 * This method checks for empty or null collections.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Collection<?> value) {
		return value == null || value.isEmpty();
	}

	/**
	 * This method checks for if map is empty.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> value) {
		return value == null || value.isEmpty();
	}

	/**
	 * This method checks for empty or null string.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns Azure SQL database connection object.
	 * 
	 * @param connectionString
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws AzureChatException
	 */
	public static Connection getConnection(String connectionString)
			throws ClassNotFoundException, SQLException, AzureChatException {
		Connection connection = null;
		Class.forName(AzureChatUtils.getProperty(AzureChatConstants.DRIVER));
		connection = DriverManager.getConnection(connectionString);
		return connection;
	}

	/**
	 * This method prepare connection string for the Azure SQL Database.
	 * 
	 * @return
	 * @throws AzureChatException
	 */
	public static String buildConnectionString() throws AzureChatException {
		String timeout = AzureChatUtils
				.getProperty(AzureChatConstants.DB_PROP_KEY_LOG_IN_TIMEOUT);

		if (AzureChatUtils.isEmptyOrNull(timeout)) {
			timeout = AzureChatConstants.DB_PROP_TIMEOUT_VAL;
		}

		StringBuilder connectionString = new StringBuilder(
				AzureChatUtils.getProperty(AzureChatConstants.DB_PROP_KEY_URL));

		connectionString
				.append(TOKEN_SEPARATOR)
				.append("database=")
				.append(AzureChatUtils
						.getProperty(AzureChatConstants.DB_PROP_KEY_DATABASE));

		connectionString
				.append(TOKEN_SEPARATOR)
				.append("user=")
				.append(AzureChatUtils
						.getProperty(AzureChatConstants.DB_PROP_KEY_USER));

		connectionString
				.append(TOKEN_SEPARATOR)
				.append("password=")
				.append(AzureChatUtils
						.getProperty(AzureChatConstants.DB_PROP_KEY_PASSWORD));

		connectionString
				.append(TOKEN_SEPARATOR)
				.append("encrypt=")
				.append(AzureChatUtils
						.getProperty(AzureChatConstants.DB_PROP_KEY_ENCRYPT));

		connectionString
				.append(TOKEN_SEPARATOR)
				.append("hostNameInCertificate=")
				.append(AzureChatUtils
						.getProperty(AzureChatConstants.DB_PROP_KEY_HOST_NAME_IN_CERT));

		connectionString.append(TOKEN_SEPARATOR).append("loginTimeout=")
				.append(timeout).append(TOKEN_SEPARATOR);

		return connectionString.toString();
	}

	/**
	 * This method converts the bytes value into the megabytes value.
	 * 
	 * @param bytes
	 * @return
	 */
	public static Long getMegaBytes(Long bytes) {
		if (null != bytes) {
			return bytes / 1048576;
		}
		return null;
	}

	/**
	 * This method extracts the digits from the provided string.
	 * 
	 * @param str
	 * @return
	 */
	public static Long getNumbers(String str) {
		Long num = null;
		if (null != str) {
			str = str.replaceAll(AzureChatConstants.REGEX_ONLY_DIGITS,
					AzureChatConstants.CONSTANT_EMPTY_STRING);
			num = Long.parseLong(str);
		}
		return num;
	}

	/**
	 * This method populates the error in input bean.
	 * 
	 * @param baseBean
	 * @param excpCode
	 * @param excpMsg
	 */
	public static void populateErrors(BaseBean baseBean, String excpCode,
			String excpMsg) {
		ErrorListBean errorListBean = new ErrorListBean();
		List<ErrorBean> errorBeanList = new ArrayList<ErrorBean>();
		ErrorBean errorBean = new ErrorBean();
		errorBean.setExcpMsg(excpMsg);
		errorListBean.setExcpCode(excpCode);
		errorBeanList.add(errorBean);
		errorListBean.setErrorList(errorBeanList);
		baseBean.setErrorList(errorListBean);
	}

	/**
	 * This method returns the SAS URL for the input container name.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getSASUrl(String containerName) throws Exception {
		CloudBlobContainer cloudBlobContainer = AzureChatStorageUtils
				.getCloudBlobContainer(containerName);
		String signature = AzureChatStorageUtils
				.generateSASURL(cloudBlobContainer);
		return signature;
	}

	/**
	 * This method generates and return the random GUID using java.util.UUID.
	 * 
	 * @return
	 */
	public static String getGUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * This method returns the current time stamp.
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * This method converts the string number value to the integer
	 * 
	 * @param expiryTime
	 * @return
	 */
	public static int covertMinToSec(String min) {
		if (!isEmptyOrNull(min)) {
			return (60 * Integer.valueOf(min));
		}
		return 0;
	}

	/**
	 * This method converts the string to integer value.
	 * 
	 * @param str
	 * @return
	 */
	public static long covertStringToLong(String str) {
		if (!isEmptyOrNull(str)) {
			return Long.parseLong(str);
		}
		return 0;
	}

	/**
	 * This method retrieve the preferences from the property file.Prepare and
	 * return the ArrayList.
	 * 
	 * @return
	 * @throws AzureChatException
	 */
	public static List<String> getPreferences() throws AzureChatException {
		List<String> list = new ArrayList<String>();
		Properties properties = getProperties();
		int propertyCount = Integer.parseInt(properties
				.getProperty(AzureChatConstants.USER_PREF_COUNT_KEY));
		for (int i = 1; i <= propertyCount; i++) {
			list.add(properties
					.getProperty(AzureChatConstants.USER_PREF_PREP_KEY + i));
		}
		return list;
	}

	/**
	 * This method releases this PrepareStatement,ResultSet and Connection
	 * object's database and JDBC resources.
	 * 
	 * @param prepStmt
	 * @param rs
	 * @param con
	 * @throws SQLException
	 */
	public static void closeDatabaseResources(PreparedStatement prepStmt,
			ResultSet rs, Connection con) throws SQLException {
		if (prepStmt != null) {
			prepStmt.close();
		}
		if (rs != null) {
			rs.close();
		}
		if (con != null) {
			con.close();
		}

	}

	/**
	 * This method releases this Connection and PrepareStatement object's
	 * database and JDBC resources.
	 * 
	 * @param prepStmt
	 * @param con
	 * @throws SQLException
	 */
	public static void closeDatabaseResources(PreparedStatement prepStmt,
			Connection con) throws SQLException {
		if (prepStmt != null) {
			prepStmt.close();
		}

		if (con != null) {
			con.close();
		}

	}

	/**
	 * This method releases this ResutlSet and PrepareStatement object's
	 * database and JDBC resources.
	 * 
	 * @param prepStmt
	 * @param rs
	 * @throws SQLException
	 */
	public static void closeDatabaseResources(PreparedStatement prepStmt,
			ResultSet rs) throws SQLException {
		if (prepStmt != null) {
			prepStmt.close();
		}
		if (rs != null) {
			rs.close();
		}

	}

	/**
	 * This Method returns service end point URL from blob URL.
	 * 
	 * @param url
	 * @return serviceUrl
	 */
	public static String getServiceEndpointUrl(String url) {
		String serviceUrl = url.substring(
				AzureChatConstants.CONSTANT_INT_ZERO,
				url.indexOf(AzureChatConstants.LITERAL_BACKSLASH,
						url.indexOf(AzureChatConstants.LITERAL_DOT))
						+ AzureChatConstants.CONSTANT_INT_ONE);
		return serviceUrl;
	}

	/**
	 * This method will return container name for given URL.
	 * 
	 * @param url
	 * @return
	 */
	public static String getContainerName(String url) {
		int startIndex = url.indexOf(AzureChatConstants.LITERAL_BACKSLASH,
				url.indexOf(AzureChatConstants.LITERAL_DOT))
				+ AzureChatConstants.CONSTANT_INT_ONE;
		int endIndex = url.indexOf(AzureChatConstants.LITERAL_BACKSLASH,
				startIndex + AzureChatConstants.CONSTANT_INT_ONE);
		String containerName = url.substring(startIndex, endIndex);
		return containerName;
	}

	/**
	 * This method will find filename using URL and container name.
	 * 
	 * @param url
	 * @param containerName
	 * @return
	 * @author prajakta_pednekar
	 */
	public static String getFileName(String url, String containerName) {
		int startIndex = url.indexOf(containerName
				+ AzureChatConstants.CONSTANT_BACK_SLASH)
				+ containerName.length() + AzureChatConstants.CONSTANT_INT_ONE;
		int endIndex = url.indexOf(AzureChatConstants.LITERAL_QUESTION_MARK,
				startIndex + AzureChatConstants.CONSTANT_INT_ONE);
		String fileName = url.substring(startIndex, endIndex);
		return fileName;
	}

	/**
	 * This method removes the special characters from the file name except file
	 * extension separator i.e. DOT('.')
	 * 
	 * @param fileName
	 * @return
	 */
	public static String removeSpecialCharactersFromFileName(String fileName) {
		int index = fileName.lastIndexOf(AzureChatConstants.CONSTANT_DOT);
		String fileExt = fileName.substring(index, fileName.length());
		String fileNameWithoutExt = fileName.substring(
				AzureChatConstants.CONSTANT_INT_ZERO, index);
		String fileNameWithoutSpecialChar = fileNameWithoutExt.replaceAll(
				AzureChatConstants.REGEX_ONLY_ALPHA_NUMERIC,
				AzureChatConstants.CONSTANT_EMPTY_STRING);
		return fileNameWithoutSpecialChar + fileExt;
	}

	/**
	 * This method extract the video file name from input videoBlobURL.
	 * 
	 * @param videoBlobURL
	 * @return
	 * @author prajakta_pednekar
	 */
	public static String getFileNameFromURL(String videoBlobURL) {
		String containerName = getContainerName(videoBlobURL);
		String videoFileName = getFileName(videoBlobURL, containerName);
		return videoFileName;
	}

	/**
	 * This method substring the file name into the file name and file extension
	 * and return the file name only.
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileNameWithoutExtention(String fileName) {
		if (null != fileName
				&& fileName.contains(AzureChatConstants.CONSTANT_DOT)) {
			fileName = fileName.substring(AzureChatConstants.CONSTANT_INT_ZERO,
					fileName.lastIndexOf(AzureChatConstants.CONSTANT_DOT));
		}
		return fileName;
	}

	/**
	 * This method return objects string representation.
	 * 
	 * @param str
	 * @return
	 */
	public static String toString(Object obj) {
		return obj != null ? obj.toString() : null;
	}

}
