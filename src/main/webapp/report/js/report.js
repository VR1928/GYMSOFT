function addItem()
    {	 
    	
        var selIndexes = "";
        var selectTag = document.getElementById("list2");
        for (var i = 0; i < selectTag.options.length; i++) {
            var optionTag = selectTag.options[i];
            if (optionTag.selected) {
                if (selIndexes.length > 0)
                    selIndexes += ", ";
                selIndexes += optionTag.text;
                var opt = document.createElement("option");
                document.getElementById("list1").options.add(opt);
                opt.text = optionTag.text;
                opt.value = optionTag.text;
                

               }
           
            
        }
        var src = document.getElementById("list2");
        
        //iterate through each option of the listbox
        for(var count= src.options.length-1; count >= 0; count--) {
     
             //if the option is selected, delete the option
            if(src.options[count].selected == true) {
      
                    try {
                             src.remove(count, null);
                             
                     } catch(error) {
                             
                             src.remove(count);
                    }
            }
        }
        
}

function removeItem(){
	var selIndexes = "";
    var selectTag = document.getElementById("list1");
    for (var i = 0; i < selectTag.options.length; i++) {
        var optionTag = selectTag.options[i];
        if (optionTag.selected) {
            if (selIndexes.length > 0)
                selIndexes += ", ";
            selIndexes += optionTag.text;
            var opt = document.createElement("option");
            document.getElementById("list2").options.add(opt);
            opt.text = optionTag.text;
            opt.value = optionTag.text;
            

           }
       
        
    }
    
	var src = document.getElementById("list1");
    
    //iterate through each option of the listbox
    for(var count= src.options.length-1; count >= 0; count--) {
 
         //if the option is selected, delete the option
        if(src.options[count].selected == true) {
  
                try {
                         src.remove(count, null);
                         
                 } catch(error) {
                         
                         src.remove(count);
                }
        }
    }
    
	 
     
}

function listbox_move(listID, direction) {
	 
    var listbox = document.getElementById(listID);
    var selIndex = listbox.selectedIndex;
 
    if(-1 == selIndex) {
        alert("Please select an option to move.");
        return;
    }
 
    var increment = -1;
    if(direction == 'up')
        increment = -1;
    else
        increment = 1;
 
    if((selIndex + increment) < 0 ||
        (selIndex + increment) > (listbox.options.length-1)) {
        return;
    }
 
    var selValue = listbox.options[selIndex].value;
    var selText = listbox.options[selIndex].text;
    listbox.options[selIndex].value = listbox.options[selIndex + increment].value;
    listbox.options[selIndex].text = listbox.options[selIndex + increment].text;
 
    listbox.options[selIndex + increment].value = selValue;
    listbox.options[selIndex + increment].text = selText;
 
    listbox.selectedIndex = selIndex + increment;
}

function showColumns(){
	document.getElementById('report_frm').submit();
	
}

var danAnalysisfilename = '';

function  dnaAnalysysPreview(){
//	$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
	var diaryUser = document.getElementById('diaryUser').value;			
	
	var url = "previewReportSummary?diaryUser="+diaryUser+"";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = dnaAnalysysPreviewRequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function dnaAnalysysPreviewRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
		//	$(document).ajaxStop($.unblockUI);
			
			danAnalysisfilename = req.responseText;
		
			/*document.getElementById('dnaAnalysysPreviewId').innerHTML = '<a href = liveData/DNAAnalysysReport/'+danAnalysisfilename+' target="blank" class="btn btn-primary">Preview</a>';*/
			
			document.getElementById('sendMailDNAAnalysysId').innerHTML =  '<button type="button" id="sendMailDNAAnalysisReportId" class="btn btn-primary" onclick="return sendEmailDNAAnalysis();">Send Mail</button>';
			
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'DNA Analysis Report');
         }
	}

}

