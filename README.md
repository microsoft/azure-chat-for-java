#Azure-chat-for-java
This readme outlines steps for configuring and deploying “azure-chat-for-java” application.

##Application summary:
Azure Chat for Java, is simple Java-based photo and video sharing application. 
Following Azure services are used in the application.

a)	Azure Access control service.

b)	Azure Storage Service– Blob , queue and table

c)	SQL Azure

d)	Azure service bus queues

e)	Azure media services.

##Software Requirements:
In order to run the application you need to install following items:

1.	 JDK 1.8
2.	 Eclipse Luna 
3.	 Any JEE web container
4.	 Spring 4.1

##Steps to deploy

1.	Import project into Eclipse
2.	Configure JEE server
3.	Create Azure services
4.	Configuration application properties
5.	Run application locally or in Azure cloud.

##Azure Services configuration 
Following Azure services needs to be created from Azure management portal

1. Access control name space
	* Sign into the Azure Management Portal.
    * Click New
    * App Services 
    * Access Control  
    * Quick Create. 
	* Fill required information and create a name space.
	* Click on manage to define configuration for Access control service.
	* Click on identity providers and add identity providers. Application currently supports Live, Yahoo and Google. Please note that Google deprecated OpenID 2.0 support , so newly created ACS namespaces may not support Google as an identity provider.
    *	Click on relying party applications and configure properties as below
    
    	a)	For Name: Enter any valid name.
        
        b)	For Realm: Enter URI of your application. Please note that the same URI is used to forward user to home page after logout.
E.g. For local deployment, use http://localhost:8080/azchat-web/
       For cloud deployment, use http://<cloudServiceName>.cloudapp.net/azchat-web/
       
		c)	For Return URL: Enter URI of application landing page
E.g. For local deployment, use http://localhost:8080/azchat-web/login.htm
      For cloud deployment, use http://<cloudServiceName>.cloudapp.net/azchat-web/login.htm

		d)	For Error URL: Enter URI of application error page
E.g. For local deployment, use http://localhost:8080/azchat-web/error.htm
      For cloud deployment, use http://<cloudServiceName>.cloudapp.net/azchat-web/error.htm
		e)	For Token lifetime: change from default 5 minutes to 60 minutes
     
             
 * Click on Rule groups 
 
  * Select relying party application 
  * Generate 
  * Save
        
 * Click on certificate and Keys -> Add -> Choose sample pfx file provided in project , for password enter “Abcd.1234” and click on save.
    
    Alternatively you can create your own certs using Eclipse plugin. In case if you are creating your own certs, ensure to copy cer file to projects WEB-INF/cert folder with name “_acs_signing.cer”
    
 * Click on Application integration -> Login pages -> and note down ACS end point.
It will be in the form of https://your ACS namespace>.accesscontrol.windows.net:443/v2/wsfederation

	* Update application web.xml
     1.	Update < PassiveRequestorEndpoint> value with ACS name space. https://<namepace name> accesscontrol.windows.net:443/v2/wsfederation
 For e.g. https://mynameapce.accesscontrol.windows.net:443/v2/wsfederation
     2.	Update RelyingPartRealm value as defined in the portal.

2.	Azure SQL Database:  
 * Log into the Azure Management Portal.
 * At the bottom of the navigation pane, click NEW.
 * Click DATA SERVICES, then STORAGE, and then click QUICK CREATE
 * Enter necessary information and create service
 * With the help of JDBC string, update the following field from “azchatResources.properties“ file
	   * db.username : name of the user
       * db.password : password of storage account
       * db.name : database name
 
3.	Azure Storage Account: 
 * Sign into the Management Portal.
 * Click Create New, click Storage, and then click Quick Create.
 * Fill the necessary fields and create storage account.
 * With the help of portal, update following fields from “azchatResources.properties” file
 	   * storage.account.name : Name of the storage account.
       * storage.account.key : Primary access key of storage account
       
4.	Azure service bus:
 * Sign into the Management Portal.
 * In the left navigation pane of the Management Portal, click Service Bus.
 * In the lower pane of the Management Portal, click Create.
 * Fill the necessary fields, create Azure service bus.
 * With the help of connection information on portal, update following field from “azchatResources.properties” file
 
 	   * service.bus.namespace : name space of Azure service bus.
       * service.bus.sasKeyName : shared access key name and can be obtained from connection string
       * service.bus.sasKey : Shared access key and it can be obtained from connection string
       
       
5. Azure media service:
 * Sign into the Management Portal.
 * Click New, click Media Service, and then click Quick Create.
 * Fill the necessary fields and create media service
•	Scale media service. Details can be obtained from http://azure.microsoft.com/en-us/documentation/articles/media-services-how-to-scale/. On demand streaming feature, only works after scaling media service.
 * Get media service name and primary key from dashboard. Put values in “azchatResources.properties” file.
 	   * media.service.accName : media service account name
       * media.service.primarykey : Primary media service access key

 NOTE:  For more details on Azure Storage Account, please refer following link
http://azure.microsoft.com/en-in/documentation/articles/storage-create-storage-account/
       

#Local Deployment:
To deploy the application locally, user needs to update the settings given below.
 * Edit Relying Party Application on portal for local setting.
 * Edit RelyingPartRealm in web.xml.
 * Add LOG4J_HOME environment variable
 * Configure necessary firewall rules needs to be added in the portal to allow access for the IP
 
#Azure deployment:
 * Edit Relying Party Application on portal for azure settings
 * Edit RelyingPartRealm in web.xml.
 * Install Azure Eclipse plugin for java using update site http://dl.msopentech.com/eclipse.
 * Create azure deployment project.
 * Add LOG4J_HOME environment variable in deployment project
 * Deploy on Azure.
 * For more details please refer https://msdn.microsoft.com/en-us/library/azure/hh690944.aspx
