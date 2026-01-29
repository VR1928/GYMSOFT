/**
 * 
 */
function getlist(type){
	
	document.getElementById("feedbackform").submit();	
		

	
	
}


function getPatient(id){
	document.getElementById("patient").value= id;
var x=document.getElementById("treatmenttype").value;
document.getElementById("treatmenttype1").value= x;
}
 


function checkifChecked(){
	var x=document.getElementById("patientlist").value;
	if(x==0){
		alert("Select Patient");
		return false;
	}
	else{
		return true;
	}
	
}

function allfeedbackpatients(){
	var type=document.getElementById("treatmenttype").value;
	var url="getallpatientlistForfeedbackClient?type="+type+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
         
	
	
    req.onreadystatechange = allfeedbackpatientsRequest;
    req.open("GET", url, true); 
              
    req.send(null)
	
}


function allfeedbackpatientsRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var txt = req.responseText;
			
			document.getElementById("allPatient").innerHTML=txt;
			
		}
	}
}