function sendEmailDNAAnalysis(){
	
	document.getElementById('DNAAnalysisReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/DNAAnalysysReport/'+danAnalysisfilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="dapdf" checked="checked"> </h4>';
	
	$('#sendEmaildnaAnalysisPopup').modal( "show" );
	
}

function senddnaAnalysisReportMail(){
	
	$('#sendEmaildnaAnalysisPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	var to = document.getElementById('danAnalysisEmail').value;
	var cc = document.getElementById('danAnalysisccEmail').value;
	var subject = document.getElementById('danAnalysissubject').value;
	var emailBody = document.getElementById('danAnalysisEmailBody').value;		
	var file = document.getElementById('dapdf').value;
	
	//alert(emailBody);
	var url = "sendEmailDNAAnalysisSummary?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = senddnaAnalysisReportMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function senddnaAnalysisReportMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmaildnaAnalysisPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmaildnaAnalysisPopup').modal( "hide" );
				
	         }
		}
	
}

var appVsDnafilename = '';

function previewAppVsDNA(){
	
//	$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
	var fromDate = document.getElementById('fromDate').value;		
	var toDate = document.getElementById('toDate').value;	
	
	var url = "previewAPPTVsDnaReportSummary?fromDate="+fromDate+"&toDate="+toDate+"";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = previewAppVsDNARequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function previewAppVsDNARequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
						
		//	$(document).ajaxStop($.unblockUI);
			appVsDnafilename = req.responseText;
		
			/*document.getElementById('previewAppVsDNAId').innerHTML = '<a href = liveData/ApptVsDNAReport/'+appVsDnafilename+' target="blank" class="btn btn-primary">Preview</a>';*/
			
			document.getElementById('sendMailAppVsDnaId').innerHTML = '<button type="button" id="sendMaildnaVsAppReportId" class="btn btn-primary" onclick="return sendEmailAppVsDna();">Send Mail</button>';
			
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'Appointment Kept Vs DNA Report');
			
         }
	}

}

function sendEmailAppVsDna(){
	
	document.getElementById('appVsDnaReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/ApptVsDNAReport/'+appVsDnafilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="avdpdf" checked="checked"> </h4>';
	
	$('#sendEmailAppVsDnaPopup').modal( "show" );
	
}

function sendAppVsDnaReportMail(){
	
	$('#sendEmailAppVsDnaPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	var to = document.getElementById('appVsDnaEmail').value;
	var cc = document.getElementById('appVsDnaccEmail').value;
	var subject = document.getElementById('appVsDnasubject').value;
	var emailBody = document.getElementById('appVsDnaEmailBody').value;		
	var file = document.getElementById('avdpdf').value;
	
	//alert(emailBody);
	var url = "sendEmailappVsDnaSummary?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendAppVsDnaReportMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function sendAppVsDnaReportMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmailAppVsDnaPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmailAppVsDnaPopup').modal( "hide" );
				
	         }
		}
	
}

var referalTreatfilename = '';

function previewReferalTreatment(){
	
//	$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
	
	var referal = document.getElementById('referal').value;			
	
	var url = "previewReferalTreatmentReportSummary?referal="+referal+"";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = previewReferalTreatmentRequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function previewReferalTreatmentRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			
		//	$(document).ajaxStop($.unblockUI);
			referalTreatfilename = req.responseText;
		
			/*document.getElementById('previewReferalTreatmentId').innerHTML = '<a href = liveData/ReferalTreatmentReport/'+referalTreatfilename+' target="blank" class="btn btn-primary">Preview</a>';*/
		
			document.getElementById('sendMailreferalTreatmentId').innerHTML = '<button type="button" id="sendMailreferalReportId" class="btn btn-primary" onclick="return sendEmailReferalTreat();">Send Mail</button>';
			
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'Treatment States By Referal Report');
			
         }
	}

}

function sendEmailReferalTreat(){
	
	document.getElementById('referalReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/ReferalTreatmentReport/'+referalTreatfilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="rtpdf" checked="checked"> </h4>';
	
	$('#sendEmailReferalPopup').modal( "show" );
}

function sendReferalReportMail(){
	$('#sendEmailReferalPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	
	var to = document.getElementById('referalEmail').value;
	var cc = document.getElementById('referalccEmail').value;
	var subject = document.getElementById('referalsubject').value;
	var emailBody = document.getElementById('referalEmailBody').value;		
	var file = document.getElementById('rtpdf').value;
	
	//alert(emailBody);
	var url = "sendEmailreferalSummary?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendReferalReportMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function sendReferalReportMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmailReferalPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmailReferalPopup').modal( "hide" );
				
	         }
		}
	
}



