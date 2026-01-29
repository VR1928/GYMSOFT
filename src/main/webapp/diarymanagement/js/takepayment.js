function ShowChashPaymentPopup(){
$( "#takepaymentmodel" ).modal( "show" );
}

function setAmountDue(){
	var totalAmount = document.getElementById('totalamount').value;
	var disctype = document.getElementById('disctype').value;
	var discount = document.getElementById('discount').value;
	var discountAmt = totalAmount * (discount/100);
	if(disctype==1){
	 discountAmt = discount;
	}
	var amountDue = totalAmount - discountAmt;
	
	document.getElementById('payAmount').value = amountDue;
}

var totalamt1 = 0;

function saveCashDesk(){

	var t1 = document.getElementById('payAmount').value;
	// totalamt1 = document.getElementById('hdntotal').value;
	 if(document.getElementById('howpaid').value==0){
		
		jAlert('error', 'Please select payment mode.', 'Error Dialog');
		
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		
	}else if( document.getElementById('howpaid').value=='prepayment' && document.getElementById('hiddenbalence').value=='false'){
		jAlert('error', 'No credit balence. Please select other payment mode ', 'Error Dialog');
		
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	
	}else if(parseFloat(t1) > parseFloat(totalamt1)){
		//jAlert('error', 'Paid amt is greater than balance Amount.', 'Error Dialog');
		document.getElementById('crdpaidamt').innerHTML = currencySign+t1;
		document.getElementById('crdbalamt').innerHTML = currencySign+totalamt1;
		var remain = t1 - totalamt1;
		
		document.getElementById('crdremainamt').innerHTML = currencySign + remain;
		$( '#creditnotepopup' ).modal( "show" );
		
	}else if(document.getElementById('howpaid').value=='prepayment'){
		if(parseFloat(t1)>parseFloat(document.getElementById('prepaymntamntid').value)){
			jAlert('error', 'Credit balence can not be less than payamount. ', 'Error Dialog')
			
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
		}else{
			
			if(document.getElementById('invcetype').value==0){
				jAlert('error', 'Please select invoice type', 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}else{
				//var creditnote = document.getElementById('creditnote').value;
		
				//document.getElementById('creditNotes').value = creditnote;
				 $( "#takepaymentmodel" ).modal( "hide" );
				saveAppointmentSlot(0);
				 
				  $( "#appointment" ).modal( "hide" );
				
			}
			
		}
	}else if(t1<0){
		jAlert('error', 'Payment Received Amount is Not less than 0', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}
	
	
	else{
		
		
		
		if(document.getElementById('invcetype').value==0){
				jAlert('error', 'Please select invoice type', 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}else{
				//var creditnote = document.getElementById('creditnote').value;
		
				//document.getElementById('creditNotes').value = creditnote;
				 $( "#takepaymentmodel" ).modal( "hide" );
				saveAppointmentSlot(0);
				
				  $( "#appointment" ).modal( "hide" );
				
			}
	}
}

function getTakePaymentCharge(){
	
	
	document.getElementById('disctype').value = 0;
	document.getElementById('discount').value = 0;
	
	var clientId = "0";
	if(document.getElementById('clientId')){
		clientId = document.getElementById('clientId').value;
	}
	var apmtType = document.getElementById('apmtType').value;
	/*var url = "chargeNotAvailableSlot?apmtType="+apmtType+" ";*/
	var url = "chargeopdotBookAppointmentAjax?apmtType="+apmtType+"&clientId="+clientId+" ";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = getTakePaymentChargeRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function getTakePaymentChargeRequest(){
	
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			  var str=req.responseText;
	          var data=str.split("~");
			document.getElementById('totalamount').value = parseFloat(data[0])+parseFloat(data[1]);
			totalamt1 = parseFloat(data[0])+parseFloat(data[1]);
			document.getElementById('payAmount').value  = totalamt1;
			document.getElementById('opdotcharge').value=data[0];
			document.getElementById('opdotregcharge').value=data[1];
			
			if(!(data[1]>0)){
				document.getElementById('opdotregcharge').readOnly =true;
			}
			
			 var isot = document.getElementById('radio3').checked;
			 if(isot==true){
				 totalamt1 = 0;
				 var potcharge = document.getElementById('potcharge').value;
				 var psurcharge = document.getElementById('psurcharge').value;
				 var panetcharge = document.getElementById('panetcharge').value;
				 var sic = document.getElementById('sic').value;
				 if(potcharge==""){
					 potcharge="0";
				 }
				 if(psurcharge==""){
					 psurcharge="0";
				 }
				 if(panetcharge==""){
					 panetcharge="0";
				 }
				 if(sic==""){
					 sic="0";
				 }
				 var assistaffcharge = 0;
				 if(document.getElementById("assistaffcharge")){
					 assistaffcharge = document.getElementById("assistaffcharge").value;
					 if(assistaffcharge==""){
						 assistaffcharge="0";
					 }
				 }
				 totalamt1 = parseFloat(potcharge) + parseFloat(psurcharge) + parseFloat(panetcharge) + parseFloat(sic) + parseFloat(assistaffcharge);
				 document.getElementById('totalamount').value = totalamt1;
				 document.getElementById('payAmount').value  = totalamt1;
				 
				document.getElementById('opdotcharge').value=totalamt1;
				document.getElementById('opdotregcharge').value="0";
				document.getElementById('opdotregcharge').readOnly =true;
			 }
			
		}
		
		//if payee is tp set paid amount to be 0
		if(data[2]=='true'){
		if(document.getElementById('paybypatient1').checked == true){
			document.getElementById('payAmount').value  = 0;
			document.getElementById('howpaid').value  = 'Cash';
			
		}
		}
	}
}


function opdPaymentAjax(){
	
	var url = "chargeNotAvailableSlot?apmtType="+apmtType+" ";
	
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = opdPaymentAjaxRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function opdPaymentAjaxRequest(){

	if (req.readyState == 4) {
		if (req.status == 200) {
		
		}
	}

}

function changeOPDRegcharge(val){
	var opdotcharge = parseFloat(document.getElementById('opdotcharge').value);
	var total = opdotcharge + parseFloat(val);
	document.getElementById('totalamount').value=total;
	setAmountDue();
}
