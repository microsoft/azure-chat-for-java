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
package com.microsoftopentechnologies.azchat.web.mediaservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.media.MediaConfiguration;
import com.microsoft.windowsazure.services.media.MediaContract;
import com.microsoft.windowsazure.services.media.MediaService;
import com.microsoft.windowsazure.services.media.WritableBlobContainerContract;
import com.microsoft.windowsazure.services.media.models.AccessPolicy;
import com.microsoft.windowsazure.services.media.models.AccessPolicyInfo;
import com.microsoft.windowsazure.services.media.models.AccessPolicyPermission;
import com.microsoft.windowsazure.services.media.models.Asset;
import com.microsoft.windowsazure.services.media.models.AssetFile;
import com.microsoft.windowsazure.services.media.models.AssetFileInfo;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.microsoft.windowsazure.services.media.models.ErrorDetail;
import com.microsoft.windowsazure.services.media.models.Job;
import com.microsoft.windowsazure.services.media.models.JobInfo;
import com.microsoft.windowsazure.services.media.models.JobState;
import com.microsoft.windowsazure.services.media.models.ListResult;
import com.microsoft.windowsazure.services.media.models.Locator;
import com.microsoft.windowsazure.services.media.models.LocatorInfo;
import com.microsoft.windowsazure.services.media.models.LocatorType;
import com.microsoft.windowsazure.services.media.models.MediaProcessor;
import com.microsoft.windowsazure.services.media.models.MediaProcessorInfo;
import com.microsoft.windowsazure.services.media.models.Task;
import com.microsoft.windowsazure.services.media.models.TaskInfo;
import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatStartupUtils;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.data.beans.MediaServiceOutputBean;

/**
 * This class provides operations for performing media service operations on
 * AzChat video message by using azure media service API.
 * 
 * @author prajakta_pednekar
 *
 */
