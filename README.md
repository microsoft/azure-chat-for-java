# Azure-chat-for-java
This readme outlines steps for configuring and deploying the “azure-chat-for-java” application.

## Application summary:
Azure Chat for Java is a sample Java application designed to show key features of the [Microsoft Azure SDK for Java](http://azure.microsoft.com/en-us/develop/java/ "Azure Dev Center").    

 
Following [Microsoft Azure services](http://azure.microsoft.com/ "Microsoft Azure") are used in the application.


- [Azure Access Control Service (ACS)](http://azure.microsoft.com/en-us/documentation/articles/active-directory-java-authenticate-users-access-control-eclipse/ "Azure ACS") manages user identities and access to the application 

- [Azure SQL Database](http://azure.microsoft.com/en-us/documentation/articles/sql-data-java-how-to-use-sql-database/ "Azure SQL") stores user profiles
- [Azure Tables](http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-table-storage/) manage friend relationships

- [Azure Storage](http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-blob-storage/) stores media

- [Azure Media Services](http://azure.microsoft.com/en-us/documentation/articles/media-services-java-how-to-use/) manage video encoding and streaming

- [Azure Queue Storage](http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-queue-storage/) manages video uploads to storage then encodes them using media services  

- [Azure Service Bus Queues](http://azure.microsoft.com/en-us/documentation/articles/service-bus-java-how-to-use-queues/) Manage deletion of items from storage once media services has completed processing, and controls message expiry  


## Software Requirements:
In order to run the application you need to install the following items:

1.	 JDK 1.8
2.	 Apache Maven
3.	 Eclipse EE
4.	 Any major JEE Web App Server
5.	 Spring 4.1

## Steps to deploy

1.	Clone this GitHub repo
2.	Install Packages via Maven (mvn clean compile package)
3.	Import this project into Eclipse as a Maven Project
4.	Configure a JEE server (we use Tomcat for this example)
5.	Create the required Azure services as described below
6.	Configure the application properties based on the Azure services you create
7.	Run the application locally or on Microsoft Azure

## Cloning the Repo and Installing Packages via Maven

### Install the JDK, Eclipse and Maven and prepare the Eclipse Maven Project
1. Make sure [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), [Eclipse EE](https://eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2) and [Apache Maven](http://maven.apache.org/download.cgi) are installed and working on your local machine
2. Clone [this GitHub repo](http://github.com/MSOpenTech/azure-chat-for-java/) locally using your favorite Git tools
3. Run the following maven commands:

	*mvn clean compile package* imports the third party files and generates a .WAR file

	*mvn eclipse:eclipse* generates eclipse project metadata and properties 

The project is now ready to run on an App Server.

OPTIONAL - Import the package into Eclipse as a Maven Project:

* File > import > Existing Maven Project
* In Eclipse, right-click on the project then select Run As > Maven Install to build the project in Eclipse and rebuild the .WAR file   

## Azure Services configuration 
The following Azure services need to be created from the Azure management portal.  If you don't already have an Azure account you can sign up for a free trial at [http://azure.microsoft.com](http://azure.microsoft.com)  


### Create an Azure Cloud Service

1. Azure Cloud Service - you need to create a unique Azure Cloud Service name to be used with the other services below.
    * Sign into the Azure Management Portal
    * Click New > Compute > Cloud Service > Quick Create
    * Enter a Cloud Service name and an appropriate region.

### Configure the Azure Access Control Service
1. Access control name space
	* Sign into the Azure Management Portal
    * Click New > App Services > Active Directory > Access Control > Quick Create. 
	* Enter a name space and a region, click CREATE.
	* After the service has been created and activated, click on MANAGE to define the configuration for the Access control service.
	* Click on identity providers and the click and add an identity provider. The Sample application currently supports Live, Yahoo and Google. 
		* Note that Google [deprecated OpenID 2.0 support as of May 19, 2014](https://developers.google.com/accounts/docs/OpenID2 "OpenID support Deprecation") ,so ACS namespaces created after that date do not support Google as an identity provider.
    
	*Click on relying party applications and configure properties as below
   	* Name: Any valid name.  Note the name is case sensitive.
   	* For Realm: Enter URI of your Cloud Serivce that you created above. 
    * Please note that the same URI is used to forward the user to home page after logout.

	For local deployment, use http://localhost:8080/yourURI/ and for cloud deployment, use http://<cloudServiceName>.cloudapp.net/yourURI/
       
	* For the Return URL: Enter the URI of the application landing page
		* For local deployment, use http://localhost:8080/yourURI/login.htm
	    * For cloud deployment, use http://<cloudServiceName>.cloudapp.net/yourURI/login.htm

	* For the Error URL: Enter the URI of the application error page
		* For local deployment, use http://localhost:8080/yourURI/error.htm
      	* For cloud deployment, use http://<cloudServiceName>.cloudapp.net/yourURI/error.htm
  	* Leave the defaults for Token Format  (SAML 2.0) and Token Encryption Policy (None)

	* Change Token lifetime from the default 10 minutes (600 seconds) to 60 minutes (3600 seconds)

 	* Under Authentication Settings, make sure that all identity providers you set up are checked and that "Create new rule group" is checked. 

	* Under Token Signing Settings, Leave the default for Token Signing (Use service namespace certificate)

	* Click Save
 
	*Next, click on Rule Groups on left hand menu, then click on the relying party rule group you just created  
	* Select all the Identity Providers you have created, then click the Generate button
	* When the default rule group has been generated, click Save.
	* Click on certificate and Keys, then under Token Signing, select on Add > then browse for the sample pfx file provided in the project
	* Click save
		* The password for the pfx file is “Abcd.1234”
	 
    
    Alternatively you can c[reate your own certs using the Eclipse plugin](https://msdn.microsoft.com/en-us/library/azure/dn621095.aspx). In case if you are creating your own certs, ensure to copy cer file to projects WEB-INF/cert folder with name “_acs_signing.cer”
    
 * Click on Application integration -> Login pages -> and copy the ACS endpoint under "Option 1: Link to an ACS-hosted login page" to the left of the first question mark (?) to your clipboard.
 * It will be in the form of https://your ACS namespace>.accesscontrol.windows.net:443/v2/wsfederation

	### Update the web.xml in your Eclipse Project
	In Eclipse, navigate to \azure-chat-for-java-master\azchat-web\src\main\webapp\WEB-INF
     1.	Paste from the clipboard into the PassiveRequestorEndpoint init value 
     2.	Update the RelyingPartRealm value with your Cloud Service name

### Create an Azure SQL Database:  
 * Log into the Azure Management Portal
 * At the bottom of the navigation pane, click NEW > Data Services > SQL Database > Quick Create.
 * Choose New SQL Database Server
 * Select an appropriate region, choose a login name and a password.  Make a note of both.
 * When the Database is created and shows as online, 
 * Next - set up DB access by opening your Database service, then click on Configure
	 * If you plan on runnning this aplpication locally, add your current IP address to the allowed IP addresses.  
	 *  If you plan to run this application on Azure, make sure Windows Azure Services is selected as an allowed service.
 * Open the database service then click on "Show Connection Strings" on the right. 
 * Open the azchatResources.properties file, located under azure-chat-for-java-master\azchat-web\src\main\resources in the Eclipse project
 * Use the values from the JDBC connection string to update the values:
 
	* db.username: the user ID you created when the database was created
	* db.password: the password you created when the database was created
	* db.name: The name of the database you just created
	* db.url: the URL value from the JDBC String
	* db.hostNameInCertificate: *.database.windows.net
	* db.loginTimeout: 30
	* db.encrypt: false
	* db.driver.classname: com.microsoft.sqlserver.jdbc.SQLServerDriver
 
### Create an Azure Storage Account: 
 * Sign into the Management Portal.
 * Click New > Storage > Quick Create.
 * Select an appropriate storage name, region, and choose the default replication of Geo-Redundant
 * Open the azchatResources.properties file, located under azure-chat-for-java-master\azchat-web\src\main\resources in the Eclipse project
 * In the portal, open the Storage service and click on Manage Access Keys at the bottom of the screen to update the values:
 
 	   * storage.account.name : Name of the storage account.
       * storage.account.key : Primary access key of storage account
       * default.endpoint.protocol: DefaultEndpointsProtocol=http;
  * For more details on Azure Storage Accounts, click [here](http://azure.microsoft.com/en-in/documentation/articles/storage-create-storage-account/)
        
### Create an Azure Service Bus Queue
 * Sign into the Management Portal.
 * Click New > App Services > Service Bus > Queue > Quick Create.
 * In the lower pane of the Management Portal, click Create.
 * Select an appropriate queue name, region, and namespace
 * When the Service Bus Queue is created and shows as Active, open the Queue service then click on "Manage Connection Strings" on the right. 
 *Open the azchatResources.properties file, located under azure-chat-for-java-master\azchat-web\src\main\resources in the Eclipse project
 * Use the values from the Access Connection Information to update the values:
 
 	   * service.bus.namespace: The namespace you just created.
       * service.bus.sasKeyName: The shared access key name, usually RootManageSharedAccessKey
       * service.bus.sasKey: Everything to the right of SharedAccessKey= in the Connection String
       * service.bus.rooturi:.servicebus.windows.net
       
       

### Create an Azure Media Service
 * Sign into the Management Portal.
 * Click New > App Services > Media Service > Quick Create
 * Select an a
 * ppropriate name and region
 * Choose the default of Create a new Storage Account and the default Storage Account name
 
	##### Scale your Streaming Endpoints and Encoding for the Media Service
	The on demand streaming feature for Azure Chat for Java only works after you scale your media service.   
	* When the Media Service created and shows as Active, open the service then click on STREAMING ENDPOINTS form the menu along the top
	* Click on default and configure Streaming units to 2 units
	* Next, use the arrow at the left to go back to the main menu and click ENCODING
	* Configure Encoding units to 2 units    
	* When you click on STREAMING ENDPOINTS again, the default endpoint should show as Running
	* More details on scaling can be found [here](http://azure.microsoft.com/en-us/documentation/articles/media-services-how-to-scale/)  
	 

 * When the Media Service created and shows as Active, select the service then click on "Manage Keys" at the bottom of the screen.
  
 * Open the azchatResources.properties file, located under azure-chat-for-java-master\azchat-web\src\main\resources in the Eclipse project
 * Use the values from the Media Service access Keys to update the values:
 	   * media.service.accName : media service account name
       * media.service.primarykey : Primary media service access key
      

       

## Local Deployment:
To deploy the application locally, user needs to follow these steps:

### Edit Azure Access Control to point to the local application
 * Edit the Relying Party Application on the portal to point to local URLs.  
 * See the "Configure the Azure Access Control Service" section above for how to change URLs.
  
### Update the web.xml in your Eclipse Project
* In Eclipse, navigate to azure-chat-for-java-master\azchat-web\src\main\webapp\WEB-INF
	* Update the RelyingPartRealm value with your local URL 
* Edit the LOG4J_HOME environment variable
* Configure firewall rules in the portal to allow access for the local IP address

* Log into the Azure Management Portal
* Set up Azure SQL Database access by opening your Database service, then click on Configure
and add your current IP address to the allowed IP addresses

* Run maven to generate a .War file to use on an App Server:

	*mvn clean compile package* 

 
## Azure deployment:

### Edit Azure Access Control to point to the Azure application
 * Log into the Azure Management Portal
 * Edit the Relying Party Application on the portal to point to the URLs for your Azure Cloud service.  
	 * See the "Configure the Azure Access Control Service" section above for how to change URLs.
* Set up Azure SQL Database access by opening your Database service, then click on Configure, and make sure Windows Azure Services is selected as an allowed service.

  
### Update the web.xml in your Eclipse Project
* In Eclipse, navigate to azure-chat-for-java-master\azchat-web\src\main\webapp\WEB-INF
	* Update the RelyingPartRealm value with your Cloud Service name 

### Deploy your Cloud Service using the Eclipse Toolkit for Java
* [Install the Eclipse Toolkit for Java](https://msdn.microsoft.com/en-us/library/azure/hh690946.aspx)
* [Create an Azure Deployment project](https://msdn.microsoft.com/en-us/library/azure/hh690944.aspx).
	* Add a LOG4J_HOME environment variable in the deployment project 


* Run Maven to build the packages to use on Azure:

	*mvn clean compile package* 

* Deploy using the Azure Toolkit for Eclipse as outlined [at the end of this tutorial](https://msdn.microsoft.com/en-us/library/azure/hh690944.aspx)
