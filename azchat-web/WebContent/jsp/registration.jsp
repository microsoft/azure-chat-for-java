<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Registration</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/registration.js"></script>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/common.js"></script>
<link href="${pageContext.request.contextPath}/css/azchatstyles.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<style>
 .header {
	color: #36A0FF;
	font-size: 27px;
	padding: 10px;
}

.bigicon {
	font-size: 35px;
	color: #36A0FF;
}

.typeahead {
	background-color: #FFFFFF;
	width: 545px;
}

.typeahead:focus {
	border: 1px solid #0097CF;
}
</style>
</head>
<body>
	<div class="azchat-div">
		<div class="container">
			<nav id="index_nav1" class="navbar" role="navigation">
			<div class="row navbar-custom">
				<div class="col-sm-6 col-md-4 col-lg-3">
					<div class="navbar-header">
						<div class="img col-sm-6 col-md-4 col-lg-3">
							<a target="_blank" class="nav navbar-nav" href="#"><img
								src="${pageContext.request.contextPath}/images/logo.jpg" 
								alt="LOGO" style="margin-top: 10px;"></a>
						</div>
					</div>
				</div>
				<div class="col-sm-6 col-md-4 col-lg-4 color-black">
					<h4>
						<spring:message code="label.project.title" />
					</h4>
					<h6 style="margin-left: 30px;">
						<spring:message code="label.project.subtitle" />
					</h6>
				</div>				
			</div>
			</nav>
			<div class="jumbotron margin-zero" style="max-height: 25px;padding: 1.5em 0.6em;background-color: #6DA5CD;margin-bottom: 30px;"></div>
			<div class="row">
				<div class="col-md-12">
					<div align="center" class="errorDiv"><c:import url="/jsp/error.jsp"/></div>
					<div class="well well-sm">
						<form:form id="registrationForm" class="form-horizontal" commandName="userBean" enctype="multipart/form-data"
							data-toggle="validator" role="form">
							<!-- Hidden Input to hold some important values -->
							<form:hidden path="idProvider" value="${userBean.idProvider}" />
							<form:hidden path="nameID" value="${userBean.nameID}"/>
							<fieldset>
								<legend class="text-center header">New User	Registration</legend>

								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-user bigicon"></i></span>
									<div class="col-md-6">
										<form:input path="firstName" value="${userBean.firstName}"  id="fname" name="name" type="text"
											placeholder="First Name" class="form-control" required="true" />
									</div>
								</div>
								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-user bigicon"></i></span>
									<div class="col-md-6">
										<form:input path="lastName" value="${userBean.lastName}" id="lname" name="name" type="text"
											placeholder="Last Name" class="form-control" required="true"/>
									</div>
								</div>

								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-envelope-o bigicon"></i></span>
									<div class="col-md-6">
										<form:input path="email" value="${userBean.email}" id="email" name="email" type="email"
											placeholder="Email Address"
											data-error="That email address is invalid"
											class="form-control" required="true"/>
									</div>
								</div>

								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-envelope-o bigicon"></i></span>
									<div class="col-md-6">
										<label for="avatar" class="form-control" style="background-color: white;font-weight: 100;text-align: left; width: 545px;">
											Upload your Avatar</label>
										<form:input path="multipartFile" id="avatar" name="multipartFile[0]" type="file"
											placeholder="Upload your Avatar" width="0px;"/>											
									</div>
									
								</div>

								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-envelope-o bigicon"></i></span>

									<div id="div_countryCD" class="col-md-6">
										<input value="${userBean.countryCD}" id="input_countryCD" name="country" type="text"
											placeholder="Country" class="typeahead form-control"
											autocomplete="off" spellcheck="false" required/>
										<form:hidden path="countryCD" id="dupCountryCD"/>						
									</div>									
								</div>

								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-phone-square bigicon"></i></span>
									<div class="col-md-6">
										<form:input path="phoneNo"  id="phone" name="phone" type="text" placeholder="Phone"
											class="form-control" maxlength="10"  required="true"/>
									</div>
								</div>
								
								<div class="form-group">
									<span class="col-md-1 col-md-offset-2 text-center"><i
										class="fa fa-phone-square bigicon"></i></span>
									<div class="col-md-6">	
									 	<c:forEach items="${userBean.usrPrefList}" var="userPrefBean" varStatus="status">
											<form:checkbox path="usrPrefList[${status.index}].isChecked" value="${userPrefBean.isChecked}" id="userPref"/>
											<label for="userPref" style="color: black;font-weight: 100;">${userPrefBean.prefDesc}</label>
											<form:hidden path="usrPrefList[${status.index}].prefDesc"/>
									 	</c:forEach>
								 	</div>
								</div>
								<div class="form-group">
									<div class="col-md-12 text-center">
										<button type="submit" class="btn btn-primary btn-md">Register</button>
									</div>
								</div>
							</fieldset>
						</form:form>
					</div>
				</div>
			</div>
			<hr>
				<div class="row" id="footer">
					<div class="col-sm-6"></div>
					<div class="col-sm-12">
						<p class="pull-left" style="margin-top: 70px;font-size: 11px;color:black;">
							<i class="glyphicon glyphicon-copyright-mark"></i>
							<spring:message code="label.copyright" />
						</p>
					</div>
				</div>
		</div>
	</div>
</body>
</html>