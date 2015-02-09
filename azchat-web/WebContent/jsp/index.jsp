<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>An Azure Demo application</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css" />
</head>
<body>
	<div class="azchat-div">
		<div class="container">
			<div class="errorDiv"><c:import url="/jsp/error.jsp"/></div>
			<nav id="index_nav1" class="navbar" role="navigation">
			<div class="row navbar-custom">
				<div class="col-sm-6 col-md-4 col-lg-3">
					<div class="navbar-header">
						<div class="img col-sm-6 col-md-4 col-lg-3">
							<a target="_blank" class="nav navbar-nav" href="#"><img
								src="images/logo.jpg" alt="LOGO" style="margin-top: 10px;"></a>
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
				<div class="col-sm-6 col-md-4 col-lg-2 col-lg-offset-3 ">
					<div class="collapse navbar-collapse pull-right" id="navbarCollapse">
						<a href="/azchat-web/login.htm" style="color: #3b5999;font-weight: 600;">
									<spring:message	code="label.signIn"/>
						</a>
					</div>
				</div>
			</div>
			</nav>
			<div class="jumbotron margin-zero" style="max-height: 25px;padding: 1.5em 0.6em;background-color: #6DA5CD;"></div>
			<!-- Embedded Azure info page here.Need to change the file name in case other than azureInfo.jsp -->	
			<div id="azureInfo">
				<c:import url="/jsp/azureInfo.jsp"/>
			</div>
			<hr class=" margin-zero"/>
			<div class="row margin-zero ">
				<div class="col-sm-12 color-black">
					<footer>
					<p style="margin-top: 75px; font-size: 11px;">
						<i class="glyphicon glyphicon-copyright-mark"></i>
						<spring:message code="label.copyright" />
					</p>
					</footer>
				</div>
			</div>
		</div>
		<!-- main container -->
	</div>
	<!-- azchat main div -->
</body>
</html>
