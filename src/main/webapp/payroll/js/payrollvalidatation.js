function setpersal(id, val, perid) {
	var regx=/^\d*\.?\d*$/;
	if(val>100 || !regx.test(val) || val<0){
		jAlert('error', "Please Enter % under 100, Non Negative and Number Only!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		document.getElementById("" + id).value =0;
		document.getElementById(perid).value =0;
	}else{
	var basic = Number(document.getElementById("basicsal").value);
	var peramt = val / 100 * basic;
	document.getElementById("" + id).value = Number(peramt);
	setnetearn();
	}
}
function setbasicsal(id, val) {
	var regx=/^\d*\.?\d*$/;
	var fixedsal = Number(document.getElementById("fixedsal").value);
//	if(Number(fixedsal)>0 || fixedsal!=''){
//		document.getElementById("basicsalper").readOnly=true;
//	}else{
//		document.getElementById("basicsalper").readOnly=false;
//	}
	if(val>100 || !regx.test(val) || val<0){
		jAlert('error', "Please Enter % under 100, Non Negative and Number Only!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		document.getElementById("" + id).value =0;
		document.getElementById("basicsalper").value =0;
	}else{
		
	
	var peramt = val / 100 * fixedsal;
	document.getElementById("" + id).value = Number(peramt);
	}
	setnetearn();
}
function setfixedsal() {
	var regx=/^\d*\.?\d*$/;
	var fixedsal = Number(document.getElementById("fixedsal").value);
	if(Number(fixedsal)<=0 || fixedsal==''|| !regx.test(fixedsal)){
		document.getElementById("basicsalper").readOnly=true;
		jAlert('error', "Please Enter Non Nagative and Number Value!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		document.getElementById("fixedsal").value=0;
	}else{
		document.getElementById("basicsalper").readOnly=false;
	}
}
function getdesignation(val) {
	var url = "getdesignationPayrollEmployee?deptid=" + val + "";

	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}

	req.onreadystatechange = getdesignationRequest;
	req.open("GET", url, true);

	req.send(null);

}
function getdesignationRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {

			document.getElementById("designationdiv").innerHTML = req.responseText;
		}
	}
}
function addempvalidate() {
	var firstname = document.getElementById("firstname1").value;
	var lastname = document.getElementById("lastname1").value;
//	var email = document.getElementById("email").value;
//	var empcode = document.getElementById("empcode").value;
//	var datejoin = document.getElementById("date_join").value;
//	var department = document.getElementById("department").value;
	var dob = document.getElementById("dob1").value;
	var mobNo = document.getElementById("mobNo1").value;
	var date_join = document.getElementById("date_join").value;
	var emailregEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	var isError = false;
	var regEx = /^\d{10}$/;
	if (firstname == '') {

		jAlert('error', "Please enter First Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (firstname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} else if (lastname == '') {
		jAlert('error', "Please enter Last Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (lastname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} /*else if (dob=='') {

		jAlert('error', "Please Enter DOB!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} */
	/*else if (email == '') {
		jAlert('error', "Please enter Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (!emailregEx.test(email)) {
		jAlert('error', "Please enter Valid Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (empcode == '') {
		jAlert('error', "Please enter Employee Code!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (datejoin == '') {
		jAlert('error', "Please enter Joining Date!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (department == '') {
		jAlert('error', "Please Select Department!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (designation == '') {
		jAlert('error', "Please Select Designation!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}*/else if(!regEx.test(mobNo)) {
		var mobNo = document.createElement("label");
	    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}
	else if(mobNo.length!=10) {
	    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else {
		if (isError == true) {
		} else {
			document.getElementById("empreg").style.display="none";
			$('#loader-wrapper').modal( "show" );
			document.getElementById("addemployeefrm").submit();
		}
	}
}
function updatecust(){
		var firstname = document.getElementById("firstname").value;
		var lastname = document.getElementById("lastname").value;
//		var email = document.getElementById("email").value;
//		var empcode = document.getElementById("empcode").value;
//		var datejoin = document.getElementById("date_join").value;
//		var department = document.getElementById("department").value;
		var dob = document.getElementById("dob").value;
		var mobNo = document.getElementById("mobNo").value;
		var date_join = document.getElementById("date_join").value;
		var emailregEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		var isError = false;
		var regEx = /^\d{10}$/;
		if (firstname == '') {

			jAlert('error', "Please enter First Name!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (firstname.match(/\s/g)) {

			jAlert('error', "Space Not Allowed!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;

		} else if (lastname == '') {
			jAlert('error', "Please enter Last Name!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (lastname.match(/\s/g)) {

			jAlert('error', "Space Not Allowed!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;

		} /*else if (dob=='') {

			jAlert('error', "Please Enter DOB!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;

		} */
		/*else if (email == '') {
			jAlert('error', "Please enter Email ID!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (!emailregEx.test(email)) {
			jAlert('error', "Please enter Valid Email ID!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (empcode == '') {
			jAlert('error', "Please enter Employee Code!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (datejoin == '') {
			jAlert('error', "Please enter Joining Date!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (department == '') {
			jAlert('error', "Please Select Department!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if (designation == '') {
			jAlert('error', "Please Select Designation!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		}*/else if(!regEx.test(mobNo)) {
			var mobNo = document.createElement("label");
		    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		}
		else if(mobNo.length!=10) {
		    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
		}else {
			if (isError == true) {
			} else {
				document.getElementById("empreg").style.display="none";
				$('#loader-wrapper').modal( "show" );
				document.getElementById("updateempfrm").submit();
			}
		}
	}
function updateempvalidate() {
	var firstname = document.getElementById("firstname").value;
	var lastname = document.getElementById("lastname").value;
	var dob = document.getElementById("dob").value;
	var gender = document.getElementById("gender").value;
	var currentaddress = document.getElementById("currentaddress").value;
	var email = document.getElementById("email").value;
	var county = document.getElementById("county").value;
	var town = document.getElementById("town").value;
	var mobNo = document.getElementById("mobNo").value;
	var department = document.getElementById("department").value;
	var designation = document.getElementById("designation").value;
	var nationality = document.getElementById("nationality").value;
	var alpha = /^[a-zA-Z-,]+(\s{0,1}[a-zA-Z-, ])*$/;
	var regEx = /^\d{10}$/;
	var emailregEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	var isError = false;

	if (firstname == '') {

		jAlert('error', "Please enter First Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (firstname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} else if (lastname == '') {
		jAlert('error', "Please enter Last Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (lastname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} else if (dob == '') {
		jAlert('error', "Please enter DOB!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (gender == 0) {
		jAlert('error', "Please Select Gender!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (currentaddress == '') {
		jAlert('error', "Please Enter Address!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (email == '') {
		jAlert('error', "Please enter Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (!emailregEx.test(email)) {
		jAlert('error', "Please enter Valid Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (county == 0) {
		jAlert('error', "Please Select County/State!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (town == 0) {
		jAlert('error', "Please Select Town!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}else if (nationality=='') {
		jAlert('error', "Please Enter  Nationality!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}else if (!alpha.test(nationality)) {
		jAlert('error', "Please Enter Alphabet Only In Nationality!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}else if (mobNo == '') {
		jAlert('error', "Please Enter Mobile No!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (!regEx.test(mobNo)) {
		jAlert('error', "Please Enter Valid Mobile No!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (department == '') {
		jAlert('error', "Please Select Department!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (designation == '') {
		jAlert('error', "Please Select Designation!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else {
		if (isError == true) {
		} else {
			document.getElementById("updateempfrm").submit();
		}
	}
}

function tdscal(val,id,rid) {
	var regx=/^\d*\.?\d*$/;
	if(val>100 || !regx.test(val) || val<0){
		jAlert('error', "Please Enter % under 100, Non Negative and Number Only!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		document.getElementById(id).value =0;
		document.getElementById(rid).value =0;
	}else{
	var basic = Number(document.getElementById("basicsal").value);
	var da = Number(document.getElementById("da").value);
	var hra = Number(document.getElementById("hra").value);
	var conveyance = Number(document.getElementById("conveyance").value);
	var perdevallow = Number(document.getElementById("perdevallow").value);
	var medical_allownces = Number(document.getElementById("medical_allownces").value);
	var sal = basic + da + hra + conveyance + perdevallow + medical_allownces;
	var peramt = val / 100 * sal;
	document.getElementById("tds").value = peramt.toFixed(2);
	}
	setnetearn();
	
}
function leavevalidate(){
	var leavetype = document.getElementById("leavetype").value;
	var leavefromdate = document.getElementById("leavefromdate").value;
	var leavetodate = document.getElementById("leavetodate").value;
	var leave_reason = document.getElementById("leave_reason").value;
	var isError = false;
	  if (leavetype == '0') {
			jAlert('error', "Please Select Leave Type!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		}else if(leavefromdate==''){
			jAlert('error', "Please Enter From Date!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if(leavetodate==''){
			jAlert('error', "Please Enter To Date!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if(leave_reason==''){
			jAlert('error', "Please Enter To Leave Reason!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} 
	  
	  
	  else {
			if (isError == true) {
			} else {
				document.getElementById("empreg").style.display="none";
				$('#loader-wrapper').modal( "show" );
				document.getElementById("leaveform").submit();
			}
		}
	
}
	  function updateempbankvalidate(){
		  var bank_name = document.getElementById("bank_name").value;
		  var bank_branch = document.getElementById("bank_branch").value;
		  var account_no = document.getElementById("account_no").value;
			var ifsc_code = document.getElementById("ifsc_code").value;
			var alpha = /^[a-zA-Z-,]+(\s{0,1}[a-zA-Z-, ])*$/;
			 var numbers = /^[0-9]+$/;
			var isError = false;

			if (bank_name == '0' || bank_name == '') {

				jAlert('error', "Please enter Bank Name!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}else if(!alpha.test(bank_name)){
				jAlert('error', "Only Alphabet Allowed in Bank Name!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}else if (bank_branch == '0' || bank_branch == '') {
				jAlert('error', "Please enter Bank Branch Address!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}else if (account_no == '0' || account_no == '') {
				jAlert('error', "Please Enter Account No!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}else if ( !numbers.test(account_no)) {
				jAlert('error', "Only number allowed in Account No.!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}else if (ifsc_code == '0' || ifsc_code == '') {
				jAlert('error', "Please enter IFSC Code!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
			}

			  else {
					if (isError == true) {
					} else {
						document.getElementById("updatebankfrm").submit();
					}
				}
	  }

function updateempuanpfvalidate() {
	 var uanno = document.getElementById("uanno").value;
	 var esicno = document.getElementById("esicno").value;
	 var numbers = /^[0-9\s]*$/;
		var isError = false;
		if (!numbers.test(uanno)) {
			jAlert('error', "Only number allowed in UAN No..!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		}else if(!numbers.test(esicno)){
			jAlert('error', "Only number allowed in ESIC No.!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		}else {
				if (isError == true) {
				} else {
					$('#loader-wrapper').modal( "show" );
					document.getElementById("uanpffrm").submit();
				}
			}
	
}

function updateleavevalidate() {
	$('#loader-wrapper').modal( "show" );
	document.getElementById("leavegivenfrm").submit();
}
function geteditleaves(id){
	var url = "geteditleavePayrollDashBoard?id="+id+"";
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = geteditleavesRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}
function geteditleavesRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			 var str = req.responseText;
	    	  var data = str.split("~");
	    	  document.getElementById("leavetype1").value = data[0];
	    	  document.getElementById("fromleave").value = data[1];
	    	  document.getElementById("toleave").value = data[2];
	    	  document.getElementById("leavedays").value = data[3];
	    	  document.getElementById("levereason").value = data[4];
	    	  document.getElementById("upleaveid").value = data[5];
	    	  $('#edit_leave').modal( "show" ); 
       }
		}	 
	

}
function insertpunchin(val) {
	var remark="";
	if(val==1){
		remark= document.getElementById("remarkintime").value;
	}else if(val==2){
		remark= document.getElementById("remarkouttime").value;
	}else{
		remark="";
	}
	var url = "setpunchinAttendance?status=" + val + "&remark="+remark+"";

	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}

	req.onreadystatechange = getdesignationRequest;
	req.open("GET", url, true);

	req.send(null);

}
function getdesignationRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {

			window.location.reload();
		}
	}
}
function validatepersonal(){
	 var adhno = document.getElementById("adhno").value;
	 var numbers = /^[0-9]+$/;
		var isError = false;

		if (!numbers.test(adhno)) {

			jAlert('error', "Please enter Number Only in Aadhar No!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
			document.getElementById("adhno").value=0;
		} else if(adhno.length!=12){

				jAlert('error', "Please Enter 12 Digit Aadhar No!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				isError = true;
				document.getElementById("adhno").value=0;
		}

		  else {
				if (isError == true) {
				} else {
					document.getElementById("updatepersonalfrm").submit();
				}
			}
 
}
function currenttime() {
		  var date=new Date();
	
		 document.getElementById("fromtime").value=formatTime(date);
		 $('#add_otrequest').modal( "show" ); 
}
const formatTime = (date) => {
	   var hours = date.getHours();
	  var mins = date.getMinutes();
	  if(hours<=9){
		  hours="0"+hours;
		}
	  if(mins<=9){
		  mins="0"+mins;
		}
	  return `${hours}:${mins}`;
	}
const getTwoDigits = (value) => value < 10 ? `0${value}` : value;

function timevalidate(){
	var fromtime = document.getElementById("fromtime").value;
	var totime = document.getElementById("totime").value;
	var otreason = document.getElementById("ot_reason").value;
	var isError = false;
		 if(fromtime==''){
			jAlert('error', "Please Enter From Time!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if(totime==''){
			jAlert('error', "Please Enter To Time!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} else if(otreason==''){
			jAlert('error', "Please Enter To OT Reason!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			isError = true;
		} 
	  
	  
	  else {
			if (isError == true) {
			} else {
				document.getElementById("empreg").style.display="none";
				$('#loader-wrapper').modal( "show" );
				document.getElementById("otform").submit();
			}
		}
	
}

function changepunchtime(val,id,colname){
	var url = "changepunchtimeAttendance?&id="+id+"&val="+val+"&colname="+colname;
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = changepunchtimereq();
	req.open("GET", url, true); 
	req.send(null);
}
function changepunchtimereq(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			window.location.reload();
			}
		}
}
function selectallapproveleave(val) {
	
	if (val.checked == true) {
		$('.aproveallleaveclass').each(function() { // loop through each checkbox
			this.checked = true; // deselect all checkboxes with class// "checkbox1"
		});
	} else {
		$('.aproveallleaveclass').each(function() { // loop through each checkbox
			this.checked = false; // deselect all checkboxes with class// "checkbox1"
		});
	}
}

function approvedleaveAll(){
	document.getElementById("isgroupdiscount").value="1";
	var flagcheck= false;
	 $('.aproveallleaveclass').each(function() {
			if (this.checked == true) {
				flagcheck= true;
			}
	});
	 if(flagcheck){
		 $('#approvedmodel').modal( "show" );
	 }else{
		 jAlert('error', 'Please select at least one checkbox for all approve!!!', 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	 }
	
}

function approvedLeaveinvoice() {
	 var approve_notes = document.getElementById("approve_notes").value;
	 if(approve_notes==''){
		 jAlert('error', 'Please enter aprrove note!!!', 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	 }else{
		 allaprvenoerroe= false;
		 var isgroupdiscount =document.getElementById("isgroupdiscount").value;
		 if(isgroupdiscount==0){
			 $("#dashboardloaderPopup").modal('show');
		 }else{
			 $("#dashboardloaderPopup").modal('show');
			 	var allaprovediscountids="0";
				 $('.aproveallleaveclass').each(function() {
						if (this.checked == true) {
							var i = this.value;
							allaprovediscountids = allaprovediscountids+","+i;
						}
				});
				 if(!allaprvenoerroe){
					 document.getElementById("allleave_id").value=allaprovediscountids;
		    	     document.getElementById("updateleavefrm").submit();
				 }
		 }
		
	 }

}


function geteditot(id){
	var url = "geteditotPayrollDashBoard?id="+id+"";
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = geteditotRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}
function geteditotRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			 var str = req.responseText;
	    	  var data = str.split("~");
	    	  document.getElementById("fromtime1").value = data[0];
	    	  document.getElementById("totime1").value = data[1];
	    	  document.getElementById("time1").value = data[2];
	    	  document.getElementById("otreason1").value = data[3];
	    	  document.getElementById("upleaveid").value = data[4];
	    	  $('#edit_ot').modal( "show" ); 
       }
		}	 
	

}


function setactivests(clientid,flag){
			   var url="switchautochargePayrollEmployee?clientid="+clientid+"&flag="+flag+"";
			   
			   if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = setactivestsRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);
	 
	
}
function setactivestsRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			location.reload();
		}
	}
	
}

function getsmsCount(){
	
	var url = "gymsmsPayrollEmployee";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = getsmsCountRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	
}
function getsmsCountRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				/*document.getElementById("allPatient").innerHTML = req.responseText;*/
				var data = req.responseText;
				var tmp = data.split('/');
				document.getElementById("tsms").innerHTML = tmp[1];
				document.getElementById("rsms").innerHTML = tmp[2];
				document.getElementById("usms").innerHTML = tmp[0];
				$('#smscountPopupid').modal( "show" );
	         	
				
	         }
		}
	}

function updatecust1() {

	var firstname = document.getElementById("firstname").value;
	var lastname = document.getElementById("lastname").value;
//	var email = document.getElementById("email").value;
//	var empcode = document.getElementById("empcode").value;
//	var datejoin = document.getElementById("date_join").value;
//	var department = document.getElementById("department").value;
	var dob = document.getElementById("dob").value;
	var mobNo = document.getElementById("mobNo").value;
	var date_join = document.getElementById("date_join").value;
	var emailregEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	var isError = false;
	var regEx = /^\d{10}$/;
	if (firstname == '') {

		jAlert('error', "Please enter First Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (firstname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} else if (lastname == '') {
		jAlert('error', "Please enter Last Name!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (lastname.match(/\s/g)) {

		jAlert('error', "Space Not Allowed!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} else if (dob=='') {

		jAlert('error', "Please Enter DOB!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;

	} 
	/*else if (email == '') {
		jAlert('error', "Please enter Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (!emailregEx.test(email)) {
		jAlert('error', "Please enter Valid Email ID!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (empcode == '') {
		jAlert('error', "Please enter Employee Code!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (datejoin == '') {
		jAlert('error', "Please enter Joining Date!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (department == '') {
		jAlert('error', "Please Select Department!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	} else if (designation == '') {
		jAlert('error', "Please Select Designation!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}*/else if(!regEx.test(mobNo)) {
		var mobNo = document.createElement("label");
	    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		isError = true;
	}
	else if(mobNo.length!=10) {
	    jAlert('error', "Please enter valid mobile no.", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else {
		if (isError == true) {
		} else {
			document.getElementById("empreg").style.display="none";
			$('#loader-wrapper').modal( "show" );
			document.getElementById("updateempfrm").submit();
		}
	}

	
}