var noApptActivityFilename = "";

function previewNoApptActivity(){
	
	//$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
	var url = "previewNoApptActivityClientRpt";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = previewNoApptActivityRequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function previewNoApptActivityRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			
			//$(document).ajaxStop($.unblockUI);
			noApptActivityFilename = req.responseText;
		
			/*document.getElementById('previewNoApptActivityId').innerHTML = '<a href = liveData/NoApptActivityReport/'+noApptActivityFilename+' target="blank" class="btn btn-primary">Preview</a>';*/
						
			document.getElementById('sendMailNoApptActivityId').innerHTML = '<button type="button" id="sendMailNoApptActivityReportId" class="btn btn-primary" onclick="return sendEmailNoApptActivity();">Send Mail</button>';
			
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'No Appointment Activity Report');
			
         }
	}

}

function sendEmailNoApptActivity(){
	
	document.getElementById('noApptActivityReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/NoApptActivityReport/'+noApptActivityFilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="naapdf" checked="checked"> </h4>';
	
	$('#sendEmailNoApptActivityPopup').modal( "show" );
}

function sendNoApptActivityMail(){
	$('#sendEmailNoApptActivityPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	
	var to = document.getElementById('noApptActivityEmail').value;
	var cc = document.getElementById('noApptActivityccEmail').value;
	var subject = document.getElementById('noApptActivitysubject').value;
	var emailBody = document.getElementById('noApptActivityEmailBody').value;		
	var file = document.getElementById('naapdf').value;
	
	//alert(emailBody);
	var url = "sendEmailNoApptActivityClientRpt?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendNoApptActivityMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function sendNoApptActivityMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmailNoApptActivityPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmailNoApptActivityPopup').modal( "hide" );
				
	         }
		}
	
}

var dnaNoFuturefilename = "";

function dnaNoFutureApptPreview(){
	
//$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
var url = "dnaNoFutureApptPreviewClientRpt";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = dnaNoFutureApptPreviewRequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function dnaNoFutureApptPreviewRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			
			//$(document).ajaxStop($.unblockUI);
			dnaNoFuturefilename = req.responseText;
		
			/*document.getElementById('dnaNoFutureApptPreviewId').innerHTML = '<a href = liveData/dnaNoFutureApptReport/'+dnaNoFuturefilename+' target="blank" class="btn btn-primary">Preview</a>';*/
			
			document.getElementById('sendMaildnaNoFutureApptId').innerHTML = '<button type="button" id="sendMaildnaNoFutureApptReportId" class="btn btn-primary" onclick="return sendEmaildnaNoFutureAppt();">Send Mail</button>';
						
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'DNA With No Future Appointment Report');
			
         }
	}

}

function sendEmaildnaNoFutureAppt(){
	
	document.getElementById('dnaNoFutureReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/dnaNoFutureApptReport/'+dnaNoFuturefilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="dnfapdf" checked="checked"> </h4>';
	
	$('#sendEmaildnaNoFutureapptPopup').modal( "show" );
}

function senddnaNoFutureApptMail(){
	$('#sendEmaildnaNoFutureapptPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	
	var to = document.getElementById('dnaNoFutureEmail').value;
	var cc = document.getElementById('dnaNoFutureccEmail').value;
	var subject = document.getElementById('dnaNoFuturesubject').value;
	var emailBody = document.getElementById('dnaNoFutureEmailBody').value;		
	var file = document.getElementById('dnfapdf').value;
	
	//alert(emailBody);
	var url = "sendEmaildnaNoFutureApptClientRpt?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = senddnaNoFutureApptMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function senddnaNoFutureApptMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmaildnaNoFutureapptPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmaildnaNoFutureapptPopup').modal( "hide" );
				
	         }
		}
	
}

