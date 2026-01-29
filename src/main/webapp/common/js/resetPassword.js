var checkemail = 0;
var oldpassword = 0;
function validforgotPassword(){
	validatePromoJSON = {
            "validatorData": [
                  
                    { "Element": "#emailId", "FunctionName": "emailId" }
                    
	 
            ]
        };
	 	if(iterateThroughtElemsForValidations(validatePromoJSON))
		 {
	 		if(checkemail == 0){
				 document.getElementById('emailIdError').innerHTML="";

	 		return true;
	 		}
	 		else{
		 		 document.getElementById('emailIdError').innerHTML="";
				 var userId = document.createElement("label");
				 userId.innerHTML = "Email Id Not Registered";
			     document.getElementById('emailIdError').appendChild(userId);
		 		return false;
		 	}
			  

		 }
	 	else{
	 		return false;
	 	}
	 	
}


function checkExist1(){
	
	
	var r=false;
	var emailId = document.getElementById('emailId').value;
	
	 var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

	 if(emailId=="")
		 {
		 
		 document.getElementById('emailIdError').innerHTML="";
		 var userId = document.createElement("label");
		 userId.innerHTML = "Please Enter Email Id ";
	     document.getElementById('emailIdError').appendChild(userId);
       r= false;
		 }
	 else
		 {
		 
		 
	 
       if (reg.test(emailId) == false) 
       {
    	   document.getElementById('emailIdError').innerHTML="";
			 var userId = document.createElement("label");
			 userId.innerHTML = "Invalid Email Id ";
		     document.getElementById('emailIdError').appendChild(userId);
           r= false;
       }
       else
       	
       {
    	 		var url = "checkEmailIdResetPassword?emailId="+emailId+"";

    		if (window.XMLHttpRequest) {
    				req = new XMLHttpRequest();
    			}
    			else if (window.ActiveXObject) {
    				isIE = true;
    				req = new ActiveXObject("Microsoft.XMLHTTP");
    			}   
    		               
    		    req.onreadystatechange = checkExistRequest;
    		    req.open("GET", url, true); 
    		              
    		    req.send(null);
    		    
    		    r= true;
       	
      	}
       return r;
      
		 }
	 
	 
	 return r;
	 
	 
}
var r=false;
	function checkExist(){
		
	
		var emailId = document.getElementById('emailId').value;
		
		 var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

		 if(emailId=="")
			 {
			 
			 document.getElementById('emailIdError').innerHTML="";
			 var userId = document.createElement("label");
			 userId.innerHTML = "Please Enter Email Id ";
		     document.getElementById('emailIdError').appendChild(userId);
	       r= false;
			 }
		 else
			 {
			 
			 
		 
	       if (reg.test(emailId) == false) 
	       {
	    	   document.getElementById('emailIdError').innerHTML="";
				 var userId = document.createElement("label");
				 userId.innerHTML = "Invalid Email Id ";
			     document.getElementById('emailIdError').appendChild(userId);
	           r= false;
	       }
	       else
	       	
	       {
	    	 		var url = "checkEmailIdResetPassword?emailId="+emailId+"";

	    		if (window.XMLHttpRequest) {
	    				req = new XMLHttpRequest();
	    			}
	    			else if (window.ActiveXObject) {
	    				isIE = true;
	    				req = new ActiveXObject("Microsoft.XMLHTTP");
	    			}   
	    		               
	    		    req.onreadystatechange = checkExistRequest;
	    		    req.open("GET", url, true); 
	    		              
	    		    req.send(null);
	    		    
	    		 //   r= true;
	       	
	      	}
	      
	       return r;
	      
			 }
		 
		 return r;

			/*var emailId = document.getElementById('emailId').value;
	 		var url = "checkEmailIdResetPassword?emailId="+emailId+"";

			if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = checkExistRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);*/
			
	}
	function checkExistRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
			
				var exist = req.responseText;
				if(exist == 'true')
				{

					checkemail = 1;
					 document.getElementById('emailIdError').innerHTML="";
					 var userId = document.createElement("label");
					 userId.innerHTML = "Email Id Not Registered";
				     document.getElementById('emailIdError').appendChild(userId);
				     r= false;
				}else
				 {

					
					checkemail = 0;

					 document.getElementById('emailIdError').innerHTML="";
					 r= true;
					
					
					

				 }
				
				
	         	
				
	         }
		}
	}


