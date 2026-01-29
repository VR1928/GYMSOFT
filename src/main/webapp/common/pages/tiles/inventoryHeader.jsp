<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@page import="com.apm.common.web.common.helper.LoginHelper"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

 <script type="text/javascript" src="inventory/js/addcategory.js"></script>
 <script type="text/javascript" src="pharmacy/js/pharmacy.js"></script>
</head>
<%LoginInfo loginfo = LoginHelper.getLoginInfo(request); %>
<body>
<div class="row details" style="background: #339966 !important;">
								<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
								<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 oneseticonleft">
									<img src="dashboardicon/inventory.png" class="img-responsive prescripiconcircle" style="width:28px;margin-top:2px;"/>
								</div>
								<div class="col-lg-7 col-md-7 titlestleftiocn">
								 <%if(session.getAttribute("isfromcathlab")!=null){ %>
						  			<%  String isfromcathlab =  (String)session.getAttribute("isfromcathlab"); %>
						  				<% if(isfromcathlab.equals("1")){%>
											<h4 style="font-size:12px;">Cathlab Dashboard </h4>
										<%}else{ %>
											<h4 style="font-size:12px;">Inventory Dashboard </h4>
										<%} %>
								<%}else{ %>
									<h4 style="font-size:12px;">Inventory Dashboard </h4>
								<%} %>
								</div>
								<div class="col-lg-2 col-md-2" style="float: right;margin-top: 3px;">
												<%if(loginfo.getUserType()==2 || loginfo.getPharmacyUserType()==2 ) {%>
												<s:select list="locationListPharmacy" onchange="setlocPharmacy(this.value)" theme="simple" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										     	</s:select> 
										     	<%}else{%>  
										     	<s:select list="locationListPharmacy" id="dropdown" onchange="setlocPharmacy(this.value)" theme="simple" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										     	</s:select> 
										     	<%} %> 	
								</div>
								<div class="col-lg-2 col-md-2" style="float: right;margin-top: 3px;margin-right: 10px;">
												
												<%-- <s:if test="hidecatagoryid==1">
													
												</s:if>
												<s:else>
													<s:select list="categoryList"  name="category_id" listKey="id" listValue="name" cssClass="form-control chosen-select" id="categoryid" headerKey="0" headerValue="All" onchange="setCategory(this.value)" />
										    		<!--<option value="0">Select Inventory</option>
										    		<option value="1">Housekeeping</option>
										    		<option value="2">Medicine</option>
										    	-->
												</s:else> --%>
												
								</div>

								</div>
							</div>
</body>
<SCRIPT type="text/javascript">
      $("#dropdown").prop("disabled", true);
</SCRIPT>
</html>