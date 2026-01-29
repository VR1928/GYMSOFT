<%@page import="com.apm.common.utils.DateTimeUtils"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%--  <script type="text/javascript"
	src="diarymanagement/js/nonavailableslot.js"></script>  --%>
	<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<link href="common/css/Style.css" rel="stylesheet" type="text/css" /> 
	 <link href="common/css/responsive.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript" src="common/js/pagination.js"></script>
<script type="text/javascript" src="diarymanagement/js/manageclient.js"></script>
    <script type="text/javascript" src="diarymanagement/js/otnotes.js"></script>
    
    
    
<script type="text/javascript" src="accounts/js/commonaddcharge.js"></script> 
<script type="text/javascript" src="emr/js/addInvestigation.js"></script> 
<script type="text/javascript" src="emr/js/viewinvestigation.js"></script> 
 <script type="text/javascript" src="diarymanagement/js/sendsms.js"></script>
  <script type="text/javascript" src="diarymanagement/js/sendApmtAttachnment.js"></script>
   <script type="text/javascript" src="common/js/pagination.js"></script>
   <script type="text/javascript" src="thirdParties/js/nicEdit.js"></script>
   
	   <script type="text/javascript" src="tools/js/sendLetter.js"></script> 
	    <script type="text/javascript" src="thirdParties/js/nicEdit.js"></script>
	    <script type="text/javascript" src="diarymanagement/js/newinvestigationclient.js"></script>

<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>
<%LoginInfo loginfo = LoginHelper.getLoginInfo(request); %>

<style>
    .disabled {
    z-index: 1000 !important;
    background-color: lightgrey !important;
    opacity: 0.6 !important;
    pointer-events: none !important;
}
	.thumbnail{
		 height: 82px !important;

	}
	.micimg{
	float: left;
    width: 7%;
}
td {
	font-size: small !important;
}
</style>
  <script>
   $(document).ready(function () {
	    $(".disblebtnone").on("click", function() {
	        $(this).attr("disabled", "disabled");
	        doWork();
	    });
	    
	    $(".editadmndate").datepicker({

    		dateFormat : 'dd-mm-yy',
    		yearRange: yearrange,
    		minDate : '30-12-1880',
    		changeMonth : true,
    		changeYear : true
    	});
	});
   </script>




<script>





function startConverting1(element) {

   var abc=element.src.split('/');
   
     var right = "cicon/mic_off.png";
         var left = "cicon/mic.png";
         element.src = element.bln ? right : left;
         element.bln = !element.bln;
         
       //  document.getElementById("otnotes").value=localStorage.getItem("xx");
   if(abc[5]=="mic_off.png")
   {
    startConvertingsearchp();
   }
   else{
   //var textvalue=document.getElementById("otnotes").value;
  // localStorage.setItem("xx",textvalue);
   location.reload();
   }
}


function searchbyipd(){
	document.getElementById('clientFrm').submit();
}
</script>




<div class="">
							<div class="">
								<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
								<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 oneseticonleft">
									<img src="dashboardicon/clints.png" class="img-responsive prescripiconcircle">
								</div>
								<div class="col-lg-11 col-md-11 col-sm-11 col-xs-11 titlestleftiocn">
								<h4><%=loginfo.getPatientname_field() %> Panel</h4>
								</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										<div>
											<div class="row">
	<div class="col-lg-12">
		<s:hidden name = "message" id = "message"></s:hidden>
	<s:if test="hasActionMessages()">
	<script>
		var msg = " " + document.getElementById('message').value;
		showGrowl('', msg, 'success', 'fa fa-check');
		</script>
	</s:if>
	</div>
</div>
<div>
<%-- <div class="row">
	<!-- /.col-lg-6 -->
	
		<s:form action="searchClient" theme="simple" id = "clientFrm">
			<div class="col-lg-1 col-md-1">
			<label>show all</label>
			<s:checkbox name="showAll" id="showAll" onclick="sortByPractitioner(this.value)" title="Show all booked and non booked Appointment client list"/>
			</div>
		
			<div class="col-lg-2 col-md-2">
			
			
			<s:select name="status"
				list="#{'1':'Active','0':'Inactive'}"
				cssClass="form-control" headerKey="All" headerValue="All" onchange="showByStatus()"></s:select>
			</div>
			<div class="col-lg-5 col-md-5">
			<div class="input-group">
			
				<s:textfield theme="simple" name="searchText"
					placeholder="Search By First name, Last name, Mobile No,DOB,Post Code"  cssClass="form-control" />
				<span class="input-group-btn"> <input type="submit"
					value="Go" class="btn btn-primary" />
				</span>
			</div>
			</div>
			
				<div class="col-lg-2 col-md-2">
				
					<s:select name="diaryUser" list="userList" listKey="id" listValue="diaryUser" cssClass="form-control" headerKey="0" headerValue="Select Practitioner" theme="simple" onchange = "sortByPractitioner(this.value)" ></s:select>
							
				</div>
			
		</s:form>
		<!-- /input-group -->
	

	
	<!-- /.col-lg-6 -->
	<div class="col-lg-2 col-md-2">
		<a href="addCompleteInfoClient" class="btn btn-primary"
			style="text-decoration: none"><i class="fa fa-user"></i> Add New
			Client</a>

	</div>