function validResetPswd(){
	validatePromoJSON = {
            "validatorData": [
                  
                    { "Element": "#emailId", "FunctionName": "emailId" },
                    { "Element": "#password", "ElementToCompare": "#confirmPassword", "FunctionName": "password" }
                    
	 
            ]
        };
	if(iterateThroughtElemsForValidations(validatePromoJSON) && checkemail == 0)
	 {
		document.getElementById('modifyResetPassword').submit();
		return true;
		
	 }
	else if(iterateThroughtElemsForValidations(validatePromoJSON) && checkemail == 1){
		document.getElementById('emailIdError').innerHTML="";
		 var userId = document.createElement("label");
		 userId.innerHTML = "Email Id Not Registered";
	     document.getElementById('emailIdError').appendChild(userId);
	     return false;
	}
else{
	 return false;
}
}
function checkThisOldPassword(oldpassword){
	var url = "checkcheckThisOldPswdResetPassword?oldpassword="+oldpassword+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = checkThisOldPasswordRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}

function checkThisOldPasswordRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
		
			var exist = req.responseText;
			if(exist == 'true')
			{
				 oldpassword = 1;
				 document.getElementById('oldPasswordError').innerHTML="";
				 var userId = document.createElement("label");
				 userId.innerHTML = "Old Password Is Wroung";
			     document.getElementById('oldPasswordError').appendChild(userId);
			     return false;

			}else
			 {
				oldpassword = 0;

				 document.getElementById('oldPasswordError').innerHTML="";
				 return true;
			 }
			
			
         	
			
         }
	}
}
function validChangePassword(){
	validatePromoJSON = {
            "validatorData": [
                  

					{ "Element": "#oldPassword", "FunctionName": "notEmpty" },
                    { "Element": "#password", "ElementToCompare": "#confirmPassword", "FunctionName": "password" },
                    
                    
                    
	 
            ]
        };
	 	if(iterateThroughtElemsForValidations(validatePromoJSON) && oldpassword == 0)
		 {
	 		
	 		var mobile=document.getElementById("mobile").value;
	 		if(mobile.length<9 || mobile==''){
	 			
	 			jAlert('error', 'please enter mobile number!', 'Error Dialog');
	 			
	 			setTimeout(function() {
	 				$("#popup_container").remove();
	 				removeAlertCss();
	 			}, alertmsgduration);
	 			
	 			
	 		} else {
	 			
	 			var url = "sendotpforchangepasswordDiaryMangent?mobile="+mobile+"";
				
				if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
				               
				req.onreadystatechange = updatepasswordRequest;
				req.open("GET", url, true); 
				req.send(null);
		 		
	 			
	 			
	 		}
	 		/*document.getElementById('changePaswdResetPassword').submit();
	 		return true;*/
	 		
		 }
	 	else if(iterateThroughtElemsForValidations(validatePromoJSON) && oldpassword == 1){
	 		 document.getElementById('oldPasswordError').innerHTML="";
			 var userId = document.createElement("label");
			 userId.innerHTML = "Old Password Is Wroung";
		     document.getElementById('oldPasswordError').appendChild(userId);
		     return false;
	 	}
	 else{
		 return false;
	 }
}


function updatepasswordRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				var str = req.responseText;
				document.getElementById("newotp").value=str;
				$('#otpmodel').modal("show");
			}
		}
	}


function confirmotp(){
	var otp = document.getElementById("otp").value;
	var oldotp = document.getElementById("newotp").value;
	var confirm_pwd = document.getElementById("password").value;
	var mobile = document.getElementById("mobile").value;
	if(otp==''){
		jAlert('error', "Plz enter otp!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
	
	}else if(otp!=oldotp){
		jAlert('error', "Plz enter valid OTP!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		document.getElementById("otp").value='';
		$('#otpmodel').modal("show");
	}else{
		/*var url = "updatepharmacyuserpwdDiaryMangent?mobile="+mobile+"&confirm_pwd="+confirm_pwd+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
		req.onreadystatechange = confirmotpRequest;
		req.open("GET", url, true); 
		req.send(null);*/
		document.getElementById("changePaswdResetPassword").submit();
	}
}
/*function confirmotpRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				window.location = 'Logout';
			}
		}
	}*/


