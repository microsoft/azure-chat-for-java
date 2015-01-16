<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>An Azure Demo application</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap3-typeahead.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/home.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/azchatstyles.css">
</head>
<body>
	<div class="wrapper">
		<div class="box">
			<div class="row side-row side-row-left">	
				<!-- main right column -->
				<div class="column col-sm-12 col-xs-12" id="main">
					<!-- top nav -->
					<div class="navbar navbar-lblue navbar-fixed-top">
						<div class="navbar-header" style="padding-right: 50px;">							
							<a href="/" class="navbar-brand logo" style="height: 0;padding: 0;"><img
								src="${pageContext.request.contextPath}/images/logo.jpg" width="30px" height="30px" ></a> 
								<label style="margin-top: 4px; margin-left: 5px;"><h4><spring:message code="label.title" /></h4>
								</label>
						</div>
						<nav class="collapse navbar-collapse" role="navigation">
							<form class="navbar-form navbar-left" style="">
								<div class="input-group input-group-sm"
									style="max-width: 500px;">
									<input type="hidden" id="hi_UserID"  value="${userBean.userID}"/>
									<input type="hidden" id="hi_UserName"  value="${userBean.firstName}"/>
									<input type="hidden" id="hi_nameID"  value="${userBean.nameID}"/>
									<input type="hidden" id="hi_nameID"  value="${userBean.idProvider}"/>
									<input class="ajax-typeahead  form-control" data-provide="typeahead"
										placeholder="Search Friends" name="srch-term"
										id="input_searchFrnds" type="text" style="width: 450px;"
										autocomplete="off" spellcheck="false" data-link="searchFriends.htm" />
								</div>
							</form>
							<div class="span12 pull-right">
								<ul class="nav navbar-nav">
									<li><img id="img_navUserImage" width="25px;" height="25px;" style="margin-top: 10px;margin-bottom: 5px;" src="${userBean.photoUrl}"/></li>
									<li id="li_logInUser"><a href="#">${userBean.firstName} </a></li>
									<li id="li_home"><a href="#"><i class="glyphicon glyphicon-home"></i>
											Home</a></li>
	
									<li id="li_userIcon" class="dropdown">										
										<a href="#" class="dropdown-toggle" data-toggle="dropdown" >											
											<span id="span_pendngFrndReqCnt" class="badge badge-info"><i class="glyphicon glyphicon-user"></i>${userBean.pendingFriendReq}</span>											 										 
										</a>
										<ul class="dropdown-menu pull-right" id="pndngFrndList">		            
								        </ul>									
									</li>
	
									<li id="li_logout"><a href="${pageContext.request.contextPath}/logout.htm?isLogout=true" role="button">Logout</a></li>
								</ul>
							</div>
						</nav>
					</div>
					<!-- /top nav -->

					<div class="padding">
						<div class="full col-sm-9">
							<div align="left"  id="errorDivAjaxPage" class="errorDiv color-black">
							</div>
							<div align="left"  id="errorDivHomePage" class="errorDiv color-black">
									<c:import url="/jsp/error.jsp" />
							</div>
							<!-- content -->
							<div class="row">
								<!-- main col left -->
								<div class="col-sm-7">
								<!--                                         Content Share                                               START   -->
									<div id="div_contentShare" class="well">
										<form action="#" method="post" role="form"	enctype="multipart/form-data" class="update-box">
												<ul class="post-case">
													<li id="li_shareupdate" class="post-case"><a class="status" title="" href="#">
														<i class="glyphicon glyphicon-file"></i><spring:message code="label.share.update"/></a>
													</li>
													<li id="li_updatePhotoVedio" class="post-case"><a class="photos" href="#">
														<i class="glyphicon glyphicon-picture" style="margin-right:3px;"></i><spring:message code="label.upload.photo.video"/></a>
													</li>
												</ul>
											<div class="communicate">
												<div class="arrow"></div>
												<div class="panel panel-default">
													<div class="panel-heading" style="height: 48px;">
														<div id="staticContent">
														   <spring:message code="label.update.status"/>
														</div>
														<div id="mediaContent" hidden="true">
															<input type="file" id="input_shareContentPhotoVedio"/>
																<div class="input-append">			
																<input id="picUrl" class="input-sm" type="text">
																	<a href="#" id="openMedia">
																		<i class="glyphicon glyphicon-folder-open"></i>
																	</a>
																</div>
														</div>						
													</div>
													<div class="panel-body">													
														<div class="">
															<textarea name="message" cols="40" rows="10"
																id="textArea_status_message" class="form-control message"
																style="height: 62px; overflow: hidden;"
																placeholder="<spring:message code="placeholder.update.status"/>"></textarea>
														</div>
													</div>
													<div class="panel-footer">
														<div class="row">
															<div class="col-md-12">
																<div class="form-inline">

																	<label
																		style="font-family: Lucida Console; font-weight: bold; padding-right: 10px;">
																			<spring:message code="placeholder.expires.in"/>
																	</label> 
																	<select id="select_msgExpiryTime" name="Expiry"	class="form-control privacy-dropdown input-sm">
																		<option value="1" selected="selected">1 Hour</option>
																		<option value="2">2 Hour</option>
																		<option value="3">3 Hour</option>
																	</select>
																	<input id="submit_contentShare" type="button" name="submit" value="Post"	class="btn btn-primary pull-right">
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
								<!--                                         Content Share                                                 END   -->
								<!--                                         Friend Request Status                                       START   -->
									<div id="div_FrndReqStatus" class="well" >
											<div class="row-fluid">
												<div class="span12">
													<div style="overflow:auto;width:90%;">
														<div style="display:inline-block;width:100px;height:80px;" class="col-sm-6 col-md-4 col-lg-3"  id="div_photo">
															<img id="img_friend" height="80px;" width="80px;"></img>
														</div>
														<div style="display:inline-block;width:300px;margin-top:20px;" class="col-sm-6 col-md-4 col-lg-3" id="div_FriendName">
															<input type="hidden" id="input_frndID"/>
															<h4><span class="" id="lb_friendName"><a id="href_friendName" href="#"></a></span></h4> 
														</div>
														<div style="display:inline-block;width:200px;margin-top:10px;"  class="col-sm-6 col-md-4 col-lg-3"  id="div_Status">
															<h4><span id="span_status" class="inline-block" style="margin-top: 40px;"></span></h4>
															<button type="button" id="btn_status" class="btn btn-primary"></button>
														</div>
												 	</div>
											   </div>
											</div>
									</div>
								<!--                                         Friend Request Status                                         END   -->
								<!--                                           Profile EDIT                                              START -->
									<div id="div_ProfileEdit" class="well" >
											<div class="row-fluid">
												<div class="span12">
												<form id="profileUpdationForm" class="form-horizontal" data-toggle="validator" role="form">
													  <fieldset>
														<legend class="text-center header">Update Profile</legend>

															<div class="form-group" >
																<span class="col-md-1 col-md-offset-2 text-center">
																	<i class="fa fa-user bigicon"></i>
																</span>
																<div class="col-md-7">
																	<input value="${userBean.firstName}"  id="input_profUpdt_fname" name="name" type="text"
																		placeholder="First Name" class="form-control" required/>
																</div>
															</div>
															<div class="form-group">
																<span class="col-md-1 col-md-offset-2 text-center">
																	<i	class="fa fa-user bigicon"></i>
																</span>
																<div class="col-md-7">
																	<input value="${userBean.lastName}" id="input_profUpdt_lname" name="name" type="text"
																			placeholder="Last Name" class="form-control" required/>
																</div>
															</div>

															<div class="form-group">
																<span class="col-md-1 col-md-offset-2 text-center"><i
																			class="fa fa-envelope-o bigicon"></i></span>
																<div class="col-md-7">
																<input value="${userBean.email}" id="input_profUpdt_email" name="email" type="email"
																			placeholder="Email Address"
																			data-error="That email address is invalid"
																			class="form-control" required/>
																</div>
															</div>

															<div class="form-group">
																<span class="col-md-1 col-md-offset-2 text-center"><i
																		class="fa fa-envelope-o bigicon"></i>
																</span>
																<div class="form-inline">
																	<div class="col-md-3" style="height: 70px;width: 70px;">
																		<img id="img_usrProfUpdateImg" height="70px;" width="70px;" src="${userBean.photoUrl}"/>
																	</div>
																	<div class="col-md-7" style="margin-top: 10px;">																					
																		<!-- <input style="width: 319px;" id="input_profUpdt_avatar" name="avatar" type="file" class="form-control"/> -->											
																		 <label for="input_profUpdt_avatar" class="form-control" style="text-align:left; width: 305px;font-weight: 200;margin-left: 10px;margin-top: 10px;">Update your Avatar</label>	
																		 	<input id="input_profUpdt_avatar" type="file" id="input_profUpdt_avatar" name="avatar" placeholder="Update your Avatar"/>
																		 	 
																	</div>
																</div>
															</div>
															<div class="form-group">
																<span class="col-md-1 col-md-offset-2 text-center"><i
																	class="fa fa-envelope-o bigicon"></i></span>

																<div class="col-md-7"> 
																<input value="${userBean.countryCD}" id="input_profUpdt_country" name="country" type="text"
																		placeholder="Country" class="typeahead form-control"
																		autocomplete="off" spellcheck="false" required/>
																</div>
															</div>

															<div class="form-group">
																<span class="col-md-1 col-md-offset-2 text-center"><i
																				class="fa fa-phone-square bigicon"></i></span>
																<div class="col-md-7">
																<input value="${userBean.phoneNo}" id="input_profUpdt_phone" name="phone" type="text" placeholder="Phone"
																		class="form-control" required/>
																</div>
															</div>

															<div class="form-group">
																	<div class="col-md-12 text-center">
																		<button type="button" id="btn_profUpdt_submt" class="btn btn-primary btn-md">Update</button>
																	</div>
															</div>
													</fieldset>
													</form>
											   </div>
											</div>
									</div>
								 <!--                                           Profile EDIT                                              END   -->
								  <div class="panel panel-default">
										<div class="panel-heading">
											<h4><spring:message code="label.what.is.azure"/></h4>
										</div>
										<div class="panel-body">
										  Microsoft Azure (formerly Windows Azure before 25 March 2014) is a cloud computing platform and infrastructure, created by 
										  Microsoft, for building, deploying and managing applications and services through a global network of Microsoft-managed datacenters.
										  It provides both PaaS and IaaS services and supports many different programming languages, tools and frameworks, including both Microsoft-specific 
										  and third-party software and systems. Azure was released on 1 February 2010.
										</div>
								  </div>



								</div>
								<!-- main col right -->
								<div class="col-sm-4">
									<div id="div_contentShareList" class="well">
										<div class="communicate">
											<div class="arrow"></div>
											<div class="panel panel-default">
												<div class="panel-heading">
														<spring:message code="label.content.share"/>
												</div>
												<div class="panel-body">
													<div class="table-responsive" style=" height:400px;overflow: scroll;">
													 <table class="table">
													 	 <tbody>
													 	 	<c:forEach items="${userBean.userMessageListBean.userMsgList}" var="userMessageBean" varStatus="status">
													 	 		<tr class="info">
													 	 			<td><img src="${userMessageBean.photoUrl}" height="50px;" width="50px;"/><td>
													 	 			<td><label class="text-center">${userMessageBean.ownerName}</label><td>
													 	 			<c:if test="${userMessageBean.mediaType eq 'Vedio'}">
														 	 			<td>
														 	 				<video>
														 	 					<source src="<c:out value="${userMessageBean.mediaUrl}"/>" type="<c:out value="${userMessageBean.mediaType}"/>"></source>
														 	 				</video>
													 	 				</td>
												 	 				</c:if>
												 	 				<c:if test="${userMessageBean.mediaType eq 'Image'}">
														 	 			<td>
														 	 				<img src="${userMessageBean.mediaUrl}" width="50px;" height="50px;"/>
													 	 				</td>
												 	 				</c:if>
													 	 		</tr>
													 	 		<tr class="warning">
													 	 		   <td><input type="checkbox"/><label class="priamry" style="width: 20px;height: 20px;">Like</label></td>
													 	 		   <td><textarea cols="8" rows="2"></textarea></td>
													 	 		   <td><button style="width: 20px;height: 20px;" type="button"></button></td>
													 	 		   
													 	 		</tr>
													 	 	</c:forEach>
													 	 </tbody>
													 </table>
													 </div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!--/row-->
							<hr>
							<div class="row" id="footer">
								<div class="col-sm-6"></div>
								<div class="col-sm-12">
									<p class="pull-left" style="margin-top: 70px;font-size: 11px;">
										<i class="glyphicon glyphicon-copyright-mark"></i>
										<spring:message code="label.copyright" />
									</p>
								</div>
							</div>
						</div>
						<!-- /col-9 -->
					</div>
					<!-- /padding -->
				</div>
				<!-- /main -->
			</div>
		</div>
	</div>
</body>
</html>
