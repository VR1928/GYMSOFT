<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.apm.main.common.constants.Constants"%>
<%LoginInfo loginfo = LoginHelper.getLoginInfo(request);%>
<script type="text/javascript" src="accounts/js/commonaddcharge.js"></script> 
<script type="text/javascript" src="accounts/js/accounts.js"></script>
<script type="text/javascript" src="accounts/js/additionalCharges.js"></script>
<%if(loginfo.getClinicUserid().equals("pcsadmin")){ %>
	<script type="text/javascript" src="thirdParties/js/nicEdit.js"></script>
	<input type="hidden" id="pcsaccount">
<%} %>

<!-- Main Quill library -->
<script src="//cdn.quilljs.com/1.3.6/quill.js"></script>
<script src="//cdn.quilljs.com/1.3.6/quill.min.js"></script>

<!-- Theme included stylesheets -->
<link href="//cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
<link href="//cdn.quilljs.com/1.3.6/quill.bubble.css" rel="stylesheet">

<!-- Core build with no theme, formatting, non-essential modules -->
<link href="//cdn.quilljs.com/1.3.6/quill.core.css" rel="stylesheet">
<script src="//cdn.quilljs.com/1.3.6/quill.core.js"></script>


<script>
window.onload = function () {
	
	currencySign = '<%=Constants.getCurrency(loginfo)%>';
	$("#date").datepicker({

			dateFormat : 'dd-mm-yy',
			yearRange: yearrange,
			minDate : '30-12-1880',
			changeMonth : true,
			changeYear : true

		});
	
	$("#fdt1").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true

	});
	$("#tdt1").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true

	});
}
/* $(document).ready(function() {
	$("#fdt1").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true

	});
   $("#tdt1").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true

	});
			
	
} */


bkLib.onDomLoaded(function() {
	
	 new nicEditor({buttonList : ['fontSize','bold','italic','underline','strikeThrough','subscript','superscript','html','image'],maxHeight : 150}).panelInstance('chargedescriptionnew');
	    
	    
});
</script>

<style>
hr {
    margin-top: 5px;
    margin-bottom: 5px !important;
    border: 0;
    border-top: 1px solid #eee;
}
.dato{
width: 35%;
}
</style>
<div class="">
							<div class="">
								<div class="row details">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="background-color: #0199e9">

										<h4>Add Charge</h4>

									</div>
								</div>
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										<div>

