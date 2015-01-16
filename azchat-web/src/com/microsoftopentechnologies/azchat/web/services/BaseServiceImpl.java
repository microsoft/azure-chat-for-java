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
 * controller has to call this method with ServiceActionEnum.serviceAction
 * property added in the model bean and also provide the executeSerice
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
	 * common method to call the azchat services and handle the exceptions.
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
			LOGGER.error("AZChatBusinessException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addBusinessException(e, baseBean);

		} catch (AzureChatSystemException e) {
			LOGGER.error("AZChatSystemException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addSystemException(e, baseBean);

		} catch (AzureChatException e) {
			LOGGER.error("AZChatException catched. Exception Message : "
					+ e.getMessage() + "Parsing and Adding the exceptions.");
			addAZChatException(e, baseBean);

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
	 * This method parse and add the azchat exceptions to the baseBean.
	 * 
	 * @param e
	 * @param baseBean
	 */
	private void addAZChatException(AzureChatException e, BaseBean baseBean) {
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

}
