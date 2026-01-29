<%@page import="com.apm.Registration.eu.entity.Clinic"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<% Clinic clinic=(Clinic) request.getAttribute("clinicaccess");%>
<html>
<head>
<%String checked=""; %>
<link rel="stylesheet" href="_assets/newtheme/css/main.css">

<link rel="stylesheet" href="mis/kpiplugin/css/style.css">
<style>
.header{
 padding-bottom: 5px;
}
.headline{
    background-color: #666666;
    font-size: 13px;
    color: white;
    padding-left: 10px;
   
}
       .main { 
            display: block; 
            position: relative; 
            padding-left: 45px; 
            margin-bottom: 0px; 
            cursor: pointer; 
            font-size: 13px; 
        } 
          
        /* Hide the default checkbox */ 
        input[type=checkbox] { 
            visibility: hidden; 
        } 
          
        /* Creating a custom checkbox 
        based on demand */ 
        .geekmark { 
            position: absolute; 
            top: 0; 
            left: 0; 
            height: 21px; 
            width: 25px; 
            background-color: green; 
        } 
          
        /* Specify the background color to be 
        shown when hovering over checkbox */ 
        .main:hover input ~ .geekmark { 
            background-color: yellow; 
        } 
          
        /* Specify the background color to be 
        shown when checkbox is active */ 
        .main input:active ~ .geekmark { 
            background-color: red; 
        } 
          
        /* Specify the background color to be 
        shown when checkbox is checked */ 
        .main input:checked ~ .geekmark { 
            background-color: green; 
        } 
          
        /* Checkmark to be shown in checkbox */ 
        /* It is not be shown when not checked */ 
        .geekmark:after { 
            content: ""; 
            position: absolute; 
            display: none; 
        } 
          
        /* Display checkmark when checked */ 
        .main input:checked ~ .geekmark:after { 
            display: block; 
        } 
          
        /* Styling the checkmark using webkit */ 
        /* Rotated the rectangle by 45 degree and  
        showing only two border to make it look 
        like a tickmark */ 
        .main .geekmark:after { 
            left: 8px; 
            bottom: 5px; 
            width: 6px; 
            height: 12px; 
            border: solid white; 
            border-width: 0 4px 4px 0; 
            -webkit-transform: rotate(45deg); 
            -ms-transform: rotate(45deg); 
            transform: rotate(45deg); 
        } 
        
       .collapse{
       padding: 10px;
       } 
</style>
</head>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="margin-bottom: 20px">
		<div class="row details" style="background-color: #339966 !important;">
			<h4 align="center">Clinic Access</h4>
		</div>
