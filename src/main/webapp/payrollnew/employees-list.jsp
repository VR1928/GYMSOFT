<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<script>


window.onload = function () {
	
	//document.getElementById("allemp").className="active";	
	
	var autocharge= document.getElementById('chkautocharge').value;
	if(autocharge=="1"){
		document.getElementById('autocharge').checked=true;
	} else {
		document.getElementById('autocharge').checked=false;
	}
}

function showmenu(val){
	 document.getElementById("showmenu"+val).className="dropdown-menu dropdown-menu-right show"; 
	 setTimeout(function() {
		 document.getElementById("showmenu"+val).className="dropdown-menu dropdown-menu-right"; 
		}, alertmsgduration);
}
</script>
<script type="text/javascript">

    
    </script>
    
<html lang="en">
    <head>
     <link rel="stylesheet" type="text/css" href="payrollnew/assets/css/dataTables.bootstrap.min.css">
        <meta charset="utf-8">
        <script type="text/javascript" src="common/js/pagination.js"></script>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <meta name="description" content="Smarthr - Bootstrap Admin Template">
		<meta name="keywords" content="admin, estimates, bootstrap, business, corporate, creative, management, minimal, modern, accounts, invoice, html5, responsive, CRM, Projects">
        <meta name="author" content="Dreamguys - Bootstrap Admin Template">
        <meta name="robots" content="noindex, nofollow">
        <SCRIPT type="text/javascript" src="payroll/js/payrollvalidatation.js"></SCRIPT>
        <title>Employees - HRMS admin template</title>
    </head>
    <script type="text/javascript">
    $(document).ready(function() {
        $('#example').DataTable( {
            "pagingType": "full_numbers"
        } );
    } );
    </script>
     <script type="text/javascript">
  $(function() {
    $('#date_join').datetimepicker({
      language: 'pt-BR'
    });
  });
