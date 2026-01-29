 <%@page import="com.apm.main.common.constants.Constants"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
 <script type="text/javascript" src="appointment/js/ledger.js"></script> 
         
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

<br/>

<div class="row details" style="margin-top: -35px !important;">
                                    <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
                                    <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 oneseticonleft">
         <img src="dashboardicon/Financei.png" class="img-responsive prescripiconcircle">
          </div>
          <div class="col-lg-11 col-md-11 col-sm-11 col-xs-11 titlestleftiocn">
          <h4>Create New Ledger</h4>
          
          
          </div>
                                    </div>
                                </div>
  <!-- Accounts Common Menu -->
                                
                                <div class="row ">
                               	 <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 typedose hidden">
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('ExpenceManagement')" type="button" class="btn btn-success" >Create Voucher</button>
                               			</a>
                               		</div>
                               		
                               		
                               		
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('ledgAppointmentType')" type="button" class="btn btn-success" >Create New Ledger</button>
                               			</a>
                               		</div>
                               		
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('viewledrportAppointmentType')" type="button" class="btn btn-success" >View Ledger Report</button>
                               			</a>
                               		</div>
                               		
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('aheadAppointmentType')" type="button" class="btn btn-success" >Add New Ledger Head</button>
                               			</a>
                               		</div>
                               		
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('ocrptsAppointmentType')" type="button" class="btn btn-success" >Opening / Closing Report</button>
                               			</a>
                               		</div>
                               		
                               		<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2" >
                               			<a href="#">
                               				 <button onclick="opencPopup('tbAppointmentType')" type="button" class="btn btn-success" >Trial Balance</button>
                               			</a>
                               		</div>
                               		
                               	</div>
                                	
                                <div class="row hidden">
                               	 <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 typedose">
                               	 	<hr/>
                               	 </div>
                               	</div>		
                                  
                                

<s:form action="ledgAppointmentType" id="servicefrm" theme="simple"> 
<s:hidden name="ledgername" id="selectedledgernameid"/>
</s:form>

<s:form action="newledgAppointmentType" id="newledgerfrm" theme="simple"> 
<s:hidden name="hdnnewledger" id="hdnnewledger"/>
<s:hidden name="hdnhowpaid" id="hdnhowpaid"/>
<s:hidden name="bnkname" id="bnkname"/>
<s:hidden name="ltype" id="ltype"/>

<s:hidden name="aheadname" id="aheadname"/>
<s:hidden name="opbal" id="opbal"/>



</s:form>


<s:form action="sveledAppointmentType" id="ledgerfrm" theme="simple"> 
		
                                <br>
		 <div class="row">
			<div class="col-lg-12">
			
			<div class="col-lg-2 padingleftrightzeroi hidden">
			 
    <button onclick="openDisplayPopup('aheadAppointmentType')" type="button" class="btn btn-primary addvoucher" id="btngo">Add Account Head</button>
				 
			</div>
				
				<div class="col-lg-2">
						<s:select name="ledgername" id="ledgername" list="ledgerList" listKey="id" listValue="name"
			headerKey="0" headerValue="Select Ledger" cssClass="form-control chosen-select"
			onchange="showSelectedServices()" style="width: 289px !important;"
			/>
			</div>
			
			<div class="col-lg-2">
					<input type="text" name="newledger" id="newledger" class="form-control"
					placeholder="Enter New Ledger"/>
					
			</div>
			<div class="col-lg-2">
			 <select name="howpaid" id="howpaid" class="form-control" >
				<option value="0">Select</option>
				<option value="Cash">Cash</option>
				<option value="Cheque">Cheque</option>
				<option value="NEFT">NEFT</option>
				<!--<option value="BACS">BACS</option>-->
				<!-- <option value="C/Card">Credit Card</option> -->
				<option value="D/Card">Card</option>
				<option value="Other">Other</option>
				<option value="<%=Constants.PREPYMENT %>"><%=Constants.PREPYMENT %></option>
			</select> 
			</div>
			
			
			<div class="col-lg-2">
				 <s:select name="xltype" id="xltype" 
				list="#{'0':'Select Ledger Type','1':'Journal','2':'Bank','3':'Cash'}"
				cssClass="form-control chosen-select" onchange="setActionForAll()"></s:select>
			</div>
			
			<div class="col-lg-2">
				  <s:select name="hdnbnkname" id="hdnbnkname" list="bankNameList"
		  cssClass="form-control" listKey="id" listValue="name"
		  headerKey="0" headerValue="Select Bank"
		  />	
			</div>
			
			
			
			
			</div>
		</div>
		<br/>
		 <div class="row">
			<div class="col-lg-12">
				<div class="col-lg-3">
						<s:select name="aheadnamex" id="aheadnamex" list="aheadNameList" listKey="id" listValue="name"
			headerKey="0" headerValue="Select Account Head" cssClass="form-control chosen-select"
			onchange="showSelectedAheadServices()"
			/>
			</div>
			
			<div class="col-lg-3">
					<input type="number" name="opbalx" id="opbalx"
					class="form-control" value="0"/>
		
			</div>
			
			<div class="col-lg-1">
				 <button onclick="addnewLedger()" type="button" class="btn btn-primary addvoucher" id="btngo">Add</button>
				 
				 
			</div>
			</div>
		</div>
		 <div class="row hidden">
			<div class="col-lg-12">
			
			<div class="col-lg-2">
				
				 <button onclick="viewledgerreport()" type="button" class="btn btn-primary addvoucher" id="btngo">View Ledger Report</button>
				
			 
				
			</div>
			 <div class="col-lg-1">
    
    <button onclick="opencPopup('ExpenceManagement')"  type="button" class="btn btn-primary addvoucher" id="btngo">Voucher</button>
    
     
   </div>
			
			</div>
		</div>
		
		
		<br/>



		<div class="scroll">
		<table class="my-table tablexls" id ="chargestbl1" style="width: 100%;font-size: 8px">
							<thead>
							<tr>
					
					
		
					    	<th>Services</th>
						
						</tr>
						</thead>
					
							<tbody>
					 	<s:iterator value="ledgerserviceList" status="rowstatus">
							<tr>
								<td>
									<input type="checkbox" id="chledger<s:property value="id"/>" name="chledger" class="ledgercase"
									value="<s:property value="id"/>"
									>
									<s:property value="name"/>
								</td>
								
							</tr>
						
						</s:iterator> 

					
							</tbody>
							
						</table>
						</div>
						
					<br>	
					
					
					
					<s:hidden name="hdnledgerserviceid" id="hdnledgerserviceid"/>
					<s:hidden name="dbselectedservices" id="dbselectedservices"/>
					
					
					 <div class="row col-lg-12 col-md-12 col-xs-12 col-sm-12">
		            <div class="form-group text-right hidden-print">
                                    	
                                    	<button onclick="saveLedgerServices()" type="button" class="btn btn-primary addvoucher lg" id="btngo" title="Print">Save</button>
                                    	 
                                    </div>
                              </div> 
                              
                  </s:form>
                  
                  
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
                        