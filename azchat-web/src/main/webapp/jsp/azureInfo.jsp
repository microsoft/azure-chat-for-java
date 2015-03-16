<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="row">
		<div class="col-lg-12 color-black">
			<p style="font-size: 15px;margin-top: 10px;font-style: italic;font-weight: bold;">
				<spring:message code="about.message" />
			</p>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-12 color-black">
			<h6 style="font-size: 14px; font-family: sans-serif;">
				<spring:message code="refer.link.message" />
			</h6>
		</div>
		</div>
			<div class="row">
				<div class="col-sm-6 col-md-4 col-lg-4">
					<div class="thumbnail">
						<img src="images/Azure-automation.png" alt="Azure Automation" width="100px" height="100px">
						<div class="caption text-center">
							<label><spring:message code="label.compute" /></label>
						</div>
					</div>
					<ul style="font-size: 13px; font-family: sans-serif;">
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/web-sites-java-get-started/"><spring:message
									code="label.href.create.web.sites" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/virtual-machines-java-run-tomcat-application-server/"><spring:message
									code="label.href.tomcat" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/virtual-machines-java-run-compute-intensive-task/"><spring:message
									code="label.href.VM" /></a></li>
						<li><a
							href="http://msdn.microsoft.com/library/azure/hh690944(VS.103).aspx"><spring:message
									code="label.href.eclipse" /></a></li>
						<li><a href="http://petclinic.cloudapp.net/"><spring:message
									code="label.href.spring" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/develop/java/compute/"><spring:message
									code="label.href.show.all" /></a></li>
					</ul>
				</div>
				<div class="col-sm-6 col-md-4 col-lg-4">
					<div class="thumbnail">
						<img src="images/Azure-SQL.png" alt="Azure SQl Database" width="100px" height="100px">
						<div class="caption text-center">
							<label><spring:message code="label.data.services" /></label>
						</div>
					</div>
					<ul style="font-size: 13px; font-family: sans-serif;">
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-blob-storage/"><spring:message
									code="label.href.store.data.blob" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-table-storage/"><spring:message
									code="label.href.store.data.table" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/sql-data-java-how-to-use-sql-database/"><spring:message
									code="label.href.store.data.db" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/storage-java-use-blob-storage-on-premises-app/"><spring:message
									code="label.href.store.on.perm" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/sql-database-manage-azure-ssms/"><spring:message
									code="label.href.manage.sql.db" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/develop/java/data/"><spring:message
									code="label.href.show.all" /></a></li>
					</ul>
				</div>
				<div class="col-sm-6 col-md-4 col-lg-4">
					<div class="thumbnail">
						<img src="images/VM-symbol.png" alt="App Services" width="100px" height="100px">
						<div class="caption text-center">
							<label><spring:message code="label.app.services" /></label>
						</div>
					</div>
					<ul style="font-size: 13px; font-family: sans-serif;">
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/service-bus-java-how-to-use-queues/"><spring:message
									code="label.href.msg.bet.app" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/active-directory-java-authenticate-users-access-control-eclipse/"><spring:message
									code="label.href.auth.usera" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/store-sendgrid-java-how-to-send-email/"><spring:message
									code="label.href.send.email.grid" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/documentation/articles/partner-twilio-java-how-to-use-voice-sms/"><spring:message
									code="label.href.use.twilio" /></a></li>
						<li><a
							href="http://azure.microsoft.com/en-us/develop/java/app-services/"><spring:message
									code="label.href.show.all" /></a></li>
					</ul>
				</div>
			</div>
</body>
</html>