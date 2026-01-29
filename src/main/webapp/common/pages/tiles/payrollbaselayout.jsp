
<%@page import="com.apm.main.common.constants.Constants"%>
<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%LoginInfo loginfo = LoginHelper.getLoginInfo(request);%>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
  <title>
  <%-- <%if(loginfo.isPayrollaccess()){ %>
  Payroll
  <%}else{ %>
  MY HR
  <%} %> --%>
  </title>
  <link rel="shortcut icon" type="image/x-icon" href="payrollnew/assets/images/favicon.ico">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,500,600,700" rel="stylesheet">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/fullcalendar.min.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/select2.min.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/bootstrap-datetimepicker.min.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/plugins/morris/morris.css">
  <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/style.css">
  
<script src="common/chosen_v1.1.0/chosen.jquery.js"
	type="text/javascript"></script>
<script src="common/Bootstrap/js/bootstrap.min.js"
	type="text/javascript"></script>

<script src="common/js/jquery.js" type="text/javascript"></script>
<script src="common/js/jquery.alerts.js" type="text/javascript"></script>
<link href="common/css/jquery.alerts.css" rel="stylesheet"
	type="text/css" media="screen" />
<script src="common/js/jquery/additional-validation-methods.js"
	type="text/javascript"></script>
	
<script src="common/js/jquery/jquery.validate.js" type="text/javascript"></script>


<script type="text/javascript" src="common/js/fullscreen.js"></script>

<script type="text/javascript" src="common/js/masterValidators.js"></script>

<script type="text/javascript" src="common/js/jquery/bootstrap-growl-master/bootstrap-growl.min.js"></script>
<script src="common/Bootstrap/js/bootstrap.min.js"
	type="text/javascript"></script>>
	<!-- <link href="/SomeProject/static/bundle-bundle_bootstrap_head.css" type="text/css" rel="stylesheet" media="screen, projection">
<link rel="stylesheet" type="text/css" href="print.css" media="print" /> -->
<style type="text/css">
.hidden{
display: none;
}
</style>
</head> 
  <body>
    <div class="main-wrapper ">
       <div class="header">
       	<tiles:insertAttribute name="header" />
       </div>
       <div class="sidebar noprint hidden" id="sidebar" style="width: 0">
         <div class="sidebar-inner slimscroll ">
					<div id="sidebar-menu" class="sidebar-menu">
						<ul>
						   <li class="submenu">
								<a href="#" class="" id="empmenu" onclick="getsmsCount()"><i class="la la-user" ></i> <span> CheckSMS</span> <span class="menu-arrow"></span></a>
								
							</li>
						
							<li class="submenu">
								<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-user" ></i> <span> Customer</span> <span class="menu-arrow"></span></a>
								<ul style="display: none;">
								 		<li><a href="#" onclick="openSamePopup('PayrollEmployee?status=1')" id="allemp">All Customer</a></li>
								</ul>
							</li>
							<li class="submenu">
										<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-book" ></i> <span> Trainer</span> <span class="menu-arrow"></span></a>
										<ul style="display: none;">
								 			<li><a href="#" onclick="opencPopup('UserProfile')" id="allemp">List Trainer</a></li>
								 			<li><a href="#" onclick="opencPopup('listtrainernoteNotAvailableSlot')" id="allemp">All Notes</a></li>
								 			
										</ul> 
							</li>	
							 
							<li class="submenu">
								<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-user" ></i> <span> Master</span> <span class="menu-arrow"></span></a>
								<ul style="display: none;">
								 		<li><a href="#" onclick="opencPopup('AppointmentType?selectedid=2')" id="allemp">Create Charge</a></li>
								 		
								 		
								 </ul>
							</li>
							
							<li class="submenu">
								<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-user" ></i> <span>Diet</span> <span class="menu-arrow"></span></a>
								<ul style="display: none;">
								 		<li><a href="#" onclick="opencPopup('addDeclaration')" id="allemp">Create Template</a></li>
								 		
								 		
								 </ul>
							</li>
							
							<li class="submenu">
										<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-book" ></i> <span> Package</span> <span class="menu-arrow"></span></a>
										<ul style="display: none;">
								 			<li><a href="#" onclick="opencPopup('PackageMaster?selectedid=44')" id="allemp">Packages</a></li>
										</ul> 
							</li>
							
							<li class="submenu">
										<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-book" ></i> <span> Voucher</span> <span class="menu-arrow"></span></a>
										<ul style="display: none;">
								 			<li><a href="#" onclick="opencPopup('ExpenceManagement?action=0')" id="allemp">Voucher</a></li>
										</ul> 
							</li>
							
							<li class="submenu">
										<a href="#" class="" id="empmenu" onclick="changesubdrop('empmenu')"><i class="la la-book" ></i> <span> Report</span> <span class="menu-arrow"></span></a>
										<ul style="display: none;">
								 			<li><a href="#" onclick="opencPopup('paymentreportSummary')" id="allemp">Payment Report</a></li>
								 			<li><a href="#" onclick="opencPopup('invoiceReportChargesRpt')" id="allemp">Invoice Report</a></li>
								 			
										</ul> 
							</li>	 
							</ul>		 
									
					</div>
                </div>
     
       
       </div>
      <div class="page-wrapper" style="margin-left: 0;"> 
	   <div class="content container-fluid">
	    <tiles:insertAttribute name="body" />
	   </div>
       </div>
    </div>
    <div class="sidebar-overlay noprint" data-reff="#sidebar"></div>
   <!--  <script type="text/javascript" src="payrollnew/assets/js/jquery-3.2.1.min.js"></script> -->
   <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script> -->
   <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
   <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script> -->
    <script type="text/javascript" src="payrollnew/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="payrollnew/assets/js/jquery.slimscroll.js"></script>
    <script type="text/javascript" src="payrollnew/assets/js/select2.min.js"></script>
    <script type="text/javascript" src="payrollnew/assets/js/moment.min.js"></script>
    <script type="text/javascript" src="payrollnew/assets/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="payrollnew/assets/plugins/morris/morris.min.js"></script>
    <script type="text/javascript" src="payrollnew/assets/js/app.js"></script>
    
    <script type="text/javascript">
    function changesubdrop(val) {
    	 var element = document.getElementById(""+val);
    	   element.classList.remove("subdrop");
    	/* document.getElementById(""+val).className="subdrop"; */
   document.getElementById(""+val).className += " subdrop";
  
  </script>
    <!--  <div class="modal fade" id="smscountPopupid" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
					 <h4 class="modal-title" id="myModalLabel">Check SMS</h4>
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times; </span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Checked SMS</h4>
					</div>
					<div class="modal-body">
						<label>Total SMS : <span id="tsms"></span></label></br>
						<label>Remaining SMS : <span id="rsms"></span></label></br>
						<label>Used SMS : <span id="usms"></span></label>
					</div>
					<div class="modal-footer" style="display: none;">

						<button type="button" class="btn btn-primary"
							onclick="openprintOpdInvoice()" data-dismiss="modal">Yes</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal">No</button>
					</div>
				</div>
			</div>
		</div> -->
  </body>
  
 
</html>


