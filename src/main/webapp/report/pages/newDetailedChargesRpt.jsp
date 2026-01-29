<%@page import="com.apm.Appointment.eu.entity.AppointmentType"%>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>
<script type="text/javascript" src="report/js/report.js"></script>
<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>

<!-- (Optional) Latest compiled and minified JavaScript translation files -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/i18n/defaults-*.min.js"></script>
<% ArrayList<AppointmentType> apmtlist= (ArrayList<AppointmentType>)session.getAttribute("apmttype");%>

<script>


 function printExcel() {

       $(".xlstable").table2excel({
					exclude: ".noExl",
					name: "Detailed Charges report",
					filename: "detailedCharges",
					fileext: ".xls",
					exclude_img: true,
					exclude_links: true,
					exclude_inputs: true
				});
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
</script>

<div class="">
	<div class="print-visible hidden-md hidden-lg" style="height: 135px;">
		<div id="newpost" class="col-lg-12 col-md-12 col-xs-12 col-sm-12 borderbot">
			<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="padding-left:0px;padding-right:0px;">
				 <link href="common/css/printpreview.css" rel="stylesheet" />
			<%@ include file="/accounts/pages/letterhead.jsp" %>
			</div>
		</div>
	</div>
	<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

										<h4>Charges Report Detailed</h4>

									</div>
								</div>

<div class="row ">

<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
<s:form action="newDetailedChargesRpt" theme="simple" id = "invoicerportfrm">
<div class="col-lg-12 col-md-12 topback2 hidden-print">
<div class="form-inline">
			
			<div class="form-group" style="width:7%;">
			FROM DATE
			</div>
			<div class="form-group" style="width:7%;">
				<s:textfield readonly="true" name="fromDate" id="fromDate"
					cssClass="form-control" theme="simple" style="width:100%;" placeholder="from date"></s:textfield>
			</div>
			<div class="form-group" style="width:5%;">
			TO DATE	
			</div>
			<div class="form-group" style="width:7%;">
			<s:textfield readonly="true" name="toDate" id="toDate"
					cssClass="form-control" theme="simple" style="width:100%;" placeholder="to date"></s:textfield>
			</div>
			
			<!-- <div class="form-group"  id="apmtt" > -->
			<%-- <span>Appointment Type</span>
			<select class="selectpicker" name="appmmt" multiple>
			<%for(AppointmentType apmt:apmtlist){ %>
			<option><%= apmt.getName() %></option>
			<% }%>
			</select> --%>
				<%-- <s:select name="apmtType"  style="width: 40%" list="apmtlist" listKey="name" listValue="name"   cssClass="form-control showToolTip chosen-select" headerKey="" headerValue="All Appointments"  multiple=''></s:select> 
			</div> --%>
			
            <input type="hidden" name="hideselect" id="hideselect" value=""/>
			<div class="form-group">
				<button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" style="width:100%;">All Appointment <span class="caret"></span></button>
												<ul class="dropdown-menu" style="margin-left: 357px !important">
													<s:iterator value="apmtlist">
												 	 <li><a href="#" class="small" data-value="name" tabIndex="-1"><input type="checkbox"  class="pacss" name="apmtType" onclick="setActionForAll()" value="'<s:property value="name" />'"/>&nbsp;<span class="spandrop"><s:property value="name"/></span></a></li>
												 	 
												  </s:iterator>
												 
												</ul>
			</div>
			<div class="form-group"  style="width: 15%">
				<s:select name="doctor"  style="width: 100%" list="doctorList" listKey="id" listValue="diaryUser"  multiple="" cssClass="form-control showToolTip chosen-select" headerKey="" headerValue="All Diaryusers"  ></s:select>
			</div>
			
			<div class="form-group"  >
		<s:select name="ipdopd" id="" 
					list="#{'':'Invoice Type ','0':'IPD','1':'OPD','2':'INVESTIGATION','3':'Daycare'}"
					cssClass="form-control chosen-select" ></s:select>
			</div>
			<div class="form-group"  style="width: 15%">
				<s:select name="clientid"  style="width: 100%" list="invoicedclients" listKey="id" listValue="clientName"  multiple="" cssClass="form-control showToolTip chosen-select" headerKey="" headerValue="All Patients"  ></s:select>
			</div>
			
			<div class="form-group"  style="width: 15%">
			<input type="text" class="form-control" placeholder="Invoice No" name="searchinv" value=<s:property value="searchinv"/>>
			</div>
			<div class="form-group">
									<s:select cssClass="form-control showToolTip chosen"
										id="condition" name="condition" list="conditionList"
										listKey="id" listValue="treatmentType" headerKey="0"
										theme="simple" headerValue="Select Speciality" />
								</div>
			<div class="form-group">
				<s:submit value="Go" theme="simple" cssClass="btn btn-primary"></s:submit>
				<a type="button" class="btn btn-primary"  title="Print" onclick="printpage()"><i class="fa fa-print"></i></a>
			<a type="button" id="btnxls" title="Save As XLS" onclick="printExcel()" class="btn btn-primary"><i class="fa fa-file-excel-o"></i></a>
				</div>

</div>
</div>
</s:form>
<%int i=1; %>
<table class="my-table xlstable" style="width: 100%">

				<tr bgcolor="#4E7894" >
				<td style="color:#eee;width: 1%">Sr. No.</td>
				<td style="color:#eee;width: 5%">Invoice Date</td>
					<td style="color:#eee">Old Invoice No </td>
					<td style="color:#eee">Invoice No </td>
				<td style="width: 5% ;color:#eee"">Date</td>
				<td style="color:#eee">Charge No </td>
				<td style="color:#eee">Status</td>
				<td style="color:#eee";width="12%">Patient Name</td>
				<td style="color:#eee">Payee</td>
				<td style="color:#eee";width="13%">Dept</td>
				<td style="color:#eee";width="10%">Doctor Name</td>
					<td style="color:#eee">Charge Type</td>
					<td style="color:#eee">Ward Name</td>
					<td style="color:#eee;width: 10%">Charges Name</td>
					
					<!-- <td style="color:#eee">ChargeType</td> -->
					<td style="color:#eee">Charges</td>
				<td style="color:#eee">Qty</td>	
				<td style="color:#eee">Total Amount</td>	
				<td style="color:#eee">Invoice Amount</td>	
				<td style="color:#eee">Discount</td>	
				<td style="color:#eee">Type</td>
				<td style="color:#eee">Charge Pract</td>	
				</tr>
				<tfoot>
				<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td><b><s:property value="debitTotal"/></b></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				</tr>
				</tfoot>
				<s:if test="list.size!=0">
				<s:iterator value="list">
				<tr>
				<td><%=i++ %></td>
				<td><s:property value="invdate"/></td>
				<td><s:property value="newsr"/></td>
				<td><s:property value="ipdopdseq"/></td>
				<td><s:property value="commencing"/></td>
				<td><s:property value="invoiceid"/></td>
				
				<s:if test="chargesInvoiced==true">
									<td>Invoiced</td>
								</s:if>
								<s:else>
									<td>Not Invoiced</td>
								</s:else>
				<td><s:property value="clientName"/></td>
				<td><s:property value="whoPay"/></td>
				<td><s:property value="locationName"/></td>
				<td><s:property value="username"/></td>
				<td>
				<s:if test="chargeType=='Bed Charge'">
				Accommodation Charge
				</s:if>
				<s:else><s:property value="chargeType"/></s:else>
				<td><s:property value="ward"/></td>
				<td><s:property value="appointmentType"/></td>
				
					
					<%-- <td><s:property value="chargeType"/></td> --%>
					<td><s:property value="creditCharge"/></td>
					<td><s:property value="quantity"/></td>
					<td><s:property value="totalAmount"/></td>
					<td><s:property value="debitAmount"/></td>
					<td><s:property value="discount"/></td>
					<td><s:property value="itype"/></td>
					<td><s:property value="firstname"/></td>
				</tr>
				</s:iterator>
				</s:if>
				</table>
</div>
</div>

</div>


  <script src="common/chosen_v1.1.0/chosen.jquery.js" type="text/javascript"></script>
  <script src="common/chosen_v1.1.0/docsupport/prism.js" type="text/javascript" charset="utf-8"></script>
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
  
  
  <script  type="text/javascript">
	function setActionForAll(){
		var hideselect="";
	  $('.pacss').each(function() { 
			 if(this.checked==true) {
				if(hideselect==''){
					hideselect =this.value;
				} else{
					hideselect =hideselect + ',' + this.value;	
				}
			 }     
		});
		
	  document.getElementById('hideselect').value=hideselect;
	}
	
	  </script>