<s:form action="saveAdditional" theme="simple">
<s:hidden name="packageids" ></s:hidden>
<div class="row">
	
	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
		<div class="">
			<div class="">
			
			<div class="col-lg-12 col-md-12 topback2" style="display: none;">
				<div class= "col-lg-1 col-md-1 col-xs-12 col-sm-2">
					<label>Create</label>
				</div>
				<div class= "col-lg-3 col-md-3 col-xs-3 col-sm-3">
					<input type="radio" id="creditCharge" name="creditDebitCharge" value="1" onclick="setresetaction()" > <label>Credit Charge</label> <br>
					<input type="radio"	id="debitCharge" name="creditDebitCharge" value="0" checked="checked" onclick="setresetaction()"> <label>Debit Charge</label>
				</div>
				<label id = "creditDebitChargeError" class="text-danger"></label>
			</div>
			
	
	<div class="row col-lg-12 col-md-12">
		<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4 " style="display: none">
			<label>Select <%=loginfo.getPatientname_field() %>:</label>
			<s:textfield name="client" id="client"  readonly="true" cssClass="form-control" onclick="showPopUpDetails()" data-toggle="modal" data-target="#clientSearch"/>
			<s:hidden name="clientId" id="clientId"></s:hidden>
			<s:hidden name="ipdadmissionid" id="ipdaddmissionid"></s:hidden>
			<label id = "clientError" class="text-danger"></label>
			<br>
			<%-- <label>Department:</label>
			<s:if test="%{#locationList != 'null'}">
									<s:select name="location" id="location" list="locationList" headerKey="0" headerValue="Select Location"
										listKey="id" listValue="location" theme="simple"
										cssClass="form-control chosen-select" />
								</s:if>
									<label id = "locError" class="text-danger"></label> --%>
		</div>
		<div class= "col-lg-4 col-md-4 col-xs-4 col-sm-4" >
			<input style="display: none;" type="radio" id="payBuy1" name="payby" value="0" checked="checked"> <label> <%=loginfo.getPatientname_field()%>  Account : </label> <br>
			<label id="clientDetailsId"></label>
			<br>
			<label style="padding-top: 20px">Date:</label><br>
			<s:textfield onchange="setChargeDate(this.value)" readonly="true" theme="simple" id="date" name="date" placeholder="Select Date" cssClass="form-control dato" />
			
		</div>
		 <div class= "col-lg-4 col-md-4 col-xs-4 col-sm-4" >
		<%-- <label>Addmission Date:</label>&nbsp;
			<s:property value="admissionDate"/><br>
		  <s:if test="payee==1">  
		 
			<input style="display: none;" type="radio"	id="payBuy2" name="payby" value="1"> <label>Third Party Account : </label> <br>
			<label id="tpDetailsId"></label>
			<s:select list="patientPackageList"  name="packageids" listKey="id" listValue="name" cssClass="form-control chosen-select dato" id="packageids" headerKey="0" 
			headerValue="Select Package" onchange="addpackageoncreatecharge(this.value)"/>
			<%if(loginfo.getIskunal()==1){ %>
	<s:if test="invoicecreated">
	<span><input type="button" value="Modify  Packages" onclick="showpkgsapplied()" class='btn btn-primary' disabled="disabled"></span>
	</s:if>
	<s:else>
	<span><input type="button" value="Modify  Packages" onclick="showpkgsapplied()" class='btn btn-primary'></span>
	</s:else>
		
		<%} %>
			
		
		
		 </s:if>  
		  --%>
		 <input style="display: none;" type="radio"	id="payBuy2" name="payby" value="0"> <label> Account : </label> <br>
			<label id="tpDetailsId"></label>
			<s:select list="patientPackageList"  name="packageids" listKey="id" listValue="name" cssClass="form-control chosen-select dato" id="packageids" headerKey="0" 
			headerValue="Select Package" onchange="addpackageoncreatecharge(this.value)"/>
	<%-- <s:if test="invoicecreated">
	<span><input type="button" value="Modify  Packages" onclick="showpkgsapplied()" class='btn btn-primary' disabled="disabled"></span>
	</s:if>
	<s:else>
	<span><input type="button" value="Modify  Packages" onclick="showpkgsapplied()" class='btn btn-primary'></span>
	</s:else> --%>
		
</div>
		
		<label id = "paybyError" class="text-danger"></label>
		
	</div>

	<%-- <div class="row col-lg-12 col-md-12">
	<div class= "col-lg-6 col-md-6 col-xs-8 col-sm-8" style="padding-bottom: 15px !important">
	<s:select list="patientPackageList"  name="packageids" listKey="id" listValue="name" cssClass="form-control chosen-select" id="packageids" headerKey="0" headerValue="Select Package" onchange="addpackageoncreatecharge(this.value)"/>
	
	</div> --%>
	
	</div>
	
	<hr/>
	<input type="hidden" name="packageid" id="packageid">
	
	
</div>
</div>
		