var noActivityFilename = '';

function previewNoActivity(){
	
//$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
var url = "previewNoActivityClientRpt";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = previewNoActivityRequest;
    req.open("GET", url, true); 
              
    req.send(null);

}
function previewNoActivityRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			
		//	$(document).ajaxStop($.unblockUI);
			noActivityFilename = req.responseText;
		
			document.getElementById('previewNoActivityId').innerHTML = '<a href = liveData/NoActivityReport/'+noActivityFilename+' target="blank" class="btn btn-primary">Preview</a>';
						
			document.getElementById('sendMailNoActivityId').innerHTML = '<button type="button" id="sendMailNoActivityReportId" class="btn btn-primary" onclick="return sendEmailNoActivity();">Send Mail</button>';
			
			//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
			jAlert('success', 'PDF Created successfully!!', 'No Activity Report');
			
         }
	}

}

function sendEmailNoActivity(){
	
	document.getElementById('noActivityReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/NoActivityReport/'+noApptActivityFilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="napdf" checked="checked"> </h4>';
	
	$('#sendEmailNoActivityPopup').modal( "show" );
}


function sendNoActivityMail(){
	$('#sendEmailNoActivityPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
	
	var to = document.getElementById('noActivityEmail').value;
	var cc = document.getElementById('noActivityccEmail').value;
	var subject = document.getElementById('noActivitysubject').value;
	var emailBody = document.getElementById('noActivityEmailBody').value;		
	var file = document.getElementById('napdf').value;
	
	//alert(emailBody);
	var url = "sendEmailNoActivityClientRpt?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendNoActivityMailRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function sendNoActivityMailRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			
				$('#sendEmailNoActivityPopup').unblock();
				showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
				$('#sendEmailNoActivityPopup').modal( "hide" );
				
	         }
		}
	
}

