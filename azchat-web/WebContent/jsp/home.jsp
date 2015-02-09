<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title><spring:message code="label.project.homepage.title" /></title>
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
					<div class="navbar navbar-lblue navbar-fixed-top col-xs-12">
						<div class="navbar-header" style="padding-right: 50px;">							
							<a href="/" class="navbar-brand logo" style="height: 0;padding: 0;"><img
								src="${pageContext.request.contextPath}/images/logo.jpg" width="30px" height="30px" ></a> 
								<label style="margin-top: 4px; margin-left: 5px;" class="pull-right"><h4><spring:message code="label.title" /></h4></label>
						</div>
						<nav class="collapse navbar-collapse" role="navigation">
							<form class="navbar-form navbar-left">
								<div class="input-group input-group-sm"	style="max-width: 500px;">
									<input type="hidden" id="hi_UserID"  value="${userBean.userID}"/>
									<input type="hidden" id="hi_UserName"  value="${userBean.firstName}"/>
									<input type="hidden" id="hi_UserLName"  value="${userBean.lastName}"/>
									<input type="hidden" id="hi_nameID"  value="${userBean.nameID}"/>
									<input type="hidden" id="hi_photoUrl"  value="${userBean.photoUrl}"/>
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
						<div class="full col-sm-9 col-xs-9">
							<div  align="left"  id="errorDivAjaxPage" class="errorDiv text-success"></div>
							<div  align="left"  id="errorDivHomePage" class="errorDiv color-black"><c:import url="/jsp/error.jsp" /></div>
							<!-- content -->
							<div class="row">
						<!-- main column left -->
								<div class="col-sm-6">
								<!-- Content Share  START   -->
									<div id="div_contentShare" class="well">
										<form action="#" method="post" role="form"	enctype="multipart/form-data" class="update-box">
											<ul class="post-case">
												<li id="li_shareupdate" class="post-case"><a class="status" title="" href="#">
													<i class="glyphicon glyphicon-file"></i><spring:message code="label.share.update"/></a>
												</li>
												<li id="li_updatePhotoVideo" class="post-case"><a class="photos" href="#">
													<i class="glyphicon glyphicon-picture" style="margin-right:3px;"></i><spring:message code="label.upload.photo.video"/></a>
												</li>
											</ul>
											<div class="communicate">
												<div class="arrow"></div>
												<div class="panel panel-default">
													<div class="panel-heading" style="height: 48px;">
														<div id="staticContent">
														   <div class="panel-title"><spring:message code="label.update.status"/></div>
														</div>
														<div id="mediaContent" hidden="true">
															<div class="panel-title">
															<input type="file" id="input_shareContentPhotoVideo"/>
																<div class="input-append">			
																<input id="picUrl" class="input-sm upload-text" type="text">
																	<a href="#" id="openMedia">
																		<i class="glyphicon glyphicon-folder-open"></i>
																	</a>
																</div>
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
																	<select id="select_msgExpiryTime" name="Expiry"	class="form-control input-sm">
																		<option value="60" selected="selected">1 Hour</option>
																		<option value="300">5 Hour</option>
																		<option value="1440">1 Day</option>
																		<option value="7200">5 Day</option>
																	</select>
																	<input id="submit_contentShare" type="button" name="submit" value="Post" class="btn btn-primary pull-right">
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
								<!--  Content Share   END   -->
								<!--  Friend Request Status    START   -->
									<div id="div_FrndReqStatus" class="well" >
											<div class="row-fluid">
												<div class="friend-info-box span12">
														<div class="col-sm-6"  id="div_photo">
															<img id="img_friend" class="friend-image-size"></img>
														</div>
														<div class="col-sm-6" id="div_FriendName">
															<input type="hidden" id="input_frndID"/>
															<h4><span class="" id="lb_friendName"><a id="href_friendName" href="#"></a></span></h4> 
														</div>
														<div class="col-sm-6" id="div_Status" style="margin-top: 40px;">
															<h4><span id="span_status" class="inline-block"></span></h4>
															<button type="button" id="btn_status" class="btn btn-primary"></button>
														</div>
											   </div>
											</div>
									</div>
								<!--   Friend Request Status  END   -->
								<!--   Profile EDIT  START -->
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
																		 <label for="input_profUpdt_avatar" class="form-control avatar-label"><spring:message code="label.update.avatar"/></label>	
																		 	<input id="input_profUpdt_avatar" type="file" id="input_profUpdt_avatar" name="avatar"/>
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
								 <!--    Profile EDIT END   -->
								  <div class="panel panel-default">
									<div class="panel-heading">
										<div class="panel-title"><spring:message code="label.what.is.azure"/></div>
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
								<div class="col-sm-6">
							<!-- status Feed Box starts Here -->
									<div id="div_contentShareList">
										<div class="communicate">
											<div class="panel panel-default">
												<div class="panel-heading">
														<div class="panel-title"><spring:message code="label.content.share"/></div>
												</div>
												<div class="panel-body">
													<div class="status-update-box">
												 	 	<c:forEach items="${userBean.userMessageListBean.userMsgList}" var="userMessageBean" varStatus="status">
												 	 		<div class="row" style="height: auto;">
												 	 			<div class="col-md-12 statusBorder">
												 	 				<div class="profileInfo">
												 	 					<div class="profilePic col-md-2">
													 	 					<img src="${userMessageBean.photoUrl}" height="50px;" width="50px;"/>
													 	 				</div>
														 	 			<div class="profileUserName col-md-6">
														 	 				<label>${userMessageBean.ownerName}</label>
														 	 			</div>
														 	 			<div class="col-md-4 time-ago pull right">
														 	 				<label style="font-weight: 100;"></label>
														 	 			</div>
												 	 				</div>
														 	 		<div class="userStatus text-justify">
														 	 			<label><font spellcheck="false" style="font-weight: 100;font-stretch: normal;white-space: pre-line;">${userMessageBean.msgText}</font></label>
														 	 		</div>
													 	 			<c:if test="${userMessageBean.mediaType eq 'video'}">
														 	 			<div>
														 	 				<video class="videoContent" id="video_${userMessageBean.msgID}" width="640" height="360" poster="${pageContext.request.contextPath}/images/posterCover.jpg" controls preload="none" onclick="startVideo(this);">
														 	 					<source src="<c:out value="${userMessageBean.mediaUrl}"/>" type="<c:out value="${userMessageBean.mediaType}"/>"></source>
														 	 				</video>											 	 			
														 	 			</div>
												 	 				</c:if>
												 	 				<c:if test="${userMessageBean.mediaType eq 'image'}">
														 	 			<div>
														 	 				<img class="imageContent" src="${userMessageBean.mediaUrl}"/>											 	 			
														 	 			</div>
												 	 				</c:if>
												 	 				<div class="like-comment-status col-md-12">
												 	 					<div style="float:left; margin:0 .5em 0 0;">
												 	 						<span id="span_likeCount_${userMessageBean.msgID}"><i class="glyphicon glyphicon-thumbs-up"></i>
												 	 							<c:out value="${userMessageBean.likeCount}"/>
												 	 						</span>
												 	 							<span id="span_like_${userMessageBean.msgID}">
												 	 								<a id="a_Like_${userMessageBean.msgID}" class="likeUnlike" href="#">
												 	 									<c:if test="${userMessageBean.isLike eq 'false'}">Like</c:if>
												 	 									<c:if test="${userMessageBean.isLike eq 'true'}">Unlike</c:if>
												 	 								</a>
											 	 								</span>
												 	 					</div>
												 	 					<div style="float:left;">
												 	 						<span id="span_CommentCount_${userMessageBean.msgID}"><i class="glyphicon glyphicon-edit"></i>
												 	 							<c:out value="${userMessageBean.commentCount}"/>
												 	 						</span>
												 	 					</div>
												 	 				</div>
												 	 				<div class="like-comment-data col-md-12">
												 	 					<div class="like-comment-status-sub-content" id="like-comment-status-sub-content_${userMessageBean.msgID}">
												 	 						<!-- Loop out all the comments here -->
												 	 						<c:forEach items="${userMessageBean.msgCommentList}" var="messageCommentBean" varStatus="status">
													 	 						<div class="friendComment">
														 	 						<div class="profilePic col-md-2">
															 	 						<img src="${messageCommentBean.photoUrl}" height="50px;" width="50px;"/>
															 	 					</div>
																 	 				<div class="profileUserName col-md-10">
															 	 						<div class="FriendNnme">
																 	 						<label>${messageCommentBean.friendName}</label>
																 	 					</div>
																 	 					<div class="frnd-sub-Comment">
															 	 							<label style="font-weight: 100;">${messageCommentBean.comment}</label> 
															 	 						</div>
																 	 				</div>
													 	 						</div>
												 	 						</c:forEach>
														 	 				<!-- Loop out all the comments here end-->
												 	 						<div class="userComment">
													 	 						<div class="profilePic col-md-2">
														 	 						<img id="img_usrMsgCommentFrndPhoto" src="${userBean.photoUrl}" height="50px;" width="50px;"/>
														 	 					</div>
														 	 					<div class="profileUserName col-md-10">
															 	 						<div class="FriendNnme">
																 	 						<label id="lb_usrMsgCommentFrndName"><c:out value="${userBean.firstName}"/> <c:out value="${userBean.lastName}"/></label>
																 	 					</div>
																 	 					<div class="frnd-sub-Comment" id="${userMessageBean.msgID}">
															 	 							<textarea class="ta_usrMsgComment" id="ta_usrMsgComment_${userMessageBean.msgID}"  rows="1" cols="50" maxlength="200"></textarea>		
															 	 						</div>
																 	 			</div>
												 	 						</div>														 	 											 	 					
												 	 					</div>
												 	 				</div>
												 	 				<div class="status-border"></div>
													 	 		</div>													 	 		
												 	 		</div>
												 	 	</c:forEach>
													</div>
												</div>
											</div>
										</div>
									</div>
							<!-- status Feed Box Ends Here -->	
								</div>
							</div>
							<!--/row-->
							<hr>
							<div class="row footer" id="footer">
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
		<!-- Loading all js file -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/dash.all.js"></script>
	<script type="text/javascript"
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script type="text/javascript"
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/bootstrap3-typeahead.min.js"></script>
	<script type="text/javascript" 
		src="${pageContext.request.contextPath}/js/common.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>
