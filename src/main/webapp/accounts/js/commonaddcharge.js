function createestimate(){
 document.getElementById("estimateclientid").value = ipdclientid;	

				var t = 'formtarget';
				var left = (screen.width / 2) - (700 / 2);
				var top = (screen.height / 2) - (550 / 2);
				var oldwindow = window.open("", t,
						"status = 1,height = "+openpopupheight +",width = "+openpopupwidth +",resizable = 1,scrollbars=yes,left=" + 0
								+ ",top=" + 50);
				
				oldwindow.focus();
 
 
 
 document.getElementById("estimatefrm").submit();
 
}


function createdebitestimate(){
	document.getElementById("estimateclientid").value = document.getElementById('clientId').value;

				var t = 'formtarget';
				var left = (screen.width / 2) - (700 / 2);
				var top = (screen.height / 2) - (550 / 2);
				var oldwindow = window.open("", t,
						"status = 1,height = "+openpopupheight +",width = "+openpopupwidth +",resizable = 1,scrollbars=yes,left=" + 0
								+ ",top=" + 50);
				
				oldwindow.focus();
 
 
 
 document.getElementById("estimatefrm").submit();
}


function createopdestimate(){
	document.getElementById("estimateclientid").value = patientId;

				var t = 'formtarget';
				var left = (screen.width / 2) - (700 / 2);
				var top = (screen.height / 2) - (550 / 2);
				var oldwindow = window.open("", t,
						"status = 1,height = "+openpopupheight +",width = "+openpopupwidth +",resizable = 1,scrollbars=yes,left=" + 0
								+ ",top=" + 50);
				
				oldwindow.focus();
 
 
 
 document.getElementById("estimatefrm").submit();
}