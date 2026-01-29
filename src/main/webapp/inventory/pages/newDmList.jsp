<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>
<script type="text/javascript" src="report/js/report.js"></script>
<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>
<script type="text/javascript" src="common/js/pagination.js"></script>
<SCRIPT type="text/javascript" src="inventory/js/procurement.js"></SCRIPT>
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

										<h4>Delivery Memo List</h4>

									</div>
								</div>

<div class="row ">
<%
									LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		   						%>
    
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
 <s:form theme="simple" action="newDmListProcurement" method="post">
<div class="form-inline">
 
                        <div class="col-md-12" style="padding: 0px;">
                        <div class="col-lg-10 col-md-10 col-sm-10">
                        
                        <div class="form-group">
	                           		<span class="text-uppercase"><b>DM LIST</b> &nbsp; - &nbsp;</span>
	                           </div>
                        <div class="form-group" style="width: 7%;">
                        	<s:textfield id="fromDate" name="fromdate"  cssClass="form-control" placeholder="From Date" cssStyle="width:100%;"/>
                        </div>
                         &nbsp; - &nbsp;
                        <div class="form-group" >
                        	<s:textfield  name="todate" id="toDate"  cssClass="form-control" placeholder="To Date" cssStyle="width:100%;"/>
                        </div>
                        <div class="form-group">
                        <s:select  style="width:25%" list="vendorList" id="vendorlist" name="vendor_id" listKey="id" listValue="name" headerKey="0" headerValue="Select Vendor" cssClass=" chosen-select"></s:select>
                        </div> 
                         <div class="form-group" style="width: 15%">
                        <s:select list="#{'2':'All(Invoice Created)','1':'Yes', '0':'NO'}" name="invoice_created" cssClass=" chosen-select"/>
                        </div>
                        <div class="form-group">
                        	<button type="submit" class="btn btn-primary">Go</button>
                        </div>
                         &nbsp;  &nbsp;  &nbsp;  &nbsp;
                        <div class="form-group">
                        		<a class="btn btn-primary" href="#" onclick="dmdashboardcall()"> DM Dashboard</a>
                        </div>
</div>
</div>

</div>
</s:form>
<div>
<%--<table class="table my-table xlstable table-bordered" style="width: 100%;" id="mynewtab" >
 <tr>
<th>sr no</th>
<th>DM no</th>
<th>Dm Date</th>
<th>Dm Vendor</th>
</tr>

<%int i=0; %>
<s:iterator value="dmlist">
						<tr>
						<td><%=++i %></td>
						<td><s:property value="delivermemoid"/></td>
	          	        <td><s:property value="delivermemodate"/></td>
	          	        <td><s:property value="vendor"/></td>
	          	        </tr>

