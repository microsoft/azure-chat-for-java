/**
 * This js file contains functions related to the azchat friend request
 * management and content sharing.
 */
var context ;
var player;
var waitDiv = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" aria-hidden="true" role="dialog" style="padding-top:15%; overflow-y:visible;"><div class="modal-dialog modal-m"><h2 align=center>Processing...</h1></div></div>');
$(document).ready(function() {
	$("#div_contentShare").show();
	$("#div_FrndReqStatus").hide();
	$("#div_ProfileEdit").hide();
	hideErrorDiv();
	var isFriendPageVisited=false;
	
	$('input[id=input_shareContentPhotoVideo]').change(function() {
		$('#picUrl').val($(this).val());
	});
	
	$('#openMedia').click(function() {
		$('input[id=input_shareContentPhotoVideo]').click();
	});
	
	$('.status').click(function() {
			$('.arrow').css("left", 7);
			$("#staticContent").show();
			$("#mediaContent").hide();
			$('#picUrl').val("");
			document.getElementById('input_shareContentPhotoVideo').value = ''
			//$("#input_shareContentPhotoVideo")[0].files[0]='';
	});
	$('.photos').click(function() {
			$('.arrow').css("left", 170);
			$("#staticContent").hide();
			$("#mediaContent").show();
			document.getElementById('input_shareContentPhotoVideo').value = ''
			//$("#input_shareContentPhotoVideo")[0].files[0]='';
	});
	
	$('body').on('click', '.like-comment-status', function(event) {
	    $(this).parent().children(".like-comment-data").slideToggle(500);
	});
	
	context = new Dash.di.DashContext();
	player  = new MediaPlayer(context);
	pendingFrndMap = {};
	map = {};
	$("#input_searchFrnds").typeahead({
		source : function(query, process) {
			          hideErrorDiv();
		              var $friends=new Array;
		              $friends=[""]
					return $.ajax({
						url : "searchFriends.htm",
						type : "GET",
						data : {
							"firstName" : query
						},
						dayaType : "JSON",
						cache : true,
						success : function(data) {
							//Service Layer Errors          
							var serErrMsg="";
							if(data.errorList!= undefined || data.errorList!=null){
								if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
									$.each(data.errorList.errorList,function(index,errorBean){
										serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
									});
									showServiceErrors(serErrMsg);
								}
							}else{
									//Map to store the Friend Objects in global variable.
										$.each(data.friendList, function (i, friend) {
												map[friend.friendID] = friend;
										 }); 
									  //New Map Implementation      start
											$("#errorDivAjaxPage").empty();
											$.map(data.friendList,function(friendBean){
												var group;
												group = {
														 id: friendBean.friendID,
														 name: friendBean.friendName,
														 toString: function () {
														 return JSON.stringify(this);
														 },
														 toLowerCase: function () {
														 return this.name.toLowerCase();
														 },
														 indexOf: function (string) {
														 return String.prototype.indexOf.apply(this.friendName, arguments);
														 },
														 replace: function (string) {
														 var value = '';
														 value += this.name;
														 if(typeof(this.name) != 'undefined') {
														 value += ' <span class="pull-right muted">';
														 value += '</span>';
														 }
														 return String.prototype.replace.apply('<div style="padding: 10px; font-size: 1em;">' + value + '</div>', arguments);
														 }
														 };
														 $friends.push(group);
										 }); //Map
														process($friends);
							} //End Else
				    	//New Map Implementation          end
						}, //CALLBACK SUCCESS END
						error : function(xhr, status, error) {
							showError(xhr, status, error)
						} // CALLBACK ERROR   END
					});  //AJAX END
				},  //  TYPEAHEAD SOURCE :   END
	    matcher: function (item) {
	        if (item.toLowerCase().indexOf(this.query.trim().toLowerCase()) != -1) {
	            return true;
	        }
	    },
	    sorter: function (items) {
	        return items.sort();
	    } ,
	    highlighter: function (item) {
	        var regex = new RegExp( '(' + this.query + ')', 'gi' );
	        return item.replace( regex, "<font class='typeahead-srch-frnd'><strong>$1</strong><font>" );
	    },
	    updater: function (item) {
	    	var item = JSON.parse(item); 
	    	hideErrorDiv();
	    	processSelectedFriend(item.id,$("#hi_UserID").val(),map[item.id])
	        return item.name;
	    }
});
	
/**
 * Get list of Pending friend Requests.(getPendingFriendList)
 */	
$("#li_userIcon").click(function(event){
	$("ul#pndngFrndList").empty();
	hideErrorDiv();
	$.ajax({
		url : "getPendingFriendList.htm",
		type : "GET",
		data : { 
			    "loggedInUserID" : $("#hi_UserID").val(),
				},
		dayaType : "JSON",
		cache : false,
		success : function(data) {
			//Service Layer Errors          
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else if(data.friendList==null){
				$("#span_pendngFrndReqCnt").html("<i class='glyphicon glyphicon-user'></i> 0");
				$("ul#pndngFrndList").html('<li id=frndID><a href="#">No Pending Friend Request.</a></li>');
			}
			else{
				//Create global map for pending friends for this user.
				$("#span_pendngFrndReqCnt").html("<i class='glyphicon glyphicon-user'></i> "+data.pendingFriendReq+" ");
				$.each(data.friendList,function(index,friendBean){
					pendingFrndMap[friendBean.friendID]=friendBean;
					$("ul#pndngFrndList").append('<li id='+friendBean.friendID+'><a href="#">'+friendBean.friendName+'<button class="btn-mini btn-primary" style="margin-left: 10px;" value="approve"><i class="glyphicon glyphicon-ok icon-white"></i><span>Approve</span></button><button class="btn-mini btn-primary" style="margin-left: 10px;" value="reject"><i class="glyphicon glyphicon-remove icon-white"></i><span>Reject</span></button></a></li>');			
				});
			}		
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	});
});

/**
 * This event method triggers the approve/reject friend request ajax call.
 */
$("ul#pndngFrndList").on('click','button',function (){
	hideErrorDiv();
	var friendID = $(this).closest("li").attr("id");
	var status  = $(this).attr("value");
	hideErrorDiv();
	approveRejectFriend(friendID, pendingFrndMap[friendID].userID, pendingFrndMap[friendID].friendName, pendingFrndMap[friendID].friendPhotoUrl,status);
	
	if(status=='approve'){
		  $("#span_status").text("Friend");
		  $("#btn_reject").remove();
		  $("#btn_status").hide();
	  }else{
		  $("#btn_status").text("Add Friend");
		  $("#btn_reject").remove();
	  }
});


/**
 * This click events triggers the ajax call for addFriend and approveReject friend based on the button value.
 */	
$("body").on('click','#btn_status,#btn_reject',function(){
	hideErrorDiv();
	var btn_val=$(this).text();
	if(btn_val=='Rejected' || btn_val=='Friend' || btn_val=='Pending Approval' || btn_val == 'Friend Request Sent' )	{
		return false;
	}
	if(btn_val=='Add Friend'){
		addFriend($("#input_frndID").val(),$("#hi_UserID").val(),map[$("#input_frndID").val()].friendName,map[$("#input_frndID").val()].friendPhotoUrl);
	}else if(btn_val=='Approve' || btn_val=='Reject'){
	  approveRejectFriend($("#input_frndID").val(),$("#hi_UserID").val(),map[$("#input_frndID").val()].friendName,map[$("#input_frndID").val()].friendPhotoUrl,btn_val);
	  $("#btn_reject").empty().hide();	
	  $("#btn_status").empty().hide();
	  $("li#"+$("#input_frndID").val()).remove();
	  if(btn_val=='Approve'){
		  $("#span_status").text("Friend").show();
	  }else{
		  $("#btn_status").text("Add Friend").show();
	  }
	}else if(btn_val=='Edit Profile'){
		goUpdateProfile();
	}
	
});

/**
 * This event method will handle home click.Reset the other view and show content sharing div.(goHome)
 */
$("#li_home").click(function(){
	$("#div_contentShare").show();
	$("#div_FrndReqStatus").hide();
	$("#div_ProfileEdit").hide();
	$("#input_searchFrnds").val("");
	$("ul#pndngFrndList").empty();
	hideErrorDiv();
	//If Friend page visited.Replace the content with the original.
	if(isFriendPageVisited==true){
		getUserContnet($("#hi_UserID").val(), "UserAndFriends");
	}
});

/**
 *  This event method will redirect user to the profile edit page. 
 */
$("#li_logInUser").click(function(event){
	hideErrorDiv();
	goUpdateProfile();
}); //End Profile Edit Click

/**
 * This method update the user profile. (updateUserProfile)
 */
$("#btn_profUpdt_submt").click(function(event){
	hideErrorDiv();
	//Create the multipart form
	var fd = new FormData();
	fd.append('loggedInUserID',$("#hi_UserID").val());
	fd.append('firstName',$("#input_profUpdt_fname").val());
	fd.append('lastName',$("#input_profUpdt_lname").val());
	fd.append('country',getCountryByName($("#input_profUpdt_country").val()).countryCD);
	fd.append('email',$("#input_profUpdt_email").val());
	fd.append('phone',$("#input_profUpdt_phone").val());
	fd.append('userProfileImage', $("#input_profUpdt_avatar")[0].files[0]);
	fd.append("nameID",$("#hi_nameID").val());
	waitDiv.modal();
	$.ajax ({
		url : "updateUserProfile.htm",
		type : "POST",
		data : fd,
		contentType : false,
		processData: false,
		cache : false,
		dayaType : "JSON",
		complete : function() {
			waitDiv.modal('hide');
		},
		success : function(data) {
			//Service Layer Errors          
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else{
				$("#img_navUserImage").prop("src",data.photoUrl);
				$("#img_usrProfUpdateImg").prop("src",data.photoUrl);
				$("#a_logInUser").text(data.firstName);
				$("#hi_UserName").val(data.firstName);
				$("#hi_UserLName").val(data.lastName);
				$("#hi_photoUrl").val(data.photoUrl);
				showSuccess(data.msg);
		}
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	}); //End updateUserProfile AJAX call
}); //End Profile Submit Click 

//Typeahead for Upate user profile    start
	$("#input_profUpdt_country").typeahead({
		source : function(query, process) {
			return 	process(getCountryNames());
		},  //  TYPEAHEAD SOURCE :   END
	    matcher: function (item) {
	        if (item.toLowerCase().indexOf(this.query.trim().toLowerCase()) != -1) {
	            return true;
	        }
	    },
	    sorter: function (items) {
	        return items.sort();
	    } ,
	    highlighter: function (item) {
	        var regex = new RegExp( '(' + this.query + ')', 'gi' );
	        return item.replace( regex, "<strong>$1</strong>" );
	    },
	    updater: function (item) {
	        return item;
	    }
	});
//Typeahead for Upate user profile    End
//User Message/Content Share POST   start
	/**
	 * This click event trigger the ajax call to store the user content in azure storage.
	 */
   $("#submit_contentShare").click(function(){
	   hideErrorDiv();
	   $(this).prop("disabled",true);
	     shareContent();
	   $(this).prop("disabled",false);
   });
 //Hide the error/Success message on new msg typing start
   $("#textArea_status_message").keyup(function(){
	   hideErrorDiv();
   })
 //Hide the error/Success message on new msg typing end  
//User Message/Content Share  POST  end
   /**
    * This keypress event on the text area check for the enter key code and call the update comment.
    */
   $("body").on("keypress",".ta_usrMsgComment",function(event){
	   hideErrorDiv();
	   var keycode=(event.keyCode ? event.keyCode : event.which);
	   var msgID=$(this).parent().attr('id');
	   if(keycode=='13'){
		   $(this).prop("disabled",true);
		    updateMessageComment(msgID);
		   $(this).prop("disabled",false);
	   } 
   });
   
   /**
    * This event triggers the ajax call for user like for corresponding message.
    */
   $("body").on('click','.likeUnlike',function(){
	   hideErrorDiv();
	   //Token No 3 is the Msg ID
	   var msgID=$(this).attr('id').split("_")[2];
	   $(this).prop("disabled",true);
	   updateUserLike(msgID,$(this).text());
	   $(this).prop("disabled",false);
   });
   
   $("body").on('click','#href_friendName',function(event){
	   hideErrorDiv();
	   isFriendPageVisited=true;
	   getUserContnet($("#input_frndID").val(),"UserOnly");
   });
	
}); // Document Ready End  

/**
 * AJAX CAll to fetch the user friend request status.
 * @param friend
 * @param userID
 */
function processSelectedFriend(friendID,userID,friend){
	$("#div_contentShare").hide();
	$("#div_FrndReqStatus").show();
	$("#div_ProfileEdit").hide();
	$.ajax({
		url : "friendStatus.htm",
		type : "GET",
		data : {"friendID" : friendID ,
				"loggedInUserID" :userID
				},
		dayaType : "JSON",
		cache : false,
		success : function(data) {
			//Service Layer Errors          
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else{
			$("#div_contentShare").hide();
			$("#div_FrndReqStatus").show();
			$("#input_frndID").val(friendID);
			$("#href_friendName").text(data.friendName);
			$("#img_friend").attr("src",data.friendPhotoUrl); 
			if(data.status=='noFriend'){
				$("#btn_status").text("Add Friend").show();;
				$("#span_status").text("Add Friend").hide();
			}else if(data.status=='rejected'){
				$("#btn_status").text("Rejected").hide();
				$("#span_status").text("Rejected").show();
			}else if(data.status=='pendingApproval'){
				$("#btn_reject").remove();
				$("#btn_status").text("Approve").show();
				$("#div_Status").append("<button id='btn_reject' class='btn btn-primary' style='margin-left:2px;' value='Reject'>Reject</button>");
				$("#span_status").text("Approve").hide();
			}else if(data.status=='approved'){
				$("#btn_status").text("Friend").hide();;
				$("#span_status").text("Friend").show();
			}else if(data.status=='sent'){
				$("#btn_status").text("Friend Request Sent").hide();
				$("#span_status").text("Friend Request Sent").show();
			}else if(data.status=='you'){
				$("#btn_status").text("Edit Profile").show();
				$("#span_status").text("Edit Profile").hide();
			}else if(data.status=='' || data.status==null || data.status=='undefined'){
				showError(xhr, status, "Unable to fetch the user friend status.")
			}
			}
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	});
	
}
/**
 * This function adds establish the relation ship between input friend and user id.
 * @param friendID
 * @param userID
 * @param friendName
 * @param friendPhotoUrl
 */
function addFriend(friendID,userID,friendName,friendPhotoUrl){
	$.ajax({
		url : "addFriend.htm",
		type : "GET",
		data : {"friendID" : friendID ,
				"loggedInUserID" : userID,
				"friendName" : friendName ,
				"photoUrl" : friendPhotoUrl
				},
		dayaType : "JSON",
		cache : false,
		success : function(data) {
			//Service Layer Errors          
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else{
				showSuccess(data.msg);
				$("#btn_status").text("Friend Request Sent").hide();
				$("#span_status").text("Friend Request Sent").show();
			}
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	});
}

/**
 * This function allow user to approve or reject the friend requests
 * @param friendID
 * @param userID
 * @param friendName
 * @param friendPhotoUrl
 */
function approveRejectFriend(friendID,userID,friendName,friendPhotoUrl,status){
	$.ajax({
		url : "updateFrndReqstatus.htm",
		type : "POST",
		data : {
				"friendID" : friendID,
			 	"loggedInUserID" :userID ,
				"friendName" : friendName,
				"photoUrl" : friendPhotoUrl,
			    "status" : status,    
				},
		dayaType : "JSON",
		cache : false,
		success : function(data) {
			//Service Layer Errors          
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else{
				$("#i_pendngFrndReqBdge").val(data.pendFrndReqCnt);
				showSuccess(data.msg);
				$("#span_pendngFrndReqCnt").html("<i class='glyphicon glyphicon-user'></i> "+getPendingFriendCount(userID)+" ");
			}
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	});
}
/**
 * This function used to populate the error in the error div of the home page.
 * @param xhr
 * @param status
 * @param error
 */
function showError(xhr, status, error) {
	$("#errorDivAjaxPage").html("<p><font color='red' size='1' style='font-weight: bold;>"+error+" "+status+"</font></p>");
	$("#errorDivAjaxPage").show();
}

/**
 * This function used to populate the success message in the error div of the home page.
 * @param xhr
 * @param status
 * @param error
 */
function showSuccess(msg) {
	$("#errorDivAjaxPage").html("<p><font size='1' color='green' style='font-weight: bold;'>"+msg+"</font></p>");
	$("#errorDivAjaxPage").show();
}

/**
 * This function delte the content and hide ajax eror div and div showing server side error.  
 */
function hideErrorDiv(){
	$("#errorDivAjaxPage").hide();
   $("#errorDivHomePage").hide();
}

/**
 * This method shows the exceptions occured at the service layer.
 * @param msg
 */
function showServiceErrors(serErrMsg){
	$("#errorDivAjaxPage").html("<p><font size='1' color='red'>"+serErrMsg+"</font></p>");
	$("#errorDivAjaxPage").show();
}

/**
 *  redirect user to the update profile page.
 */
function goUpdateProfile(){
	//Country Code Validation
	var isCountryCode=isInteger($("#input_profUpdt_country").val());
	//If country code if present then only convert to the name for user else keep as it is.
	if(isCountryCode==true){
		$("#input_profUpdt_country").val(getCountryByCode($("#input_profUpdt_country").val()).countryName);
	}
	$("#div_contentShare").hide();
	$("#div_FrndReqStatus").hide();
	$("#input_searchFrnds").val("");
	$("#div_ProfileEdit").show();
	$("#errorDivAjaxPage").empty().hide();	
}

/**
 *  This function triggers the ajax call to stote the user text messages and photo or video. 
 */
function shareContent(){
	var fd = new FormData();
    fd.append('loggedInUserID',$("#hi_UserID").val());
	fd.append('msgText',$("#textArea_status_message").val());
	fd.append('mediaPhoto',$("#input_shareContentPhotoVideo")[0].files[0]);
	fd.append('expiryTime',$("#select_msgExpiryTime").val());
	fd.append('nameID',$("#hi_nameID").val());
	fd.append('photoUrl',$("#hi_photoUrl").val());
	waitDiv.modal();
   $.ajax ({
		url : "contentShare.htm",
		type : "POST",
		data : fd,
		contentType : false,
		processData: false,
		cache : false,
		dayaType : "JSON",
		complete : function() {
			waitDiv.modal('hide');
		},
		success : function(data) {
			//Service Layer Errors
			$('#picUrl').val("");
			var serErrMsg="";
			if(data.errorList!= undefined || data.errorList!=null){
				if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
					$.each(data.errorList.errorList,function(index,errorBean){
						serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
					});
					showServiceErrors(serErrMsg);
				}
			}else{
			$("#textArea_status_message").val("");
			showSuccess(data.msg);
			//Refresh the user content          start
			 getUserContnet($("#hi_UserID").val(),"UserAndFriends");
			//Refresh the user content          end
			//Reset Input File
			 $("#input_shareContentPhotoVideo")[0].files[0]='';
			//Reset Input File
			}
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	}); //  Content Share POST Ajax call END 
}
/**
 * This method calls controller method to update the user comments on the corresponding message.
 */
function updateMessageComment(msgID){
	var fd = new FormData();
    fd.append('loggedInUserID',$("#hi_UserID").val());
	fd.append('userName',$("#lb_usrMsgCommentFrndName").text());
	fd.append('photoUrl',$("#hi_photoUrl").val());
	fd.append('comment',$("#ta_usrMsgComment_"+msgID).val());
	fd.append('msgID',msgID);
	 $.ajax ({
			url : "updateMesageComment.htm",
			type : "POST",
			data : fd,
			contentType : false,
			processData: false,
			cache : false,
			dayaType : "JSON",
			complete : function() {
			},
			success : function(data) {
				//Service Layer Errors          
				var serErrMsg="";
				if(data.errorList!= undefined || data.errorList!=null){
					if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
						$.each(data.errorList.errorList,function(index,errorBean){
							serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
						});
						showServiceErrors(serErrMsg);
					}
				}else{
					$("#ta_usrMsgComment_"+msgID).val("");
					showSuccess(data.msg);
					getMessageComment(msgID);
				}
			},
			error : function(xhr, status, error) {
				showError(xhr, status, error)
			}
		}); //  Content Share POST Ajax call END 
}

/**
 * This function triggers the ajax call to update the user like count for the perticular user.
 * @param msgID
 */
function updateUserLike(msgID,operation){
	var fd = new FormData();
    fd.append('loggedInUserID',$("#hi_UserID").val());
	fd.append('msgID',msgID);
	fd.append('userName',$("#lb_usrMsgCommentFrndName").text());
	fd.append('photoUrl',$("#hi_photoUrl").val());
	fd.append('operation',$.trim(operation));
	 $.ajax ({
			url : "updateMesageLikeStatus.htm",
			type : "POST",
			data : fd,
			contentType : false,
			processData: false,
			cache : false,
			dayaType : "JSON",
			complete : function() {
			},
			success : function(data) {
				//Service Layer Errors          
				var serErrMsg="";
				if(data.errorList!= undefined || data.errorList!=null){
					if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
						$.each(data.errorList.errorList,function(index,errorBean){
							serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
						});
						showServiceErrors(serErrMsg);
					}
				}else{
					var cnt=parseInt($.trim($("#span_likeCount_"+msgID).text()));
					if($.trim(operation) =='Like'){
						$("#a_Like_"+msgID).text(" Unlike");
						cnt++;
						$("#span_likeCount_"+msgID).html('<i id="i_LikeCount_'+msgID+'" class="glyphicon glyphicon-thumbs-up"></i>'+cnt);
						
						
					}else if($.trim(operation) =='Unlike'){
						$("#a_Like_"+msgID).text(" Like");
						cnt--;
						$("#span_likeCount_"+msgID).html('<i id="i_LikeCount_'+msgID+'" class="glyphicon glyphicon-thumbs-up"></i>'+cnt);
					}
					showSuccess(data.msg);
				}
			},
			error : function(xhr, status, error) {
				showError(xhr, status, error)
			}
	}); //  Content Share POST Ajax call END 
	
}

/**
 * This function retirieves the user data and triggers call to the refreshUserContent function to refresh the UI data.
 * @param userID
 */
function getUserContnet(userID,contentType){
	var fd = new FormData();
    fd.append('loggedInUserID',userID);
    fd.append('contentType',contentType)
	 $.ajax ({
			url : "getUserContent.htm",
			type : "POST",
			data : fd,
			contentType : false,
			processData: false,
			cache : false,
			dayaType : "JSON",
			complete : function() {
			},
			success : function(data) {
				//Service Layer Errors          
				var serErrMsg="";
				if(data.errorList!= undefined || data.errorList!=null){
					if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
						$.each(data.errorList.errorList,function(index,errorBean){
							serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
						});
						showServiceErrors(serErrMsg);
					}
				}else{
					refreshUserContents(data);
				}
			},
			error : function(xhr, status, error) {
				showError(xhr, status, error)
			}
	}); //  Content Share POST Ajax call END 
}

/**
 * This method updates the user comntent with the userBean JSON passed.
 * @param userBean
 */
function refreshUserContents(userBean){
  $(".status-update-box").html("");
  $.each(userBean.userMessageListBean.userMsgList,function(index,userMessageBean){
	$(".status-update-box").append('<div class="row" style="height:auto;">'+
			'<div class="col-md-12 statusBorder">'+
				'<div class="profileInfo">'+
					'<div class="profilePic col-md-2">'+
						'<img src="'+userMessageBean.photoUrl+'" height="50px;" width="50px;"/>'+
					'</div>'+
					'<div class="profileUserName col-md-6">'+
						'<label>'+userMessageBean.ownerName+'</label>'+
					'</div>'+
					'<div class="col-md-4 time-ago pull right">'+
						'<label style="font-weight: 100;"></label>'+
					'</div>'+
				'</div>'+										 	 				
				'<div class="userStatus text-justify">'+
					'<label><font spellcheck="false" style="font-weight: 100;font-stretch: normal;white-space: pre-line;">'+userMessageBean.msgText+'</font></label>'+
				'</div>'+
					'<div id="div_vedContent_'+userMessageBean.msgID+'" class="videoContent hide">'+				 	 			
	 				'</div>'+
					'<div id="div_imgContent_'+userMessageBean.msgID+'" class="imageContent hide">'+
					'</div>'+
				'<div class="like-comment-status col-md-12">'+
					'<div style="float:left; margin:0 .5em 0 0;">'+
						'<span id="span_likeCount_'+userMessageBean.msgID+'"><i id="i_LikeCount_'+userMessageBean.msgID+'" class="glyphicon glyphicon-thumbs-up"></i>'+
							userMessageBean.likeCount+
						'</span>'+
							'<span id="span_like_'+userMessageBean.msgID+'">'+
								'<a id="a_Like_'+userMessageBean.msgID+'" class="likeUnlike" href="#">'+
									''+
								'</a>'+
							'</span>'+
					'</div>'+
					'<div style="float:left;">'+
						'<span id="span_CommentCount_'+userMessageBean.msgID+'"><i id="i_CommnetCount_'+userMessageBean.msgID+'" class="glyphicon glyphicon-edit"></i>'+userMessageBean.commentCount+
						'</span>'+
					'</div>'+
				'</div>'+
				'<div class="like-comment-data col-md-12">'+
					'<div id="like-comment-status-sub-content_'+userMessageBean.msgID+'" class="like-comment-status-sub-content">'+				
					'</div>'+
				'</div>'+
				'<div class="status-border"></div>'+
			'</div>'+													 	 		
		'</div>');
		//Append Comments to the message.
	    refreshMessageComment(userMessageBean);
	
	  //Render Image or Video
	  if(userMessageBean.mediaType=='image'){
		  $("#div_imgContent_"+userMessageBean.msgID).removeClass('hide');
		  $("#div_imgContent_"+userMessageBean.msgID).append('<img class="img_imageContent imageContent" src="'+userMessageBean.mediaUrl+'"/>');
	  }else if(userMessageBean.mediaType=='video'){
		  $("#div_vedContent_"+userMessageBean.msgID).removeClass('hide');
		  $("#div_vedContent_"+userMessageBean.msgID).append('<video class="videoContent" id="video_'+userMessageBean.msgID+'" width="640" height="360" controls preload="none" poster="/azchat-web/images/posterCover.jpg" onclick="startVideo(this);"><source src="'+userMessageBean.mediaUrl+'"/>" type="'+userMessageBean.mediaType+'"/>"></source></video>');
	  }
	  //Render Like Unlike status
	  if(userMessageBean.isLike == true){
		  $("#a_Like_"+userMessageBean.msgID).text(" Unlike");
	  }else if (userMessageBean.isLike == false){
		  $("#a_Like_"+userMessageBean.msgID).text(" Like");
	  }
	});
}

/**
 * This function updates the user message comments.
 * @param userMessageBean
 */
function refreshMessageComment(userMessageBean){
	if(userMessageBean.msgCommentList!=null){
		$("#like-comment-status-sub-content_"+userMessageBean.msgID).empty();
			$.each(userMessageBean.msgCommentList,function(index,messageCommentBean){
				$("#span_CommentCount_"+userMessageBean.msgID).html('<i id="i_CommnetCount_'+userMessageBean.msgID+'" class="glyphicon glyphicon-edit"></i> '+userMessageBean.commentCount);
				$("#like-comment-status-sub-content_"+userMessageBean.msgID).append('<div class="friendComment">'+
					'<div class="profilePic col-md-2">'+
							'<img src="'+messageCommentBean.photoUrl+'" height="50px;" width="50px;"/>'+
						'</div>'+
					'<div class="profileUserName col-md-10">'+
							'<div class="FriendNnme">'+
							'<label>'+messageCommentBean.friendName+'</label>'+
						'</div>'+
						'<div class="UserComment">'+
								'<label style="font-weight: 100;">'+messageCommentBean.comment+'</label>'+ 
							'</div>'+
					'</div>'+
				'</div>');
			});
	}
		//Append the textarea for the user comment after populating all the comments.
		 $("#like-comment-status-sub-content_"+userMessageBean.msgID).append('<div class="userComment">'+
			'<div class="profilePic col-md-2">'+
				'<img id="img_usrMsgCommentFrndPhoto" src="'+$("#hi_photoUrl").val()+'" height="50px;" width="50px;"/>'+
			'</div>'+
			'<div class="profileUserName col-md-10">'+
						'<div class="FriendNnme">'+
						'<label id="lb_usrMsgCommentFrndName">'+$("#hi_UserName").val()+' '+$("#hi_UserLName").val()+'</label>'+
					'</div>'+
					'<div class="UserComment" id="'+userMessageBean.msgID+'">'+
							'<textarea id="ta_usrMsgComment_'+userMessageBean.msgID+'" class="ta_usrMsgComment"  rows="1" cols="50" maxlength="200"></textarea>'+		
					'</div>'+
			'</div>'+
		'</div>');	
			

	
	return "";
}
/**
 * This AJAX call fetch the message comments for the message being passed and refresh the comment section of the perticular message.
 * @param msgID
 */
function getMessageComment(msgID){
	var fd = new FormData();
    fd.append('msgID',msgID);
	 $.ajax ({
			url : "getMessageComment.htm",
			type : "POST",
			data : fd,
			contentType : false,
			processData: false,
			cache : false,
			dayaType : "JSON",
			complete : function() {
			},
			success : function(data) {
				//Service Layer Errors          
				var serErrMsg="";
				if(data.errorList!= undefined || data.errorList!=null){
					if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
						$.each(data.errorList.errorList,function(index,errorBean){
							serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
						});
						showServiceErrors(serErrMsg);
					}
				}else{
					refreshMessageComment(data);
				}
			},
			error : function(xhr, status, error) {
				showError(xhr, status, error)
			}
	}); //  getMessageComment POST Ajax call END 
}
/**
 * This function render the video using dash player.
 * @param elem
 */
