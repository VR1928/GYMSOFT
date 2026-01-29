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
<style>
.setwismar{
	float: right;
    width: 30%;
    margin-bottom: 0px !important;
    margin-top: 1px;
}
</style>

</head>
<%LoginInfo loginfo = LoginHelper.getLoginInfo(request); %>
<script type="text/javascript" src="pharmacy/js/pharmacy.js"></script>
<body>
<section id="header" class="hidden">
                <header class="clearfix">

                    <!-- Right-side navigation -->
                    <ul class="nav-right pull-right list-inline">
                        

                        <li class="dropdown nav-profile">

                            <a href class="dropdown-toggle" data-toggle="dropdown">
                                <img src="assets/images/profile-photo.jpg" alt="" class="img-circle size-30x30">
                                <span>John Douey <i class="fa fa-angle-down"></i></span>
                            </a>

                            <ul class="dropdown-menu animated littleFadeInRight" role="menu">

                                <li>
                                    <a href="#">
                                        <span class="badge bg-greensea pull-right">86%</span>
                                        <i class="fa fa-user"></i>Profile
                                    </a>
                                </li>
                                
                                <li>
                                    <a href="#">
                                        <i class="fa fa-cog"></i>Settings
                                    </a>
                                </li>
                                <li class="divider"></li>
                               
                                <li>
                                    <a href="#">
                                        <i class="fa fa-sign-out"></i>Logout
                                    </a>
                                </li>

                            </ul>
                        </li>
                    </ul>
                    <!-- Right-side navigation end -->



                </header>

            </section>

			<div class="row mainheader">
				<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
					<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 oneseticonleft">
									<img src="dashboardicon/pharmacy.png" class="img-responsive prescripiconcircle">
					</div>
					<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5 titlestleftiocn">
							<h4>Pharmacy Dashboard </h4>
					</div>	
					<div class="col-lg-6 com-md-6 col-xs-6" style="padding: 0px;margin-left: 89px;margin-top: 3px;">
							<div class="form-group setwismar">
									<%if(loginfo.getUserType()==2 || loginfo.getPharmacyUserType()==2) {%>
										<s:select list="locationListPharmacy" onchange="setlocPharmacy(this.value)"  theme="simple" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										</s:select>
									<%}else{%>
									 	<s:select list="locationListPharmacy" id="dropdown" disabled="true" onchange="setlocPharmacy(this.value)" theme="simple" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										</s:select>
									<%} %>
							</div>
							
							
							<a  data-toggle="modal" data-target="#shortcutkeystips" href="" class="pharmacyshortcutkeysemenu"><i class="fa fa-info-circle" aria-hidden="true"></i> Shortcut TIPS </a>
							
							
					</div>
				</div>
			</div>
</body>


</html>