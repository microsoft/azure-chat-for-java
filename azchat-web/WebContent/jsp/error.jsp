<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
</head>
<body>
	<!-- Render Business Errors  -->
	<c:if test="${baseBean.errorList.excpCode == 'BE' || baseBean.errorList.excpCode == 'JE'}">
		<div id="bussErrors" class="azchat-div">
			<c:forEach items="${baseBean.errorList.errorList}" varStatus="index" var="error">
				<div>
					<font size="1px;" color="red">${error.excpMsg}</font><br/>
				</div>
			</c:forEach>
		</div>
	</c:if>
	<!-- Render System Errors  -->
	<c:if test="${baseBean.errorList.excpCode == 'SE'}">
		<div id="sysErrors" class="azchat-div">
			<div class="container">
				<div class="form-inline" align="center" style="margin-top: 100px;">
				<img alt="alert" src="images/Azure-alert.png" style="width: 70px;height: 70px;">
					<h1><label class="text-danger"><spring:message code="label.error.page.sys.err.heading"/></label></h1>
		  	<c:forEach items="${baseBean.errorList.errorList}" varStatus="index" var="error">
				<div>		
					<font class="text-danger" size="4px;" style="text-align: center;">${error.excpMsg}</font><br/>
				</div>
			</c:forEach>
				<p class="text-danger" style="margin-top: 30px;"><spring:message code="label.error.page.contact.admin"/></p>
				</div>
			</div>
		</div>
	</c:if>
</body>
</html>