<%@taglib uri="/struts-tags" prefix="s"%>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
<div class="row details">
				<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

				<h4>Trainer Notes </h4>

			</div>
</div>
<script type="text/javascript">
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
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="height: 50px;padding:10px; ">
<form action="listtrainernoteNotAvailableSlot">
<div class="form-inline">
<div class="form-group">
<input type="button" class="btn btn-primary" value ="Add New Notes +" onclick="openPopup('otnotesNotAvailableSlot')">
</div>
<div class="form-group">
<s:textfield readonly="true" name="fromDate" id="fromDate" cssClass="form-control" theme="simple" placeholder="from date"></s:textfield>
					</div>
					<div class="form-group">
<s:textfield readonly="true" name="toDate" id="toDate"	cssClass="form-control" theme="simple"  placeholder="to date" ></s:textfield>
</div>
<div class="form-group">
				<s:submit value="Go" theme="simple" cssClass="btn btn-primary"></s:submit>
				</div>
</div>
</form>
</div>

<%int i=1; %>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
	<table class="my-table xlstable" style="width:100%;text-align:left;" >
	<tr style="text-align: center !important;">
	<th>Sr.No</th>
	<th>From Date</th>
	<th> To Date</th>
	<th>Print</th>
	</tr>
	<s:iterator value="practitionerApmtList">
	<tr>
	<td><%=i++ %></td>
	<td><s:property value='fromdate'/></td>
	<td><s:property value='todate'/></td>
	<td><a href="#" onclick="openPopup('printotnotesNotAvailableSlot?id=<s:property value='id'/>')">Print</a></td>
	</tr>
	</s:iterator>
	</table>
</div>
</div>