var treatmentfilename = "";
function treatmentEpisodePreview(){
	
	//$.blockUI({ message: '<h3><img src="common/images/loader1.GIF" /> Just a moment...</h3>' });
	var url = "treatmentEpisodePreviewClinical";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = treatmentEpisodePreviewRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function treatmentEpisodePreviewRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				
				//$(document).ajaxStop($.unblockUI);
				treatmentfilename = req.responseText;
			
				/*document.getElementById('treatmentPreviewId').innerHTML = '<label></label><a href = liveData/TreatmentEpisodeReport/'+treatmentfilename+' target="blank" class="btn btn-primary">Preview</a>';*/
				
				document.getElementById('sendMailTreatmentId').innerHTML = '<label></label><button type="button" id="sendMailTreatmentReportId" class="btn btn-primary" onclick="return sendEmailTreatment();">Send Mail</button>';
							
				//showGrowl('', 'PDF file is Created Successfully !!', 'success', 'fa fa-check'); 
				jAlert('success', 'PDF Created successfully!!', 'Treatment Episode List');
				
	         }
		}
}
	
	function  sendEmailTreatment(){
		
		document.getElementById('treatmentReportMailId').innerHTML = '<h4> PDF File  : <span style="margin-left:3px;"><a href = liveData/TreatmentEpisodeReport/'+treatmentfilename+' target=blank> <i class="fa fa-file-pdf-o fa-2x text-danger"></i></a></span> &nbsp; <input type="checkbox" name="ltrpdf" id="treatmentpdf" checked="checked"> </h4>';
		
		$('#sendEmailTreatmentPopup').modal( "show" );
	}
	
	function sendTreatmentReportMail(){
		
		$('#sendEmailTreatmentPopup').block({ message: '<h3><img src="common/images/loader1.GIF" /> Sending Mail, Just a moment...</h3>' }); 
		
		var to = document.getElementById('treatmentEmail').value;
		var cc = document.getElementById('treatmentccEmail').value;
		var subject = document.getElementById('treatmentsubject').value;
		var emailBody = document.getElementById('treatmentEmailBody').value;		
		var file = document.getElementById('treatmentpdf').value;
		
		//alert(emailBody);
		var url = "sendEmailTreatmentEpisodeClinical?to="+to+"&subject="+subject+"&emailBody="+emailBody+"&cc="+cc+"&file="+file+"";

		if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = sendTreatmentReportMailRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);

		}
	function sendTreatmentReportMailRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
				
					$('#sendEmailTreatmentPopup').unblock();
					showGrowl('', 'Mail Send Successfully !!', 'success', 'fa fa-check');
					$('#sendEmailTreatmentPopup').modal( "hide" );
					
		         }
			}
		
	}

	function selectKPIArea(){
		document.getElementById("kpidashboardform").submit();
	}
	
	function selectAllIndicator(val){
		  if(val.checked==true){
			   
			   $('.akash').each(function() {
				   
				     this.checked=true;
				    
			   });
			   
		   } else {
			   $('.akash').each(function() {
				   
				     this.checked=false;
				    
			   });
			   
			   
		   }
	}
	
	function saveKPIIndicator(){
		var ids="0";
		$('.akash').each(function() { 
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
		});
		if(ids=='0'){
			jAlert('error', "Please select at least one indicator!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
		}else{
			document.getElementById("allindicatorid").value = ids;
			document.getElementById("indicatorform").submit();
		}
		   
	}
	
	var val1 ="";
function disselectIndicatorCheckbox(val,indicator){
		val1 = val;
		 var t=confirm("Do you Want to Deselect?");
		    if(t==true){
		    	var url = "updateindicatormasterKPI?val="+val+"&indicator="+indicator+"";
				if (window.XMLHttpRequest) {
						req = new XMLHttpRequest();
					}
					else if (window.ActiveXObject) {
						isIE = true;
						req = new ActiveXObject("Microsoft.XMLHTTP");
					}   
				               
				    req.onreadystatechange = disselectIndicatorCheckboxRequest;
				    req.open("GET", url, true); 
				              
				    req.send(null);

		    } else {
		       document.getElementById("checki"+val).checked = true;
		    }
		
		}
	function disselectIndicatorCheckboxRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
					document.getElementById("kpitd"+val1).innerHTML = req.responseText;
		         }
			}
		
	}
	
	
	function saveIndicator(){
		var areaid = document.getElementById("areaid").value;
		var indicator = document.getElementById("indicator").value;
		var formula_desc = document.getElementById("formula_desc").value;
		var formula = document.getElementById("formula").value;
		var no_of_input = document.getElementById("no_of_input").value;
		
	  	 if(areaid=='0'){
	      	jAlert('error', "please select area!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 	
				 return false;	
	    
	      	}
	      	else if(indicator==''){
	      	jAlert('error', "please enter indicator!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 	
				 return false;	
	      	}
	      	else if(formula_desc==''){
		      	jAlert('error', "please enter formula description!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 	
					 return false;	
		      	}
	      	else if(formula==''){
		      	jAlert('error', "please enter formula!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 	
					 return false;	
		      	}
	      	else if(no_of_input==''){
		      	jAlert('error', "please enter number of input!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 	
					 return false;	
		      	}
	  	 
	      	else{
	      		return true;
	      	}

	}
	
	function setNabhSubcatagory(id) {
		var url="getnabhsubcatagoryKPI?id="+id+"";
	     if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		 }
		 else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		 }   
	     req.onreadystatechange = setNabhSubcatagoryRequest;
	     req.open("GET", url, true); 
	     req.send(null);   
	 }
	 
	 
	function setNabhSubcatagoryRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
		          document.getElementById("nabhsubcatdiv").innerHTML=req.responseText;
		           $("#nabhsubcatalistid").trigger("chosen:updated");
				   $(".chosen").chosen({allow_single_deselect: true});
	         }
		}	 
	}
	
	function submitmainform() {
		var todate = document.getElementById("todate").value;
		var fromdate = document.getElementById("fromdate").value;
		if(todate==''){
			jAlert('error', "please enter todate!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(fromdate=''){
			jAlert('error', "please enter fromdate!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else{
			var vari = dateDiff(fromdate, todate);
		}
	}
	
	function dateDiff(d1str, d2str) {
	    var d1 = new Date(d1str),
	        d2 = new Date(d2str);
	    return (d2.getTime() - d1.getTime()) / 1000 / 60 / 60 / 24 // diff in days
	}
	
	function count(){
		
		var x= document.getElementById("count").innerHTML;
		document.getElementById("printcount").innerHTML = "Count : "+x;
	}
	
	
	/*function give_expirey_date(clinicid,value){
		var url = "giveexpirydateUserAdministration?clinicid="+clinicid+"&value="+value+"";
		$('#baselayout1loaderPopup').modal( "show" );
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = give_expirey_dateRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function give_expirey_dateRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}*/
	
	
	
	
	function deactivateClinic(ip,active){
		var url = "active_deactive_clinicUserAdministration?ip="+ip+"&active="+active+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = deactivateClinicRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function deactivateClinicRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}
	
	
	
	function submitmis(){
		var fromdate = document.getElementById("fromDate").value;
		var todate = document.getElementById("toDate").value;
		if(fromdate==''){
			jAlert('error', "Please enter from date!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			return false;
		}else if(todate==''){
			jAlert('error', "Please enter to date!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			return false;
		}else{
			//parts[1] + "/" + parts[0] + "/" + parts[2]
			var parts = fromdate.split("-");
			fromdate = parts[1] + "/" + parts[0] + "/" + parts[2];
			
			var parts1 = todate.split("-");
			todate = parts1[1] + "/" + parts1[0] + "/" + parts1[2];
			
			var date1 = new Date(fromdate);
			var date2 = new Date(todate);
			var timeDiff = Math.abs(date2.getTime() - date1.getTime());
			var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
			if(diffDays>=32){
				jAlert('error', "Date differnce is greater one month!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				return false;
			}else{
				$('#baselayout1loaderPopup').modal( "show" );
				document.getElementById("mischartfrm").submit();
				return true;
			}
			
		}
		
	}

	
	
	$(document).ready(function() {
	     $(function() {
	         $("#search").autocomplete({     
	             source : function(request, response) {
	               $.ajax({
	                    url : "searchmedMaster",
	                    type : "GET",
	                    data : {
	                           term : request.term
	                    },
	                    dataType : "json",
	                    success : function(data) {
	                          response(data);
	                    }
	             });
	          }
	      });
	   });
	});
	
	
	function give_expirey_date(clinicid,value,type){
		var url = "giveexpirydateUserAdministration?clinicid="+clinicid+"&value="+value+"&type="+type;
		$('#baselayout1loaderPopup').modal( "show" );
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = give_expirey_dateRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function give_expirey_dateRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}
	
	
	
	
	function deactivateClinic(ip,active){
		var url = "active_deactive_clinicUserAdministration?ip="+ip+"&active="+active+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = deactivateClinicRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function deactivateClinicRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}
	
	
	function setSupportReqStatus(status){
		
		var ticketid=document.getElementById("ticketid").value;
		
var url = "setsupportreqstatusSupport?ticketid="+ticketid+"&status="+status+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = setSupportReqStatusRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function  setSupportReqStatusRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				/*window.location.reload();*/
				}
			}
	}
	
function setSupportReqExecutive(name){
		
		var ticketid=document.getElementById("ticketid").value;
		
var url = "setsupportreqexecutiveSupport?ticketid="+ticketid+"&name="+name+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = setSupportReqExecutiveRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function  setSupportReqExecutiveRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				/*window.location.reload();*/
				}
			}
	}

function setsupportremark(){
		
		var ticketid=document.getElementById("ticketid").value;
		var remark=document.getElementById("remark").value;
		var priority=document.getElementById("priority").value;
		var url = "setsupportremarkSupport?ticketid="+ticketid+"&remark="+remark+"&priority="+priority;
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = setsupportremarkRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
	function  setsupportremarkRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}
	
	
var tick='';
var whoglo='';
function sendsupportmsg(who,ticketid){
		tick=ticketid;
		whoglo=who;
		var msg=document.getElementById("msg"+ticketid).value;
		
		var url = "sendmsgSupport?ticketid="+ticketid+"&msg="+msg+"&who="+who+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = sendsupportmsgRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}
	
function sendsupportmsgRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			loadsupportconversation(tick,whoglo);
			}
		}
}
	

