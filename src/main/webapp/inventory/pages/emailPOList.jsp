<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>
<script type="text/javascript" src="report/js/report.js"></script>
<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>
<script type="text/javascript" src="common/js/pagination.js"></script>
<script>

 function printExcel() {

       $(".xlstable").table2excel({
					exclude: ".noExl",
					name: "Lab report",
					filename: "labreport",
					fileext: ".xls",
					exclude_img: true,
					exclude_links: true,
					exclude_inputs: true
				});
   }
  

$(document).ready(function() {

	$("#fromDate").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true

	});

	$("#toDate").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true
	});
});
</script>

<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

										<h4>PO Mail List</h4>

									</div>
								</div>

<div class="row ">

<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
 <s:form theme="simple" action="emailPOListProcurement" method="post">
<div class="form-inline">
 
                        <div class="col-md-12" style="padding: 0px;">
                        <div class="col-lg-6 col-md-6 col-sm-6">
                        
                        <div class="form-group">
	                           		<span class="text-uppercase"><b></b> &nbsp; From Date - &nbsp;</span>
	                           </div>
                        <div class="form-group" style="width: 14%;">
                        	<s:textfield id="fromDate" name="fromdate"  cssClass="form-control" placeholder="From Date" cssStyle="width:100%;"/>
                        </div>
                         &nbsp;To Date - &nbsp;
                        <div class="form-group" style="width: 14%;">
                        	<s:textfield  name="todate" id="toDate"  cssClass="form-control" placeholder="To Date" cssStyle="width:100%;"/>
                        </div>
                        
                        <div class="form-group">
                        	<button type="submit" class="btn btn-primary">Go</button>
                        </div>
                         &nbsp;  &nbsp;  &nbsp;  &nbsp;
                        
</div>
</div>

</div>
</s:form>
<div>
<table class="table my-table xlstable table-bordered" style="width: 100%;" id="mynewtab" >
<tr>
<th>sr no</th>
<th>Procurement id</th>
<th> Date</th>
<th>Sender</th>
<th>Receiver</th>
</tr>
<%int i=0; %>
<s:iterator value="emaillist">
<tr>

						<td><%=++i %></td>
						<td><s:property value="procurementid"/></td>
	          	        <td><s:property value="date"/></td>
	          	        <td><s:property value="sender"/></td>
	          	          <td><s:property value="reciver"/></td>


</tr>
</s:iterator>
</table>
</div>
</div>
	<%-- <s:form action="newDmListProcurement" name="paginationForm" id="paginationForm" theme="simple">
							    	
							    	 <s:hidden name="fromdate"></s:hidden>
							    	<s:hidden name="todate"></s:hidden> 
									<div class="col-lg-12">
									<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
										Total:<label class="text-info"><s:property value="totalRecords" /> Records</label>
									</div>
									<%@ include file="/common/pages/pagination.jsp"%>
								</div>
							</s:form>                  --%>
</div>