<div class="">
	<div class="">
	<table class="table table-bordered widthdebicha" cellspacing="0">
										<thead>
											<tr>
												
												<th style="width: 9%;" class="hidden">Paid By</th>
												<th class="chartype" style="width: 30%;">Charge Type</th>
												<th style="width: 40%;">Charge Name</th>
												<th class="text-center">Qty</th>
												<th class="text-right" style="width: 10%;">Unit Price</th>
												<th class="amount text-right" style="width: 7%;">Amount</th>
												<th class="amount text-right" style="width: 7%;">Tax Amount</th>
													
											</tr>
										</thead>


										<tbody>
												<tr>

													<td class="hidden">
													<s:if test="payee==0">
													<input type="radio" id="payBuy" name="payBuy" value="0" checked="checked"  style="display: inline; float: left;padding: 3px;"> <label for="payBuy" style="display: inline;padding: 3px;">Self</label><hr>
														<input type="radio" id="payBuy1" name="payBuy" value="1" style="display: inline; float: left;padding: 3px;"><label for="payBuy" style="display: inline; float: left;padding: 3px;">TP</label>
														
													</s:if>
													<s:else>
													<input type="radio" id="payBuy" name="payBuy" value="0"   style="display: inline; float: left;padding: 3px;"> <label for="payBuy" style="display: inline;padding: 3px;">Self</label><hr>
														<input type="radio" id="payBuy1" name="payBuy" value="1" checked="checked" style="display: inline; float: left;padding: 3px;"><label for="payBuy" style="display: inline; float: left;padding: 3px;">TP</label>
														
													</s:else>
														
														<s:hidden name="tpid" id="tpid"></s:hidden>
														<s:hidden name="payee" id="payee"></s:hidden>
														
													</td>
													<td>
														<div >
														<s:select theme="simple" name="masterchargetype" id="masterchargetype" list="masterChageTypeList" listKey="name" listValue="name"
															headerKey="0" headerValue="Select Additional Charge Type" cssClass="form-control showToolTip chosen-select"
															onchange="filterCharges(this.value)"/>
														</div>
														<s:hidden id="isindisharecharge" value="0"></s:hidden>
														<div id="visitingconsltantdiv">
															<s:hidden id="consultantdr" value="0"></s:hidden>
														</div>
													</td>
													<td>
														<div id="additionalChargeAjax" style="margin-bottom: 2px;">
														<s:select theme="simple" name="apmtType" id="chargeTYpe" list="additionalChargesList" listKey="id" listValue="name"
														headerKey="0" headerValue="Select Additional Charge Type" cssClass="form-control showToolTip chosen-select"
														onchange="setAdditionalChargeAjax1(this.value)"/>
														</div>
														
														<%if(!loginfo.isAdd_manual_charge()){ %>
														<input type="text" onchange="calcmanualcharge()"  name="mannualcharge"  id="mannualcharge"
														 class="form-control" placeholder="Manual Charge" readonly="">
														<%}else{ %>
														<input type="text" onchange="calcmanualcharge()"  name="mannualcharge"  id="mannualcharge"
														 class="form-control" placeholder="Manual Charge">
														<%} %>
														
														 <input type="hidden" name="compulsaryconsultant" id="compulsaryconsultant" value="0">
													</td>
													<td><input onchange="calcamount()" style="width: 66px;text-align: center;" type="text" id="quantity" name="quantity" 
														maxlength="3" value="1" class="form-control"></td>
													<td>
													<div  id="chargeajax">
														<input  type="text" onchange="calcmanualcharge()" onkeypress="" style="text-align:right;" id="charge" name="charge" class="form-control" />
													</div>
													
													<input type="hidden" name="hdncharge" id="hdncharge">
													
													</td>
													<td id="amount" class="text-right"></td>
													<td id="taxammt" class="text-right"></td>
												</tr>
											
												
											
										</tbody>
									</table>
									
									<div class="row addchabtn" style="margin-top: 10px;margin-left: -10px;" >
									<div class="col-lg-12 col-md-12" style="margin-top: -26px">
									<input id="addchargebtn" type="button" value="Add Charge" onclick="setChargeAmount()" disabled="disabled" class="btn btn-primary" style="float:right;">
									</div>
									<%
									String pcs="hidden";
									if(loginfo.getClinicUserid().equals("pcsadmin")){ 
									pcs="";
									}
									%>
									
									<div class="col-lg-12 col-md-12 <%=pcs %>"  >
									<p><label>Charge Description</label></p>
									
									<div class="col-lg-6 col-md-6" >	
									<textarea class="form-control" rows="5"  name='chargedescriptionnew'  id="chargedescriptionnew"></textarea>
									</div> 	 
									
									<div class="col-lg-6 col-md-6" >
									<h4><b> Taxes</b></h4>	 
									<s:iterator value="taxlist">
									<div class="form-inline">
										<input type="checkbox" class="form-control lokclass" value='<s:property value="percent"/>' onclick="calcamount()" id='<s:property value="type"/>'>
										
										<label ><s:property value="name"/>  [<s:property value="percent"/> %]</label>
									</div>
									</s:iterator>
									<input type="hidden" name='taxtypes' id='taxtypes' value='0'>
									</div>
									
									<br>
									<br>
									</div>
									 
									</div>
			
	


