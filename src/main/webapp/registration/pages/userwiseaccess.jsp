<%@page import="com.apm.Registration.eu.entity.UserProfile"%>
<%@page import="com.apm.DiaryManagement.eu.entity.DiaryManagement"%>
<%@page import="com.apm.Registration.eu.entity.Clinic"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<% UserProfile clinic=(UserProfile) request.getAttribute("userwiseaccesssetting");%>
<html>
<head>
<%String checked=""; %>
<link rel="stylesheet" href="_assets/newtheme/css/main.css">

<link rel="stylesheet" href="mis/kpiplugin/css/style.css">
<style>
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
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
		<div class="row details" style="background-color: #339966 !important;height: 37px;">
			<h4 align="center">User Wise Access (<s:property value="fullname"/> - <s:property value="job_title"/> - <s:property value="userId"/>)</h4>
		</div>
</div>
<div class="clearfix" style="height: 50px;"></div>
			<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
				<div class='headline'data-toggle="collapse" data-target=".inventory" style="height: 31px;" ><h4 style="padding-top: 7px;">Inventory</h4></div>
				
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 inventory collapse out'>
					<%if(clinic.isEdit_paypo()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Supplier Payment Edit <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'edit_paypo','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>	
	       			<%if(clinic.isAdjustmentaccess()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Inventory Adjustment Process <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'adjustmentaccess','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>			
	       		
					<%if(clinic.isSupplier_add()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Inventory Supplier Add <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'supplier_add','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>					
	       			
				</div>
				
	       	</div>
	       	
	       	<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
				
				<div class='headline'data-toggle="collapse" data-target=".pharmacy" style="height: 31px;" ><h4 style="padding-top: 7px;">Pharmacy</h4></div>
				
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 pharmacy collapse out'>
					<%if(clinic.isMax_phar_discount()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Direct Discount greater than 10% <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'max_phar_discount','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>	
	       				
	       			
				</div>
				
				
	       	</div>
	       	
	       	<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
				
				<div class='headline'data-toggle="collapse" data-target=".indent" style="height: 31px;" ><h4 style="padding-top: 7px;">Indent</h4></div>
				
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 indent collapse out'>
					<%if(clinic.isChange_indent_product()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Replace Indent Product while trasfering <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'change_indent_product','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>	
	       				
	       			
				</div>
				
				
	       	</div>
			
	       	<div class='header col-lg-12 col-md-12 col-xs-12 col-sm-12'>
				
				<div class='headline'data-toggle="collapse" data-target=".payroll" style="height: 31px;" ><h4 style="padding-top: 7px;">Payroll</h4></div>
				
				<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12 payroll collapse out'>
					<%if(clinic.isPayrollaccess()){checked="checked='checked'";}else{checked="";} %>
					<div class="col-lg-3 col-md-3 col-xs-3 col-sm-3  ">
					 	<label class="main">Payroll Admin Access <input type="checkbox" onchange="updateUserWiseAccessNew(this.checked,'payrollaccess','<s:property value="userId"/>')" <%=checked %>> <span class="geekmark"></span> </label> 
	       			</div>	
	       				
	       			
				</div>
				
				
	       	</div>
			
			<script>
			function updateUserWiseAccessNew(val,col,userid){
				/* var val=0;
				if(obj.checked){
					val=1;
				} */
				
				var url = "updateuserwiseaccessUserProfile?userid="+userid+"&val="+val+"&cname="+col+"";
				if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			    req.onreadystatechange = updateUserWiseAccessNewRequest;
			    req.open("GET", url, true); 
			    req.send(null);
				
			}
			
			
			function updateUserWiseAccessNewRequest(){
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
</html>
