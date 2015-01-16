<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
</head>
<body>
	<div>${loginBean.errorList.errorList[0].excpMsg}</div>
	<div align="center" style="">
		<form:form id="loginForm" commandName="loginBean">
			<div style="border: thin;">
				<div>
					<form:label path="" title="Name">Name : </form:label>
					<form:input path="userName" />
				</div>
				<p></p>
				<div>
					<form:label path="" title="Password">Password : </form:label>
					<form:password path="passwd" />
				</div>
				<p></p>
				<div>
					<input type="submit" name="Submit" value="Submit"/>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>