</s:iterator> --%>

 <table class="table my-table xlstable table-striped table-bordered" id="tablesort" style="width: 100%;">
                            <thead>
                            
                                <tr>
                                    <th style="width: 3%;">No</th>
                                    <th style="width: 15%;">Supplier Name</th>
                                    <th style="width: 8%;">GRN No</th>
                                    <th style="width: 8%;">GRN Date</th>
                                     <th style="width: 8%;">DM Seq. No.</th>
                                    <th style="width: 8%;">DM no</th>
									<th style="width: 8%;">Dm Date</th>
                                    <th style="width: 7%;">Invoice No</th>
                                    <th style="width: 8%;">DM ID</th>
                                    <th style="width: 10%;">Invoice Created</th>
                                    <th style="width: 10%;">Location</th>
                                    <th style="width: 15%;text-align:right;">Net Amount</th>
                                    <th class="" style="width: 5%;">Print</th>
                                    <th style="width: 3%;">Edit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%int i=0; %>
                                <s:iterator value="dmlist">
                             
                                <tr>
                                    <td><%=(++i) %></td>
                                    <td>
                                    	<s:if test="dmcmplt==0">
                                    		<%-- <a href="#" onclick="openBlankPopup('dmdashboardProcurement?vendorid=<s:property value="vendor_id"/>')"><s:property value="vendor"/></a> --%>
                                    		<a href="#" onclick="dmdashboardvendorcall(<s:property value="vendor_id"/>)"><s:property value="vendor"/></a>
                                    	</s:if>
                                    	<s:else>
                                    		<s:property value="vendor"/>
                                    	</s:else>
                                    </td>
                                    <s:if test="grnno==0">
                                    	<td>-</td>
                                    </s:if>
                                    <s:else>
                                    	<td><s:property value="grnseqno"/>/<s:property value="grnno"/></td>
                                    </s:else>
                                    <td><s:property value="grndate"/></td>
                                   
                                    <td><s:property value="dmseq"/></td>
                                    <td><s:property value="delivermemoid"/></td>
	          	        			<td><s:property value="delivermemodate"/></td>
                                    <td><s:property value="voucherno"/></td>
                                    <td><s:property value="dmid"/></td>
                                    <td>
                                    <s:if test="isdelivermemo==1">
                                    		<s:if test="dmcmplt==1">
                                    			Yes
                                    		</s:if>
                                    		<s:else>
                                    			No
                                    		</s:else>
                                    </s:if>
                                    </td>
                                    <td><s:property value="locationname"/></td>
                                    <td class="text-right">Rs.<s:property value="netAmt" /> </td>
                                    <td class="">
                                        <s:if test="confirm==1 && gudreceipt==0">
                                    		<a href="#" onclick="openPopup('printconfirmProcurement?id=<s:property value="procurementid"/>')"><i class="fa fa-print" style="padding-left: 13px;"></i></a>
                                    	</s:if>
                                    	<s:elseif test="confirm==1 && gudreceipt==1 && grnno!=0">
                                    		<a href="#" onclick="openPopup('printconfirmProcurement?id=<s:property value="procurementid"/>')"><i class="fa fa-print" style="padding-left: 13px;"></i></a>
                                    	</s:elseif>
                                    	 <s:elseif test="confirm==0">
                                    		<a href="#" onclick="openPopup('printconfirmProcurement?id=<s:property value="procurementid"/>')"><i class="fa fa-print" style="padding-left: 13px;"></i></a>
                                    	</s:elseif> 
                                    	<s:else>
                                    		<a class="baksetp1"  href="#" onclick="openPopup('grnprintProcurement?id=<s:property value="id"/>&isfromdelivermemo=1')" > <i class="fa fa-print" style="padding-left: 13px;"></i></a>
                                    	</s:else>
                                    </td>
                                    <td class="text-center">
                                    	<s:if test="dmcmplt==0">
                                        <% if(loginInfo.getUserType()==2 || loginInfo.isEdit_purchaseorder()) {%>
	                                         <s:if test="gudreceipt==1 && deleted==0 && edit==0">
	                                                <a onclick="#" href="editpoProcurement?id=<s:property value="id"/>" ><i class="fa fa-pencil" style="color:#d9534f;" aria-hidden="true"></i></a>
	                                    	 </s:if>
                                   		<%} %>
                                   		</s:if>
                                    </td>
                                   
                                   
                                </tr>
							 </s:iterator>	                            
                            </tbody>
                        </table><br />

</table>
</div>
</div>
	<s:form action="newDmListProcurement" name="paginationForm" id="paginationForm" theme="simple">
							    	
							    	 <s:hidden name="fromdate"></s:hidden>
							    	<s:hidden name="todate"></s:hidden> 
							    	<s:hidden name="vendor_id"></s:hidden>
									<div class="col-lg-12">
									<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
										Total:<label class="text-info"><s:property value="totalRecords" /> Records</label>
									</div>
									<%@ include file="/common/pages/pagination.jsp"%>
								</div>
							</s:form>                 
</div>