function sendmail(){
	
	
	
	$( "#sendEmailPopUp2" ).modal( "show" );
}


function sendPdfMail(id){
	
	var cc = document.getElementById('ccEmail').value;
	var to = document.getElementById('thirdPartEmail').value;
	var subject = document.getElementById('subject').value;
	var notes = nicEditors.findEditor( "emailBody" ).getContent();
	var clientid =  document.getElementById('hiddenclientid').value;
	var type = "Invoice";
	
	
	 var url = "emailCharges?to="+to+"&cc="+cc+"&subject="+subject+"&notes="+notes+"&id="+id+"&clientid="+clientid+"&type="+type+" ";
	    if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendPdfMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}



function sendPdfMailJson() {
	var cc = document.getElementById('ccEmail').value;
	var to = document.getElementById('thirdPartEmail').value;
	var subject = document.getElementById('subject').value;
	var notes = nicEditors.findEditor( "emailBody" ).getContent();
	var clientid =  document.getElementById('hiddenclientid').value;
	var type = "Invoice";
	
	
	
	
	var SCRIPT_REGEX = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;
	
	
    var htmlContent=document.getElementById('ml').innerHTML;
    var c2=document.getElementById('ml1').innerHTML;
    var c3=document.getElementById('ml2').innerHTML;
    while (SCRIPT_REGEX.test(htmlContent)) {
    	htmlContent = htmlContent.replace(SCRIPT_REGEX, "");
	}
    while (SCRIPT_REGEX.test(c2)) {
    	c2 = c2.replace(SCRIPT_REGEX, "");
	}
    while (SCRIPT_REGEX.test(c3)) {
    	c3 = c3.replace(SCRIPT_REGEX, "");
	}
    htmlContent=htmlContent+c2+c3;
    var dataObj= {
			"cc":""+cc+"", 
			"to":""+to+"", 
			"subject":""+subject+"", 
			"notes":""+notes+"", 
			"clientid":""+clientid+"", 
			"type":""+type+"", 
			"htmlContent":""+htmlContent+"",
			"id":"",
	 };
	 var data1 =  JSON.stringify(dataObj);
	 $.ajax({

	  url : "emailCommonnew",
	  data : data1,
	  dataType : 'json',
	  contentType : 'application/json',
	  type : 'POST',
	  async : true,
	  // beforeSend: function () { LockScreen(); },
	  success : function(data) {
		  alert("Sent");
	    },
	  error : function(result) {
	   console.log("error");
	  }
	 });

}






function sendPdfMailRequest(){
	
		if (req.readyState == 4) {
				if (req.status == 200) {
					
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
					
					
		            jAlert('success', 'Email Send Sucessfully!!', 'Success Dialog');
		            //tempAlert("Email Send Sucessfully", 5000);
		            $('#sendEmailPopUp').dialog( "close" );
				}
		}
	
}

function showinvoicechargesByDate(billsummary,invoiceid,totalAmountx,seq) {
	  var dataObj={
			  	"billsummary" : "" + billsummary + "",
			    "invoiceid" : "" + invoiceid + "",
			    "totalAmountx" : "" + totalAmountx + "",
			    "seq" : "" + seq + "",
	  };
	  var data1 =  JSON.stringify(dataObj);
	  $.ajax({

	   url : "getinvoicechargeslistBookAppointmentAjax",
	   data : data1,
	   dataType : 'json',
	   contentType : 'application/json',
	   type : 'POST',
	   async : true,
	   success : function(data) {
		   var condition= data.response1;
		   document.getElementById("viewinvoicetbody").innerHTML = data.response1;
	   },
	   error : function(result) {
		   jAlert('error', "Not Reloaded!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	   }

	  });
}