<label id = "recerror" class="text-danger"></label>

<div class="row" style="margin-top: -18px">
<div class="col-lg-12">
<label>View All Charges</label>
<table class="table table-bordered widthdebicha" cellspacing="0">
										<thead>
											<tr>
											<th style="width: 11%;">Date</th>
											<th style="width: 9%;" class="hidden">Paid By</th>
												<th class="chartype" style="width: 20%;">Charge Type</th>
												<th style="width: 35%;">Charge Name</th>
												<th class="text-center" style="width: 9%;">Qty</th>
												<th class="text-right" style="width: 9%;">Unit Price</th>
												<th class="amount text-right" style="width: 7%;">Amount</th>
												
												<th></th>

											</tr>
										</thead>


										<tbody id="cashDesk">
											
												
											
										</tbody>
									</table>
									
</div>
</div>



<div class="" >

<div class="">
<div class="col-lg-2 col-md-2 col-xs-12 col-sm-2" style="padding-left: 0px;">	

<s:submit id="createchargebtn"  value="Create Charge" cssClass="btn btn-primary"  theme="simple" onclick="hidebtn()"></s:submit>

</s:form>
</div>
<div class="col-lg-2 col-md-2 col-xs-12 col-sm-2" style="padding-left: 0px;">	
				<s:form action="createInvoiceAdditional" id = "createInvoiceAdditional">
					<s:hidden name="clientId" id="hdnciclientid"/>
					<s:hidden name="client" id="hdncliclient" />
					<s:hidden name="apmtType" id="hdnciapmttype"/>
					<s:hidden name = "location" id="hdnciloc"></s:hidden>
					<s:hidden name= "payby" id="hdncipayby" ></s:hidden>
					<s:hidden name= "date" id="hdncrinvdate" ></s:hidden>
					
					<!-- <input type="submit" id="createinvoicebtn" class="btn btn-primary" value=" Create Invoice "> -->
				</s:form>
		</div>   
		<div class="col-lg-2 col-md-2 col-xs-12 col-sm-2" style="padding-left: 0px;">	
				<s:form action="casdeskAdditional" id = "casdeskAdditional">
					
					<s:hidden name="clientId" id="hdncdclientid" />
					<s:hidden name="client" id="hdncdclient" />
					<s:hidden name="apmtType" id="hdncdapmttype"/>
					<s:hidden name = "location" id="hdncdloc"></s:hidden>
					<s:hidden name = "date" id="hdncddate"></s:hidden>
					<s:hidden name = "date1" id="hdncidate"></s:hidden>
					
					<s:hidden name= "payby" id="hdncdpayby" ></s:hidden>
					<s:hidden name = "creditDebitCharge" id="casdeskAdditional_creditDebitCharge"></s:hidden>
					<%if(loginfo.isCash_desk() || loginfo.getUserType()==2){ %>
						<input type="submit"  class="btn btn-primary hidden" value=" Cash Desk " >
					<%} %>
				</s:form>
		</div>
		<s:hidden id="location" value="0"></s:hidden>
		<%-- <div class="col-lg-2 col-md-2 col-xs-12 col-sm-2" style="padding-left: 0px;">	
		
			<s:form action="estimateCharges" theme="simple" id="estimatefrm" target="formtarget">
					<s:hidden name="clientId" id="estimateclientid"/>
					<s:hidden name="actionType" value="0"/>
					
						<button type="button" class="btn btn-primary" 
							onclick="createdebitestimate()">View
							Estimate</button>
					
				</s:form>
		</div> --%>
		