function createticket(){

	$('#ticketadd').modal( "show" );
}

function  getuserlistsupport(value){
	var url = "getalluserlistSupport?clinicid="+value;
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = getuserlistsupportReq;
    req.open("GET", url, true); 
              
    req.send(null);

}
	
	
function getuserlistsupportReq(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById("userlist").innerHTML="<label>Clinic Users</label>"+req.responseText;
			$("#useres").trigger("chosen:updated");
			  $(".chosen-select").chosen({allow_single_deselect: true});
			}
		}

}



function getusermobile(obj){
	var value=obj.value;
	var str= value.split("(");
	var text=str[1].replace(')','');
	document.getElementById("mbl").value=text;

	
	
}


function submitticketgenform(){
	var clinicids=document.getElementById("clinicids").value;
	if(document.getElementById("clinicids").value==''){
		document.getElementById("error").innerHTML="Select Clinic First !";
		return false;
	}
	var tu=document.getElementById("useres").value;
	if(document.getElementById("useres").value==''||document.getElementById("useres").value=='()'){
		document.getElementById("error").innerHTML="Select User First !";
		return false;
	}
	
	var qt=document.getElementById("query_type").value;
	if(document.getElementById("query_type").value=='0'){
		document.getElementById("error").innerHTML="Select Query Type First !";
		return false;
	}
	var q=document.getElementById("query").value;
	if(document.getElementById("query").value==''){
		document.getElementById("error").innerHTML="Enter Query  First !";
		return false;
	}
	if(document.getElementById("query").value==''){
		document.getElementById("error").innerHTML="Enter Query  First !";
		return false;
	}
	else{
		$('#baselayout1loaderPopup').modal( "show" );
		document.getElementById("ticketgenmodalform").submit();
	}
}



