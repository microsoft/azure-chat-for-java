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

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * ContextAware implementation to store the ServletContext to be used in the
 * application as a cache.
 * 
 * @author Dnyaneshwar_pawar
 *
 */
@Component
public class AzureChatAppCtxUtils implements ServletContextAware {

	private static ServletContext ctx;

	/**
	 * get application context.
	 * 
	 * @return ctx
	 */
	public static ServletContext getApplicationContext() {
		return ctx;
	}

	/**
	 * servletContext to be set.
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		ctx = servletContext;
	}

}