</div>
<input type= "hidden" id = "commencing">
<!-- /.row -->
<br />
<s:hidden name="message" id="message"></s:hidden>
<s:if test="hasActionMessages()">
	<script>
		var msg = " " + document.getElementById('message').value;
		showGrowl('', msg, 'success', 'fa fa-check');
		
	</script>
	
</s:if>
<div>

	<input type="hidden" id="client" name="client"> <input
		type="hidden" id="clientId" name="clientId">
<div class="row" style="padding-left: 15px;padding-right: 15px"> --%>
		
		
		
	<%-- 	<div class="col-lg-12">
	<div class="table-responsive">
		<table class="table table-striped table-condensed table-hover table-bordered">
			<thead>
				<tr>
					<th><i class="fa fa-user"></i> Id / old Client Id</th>
					<!--  <th id = "hideLink">Name <a href="#" onclick="hideRow('<s:property value = "allPatientList.size"/>')">Hide</a> </th> 
					-->
					
					<th id = "hideLink" class="sortable <s:if test="#attr.pagination.sortColumn.equals('firstname')">sorted <s:property value="#attr.pagination.sortClass"/> </s:if>">
						<a href="#" onclick="fnPagination(6,'firstname');"><i class="fa fa-user"></i> Name</a> <a href="#" onclick="hideRow('<s:property value = "allPatientList.size"/>')">Hide</a></th>
					
					<th> <i class="fa fa-mobile-phone"></i> Mobile No</th>
					<th><i class="fa fa-envelope"></i> Email</th>
                              <th><i class="fa fa-map-marker"></i> Post Code</th>
                              <th><i class="fa fa-calendar"></i> DOB</th>
                             <!--  <th><i class="fa fa-user"></i> Old Client ID</th> -->
                              <th><i class="fa fa-file"></i> Note</th>
                              <th><i class="fa fa-calendar"></i> Last Modified</th>
					
					<th><i class="fa fa-edit"></i>Edit</th>
					<th><i class="fa fa-trash-o"></i>Delete</th>
					<th><i class="fa fa-print"></i>Print</th>
					<th><i class=" fa fa-edit"></i> Status</th>
					<th>Treatment Episode</th>
					
					
					
				</tr>
			</thead>
			<tbody> --%>
			
			
			  <div class="col-md-12 col-xs-12 col-lg-12 col-sm-12">
                      <div class="row">
                        	<table class="table table-hover table-bordered">
                              <div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 clintbot">
                                  <div class="row">
                                   <s:form action="searchClient" theme="simple" id = "clientFrm">
                                   	<input type= "hidden" id = "commencing">
									<s:hidden name="message" id="message"></s:hidden>
									<input type="hidden" id="client" name="client"> 
									<input type="hidden" id="clientId" name="clientId">
		
                                      <div class="col-lg-12 col-md-12 col-xs-12 clientsearchbak">
                                      	<div class="row">
                                         	<div class="col-lg-4 col-md-4 col-sm-4">
                                              
                                                	<div class="input-group">
				<s:textfield theme="simple" name="searchText" id="adharsearch" placeholder="Search by Addhar No, Name, Pincode"  cssClass="form-control" />
				<span class="input-group-btn"> 
					<input type="submit" value="Go" class="btn btn-primary" />
					<a href="#" class="btn btn-primary" style="margin-left: 5px;"><i class="fa fa-refresh" aria-hidden="true"></i></a>
					
				</span>
			</div>
			<div style="margin-top: 9px;">
				<img src="cicon/mic_off.png" class="img-responsive micimg" onclick="startConverting1(this)" title="Microphone" id="changer"></img> &nbsp; Search By Voice Over &nbsp; | &nbsp; <s:checkbox  name="showAll" id="showAll" onclick="sortByPractitioner(this.value)" title="Show all booked and non booked Appointment client list"/>
                                                 <span>Select to view all patients.</span>
                                               
			</div>
			
			 
                                              </div>
                                              <div class="col-lg-1 col-md-2 col-sm-2">
                                                 <s:select name="status"
													list="#{'1':'Active','0':'Inactive'}"
													cssClass="form-control" headerKey="All" headerValue="All" onchange="showByStatus()"></s:select>
                                              </div>
                                              <div class="col-lg-2 col-md-2 col-sm-2">
                                               <s:if test="showAll==true">
                                              		 <s:select name="diaryUser" list="userList" listKey="id" listValue="diaryUser" cssClass="form-control" headerKey="0" headerValue="Select " theme="simple" onchange = "sortByPractitioner(this.value)" disabled="true" ></s:select>
                                              </s:if>
                                              <s:else>
                                              		 <s:select name="diaryUser" list="userList" listKey="id" listValue="diaryUser" cssClass="form-control" headerKey="0" headerValue="Select " theme="simple" onchange = "sortByPractitioner(this.value)" ></s:select>
                                              </s:else>
                                               <br><br>
                                               <%if(loginfo.getIskunal()==1){ %>
                                               <div class='form-inline'>
                                               <%String manualipdseq=DateTimeUtils.isNull((String)request.getAttribute("manualipdseq")); %>
                                               <input type="text" class='form-control' name='manualipdseq'   value="<%=manualipdseq %>" placeholder="Search By Ipd " style="width: 60% ">&nbsp;&nbsp; 
                                               <input type="button" class='btn btn-primary' value='search' onclick="searchbyipd()">
                                               </div>
                                               <%} %>
                                              </div>
                                              <div class="col-lg-5 col-md-4 col-sm-4" style="margin-top:-10px;">
                                                <div class="col-xs-3 col-md-2 panthumb" >
                                                <%if(loginfo.isClient_add()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openPopup('addCompleteInfoClient')">
                                                          <img src="cicon/Add_client.png" alt="Add Clients" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Add <%=loginfo.getPatientname_field() %></p>
                                                          </div>
                                                          </a>
                                                           </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/Add_client.png" alt="Add Clients" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Add <%=loginfo.getPatientname_field() %></p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
 													<div class="col-xs-3 col-md-2 panthumb">
 													<%if(loginfo.isClient_forms()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openAssesmentFormPopup('0','0','0')">
                                                          <img src="cicon/assesments_form.png" alt="assesments_form" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Forms</p>
                                                          </div>
                                                          </a>
                                                           </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          	<img src="cicon/assesments_form.png" alt="assesments_form" style="width: 45px;">
	                                                         <div class="caption">
	                                                              <p class="textclinet">Forms</p>
	                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                  <div class="col-xs-3 col-md-2 panthumb" >
                                                  <%if(loginfo.isClient_emr()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openemrhere()">
                                                          <img src="cicon/emr.png" alt="Email" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">EMR</p>
                                                          </div>
                                                         </a>
                                                           </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          	 <img src="cicon/emr.png" alt="Email" style="width: 45px;">
	                                                          <div class="caption">
	                                                              <p class="textclinet">EMR</p>
	                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
												<div class="col-xs-3 col-md-2 panthumb">
												<%if(loginfo.isClient_discharge()){ %>
                                                     <div class="thumbnail">
                                                     <a href="#" onclick="opencPopup('InitialDischarge')">
                                                         <img src="cicon/dischargebed.png" alt="view_Account" style="width: 45px;">
                                                         <div class="caption">
                                                             <p class="textclinet">Discharge</p>
                                                         </div>
                                                        </a>
                                                         </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          	 <img src="cicon/dischargebed.png" alt="view_Account" style="width: 45px;">
	                                                         <div class="caption">
	                                                             <p class="textclinet">Discharge</p>
	                                                         </div>
                                                     </div>
                                                     <%} %>
                                                 </div>
                                                 <!-- feedback -->
                                                     <div class="col-xs-3 col-md-2 panthumb" >
                                                       <div class="thumbnail">
                                                       
                                                        <a href="#" onclick="openPopup('feedbackFormClient')">
                                                         <img src="cicon/feedback.png" alt="view_Account" style="width: 45px;">
                                                         <div class="caption">
                                                             <p class="textclinet">FeedBack</p>
                                                         </div>
                                                        </a>
                                                       
                                                       </div>
                                                     </div>
                                                      <!-- follow up -->
                                                     <div class="col-xs-3 col-md-2 panthumb" >
                                                       <div class="thumbnail">
                                                       
                                                        <a href="#" onclick="openPopup('followupdashboardClient')">
                                                         <img src="cicon/followup.png" alt="view_Account" style="width: 45px;">
                                                         <div class="caption">
                                                             <p class="textclinet">Follow Up</p>
                                                         </div>
                                                        </a>
                                                       
                                                       </div>
                                                     </div>
                                              </div>
                                          </div><br />
                                      </div>
                                     </s:form> 
                                     
                                     
                                  </div>
                              </div><br />

                              <div class="">
                                 
                                  
                                  
                                  
                                 
                                      <thead>
                                      	
                                      	<tr>
					<th>Client Id / UHID</th>
					<!--  <th id = "hideLink">Name <a href="#" onclick="hideRow('<s:property value = "allPatientList.size"/>')">Hide</a> </th> 
					-->
					
					<th>Adhar No</th>
					
					<th id = "hideLink" class="sortable <s:if test="#attr.pagination.sortColumn.equals('firstname')">sorted <s:property value="#attr.pagination.sortClass"/> </s:if>">
						Name <a href="#" onclick="hideRow('<s:property value = "allPatientList.size"/>')" style="color: yellow;display:none;">Hide</a></th>
					 <th>Age</th>
					<th>Mobile No</th>
					<th>Email</th>
                              <th>Pin Code</th>
                             
                             <!--  <th><i class="fa fa-user"></i> Old Client ID</th> -->
                            <!--   <th>Note</th> -->
                              <th>Last Modified</th>
					<!-- <th><i class="fa fa-print"></i>Print</th> -->
					<th>Status</th>
					<th>Treatment Episode</th>
					<th>Barcode</th>
					<th>Label</th>
					<th>Edit</th>
					<th style="text-align: center;">Auto Charge</th>
					<th class="hidden">Delete</th>
				</tr>
                                      
                                      </thead>
			
			
			
			<%int count =1;%>
				<s:if test="allPatientList.size!=0">
					<s:iterator value="allPatientList" status="rowstatus">
						<tr id="<s:property value="id" />" >

							<%-- <s:if test="oldclientId>0">
								<td>0000<s:property value="id" /> / <s:property value="oldclientId" /> </td>
							 </s:if>
							 <s:else>
							 	<td>0000<s:property value="id" /> / <s:property value="abrivationid"/></td>
							 </s:else>
							 	 --%>
							<td><s:property value="id" /> / <s:property value="abrivationid"/></td> 	 
							<td><s:property value="adhno" /></td>
							<td><span style="cursor: pointer;" onclick="setSelectedRows(<s:property value="id" />,'<s:property value="abrivationid"/>','<s:property value="firstName"/>/<s:property value="lastName"/>','<s:property value="age1"/>','<s:property value="gender"/>')" id = "nametd<%=count%>"><s:property value="title" /> <s:property value="firstName" /> <s:property value="middlename" />  <s:property value="lastName" /></span> 
										<s:if test="ipdList.size>0">
											<i title="View Ipd Details" class="fa fa-arrow-down" style="cursor: pointer;" onclick="showhideClientIpdDetails(<s:property value="id"/>)"></i>							</s:if>
									</td>	
							<td><s:property value="age1" /></td>
							<td><s:property value="mobNo" /></td>
							<td><span id = "emailtd<%=count%>"><s:property value="email" /></span></td>
							<td><s:property value="postCode" /></td>
							
							
							
							<%-- <td><s:property value="oldclientId" /></td> --%>
							<%-- <s:if test = "note == null || note == '' " >
									<td id = "td<s:property value="id" />"><a title = 'Save' onclick="addNote('<s:property value="id" />')" href="#"><i class="fa fa-plus"></i>Save</a>&nbsp;&nbsp;
									<s:textfield name = "note" id = "note%{id}"  title="Enter Note"
											cssClass="form-control showToolTip" data-toggle="tooltip"
											placeholder="Enter Note" theme="simple" maxlength="50"></s:textfield>									
											</td>
							</s:if>
							<s:else>
								<td id = "td<s:property value="id" />">
								<div style="height: 40px;overflow: auto;width: 160px" >
								<a title = "Edit" onclick="editNote('<s:property value="id" />')" href="#"><i class="fa fa-edit"></i></a>&nbsp;&nbsp; <a title = 'Delete' onclick="deleteNote('<s:property value="id" />')" href="#"><i class="fa fa-trash-o"></i></a><br>
								<s:property value="note" />
								</div>
							</td>
							</s:else>
							
 --%>	
 						<%-- <td style="width: 160px;"><span style="cursor:pointer" onclick="showAllNOte('<s:property value="clientNote"/>')"><s:property value="note" /></span></td> --%>
 						
 						<td><s:property value="lastModified" /></td>
							
						
							
							
							<s:url action="printProfileClient" id="view">
								<s:param name="selectedid" value="%{id}"></s:param>
							</s:url>
						<%-- 	<td class="text-center"><s:a href="%{view}" target = "black">
									<i class="fa fa-print"></i>
							</s:a></td> --%>
								
							<td style="text-align: center;">	
							
							<s:if test="status==1">
								<a href="#" onclick="changeStatusToInActive('<s:property value = "id" />')"> <img src="common/img/active.png" style="width: 50px;"> </a>
							</s:if>
							<s:else>
								<a href="#" onclick="changeStatusToActive('<s:property value = "id" />')"> <img  src="common/img/inactive.png" style="width: 50px;"> </a>
							</s:else>

							</td>	
							<s:hidden id="changeStatus%{id}" name = "status"></s:hidden>
							<td style="text-align: center;"><a href ="#" onclick="createTreatmentEpisode('<s:property value = "id" />','<s:property value="title" /> <s:property value="firstName" /> <s:property
									value="lastName" />','<s:property value = "treatmentType"/>','<s:property value="policyNo"/>','<s:property value="whopay"/>','<s:property value="thirdPartyCompanyName"/>')"> Create</a></td>
									
							<td><a href="#" onclick="openPopup('barcodeIpd?selectedid=<s:property value="id"/>')">Barcode</a></td>
							<td><a href="#" onclick="openPopup('patientlabelIpd?selectedid=<s:property value="id"/>')">Label</a></td>
							<s:hidden value="%{id}" name="id"></s:hidden>
							<s:url action="editClient" id="edit">
								<s:param name="selectedid" value="%{id}"></s:param>
							</s:url>
							<td style="cursor: pointer;" class="text-center">
							<% if(loginfo.isClient_edit()){%>
							
							<s:a href="%{edit}" title = "Edit">
									<i class="fa fa-edit"></i>
								</s:a>
								<%} %>
							</td>
							<td style="text-align: center;">
							<s:if test="isautocharge==1">
								<input type="checkbox" onclick="setAutochargeClient(<s:property value="id"/>,this.checked)" checked="checked" >
							</s:if>
							<s:else>
								<input type="checkbox" onclick="setAutochargeClient(<s:property value="id"/>,this.checked)" >
							</s:else>
								
							</td>
							<s:url action="deleteClient" id="delete">
								<s:param name="selectedid" value="%{id}"></s:param>
							</s:url>
							<td style="cursor: pointer;" class="text-center hidden"><s:a href="%{delete}" title ="Delete"
									onclick="return confirmedDelete()" cssClass="text-danger">
									<i class="fa fa-trash-o"></i>
								</s:a></td>
						</tr>
						
						<tr id="ipd<s:property value="id"/>" style="display: none;">
							<td colspan="14">
								<table class="table table-striped table-bordered table-responsive">
									<thead>
										<tr>
											<th>Admission ID </th>
											<th>Admission Date / Time</th>
											<th></th>
											<th>Discharge Date / Time</th>
											<th>Discharge Print / Form</th>
											
											<th>View Account</th>
											<th>label</th>
											
										</tr>
									</thead>
									<tbody>
										<s:iterator value="ipdList">
											<tr>
												<td><a href="#" onclick="opencPopup('printIpd?selectedid=<s:property value="addmissionid"/>')"><s:property value="ipdseqno"/></a></td>
												<td><s:property value="admissiondate"/></td>
												<s:if test="cancel==1">
												    <td>Cancelled (<s:property value="cancelnote"/>) by <s:property value="cancelUser"/></td>
												</s:if>
												<s:else>
												 	<td></td>
												</s:else>
												
												<td><s:property value="dischargeDate"/></td>
												<td><a href="#" onclick="opencPopup('printdischargeCommonnew?selectedid=<s:property value="addmissionid"/>&clientid=<s:property value="clientid"/>')"><i class='fa fa-print' title="Print Discharge Card"></i></a>
												 / <a href="#" onclick="opencPopup('dischargeCommonnew?selectedid=<s:property value="addmissionid"/>&clientid=<s:property value="clientid"/>')"><i class='fa fa-file' title="Open Discharge Card"></i></a>
			                                    </td>
												<td><a href="#" onclick="opencPopup('Statement?clientId=<s:property value="clientid"/>&ipdidnew=<s:property value="addmissionid"/>')"><i class='fa fa-line-chart' title="Open Accounts"></i></a></td>	
												<td><a href="#" onclick="openPopup('patientlabelIpd?labelipdid=<s:property value="addmissionid"/>&selectedid=<s:property value="clientid"/>')">Label</a></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</td>
						</tr>
					
					<%count = count+1;%>
					</s:iterator>
				</s:if>
				<s:else>
						There is no Client found!!
					</s:else>
			</tbody>
		</table>
	</div>

</div></div>


	<s:form action="moveClient" name="paginationForm" id="paginationForm"
		theme="simple">
		<s:hidden name="showAll"/>
		<s:hidden name="status"></s:hidden>
		<div class="col-lg-12 col-md-12" style="padding:0px;margin-top:15px;">
			<div class="col-lg-4 col-md-4 col-sm-4 hidden-xs text-left" style="padding:0px;">
				Total:<b class="text-info"><s:property value="totalRecords" /></b>
			</div>
			<%@ include file="/common/pages/pagination.jsp"%>
		</div>




	</s:form>

</div>


<!-- Patient Panel Modal -->
<div class="modal fade" id="clickicon" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><%=loginfo.getPatientname_field() %> Panel -<label id="pname"></label></h4>
      </div>
      <!-- <div class="modal-body" style="min-height: 179px;"> -->
       <div class="modal-body" style="min-height: 268px;">
       <div class="col-lg-12 borderdesing">
                                              <div class="row">
                                                  
                                                 <div class="col-xs-2 col-md-2 panthumb" >
                                                 <%if(loginfo.isClient_emai()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openprintletter()">
                                                          <img src="cicon/email.png" alt="Email" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Email/Letter</p>
                                                          </div>
                                                         </a>
                                                          </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		<img src="cicon/email.png" alt="Email" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Email/Letter</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                 <div class="col-xs-2 col-md-2 panthumb" >
                                                  <%if(loginfo.isClient_print()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="opendislayprofile()">
                                                          <img src="cicon/print.png" alt="Email" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Print Record</p>
                                                          </div>
                                                          </a>
                                                           </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		  <img src="cicon/print.png" alt="Email" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Print Record</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                 
                                                <div class="col-xs-2 col-md-2 panthumb" >
                                                <%if(loginfo.isClient_emai()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="opentreatmentpopup()">
                                                          <img src="cicon/treatment.png" alt="Email" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Treatment Episode</p>
                                                          </div>
                                                          </a>
                                                            </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/treatment.png" alt="Email" style="width: 45px;">
		                                                         <div class="caption">
		                                                              <p class="textclinet">Treatment Episode</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                               <div class="col-xs-2 col-md-2 panthumb" >
                                                      <div class="thumbnail">
                                                      	 <a href="#" onclick="openappointmentopopup()">
                                                          <img src="cicon/appointment.png" alt="Email" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Appointment</p>
                                                          </div>
                                                          </a>
                                                      </div>
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                   <%if(loginfo.isClient_treatment()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openlogpopup()">
                                                          <img src="cicon/log.png" alt="log" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Log</p>
                                                          </div>
                                                          </a>
                                                            </div>
                                                          <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/log.png" alt="log" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Log</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                  <%if(loginfo.isClient_recordpayment()){ %>
                                                      <div class="thumbnail">
                                                      	<a href="#" onclick="openrecordpaymentpopup()">
                                                          <img src="cicon/cash_desk.png" alt="record_payment" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Record Payment</p>
                                                          </div>
                                                          </a>
                                                           </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/cash_desk.png" alt="record_payment" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Record Payment</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>

                                              </div>
                                              <div class="row">
                                                  
                                                  
                                                 
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                   <%if(loginfo.isClient_cashdesk()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openaccountpopup()">
                                                          <img src="cicon/cash_desk.png" alt="cash_desk" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Cash Desk</p>
                                                          </div>
                                                          </a>
                                                           </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/cash_desk.png" alt="cash_desk" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Cash Desk</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                      <div class="thumbnail">
                                                      	<a href="#"  onclick="openadvrefundpopup()">
                                                          <img src="cicon/raise_credit.png" alt="raise_credit" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Advance & Refund</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  
                                                    <div class="col-xs-2 col-md-2 panthumb">
                                                    <%if(loginfo.isClient_addcharge()){ %>
                                                      <div class="thumbnail">
                                                      	<a href="#"  onclick="openaddchargepopup()">
                                                          <img src="cicon/raise_credit.png" alt="raise_credit" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Add Charge</p>
                                                          </div>
                                                         </a>
                                                           </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		 <img src="cicon/raise_credit.png" alt="raise_credit" style="width: 45px;">
		                                                         <div class="caption">
		                                                              <p class="textclinet">Add Charge</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                  
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                  <%if(loginfo.isClient_viewaccount()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openaccount()">
                                                          <img src="cicon/view_ccount.png" alt="view_Account" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">View Account</p>
                                                          </div>
                                                         </a>
                                                          </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		  <img src="cicon/view_ccount.png" alt="view_Account" style="width: 45px;">
			                                                          <div class="caption">
			                                                              <p class="textclinet">View Account</p>
			                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb" >
                                                   <%if(loginfo.isClient_emr()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openemrhere()">
                                                          <img src="cicon/emr.png" alt="Email" style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">EMR</p>
                                                          </div>
                                                         </a>
                                                          </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		  <img src="cicon/emr.png" alt="Email" style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">EMR</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div> 
                                                   <div class="col-xs-2 col-md-2 panthumb" >
                                                   <%if(loginfo.isIpd_declaration()){ %>
                                                      <div class="thumbnail">
                                                      <a href="#" onclick="openClientDeclarationPrintPopup()">
                                                          <img src="popicons/eye.png" alt="..." style="width: 45px;">
                                                          <div class="caption">
                                                              <p class="textclinet">Declaration</p>
                                                          </div>
                                                         </a>
                                                          </div>
                                                           <%}else{ %>
                                                          <div class="thumbnail disabled ">
                                                          		  <img src="popicons/eye.png" alt="..." style="width: 45px;">
		                                                          <div class="caption">
		                                                              <p class="textclinet">Declaration</p>
		                                                          </div>
									    					
                                                      </div>
                                                      <%} %>
                                                  </div> 
                                              </div>
                                              
                                              <div class="row">
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                  	 <div class="thumbnail">
                                                      	<a href="#"  onclick="opennewimmunizationchartpopup()">
                                                        <img src="cicon/pregnancysm.png" alt="raise_credit" style="width: 45px;">  
                                                         <div class="caption">
                                                              <p class="textclinet">Immunization Chart</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                      	<div class="thumbnail">
                                                      	<a href="#"  onclick="openheadcircumferencechartpopup()">
                                                        <img src="cicon/ChildGrowthChart.png" alt="raise_credit" style="width: 45px;"> 
                                                         <div class="caption">
                                                              <p class="textclinet">Child Growth Chart</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail">
                                                      	<a href="#"  onclick="opennewimmunizationchartpopup1()">
                                                        <img src="cicon/Anc_icon.png" alt="raise_credit" style="width: 45px;">  
                                                         <div class="caption">
                                                              <p class="textclinet">Antenatal Schedule</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail">
                                                      	<a href="#"  onclick="openInvestigationdash()">
                                                        <img src="cicon/invst.png" alt="raise_credit" style="width: 45px;">  
                                                         <div class="caption">
                                                              <p class="textclinet">View Investigation</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                   <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail" style="text-align: center;">
                                                      	<a href="#"  onclick="generatePatientbarcode()">
                                                        <!-- <img src="cicon/invst.png" alt="raise_credit" style="width: 45px;">   -->
                                                        <span style="text-align:center"><i class='fa fa-barcode fa-3x' aria-hidden="true" style="color: #333"></i></span>
                                                         <div class="caption">
                                                              <p class="textclinet">Barcode</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  
                                                       <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail" style="text-align: center;">
                                                      	<a href="#"  onclick="opennephrologyvacc()">
                                                        <!-- <img src="cicon/invst.png" alt="raise_credit" style="width: 45px;">   -->
                                                        <span style="text-align:center"><i class='fa fa-drupal fa-3x' aria-hidden="true" style="color: #333"></i></span>
                                                         <div class="caption">
                                                              <p class="textclinet">Nephrology Vaccination</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  <%if(loginfo.getIskunal()==1){ %>
                                                    <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail">
                                                      	<a href="#"  onclick="editdisdate()">
                                                       <img src="cicon/log.png" alt="Change Admission Date" style="width: 45px;">
                                                         <div class="caption">
                                                              <p class="textclinet">Change Admission Date</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div>
                                                  <%} %>
                                                   <!--   <div class="col-xs-2 col-md-2 panthumb">
                                                   <div class="thumbnail">
                                                      	<a href="#"  onclick="openInvestigationdash1()">
                                                        <img src="cicon/invst.png" alt="raise_credit" style="width: 45px;">  
                                                         <div class="caption">
                                                              <p class="textclinet">Give Investigation</p>
                                                          </div>
                                                         </a>
                                                      </div>
                                                  </div> -->
                                                  <div class="col-xs-2 col-md-2 panthumb">
                                                 
                                                  </div>
                                                  <div class="col-xs-2 col-md-2 panthumb" >
                                                  
                                                  </div> 
                                                  <div class="col-xs-2 col-md-2 panthumb" >
                                                  
                                                  </div> 
                                              </div>
                                              
                                          </div>
      </div>
      <div class="modal-footer hidden">
        <button type="button" class="btn btn-default hidden" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary hidden">Save changes</button>
      </div>
    </div>
  </div>
</div>

	<!-- add invesgtigation Modal -->
	<input type="hidden" id="invchargeinfodata"/>
		<input type="hidden" id="invstdateandtime" value="Today"/>
	 <div class="modal fade" id="investigationpopup" tabindex="-1" role="dialog"
	aria-labelledby="lblsemdsmspopup" aria-hidden="true" data-keyboard="false" data-backdrop="static">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button  type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h5 class="modal-title" id="">Create Investigation For <b class="pname" id="invstcmyModalLabel">NAME: </b></h5>
				</div>
				<div class="modal-body" id="investi">
						
						
					<%@ include file="/emr/pages/addInvestigation.jsp" %>
				    
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						onclick="saveViewInvestigation()">Save</button>
						
						<!-- <button type="button" class="btn btn-primary"
						onclick="insertEmrPriscription(1)">Save & Print</button> -->

					<button  type="button" class="btn btn-primary hidden" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	

<!-- Add Treatment Episode -->
	<div class="modal fade" id="showallnotepopupdiv" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"><%=loginfo.getPatientname_field() %> Note</h4>
				</div>
				<div class="modal-body">
					<textarea rows="8" cols="80" readonly="readonly" name="cnote" id="cnote"></textarea>
				</div>
				<div class="modal-footer">

					<!-- <button type="button" class="btn btn-primary"
						onclick="saveTreatment();">Save</button> -->
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	

<!-- Add Treatment Episode -->
	<div class="modal fade" id="addTreatmentEpisodeDiv" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create Treatment
						Episode</h4>
				</div>
				<div class="modal-body">
					<%@ include file="/treatmentEpisode/pages/addTreatmentEpisode.jsp"%>
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-primary disblebtnone"
						onclick="saveTreatment();">Save</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Create Treatment Episode or Not -->
	<div class="modal fade" id="tratementAddDiv" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create Treatment
						Episode</h4>
				</div>
				<div class="modal-body">
					<label>New client has been added successfully. Do you want to create treatment episode?</label>
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-primary"
						onclick="getCurrentClientDetails();" data-dismiss="modal">Yes</button>
					<button type="button" class="btn btn-primary"
						onclick="redirectToDashboard()" data-dismiss="modal">No</button>	
				</div>
			</div>
		</div>
	</div>
	<div id="editadmndatemodal" class="modal fade" role="dialog">
  <div class="modal-dialog modal-sm">
   
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" style="text-align: center;">Change Admission Date</h4>
      </div>
      
      	<div class="modal-body">
     		<div class="col-lg-12 col-md-12 col-sm-12" style="padding: 0px;">
     		
     		<label >Edit Date : </label>
     		<input type="text" name='editadmndate' id='editadmndate' class="form-control editadmndate" style="" readonly>
     		<br><br>
     		  <div class="form-group">
				    <label for="exampleInputName2">HH :</label>
								
									    <s:select name="hour" cssStyle="width: 30%" id="hour" list="hourList" cssClass="form-control" headerKey="0" headerValue="Select" />
									 
					<label for="exampleInputName2">MM :</label>				  
									
									     <s:select name="minute" cssStyle="width: 30%"  id="minute" list="minuteList" cssClass="form-control " headerKey="0" headerValue="Select"/>
									
				  </div>
     		</div>
     	</div>
     	<div class="modal-footer">
     	<input type="button" value="Save" class="btn btn-danger" onclick="saaveadmndate()" >
     	</div>
     </div>
     </div>
     </div>
		<s:form action="newsavedClient" id= "redirectToDashboard">
		
	</s:form>

											
										</div>
									</div>
								</div>
							</div>
						



