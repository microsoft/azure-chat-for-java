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
package com.microsoftopentechnologies.azchat.web.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorBean;
import com.microsoftopentechnologies.azchat.web.data.beans.ErrorListBean;

/**
 * Base Service providing implementation for invokeService operation of the
 * BaseService contract which in turn has a call to excecuteService. Every
 * controller has to call invokeService method with ServiceActionEnum.serviceAction
 * property added in the model bean and service has to provide the executeSerice
 * implementation.An instance specific executeService will be called.Back trace
 * will parse and add exceptions to model bean if any and return the model bean
 * to controller.
 * 
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public abstract class BaseServiceImpl implements BaseService {
	private static final Logger LOGGER = LogManager
			.getLogger(BaseServiceImpl.class);

	/**
	 * This is a common method to call the AzChat services and handle the exceptions.
	 * 
	 * @param baseBean
	 * @return
	 */
	@Override
	public BaseBean invokeService(BaseBean baseBean) {
		LOGGER.info("[BaseServiceImpl][invokeService] start");
		try {
			executeService(baseBean);
		} catch (AzureChatBusinessException e) {
			LOGGER.error("AzureChatBusinessException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addBusinessException(e, baseBean);

		} catch (AzureChatSystemException e) {
			LOGGER.error("AzureChatSystemException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addSystemException(e, baseBean);

		} catch (AzureChatException e) {
			LOGGER.error("AzureChatException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addAzChatException(e, baseBean);

		} catch (Exception e) {
			LOGGER.error("Exception catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addException(e, baseBean);

		}
		LOGGER.info("[BaseServiceImpl][invokeService] end");
		return baseBean;

	}

	/**
	 * This method parse and add the java exceptions to the baseBean.
	 * 
	 * @param e
	 * @param baseBean
	 */
	private void addException(Exception e, BaseBean baseBean) {
		LOGGER.info("[BaseServiceImpl][addException] start");
		ErrorBean error = new ErrorBean();
		ErrorListBean errorListBean = new ErrorListBean();
		List<ErrorBean> errorList = new ArrayList<>();

		error.setExcpType(e.getClass().getSimpleName());
		error.setExcpMsg(e.getMessage());
		errorList.add(error);
		errorListBean.setExcpCode(AzureChatConstants.EXCEP_CODE_JAVA_EXCEPTION);
		LOGGER.debug("Exception Code Set : " + errorListBean.getExcpCode());
		errorListBean.setErrorList(errorList);
		baseBean.setHasErrors(true);
		baseBean.setErrorList(errorListBean);
		LOGGER.info("[BaseServiceImpl][addException] end");

	}

	/**
	 * This method parse and add the AzChat exceptions to the baseBean.
	 * 
	 * @param e
	 * @param baseBean
	 */
	private void addAzChatException(AzureChatException e, BaseBean baseBean) {
		LOGGER.info("[BaseServiceImpl][addAZChatException] start");
		ErrorBean error = new ErrorBean();
		ErrorListBean errorListBean = new ErrorListBean();
		List<ErrorBean> errorList = new ArrayList<>();

		error.setExcpType(e.getExcpType());
		error.setExcpMsg(e.getExcpMsg());
		errorList.add(error);
		errorListBean.setExcpCode(e.getExcpCode());
		LOGGER.debug("Exception Code Set : " + errorListBean.getExcpCode());
		errorListBean.setErrorList(errorList);
		baseBean.setHasErrors(true);
		baseBean.setErrorList(errorListBean);
		LOGGER.info("[BaseServiceImpl][addAZChatException] end");

	}

	/**
	 * This method parse and add the system exceptions to the baseBean.
	 * 
	 * @param e
	 * @param baseBean
	 */
	private void addSystemException(AzureChatSystemException e,
			BaseBean baseBean) {
		LOGGER.info("[BaseServiceImpl][addSystemException] start");
		ErrorBean error = new ErrorBean();
		ErrorListBean errorListBean = new ErrorListBean();
		List<ErrorBean> errorList = new ArrayList<>();
		if (null != e.getExcpCode()) {
			errorListBean.setExcpCode(e.getExcpCode());
		} else {
			errorListBean
					.setExcpCode(AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION);
		}
		LOGGER.debug("Exception Code Set : " + errorListBean.getExcpCode());
		error.setExcpMsg(e.getExcpMsg());
		errorList.add(error);
		errorListBean.setErrorList(errorList);
		baseBean.setHasErrors(true);
		baseBean.setErrorList(errorListBean);
		LOGGER.info("[BaseServiceImpl][addSystemException] end");

	}

	/**
	 * This method parse and add the business exceptions to the baseBean.
	 * 
	 * @param e
	 * @param baseBean
	 */
	private void addBusinessException(AzureChatBusinessException e,
			BaseBean baseBean) {
		LOGGER.info("[BaseServiceImpl][addBusinessException] start");
		ErrorBean error = new ErrorBean();
		ErrorListBean errorListBean = new ErrorListBean();
		List<ErrorBean> errorList = new ArrayList<>();
		if (null != e.getExcpCode()) {

			errorListBean.setExcpCode(e.getExcpCode());
		} else {
			errorListBean
					.setExcpCode(AzureChatConstants.EXCEP_CODE_BUSSINESS_EXCEPTION);
		}
		LOGGER.debug("Exception Code Set : " + errorListBean.getExcpCode());
		error.setExcpMsg(e.getExcpMsg());
		errorList.add(error);
		errorListBean.setErrorList(errorList);
		baseBean.setHasErrors(true);
		baseBean.setErrorList(errorListBean);
		LOGGER.info("[BaseServiceImpl][addBusinessException] end");

	}

	/**
	 * This method used by all the services trim the input string.
	 * 
	 * @param str
	 * @return
	 */
	protected String trim(String str) {
		return str != null ? str.trim() : str;
	}

}
