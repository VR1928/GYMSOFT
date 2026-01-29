/**
 * 
 */

function savePatient() {
	
	var button= document.getElementById("invstibtn");
	button.style.display = "none";
	var title=document.getElementById("title").value;
	var firstName=document.getElementById("firstName").value;
	var middleName=document.getElementById("middleName").value;
	var lastName=document.getElementById("lastName").value;
    	
	var age=document.getElementById("age").value;
	var address=document.getElementById("address").value;	
	var mobNo=document.getElementById("mobNo").value;
	var email=document.getElementById("email").value;
    var doctorName=document.getElementById("doctorname").value;    	
	var state= document.getElementById("state").value;
	var town= document.getElementById("town").value;
	var dob= document.getElementById("dob").value;
	var gender = document.getElementById("gender").value;
	
	
	document.getElementById('fnameError').innerHTML = "";
	document.getElementById('lnameError').innerHTML = "";
	document.getElementById('doctorError').innerHTML="";
	document.getElementById('stateError').innerHTML="";
	document.getElementById('ageError').innerHTML="";
	document.getElementById('townError').innerHTML="";
	document.getElementById('dobError').innerHTML="";
	if(document.getElementById('genderError1')){
		document.getElementById('genderError1').innerHTML="";
	}
	
	var chk=0;
	
	var regEx = /^\d{10}$/;
	    var emailregEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

		if(firstName == ""){
			var firstn = document.createElement("label");
			firstn.innerHTML = "Please Enter First Name";
		    document.getElementById('fnameError').appendChild(firstn);
		    chk=1;
		}
//		if(firstName.match(/\s/g)){
//			var firstn = document.createElement("label");
//			firstn.innerHTML = "Space Not Allow";
//		    document.getElementById('fnameError').appendChild(firstn);
//		    chk=1;
//			
//
//		}
//		if(firstName.match(/[^a-zA-Z]+/)){
//			var firstn = document.createElement("label");
//			firstn.innerHTML = "Special Character or Number Not Allow";
//		    document.getElementById('fnameError').appendChild(firstn);
//		    chk=1;
//		}
		if(lastName == ""){
			var lastName = document.createElement("label");
			lastName.innerHTML = "Please Enter Last Name";
		    document.getElementById('lnameError').appendChild(lastName);
		    chk=1;
		}
	/*	if(lastName.match(/\s/g)){
			var lastName = document.createElement("label");
			lastName.innerHTML = "Space Not Allow";
		    document.getElementById('lnameError').appendChild(lastName);
		    chk=1;
		    
		}
		if(lastName.match(/[^a-zA-Z]+/)){
			var lastName = document.createElement("label");
			lastName.innerHTML = "Special Character or Number Not Allow";
		    document.getElementById('fnameError').appendChild(lastName);
		    chk=1;
		}
		
		if(middleName.match(/[^a-zA-Z]+/)){
			var middleName = document.createElement("label");
			middleName.innerHTML = "Special Character or Number Not Allow";
		    document.getElementById('fnameError').appendChild(middleName);
		    chk=1;
		}*/
		
		if(dob == ""){
			var  dob= document.createElement("label");
			dob.innerHTML = "Please select DOB";
		    document.getElementById('dobError').appendChild(dob);
		    chk=1;
		}
	
		if(doctorName == ""){
			var  doctorName= document.createElement("label");
			doctorName.innerHTML = "Please Enter Doctor Name";
		    document.getElementById('doctorError').appendChild(doctorName);
		    chk=1;
		}
		if(age == ""){
			var  doctorName= document.createElement("label");
			doctorName.innerHTML = "Please Enter Age";
		    document.getElementById('ageError').appendChild(doctorName);
		    chk=1;
		}
		if(state == "0"){
			var  statename= document.createElement("label");
			statename.innerHTML = "Please Select State";
		    document.getElementById('stateError').appendChild(statename);
		    chk=1;
		}
		if(town == "0"){
			var cityName= document.createElement("label");
			cityName.innerHTML = "Please Select City";
		    document.getElementById('townError').appendChild(cityName);
		    chk=1;
		}
		if(document.getElementById('genderError1')){
			if(gender==''){
				var genderName= document.createElement("label");
				genderName.innerHTML = "Please select gender";
				document.getElementById('genderError1').appendChild(genderName);
				chk=1;
			}
		}
		if(mobNo == "") {
			
		}
		else if(!regEx.test(mobNo)) {
			var mobNo = document.createElement("label");
			mobNo.innerHTML = "Please Enter Valid No.";
		    document.getElementById('mobNoError1').appendChild(mobNo);
		    chk=1;
		}else if(mobNo.length!=10) {
			var mobNo = document.createElement("label");
			mobNo.innerHTML = "Please Enter Valid No.";
		    document.getElementById('mobNoError1').appendChild(mobNo);
		    chk=1;
		}
		if(chk==1) {
			button.style.display = "block";
			 return false;
		}	
		else {
			$("#baselayout1loaderPopup").modal("show");
		     document.getElementById("patientForm").submit();
					   	
		}
		 
}


