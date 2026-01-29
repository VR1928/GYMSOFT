<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="common/js/pagination.js"></script>
<script type="text/javascript" src="assesmentForms/js/assessment.js"></script>
<script type="text/javascript" src="appointment/js/appointment_type.js"></script>
<script type="text/javascript" src="common/js/masters.js"></script>
 <script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"> </script>
<SCRIPT type="text/javascript">


function printStockReportExcel() {
    $(".tablestock").table2excel({
				exclude: ".noExl",
				name: "Investigation Type Report",
				filename: "invtypereport",
				fileext: ".xls",
				exclude_img: true,
				exclude_links: true,
				exclude_inputs: true
			});
}

</script>


<div class="">
							<div class="">
								<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

										<h4>Investigation Type</h4>

									</div>
								</div>
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										<div>
<div class="col-lg-12 col-md-12 topback2">
	<div class="col-lg-3 col-md-2">
			<s:select list="masterlist"
					cssClass="form-control showToolTip chosen-select" name="mastername"
					listKey="id" listValue="name" onchange="selectAction(this.value)"></s:select>
		</div>
		<div class="col-lg-1 col-md-1">
			<a class="btn btn-primary" href="#" onclick="opencPopup('addInvestigationMaster')"><i
				class="fa fa-plus"></i> Add</a>
		</div>
		<div class="col-lg-3 col-md-2">
			<input type="button" value ="Set TP" onclick="openPopup('thirdpartysettingsInvestigation')" class="btn btn-danger">
			
		</div>
		
<form action="InvestigationMaster">
<div class='form-inline'>	
		<div class="col-lg-1 col-md-1 form-group">
			<s:select name="isdeleted" id="isdeleted" 
				list="#{'2':'All','0':'Active','1':'Deleted'}"
				cssClass="form-control chosen-select" ></s:select>
			</div>
		<div class="col-lg-3 col-md-2 form-group">
			<s:textfield theme="simple" name="searchText" placeholder="Search By Investigation Type"  cssClass="form-control" />
			
		</div>
		<div class="col-lg-1 col-md-1 form-group">
			<input type="submit" value="Go" class="btn btn-primary"/>
			<a type="button"  title="Save As XLS" onclick="printStockReportExcel()" class="btn btn-primary"><i class="fa fa-file-excel-o"></i></a>
		</div>
</div>		
</form>
	<!--<div class="col-lg-3 col-md-2">
	<label>Select Master</label>
	<s:select list="masterlist" name="mastername"
					listKey="id" listValue="name" onchange="selectAction(this.value)" cssClass="form-control showToolTip chosen-select"></s:select>
	</div>
	<div class="col-lg-6 col-md-8">
		<a class="btn btn-primary" href="addInvestigationMaster" style="margin-top:21px;"><i
			class="fa fa-plus"></i> Add</a>
	</div>
	<div class="col-lg-3 col-md-2"></div>
</div>
--><div class="row">
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

<div class="row">
	<div class="col-lg-12">
		<div class="">
			<table id="results"
				class="table table-hove table-bordered table-striped table-condensed tablestock">
				<thead>
					<tr>
						<th class="text-left">Sr No</th>
						<th class="text-left">Investigation Type</th>
						<th class="text-left">Report Type</th>
						<th class="text-left">Charge</th>
						<th class="text-left">Round Charge</th>
						<th class="text-center hidden-print">Edit</th>
						<th class="text-center hidden-print">Delete</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="invsTypeList.size!=0">
						<s:iterator value="invsTypeList" status="element">
							<tr>
								<td><s:property value="id" /></td>
								<td><s:property value="name" /></td>
								<td><s:property value="report_type" /></td>
								<td><s:property value="charge"/></td>
								<td><s:property value="roundcharge" /></td>
								<s:hidden value="%{id}" name="id"></s:hidden>
								
								 <s:if test='%{id==35}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:if>
								 <s:elseif test='%{id==94}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:elseif>
								
								 <s:elseif test='%{id==8}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:elseif>
								
								 <s:elseif test='%{id==16}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:elseif>
								
								<s:elseif test='%{id==50}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:elseif>
								
								<s:elseif test='%{id==91}'> 
								 	 <td class="text-center hidden-print">Can't Edit</td>
									<td class="text-center hidden-print">Can't Delete</td>
								</s:elseif>
							
								<s:else>
									<s:url action="editInvestigationMaster" id="edit">
									<s:param name="selectedid" value="%{id}"></s:param>
								</s:url>
								<!--<td class="text-center"><s:a href="%{edit}"
										class="text-warning">
										<i class="fa fa-edit"></i>
									</s:a></td>
								-->
								<td class="text-center hidden-print"><a href="#" onclick="opencPopup('editInvestigationMaster?selectedid=<s:property value="id" />')"><i class="fa fa-edit"></i></a></td>
								<s:url action="deleteInvestigationMaster" id="delete">
									<s:param name="selectedid" value="%{id}"></s:param>
								</s:url>
								<s:if test="isdeleted==1">
								
								<td class="text-center hidden-print">Deleted</td>
								</s:if>	
								<s:else>	
								<td class="text-center hidden-print"><s:a href="%{delete}"
										onclick="return confirmedDelete()" cssClass="text-danger">
										<i class="fa fa-trash-o"></i>
										
									</s:a></td>
									</s:else>
							</s:else>
							</tr>
						</s:iterator>
					</s:if>
				</tbody>
				<s:else>
						There is no Investigation Type List found!!
					</s:else>
			</table>
		</div>
	</div>
</div>
<s:form action="InvestigationMaster" name="paginationForm" id="paginationForm" theme="simple">
	<div class="col-lg-12 col-md-12" style="padding:0px;">
		<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
			Total:<label class="text-info"><s:property value="totalRecords" /></label>
		</div>
		<%@ include file="/common/pages/pagination.jsp"%>
	</div>

</s:form>
											

											
										</div>
									</div>
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



