<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.apm.ThirdParties.web.form.*"%>
<script type="text/javascript" src="thirdParties/js/addThirdPartyValidaton.js"></script>


<style>


.tab-content {
	min-height: 515px;
}

	.nav-tabs { border-bottom: 2px solid #DDD;background-color: #f5f5f5; }
	.nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover { border-width: 0; }
	.nav-tabs > li > a { border: none; color: #666; }
	.nav-tabs > li.active > a, .nav-tabs > li > a:hover { border: none; color: #339966 !important; background: transparent; }
	.nav-tabs > li > a::after { content: ""; background: #339966; height: 2px; position: absolute; width: 100%; left: 0px; bottom: -1px; transition: all 250ms ease 0s; transform: scale(0); }
	.nav-tabs > li.active > a::after, .nav-tabs > li:hover > a::after { transform: scale(1); }
	.tab-nav > li > a::after { background: #21527d none repeat scroll 0% 0%; color: #fff; }
	.tab-pane { padding: 15px 0; }
	.tab-content{padding: 0px 10px 0px 10px;}
	
	.card {background: #FFF none repeat scroll 0% 0%; box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.3); margin-bottom: 30px; }
	h4 {
    font-size: 15px;
}

ul {
    margin-top: 0;
    margin-bottom: 10px;
    padding: 0px;
    list-style: none;
}
.minhewithbor{
	    min-height: 180px;
	    border-right:1px solid #ddd;
}
</style>
	
	
	
		
		<s:form action="saveTPDataThirdParty" theme="simple">
		<div class="col-lg-1 col-md-1"></div>
		<div class="col-lg-10 col-md-10 col-xs-12">
		<div class="col-lg-12 col-sm-12 details" style="padding-top: 12px;"><h4 style="margin-top: 7px;font-size: 14px;">Add New Third Party (Insurance Co/ Groups)</h4></div>
		
			
			<div class="card">
					<ul class="nav nav-tabs" role="tablist">
                             <li role="presentation" class="active"><a href="#one" aria-controls="one" role="tab" data-toggle="tab">TP/GP Name/Address</a></li>
                             <li role="presentation"><a href="#two" aria-controls="two" role="tab" data-toggle="tab">Account Setting</a></li>
                            
                        </ul>
                        <!-- Tab panes -->
                        <div class="tab-content">
                                 <div role="tabpanel" class="tab-pane active" id="one">
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>TP Type (Surgery / Insurance Co /Group)<span class="text-danger">*</span></label><br>
													<s:select id="type1" name="type" list="thirdPartyTypeList"
													listKey="id" listValue="type" headerValue="Select TP Type"
													headerKey="0" cssClass="form-control showToolTip chosen-select"
													data-toggle="tooltip" onchange="setTypeName(this.value)" />
													<label  id = "tpTypeError" class="text-danger"></label>	
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
												<label>TP Name<span class="text-danger">*</span></label><br>
													<s:select id="typeName1" name="typeName" list="tpnameList"
													listKey="id" listValue="typeName" headerValue="Select InsuranceCo/Surgery Name"
													headerKey="0" cssClass="form-control showToolTip chosen-select"
													data-toggle="tooltip" onchange="disableFiled(this.value)" />
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>TP Name<span class="text-danger">*</span></label><br>
													<s:if test="typeName==0">	
													<s:textfield id="companyName" name="companyName" theme="simple"
														cssClass="form-control showToolTip" data-toggle="tooltip"
														placeholder="Enter New InsuranceCo/Surgery Name"
														title="Enter New InsuranceCo/Surgery Name" onblur="initialFirstCap(this);" />
												</s:if>
												<s:else>
													<s:textfield id="companyName" name="companyName" theme="simple"
														cssClass="form-control showToolTip" data-toggle="tooltip"
														placeholder="Enter New InsuranceCo/Surgery Name"
														title="Enter New InsuranceCo/Surgery Name" disabled="true" onblur="initialFirstCap(this);" />
												</s:else>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Is Main TP </label><br>
													 <s:checkbox name="maintp" id="maintp"></s:checkbox> 	
											</div>
                                 		</div>
                                 		
                                 		
                                 	</div>
                                 	
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>Short Name</label><br>
													<s:textfield id="shortname" name="shortname" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Short Name" title="Enter Short Name" onblur="" />
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
												<label>First Line of Address</label><br>
													<s:textfield id="tpaddress" name="address" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Address" title="Enter Address" onblur="initialFirstCap(this);" />
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Second Line of Address</label><br>
													<s:textfield name="secondLineAddress" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Second Line Address" title="Enter Second Line Address" onblur="initialFirstCap(this);" />
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 		</div>
                                 	</div>
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>Town/City</label><br>
													<s:textfield id="tptown" name="town" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Town" title="Enter Town"  onblur="initialFirstCap(this);" />
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
												<label>Country</label><br>
													<s:if test="%{#countryList != 'null'}" >
				   			<s:select id="tpcountry" name="country" list="countryList" headerKey="0" headerValue="Select Country" 
				   			labelposition="left" title="Select your country from list" theme="simple" cssClass="form-control showToolTip chosen-select"
							data-toggle="tooltip"   />
		   	   				</s:if>	
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Post Code</label><br>
													<s:textfield id="tppostcode" name="postcode" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Post Code" title="Enter Post Code" onblur="initialCap(this);"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 		</div>
                                 	</div>
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>Direct Phone No.</label><br>
													<s:textfield id="telephoneLine" name="telephoneLine"
								theme="simple" cssClass="form-control showToolTip"
								data-toggle="tooltip" placeholder="Enter Phone No."
								title="Direct Telephone No."
								onchange="checTelePhoneLineValidation(this.value)" />
								<label id = "telephoneLineError" class="text-danger"></label>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
												<label>Fax</label><br>
													<s:textfield id = "fax" name = "fax" theme="simple" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Fax" title="Enter Fax"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Website</label><br>
													<s:textfield id = "web" name = "web" theme="simple" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Website" title="Enter Website"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 		</div>
                                 	</div>
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>Email</label><br>
													<s:textfield id="compnyEmail" name="compnyEmail" theme="simple"
								cssClass="form-control showToolTip" data-toggle="tooltip"
								placeholder="Enter Email" title="Enter Email ID"
								onchange="checkComapnyEmailValidation(this.value)" />
										 			<label id = "compnyEmailError" class="text-danger"></label>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
												<label>Email-CC</label><br>
													<s:textfield id = "emailCc" name = "emailCc" theme="simple" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Email ID" title="Enter Eamil-Cc"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Unit</label><br>
													<s:textfield id = "unit" name = "unit" theme="simple" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Unit" title="Enter Unit"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 		</div>
                                 	</div>
                                 	<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 		<div class="col-lg-6 col-md-6 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
												<label>Note</label><br>
													<s:textarea placeholder="TP Note" rows="5" name = "tpAccountSettingNotes" id = "tpAccountSettingNotes" cssClass = "form-control showToolTip" onblur="initialFirstCap(this);"></s:textarea>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			<div class="form-group">
												<label>Area</label><br>
													<s:textfield id = "area" name = "area" theme="simple" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Area" title="Enter Area"/>
											</div>
                                 		</div>
                                 		<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 		</div>
                                 	</div>
                                 	
                                 	
                                 	
                                 	
                                 </div>
                                
                                 <div role="tabpanel" class="tab-pane" id="two">
                                 
                                  <div id = "accountSetting">
                                 		<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 			<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
													<label>Agreed Credit Limit</label><br>
													<s:textfield name = "outInvoiceLimit" id = "outInvoiceLimit" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "00.00" title="Enter Credit Limit"/>
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Credit Reminder Limit</label><br>
													<s:textfield name = "accountWarningLimit" id = "accountWarningLimit" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "00.00" title="Enter Credit Reminder limit"/>
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Agreed Credit Duration</label><br>
													<s:textfield name = "creditDuration" id = "creditDuration" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Value in Days" title="Enter Agreed Duration In Days"/>
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Credit Reminder Duration</label><br>
													<s:textfield name = "creditReminderDuration" id = "creditReminderDuration" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Value in Days" title="Enter Credit Reminder Duration In Days"/>
												</div>
                                 			</div>
                                 		</div>
                                 		<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 			<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
													<label>DNA Limit</label><br>
													<s:textfield name = "dnaLimit" id = "dnaLimit" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter DNA Limit"/>
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Checked DNA Limit For All</label><br>
													<s:checkbox name="dnaForAll" id="dnaForAll"/>
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Ipd Share To Practitioner in %</label><br>
													<s:textfield name = "ipdShare" id = "ipdShare" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Ipd Share" title="Enter Ipd Share" />
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 				<div class="form-group">
													<label>Surgeon Share in %</label><br>
													<s:textfield name = "surgeonShare" id = "surgeonShare" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Surgeon Share" title="Enter Surgeon Share"/>
												</div>
                                 			</div>
                                 		</div>
                                 		<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 			<div class="col-lg-3 col-md-3 col-xs-12" style="padding-left: 0px;">
                                 				<div class="form-group">
													<label>Opd Share To Practitioner in %</label><br>
													<s:textfield name = "opdShare" id = "opdShare" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter Opd Share" title="Enter Opd Share" />
												</div>
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 			</div>
                                 			<div class="col-lg-3 col-md-3 col-xs-12">
                                 			
                                 			</div>
                                 		</div>
                                 		<div class="col-lg-12" style="background-color: #ccc;padding: 4px 0px 0px 6px;margin-bottom: 10px;">
											<label>Appointment Charges</label>
										</div>
                                 		<div class="col-lg-12 col-md-12 col-xs-12" style="padding:0px;">
                                 			<div class="panel panel-primary">
					<div class="panel-body">
					<div class="row" style="margin-left: 0px;margin-right: 0px;">
						<div class="col-lg-3 col-md-3" style="border-right: solid 1px #948E8E;margin-top: -5px;    background-color: #efefef;min-height: 43px;">
							<label>Default Appointment Name</label>
							
						</div>
						<div class="col-lg-3 col-md-3" style="border-right: solid 1px #948E8E;margin-top: -5px;    background-color: #efefef;min-height: 43px;">
							<label>Appointment Name</label>
							
						</div>
						<div class="col-lg-1 col-md-1" style="border-right: solid 1px #948E8E;min-height: 44px;margin-top: -5px;    background-color: #efefef;">
							
							<label>DNA Offset</label>
						</div>
						<div class="col-lg-3 col-md-3" style="border-right: solid 1px #948E8E;margin-top: -5px;    background-color: #efefef;">
								<label style="margin-left: 34px;">Appointment Charges For</label><br>
							<div class="row"style="margin-top: -3px; background-color: antiquewhite; padding: 2px 0px 0px;">
								<div class="col-lg-6 col-md-6" style="border-right: solid 1px #948E8E;">
							
									<label>DNA</label>
								</div>
								<div class="col-lg-6 col-md-6">
								
									<label>Completed</label>
								</div>
							</div>
						</div>
					
						<div class="col-lg-2 col-md-2" style="margin-top: -5px;background-color: #efefef;min-height: 43px;">
							<label>Duration</label>
						</div>
					</div>
					
					<br>
					 
						
							<div class="row">
									<div class="col-lg-3 col-md-3">
										<label>Initial Appointment</label>
									</div>
									<div class="col-lg-3 col-md-3">
		 								<s:textfield onblur="initialFirstCap(this);" name = "dnaInitialApmt" id = "dnaInitialApmt" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter name 15 char max" title="Enter Initail Apmt charges"/>
									</div>
									
									<div class="col-lg-1 col-md-1">
										<s:checkbox name="initialOffsetdna" id="initialOffsetdna"/>
									</div>
									
									<div class="col-lg-3 col-md-3">
										<div class="row">
											<div class="col-lg-6 col-md-6">
		 										<s:textfield cssStyle="text-align:center" maxlength="6" name = "dnaInitialCharge" id = "dnaInitialCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Initail Apmt charges"/>
											</div>
											<div class="col-lg-6 col-md-6">
		 										<s:textfield cssStyle="text-align:center" maxlength="6" name = "completedInitialCharge" id = "completedInitialCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Initail Apmt charges"/>
											</div>
										</div>
									</div>
									
									<div class="col-lg-2 col-md-2">
		 									<s:if test="%{#apmtDurationList != 'null'}">
											<s:select id="compltInitialApmtDuration" name="compltInitialApmtDuration" list="apmtDurationList"
												headerKey="0" headerValue="00:00" theme="simple"
												cssClass="showToolTip form-control" data-toggle="tooltip"
												title="Select Duration" />
										</s:if>
									</div>
									
							</div><br> 
							
							
							
							<div class="row">
									<div class="col-lg-3 col-md-3">
										<label>Follow-up Appointment</label>
									
									</div>
									<div class="col-lg-3 col-md-3">
		 								<s:textfield onblur="initialFirstCap(this);"  name = "dnafollowupApmt" id = "dnafollowupApmt" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter name 15 char max" title="Enter FollowUP Apmt charges"/>
									</div>
								
									<div class="col-lg-1 col-md-1">
										<s:checkbox name="followupOffsetdna" id="followupOffsetdna"/>
									</div>
									
									
									<div class="col-lg-3 col-md-3">
										<div class="row">
											<div class="col-lg-6 col-md-6">
			 									<s:textfield cssStyle="text-align:center" maxlength="6" name = "dnaFollowupCharge" id = "dnaFollowupCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter FollowUP Apmt charges"/>
											</div>
											<div class="col-lg-6 col-md-6">
				 								<s:textfield cssStyle="text-align:center" maxlength="6" name = "completedFollowupCharge" id = "completedFollowupCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter FollowUP Apmt charges"/>
											</div>
										</div>
									</div>
									
									<div class="col-lg-2 col-md-2">
		 									<s:if test="%{#apmtDurationList != 'null'}">
											<s:select id="compltfollowupApmtDuration" name="compltfollowupApmtDuration" list="apmtDurationList"
												headerKey="0" headerValue="00:00" theme="simple"
												cssClass="showToolTip form-control" data-toggle="tooltip"
												title="Select Duration" />
										</s:if>
									</div>
									
							</div><br>
							
							
							<div class="row">
									<div class="col-lg-3 col-md-3">
										<label>Final Appointment</label>
									
									</div>
									<div class="col-lg-3 col-md-3">
		 								<s:textfield onblur="initialFirstCap(this);"  name = "dnafinalApmt" id = "dnafinalApmt" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter name 15 char max" title="Enter Final Apmt charges"/>
									</div>
									
									<div class="col-lg-1 col-md-1">
										<s:checkbox name="finalOffsetdna" id="finalOffsetdna"/>
									</div>
									
									
									<div class="col-lg-3 col-md-3">
										<div class="row">
											<div class="col-lg-6 col-md-6">
				 								<s:textfield cssStyle="text-align:center" maxlength="6" name = "dnaFinalCharge" id = "dnaFinalCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Final Apmt charges"/>
											</div>
											<div class="col-lg-6 col-md-6">
				 								<s:textfield cssStyle="text-align:center" maxlength="6" name = "completedFinalCharge" id = "completedFinalCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Final Apmt charges"/>
											</div>
										</div>
									</div>
									
									<div class="col-lg-2 col-md-2">
		 								<s:if test="%{#apmtDurationList != 'null'}">
											<s:select id="compltfinalApmtDuration" name="compltfinalApmtDuration" list="apmtDurationList"
												headerKey="0" headerValue="00:00" theme="simple"
												cssClass="showToolTip form-control" data-toggle="tooltip"
												title="Select Duration" />
										</s:if>
										
									</div>
							</div> 
							<br>
							
							 <div class="row">
									<div class="col-lg-3 col-md-3">
										<label>Maintenance</label>
									
									</div>
									<div class="col-lg-3 col-md-3">
		 								<s:textfield onblur="initialFirstCap(this);"  name = "dnamaintenanceApmt" id = "dnamaintenanceApmt" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "Enter name 15 char max" title="Enter Maintence charges"/>
									</div>
									
									<div class="col-lg-1 col-md-1">
										<s:checkbox name="maintnanceOffsetdna" id="finalOffsetdna"/>
									</div>
									
									
									<div class="col-lg-3 col-md-3">
										<div class="row">
											<div class="col-lg-6 col-md-6">
				 								<s:textfield cssStyle="text-align:center" maxlength="6" name = "dnaMaintnanceCharge" id = "dnaMaintnanceCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Final Apmt charges"/>
											</div>
											<div class="col-lg-6 col-md-6">
				 								<s:textfield cssStyle="text-align:center" maxlength="6" name = "completedMaintnanceCharge" id = "completedMaintnanceCharge" cssClass="form-control showToolTip" data-toggle = "tooltip" placeholder = "0.00" title="Enter Final Apmt charges"/>
											</div>
										</div>
									</div>
									
									
									<div class="col-lg-2 col-md-2">
		 								<s:if test="%{#apmtDurationList != 'null'}">
											<s:select id="compltmaintenanceApmtDuration" name="compltmaintenanceApmtDuration" list="apmtDurationList"
												headerKey="0" headerValue="00:00" theme="simple"
												cssClass="showToolTip form-control" data-toggle="tooltip"
												title="Select Duration" />
										</s:if>
									</div>
							</div> 
							
							<a onclick="addRow('dynamicallyAddApmtTable')" class="btn btn-primary"><i class="fa fa-plus"></i> Add More</a>
							<a onclick="deleteRow('dynamicallyAddApmtTable')" class="btn btn-primary" id = "deletefiled"><i class="fa fa-trash-o"></i> Delete Row</a>
							<table class="table" id = "dynamicallyAddApmtTable" style="display: none">
								<thead id = "showHeaderBlock">
									<tr>
										<th align="center">Delete</th>
										<th align="center">#</th>
										<th align="center">Appointment Name</th>	
										<th align="center"> DNA Offset</th>	
										<th align="center">DNA Charge</th>	
										<th align="center">Completed Charge</th>	
										<th align="center">Appointment Duration</th>	
									</tr>
								</thead>
								
							
							<tbody>
						<s:hidden name = "danyamiclistpresent" id = "danyamiclistpresent"></s:hidden>
						<s:if test="typeName > 0">
								<%ArrayList<DynamicAppointment>dynamicApmtTypeList = (ArrayList<DynamicAppointment>)session.getAttribute("dynamicApmtTypeList"); 
								int i = 0;
								int count = 1;
							%>
								<% for(DynamicAppointment dynamicAppointment:dynamicApmtTypeList ) {%>
									<tr>
										<td>
										<input type="checkbox" name="chk">
										</td>
            					 		<td> <%=count%> </td>
										<td><input placeholder="Enter name 15 char max" size="50" type="text" id = "dynamicApmt[<%=i%>].dnaName" name="dynamicApmt[<%=i%>].dnaName" value="<%=dynamicAppointment.getDnaName() %>" class="form-control showToolTip dnaName"></td>
										
										 <%if(dynamicAppointment.isOffset()){ %> 
										 	<input type="hidden"  name="dynamicApmt[<%=i%>].offset" id="dynamicApmt[<%=i%>].offset" value="true"/>
											 <td><input type="checkbox" name="chdna<%=i %>" id="chdna<%=i %>" onclick="setDnaOffsetValue(<%=i %>)" checked="checked"> </td>
										<% }else{%>
											<input type="hidden"  name="dynamicApmt[<%=i%>].offset" id="dynamicApmt[<%=i%>].offset" />
											 <td><input type="checkbox" name="chdna<%=i %>" id="chdna<%=i %>" onclick="setDnaOffsetValue(<%=i %>)"> </td>
										<% }%>
										<td><input type="text" style="text-align: center;" maxlength="6" id = "dynamicApmt[<%=i%>].dnaCharge" name="dynamicApmt[<%=i%>].dnaCharge" value="<%=dynamicAppointment.getDnaCharge() %>" class="form-control showToolTip dnaCharge"></td>
											
										<%-- <td><input type="text" name="dynamicApmt[<%=i%>].apmtName" value="<%=dynamicAppointment.getApmtName() %>" class="form-control showToolTip apmtName"></td> --%>
										<td><input type="text" style="text-align: center;" placeholder="0.00" maxlength="6" name="dynamicApmt[<%=i%>].apmtCharge" value="<%=dynamicAppointment.getApmtCharge() %>" class="form-control showToolTip apmtCharge"></td>
										<td>
										<select name="dynamicApmt[<%=i%>].apmtDuaration"  placeholder="0.00" value="<%=dynamicAppointment.getApmtDuaration()%>" class="form-control showToolTip apmtDuaration">
											<option value="<%=dynamicAppointment.getApmtDuaration()%>"><%=dynamicAppointment.getApmtDuaration()%></option>
											
											<option value="00:15">00:15</option>
											<option value="00:30">00:30</option>
											<option value="00:45">00:45</option>
											<option value="01:00">01:00</option>
											<option value="01:15">01:15</option>
											<option value="01:30">01:30</option>
										</select>
										</td>
										<td>
										<input type="hidden" id = "dynamicApmt[<%=i%>].id" name="dynamicApmt[<%=i%>].id" value="<%=dynamicAppointment.getId()%>">
										</td>
									</tr>
								<% i++;%>
								<% count++;%>
										<%} %>
							</s:if>	
							</tbody>
							</table>
						</div>	
						</div>
                                 		
                                 		</div>
                                 		
                                 		
                                 </div>
                                 
                                 </div>
                                 
                        </div>
	</div>
		<s:token />			


<div class="row" id="insurenceorgroupsaveid">
				<div class="col-lg-12 col-md-12">
					<span style="color:red">Note:</span>
				</div>
				<div class="col-lg-10 col-md-10">
					<ul>
						<li>Appointment name allow 15 character max</li>
						<li>Select Offset Check box to Not count/Offset the DNA toward the Consultation Session for the Treatment Episode</li>
					</ul>
				</div>
				<div class="col-lg-2 col-md-2">
						<input style="float: right;" type="submit" class="btn btn-primary" value="Save" onclick="return checkValidations()">
				</div>
				</div>
		</div>
		<div class="col-lg-1 col-md-1"></div>
	
	
	
	
	<div class="">
							<div class="">
								
								<div class="row ">
									<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
										<div>


											


	<div class="">
		
	
		<div class="">
			<div class="">
				<div class="form-group">
					
						
									
					<div class="col-lg-12 col-md-12" id = "gpcontactdiv" style="display: none;">
									<div class="row" style="padding-left: 15px;padding-right: 15px;">
										<div class="col-lg-12" style="background-color: #65c4fd">
											<label>Contact Details</label>
										</div>
									</div>
							<div class="panel panel-primary">
							<div class="panel-body">
							<div class="row">
								<div class="col-lg-3 col-md-3">
									<label>Contact/GP Name</label><label><span
										class="text-danger">*</span></label>
									<s:textfield id="gpname" name="gpname" title="Enter GP Name"
										cssClass="form-control showToolTip" data-toggle="tooltip"
										placeholder="Enter GP Name" onblur="initialFirstCap(this);" />
								</div>

								<div class="col-lg-3 col-md-3">
									<label>Work Phone No.</label>
									<s:textfield id="workphno" name="workphno"
										title="Enter work ph no" cssClass="form-control showToolTip"
										data-toggle="tooltip" placeholder="Enter work ph no" />
								</div>



								<div class="col-lg-3 col-md-3">
									<label>Email</label>
									<s:textfield id="gpemailid" name="gpemailid"
										title="Enter Email" cssClass="form-control showToolTip"
										data-toggle="tooltip" placeholder="Enter Email" />
								</div>

								<div class="col-lg-3 col-md-3">
									<label>Fax</label>
									<s:textfield id="gpfax" name="gpfax" title="Enter Fax"
										cssClass="form-control showToolTip" data-toggle="tooltip"
										placeholder="Enter Fax" />
								</div>
								
							</div>
							<br>
							<div class="row">
								<div class="col-lg-6 col-md-6">

									<label>Notes</label>

									<s:textarea id="notes" name="notes" title="Enter Notes"
										cssClass="form-control showToolTip" data-toggle="tooltip"
										placeholder="Enter notes" onblur="initialFirstCap(this);"></s:textarea>
								</div>
								<div class="col-lg-3 col-md-3" style="margin-top: 38px;" id="doctorsurgerysaveid">
									<input type="submit" class="btn btn-primary" value="  Save  " onclick="return checkValidations()">
								</div>

							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>
</div>	
	
	</s:form>
	
					
			
		
		
		
		
			<!--<div class="row" id = "savetpbtn">
					<div class="col-lg-2 col-md-2"></div>
				</div> -->
		


<s:form action="editTPDetailsNewThirdParty" id="tpnamefrm">
	<input type="hidden" name="selectedid" id="selectedid">
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


	
			