function showPopUp(){
	
	var url = "showclientInvestigation";

	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showAllPatientPopUpRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function showAllPatientPopUpRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				document.getElementById("allPatient").innerHTML = req.responseText;
				$('#clientSearchDiv').modal( "show" );	
	         }
		}
	}
	
	
	function setPatientName(id,type,age,gender,dname) {
		var name=document.getElementById("firstnameid"+id).value;
	 patientId = id;
	 
 diaryuserId = 0;
 editcondition_id = 0;
 editAppointId = 0;
 

	
		
		var n=name.split("/");
		var newname="";
		
		if(n.size==3){
			newname=n[0]+" "+n[1]+" "+n[2]; 
		}else{
			 newname=n[0]+" "+n[1]; 
		}
		
		
		var d=dname.split("/");
		var doctorname = '';
		for(i=0;i<d.length;i++){
			doctorname = d[0]+" "+d[1]+" "+d[2]; 
		}
		
		var data="Name: "+newname+" | Age: "+age+"/ "+gender+"";
		
	    document.getElementById("invstcmyModalLabel").innerHTML=data;
	    document.getElementById("invstpriscdoctornameid").innerHTML = doctorname;
	     document.getElementById("invstdatetimeid").innerHTML = document.getElementById("invstdateandtime").value;
	     
	    
		
		$('#investigationpopup').modal( "show" );

        $('#clientSearchDiv').modal( "hide" );	 		
		
	}
	
	
	
	function searchPatient(){
	var searchText = document.getElementById("searchText1").value;
	var url = "searchParticularPatientInvestigation?searchText="+searchText+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = searchPatientRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function searchPatientRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				document.getElementById("allPatient").innerHTML = req.responseText;
	         	
				
	         }
		}
	}
	


function getCitiesajax(val) {
     var url="getcitiesajaxInventory?state="+val+"";
  
     if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = getCitiesajaxRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
 
 }
 
 function getCitiesajaxRequest(){
    if (req.readyState == 4) {
		if (req.status == 200) {
		   
		   document.getElementById("citydiv").innerHTML=req.responseText;
		    $("#town").trigger("chosen:updated");
			$(".chosen").chosen({allow_single_deselect: true});
         }
	}	 	
}

  function getStateAjax(val) {
     var url="getstateajaxInventory?city="+val+"";
  
     if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = getStateAjaxRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
 
 }
 
 function getStateAjaxRequest(){
    if (req.readyState == 4) {
		if (req.status == 200) {
		   document.getElementById("statediv").innerHTML=req.responseText;
         }
	}	 
}

function getStateAjaxnew(val) {
     var url="getstateajaxnewInventory?city="+val+"";
  
     if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = getStateAjaxRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
 
 }
 
 function getStateAjaxRequest(){
    if (req.readyState == 4) {
		if (req.status == 200) {
		   document.getElementById("statediv").innerHTML=req.responseText;
         }
	}	 
}





