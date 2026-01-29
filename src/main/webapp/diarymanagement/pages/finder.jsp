<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="diarymanagement/js/finder.js"></script>

<%-- <script type="text/javascript" src="diarymanagement/js/nonavailableslot.js"></script>
<script type="text/javascript" src="diarymanagement/js/notavailableslotpopupscript.js"></script> 
<script type="text/javascript" src="diarymanagement/js/commonAppointmentView.js"></script>
 --%>
<link href="diarymanagement/css/popupstyle.css" rel="stylesheet" type="text/css" />
<link href="diarymanagement/css/subModal.css" rel="stylesheet" type="text/css" />


<div class="">
							<div class="">
								<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
								<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 oneseticonleft">
									<img src="dashboardicon/shirt.png" class="img-responsive prescripiconcircle">
								</div>
								<div class="col-lg-11 col-md-11 col-sm-11 col-xs-11 titlestleftiocn">
								<h4>Finder</h4>
								</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										<div>


	<div class="col-lg-12 col-md-12">
		<s:hidden name = "message" id = "message"></s:hidden>
	<s:if test="hasActionMessages()">
	<script>
		var msg = " " + document.getElementById('message').value;
		showGrowl('', msg, 'success', 'fa fa-check');
		</script>
	</s:if>
	</div>

			
	<%-- <script type="text/javascript">
	
	$(function() {
		$( "#clientSearchDiv").dialog({
			autoOpen: false,
			resizable: true,
			height: 500,
			width: 650,
			modal: true,
			buttons: {
				
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
		
		$( "#appointment" ).dialog({
			autoOpen: false,
			resizable: true,
			height: 400,
			width: 450,
			modal: true,
			
			buttons: {
				"Save": function() {
					//$( this ).dialog( "close" );
					var starttime = read_cookie("cookieNewStarttime");
					
						$(this).saveSlot(editAppointId,editStartTime);
						
					
				
					
				},
				Cancel: function() {
					//document.getElementById('addTreatment').disabled = true;
					$( this ).dialog( "close" );
				}
				
			}
		});
		
		
		$( "#checkavlbltydivpopup" ).dialog({
			autoOpen: false,
			resizable: true,
			height: 400,
			width: 700,
			modal: true,
			buttons: {
				"Ok": function() {
					//setClientDidNotComeConfirm();
					$( this ).dialog( "close" );
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
				
			}
		});
		
		$( "#addTreatmentEpisodeDiv" ).dialog({
			autoOpen: false,
			resizable: true,
			height: 500,
			width: 450,
			modal: true,
			buttons: {
				"Save": function() {
					saveTreatment();
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
					

				}
				
			}
		});
		
		
		$( "#addPatientDiv" ).dialog({
			autoOpen: false,
			resizable: true,
			height: 600,
			width: 500,
			modal: true,
			buttons: {
				"Save": function() {
					savePatient();
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
				
			}
		});
		
		
		
	});
	 
	</script>
	 --%>
	
	
	
	
		
		<span class="error"><s:actionerror id="server_side_error"/></span>
		
		 
		
		<input type="hidden" id="invoicee" name="invoicee"/>
		<input type="hidden" id="commencing" name="commencing"/>
		<input type="hidden" id="caldate" name="caldate"/>
		
		
<%-- <s:form action="Finder"  theme="simple">
			
<div class="row">
<div class="col-lg-1 col-md-1" style = "padding-left: 59px;">
	<label>Client:</label>
</div>
	<div class="col-lg-5 col-md-5">
	
		<div class="input-group">
			<s:hidden name="clientId" id = "clientId" ></s:hidden>
			<s:textfield  name="client" id="client" readonly="true"
				  cssClass="form-control" onclick="showPopUp()"></s:textfield> <span
				class="input-group-btn">
			<s:submit value="Go!" cssClass="btn btn-primary"></s:submit>	
			
		</span>
		</div>
	</div>
</div>
</s:form> --%>


<script>
	$(document).ready(function() {

		$("#fromDate").datepicker({

			dateFormat : 'dd/mm/yy',
			yearRange: yearrange,
			minDate : '30/12/1880',
			changeMonth : true,
			changeYear : true

		});

	});	
	
</script>

<s:form action="Finder"  theme="simple">
<div class="col-lg-12 col-md-12 topback2 hidden-print">
	<div class="col-lg-1 col-md-1">
		<label class="findersearch">Search : </label>
	</div>
	<div class="col-lg-4 col-md-4" style="padding-left:0px;">
		
			<s:textfield  name="client" id="client" readonly="true"
				  cssClass="form-control" onclick="showPopUp()" placeholder="Search by patient"></s:textfield>
		</div>
		
		
		<div class="col-lg-2 col-md-2">
			
					<s:textfield readonly="true" name="fromDate" id="fromDate" placeholder="Search by Date"
				cssClass="form-control" theme="simple"></s:textfield>
		</div>
		
		<div class="col-lg-1 col-md-1">
				<s:submit value="Go" theme="simple" cssClass="btn btn-primary" cssStyle="width:99px;"></s:submit>
			</div>	
</div>
</s:form>

<br/>		
	
		<div style="color: red;">Note : Please note Appointment Finder will only show todays and future appointment in the dashboard.</div>
		<s:if test="finderList!=null">
			<div class="row">
				<div class="col-lg-12">
					<div class="table-responsive">
						<table class="table table-hover table-condensed table-bordered">
							<thead>
						
					
					<tr>
						<th>Date</th>
						<th>Practitioner</th>
						<th>Patient</th>
						<th>Time</th>
						<th>Duration</th>
						<th>Appointment Type</th>
						<th>Treatment Episode Name </th>
						<th>Status</th>
						<th>Notes</th>
					<!-- 	<th>Modify</th> -->
						<th>Cancel</th>
						
					</tr>
					</thead>
					<tbody>
					<s:iterator value="finderList">
					
						<s:if test="oldata==true">
						<tr id="<s:property value="id"/>" style="background-color: rgb(229, 217, 129)">
							<td><s:property value="commencing"/></td>
							<td><s:property value="diaryUser"/></td>
							<td><s:property value="clientName"/></td>
							<td><s:property value="sTime"/></td>
							<td><s:property value="duration"/></td>
							<td><s:property value="apmttypetext"/></td>
							<td><s:property value="treatmentSessions"/></td>
							<s:if test="status==0">
								<td>Appointment Slot</td>
							</s:if>
							<s:else>
								<td>Blocked Slot</td>
							</s:else>
							<td><s:property value="notes"/></td>
							
							<s:url action="editUserProfile" id="edit">
	 							<s:param name="id" value="%{id}"></s:param>
							</s:url>
	  						 <%-- <td class="text-center"><a class="text-warning" href="#" onclick="modifyAppointment('<s:property value="commencing"/>','<s:property value="diaryUser"/>','<s:property value="sTime"/>','<s:property value="duration"/>','<s:property value="apmtType"/>','<s:property value="notes"/>','<s:property value="endTime"/>','<s:property value="location"/>','<s:property value="treatmentEpisodeId"/>','<s:property value="id"/>','<s:property value="apmtSlotId"/>','<s:property value="diaryUserId"/>','<s:property value="clientId"/>','<s:property value = "condition"/>');"><i class="fa fa-edit"></i></a></td> --%>
	  						
	 						<s:url action="deleteFinder" id="deleteFinder">
								<s:param name="id" value="%{id}"></s:param>
							</s:url>
							<%-- <td class="text-center"><s:a href="%{deleteFinder}" cssClass="text-danger" onclick = "return confirmedDelete()"><i class="fa fa-trash-o"></i></s:a></td> --%>	
							
							<td class="text-center"><a href="#" onclick = "openFinderCancelApmtPopup(<s:property value="id"/>)"><i class="fa fa-trash-o"></i></a></td>
						</tr>
						</s:if>
						<s:else>
						<tr id="<s:property value="id"/>">
							<td><s:property value="commencing"/></td>
							<td><s:property value="diaryUser"/></td>
							<td><s:property value="clientName"/></td>
							<td><s:property value="sTime"/></td>
							<td><s:property value="duration"/></td>
							<td><s:property value="apmttypetext"/></td>
							<td><s:property value="treatmentSessions"/></td>
							<s:if test="status==0">
								<td>Appointment Slot</td>
							</s:if>
							<s:else>
								<td>Blocked Slot</td>
							</s:else>
							<td><s:property value="notes"/></td>
							
							<s:url action="editUserProfile" id="edit">
	 							<s:param name="id" value="%{id}"></s:param>
							</s:url>
	  						 <%-- <td class="text-center"><a class="text-warning" href="#" onclick="modifyAppointment('<s:property value="commencing"/>','<s:property value="diaryUser"/>','<s:property value="sTime"/>','<s:property value="duration"/>','<s:property value="apmtType"/>','<s:property value="notes"/>','<s:property value="endTime"/>','<s:property value="location"/>','<s:property value="treatmentEpisodeId"/>','<s:property value="id"/>','<s:property value="apmtSlotId"/>','<s:property value="diaryUserId"/>','<s:property value="clientId"/>');"><i class="fa fa-edit"></i></a></td> --%>
	  						
	 						<s:url action="deleteFinder" id="deleteFinder">
								<s:param name="id" value="%{id}"></s:param>
							</s:url>
							<td class="text-center"><a href="#" onclick = "openFinderCancelApmtPopup(<s:property value="id"/>)"><i class="fa fa-trash-o"></i></a></td>	
						</tr>
						</s:else>
					
						
					
					</s:iterator>
				</tbody>
				</table>
				</div>
				</div>
				</div>
				
			
			</s:if>
			
			
			<s:form action="deleteFinder" id="finderfrm">
				<s:hidden name="id" id="id"/>
				<s:hidden name="cancelApmtNote" id="cancelfinderApmtNote"/>
				<s:hidden name="clientId" id = "clientId" ></s:hidden>
			
			</s:form>
			
			
	<!-- Cancel Appointment Note Popup  -->

	<div class="modal fade" id="cancelApmtNoteDiv" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">Cancel Appointment Note</h4>
				</div>
				<div class="modal-body">
				
				<label>Note:</label>
				<textarea id = "cancelApmtNote" name="cancelApmtNote" class="form-control showToolTip" placeholder="Enter Cancellation Note"></textarea>
				
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" 
						onclick="deleteFinderNotAviSlot()">Cancel Appointment </button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Modal -->
<div class="modal fade" id="clientSearch" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">Patient Search</h4>
      </div>
      <div class="modal-body">
        <%@ include file="/diarymanagement/pages/allPatientsList.jsp"%>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
				
			
 <%-- <div class="modal fade" id="appointment" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg"">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Modify Appointment</h4>
				</div>
				<div class="modal-body">
						<%@ include file="/diarymanagement/pages/bookAppointment.jsp" %>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="bookSlot()">Book</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="addPatientDiv" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"  data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">			
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add New Patient</h4>
				</div>
				<div class="modal-body" >
					<%@ include file="/diarymanagement/pages/addPatientPage.jsp"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						onclick="return savePatient()">Save</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" >Close</button>
				</div>
			</div>
		</div>
	</div>
			
<div class="modal fade" id="addTreatmentEpisodeDiv" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add Treatment Episode</h4>
				</div>
				<div class="modal-body">
						<%@ include file = "/treatmentEpisode/pages/addTreatmentEpisode.jsp"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>	

<div class="modal fade" id="checkavlbltydivpopup" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Appointment Availability</h4>
				</div>
				<div class="modal-body">
					<%@ include file="/diarymanagement/pages/checkAvailability.jsp" %>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>	

	 --%>
											

											
										</div>
									</div>
								</div>
							</div>
						</div>












	






