var clientname = "";
$(document).ready(function(){
	
	if(document.getElementById('message').value == 'Client Registered Suceessfully'){
		
		openPopUP();
		
	}
});	

var patientNameOrig="";
function changeStatusToActive(id){
	var url = "changeStatusToActiveClient?id="+id+"";
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = changeStatusToActiveRequest;
    req.open("GET", url, true); 
              
    req.send(null);
	
}

function changeStatusToActiveRequest(){
	
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			
			window.location.reload();
			
		}
	}
}

function changeStatusToInActive(id){
	
	var url = "changeStatusToInActiveClient?id="+id+"";
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = changeStatusToInActiveRequest;
    req.open("GET", url, true); 
              
    req.send(null)
	
}

function changeStatusToInActiveRequest(){
	
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			
			window.location.reload();
			
		}
	}
}	


function showByStatus(){
	document.getElementById('clientFrm').submit();
}
var cid = "";
var note = "";

function addNote(id){
	cid = id;
	
	
	 note = document.getElementById('note'+id).value;
	 if(note == "" || note == null || note == undefined){
			jAlert('error', 'Plese Enter Note.', 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			return false;
	 }
	 else{
	var url = "saveNoteClient?note="+note+"&id="+id+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = addNoteRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);


	    return true;
	 }
	
}
function addNoteRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById('td'+cid).innerHTML = req.responseText;
			
		}
	 }
	}
 
function editNote(id){
	cid = id;
	
	var url = "getNoteAndSetClient?id="+id+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = editNoteRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);


	    return true;
}
function editNoteRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			document.getElementById('td'+cid).innerHTML = req.responseText;
			
		}
	 }
	}

function updateNote(id){
	cid = id;
	
	 note = document.getElementById('note'+id).value;
	var url = "updateNoteClient?note="+note+"&id="+id+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = updateNoteRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);


	    return true;
	
}
function updateNoteRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById('td'+cid).innerHTML = req.responseText;
			
		}
	 }
	}
function deleteNote(id){
	cid = id;
	
	var url = "deleteNoteClient?id="+id+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = deleteNoteRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);


	    return true;
}
function deleteNoteRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById('td'+cid).innerHTML = req.responseText;
			
		}
	 }
	}
function sortByPractitioner(id){
	document.getElementById('clientFrm').submit();
}

function hideRow(size){
	

		for(var i = 1;i<=size ; i++){
			
			document.getElementById("nametd"+i).style.display = 'none';
			document.getElementById("emailtd"+i).style.display = 'none';
			
		}
	
		//document.getElementById('showLink').style.display = '';
		var str = "Name <a href='#' onclick=showRow("+size+")>Show</a>";
		document.getElementById('hideLink').innerHTML = str;
	}
function showRow(size){
	

	for(var i = 1;i<=size ; i++){
		
		document.getElementById("nametd"+i).style.display = 'block';
		document.getElementById("emailtd"+i).style.display = 'block';
		
	}

	var str = "<a href='#' onclick=fnPagination(6,'firstname');>Name </a> <a href='#' onclick=hideRow("+size+")>Hide</a>";
	document.getElementById('hideLink').innerHTML = str;
}
	

function fnPagination(type,param){
	var oPage_number = document.getElementById("page_number");
	var page_number = parseInt(oPage_number.value);
	switch (type)
	{
	case 1://Next
		oPage_number.value = (page_number+1);
		break;
	case 2://Last
		oPage_number.value = param;
		break;
	case 3://Previous
		oPage_number.value = (page_number-1);
		break;
	case 4://First
		oPage_number.value = 1;
		break;
	case 5://Page size change
		oPage_number.value = 1;
		break;
	case 6://sort
		var oSortColumn = document.getElementById("sortColumn");
		var oSortOrder = document.getElementById("sortOrder");
		if(oSortColumn.value == param && oSortOrder.value != "DESC") 
			oSortOrder.value = "DESC";
		else oSortOrder.value = "ASC";
		oSortColumn.value = param;
		break;
	}
	
	//document.paginationForm.submit();
	//$("#paginationForm").submit();
	document.getElementById('paginationForm').submit();
}

