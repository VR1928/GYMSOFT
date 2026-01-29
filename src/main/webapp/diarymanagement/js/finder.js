var tmentepisodeid = 0;
var tempclientid = 0;
function modifyAppointment(date,diaryuser,starttime,duration,apmtType,notes,endtime,location,treatmentepisodeid,id,apmtSlotId,diaryuserid,clientid,condition){
	
	$('#appointment').modal( "show" );
	
	document.getElementById('user').value = diaryuser;
	document.getElementById('client').value = document.getElementById('findclient').value;
	document.getElementById('location').value = location;
	document.getElementById('date').value = date;
	document.getElementById('sTime').value = starttime;
	document.getElementById('endTime').value = endtime;
	document.getElementById('apmtDuration').value = duration;
	document.getElementById('apmtType').value = apmtType;
	$("#apmtType").trigger("chosen:updated");
	$(".chosen").chosen({allow_single_deselect: true});
	document.getElementById('condition').value = condition;
	$("#condition").trigger("chosen:updated");
	$(".chosen").chosen({allow_single_deselect: true});
	document.getElementById('notes').value = notes;
	
	var cid = clientid;
	
	
	tmentepisodeid = treatmentepisodeid;
	
	editStartTime = starttime;
	editAppointId = id;
	document.getElementById('slotId').value = apmtSlotId;
	document.getElementById('diaryUserId').value = diaryuserid;
	document.getElementById('clientId').value =clientid;
	
	
	
	findTreatmentEpisode(cid);
	
	
	
}


function findTreatmentEpisode(clientid){
	
	//var clientid = read_cookie("cookieClientId");
	
	var url = "setTreatmentEpisode?clientid="+clientid+" ";
   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = findTreatmentEpisodeRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function findTreatmentEpisodeRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById('treatmentepisodeajax').innerHTML = req.responseText;
			document.getElementById('treatmentEpisode').value = tmentepisodeid;
		}
	}
}


function showPopUp(){
	
	var url = "Client";

	$('#clientSearchDiv').modal( "show" );
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
				
	         	
				
	         }
		}
	}
	
	
/*function setPatientName(name,id,type,typeName){
	
	document.getElementById("findclient").value = name;
	document.getElementById("client").value = name;
	document.getElementById("clientId").value = id;
	getClientFullName(id);

	
	$('#clientSearchDiv').modal( "hide" );	
	
	
        
		
}*/

var finderapmtid = 0;


function openFinderCancelApmtPopup(id){
	finderapmtid = id;
	
	 $( "#cancelApmtNoteDiv" ).modal( "show" );
	 
}

function deleteFinderNotAviSlot(){
	if(document.getElementById('cancelApmtNote').value==''){
		jAlert('error', 'Please enter cancel appointment note.', 'Error Dialog');
		
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		
	}else{
		document.getElementById("id").value = finderapmtid;
		document.getElementById("cancelfinderApmtNote").value =  document.getElementById("cancelApmtNote").value;
		document.getElementById("finderfrm").submit();
		$( "#cancelApmtNoteDiv" ).modal( "hide" );
		
	}
	
	
}