</div>



		
		  
		<br><br><br>
		
</div>
<!-- edit package -->
<div class="modal fade" id="editpack" tabindex="-1" role="dialog"
	aria-labelledby="lblsemdsmspopup" aria-hidden="false" data-keyboard="false" data-backdrop="static" style="background-color: rgba(0, 0, 0, 0.5);">
  <div class="modal-dialog">
    Modal content
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Edit Applied Packages</h4>
      </div>
      <form action="savepkgDataeditIpdDashboard">
      	<div class="modal-body">
     		
     			<div id ="appliedpkglistdiv" style="width: 50%">
			
      		</div>
      		<div style="width: 100%">
      		<br>
      		<input type="hidden" name='hidenipdid' id='hidenipdid'>
      		<input type="hidden" name="lastipdid" id="lastipdid" value="<s:property value="lastipdid" />">
      		<div class='form-inline'>
      		<label>From date :</label><span ><input type="text" name='fdt1' id='fdt1'  class='form-control' style="width: 14%" ></span>
      		 <s:select name="hrpkg1" theme="simple" id="hrpkg1" list="hourList" cssClass="form-control" headerKey="0" headerValue="Select" cssStyle="width:7%;"/>
      		  <s:select name="mntpkg1" theme="simple" id="mntpkg1" list="minuteList" cssClass="form-control" headerKey="0" headerValue="Select" cssStyle="width:7%;"/>
      	
      		<label>To date :</label><span><input type="text" name='tdt1' id='tdt1' class='form-control' style="width: 14%" ></span>
      		 <s:select name="hrpkg" theme="simple" id="hrpkg" list="hourList" cssClass="form-control" headerKey="0" headerValue="Select" cssStyle="width:7%;"/>
      		  <s:select name="mntpkg" theme="simple" id="mntpkg" list="minuteList" cssClass="form-control" headerKey="0" headerValue="Select" cssStyle="width:7%;"/>
      		<label>Amount </label> :<input type="number" name='amtpkgedit' class='form-control' id='amtpkgedit' style="width: 10%">		
      		</div>
      		</div>
     <input type="hidden" name="statementred" value="5">
      	<br>
      	<div id='listeditpkg'>
      	<table class='my-table table'>
      	<thead>
      	<tr>
      	<th>Sr. No.</th>
      	<th>Name</th>
      	<th>Amount</th>
      	
      	</tr>
      	</thead>
      	<tbody id='listeditpkgbody'>
      	
      	</tbody>
      	</table>
      	</div>
      	<div>
      	<input type="submit" class='btn btn-success'>
      	</div>
     	</div>
      </form>
     </div>
     </div>
     </div>
<!-- Modal -->
<div class="modal fade" id="clientSearch" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">Client Search</h4>
      </div>
      <div class="modal-body">
        <%@ include file="/accounts/pages/allClientDetails.jsp"%>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>	
											

											
										</div>
									</div>
								</div>
							</div>
						</div>
<div class="modal fade" style="background: rgba(255, 255, 255, 0.93);" id="dashboardloaderPopup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="">
				<div class="modal-body text-center">
					<img src="common/images/hourglass1.gif" class="img-responsive" style="margin-left:auto;margin-right:auto;"></img>
					
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
<script type="text/javascript">
function hidebtn(){
	$("#dashboardloaderPopup").modal("show");
}
</script>
