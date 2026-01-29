
<%@taglib uri="/struts-tags" prefix="s" %>		

<s:form action="getAllPrintDataNotAvailableSlot?action=dashboard" id="opdpreviewfrm">
<s:hidden name="printCommencing" id="printCommencing"/>
<s:hidden name="printLocation" id="printLocation"/>
<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>

 
<style>

.topback2 {
    background-color: #f5f5f5;
    padding: 10px 10px 10px 10px;
}

.borderbot{
	border-bottom: 2px solid #6699cc;
    padding-top: 36px;
    padding-bottom: 15px;
    height: 135px;
}
.clinicname {
    font-size: 20px;
    font-weight: bold;
}
.clicniaddress {
    font-size: 12px;
    font-weight: bold;
}
.rgeno{
	    float: right;
    font-size: 11px;
    padding-top: 8px;
}
.titleset{
	margin: 0px;
    color: #6699cc;
    border-bottom: 1px dashed #efefef;
    font-size: 17px;
    line-height: 20px;
}
label {
    display: inline-block;
    max-width: 100%;
    margin-bottom: 0px;
    font-weight: bold;
}
td, th {
    padding: 0px 5px 0px 5px !important;
    border-right: 1px solid #eee !important;
}
.setticolors{
	border-bottom: 4px double #ddd;
	font-size:16px;
	color: firebrick;
}
.setotas{
	  padding: 0px 6px 4px 0px;
    text-align: right;
    color: green;
    font-size: 12px;
}
p {
    margin: 0 0 2.5px !important;
}
</style>
<script>

$(document).ready(function() {
	


	

	$("#fromDate").datepicker({

		dateFormat : 'dd/mm/yy',
		yearRange: yearrange,
		minDate : '30/12/1880',
		changeMonth : true,
		changeYear : true

	});

	$("#toDate").datepicker({

		dateFormat : 'dd/mm/yy',
		yearRange: yearrange,
		minDate : '30/12/1880',
		changeMonth : true,
		changeYear : true
	});
});
</script>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 hidden-lg hidden-md visible-print">
		  		<div class="row" style="height: 135px;">
					<div id="newpost" class="col-lg-12 col-md-12 col-xs-12 col-sm-12 borderbot">
							<link href="common/css/printpreview.css" rel="stylesheet" />
							<%@ include file="/accounts/pages/letterhead.jsp" %>
					</div>
				</div>
				</div>

<div class="row">
	<div class="col-lg-12 topback2">	
		<div class="form-inline">
			<div class="form-group hidden-print">
			
			
			<div class="col-lg-2" style="margin-left:-20px">
			<s:if test="%{#userList != 'null'}">
		    <s:select cssClass="form-control weekdash chosen-select"
			id="diaryUser" name="previewdiaryuser" list="userList" listKey="id"
			listValue="diaryUser" headerKey="0" theme="simple"
			headerValue="Select Practitioner"
			 />
		</s:if>
			</div>
			
			<div class="form-group col-lg-2" style="width: 15%; margin-left:70px;">
				<s:textfield readonly="true" name="fromDate" id="fromDate"
				cssClass="form-control" theme="simple" placeholder="from date" style="width: 100%;"></s:textfield>
			</div>
			<div class="form-group col-lg-2" style="width: 15%;">
				<s:textfield readonly="true" name="toDate" id="toDate"
				cssClass="form-control" theme="simple" placeholder="to date" style="width: 103%;"></s:textfield>
				
				
			</div>
			
			<div class="form-group col-lg-1" style="width: 7%;">
			<a href="#" class="btn btn-primary" onclick="showopdpreview()">Go</a>
				
			</div>
				<div class="form-group">
					<a type="button" id="btnxls" title="Save As XLS" onclick="printExcel()" class="btn btn-primary" value="Excel"><i class="fa fa-file-excel-o"></i></a>
				</div>
		
			<div class="form-group">
				<span style="font-weight:bold;">Practitioner : </span> <s:property value="diaryUser"/>
			</div>
				
			
			<div class="form-group">
				<%-- <s:submit value="Go" theme="simple" cssClass="btn btn-primary"></s:submit> --%>
				<table >
				<tbody>
				   <tr>
                                                                    <td><b>-Total OPD</b></td>
                                                                    <td><s:property value="totalopdseen"/></td>
                                                                </tr>
                                                                
                                                               <s:iterator value="opdappointmenttype">
                                                               
                                                                <tr>
                                                                    <td> <s:property value="name"/></td>
                                                                    <td><s:property value="result"/></td>
                                                                </tr>
                                                               </s:iterator>
                  </tbody>
           </table>
		<%-- <s:if test="%{#userList != 'null'}">
		<s:select cssClass="form-control weekdash chosen-select"
			id="diaryUser" name="previewdiaryuser" list="userList" listKey="id"
			listValue="diaryUser" headerKey="0" theme="simple"
			headerValue="Select Practitioner"
			 />
		</s:if> --%>
			</div>
		</div>
		
		
	</div>  
</div>
</div>
</s:form>

<script>	
function showopdpreview(){
	document.getElementById('opdpreviewfrm').submit();
}