function sethospitalstatus(clinicid,status){
var url = "active_deactive_clinicUserAdministration?clinicid="+clinicid+"&status="+status;

$('#baselayout1loaderPopup').modal( "show" );
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = sethospitalstatusReq;
    req.open("GET", url, true); 
              
    req.send(null);
}

function sethospitalstatusReq(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			window.location.reload();
			}
		}
}



function setpymentreportstatus(status){
	var id=document.getElementById('statusid').value
	var url = "setpaymentreportnotesstatusChargesRpt?id="+id+"&status="+status;

	$('#baselayout1loaderPopup').modal( "show" );
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = setpymentreportstatusReq;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	}

	function setpymentreportstatusReq(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
				}
			}
	}
	
	
	function getconversationall(id,type){
		var x=document.getElementById('newrow'+id).className;
		if(x=='hidden'){
			document.getElementById('newrow'+id).className='';
			document.getElementById('newcol'+id).innerHTML='';
		}else{
			document.getElementById('newrow'+id).className='hidden';
		}
		loadsupportconversation(id,type);
	}
	
	
	
	function loadsupportconversation(ticket,type){
		
		 var dataObj= {
				"ticketid":""+ticket+"", 
				"type":""+type+"", 
		 };
		 var data1 =  JSON.stringify(dataObj);
		 $.ajax({

		  url : "loadsupportconversationSupport",
		  data : data1,
		  dataType : 'json',
		  contentType : 'application/json',
		  type : 'POST',
		  async : true,
		  // beforeSend: function () { LockScreen(); },
		  success : function(data) {
			
			  document.getElementById('newcol'+ticket).innerHTML=data.datax;
			  
		    },
		  error : function(result) {
		   console.log("error");
		  }
		 });
	}
	
	
	
	function submitl(id){
		$('#baselayout1loaderPopup').modal( "show" );
		document.getElementById('ppp'+id).innerHTML="<input type='hidden' name='ticketid' value='"+id+"' >";
		document.getElementById('form'+id).submit();
	
		
	}