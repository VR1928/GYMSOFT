<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<script>


window.onload = function () {
	
	//document.getElementById("allemp").className="active";	
	
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
        <SCRIPT type="text/javascript" src="payroll/js/payrollmaster.js"></SCRIPT>
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
							<h4 class="page-title">Inventory</h4>
						</div>
						<div class="col-auto text-right float-right ml-auto m-b-30">
							<a href="#" class="btn add-btn" data-toggle="modal" data-target="#add_employee"><i class="fa fa-plus"></i> Add Equipment</a>
						</div>
					</div>
					<!-- /Page Title -->
					
					<!-- Search Filter -->
					
					<form action="showqualificationPayrollDepartment">
					<div class="row filter-row" >
							<div class="form-group form-focus" style="width: 20%">
								<s:textfield name="empnamesearch" cssClass="form-control floating"/>
								<label class="focus-label">Machine Id/Name</label>
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
											<th>Machine ID</th>
											<th>Name</th>
											<th>Weight</th>
											<th>Quantity</th>
											<th style="text-align: center;">Edit</th>
											<th style="text-align: center;">Delete</th>
										</tr>
									</thead>
									<tbody>
									<s:iterator value="qualificationlist">
										<tr>
										<td><%=i++ %></td>
											<td><s:property value="emp_id"/></td>
											<td>
											<s:property value="name"/>
											</td>
											<td><s:property value="weight"/></td>
											<td><s:property value="quantity"/></td>
											 <td style="text-align: center;">
														<a class="dropdown-item" href="#" data-toggle="modal" data-target="#profile_info" onclick="qualificationedit(<s:property value="id"/>)"><i class="fa fa-pencil m-r-5"></i> </a></td>
														<td style="text-align: center;">
														<a class="dropdown-item" href="#" data-toggle="modal" onclick="deletedept(<s:property value="id"/>)"><i class="fa fa-trash-o m-r-5"></i> </a>
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
				
				<%-- <s:form action="PayrollEmployee" name="paginationForm" id="paginationForm" theme="simple" style="padding-left: 59px;">
							     <s:hidden name="empnamesearch"></s:hidden>
								<div class="row" style="margin-top:15px;">
									<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
										Total:<label class="text-info"><s:property value="totalRecords" /></label>
									</div>
									<s:include value="/common/pages/pagination.jsp"></s:include>
								</div>
							</s:form> --%>
				<!-- Edit Employee Modal -->
				<div id="profile_info" class="modal custom-modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Update Equipment Details</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<form action="updatequalificationPayrollDepartment" id="updateempfrm">
								<s:hidden id="selectid" name="id"/>
								<div class="row">
								<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Machine Id</label>
												<input class="form-control" type="text" name="emp_id" id="emp_id">
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Name </label>
												<input class="form-control" type="text" name="name" id="name">
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Weight </label>
												<input class="form-control" type="text" name="weight" id="weight">
											</div>
										</div>
										 <div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Quantity</label>
												<input class="form-control" type="text" name="quantity" id="quantity">
											</div>
										</div>
										</div>
										<div class="submit-section">
										  <button class="btn btn-primary submit-btn">Submit</button>  
										<!--  <a href="#" onclick="updatecust()" class="btn btn-primary submit-btn" id="empreg">Update</a>  -->
									</div>
								</form>
							</div>
						</div>
					</div>
					
				</div>
				<!-- /Edit Employee Modal -->
				
				<!-- Delete Employee Modal -->
				<div class="modal custom-modal fade" id="delete_qualification" role="dialog">
					<div class="modal-dialog modal-dialog-centered">
						<div class="modal-content">
							<div class="modal-body">
								<div class="form-header">
									<h3>Delete Entry</h3>
									<p>Are you sure want to delete?</p>
								</div>
								<div class="modal-btn delete-action">
								<input type="hidden" id="qualificid">
									<div class="row">
										<div class="col-6">
											<a href="#" class="btn btn-primary continue-btn" onclick="deletequalification()">Delete</a>
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
				
				<!-- Add Employee Modal -->
				<div id="add_employee" class="modal custom-modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-centered modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Add Equipment</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<form action="addqualificationPayrollDepartment" id="addemployeefrm">
									<div class="row">
									<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Machine Id</label>
												<input class="form-control" type="text" name="emp_id">
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Name </label>
												<input class="form-control" type="text" name="name">
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Weight </label>
												<input class="form-control" type="text" name="weight">
											</div>
										</div>
										 <div class="col-sm-6">
											<div class="form-group">
												<label class="col-form-label">Quantity</label>
												<input class="form-control" type="text" name="quantity">
											</div>
										</div>
										 <div class="col-sm-6">
											<div class="form-group">
											</div>
											</div>
									<div class="submit-section">
										  <button class="btn btn-primary submit-btn">Submit</button>
									<!--  <a href="#" onclick="addempvalidate()" class="btn btn-primary submit-btn" id="empreg"style="margin-left: 260px">Submit</a> -->
									</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- /Add Employee Modal -->
				
				
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
		
		function qualificationedit(val){
			   var url="editqualificationPayrollDepartment?selectedid="+val+"";
			   if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   

			    req.onreadystatechange = qualificationeditRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);
			   
			}

			function qualificationeditRequest(){
				if (req.readyState == 4) {
					if (req.status == 200) {
						var str=req.responseText;
						
						var data=str.split("##");
						  document.getElementById("selectid").value=data[0];
						  document.getElementById("name").value=data[1];
						  document.getElementById("weight").value=data[2];
						  document.getElementById("quantity").value=data[3];
						  document.getElementById("emp_id").value=data[4];
			 		}
			    }
			}
			
			

			function deletedept(val) {
				document.getElementById("qualificid").value=val;
				$('#delete_qualification').modal( "show" ); 
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