function createTreatmentEpisode(clientId,name,condition,policyno,whopay,tpname){
	treatmentpolicyno = policyno;
	document.getElementById('selectTpbtn').style.display = 'none';
	document.getElementById('clientId').value = clientId;

	var client = name;
	var conditionType = condition;
	clientname = name;
	document.getElementById('tempclientid').value = clientId;
	document.getElementById('tempclientname').value = name;
	document.getElementById('treatmentType1').value = conditionType;
	$("#treatmentType1").trigger("chosen:updated");
	$(".chosen").chosen({allow_single_deselect: true});
	
	if(whopay == 'Third Party')
		
	{
		
		document.getElementById('invoicee').value = tpname;	
		document.getElementById('authorisationCode').disabled = false;
		document.getElementById('payby').checked = true;
		document.getElementById('payby1').checked = false;
		
		document.getElementById('policynodiv').style.display='';
		document.getElementById('trtmentPolicyNo').value = treatmentpolicyno;
	}
	else{
		
		document.getElementById('invoicee').value = clientname;	
		document.getElementById('authorisationCode').disabled = true;
		document.getElementById('payby1').checked = true;
		document.getElementById('payby').checked = false;
		
		document.getElementById('policynodiv').style.display='none';
	}
	
	$('#addTreatmentEpisodeDiv').modal( "show" );
	
		/*var url = "setWhoWillPayClient?clientId="+clientId+"&client="+client+"";

		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = createTreatmentEpisodeRequests;
	    req.open("GET", url, true); 
	              
	    req.send(null);	*/
	

}
/*function createTreatmentEpisodeRequests(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			var data = req.responseText;
				
				var temp = data.split("/");
				var name = temp[0];			
				var whopay= temp[1];
				treatmentpolicyno = temp[2];
				
				if(whopay == 'Third Party')
					
				{
					
					document.getElementById('invoicee').value = name;	
					document.getElementById('authorisationCode').disabled = false;
					document.getElementById('payby').checked = true;
					document.getElementById('payby1').checked = false;
					
					document.getElementById('policynodiv').style.display='';
					document.getElementById('trtmentPolicyNo').value = treatmentpolicyno;
				}
				else{
					
					document.getElementById('invoicee').value = clientname;	
					document.getElementById('authorisationCode').disabled = true;
					document.getElementById('payby1').checked = true;
					document.getElementById('payby').checked = false;
					
					document.getElementById('policynodiv').style.display='none';
				}
					
				$('#addTreatmentEpisodeDiv').modal( "show" );
					
				
				
				
	         }
		}
	 
	}
*/
function saveTreatment(){
	
	var chk=0;
	var client = document.getElementById('tempclientname').value;
	var clientId = document.getElementById('tempclientid').value;
	
	//var date = document.getElementById('date').value;
	//var diaryUser = document.getElementById('diaryUserId').value;
	var condition = document.getElementById('treatmentType1').value;
	
	var treatmentEpisodeName = document.getElementById('treatmentEpisodeName').value;
	var referalDate = document.getElementById('referalDate').value;
	var referalendDate = document.getElementById('referalendDate').value;
	var referralName = document.getElementById('referralName').value;
	var referralSource = document.getElementById('referralSource').value;
	//var condition = document.getElementById('treatmentType').value;
	/*var referralContact = document.getElementById('referralContact').value;
	var referralLetter = document.getElementById('referralLetter').value;*/
	var payby = "";
	var invoicee1 = document.getElementById('invoicee').value;
	var authorisationCode = document.getElementById('authorisationCode').value;
	var spendLimit = document.getElementById('spendLimit').value;
	var approvedLimit = document.getElementById('approvedLimit').value;
	var consultationLimit = document.getElementById('consultationLimit').value;
	

	

	document.getElementById('authorisationCodeError').innerHTML = "";
	document.getElementById('treatmentTypeError').innerHTML = "";
	document.getElementById('consultationLimitError').innerHTML = "";
	document.getElementById('treatmentNameError').innerHTML ="";
	document.getElementById('invoiceeError').innerHTML ="";
	if(document.getElementById('payby1').checked){
		payby = "Client";
	}
	
	
	/*if (spendLimit ==  " ") {
      	var spendLimit = document.createElement("label");
      	spendLimit.innerHTML = "Enter Spend Limit";
        document.getElementById('spendLimitError').appendChild(spendLimit);
        chk=1;
     }  */
	/*if (condition == 0) {
      	var condition = document.createElement("label");
      	condition.innerHTML = "Select Condition";
        document.getElementById('treatmentTypeError').appendChild(condition);
        chk=1;
     } */
	if (consultationLimit == "" || consultationLimit == undefined) {
      	var consultationLimit = document.createElement("label");
      	consultationLimit.innerHTML = "Enter Consultation Limit";
        document.getElementById('consultationLimitError').appendChild(consultationLimit);
        chk=1;
     }  
	if (invoicee1 ==  "" || invoicee1 == null || invoicee1 == undefined) {
      	var consultationLimit = document.createElement("label");
      	consultationLimit.innerHTML = "Enter Invoice";
        document.getElementById('invoiceeError').appendChild(consultationLimit);
        chk=1;
     }  
	
   	if (treatmentEpisodeName == "") {
      	var treatmentEpisodeName = document.createElement("label");
      	treatmentEpisodeName.innerHTML = "Enter Name";
        document.getElementById('treatmentNameError').appendChild(treatmentEpisodeName);
        chk=1;
     }  
     
      if (referalDate == "") {
      	var treatmentEpisodeName = document.createElement("label");
      	treatmentEpisodeName.innerHTML = "Enter Referal From Date";
        document.getElementById('referalDateError').appendChild(treatmentEpisodeName);
        chk=1;
     }  
      
      if (referalendDate == "") {
      	var treatmentEpisodeName = document.createElement("label");
      	treatmentEpisodeName.innerHTML = "Enter Referal To Date";
        document.getElementById('referalendDateError').appendChild(treatmentEpisodeName);
        chk=1;
     }  
     
	
	
	if(document.getElementById('payby').checked){
		payby = "Third Party";
		if (authorisationCode ==  "") {
	      	var authorisationCode = document.createElement("label");
	      	authorisationCode.innerHTML = "Enter Code";
	        document.getElementById('authorisationCodeError').appendChild(authorisationCode);
	        chk=1;
	     }  
	}
	
   	
   	if(chk==1)
    {
       return false;
    }
    else
    {
     
    	
    var urgent = document.getElementById('urgent').checked;
    
    var ipdopd = '0';
	 if(document.getElementById('opd').checked==true){
		 ipdopd = '0';
	 }else{
		 ipdopd = '1';
	 }
	 
	// openIpdPopup('inputIpd');
    // var url = "saveTreatmentEpisode?treatmentEpisodeName="+treatmentEpisodeName+"&payby="+payby+"&invoicee="+invoicee1+"&authorisationCode="+authorisationCode+"&spendLimit="+spendLimit+"&consultationLimit="+consultationLimit+"&client="+editClientName+"&clientId="+patientId+"&date="+date+"&diaryUser="+diaryUser+"&treatmentType="+condition+"&referalDate="+referalDate+"&referralName="+referralName+"&referralSource="+referralSource+"&referalendDate="+referalendDate+" ";
     var url = "saveTreatmentEpisode?treatmentEpisodeName="+treatmentEpisodeName+"&payby="+payby+"&invoicee="+invoicee1+"&authorisationCode="+authorisationCode+"&spendLimit="+spendLimit+"&approvedLimit="+approvedLimit+"&consultationLimit="+consultationLimit+"&client="+clientname+"&clientId="+clientId+"&treatmentType="+condition+"&referalDate="+referalDate+"&referralName="+referralName+"&referralSource="+referralSource+"&urgent="+urgent+"&ipdopd="+ipdopd+"&referalendDate="+referalendDate+" ";
 
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = saveTreatmentRequest;
    req.open("GET", url, true); 
              
    req.send(null);
    }
	
	
}

function saveTreatmentRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
		
			
			 $( "#addTreatmentEpisodeDiv" ).modal( "hide" );
			tempAlert("Treatment Episode Added Successfully.",5000);

			document.getElementById('redirectToDashboard').submit();
			
		}
	}
}



function redirectToDashboard(){
	document.getElementById('redirectToDashboard').submit();
}


function getCurrentClientDetails(){
	 	var url = "getRecentlyAddedPatientDetailsClient";
	 
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = getCurrentClientDetailsRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	    
}

function getCurrentClientDetailsRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var data = req.responseText;
			
			
			var temp = data.split("#");
			var clientId = temp[0];			
			var name = temp[1];
			var condition = temp[2];
			var newwhopay = temp[3];
			var newPolicyNo = temp[4];
			var newTpname = temp[5];
		
			
			createTreatmentEpisode(clientId,name,condition,newPolicyNo,newwhopay,newTpname);
			
		}
	}
}

function openPopUP(){
	
	$('#tratementAddDiv').modal("show");
}


var oldid = 0;
var abrivation='';
var newname="";
var newage="";
var newgender="";
function setSelectedRows(id,uhid,newname1,newage1,newgender1){
	newname=newname1;
	newage=newage1;
	newgender=newgender1;
	$(document.getElementById(id)).css('background-color', 'rgb(229, 217, 129)');
	
	
	if(oldid!=0){
		$(document.getElementById(oldid)).css('background-color', 'white');
	}

	oldid = id; 
	abrivation=uhid;
	var url="setpatientnameClient?id="+id+"";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = setSelectedRowsRequest;
    req.open("GET", url, true); 
              
    req.send(null);
	
}


function setSelectedRowsRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			document.getElementById("pname").innerText=req.responseText;
			patientNameOrig=req.responseText;
			setSelectedClientSessionRecordAjax(oldid)
			$('#clickicon').modal("show");
		}
	}
}




function confirmedDelete(){

	var r=confirm("Are you sure you want to delete this entry");
	if (r==true)
	  {
	  return true;
	  }
	else
	  {
	  return false;
	  }

	}    	
	  



function showAllNOte(note){
	
	document.getElementById('cnote').value = note;
	$('#showallnotepopupdiv').modal("show");
	
	
}

 function showhideClientIpdDetails(id){
	if(document.getElementById('ipd'+id).style.display==''){
		document.getElementById('ipd'+id).style.display='none';
	}else{
		document.getElementById('ipd'+id).style.display='';
	}
}



function openaccount(){

   openPopup('Statement?clientId='+oldid+'');
   
}

function openprintletter(){
   openPopup('printLetterInstantMsg?clientId='+oldid+'');
}

function opendislayprofile(){

   openPopup('displayProfileClient?selectedid='+oldid+'');
}

function openemrhere() {

   openPopup('getPatientRecordEmr');
}

function opentreatmentpopup(){
   
   openPopup('showTreatmentEpisode?clientId='+oldid+'');
}

function openappointmentopopup() {
   openPopup('calNotAvailableSlot?actionType=display');
}

function openlogpopup() {

   openPopup('ClientLog?id='+oldid+'');
}

function openrecordpaymentpopup() {
   openPopup('ProcessingAccount?clientId='+oldid+'');
}

function openaccountpopup(){
   openPopup('Accounts?clientId='+oldid+'');
}
function openadvrefundpopup(){
   openPopup('creditAdditional?clientId='+oldid+'');
}

function openaddchargepopup(){
   openPopup('debitAdditional?clientId='+oldid+'');
}

function openClientDeclarationPrintPopup(){

	openClientPrintPopup(oldid);
}

function openimmunizationchartpopup(){
	   openPopup('immunizationchartNotAvailableSlot?clientId='+oldid+'');
	}
function opennewimmunizationchartpopup(){
	   openPopup('newimmunizationchartNotAvailableSlot?clientId='+oldid+'');
	}
