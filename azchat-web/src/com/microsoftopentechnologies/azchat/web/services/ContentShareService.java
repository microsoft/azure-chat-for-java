/**
 * 
 */
package com.microsoftopentechnologies.azchat.web.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatBusinessException;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStorageUtils;
import com.microsoftopentechnologies.azchat.web.dao.ProfileImageRequestDAO;
import com.microsoftopentechnologies.azchat.web.dao.UserMessageEntityDAO;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.storage.UserMessageEntity;
import com.microsoftopentechnologies.azchat.web.data.beans.BaseBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserMessageBean;
import com.microsoftopentechnologies.azchat.web.data.beans.UserMessageListBean;

/**
 * @author Administrator
 *
 */
@Service
@Qualifier("contentShareService")
public class ContentShareService extends BaseServiceImpl {
	private static final Logger LOGGER = LogManager
			.getLogger(ContentShareService.class);
	@Autowired
	private ProfileImageRequestDAO profileImageRequestDAO;
	
	@Autowired
	private UserMessageEntityDAO userMessageEntityDAO;

	/**
	 * Execute Service implementation for the Content Share service.The switch
	 * takes action parameter coming from controller which is instance of
	 * ServiceActionEnum.The actions in this method are mapped conventionally by
	 * ServiceActionEnum to execute the operation and modify the model bean.
	 */
	@Override
	public BaseBean executeService(BaseBean baseBean) throws AzureChatException {
		LOGGER.info("[ContentShareService][executeService] start");
		BaseBean baseBeanObj = null;
		switch (baseBean.getServiceAction()) {
		case CONTENT_SHARE:
			baseBeanObj = shareContent((UserBean) baseBean);
			break;
		case GET_USER_CONTENT:
			baseBeanObj = getUserContent((UserBean) baseBean);
			break;
		default:
			break;
		}
		LOGGER.info("[ContentShareService][executeService] end");
		return baseBeanObj;
	}

	/**
	 * BeanPostProcessor initialization code to create and get reference to the
	 * FriendRequest table from azure storage.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("[FriendRequestService][init]          start");
		AzureChatStorageUtils
				.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_COMMENTS);
		AzureChatStorageUtils
		.createTable(AzureChatConstants.TABLE_NAME_MESSAGE_LIKES);
		AzureChatStorageUtils
		.createTable(AzureChatConstants.TABLE_NAME_USER_MESSAGE);
		AzureChatStorageUtils
		.createQueue(AzureChatConstants.QUEUE_NAME_MESSAGE);
		LOGGER.info("[FriendRequestService][init]          end");
	}
	
	/**
	 * This method retrieve the user content i.e. messages and phoho's/vedio's
	 * shared by user and his direct friends.
	 * 
	 * @param baseBean
	 * @return
	 * @throws AzureChatBusinessException
	 */
	public UserBean getUserContent(UserBean userBean)
			throws AzureChatBusinessException {
		LOGGER.info("[ContentShareService][getUserContent]start");
		try {
			// TODO Add the content retrieval code here...
			getStaticUserContent(userBean);
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching user content for user : "
					+ userBean.getFirstName()
					+ " Exception message is : "
					+ e.getMessage());
			throw new AzureChatBusinessException(
					"Exception occured while fetching user content for user : "
							+ userBean.getFirstName()
							+ " Exception message is : " + e.getMessage());
		}
		LOGGER.info("[ContentShareService][getUserContent]start");
		return userBean;
	}

	/**
	 * 
	 * @param userBean
	 * @return
	 * @throws AzureChatException
	 */
	private BaseBean shareContent(UserBean userBean) throws AzureChatException {
		LOGGER.info("[ContentShareService][shareContent] start");
		// TODO DAO Code to store the content in the user table.

		LOGGER.info("[ContentShareService][shareContent] end");
		return userBean;
	}

	/**
	 * Set static data to the userBean.
	 * 
	 * @param userBean
	 * @throws Exception
	 */
	private void getStaticUserContent(UserBean userBean) throws Exception {
		List<UserMessageBean> userMessageBeans = new ArrayList<UserMessageBean>();
		List<UserMessageEntity> userMessageEntities = userMessageEntityDAO.
												getUserAndFriendsMessages(userBean.getUserID());
		for(UserMessageEntity userMessageEntity : userMessageEntities){
			userMessageBeans.add(buildUserMessageBean(userMessageEntity));
		}
		UserMessageListBean userMessageListBean = new UserMessageListBean();
		userMessageListBean.setUserMsgList(userMessageBeans);
		userBean.setUserMessageListBean(userMessageListBean);
	}
	
	private UserMessageBean buildUserMessageBean(UserMessageEntity userMessageEntity) throws Exception{
		UserMessageBean userMessageBean = new UserMessageBean();
		userMessageBean.setMediaType(userMessageEntity.getMediaType());
		userMessageBean.setMediaUrl(userMessageEntity.getMediaURL());
		userMessageBean.setMsgID(userMessageEntity.getMessageID());
		userMessageBean.setMsgText(userMessageEntity.getTextContent());
		userMessageBean.setOwnerID(userMessageEntity.getUserID());
		userMessageBean.setOwnerName(userMessageEntity.getUserName());
		userMessageBean.setPhotoUrl(userMessageEntity.getUserPhotoBlobURL()+"?"+profileImageRequestDAO.getSignatureForPrivateAccess());
		return userMessageBean;
	}
}
