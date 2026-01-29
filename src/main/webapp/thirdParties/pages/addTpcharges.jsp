<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript" src="thirdParties/js/tpcharges.js"></script>

<style>
.form-group {
    margin-bottom: 5px;
}
.backformset{
    background-color: #f5f5f5;
    min-height: 328px;
    padding: 15px 0px 0px 0px;
    border-right: 1px solid #ddd;
}
</style>

<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

										<h4>Add New TP Charges</h4>

									</div>
								</div>

<s:form action="savechargesThirdParty" id="tpchargeform" theme="simple">
<div class="panel panel-primary">
			<div class="">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-xs-12" id = "tpchargeTable">	
						<div class="col-lg-12 col-md-12 topback2">
							<div class="col-lg-2 col-md-2 col-xs-2">
								<div class="form-group">
								<label for="exampleInputEmail1">Select Payee</label>
	    						<s:select list="#{'Select Payee':'Select Payee','0':'Self','1':'Third Party'}"  onchange="settpchanges(this.value)"  name="payee" id="payee" cssClass="form-control chosen-select"/>
							</div>
							</div>
							<div class="col-lg-2 col-md-2 col-xs-2">
								<div class="form-group">
									<label for="exampleInputEmail1" id="hstp">Select TP</label>
									<div class="hidden" id="dstp">
										<s:select list="thirdPartyList" listKey="id" listValue="companyName" id="tpid" headerKey="0" headerValue="Select Third Party" name="tpid" title="select Third Party"
														cssClass="form-control chosen-select"   > </s:select>
									</div>
								</div>
							</div>
							<div class="col-lg-2 col-md-2 col-xs-2">
								<div class="form-group">
							<label for="exampleInputEmail1">Select Ward</label>
    							<s:select list="wardlist" listKey="id" listValue="wardname" name="wardid" id="wardid" title="Select Ward" headerKey="0" headerValue="Select Ward"
											cssClass="form-control chosen-select"   > </s:select>
							</div>
							</div>
							
							<div class="col-lg-2 col-md-2 col-xs-2">
								<div class="form-group">
								<label for="exampleInputEmail1">Charge Type</label>
	    						<s:select list="newChargeTypeList" listKey="name" listValue="name" name="chargeType" id="chargeType" title="select Charge Type"
												  cssClass="form-control chosen-select" onchange="getcharges(this.value)" headerKey="0" headerValue="Select Charge Type" > </s:select>
							</div>
							</div>
							<div class="col-lg-2 col-md-2 col-xs-2">
								<div class="form-group hidden" id="sinv">
									<label for="exampleInputEmail1">Investigation Type</label>
	    							<s:select list="invsTypeList" name="invstype" id="invstype" onchange="getinvestigationcharges(this.value)" listKey="id" listValue="name" headerKey="0" headerValue="Select Investigation Name" cssClass="form-control chosen-select" ></s:select>
								</div>
							
							</div>
							<div class="col-lg-2 col-md-2 col-xs-2">
							<div class="form-group hidden" id="perdiv">
								<label for="exampleInputEmail1">Enter Percentage</label>
									<div class="col-lg-8 col-md-8 col-xs-8">
										<input type="number" id="peramount" class="form-control" placeholder="Enter %">
									</div>
									<div class="col-lg-4 col-md-4 col-xs-4">
										<input type="button" value="Apply" onclick="calculateandincrese()" class="btn btn-primary">
									</div>
							</div>
							</div>
							
						</div>
					
					
										
						
						<div class="form-group hidden">
								<a onclick="" class="btn btn-primary"><i class="fa fa-plus"></i> Add More</a>
								
								<a onclick="" class="btn btn-primary" ><i class="fa fa-trash-o"></i> Delete Row</a>
					</div>
			</div>
			
			<div class="col-lg-12 col-md-12 col-xs-12">
			<div class="col-lg-12 col-md-12 col-xs-12" style="margin-top: 15px;">
				<div class="form-group">
						    <label for="exampleInputEmail1">Charges</label>
						    <div id="scroll" >
												<!--<s:iterator value="appointmentTypeList">
												<div class="col-lg-12 col-md-12 col-sm-12" style="padding:0px;border-top: 1px solid #ddd;padding-top: 2px;">
														<div class="col-lg-8 col-md-8 col-sm-8" style="padding:0px;">
														 		<div class="form-group">
																<input class="case" type="checkbox"   id="ch<s:property value="id"/>" name="ch<s:property value="id"/>" value="<s:property value="id"/>">
																<s:property value="name"/>
															</div>
														 </div>
														 <div class="col-lg-4 col-md-4 col-sm-4" style="padding:0px;">
														 	    <div class="form-inline">
															
															<div class="form-group" style="width:49%">
																<input type="text" name="mrp<s:property value="id"/>" title="enter mrp" id="mrp<s:property value="id"/>" class="form-control" placeholder="Enter MRP" style="width:100%;background-color: #f5f5f5;" />
															</div>
															<div class="form-group" style="width:49%">
																<input type="text" class="form-control" name="code<s:property value="id"/>" id="code<s:property value="id"/>" title="enter code" placeholder="Enter Code" style="width:100%;background-color: #f5f5f5;" />
															</div>
														</div>
														 </div>
												</div>
												</s:iterator>
										--></div>
										<input type="hidden" id="notes" name="notes" />
						  </div>
			</div>
						 
						
					</div>
			
		</div>
	</div>
	<div class="col-lg-3 col-md-2"></div>
</div>

	
		<div class="col-lg-12 col-md-12 text-right">
			<input type="button" class="btn btn-primary" value="Save" onclick="saveAllCharges()" />
			<s:reset cssClass="btn btn-primary" />
			<a href="backInvestigationNameMaster" class="btn btn-primary">Back</a>
		</div>
	
	<s:token></s:token>
</s:form>

<script src="_assets/newtheme/js/vendor/slimscroll/jquery.slimscroll.min.js"></script>
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

<script>
$(function () {
    $('#scroll').slimScroll({
        height: '300px'
    });
});

</script>



