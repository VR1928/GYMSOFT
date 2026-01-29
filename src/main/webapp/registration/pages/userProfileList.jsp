<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="registration/js/userprofile.js"></script>
<%-- <script type="text/javascript" src="registration/js/edituserprofile.js"></script> --%>
<script type="text/javascript" src="common/js/pagination.js"></script>

<link rel="stylesheet" href="_assets/newtheme/css/main.css">
<link rel="stylesheet" href="_assets/newtheme/css/responsive.css">



<style>
	.form-control {
	    border: 1px solid #ddd;
	    background-color: #fff;
	}
</style>


<%LoginInfo loginfo = LoginHelper.getLoginInfo(request); %>

<div class="">
							<div class="">
								<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

										<h4>View Trainer</h4>

									</div>
								</div>
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										

<div class="col-lg-12 col-md-12 topback2">
	<s:form action="UserProfile" theme="simple">
		<div class="form-inline">
		<div class="form-group">
			<s:textfield theme="simple" name="searchText" placeholder="Search by Name / Job Title" size="40" cssClass="form-control" />
		</div>
		<div class="form-group">
			<input type="submit" value="Go" class="btn btn-primary" />
		</div>
		<%-- <div class="form-group">
			<select class="form-control">
				<option>Globle Access</option>
				<option>ADMIN</option>
				<option>PHARMACIST</option>
			</select>
		</div> --%>
		<div class="form-group" style="float: right;">
			<!-- <a href="#" onclick="openPopup('roleAccessSettingClinicRegistration')" class="btn btn-primary">Access Setting</a> -->
				<a href="inputUserProfile" class="btn btn-primary" style="text-decoration: none;"><i class="fa fa-plus"></i> Add New Trainer</a>
		</div>
	</div>
		</s:form>
	
	
	
	
	</div>
	<s:hidden name = "message" id = "message"></s:hidden>
	<s:if test="hasActionMessages()">
	<script>
		var msg = " " + document.getElementById('message').value;
		showGrowl('', msg, 'success', 'fa fa-check');
		</script>
	</s:if>
	

		<div class="">
		<table class="table  table-condensed table-striped table-bordered text-uppercase">
			<thead>
			<tr>
				<th>Trainer Name</th>
				<th>Mobile No</th>
				<th>Role</th>
				<th>Edit</th>
				
				<%-- <th class=" sortable <s:if test="#attr.pagination.sortColumn.equals('firstname')">sorted <s:property value="#attr.pagination.sortClass"/> </s:if>" style="width: 20%;">
					<a href="#" onclick="fnPagination(6,'firstname');" style="color:#fff;"> Users Details</a></th> --%>
			</tr>
			</thead>
			<tbody>
			<%int i=0; %>
			<s:if test="userProfileList.size!=0">
				<s:iterator value="userProfileList" >
					<tr>
						<td><s:property value="firstname" /> <s:property value="lastname" /></td>
						<td><s:property value="mobile" /></td>
						<td><s:property value="jobtitle" /></td>
						<s:hidden value="%{id}" name="id"></s:hidden>
						<s:url action="editUserProfile" id="edit">
							<s:param name="id" value="%{id}"></s:param>
						</s:url>
						
						<td>
								<s:a href="%{edit}">
									<i class="fa fa-edit"></i>
								</s:a>
						</td>
							
							
								
								
						<s:url action="deleteUserProfile" id="delete">
							<s:param name="id" value="%{id}"></s:param>
						</s:url>
						<%-- <td class="text-center hidden"><s:a href="%{delete}" onclick="return confirmedDelete()" cssClass="text-danger" >
								<i class="fa fa-trash-o"></i>
							</s:a></td> --%>
					</tr>
					<%i++; %>
				</s:iterator>
			</s:if>
			<s:else>
						There is no User Profile found!!
					</s:else>
</tbody>
		</table>

</div>
	
	
	
	

<s:form action="UserProfile" name="paginationForm" id="paginationForm" theme="simple">
		<s:hidden name="searchText"></s:hidden>
		<div class="col-lg-12 col-md-12" style="padding:0px;margin-top:15px;">
		<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
			Total:<b class="text-info"><s:property value="totalRecords" /></b>
		</div>		 
			<%@ include file="/common/pages/pagination.jsp"%>
		</div>
	</s:form>

											

											
										
									</div>
								</div>
							</div>
						</div>