function startVideo(elem){
	  var url =  $("#"+elem.id).children("source").attr("src");
	  player.startup();
	  player.attachView(document.querySelector("#"+elem.id));
	  player.setAutoPlay(false);
	  player.attachSource(url);
}
/**
 * This method returns the pending friend list count for the logged in user.
 * @param userID
 * @returns {Number}
 */
function getPendingFriendCount(userID){
	var fd = new FormData();
    fd.append('userID',userID);
	var pendingFriendCount=0;
	 $.ajax ({
			url : "getPendingFriendCount.htm",
			type : "POST",
			data : fd,
			contentType : false,
			processData: false,
			cache : false,
			async: false,
			dayaType : "JSON",
			complete : function() {
			},
			success : function(data) {
				//Service Layer Errors          
				var serErrMsg="";
				if(data.errorList!= undefined || data.errorList!=null){
					if(data.errorList.errorList!= undefined || data.errorList.errorList!=null){
						$.each(data.errorList.errorList,function(index,errorBean){
							serErrMsg=serErrMsg+" "+errorBean.excpMsg+" </br>";
						});
						showServiceErrors(serErrMsg);
					}
				}else{
					pendingFriendCount=data.pendingFriendReq;
					return pendingFriendCount;
				}
			},
			error : function(xhr, status, error) {
				showError(xhr, status, error)
			}
	}); //  getPendingFriendCount POST Ajax call END
	return pendingFriendCount;
}
