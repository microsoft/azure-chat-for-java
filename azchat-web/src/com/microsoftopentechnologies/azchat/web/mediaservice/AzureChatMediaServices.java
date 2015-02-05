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
package com.microsoftopentechnologies.azchat.web.mediaservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.azure.storage.StorageException;
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
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.data.beans.MediaServiceOutputBean;

/**
 * This class if for handling media services.
 * 
 * @author prajakta_pednekar
 *
 */
public class AzureChatMediaServices {
	private static MediaContract mediaService;
	private static AccessPolicyInfo uploadAccessPolicy;
	private static MediaProcessorInfo latestWameMediaProcessor;

	private static final Logger LOGGER = LogManager
			.getLogger(AzureChatMediaServices.class);

	/**
	 * This method is handling media service operation for given given URL
	 * 
	 * @param videoBlobURL
	 * @param activeMinutes
	 * @return
	 * @throws URISyntaxException
	 * @throws ServiceException
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InterruptedException
	 * @throws StorageException
	 * @throws AzureChatException
	 * @author prajakta_pednekar
	 */
	public static MediaServiceOutputBean performMediaServicesOperations(
			String videoBlobURL, double activeMinutes)
			throws URISyntaxException, ServiceException, FileNotFoundException,
			NoSuchAlgorithmException, InterruptedException, StorageException,
			AzureChatException {
		AzureChatMediaServices object = getInstance();
		MediaServiceOutputBean output = null;
		if (object != null) {
			String videoFileName = object
					.removeSpecialCharactersFromFileName(object
							.getFileNameFromURL(videoBlobURL));
			String fileNameWithoutExt = videoFileName;
			if (fileNameWithoutExt.contains(".")) {
				fileNameWithoutExt = fileNameWithoutExt.substring(0,
						fileNameWithoutExt.lastIndexOf("."));
			}
			File downloadedFile = object.downloadVideoBlob(videoBlobURL,
					videoFileName);
			InputStream inputStream = new FileInputStream(downloadedFile);
			AssetInfo assetToEncode = object.uploadMedia(videoFileName,
					inputStream, fileNameWithoutExt);
			AssetInfo encodedAsset = object.encode(assetToEncode,
					fileNameWithoutExt);
			String streamingUrl = object.stream(activeMinutes, encodedAsset);
			// delete temporarily stored file.
			if (downloadedFile.exists()) {
				downloadedFile.delete();
			}
			output = new MediaServiceOutputBean(streamingUrl,
					encodedAsset.getId());
			LOGGER.info("[AzureChatMediaServices][performMediaServicesOperations] Streaming URL : "
					+ streamingUrl);
		} else {
			throw new AzureChatException(
					"Unable to create media service object.");
		}
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
	public static void deleteAsset(String assetToDeleteId)
			throws ServiceException, AzureChatException {
		AzureChatMediaServices object = getInstance();
		if (object != null) {
			// Retrieve a list of all assets from media service account.
			List<AssetInfo> assets = mediaService.list(Asset.list());
			for (AssetInfo asset : assets) {
				String id = asset.getId();
				if (id.equals(assetToDeleteId)) {
					mediaService.delete(Asset.delete(id));
					LOGGER.info("[AzureChatMediaServices][deleteAsset] Deleted asset with ID : "
							+ id);
					break;
				}
			}
		} else {
			throw new AzureChatException(
					"Unable to create media service object.");
		}
	}

	/**
	 * This method is used to download video from blob
	 * 
	 * @param videoBlobURL
	 * @param videoFileName
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws FileNotFoundException
	 */
	private File downloadVideoBlob(String videoBlobURL, String videoFileName)
			throws URISyntaxException, StorageException, FileNotFoundException {
		URI endpointUri = new URI(
				AzureChatUtils.getServiceEndpointUrl(videoBlobURL));
		CloudBlobClient blobClient = new CloudBlobClient(endpointUri);
		String tempLocForVideoDownload = String.format("%s%s",
				System.getProperty("java.io.tmpdir"), "%videos%");
		File tempVideoFolder = new File(tempLocForVideoDownload);
		if (!tempVideoFolder.exists()) {
			tempVideoFolder.mkdir();
		}
		String videoFileLocation = tempLocForVideoDownload + File.separator
				+ videoFileName;

		CloudBlockBlob sasBlob = new CloudBlockBlob(new URI(videoBlobURL),
				blobClient);
		File fileTarget = new File(videoFileLocation);
		sasBlob.download(new FileOutputStream(fileTarget));
		return fileTarget;
	}

	/**
	 * Method removes special characters from video file name, as encoding will
	 * fail if it contains '.' in file name. Only one '.' for file extension is
	 * allowed.
	 * 
	 * @param videoFileName
	 * @return
	 * @author prajakta_pednekar
	 */
	private String removeSpecialCharactersFromFileName(String videoFileName) {
		int index = videoFileName.lastIndexOf(".");
		String fileExt = videoFileName.substring(index, videoFileName.length());
		String fileNameWithoutExt = videoFileName.substring(0, index);
		String fileNameWithoutSpecialChar = fileNameWithoutExt.replaceAll(
				"[^a-zA-Z0-9]", "");
		return fileNameWithoutSpecialChar + fileExt;
	}

	/**
	 * This method will find video name from URL
	 * 
	 * @param videoBlobURL
	 * @return
	 * @author prajakta_pednekar
	 */
	private String getFileNameFromURL(String videoBlobURL) {
		String containerName = AzureChatUtils.getContainerName(videoBlobURL);
		String videoFileName = AzureChatUtils.getFileName(videoBlobURL,
				containerName);
		return videoFileName;
	}

	AzureChatMediaServices() {
		try {
			mediaService = initialize();
			/*
			 * AccessPolicy It defines the permissions and duration of access
			 * (DurationInMinutes) to an Asset. None = 0, Read = 1, Write = 2,
			 * Delete = 4, List = 8 The default value is 0. Create an access
			 * policy that provides Write access for 30 minutes.
			 */
			uploadAccessPolicy = mediaService.create(AccessPolicy.create(
					"uploadAccessPolicy", 30.0,
					EnumSet.of(AccessPolicyPermission.WRITE)));
			LOGGER.info("[AzureChatMediaServices][AzureChatMediaServices] Created upload access policy with ID: "
					+ uploadAccessPolicy.getId());
			ListResult<MediaProcessorInfo> processors = mediaService
					.list(MediaProcessor.list());
			/*
			 * Azure Media Encoder Windows Azure Media Packager Windows Azure
			 * Media Encryptor Azure Media Indexer Storage Decryption
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
			LOGGER.info("[AzureChatMediaServices][AzureChatMediaServices] Using processor: "
					+ latestWameMediaProcessor.getName()
					+ " "
					+ latestWameMediaProcessor.getVersion());
		} catch (Exception e) {
			LOGGER.info("[AzureChatMediaServices][AzureChatMediaServices] Unable to create Media contract object"
					+ e.getMessage());
		}
	}

	// Class to implement Singleton design pattern.
	private static class SingletonHelper {
		private static final AzureChatMediaServices INSTANCE = new AzureChatMediaServices();
	}

	public static AzureChatMediaServices getInstance() {
		return SingletonHelper.INSTANCE;
	}

	/**
	 * This method is for initializing media service configuration
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws AzureChatException
	 */
	private static MediaContract initialize() throws ServiceException,
			AzureChatException {
		String mediaServiceUri = AzureChatConstants.MEDIA_SERVICE_URI;
		String oAuthUri = AzureChatConstants.MEDIA_SERVICE_OAUTHURI;
		String scope = AzureChatConstants.MEDIA_SERVICE_SCOPE;
		// media service account name.
		String accName = AzureChatUtils
				.getProperty(AzureChatConstants.MEDIA_SERVICE_ACCOUNTNAME);

		// media service access key.
		String accKey = AzureChatUtils
				.getProperty(AzureChatConstants.MEDIA_SERVICE_PRIKEY);

		// Specify the configuration values to use with the MediaContract
		// object.
		Configuration configuration = MediaConfiguration
				.configureWithOAuthAuthentication(mediaServiceUri, oAuthUri,
						accName, accKey, scope);

		// Create the MediaContract object using the specified configuration.
		return MediaService.create(configuration);
	}

	/**
	 * This method will upload a media file to Media Services account. It
	 * creates an asset, an access policy (using Write access) and a locator,
	 * and uses those objects to upload a local file to the asset.
	 * 
	 * @param videoFileName
	 * @param inputStream
	 * @param fileNameWithoutExt
	 * @return
	 * @throws ServiceException
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 */
	private AssetInfo uploadMedia(String videoFileName,
			InputStream inputStream, String fileNameWithoutExt)
			throws ServiceException, FileNotFoundException,
			NoSuchAlgorithmException {
		WritableBlobContainerContract uploader;
		LocatorInfo uploadLocator = null;

		AssetInfo assetToEncode = mediaService.create(Asset.create()
				.setName(fileNameWithoutExt + "Asset").setAlternateId("altId"));
		LOGGER.info("[AzureChatMediaServices][uploadMedia] Created asset with id: "
				+ assetToEncode.getId());
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
		// Create the blob writer using the locator.
		uploader = mediaService.createBlobWriter(uploadLocator);

		// Upload the local file to the asset.
		uploader.createBlockBlob(videoFileName, inputStream);

		/*
		 * The AssetFile entity represents an actual file that is stored in a
		 * blob container.
		 */
		// Inform Media Services about the uploaded files.
		mediaService.action(AssetFile.createFileInfos(assetToEncode.getId()));

		mediaService.delete(Locator.delete(uploadLocator.getId()));
		LOGGER.info("[AzureChatMediaServices][uploadMedia] Deleting upload locator as no longer required.");
		return assetToEncode;
	}

	/**
	 * This method is to encode the given file.
	 * 
	 * @param assetToEncode
	 * @param fileNameWithoutExt
	 * @return
	 * @throws ServiceException
	 * @throws InterruptedException
	 * @author prajakta_pednekar
	 */
	private AssetInfo encode(AssetInfo assetToEncode, String fileNameWithoutExt)
			throws ServiceException, InterruptedException {
		// Preset
		/*
		 * PC / MAC / ios devices = silver light & flash =
		 * "H264 Smooth Streaming 720p" HTML 5 = "H264 Broadband 720p" Dynamic
		 * packaging = "H264 Adaptive Bitrate MP4 Set 720p"
		 */
		String encodingPreset = "H264 Adaptive Bitrate MP4 Set 720p";

		Task.CreateBatchOperation task = Task
				.create(latestWameMediaProcessor.getId(),
						"<taskBody><inputAsset>JobInputAsset(0)</inputAsset><outputAsset>JobOutputAsset(0)</outputAsset></taskBody>")
				.setConfiguration(encodingPreset)
				.setName(fileNameWithoutExt + "Task");

		String uploadedAssetId = assetToEncode.getId();
		// Create a job creator that specifies the asset, priority and task for
		// the job.
		Job.Creator jobCreator = Job.create()
				.setName(fileNameWithoutExt + "Job")
				.addInputMediaAsset(uploadedAssetId).setPriority(2)
				.addTaskCreator(task);

		// Create the job within your Media Services account.
		// Creating the job automatically schedules and runs it.
		JobInfo job = mediaService.create(jobCreator);
		LOGGER.info("[AzureChatMediaServices][encode] Created and started Job with id: "
				+ job.getId());

		while (true) {
			Thread.sleep(10000);
			JobInfo currentJob = mediaService.get(Job.get(job.getId()));
			JobState state = currentJob.getState();
			if (state == JobState.Finished || state == JobState.Canceled
					|| state == JobState.Error) {
				break;
			}
		}
		LOGGER.info("[AzureChatMediaServices][encode] Finished Job with state : "
				+ job.getState().toString());
		job = mediaService.get(Job.get(job.getId()));

		if (job.getState() == JobState.Error) {
			ListResult<TaskInfo> tasks = mediaService.list(Task.list(job
					.getTasksLink()));
			for (TaskInfo task1 : tasks) {
				System.out.println("Task status for " + task1.getName());
				for (ErrorDetail detail : task1.getErrorDetails()) {
					System.out.println(detail.getMessage());
				}
			}
		}

		// delete raw video
		mediaService.delete(Asset.delete(uploadedAssetId));
		LOGGER.info("[AzureChatMediaServices][encode] Deleting raw video file as no longer required.");

		ListResult<AssetInfo> outputAssets = mediaService.list(Asset.list(job
				.getOutputAssetsLink()));
		return outputAssets.get(0);
	}

	/**
	 * This method will return streaming URL
	 * 
	 * @param activeMinutes
	 * @param encodedAsset
	 * @return
	 * @throws URISyntaxException
	 * @throws ServiceException
	 * @author prajakta_pednekar
	 */
	private String stream(double activeMinutes, AssetInfo encodedAsset)
			throws URISyntaxException, ServiceException {
		AssetInfo streamingAsset = encodedAsset;
		// activeMinutes - minutes for which streaming url would be active
		AccessPolicyInfo streaming = mediaService.create(AccessPolicy.create(
				streamingAsset.getName(), activeMinutes, EnumSet.of(
						AccessPolicyPermission.READ,
						AccessPolicyPermission.LIST)));

		String streamingUrl = "";
		ListResult<AssetFileInfo> assetFiles = mediaService.list(AssetFile
				.list(streamingAsset.getAssetFilesLink()));
		AssetFileInfo streamingAssetFile = null;
		for (AssetFileInfo file : assetFiles) {
			if (file.getName().toLowerCase().endsWith("m3u8-aapl.ism")) {
				streamingAssetFile = file;
				break;
			}
		}

		if (streamingAssetFile != null) {
			LocatorInfo locator = mediaService.create(Locator.create(
					streaming.getId(), streamingAsset.getId(),
					LocatorType.OnDemandOrigin));
			URI hlsUri = new URI(locator.getPath()
					+ streamingAssetFile.getName()
					+ "/manifest(format=m3u8-aapl)");
			streamingUrl = hlsUri.toString();
		}
		// as we need MPEG-DASH URL append it with
		// /manifest(format=mpd-time-csf)
		if (streamingUrl.isEmpty()) {
			streamingAssetFile = null;
			for (AssetFileInfo file : assetFiles) {
				if (file.getName().toLowerCase().endsWith(".ism")) {
					streamingAssetFile = file;
					break;
				}
			}
			if (streamingAssetFile != null) {
				LocatorInfo locator = mediaService.create(Locator.create(
						streaming.getId(), streamingAsset.getId(),
						LocatorType.OnDemandOrigin));
				URI smoothUri = new URI(locator.getPath()
						+ streamingAssetFile.getName()
						+ "/manifest(format=mpd-time-csf)");
				streamingUrl = smoothUri.toString();
			}
		}

		if (streamingUrl.isEmpty()) {
			streamingAssetFile = null;
			for (AssetFileInfo file : assetFiles) {
				if (file.getName().toLowerCase().endsWith(".mp4")) {
					streamingAssetFile = file;
					break;
				}
			}
			if (streamingAssetFile != null) {

				LocatorInfo locator = mediaService.create(Locator.create(
						streaming.getId(), streamingAsset.getId(),
						LocatorType.SAS));
				URI mp4Uri = new URI(locator.getPath());
				mp4Uri = new URI(mp4Uri.getScheme(), mp4Uri.getUserInfo(),
						mp4Uri.getHost(), mp4Uri.getPort(), mp4Uri.getPath()
								+ "/" + streamingAssetFile.getName(),
						mp4Uri.getQuery(), mp4Uri.getFragment());
				streamingUrl = mp4Uri.toString();
			}
		}
		return streamingUrl;
	}
}