</script>
<style>
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
</style>
<%String color="";%>
    <body>
					
				<!-- Page Content -->
                <div class="content container-fluid">
				
					<!-- Page Title -->
					<div class="row">
						<div class="col">
							<h4 class="page-title">Customers</h4>
						</div>
						<div class="col-auto text-right float-right ml-auto m-b-30">
							<a href="#" class="btn add-btn" data-toggle="modal" data-target="#add_employee"><i class="fa fa-plus"></i> Add Customer</a>
							<a href="#" onclick="getsmsCount()" class="btn add-btn" data-toggle="modal" ><i class=""></i> Check Sms</a>
							<!-- <div class="view-icons">
								<a href="PayrollEmployee?status=0" class="grid-view btn btn-link active"><i class="fa fa-th"></i></a>
								<a href="PayrollEmployee?status=1" class="list-view btn btn-link"><i class="fa fa-bars"></i></a>
							</div> -->
						</div>
					</div>
					<!-- /Page Title -->
					
					<!-- Search Filter -->
					
					<form action="PayrollEmployee">
					<div class="row filter-row" >
							<div class="form-group form-focus" style="width: 20%">
								<s:textfield name="empnamesearch" cssClass="form-control floating"/>
								<label class="focus-label">Customer Name/ID/Mob No</label>
							</div>
							<div class="col-sm-6 col-md-3">  
							<%-- <div class="form-group form-focus">
								<s:select name="department" id="department" list="departmentlist" listKey="dept_id" listValue="department" cssClass="form-control showToolTip chosen-select" headerValue="Select Department" headerKey="0">
                                    </s:select>
							</div> --%>
						</div>
						 <div class="col-sm-6 col-md-3"> 
							<div class="form-group form-focus select-focus">
								
								<s:select cssClass="select floating"
										list="#{'0':'Active', '1':'Inactive'}"
										theme="simple"  name="activestatus" style="width: 45%" />
								<label class="focus-label">Status</label>
							</div>
						</div> 
						<div class="col-sm-6 col-md-3">  
							<input type="submit" class="btn btn-success btn-block" value="Search">  
						</div>
						</div>
						    </form>
                    
					<!-- /Search Filter -->
					
					<div class="row">
						<div class="col-md-12">
							<div class="table-responsive">
								<table class="table table-striped custom-table datatable">
									<thead>
									<%int i=1; %>
									
										<tr>
											<th>Sr No</th>
											<th>Customer ID</th>
											<th>Name</th>
											<!-- <th>Email</th> -->
											<th>Mobile</th>
											<th class="text-nowrap">Join Date</th>
											<!-- <th class="text-nowrap">Start Date</th> -->
											<th class="text-nowrap">Expiry Date</th>
											<!-- <th class="text-nowrap">DOB</th> -->
											<th style="text-align: center;">Edit</th>
											<th style="text-align: center;">Delete</th>
											<th style="text-align: center;">GymDiet</th>
											<th style="text-align: center;">Add Charge</th>
											<th style="text-align: center;">View Acc</th>
											<th style="text-align: center;">Active</th>
											<!-- <th>Department</th>
											<th>Role</th>
											<th>Working Hour/Week</th> -->
											<!-- <th class="text-right no-sort">Action</th> -->
										</tr>
									</thead>
									<tbody>
									<s:iterator value="employeelist">
									<s:if test="status==1">
									<%color="red"; %>
									</s:if>
									<s:else>
									<%color=""; %>
									</s:else>
									<s:hidden name="id"/>
									<s:hidden name="clientId" value=""/>
										<tr style="color: <%=color%>">
										<td><%=i++ %></td>
											<td><s:property value="id"/></td>
											<td>
												<h2 class="table-avatar">
													<a href="#" class="avatar"><img src="liveData/document/<s:property value="image"/>"></a>
													<a class="" href="payrollprofilePayrollEmployee?empid=<s:property value="id"/>" style="color: <%=color%>"> <s:property value="name"/></a>
												</h2>
											</td>
											<%-- <td><s:property value="email"/></td> --%>
											<td><s:property value="mobNo"/></td>
											<td><s:property value="date_join"/></td>
											<%-- <td><s:property value="fromdate"/></td> --%>
											<td><s:property value="exp_date"/></td>
											<%-- <td><s:property value="dob"/></td> --%>
											 <td style="text-align: center;">
												<%-- <div class="dropdown dropdown-action">
													<a href="#" class="action-icon dropdown-toggle" data-toggle="dropdown" aria-expanded="false" onclick="showmenu(<s:property value="id"/>)"><i class="material-icons">more_vert</i></a>
													<div class="dropdown-menu dropdown-menu-right" id="showmenu<s:property value="id"/>"> --%>
														<a class="dropdown-item" href="#" data-toggle="modal" data-target="#profile_info" onclick="employeesedit(<s:property value="id"/>)"><i class="fa fa-pencil m-r-5"></i> </a></td>
														<td style="text-align: center;">
														<a class="dropdown-item" href="#" data-toggle="modal" onclick="deleteemp(<s:property value="id"/>)"><i class="fa fa-trash-o m-r-5"></i> </a>
												<!-- 	</div>
												</div> -->
											</td>
											<td><a href="#" class="btn btn-info btn-block" onclick="openPopup('displayProfileClient?selectedid=<s:property value="id" />')">GymDiet</a></td>
											 <td><a href="#" class="btn btn-info btn-block" onclick="openPopup('debitAdditional?clientId=<s:property value="id" />')">Add Charge</a></td>
											 <td><a href="#" class="btn btn-info btn-block" onclick="openPopup('Statement?clientidds=<s:property value="id" />')">View Account</a></td>
											 <td>
                                                <ul class="settings" style="padding: 0px;list-style: none;margin-bottom: 0px;">
									<li>
										<s:if test="autocharge==1">
										<div class="control-label">
	                                                <div class="onoffswitch greensea">
	                                                	<input type="checkbox" name="onoffswitch" checked="checked" onclick="setactivests(<s:property value="id" />,this.checked)" class="onoffswitch-checkbox" id="activeinactive<%=i%>">
	                                                    <label class="onoffswitch-label" for="activeinactive<%=i%>">
	                                                        <span class="onoffswitch-inner"></span>
	                                                        <span class="onoffswitch-switch"></span>
	                                                    </label>
	                                                </div>
	                                            </div>
										</s:if>
										<s:else>
												<div class="control-label">
	                                                <div class="onoffswitch greensea" id="">
	                                                	<input type="checkbox" name="onoffswitch"  onclick="setactivests(<s:property value="id" />,this.checked)" class="onoffswitch-checkbox" id="activeinactive<%=i%>">
	                                                    <label class="onoffswitch-label" for="activeinactive<%=i%>">
	                                                        <span class="onoffswitch-inner"></span>
	                                                        <span class="onoffswitch-switch"></span>
	                                                    </label>
	                                                </div>
	                                            </div>
										</s:else>
										
	                                    </li>
								</ul>
											</td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
						</div>
					</div>
                </div>
				<!-- /Page Content -->
				
				<s:form action="PayrollEmployee" name="paginationForm" id="paginationForm" theme="simple" style="padding-left: 59px;">
							     <s:hidden name="empnamesearch"></s:hidden>
								<div class="row" style="margin-top:15px;">
									<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
										Total:<label class="text-info"><s:property value="totalRecords" /></label>
									</div>
									<s:include value="/common/pages/pagination.jsp"></s:include>
								</div>
							</s:form>
				<!-- Edit Employee Modal -->
				<div id="profile_info" class="modal custom-modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Update Profile Information</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<form action="updatePayrollEmployee" id="updateempfrm">
								<s:hidden name="cust_id"/>
								<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">First Name <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="firstname" id="firstname" >
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Last Name <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="lastname" id="lastname" >
											</div>
										</div>
										 <div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Address <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="permanentaddress" id="permanentaddress">
											</div>
										</div>
										<div class="col-sm-6" style="display: none;">
											<div class="form-group">
												<label class="col-form-label">Email <span class="text-danger">*</span></label>
												<input class="form-control" type="email" name="email" id="email">
											</div>
										</div>
										<div class="col-sm-6">  
											<div class="form-group">
												<label class="col-form-label">Joining Date <span class="text-danger">*</span></label>
												<div class="cal-icon"><input class="form-control datetimepicker" type="text" name="date_join" id="date_join"></div>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Mobile No. </label>
												<input class="form-control" type="text" name="mobNo" maxLength="10" id="mobNo">
											</div>
										</div>
										<div class="col-sm-6" style="display: none;">
											<div class="form-group">
												<label class="col-form-label">Height</label>
												<input class="form-control" type="text" name="height" id="height">
											</div>
										</div>
										<div class="col-md-6" style="display: none;">
											<div class="form-group">
												<label>Weight <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="weight" id="weight">
											</div>
										</div> 
										<div class="col-md-6">
											<div class="form-group">
												<label>Gender <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="gender" id="gender">
											</div>
										</div>
										<div class="col-md-6" style="display: none;">
											<div class="form-group">
												<label>Date of Birth <span class="text-danger">*</span></label>
												<input class="form-control datetimepicker" type="text" name="dob" id="dob">
											</div>
										</div>
										</div>
										<div class="submit-section">
										<!--  <button class="btn btn-primary submit-btn">Submit</button>  -->
										 <a href="#" onclick="updatecust()" class="btn btn-primary submit-btn" id="empreg">Update</a> 
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- /Edit Employee Modal -->
				
				<!-- Delete Employee Modal -->
				<div class="modal custom-modal fade" id="delete_employee" role="dialog">
					<div class="modal-dialog modal-dialog-centered">
						<div class="modal-content">
							<div class="modal-body">
								<div class="form-header">
									<h3>Delete Employee</h3>
									<p>Are you sure want to delete?</p>
								</div>
								<div class="modal-btn delete-action">
								<input type="hidden" id="cusuid">
									<div class="row">
										<div class="col-6">
											<a href="#" class="btn btn-primary continue-btn" onclick="deletedemp()">Delete</a>
										</div>
										<div class="col-6">
											<a href="javascript:void(0);" data-dismiss="modal" class="btn btn-primary cancel-btn">Cancel</a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- /Delete Employee Modal -->
				
				<div class="modal fade" id="smscountPopupid" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
					 <h4 class="modal-title" id="myModalLabel">Check SMS</h4>
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times; </span><span class="sr-only">Close</span>
						</button>
						<!-- <h4 class="modal-title" id="myModalLabel">Checked SMS</h4> -->
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
		</div>
				<!-- Add Employee Modal -->
				<div id="add_employee" class="modal custom-modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-centered modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Add Customer</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<form action="addPayrollEmployee" id="addemployeefrm">
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">First Name <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="firstname"  id ="firstname1" >
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Last Name <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="lastname"  id="lastname1" >
											</div>
										</div>
										 <div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Address</label>
												<input class="form-control" type="text" name="permanentaddress">
											</div>
										</div>
										<div class="col-sm-6" style="display: none;">
											<div class="form-group">
												<label class="col-form-label">Email <span class="text-danger">*</span></label>
												<input class="form-control" type="email" name="email" >
											</div>
										</div>
										<div class="col-sm-6">  
											<div class="form-group">
												<label class="col-form-label">Joining Date</label>
												<div class="cal-icon"><input class="form-control datetimepicker" type="text" name="date_join" ></div>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Mobile No. <span class="text-danger">*</span></label>
												<input class="form-control" type="text" name="mobNo" id="mobNo1" maxLength="10">
											</div>
										</div>
										<div class="col-sm-6" style="display: none;">
											<div class="form-group">
												<label class="col-form-label">Height</label>
												<input class="form-control" type="text" name="height">
											</div>
										</div>
										<div class="col-md-6" style="display: none;">
											<div class="form-group">
												<label>Weight </span></label>
												<input class="form-control" type="text" name="weight">
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label>Gender </label>
												<s:select id="gender" name="gender" list="{'Male','Female','Other'}"  theme="simple" cssClass="form-control showToolTip "
														data-toggle="tooltip" title="Select Gender"  headerKey="0" headerValue="Select"/>									
											</div>
										</div>
										<div class="col-md-6" style="display: none;">
											<div class="form-group">
												 <label>Date of Birth <span class="text-danger">*</span></label>
												<input class="form-control datetimepicker" type="text" name="dob" id="dob1">
											</div>
										</div>
									<div class="submit-section">
										<!--  <button class="btn btn-primary submit-btn">Submit</button>  -->
									 <a href="#" onclick="addempvalidate()" class="btn btn-primary submit-btn" id="empreg"style="margin-left: 260px">Submit</a>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- /Add Employee Modal -->
				<%-- <div id="smscountPopupid" class="modal custom-modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-centered modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Add Customer</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
							<label>Total SMS : <span id="tsms"></span></label></br>
						<label>Remaining SMS : <span id="rsms"></span></label></br>
						<label>Used SMS : <span id="usms"></span></label>
							</div>
						</div>
					</div>
				</div> --%>
				<!--  -->
				
				
				
		 <!-- Loader -->
			<div id="loader-wrapper">
				<div id="loader">
					<div class="loader-ellips">
					  <span class="loader-ellips__dot"></span>
					  <span class="loader-ellips__dot"></span>
					  <span class="loader-ellips__dot"></span>
					  <span class="loader-ellips__dot"></span>
					</div>
				</div>
			</div>
		<!-- Sidebar Overlay -->
		<div class="sidebar-overlay" data-reff=""></div>
		
		<!-- jQuery -->
        <script src="payrollnew/assets/js/jquery-3.2.1.min.js"></script>
		
		<!-- Bootstrap Core JS -->
        <script src="payrollnew/assets/js/popper.min.js"></script>
        <script src="payrollnew/assets/js/bootstrap.min.js"></script>
		
		<!-- Slimscroll JS -->
		<script src="payrollnew/assets/js/jquery.slimscroll.min.js"></script>
		
		<!-- Select2 JS -->
		<script src="payrollnew/assets/js/select2.min.js"></script>
		
		<!-- Datetimepicker JS -->
		<script src="payrollnew/assets/js/moment.min.js"></script>
		<script src="payrollnew/assets/js/bootstrap-datetimepicker.min.js"></script>
		
		<!-- Datatable JS -->
		<script src="payrollnew/assets/js/dataTables.bootstrap4.min.js"></script>
		
		<!-- Custom JS -->
		<%-- <script src="payrollnew/assets/js/app.js"></script> --%>
		 
    </body>
		<script type="text/javascript">
		
		 function employeesedit(val){
			   var url="editPayrollEmployee?selectedid="+val+"";
			   if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   

			    req.onreadystatechange = employeeseditRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);
			   
			}

			function employeeseditRequest(){
				if (req.readyState == 4) {
					if (req.status == 200) {
						var str=req.responseText;
						var data=str.split("~");
						  document.getElementById("cust_id").value=data[0];
						  document.getElementById("firstname").value=data[1];
						  document.getElementById("lastname").value=data[2];
						  document.getElementById("email").value=data[3];
						  document.getElementById("date_join").value=data[4];
						  document.getElementById("mobNo").value=data[5];
						  document.getElementById("height").value=data[6];
						  document.getElementById("weight").value=data[7];
						  document.getElementById("gender").value=data[8];
						  document.getElementById("permanentaddress").value=data[9];
						  document.getElementById("dob").value=data[10];
			 		}
			    }
			}
			
			

			function deleteemp(val) {
				document.getElementById("cusuid").value=val;
				$('#delete_employee').modal( "show" ); 
			}
			
			function deletedemp(){
				var val=document.getElementById("cusuid").value;
			var url = "deletePayrollEmployee?id="+val+"";
			if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = deletedempRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);
			}
			function deletedempRequest(){
			if (req.readyState == 4) {
				if (req.status == 200) {
					window.location.reload();
			   }
				}	 
			}

		</script>
		
		
	
		
</html>