function opennewimmunizationchartpopup1(){
	   openPopup('newimmunizationchartNotAvailableSlot?clientId='+oldid+'&type=1');
	}

function openheadcircumferencechartpopup(){
	   openPopup('headcircumferencechartClient?clientId='+oldid+'');
	}

function opennephrologyvacc(){
	 openPopup('newimmunizationchartNotAvailableSlot?clientId='+oldid+'&type=2');
}

function calculatebmi() {
	var weight = document.getElementById("input1").value;
	var height = document.getElementById("input2").value;
	if(weight==''){
		jAlert('error', "Please enter weight!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else if(height==''){
		jAlert('error', "Please enter height !", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else{
		var divider =  parseFloat(height)/100;
		var result = parseFloat(weight)/(divider*divider);
		document.getElementById("result").value=result;
	}
}

function saveResultBMI() {
	var weight = document.getElementById("input1").value;
	var height = document.getElementById("input2").value;
	if(weight==''){
		jAlert('error', "Please enter weight!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else if(height==''){
		jAlert('error', "Please enter height !", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else{
		var divider =  parseFloat(height)/100;
		var result = parseFloat(weight)/(divider*divider);
		document.getElementById("result").value=result;
	}
}

function calculatecentimeter(val) {
	var data = parseFloat(val) * 30.48;
	document.getElementById("cmids").value = data;
}

function openInvestigationdash(){
	
	openPopup("Investigation?uhid="+abrivation);
}

function openInvestigationdash1(){
	/*setPatientName1(newname,oldid,"",newage,newgender,"");*/
	setPatientName(newname,oldid,"",newage,newgender,"");
}

function editdisdate(){
	var clientid=oldid;
	
	$('#editadmndatemodal').modal( "show" );
}

function saaveadmndate(){
	var date= document.getElementById("editadmndate").value;
	var hr= document.getElementById("hour").value;
	var min= document.getElementById("minute").value;
	if(date==''){
		alert("Date Can't be Blank");
		return;
	}
	var url = "editadmndateIpd?clientid="+oldid+"&date="+date+"&hr="+hr+"&min="+min;
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = saaveadmndateRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}
function saaveadmndateRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			 window.location.reload();	

		}
	}
}
function setPatientName1(name,id,type,age,gender,dname) {
	
	
/*name="Lokesh/g";
id=oldid;
age="24";
gender="Male";
dname="Akaash/J";
*/
	
		
		var n=name.split("/");
		var newname=n[0]+" "+n[1]+" "+n[2]; 
		
		var d=dname.split("/");
		var doctorname = '';
		for(i=0;i<d.length;i++){
			doctorname = d[0]+" "+d[1]+" "+d[2]; 
		}
		
		var data="Name: "+newname+" | Age: "+age+"/ "+gender+"";
		
	    document.getElementById("invstcmyModalLabel").innerHTML=data;
	    document.getElementById("invstpriscdoctornameid").innerHTML = "Dr.";
	    /*document.getElementById("invstdatetimeid").innerHTML = document.getElementById("invstdateandtime").value;*/
	     
	    
		
		$('#investigationpopup').modal( "show" );

      /* $('#clientSearchDiv').modal( "hide" );	*/ 		
		
	}


function setAutochargeClient(clientid,flag){
	   if(flag==true){
		   flag="1";
	   } else {
		   flag="0";
	   }
	   if(flag==1){
		   var d=window.confirm("Are you sure you want to Start Auto Charge?"); 
		   if(d){
			   var url="switchautochargeStatement?clientid="+clientid+"&flag="+flag+"";
			   
			   if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = setAutochargeClientRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);
		   }else{
			   location.reload();
		   }
	  
	   }else{
		   var url="switchautochargeStatement?clientid="+clientid+"&flag="+flag+"";
		   
		   if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = setAutochargeClientRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);
	   }
}
function setAutochargeClientRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
		}
	}
	
}
	



function generatePatientbarcode(){
	var patientid=oldid;
	openPopup('patientbarcodeCommonnew?id='+patientid+"&abrivation="+abrivation+"&patientNameOrig="+patientNameOrig);
}
