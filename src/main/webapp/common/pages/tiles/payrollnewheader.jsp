<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="print.css" media="print" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	
		<!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="payrollnew/assets/img/favicon.png">
		
		<!-- Bootstrap CSS -->
        <link rel="stylesheet" href="payrollnew/assets/css/bootstrap.min.css">
		
		<!-- Fontawesome CSS -->
        <link rel="stylesheet" href="payrollnew/assets/css/font-awesome.min.css">
		
		<!-- Lineawesome CSS -->
        <link rel="stylesheet" href="payrollnew/assets/css/line-awesome.min.css">
		
		<!-- Select2 CSS -->
		<link rel="stylesheet" href="payrollnew/assets/css/select2.min.css">
		
		<!-- Datetimepicker CSS -->
		<link rel="stylesheet" href="payrollnew/assets/css/bootstrap-datetimepicker.min.css">
		
		<!-- Main CSS -->
        <link rel="stylesheet" href="payrollnew/assets/css/style.css">
</head>
<body>
<div class="header hidden-print">
			
				<!-- Logo -->
                <div class="header-left">
                    <a href="payrollnew/index.html" class="logo">
						<img src="payrollnew/assets/img/logo.png" width="40" height="40" alt="">
					</a>
                </div>
				<!-- /Logo -->
				
				<a id="toggle_btn" href="#" onclick="onmedenu">
					<span class="bar-icon">
						<span></span>
						<span></span>
						<span></span>
					</span>
				</a>
				<%LoginInfo loginInfo = LoginHelper.getLoginInfo(request); %>
				
				<!-- Header Title -->
                <div class="page-title-box">
					<%-- <h3>  <%if(loginInfo.isPayrollaccess()){ %>
						  Payroll
						  <%}else{ %>
						  MY HR
						  <%} %></h3> --%>
                </div>
				<!-- /Header Title -->
				
				<a id="mobile_btn" class="mobile_btn" href="#sidebar"><i class="fa fa-bars"></i></a>
				
				<!-- Header Menu -->
				<ul class="nav user-menu">
					<!-- /Message Notifications -->
	<s:form action="mobLogout" id="logotfrm"></s:form>
			<script type="text/javascript">
                        	function goforlogout(){
                        		document.getElementById('logotfrm').submit();
                        	}
                        </script>
					<li class="">
						<%-- <a href="#" class="" data-toggle="dropdown">
							<span class="status online"></span></span>
							<span><%=loginInfo.getUserId() %></span>
							<span><a class="" href="#" onclick="goforlogout()">Logout</a></span>
						</a> --%>
						<div class="dropdown-menu">
						<a class="dropdown-item" href="payrollnew/profile.html">My Profile</a>
							
						</div>
					</li>
				</ul>
				<!-- /Header Menu -->
				
				<!-- Mobile Menu -->
				<div class=" mobile-user-menu" style="font-size: inherit;margin-left: auto;">
					<!-- <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-ellipsis-v"></i></a> -->
					<div class="" style="margin-left: -1603px;">
						<%-- <a href="#" class="" data-toggle="dropdown">
							<span class="status online"></span>
							<span style="color:white"><%=loginInfo.getUserId() %></span>
							<span><a class="" href="#" onclick="goforlogout()" style="color:white">Logout</a></span>
						</a> --%>
					</div>
				</div>
				<!-- /Mobile Menu -->
				
            </div>
            
			<script type="text/javascript">
			function onmedenu() {
				
				
				$(document).on('click', '#toggle_btn', function() {
					if($('body').hasClass('mini-sidebar')) {
						$('body').removeClass('mini-sidebar');
						$('.subdrop + ul').slideDown();
					} else {
						$('body').addClass('mini-sidebar');
						$('.subdrop + ul').slideUp();
					}
					return false;
				});
				$(document).on('mouseover', function(e) {
					e.stopPropagation();
					if($('body').hasClass('mini-sidebar') && $('#toggle_btn').is(':visible')) {
						var targ = $(e.target).closest('.sidebar').length;
						if(targ) {
							$('body').addClass('expand-menu');
							$('.subdrop + ul').slideDown();
						} else {
							$('body').removeClass('expand-menu');
							$('.subdrop + ul').slideUp();
						}
						return false;
					}
				});
				}
			</script>
			
</body>
</html>