$(document).ready(function() {

	$("#fromDate").datepicker({

		dateFormat : 'dd/mm/yy',
		yearRange: yearrange,
		minDate : '30/12/1880',
		changeMonth : true,
		changeYear : true

	});

	$("#toDate").datepicker({

		dateFormat : 'dd/mm/yy',
		yearRange: yearrange,
		minDate : '30/12/1880',
		changeMonth : true,
		changeYear : true
	});
});
function printExcel() {

    $(".xlstable").table2excel({
					exclude: ".noExl",
					name: "Preview List",
					filename: "preview",
					fileext: ".xls",
					exclude_img: true,
					exclude_links: true,
					exclude_inputs: true
				});
}
</script>		

<s:if test="actionType!='dashboard'">
	
<table width="1000%" align="center" style="background-color: rgb(216, 212, 212)">
	<tr>
	<s:if test="actionType=='week'">
		<td style="font-size: 20px;"><span style="font-weight:bold;">Practitioner : </span> <s:property value="diaryUser"/></td>
	</s:if>
	<s:if test="actionType=='day'">
		<td style="font-size: 20px; font-weight: bold;">Daily Schedule For <s:property value="commencing"/></td>
	</s:if>	
		
	</tr>
	<s:if test="actionType=='day'">
		<tr>
			<td style="font-weight: bold; font-size: 18px;">Practitioner : <s:property value="diaryUser"/><span style="margin-left:70px;font-size:14px;">Location : <s:property value="locationName"/></span>
			<br><br><span style="font-size: 15px;font-weight: bold;">Daily Notes:</span></td>
			
			
		</tr>
		
		
	</s:if>
	
</table>

</s:if>
	
	<div class="row">
		<div class="col-lg-12">
			<s:actionmessage cssClass="messageDiv" />
			<div class="table-responsive">
				<table class="table-bordered table-hover table-condensed width-full  xlstable"   style="font-size: 16px;">
				<col  span='11' align="center">
				<s:if test="actionType=='day'">
					<!-- <col width="2%"/>
					<col width="5%"/>
					<col width="20%"/>
					<col width="40%"/>
					<col width="20%"/>
					<col width="2%"/>
					<col width="5%"/>
					<col width="20%"/>
					<col width="40%"/>
					<col width="20%"/> -->
				</s:if>
				<s:elseif test="actionType=='dashboard'">
					<!-- <col width="10%"/>
					<col width="10%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="20%"/>
					<col width="30%"/>
					<col width="20%"/> -->
					
				</s:elseif>
				<s:else>
				<!-- 	<col width="10%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="20%"/>
					<col width="30%"/>
					<col width="20%"/> -->
					
					
				</s:else>
				
					<thead>
						<tr>
						
					
							<th style="width:1%;">Sr.</th>
							<!-- <th>Date</th> -->
							<s:if test="actionType=='dashboard'">
								<!-- <th>Practitioner</th> -->
							</s:if>
							<s:if test="actionType!='day'">
								<th style="width:15%;">Date</th>
							</s:if>
							<th style="text-align: center;width:5%;">Time</th>
							<!-- <th style="text-align: center;">Duration</th> -->
							<th>UHID</th>
							<th style="width:20%;">Patient</th>
							<th style="width:20%;">Age/Gender</th>
							<th style="width:10%;">Appointment Name</th>
							<th style="width:10%;">Note</th>
							<th style="width:10%;">Debit</th>
							<th style="width:10%;">Discount</th>
							<th style="width:10%;">Payment</th>
							
							
						</tr>
					</thead>
					<tbody>
					<%int i=1; %>
						<s:if test="practitionerApmtList.size!=0">
							<s:iterator value="practitionerApmtList" status="rowstatus">
								<tr id="<s:property  value="id" />">
									
									<s:if test="actionType=='dashboard'">
										<%-- <td><s:property value="diaryUser" /></td> --%>
									</s:if>
									<td><%=i %></td>
									<s:if test="actionType!='day'">
										<td><s:property value="commencing" /></td>
									</s:if>
									<td style="text-align: center;"><s:property value="sTime" /></td>
									<%-- <td style="text-align: center;"><s:property value="apmtDuration" /></td> --%>
									<s:if test="status==0">
									<td><s:property value="uhid" /></td>
										<td><s:property value="client" /></td>
										<td><s:property value="agegender" /></td>
										<td><s:property value="apmtType" /></td>
										<td><s:property value="notes" /></td>
										
									</s:if>
									<s:elseif  test="status==1">
									<td><s:property value="client" /></td>
									<td><s:property value="agegender" /></td>
										<s:if test="status==0">
											<td><s:property value="apmtType" /></td>
										</s:if>
										<s:else>
											<td><s:property value="apmtType" /></td>
										</s:else>
										
										<td><s:property value="notes" /></td>
										
										
										
									</s:elseif>
									<s:else>
										<td></td>
										<td></td>
										<td></td>
									</s:else>
									<td><s:property value="debit" /></td>
									<td><s:property value="discount" /></td>
									<td><s:property value="payment" /></td>
									
									
									
									
									</tr>
									<%i++; %>
								</s:iterator>
								
								
								
							</s:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		
		<div class="row" >
			
				<div class="col-lg-2 col-md-2 hidden-print">
						<input style="font-size: 16px;padding-left: 20px;padding-right: 20px;" type="submit" class="btn btn-primary" value="Print" onclick="printpage()">
			
				</div>
				</div><br>
<script src="common/chosen_v1.1.0/chosen.jquery.js" type="text/javascript"></script>
<script type="text/javascript">
    var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"100%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }
  </script>