@Service
public class AzureChatMediaService {
	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatMediaService.class);

	private static MediaContract mediaService;
	private static AccessPolicyInfo uploadAccessPolicy;
	private static MediaProcessorInfo latestWameMediaProcessor;

	@Autowired
	private AzureChatStartupUtils azureChatStartupUtils;

	/**
	 * BeanPostProcessor initialization method to create and configure media
	 * service. This method consumes the exceptions and store it in application
	 * context using azure startup utils.
	 * 
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("[AzureChatMediaServices][init] start");
		String excpMsg = null;
		try {
			LOGGER.debug("Media Service initialization started.");
			excpMsg = AzureChatUtils
					.getProperty(AzureChatConstants.EXCEP_MSG_STARTUP_MEDIA_SERVICE);
			String mediaServiceUri = AzureChatConstants.MEDIA_SERVICE_URI;
			String oAuthUri = AzureChatConstants.MEDIA_SERVICE_OAUTHURI;
			String scope = AzureChatConstants.MEDIA_SERVICE_SCOPE;
			String accName = AzureChatUtils
					.getProperty(AzureChatConstants.MEDIA_SERVICE_ACCOUNTNAME);
			String accKey = AzureChatUtils
					.getProperty(AzureChatConstants.MEDIA_SERVICE_PRIKEY);
			LOGGER.debug("URI : " + mediaServiceUri + " Auth URI : " + oAuthUri
					+ " Scope : " + scope + " Account Name : " + accKey);
			// Specify the configuration values to use with the MediaContract
			// object.
			Configuration configuration = MediaConfiguration
					.configureWithOAuthAuthentication(mediaServiceUri,
							oAuthUri, accName, accKey, scope);
			// Create the MediaContract object using the specified
			// configuration.
			mediaService = MediaService.create(configuration);
			LOGGER.debug("Media Service created.");
			/*
			 * AccessPolicy It defines the permissions and duration of access
			 * (DurationInMinutes) to an Asset. None = 0, Read = 1, Write = 2,
			 * Delete = 4, List = 8 The default value is 0. Create an access
			 * policy that provides Write access for 30 minutes.
			 */
			uploadAccessPolicy = mediaService.create(AccessPolicy.create(
					AzureChatConstants.MEDIA_SERVICE_ACCESS_POLICY_NAME,
					AzureChatConstants.MEDIA_SERVICE_ACCESS_POLICY_DURATION,
					EnumSet.of(AccessPolicyPermission.WRITE)));
			LOGGER.debug("Upload Access Policy created with ID: "
					+ uploadAccessPolicy.getId());
			ListResult<MediaProcessorInfo> processors = mediaService
					.list(MediaProcessor.list());
			/*
			 * Azure Media Encoder Windows Azure Media Packager Windows Azure
			 * Media Encryptor Azure Media Indexer Storage Decryption.
			 */
			for (MediaProcessorInfo procInfo : processors) {
				if (procInfo.getName().equals("Windows Azure Media Encoder")) {
					if (latestWameMediaProcessor == null
							|| procInfo.getVersion().compareTo(
									latestWameMediaProcessor.getVersion()) > 0) {
						latestWameMediaProcessor = procInfo;
					}

				}
			}
			LOGGER.error("Processor intitialized.  Name : "
					+ latestWameMediaProcessor.getName() + "  Version : "
					+ latestWameMediaProcessor.getVersion());
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating azure service bus queue. Exception Message :"
					+ e.getMessage());
			azureChatStartupUtils.populateStartupErrors(new AzureChatException(
					AzureChatConstants.EXCEP_CODE_SYSTEM_EXCEPTION,
					AzureChatConstants.EXCEP_TYPE_SYSTYEM_EXCEPTION, excpMsg
							+ e.getMessage()));
		}
		LOGGER.info("[AzureChatMediaServices][init] end");
	}

	/**
	 * This method perform utility and media service operations on the input
	 * blob URL.
	 * 
	 * @param videoBlobURL
	 * @param activeMinutes
	 * @return
	 * @throws Exception
	 */
	public MediaServiceOutputBean performMediaServicesOperations(
			String videoBlobURL, double activeMinutes) throws Exception {
		LOGGER.info("[AzureChatMediaServices][performMediaServicesOperations] start");
		MediaServiceOutputBean output = null;
		String videoFileName = AzureChatUtils.getFileNameFromURL(videoBlobURL);
		LOGGER.debug("Video file blob URL : " + videoBlobURL
				+ "Video file name : " + videoFileName);
		videoFileName = AzureChatUtils
				.removeSpecialCharactersFromFileName(videoFileName);
		String fileNameWithoutExt = AzureChatUtils
				.getFileNameWithoutExtention(videoFileName);
		LOGGER.debug("Video file name without extention : "
				+ fileNameWithoutExt);
		File downloadedFile = downloadVideoBlob(videoBlobURL, videoFileName);
		InputStream inputStream = new FileInputStream(downloadedFile);
		AssetInfo assetToEncode = uploadMedia(videoFileName, inputStream,
				fileNameWithoutExt);
		AssetInfo encodedAsset = encode(assetToEncode, fileNameWithoutExt);
		String streamingUrl = stream(activeMinutes, encodedAsset);
		// delete temporarily stored file.
		if (downloadedFile.exists()) {
			downloadedFile.delete();
		}
		output = new MediaServiceOutputBean(streamingUrl, encodedAsset.getId());
		LOGGER.debug("Streaming URL : " + streamingUrl);
		LOGGER.info("[AzureChatMediaServices][performMediaServicesOperations] end");
		return output;
	}

	/**
	 * Method will delete asset i.e. video files from media service and its
	 * associated storage account.
	 * 
	 * @param assetToDeleteId
	 * @throws ServiceException
	 * @throws AzureChatException
	 * @author prajakta_pednekar
	 */
	public static void deleteAsset(String assetToDeleteId) throws Exception {
		LOGGER.info("[AzureChatMediaServices][deleteAsset] start");
		List<AssetInfo> assets = mediaService.list(Asset.list());
		for (AssetInfo asset : assets) {
			String id = asset.getId();
			if (id.equals(assetToDeleteId)) {
				mediaService.delete(Asset.delete(id));
				LOGGER.debug(" Deleted asset with ID : " + id);
				LOGGER.info("[AzureChatMediaServices][deleteAsset] end");
				break;
			}
		}
		LOGGER.info("[AzureChatMediaServices][deleteAsset] end");
	}

	/**
	 * This method is used to download video from input blob URL and store to to
	 * the Operating System temporary folder.
	 * 
	 * @param videoBlobURL
	 * @param videoFileName
	 * @return
	 * @throws Exception
	 */
	private File downloadVideoBlob(String videoBlobURL, String videoFileName)
			throws Exception {
		LOGGER.info("[AzureChatMediaServices][downloadVideoBlob] start");
		URI endpointUri = new URI(
				AzureChatUtils.getServiceEndpointUrl(videoBlobURL));
		LOGGER.debug("Endpoint URI : " + endpointUri);
		CloudBlobClient blobClient = new CloudBlobClient(endpointUri);
		String tempLocForVideoDownload = String.format(
				AzureChatConstants.STRING_FORMATTER_VED_DOWNLOAD_LOCTION,
				System.getProperty(AzureChatConstants.GET_TEMP_DIRECTORY),
				AzureChatConstants.TEMP_VIDEO_FOLDER_NAME);
		LOGGER.debug("Temporary video download location : "
				+ tempLocForVideoDownload);
		File tempVideoFolder = new File(tempLocForVideoDownload);
		if (!tempVideoFolder.exists()) {
			tempVideoFolder.mkdir();
		}
		String videoFileLocation = tempLocForVideoDownload + File.separator
				+ videoFileName;
		LOGGER.debug("Temporary video file location : " + videoFileLocation);
		CloudBlockBlob sasBlob = new CloudBlockBlob(new URI(videoBlobURL),
				blobClient);
		File fileTarget = new File(videoFileLocation);
		sasBlob.download(new FileOutputStream(fileTarget));
		LOGGER.info("[AzureChatMediaServices][downloadVideoBlob] end");
		return fileTarget;
	}

	/**
	 * This method upload video file to the azure blob storage and start the
	 * media service encoding for the corresponding file
	 * 
	 * @param videoFileName
	 * @param inputStream
	 * @param fileNameWithoutExt
	 * @return
	 * @throws Exception
	 */
	private AssetInfo uploadMedia(String videoFileName,
			InputStream inputStream, String fileNameWithoutExt)
			throws Exception {
		LOGGER.info("[AzureChatMediaServices][uploadMedia] start");
		WritableBlobContainerContract uploader;
		LocatorInfo uploadLocator = null;
		AssetInfo assetToEncode = mediaService
				.create(Asset
						.create()
						.setName(
								fileNameWithoutExt
										+ AzureChatConstants.MEDIA_SERVICE_CREATE_ASSET_APPENDER)
						.setAlternateId(
								AzureChatConstants.MEDIA_SERVICE_ASSET_ATTR_ALTID));
		LOGGER.info("Asset created with id: " + assetToEncode.getId());
		/*
		 * Locator provides an entry point/location information to access the
		 * files contained in an Asset. Create a locator using the access policy
		 * and asset. Shared Access Signature (Sas) None = 0 : No valid locator
		 * will have this type SAS = 1 : Specifies Shared Access Signature (Sas)
		 * locator type OnDemandOrigin = 2 : On Demand Origin streaming endpoint
		 */
		uploadLocator = mediaService.create(Locator.create(
				uploadAccessPolicy.getId(), assetToEncode.getId(),
				LocatorType.SAS));
		LOGGER.debug("UploadLocator created.");
		// Create the blob writer using the locator.
		uploader = mediaService.createBlobWriter(uploadLocator);
		LOGGER.debug("Blob writer created.");
		// Upload the local file to the asset.
		uploader.createBlockBlob(videoFileName, inputStream);
		LOGGER.debug(videoFileName + " file is uploaded.");
		/*
		 * The AssetFile entity represents an actual file that is stored in a
		 * blob container.
		 */
		// Inform Media Services about the uploaded files.
		mediaService.action(AssetFile.createFileInfos(assetToEncode.getId()));
		LOGGER.debug("Media service action started");
		// Delete upload locator
		mediaService.delete(Locator.delete(uploadLocator.getId()));
		LOGGER.info("[AzureChatMediaServices][uploadMedia] end");
		return assetToEncode;
	}

	/**
	 * This method encode video file using azure media service API.
	 * 
	 * @param assetToEncode
	 * @param fileNameWithoutExt
	 * @return
	 * @throws Exception
	 */
	private AssetInfo encode(AssetInfo assetToEncode, String fileNameWithoutExt)
			throws Exception {
		LOGGER.info("[AzureChatMediaServices][encode] start");
		Task.CreateBatchOperation task = Task
				.create(latestWameMediaProcessor.getId(),
						AzureChatConstants.MEDIA_SERVICE_TASK_BODY)
				.setConfiguration(
						AzureChatConstants.MEDIA_SERVICE_ENCODING_TASK_CONF)
				.setName(
						fileNameWithoutExt
								+ AzureChatConstants.MEDIA_SERVICE_TASK_NAME_APPENDER);
		String uploadedAssetId = assetToEncode.getId();
		LOGGER.debug("Task created for asset ID : " + uploadedAssetId);
		// Create a job creator that specifies the asset, priority and task for
		// the job.
		Job.Creator jobCreator = Job
				.create()
				.setName(
						fileNameWithoutExt
								+ AzureChatConstants.MEDIA_SERVICE_JOB_NAME_APPENDER)
				.addInputMediaAsset(uploadedAssetId)
				.setPriority(AzureChatConstants.CONSTANT_INT_TWO)
				.addTaskCreator(task);
		// Create the job within your Media Services account.
		// Creating the job automatically schedules and runs it.
		JobInfo job = mediaService.create(jobCreator);
		LOGGER.info("Created and started Job with id: " + job.getId());
		while (true) {
			Thread.sleep(10000);
			JobInfo currentJob = mediaService.get(Job.get(job.getId()));
			JobState state = currentJob.getState();
			if (state == JobState.Finished || state == JobState.Canceled
					|| state == JobState.Error) {
				break;
			}
		}
		LOGGER.debug("Finished Job with state : " + job.getState().toString());
		job = mediaService.get(Job.get(job.getId()));
		if (job.getState() == JobState.Error) {
			ListResult<TaskInfo> tasks = mediaService.list(Task.list(job
					.getTasksLink()));
			for (TaskInfo task1 : tasks) {
				LOGGER.debug("Task status for " + task1.getName());
				for (ErrorDetail detail : task1.getErrorDetails()) {
					System.out.println(detail.getMessage());
				}
			}
		}
		// delete raw video
		mediaService.delete(Asset.delete(uploadedAssetId));
		LOGGER.debug(uploadedAssetId
				+ " asset containing raw video has been deleted.");
		ListResult<AssetInfo> outputAssets = mediaService.list(Asset.list(job
				.getOutputAssetsLink()));
		LOGGER.info("[AzureChatMediaServices][encode] end");
		return outputAssets.get(0);
	}

	/**
	 * This method extracts and return the streaming URL from AssetInfo object.
	 * 
	 * @param activeMinutes
	 * @param encodedAsset
	 * @return
	 * @throws Exception
	 */
	private String stream(double activeMinutes, AssetInfo encodedAsset)
			throws Exception {
		LOGGER.info("[AzureChatMediaServices][stream] start");
		AssetInfo streamingAsset = encodedAsset;
		AssetFileInfo streamingAssetFile = null;
		String streamingUrl = AzureChatConstants.CONSTANT_EMPTY_STRING;
		LOGGER.debug("Streaming asset : " + streamingAsset);
		// activeMinutes - minutes for which streaming url would be active
		AccessPolicyInfo streaming = mediaService.create(AccessPolicy.create(
				streamingAsset.getName(), activeMinutes, EnumSet.of(
						AccessPolicyPermission.READ,
						AccessPolicyPermission.LIST)));
		ListResult<AssetFileInfo> assetFiles = mediaService.list(AssetFile
				.list(streamingAsset.getAssetFilesLink()));
		LOGGER.debug(" Search Streaming asset info file name ends with m3u8-aapl.ism");
		streamingAssetFile = getAssetFileInfo(assetFiles,
				AzureChatConstants.MEDIA_SERVICE_ASSET_FILE__FORMAT_DEF);
		if (streamingAssetFile != null) {
			streamingUrl = getStreamingURL(streaming, streamingAssetFile,
					streamingAsset,
					AzureChatConstants.MEDIA_SERVICE_FILE_FORMAT_AAPL);
			LOGGER.debug("Found streaming info file.Streaming URL is : "
					+ streamingUrl);
		}
		if (AzureChatUtils.isEmptyOrNull(streamingUrl)) {
			LOGGER.debug(" Search Streaming asset info file name ends with .ism");
			streamingAssetFile = getAssetFileInfo(assetFiles,
					AzureChatConstants.MEDIA_SERVICE_FILE_EXTENTION_ISM);
			if (streamingAssetFile != null) {
				streamingUrl = getStreamingURL(streaming, streamingAssetFile,
						streamingAsset,
						AzureChatConstants.MEDIA_SERVICE_FILE_FORMAT_CSF);
				LOGGER.debug("Found streaming info file.Streaming URL is : "
						+ streamingUrl);
			}
		}
		if (AzureChatUtils.isEmptyOrNull(streamingUrl)) {
			LOGGER.debug(" Search Streaming asset info file name ends with .mp4");
			streamingAssetFile = getAssetFileInfo(assetFiles,
					AzureChatConstants.MEDIA_SERVICE_FILE_EXTENTION_MP4);
			if (streamingAssetFile != null) {
				streamingUrl = getMP4StreamngURL(streaming, streamingAssetFile,
						streamingAsset);
				LOGGER.debug("Found streaming info file.Streaming URL is : "
						+ streamingUrl);
			}
		}
		LOGGER.info("[AzureChatMediaServices][stream] end");
		return streamingUrl;
	}

	/**
	 * This method iterate asset file info list and if matches the file name
	 * with input extension return the asset info file object.
	 * 
	 * @param streamingURL
	 * @param assetFiles
	 * @param fileExtn
	 * @return
	 */
	private AssetFileInfo getAssetFileInfo(
			ListResult<AssetFileInfo> assetFiles, String fileExtn) {
		if (null != assetFiles) {
			for (AssetFileInfo file : assetFiles) {
				if (null != file
						&& file.getName().toLowerCase().endsWith(fileExtn)) {
					return file;
				}
			}
		}
		return null;
	}

	/**
	 * This method retrieve the streaming URL from assetInfoFile.
	 * 
	 * @param streaming
	 * @param streamingAssetFile
	 * @param streamingAsset
	 * @param fileFormat
	 * @return
	 * @throws Exception
	 */
	private String getStreamingURL(AccessPolicyInfo streaming,
			AssetFileInfo streamingAssetFile, AssetInfo streamingAsset,
			String fileFormat) throws Exception {
		String streamingURL = null;
		LocatorInfo locator = mediaService.create(Locator.create(
				streaming.getId(), streamingAsset.getId(),
				LocatorType.OnDemandOrigin));
		URI uri = new URI(locator.getPath() + streamingAssetFile.getName()
				+ fileFormat);
		streamingURL = uri.toString();
		return streamingURL;
	}

	/**
	 * This method retrieve the MP4 streaming URL from assetInfoFile.
	 * 
	 * @param streaming
	 * @param streamingAssetFile
	 * @param streamingAsset
	 * @return
	 * @throws Exception
	 */
	private String getMP4StreamngURL(AccessPolicyInfo streaming,
			AssetFileInfo streamingAssetFile, AssetInfo streamingAsset)
			throws Exception {
		String streamingURL = null;
		LocatorInfo locator = mediaService.create(Locator.create(
				streaming.getId(), streamingAsset.getId(), LocatorType.SAS));
		URI mp4Uri = new URI(locator.getPath());
		mp4Uri = new URI(mp4Uri.getScheme(), mp4Uri.getUserInfo(),
				mp4Uri.getHost(), mp4Uri.getPort(), mp4Uri.getPath()
						+ AzureChatConstants.CONSTANT_BACK_SLASH
						+ streamingAssetFile.getName(), mp4Uri.getQuery(),
				mp4Uri.getFragment());
		streamingURL = mp4Uri.toString();
		return streamingURL;

	}
}