</div>
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".opd" >OPD</div>
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 opd collapse out'>
				<%if(clinic.isOpd_tp_zero_invoice()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">OPD TP Payment Received 0 (Book With Payment)  <input type="checkbox" onchange="setfun(this,'opd_tp_zero_invoice')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       			
       			<%if(clinic.isOpd_video_icon_show()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Show video icon in OPD dashboard  <input type="checkbox" onchange="setfun(this,'opd_video_icon_show')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>				
       			
				</div>
				
				
			</div>
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".inv" >Investigations</div>
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 inv collapse out'>
				<%if(clinic.isJobtitlewise_investigation()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">JobTitle Wise Investigation <input type="checkbox" onchange="setfun(this,'jobtitlewise_investigation')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>			
       			
				<%if(clinic.isHidelettinvst()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Hide Investigation LetterHead <input type="checkbox" onchange="setfun(this,'hidelogoinvst')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       			
				<%if(clinic.isInvst_inv_apr()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Collect Investigation With Add Charges <input type="checkbox" onchange="setfun(this,'invst_inv_apr')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
				
				<%if(clinic.isInvest_order()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Investigation Order Dashboard <input type="checkbox" onchange="setfun(this,'invest_order')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
				</div>
				
				
			</div>
			
			
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".emr" >EMR</div>
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 emr collapse out'>
				
				<%if(clinic.isEmr_vitals_show()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">EMR Print Vital Show <input type="checkbox" onchange="setfun(this,'emr_vitals_show')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>			
       
				</div>
				
				
				
			</div>
			
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".ipd">IPD</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 ipd collapse out'>
			<%if(clinic.isDirect_ipd()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Direct IPD <input type="checkbox" onchange="setfun(this,'direct_ipd')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>
			<%if(clinic.isDrwise_ipd()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Doctor Wise IPD <input type="checkbox" onchange="setfun(this,'drwise_ipd')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       		<%if(clinic.isShow_wardname()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Show Ward Name <input type="checkbox" onchange="setfun(this,'show_wardname')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       		<%if(clinic.isSms_on_bedchange()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">SMS On Bed Change <input type="checkbox" onchange="setfun(this,'sms_on_bedchange')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       		</div>
       		<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 ipd collapse out'>	
       		<%if(clinic.isSms_on_newadm()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">SMS On New Admission<input type="checkbox" onchange="setfun(this,'sms_on_newadm')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       		<%if(clinic.getIpd_abbr_access()==1){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">IPD Abrivation Access<input type="checkbox" onchange="setfun(this,'ipd_abbr_access')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       		<%if(clinic.getDischarge_validation()==1){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Discharge Validation<input type="checkbox" onchange="setfun(this,'discharge_validation')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       		<%if(clinic.isNewdischargecard()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">New Discharge Card <input type="checkbox" onchange="setfun(this,'newdischargecard')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>						
       			</div>	
       		<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 ipd collapse out'>	
       		<%if(clinic.isDischarge_msg_hs()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">SMS on Discharge<input type="checkbox" onchange="setfun(this,'discharge_msg_hs')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       		</div>	
       			
			</div>
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".ot">OT</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 ot collapse out'>
			<%if(clinic.isOt_patient_sms()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">OT Book SMS for Patient <input type="checkbox" onchange="setfun(this,'ot_patient_sms')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>
       			
       			<%if(clinic.isOt_surgeon_sms()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">OT Book SMS for Surgeon <input type="checkbox" onchange="setfun(this,'ot_surgeon_sms')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>
       		</div>	
       			
			</div>
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".invent">Inventory</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 invent collapse out'>
			<%if(clinic.isBarcode_productname_show()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Show Product Name On Barcode <input type="checkbox" onchange="setfun(this,'barcode_productname_show')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       		<%if(clinic.isAuto_generic_name()){checked="checked='checked'";}else{checked="";} %>
       			<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 	<label class="main">Auto Generic name and MFG from Master <input type="checkbox" onchange="setfun(this,'auto_generic_name')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       			<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 	<label >Catalogue product auto add to prescription of location 
				 	<s:select list="locationListPharmacy" onchange="updateinventorygrnloction(this.value,'grn_to_prisc_location')" theme="simple" name="grn_to_prisc_location" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
					</s:select> 
					</label> 
       			</div>	
       			<%if(clinic.isHidecalinpoprint()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Hide Calculation In PO Print <input type="checkbox" onchange="setfun(this,'hidecalinpoprint')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
			</div>	
			</div>
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".pharamcy">Pharmacy</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 pharamcy collapse out'>
			<%if(clinic.isPhar_print_seq()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Pharmacy Summary Print Assending Order <input type="checkbox" onchange="setfun(this,'phar_print_seq')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
			</div>	
			</div>
			
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".acc">Accounts</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 acc collapse out'>
			
				<%if(clinic.isShow_unpost()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">OPD Invoice Post <input type="checkbox" onchange="setfun(this,'opd_invoice_post')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>		
       			
				<%if(clinic.isIsledgerhosp()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Ledger <input type="checkbox" onchange="setfun(this,'isledgerhosp')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       			
       			<%if(clinic.isDirect_refund_disc()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Direct Refund Discount <input type="checkbox" onchange="setfun(this,'direct_refund_disc')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       				
       			<%if(clinic.isHidelettbillinv()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Hide Header In Bill <input type="checkbox" onchange="setfun(this,'hidelogobillinv')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       				
			</div>	
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 acc collapse out'>
			<%if(clinic.isAdv_payamnt_sms()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Advance Payment SMS <input type="checkbox" onchange="setfun(this,'advance_payment_sms')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
			
			<%if(clinic.isRefund_payamnt_sms()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Refund Payment SMS <input type="checkbox" onchange="setfun(this,'ref_payment_sms')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
			<%if(clinic.isInvoice_groupby()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Grouping Invoice Charges <input type="checkbox" onchange="setfun(this,'invoice_groupby')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>
			<%if(clinic.isInvoice_charge_seqno()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Sequence Wise Master Charge Invoice <input type="checkbox" onchange="setfun(this,'invoice_charge_seqno')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>
       			
			</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 acc collapse out'>
				<%if(clinic.isDisc_approve_sms()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 <label class="main">Discount Request SMS <input type="checkbox" onchange="setfun(this,'disc_approve_sms')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>	
       		</div>
			</div>
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
			<div class='headline'data-toggle="collapse" data-target=".other">Others</div>
			<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 other collapse out'>
				<%-- <%if(clinic.isBalgopal()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Is Balgopal ?<input type="checkbox" onchange="setfun(this,'isbalgopal')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	
       			<%if(clinic.isBalgopal()){checked="checked='checked'";}else{checked="";} %>
				<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
				 <label class="main">Is Prachi Clinic ?<input type="checkbox" onchange="setfun(this,'prachi_clinic')" <%=checked %>> <span class="geekmark"></span> </label> 
       			</div>	 --%>
       			
			</div>	
			</div>
			<script>
			function checkNumberOrNotofclinic(inputtxt)
			{
				 var numbers = /^[0-9]+$/;  
			     
			     if(inputtxt.match(numbers))  {
			        return true;
			     } else {
			          return false;
			     }
			} 
			function updateinventorygrnloction(val,col){
					/* if(!checkNumberOrNotofclinic(val)){
						 jAlert('error', "Please enter valid location!", 'Error Dialog');
							setTimeout(function() {
								$("#popup_container").remove();
								removeAlertCss();
							}, alertmsgduration);
					}  */
						
					if(val==0){
						jAlert('error', "Please select valid location!", 'Error Dialog');
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration);
					}else{
						 var url = "setaccessToThisCommonnew?col="+col+"&value="+val;
							if (window.XMLHttpRequest) {
								req = new XMLHttpRequest();
							}
							else if (window.ActiveXObject) {
								isIE = true;
								req = new ActiveXObject("Microsoft.XMLHTTP");
							}   
						    req.onreadystatechange =setfunReq;
						    req.open("GET", url, true); 
						    req.send(null)
					}
			}
			
		
			function setfun(obj,col){
				var val=0;
				if(obj.checked){
					val=1;
				}
				
				 var url = "setaccessToThisCommonnew?col="+col+"&value="+val;
					
					if (window.XMLHttpRequest) {
						req = new XMLHttpRequest();
					}
					else if (window.ActiveXObject) {
						isIE = true;
						req = new ActiveXObject("Microsoft.XMLHTTP");
					}   
				               
				    req.onreadystatechange =setfunReq;
				    req.open("GET", url, true); 
				    
				    req.send(null)
				
			}
			
			
			function setfunReq(){
				if (req.readyState == 4) {
					if (req.status == 200) {
						var xx=Number(req.responseText);
						if(xx>0){
							 jAlert('success', 'Access Saved Successfully !.', 'Success Dialog');
								
								setTimeout(function() {
									$("#popup_container").remove();
									removeAlertCss();
								}, alertmsgduration);
						}
					}
				}
			}
			</script>
			<script src="common/chosen_v1.1.0/chosen.jquery.js" type="text/javascript"></script>
<script type="text/javascript">
    var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"100%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }
  </script>
</html>
