
/**
 * This js file contains functions related to the azchat friend request
 * management and content sharing.
 */
$(document).ready(function() {
	$("#div_contentShare").show();
	$("#div_FrndReqStatus").hide();
	$("#errorDivAjaxPage").hide();
	$("#div_ProfileEdit").hide();
	
	$('input[id=input_shareContentPhotoVedio]').change(function() {
		$('#picUrl').val($(this).val());
	});
	
	$('#openMedia').click(function() {
		$('input[id=input_shareContentPhotoVedio]').click();
	});
	
	//NIR  START
	$('[data-toggle=offcanvas]').click(function() {
		$(this).toggleClass('visible-xs text-center');
		$(this).find('i').toggleClass('glyphicon-chevron-right glyphicon-chevron-left');
		$('.row-offcanvas').toggleClass('active');
		$('#lg-menu').toggleClass('hidden-xs').toggleClass('visible-xs');
		$('#xs-menu').toggleClass('visible-xs').toggleClass('hidden-xs');
		$('#btnShow').toggle();
	});
	$('.status').click(function() {
			$('.arrow').css("left", 7);
			$("#staticContent").show();
			$("#mediaContent").hide();
	});
	$('.photos').click(function() {
			$('.arrow').css("left", 170);
			$("#staticContent").hide();
			$("#mediaContent").show();
	});
	
	pendingFrndMap = {};
	map = {};
	//NIR END
	$("#input_searchFrnds").typeahead({
		source : function(query, process) {
		   var $friends=new Array;
		   $friends=[""]
			return $.ajax({
				url : "searchFriends.htm",
				type : "GET",
				data : {
					"firstName" : query
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
	        return item.replace( regex, "<strong>$1</strong>" );
	    },
	    updater: function (item) {
	    	var item = JSON.parse(item); 
	    	processSelectedFriend(item.id,$("#hi_UserID").val(),map[item.id])
	        return item.name;
	    }
});
	
/**
 * Get list of Pending friend Requests.(getPendingFriendList)
 */	
$("#li_userIcon").click(function(event){
	$("ul#pndngFrndList").empty();
	$.ajax({
		url : "getPendingFriendList.htm",
		type : "GET",
		data : { 
			    "logedInUserID" : $("#hi_UserID").val(),
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
				$("ul#pndngFrndList").html('<li id=frndID><a href="#">No Pending Friend Request.</a></li>');
			}
			else{
				//Create global map for pending friends for this user.
				$.each(data.friendList,function(index,friendBean){
					pendingFrndMap[friendBean.friendID]=friendBean;
					$("ul#pndngFrndList").html('<li id='+friendBean.friendID+'><a href="#">'+friendBean.friendName+'<button class="btn-mini btn-primary" style="margin-left: 10px;" value="approve"><i class="glyphicon glyphicon-ok icon-white"></i><span>Approve</span></button><button class="btn-mini btn-primary" style="margin-left: 10px;" value="reject"><i class="glyphicon glyphicon-remove icon-white"></i><span>Reject</span></button></a></li>');			
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
	var friendID = $(this).closest("li").attr("id");
	var status  = $(this).attr("value");
	approveRejectFriend(friendID, pendingFrndMap[friendID].userID, pendingFrndMap[friendID].friendName, pendingFrndMap[friendID].friendPhotoUrl,status);
});


/**
 * This click events triggers the ajax call for addFriend and approveReject friend based on the button value.
 */	
$("#btn_status,btn_reject").click(function(event){
	var btn_val=$("#btn_status").text();
	if(btn_val=='Rejected' || btn_val=='Friend' || btn_val=='Pending Approval' || btn_val == 'Friend Request Sent' )	{
		return false;
	}
	if(btn_val=='Add Friend'){
		addFriend($("#input_frndID").val(),$("#hi_UserID").val(),map[$("#input_frndID").val()].friendName,map[$("#input_frndID").val()].friendPhotoUrl);
	}else if(btn_val=='Approve' || btn_val=='Reject'){
	  approveRejectFriend($("#input_frndID").val(),$("#hi_UserID").val(),map[$("#input_frndID").val()].friendName,map[$("#input_frndID").val()].friendPhotoUrl,btn_val);
	  $("#btn_reject").empty().hide();	
	  $("#btn_status").empty().hide();
	  $("#span_status").text("Friend").show();
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
	$("#input_searchFrnds").val("");
	$("#div_ProfileEdit").hide();
	$("#errorDivAjaxPage").empty().hide();
	$("ul#pndngFrndList").empty();
});

/**
 *  This event method will redirect user to the profile edit page. 
 */
$("#li_logInUser").click(function(event){
	goUpdateProfile();
}); //End Profile Edit Click

/**
 * This method update the user profile. (updateUserProfile)
 */
$("#btn_profUpdt_submt").click(function(event){
	//Create the multipart form
	var fd = new FormData();
	var waitDiv = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" aria-hidden="true" role="dialog" style="padding-top:15%; overflow-y:visible;"><div class="modal-dialog modal-m"><h2 align=center>Processing...</h1></div></div>');
	fd.append('logedInUserID',$("#hi_UserID").val());
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
				$("#li_logInUser").val(data.firstName);
				$("#hi_UserName").val(data.firstName);
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
//Typeahead for Upate user profile    start
//User Message/Content Share POST                   start
	/**
	 * This click event trigger the ajax call to store the user content in azure storage.
	 */
   $("#submit_contentShare").click(function(){
	   shareContent();
   });
//User Message/Content Share  POST                   end
	
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
				"logedInUserID" :userID
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
				"logedInUserID" : userID,
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
			 	"logedInUserID" :userID ,
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
 *  This function triggers the ajax call to stote the user text messages and photo or vedio. 
 */
function shareContent(){
	var fd = new FormData();
    fd.append('logedInUserID',$("#hi_UserID").val());
	fd.append('msgText',$("#textArea_status_message").val());
	fd.append('mediaPhoto',$("#input_shareContentPhotoVedio")[0].files[0]);
	fd.append('expiryTime',$("#select_msgExpiryTime").val());
   $.ajax ({
		url : "contentShare.htm",
		type : "POST",
		data : fd,
		contentType : false,
		processData: false,
		cache : false,
		dayaType : "JSON",
		complete : function() {
		},
		success : function(data) {
			$("#textArea_status_message").val("")
			showSuccess(data.msg);
		},
		error : function(xhr, status, error) {
			showError(xhr, status, error)
		}
	}); //  Content Share POST Ajax call END 
}

/**
 * This method returns the bootstrap modal to show the process progress.
 * @returns bootstrap model  
 */
function getProgressModal(){
	var waitDiv = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" aria-hidden="true" role="dialog" style="padding-top:15%; overflow-y:visible;"><div class="modal-dialog modal-m"><h2 align=center>Processing...</h1></div></div>');
	return waitDiv;
}
