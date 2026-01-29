<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="common/chosen_v1.1.0/chosen.css" rel="stylesheet"
	type="text/css" />
<link href="plugin/monthyear/MonthPicker.min.css" rel="stylesheet"
	type="text/css" />
<script src="plugin/monthyear/jquery-ui.min.js"></script>
<script src="plugin/monthyear/jquery.maskedinput.min.js"></script>
<script src="plugin/monthyear/MonthPicker.min.js"></script>


<style>
.topheadbaxck {
	background-color: rgb(239, 239, 239);
	padding: 8px 0px;
}

.table>thead>tr>td, .table>tbody>tr>td, .table>tfoot>tr>td {
	font-size: 14px;
}

.savebigbtn {
	width: 13%;
	height: 61px !important;
	font-size: 20px;
	background-color: #339966 !important;
	margin-bottom: 15px;
	line-height: 40px;
}

.chosen-container {
	width: 100% !important;
}
</style>

<SCRIPT type="text/javascript" src="inventory/js/indentproduct.js"></SCRIPT>
<SCRIPT type="text/javascript" src="inventory/js/procurement.js"></SCRIPT>


<SCRIPT type="text/javascript">
	window.onload = function() {

		$("#expiry").datepicker({

			dateFormat : 'dd-mm-yy',
			yearRange : yearrange,
			minDate : '30-12-1880',
			changeMonth : true,
			changeYear : true

		});
		$("#voucherdate").datepicker({

			dateFormat : 'dd-mm-yy',
			yearRange : yearrange,
			minDate : '30-12-1880',
			changeMonth : true,
			changeYear : true

		});
		$("#security_date").datepicker({

			dateFormat : 'dd-mm-yy',
			yearRange : yearrange,
			minDate : '30-12-1880',
			changeMonth : true,
			changeYear : true

		});
		$("#delivermemodate").datepicker({

			dateFormat : 'dd-mm-yy',
			yearRange : yearrange,
			minDate : '30-12-1880',
			changeMonth : true,
			changeYear : true

		});
		setState();

	}
</SCRIPT>


</head>
<body>
	<%
		LoginInfo loginfo = LoginHelper.getLoginInfo(request);
	%>
	<div class="row details">
		<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

			<h4>Goods Received</h4>

		</div>
	</div>
	<div class="">
	<s:form theme="simple" action="updateprocurementProcurement"
				id="formMyPo">
		<div class=""
			style="background-color: rgba(239, 239, 239, 0.42); padding: 9px; border: 1px dashed #ddd;">

			<div class="form-inline">
				<div class="form-group" style="width: 10%;">
					<s:hidden name="haslocation" id="haslocation"></s:hidden>
					<s:hidden name="expiryDateAlert" id="expiryDateAlert"></s:hidden>
					<%
						if (loginfo.getUserType() == 2) {
					%>
					<s:select list="warehouseList" id="warehouse" name="warehouse"
						listKey="id" listValue="name" headerKey="0" headerValue="Select"
						cssClass="form-control chosen-select"></s:select>
					<%
					
						} else {
					%>
					<s:hidden name="warehouse" id="warehouse">
					</s:hidden>
					<%
						}
					%>
				</div>
				<div class="form-group" id="vendid" style="width: 20%;">
					<s:select theme="simple" id="vendorid"
						onchange="setVendorProductForPo(this.value)" list="vendorList"
						listKey="id" listValue="name" headerKey="0"
						headerValue="Select Supplier"
						cssClass="form-control chosen-select" />
				</div>
				<div class="form-group" id="proddiv" style="width: 50%;">
					<select name="" class="form-control chosen-select" id="product_id"
						style="display: none;">
						<option value="0">Select</option>
					</select>
				</div>
				<div class="form-group hidden" id="categorydiv">
					<s:select list="categoryList" theme="simple" listKey="id"
						readOnly="true" listValue="name" cssClass="form-control"
						id="categoryid" name="category_id"></s:select>
				</div>
				<div class="form-group hidden" id="subdiv">
					<s:select list="subcategoryList" theme="simple" listKey="id"
						readOnly="true" listValue="name" cssClass="form-control"
						id="subcategoryid" name="subcategory_id"></s:select>
				</div>
				<div class="form-group hidden">
					<input type="number" readonly="readonly" class="form-control"
						id="rate" placeholder="Rate">
				</div>
				<div class="form-group">
					<a href="#" onclick="addProductForProcurement('prodTable')"
						class="btn btn-success">Add</a>
				</div>
				<div class="form-group">
					<a data-toggle="modal" href="#addnewproduct"
						aria-expanded="false" onclick="resetAddproductdata()"
						class="btn btn-success">New Product</a>
				</div>
			</div>





			
				<s:hidden name="procurementid" id="procurementid" />
				<%-- <div class="col-lg-12 col-md-12 col-xs-12 topheadbaxck">
									<div class="col-lg-2 col-md-2 col-sm-2 hidden">
											<div class="form-group">
												<label>Select State</label>
												<select name="" class="form-control chosen-select">
												    <option value="0">Interstate</option>
												    <option value="0">Intrastate</option>
												</select>
											</div>
											
										</div>
									 <%if(loginfo.getUserType()==2) {%>
									    <div class="col-lg-2 col-md-2 col-sm-2">
											<div class="form-group">
												<label>Select Location</label>
												<s:select list="locationListPharmacy" theme="simple" id="location" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										     	</s:select> 
												    
											</div>	
										</div>
									<%} else { %>	
									      <div class="form-group hidden">
												<label>Select Location</label>
												<s:select list="locationListPharmacy" theme="simple" id="location" name="location" cssStyle="width:30%" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Location" >
										     	</s:select> 
												    
											</div>	
									 <%} %>
										<div class="col-lg-2 col-md-2 col-sm-2">
											<div class="form-group">
												<label>Select Supplier</label>
												<s:select list="vendorList"  id="vendorid" name="vendor" listKey="id" onchange="getvendorProduct(this.value)" listValue="name" cssClass="form-control chosen-select" theme="simple" headerKey="0" headerValue="Select Supplier" />
											</div>	
										</div>
										
										<div class="col-lg-2 col-md-2 col-sm-2">
											<div class="form-group" id="prodListId" >
												<label>Select Product</label>
												<select name="" class="form-control chosen-select">
												    <option value="0">Select Product</option>
												</select>
											</div>
											
										</div>
										<div class="col-lg-1 col-md-1 col-sm-2">
											<div class="form-group">
												<a href="#" type="button" class="btn btn-primary" onclick="addNewProd()" style="margin-top: 22px;">New Product</a>
											</div>
										</div>
										<div class="col-lg-1 col-md-1 col-sm-2">
											<div class="form-group" id="subtype">
												<label>Product Type</label>
												<s:select cssClass="form-control" list="medicineTypeList" headerKey="0" listKey="id" listValue="name" headerValue="Product Type" name="medicine_type" id="medicine_type"></s:select>
												
												<!--<SELECT class="form-control" name="medicine_type" id="medicine_type">
												 <option value="Tablet">TAB</option>
												 <option value="AMP">AMP</option>
												 <option value="Tablet">CAP</option>
												 <option value="Tablet">KIT</option>
												 <option value="BTL">BTL</option>
												 <option value="ML">ML</option>
												 <option value="NOS">NOS</option>
												 <option value="PACK">PACK</option>
												 <option value="PIECE">PIECE</option>
												 <option value="SACH">SACH</option>
												 <option value="Tablet">STRIP</option>
												 <option value="TUBE">TUBE</option>
												 <option value="VIAL">VIAL</option>
												</SELECT>
												
											--></div>
										</div>
										<div class="col-lg-1 col-md-1 col-sm-2">
											<div class="form-group">
												<label>Pack</label>
												<input type="text" class="form-control" id="pack" required="required">
											</div>
										</div>
										<div class="col-lg-2 col-md-2 col-sm-2">
											<div class="form-group">
												<label>Medicine Type</label>
												<s:select list="#{'0':'Regular','1':'Narcotics','2':'H1'}" cssClass="form-control" name="medicine_shedule" id="medicine_shedule"  ></s:select>
											</div>
										</div>
										<div class="col-lg-1 col-md-1 col-sm-2">
											<a href="#" class="btn btn-warning" onclick="addProductForProcurement('prodTable')" style="margin-top: 22px;">Add</a>
										</div>
									</div>
									 --%>
				<input type="hidden" value="0" id="repeat" />
				<div class="col-lg-12 col-md-12 col-sm-12" style="padding: 0px; margin-top: 10px;">
					<b style="color: brown;"> <span id="vendorName"> </span>
						&nbsp; | &nbsp; 
						<input type="text" placeholder="Invoice No" class="form-control"  id="voucherno" required="required" name="voucherno" 
						style="width: 10%; background-color: cornsilk;" />
						<label id="errvoucher" style="color: red"></label> 
						
						&nbsp; | &nbsp; 
						<input type="text" class="form-control" readonly="readonly" id="voucherdate" name="voucherdate" placeholder="Invoice Date" 
						style="width: 10%; background-color: cornsilk;" /> 
						
						&nbsp; | &nbsp; 
						<input type="text" placeholder="Security Inward No" class="form-control" id="security_no" required="required"
						name="security_no" style="width: 10%; background-color: cornsilk;" />
						
						&nbsp; | &nbsp; 
						<input type="text" placeholder="Security Inward Date" readonly="readonly" class="form-control" id="security_date" required="required" 
						name="security_date" style="width: 10%; background-color: cornsilk;" />
						
						&nbsp; |  &nbsp; 
						<input type="checkbox" id="isdelivermemo" name="isdelivermemo" onclick="changeDMTextbox(this.checked)" >&nbsp; <span>Is Deliver Memo?</span>
						
						&nbsp; | &nbsp; 
						<input type="text" placeholder="Deliver Memo No." class="form-control" id="delivermemoid" required="required"
						name="delivermemoid" style="width: 10%; background-color: cornsilk;" readonly="readonly" /> 
					
						&nbsp; | &nbsp; 
						<input type="text" placeholder="Deliver Memo Date" readonly="readonly" class="form-control" readonly="readonly" id="delivermemodate" required="required"
						name="delivermemodate" style="width: 10%; background-color: cornsilk;" /> 
						
					</b>
					<table class="table my-table xlstable table-bordered"
						style="width: 100%; text-transform: uppercase; margin-top: 10px; margin-bottom: 0px;"
						id='prodTable'>
						<thead>
							<tr>
								<th>Sr</th>
								<th style="width: 15%;">Product</th>
								<th style="width: 5%;">Pack</th>
								<th style="width: 6%;">HSN No</th>
								<th style="width: 5%;">Mfg</th>
								<th style="width: 7%;" class="">MRP</th>
								<th style="width: 6%;">per unit</th>
								<th style="width: 5%;">GST</th>
								<th style="width: 7%;">Rate</th>
								<th style="width: 8%;">Batch No</th>
								<th style="width: 7%;">ExpDate</th>
								<th style="width: 5%;">Pack Qty</th>
								<th style="width: 5%;">Rec.Qty</th>
								<th style="width: 5%;">Dis</th>
								<th style="width: 5%;">Free Qty</th>
								<th style="width: 5%;">Shelf No</th>
								<th class="hidden"></th>
								<th>Amt</th>
				                <th>GST Amt</th>
								<th>Net Total</th>
								<th></th>
							</tr>
						</thead>
						<tbody id="receiveddata">
							<tr></tr>


						</tbody>
						<input type="hidden" id="tempcount" value="0" />
					</table>

					<div class="col-lg-12 col-md-12 col-xs-12"
						style="padding: 0px; padding-bottom: 15px;">
						<table class="table my-table xlstable table-bordered hidden"
							id="tableInner" style="width: 38%;">
							<thead>
								<tr>
									<td>Product Name</td>
									<td>Batch No</td>
									<td>Expiry Date</td>
									<td>Qty</td>
								</tr>
							</thead>
							<tbody id="innerTBody">
								<!--<tr>
		              <td>Saridon 500</td>
		              <td>AD-120</td>
		              <td>31-04-2017</td>
		              <td>12</td>
		             </tr>
		            -->
								<tr></tr>
							</tbody>
						</table>

					</div>


				</div>

				<div class="col-lg-12 col-md-12 col-xs-12" style="padding: 0px;">
					<div class="col-lg-4 col-md-4 col-xs-4"></div>
					<div class="col-lg-5 col-md-5">
						<table class="table my-table xlstable table-bordered"
							style="width: 100%; margin-top: 10px; float: right; border: 1px solid #ddd; text-align: right;">
							<thead>
								<tr>
									<th
										style="background-color: brown; width: 12%; text-align: right;">GST</th>
									<th
										style="background-color: brown; width: 20%; text-align: right;">Total
										Amt.</th>
									<th
										style="background-color: brown; width: 22%; text-align: right;">Taxable
										Amt.</th>
									<th
										style="background-color: brown; width: 19%; text-align: right;">Dis.
										Amt.</th>
									<th
										style="background-color: brown; text-align: right; width: 18%; text-align: right;">Tax
										Amt.</th>
								</tr>
							</thead>
							<tbody id="tTaxData">
								<!--
                                 <tr>
                                  <td class="text-left">6.00%</td>
                                  <td class="text-right">1478.47 </td>
                                  <td><input type="text" class="form-control" required="required" name="procurements[0].batch_no"> </td>
                                  <td class="text-right">1478.47 </td>
                                  <td class="text-right">88.70</td>
                                  
                                 </tr>
                                 <tr>
                                  <td class="text-left">13.50%</td>
                                  <td class="text-right">1478.47 </td>
                                  <td><input type="text" class="form-control" required="required" name="procurements[0].batch_no"> </td>
                                  <td class="text-right">45.49</td>
                                  <td class="text-right">45.49</td>
                                 </tr>
                                 
                                -->
							</tbody>

						</table>
						<div class="form-group">
							<label for="comment" style="text-align: left;">Remark:</label> <a
								class="hidden" href="#" data-toggle="modal"
								data-target="#viewdetailspro">view</a>
							<textarea class="form-control" rows="5" id="remark" name="remark"
								style="background-color: rgba(95, 95, 95, 0.04);"></textarea>
						</div>


					</div>


					<div class="col-lg-3 col-xs-3 col-md-3" style="padding: 0px;">
						<table style="width: 100%;">
							<tbody>
								<tr>
									<td style="text-align: right; width: 55%;">SUBTOTAL :</td>
									<td><input type="text" readonly="readonly" id="subTotal"
										class="form-control" style="margin-bottom: 5px;"></td>
								</tr>
								<tr>
									<td style="text-align: right; color: red;"><select
										name="disctype" id="disctype"
										onchange="getDiscType(this.value)" class="form-control"
										style="width: 50%; background-color: rgba(255, 0, 0, 0.07);"><option
												value="0">Percent</option>
											<option value="1">Cash</option></select> DISCOUNT :</td>
									<td>
										<!-- <input type="number" class="form-control"
										onkeyup="calDiscNet(this.value)" name="discount"
										readonly="readonly" value="0" id="discount"
										style="background-color: rgba(220, 220, 220, 0.28); color: red; margin-bottom: 5px;" /> -->
										<input type="number" class="form-control"
											onkeyup="calTotalAmt()" name="discount"
											readonly="readonly" value="0" id="discount"
											style="background-color: rgba(220, 220, 220, 0.28); color: red; margin-bottom: 5px;" />
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">Taxable Amount :</td>
									<td><input type="text" readonly="readonly"
										class="form-control" id="discountedamt" style="margin-bottom: 5px;">
										
								</tr>
								<tr>
									<td style="text-align: right;">CGST :</td>
									<td><input type="text" readonly="readonly"
										class="form-control" id="tcgst" style="margin-bottom: 5px;">
										<input type="hidden" name="vat" id="vat" /> <input
										type="hidden" name="cgst" id="cgst" /></td>
								</tr>
								<tr>
									<td style="text-align: right;">SGST :</td>
									<td><input type="text" readonly="readonly" id="tsgst"
										class="form-control" style="margin-bottom: 5px;" /><input
										type="hidden" name="sgst" id="sgst" /></td>
								</tr>
								<tr>
									<td style="text-align: right;">IGST :</td>
									<td><input type="text" readonly="readonly"
										class="form-control" id="tigst" style="margin-bottom: 5px;" /><input
										type="hidden" name="igst" id="igst" /></td>
								</tr>
								<tr>
									<td style="text-align: right;">SURCHARGE :</td>
									<td><input type="text" class="form-control"
										onkeyup="calVatDiscount()" value="0.00" name="surcharge"
										id="subcharge" style="margin-bottom: 5px;"></td>
								</tr>
								<tr>
									<td style="text-align: right;">CREDIT :</td>
									<td><input type="text" class="form-control"
										onkeyup="calVatDiscount()" id="credit" name="credit"
										style="margin-bottom: 5px;"></td>
								</tr>
								<tr>
									<td style="text-align: right;">DEBIT :</td>
									<td><input type="text" class="form-control"
										onkeyup="calVatDiscount()" id="debit" name="debit"
										style="margin-bottom: 5px;"></td>
								</tr>
								<tr>
									<td style="text-align: right; color: green;"><label>NET
											PAYBLE AMT :</label></td>
									<td><input type="text" readonly="readonly"
										class="form-control" id="netpay" name="netpay"
										style="margin-bottom: 5px; background-color: rgba(220, 220, 220, 0.28); color: green;" />
										<input type="hidden" id="nettemp" /></td>
								</tr>
								<tr>
									<td style="text-align: right;">PAYMENT :</td>
									<td><select name="paymode" id="paymode"
										class="form-control">
											<option value="Credit">CREDIT</option>
											<!-- <option value="Cash">CASH</option> -->
									</select></td>
								</tr>
							</tbody>
						</table>
					</div>
			
			<a href="#" onclick="savePoReceived()" class="btn btn-primary savebigbtn"
				style="margin-top: 15px; float: right;">Goods Received</a>
		</div>
	</div>
	</s:form>







		<!-- View Product Details Modal -->
		<div id="viewdetailspro" class="modal fade" role="dialog">
			<div class="modal-dialog modal-lg">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">
							Previous Invoice Details For - <span id="tabdata"> ALCOMAX
								TAB </span>
						</h4>
					</div>
					<div class="modal-body">
						<div class="col-lg-12 col-xs-12 col-md-12" style="padding: 0px;">
							<table class="table table-striped table-bordered"
								style="width: 100%;">
								<thead>
									<tr>
										<th style="width: 27%;">Supplier</th>
										<th style="width: 9%;">GRN Date</th>
										<th style="width: 7%;">GRN</th>
										<th style="width: 7%;">Invoice No.</th>
										<th style="width: 7%;">GST</th>
										<th style="width: 7%;">Quantity</th>
										<th style="width: 7%;">Free Qty</th>
										<th style="width: 7%; background-color: brown; text-align: right;">MRP</th>
										<th style="width: 7%; background-color: brown; text-align: right;">Sale Price</th>
										<th style="width: 7%; background-color: brown; text-align: right;">Purchase Price</th>
										<th style="width: 7%;">Dis.</th>
										<th style="width: 7%;">Total</th>
									</tr>
								</thead>
								<tbody id="innerProduct">
									<!--<tr>
            <td>ABC Supplier Pvt.Ltd</td>
            <td>22-07-2017</td>
            <td>100</td>
            <td style="background-color: rgba(165, 42, 42, 0.07);text-align:right;">23.54</td>
            <td>0</td>
            <td>12%</td>
           </tr>
           <tr>
            <td>ABC Supplier Pvt.Ltd</td>
            <td>22-07-2017</td>
            <td>100</td>
            <td style="background-color: rgba(165, 42, 42, 0.07);text-align:right;">23.54</td>
            <td>0</td>
            <td>12%</td>
           </tr>
           <tr>
            <td>ABC Supplier Pvt.Ltd</td>
            <td>22-07-2017</td>
            <td>100</td>
            <td style="background-color: rgba(165, 42, 42, 0.07);text-align:right;">23.54</td>
            <td>0</td>
            <td>12%</td>
           </tr>
           <tr>
            <td>ABC Supplier Pvt.Ltd</td>
            <td>22-07-2017</td>
            <td>100</td>
            <td style="background-color: rgba(165, 42, 42, 0.07);text-align:right;">23.54</td>
            <td>0</td>
            <td>12%</td>
           </tr>
           <tr>
            <td>ABC Supplier Pvt.Ltd</td>
            <td>22-07-2017</td>
            <td>100</td>
            <td style="background-color: rgba(165, 42, 42, 0.07);text-align:right;">23.54</td>
            <td>0</td>
            <td>12%</td>
           </tr>
           -->
								</tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>






		<!-- Create New Product Modal -->
		<div id="newpo" class="modal fade" role="dialog"
			style="background-color: rgba(0, 0, 0, 0.59);">
			<div class="modal-dialog modal-sm">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Add New Batch Order</h4>
					</div>
					<div class="modal-body">
						<div class="col-lg-12 col-xs-12 col-md-12" style="padding: 0px;">
							<s:form action="savenewpoProcurement" theme="simple"
								method="post" id="poform">
								<table class="table my-table xlstable table-bordered"
									style="width: 100%;" id="mynewtab">
									<thead>
										<tr>
											<th style="width: 40%;">Batch Number</th>
											<th style="width: 38%;">Expiry</th>
											<th>Quantity</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input class="form-control" id="batch"
												placeholder="Batch Number" type="text" /></td>
											<td><input class="form-control" id="expiry"
												placeholder="Expiry" type="text" /></td>
											<td><input class="form-control" id="qty"
												placeholder="Qty" type="text" /></td>
										</tr>

									</tbody>

								</table>
							</s:form>
							<div class="col-lg-12 col-xs-12 hidden"
								style="padding: 0px; margin-top: 15px;">
								<div class="col-lg-12 col-md-12 text-right">

									<p>
										Total : Rs.<label id="lblPOTotal">00.00</label>
									</p>
								</div>
							</div>
							<div class="col-lg-12 col-xs-12 hidden"
								style="padding: 0px; margin-top: 15px;">
								<div class="col-lg-6 col-md-6 text-left">
									<input value="Add more" onclick="addnewpo('mynewtab')"
										class="btn btn-primary" type="button">
								</div>
								<div class="col-lg-6 col-md-6 text-right" style="padding: 0px;">
									<input value="Create PO" onclick="submitPo('')"
										class="btn btn-primary pull-right" type="button">
								</div>
							</div>

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" onclick="savetoProductPo('tableInner')"
							class="btn btn-primary" style="margin-top: 15px;">Add
							Product</button>
					</div>
				</div>

			</div>
		</div>


		<!-- Add New Product Modal -->
		<div id="addnewproduct" class="modal fade" role="dialog">
			<div class="modal-dialog modal-sm">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title" id="suppliername">Add New Product</h4>
					</div>
					<div class="modal-body">
						
					<div class="col-lg-12 col-md-12">
        				<div class="form-group">
        					<label>Select Category</label>
        					<s:select list="categoryList" theme="simple" onchange="getsubproducttype(this.value)" cssClass="form-control chosen-select" id="productcategoryid" name="categoryid" listKey="id" listValue="name" headerKey="0" headerValue="Select Category" ></s:select>
        				</div>
        			</div>
        			<div class="col-lg-12 col-md-12">
        				<div class="form-group" id="subprodtypediv" >
        					<label>Sub Category</label>
        					<select name="subcategory" id="subcategory" class="form-control chosen-select">
        						<option value="0">Select Sub Category</option>
        					</select>
        				</div>
        			</div>
        			<div class="col-lg-12 col-md-12">
        				<div class="form-group"  >
        					<label>Product Type</label>
        					<s:select list="#{'Regular':'Regular','Narotics':'Narotics','H1':'H1','High Risk Medicine':'High Risk Medicine','Vaccination':'Vaccination'}" cssClass="form-control" cssStyle="width:95%;"  name="medicine_shedule" id="medicine_shedule"></s:select>
	                    </div>
        			</div>
						
					   <%--  <div class="col-lg-12 col-md-12">
							<div class="from-group">
								<label>Product Type</label> 
								
								 <s:select list="subcategoryList" id="subcategory" name="subcategory" cssClass="form-control chosen-select" listKey="id" listValue="name" headerKey="0" headerValue="Select Product Type"></s:select>	
							</div>

						</div> --%>
						<div class="col-lg-12 col-md-12">
							<div class="from-group">
								<label>Generic Name</label> 
									<!-- <input type="text" class="form-control" id="genericname" required="required"> -->
									<%if(loginfo.isAuto_generic_name()){ %>
										<s:select list="genericnamelist" theme="simple" required="required"  cssClass="form-control chosen-select" id="genericname" listKey="name" listValue="name" headerKey="" headerValue="Generic Name" style="background-color: rgba(245, 245, 245, 0.46);" ></s:select>
									<%}else{ %>
										<input type="text" class="form-control" id="genericname" required="required">
									<%} %>
							</div>

						</div>
						<div class="col-lg-12 col-md-12" style="margin-top: 10px;">

							<div class="from-group">
								<label>Product Name</label> <input type="text"
									class="form-control" onchange="chkNameExistIncatalogue(this)"
									id="prodname" required="required">
							</div>
						</div>
						
						<div class="col-lg-12 col-md-12" style="margin-top: 10px;">

							<div class="from-group">
								<label>Product Code</label> <input type="text"
									class="form-control" onchange="checkProductCodeExistSingle(this.value)"
									id="pro_code" >
							</div>
						</div>
						
						<div class="col-lg-12 col-md-12" style="margin-top: 10px;">

							<div class="from-group">
								<label>Pack</label> <input type="number"
									class="form-control" 
									id="pack" required="required">
							</div>
						</div>
						<div class="col-lg-12 col-md-12" style="margin-top: 10px;">

							<div class="from-group">
								<label>GST</label> 
								<select  id="addgst"   class="form-control chosen-select"> 
									<option value="">Select</option>
									<option value="0">0%</option>
									<option value="5">5%</option>
									<option value="12">12%</option>
									<option value="18">18%</option>
									<option value="28">28%</option>
								</select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							onclick="savenewCatalogue(0)" style="margin-top: 15px;">Save</button>
						<button type="button" class="btn btn-primary hidden" style="margin-top: 15px;"
							data-dismiss="modal">Close</button>
					</div>
					</div>
				</div>

			</div>
		</div>

		<!--Add Product Modal -->
		<div id="addproduct" class="modal fade" role="dialog">
			<div class="modal-dialog modal-lg" style="width: 99%;">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Add Product</h4>
					</div>
					<div class="modal-body">
						<div class="col-lg-12 col-xs-12 col-md-12" style="padding: 0px;">
							<table class="table my-table xlstable table-bordered"
								style="width: 100%;" id="mytable">
								<thead>
									<tr>
										<th style="width: 15%;">Category</th>
										<th style="width: 15%;">Sub-Category</th>
										<th style="width: 8%;">Product Type</th>
										<th style="width: 15%;">Product Name</th>
										<th style="width: 5%;">Pack</th>
										<th style="width: 7%;">MRP</th>
										<th style="width: 7%;">Pur.Rate</th>
										<th style="width: 7%;">Sale Price</th>
										<th style="width: 6%;">MFG</th>
										<th style="width: 6%;">GST</th>
										<th style="width: 20%;">Description</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><s:select list="categoryList" theme="simple"
												onchange="getsubcat(this.value)"
												cssClass="form-control chosen-select" id="category_id0"
												listKey="id" listValue="name" headerKey="0"
												headerValue="Select Category"></s:select></td>
										<td id="subcatdiv"><select id="subcategory_id0"
											class="form-control chosen-select">
												<option value="0">Select</option>
										</select></td>
										<td><select id="prodtype0"
											class="form-control chosen-select">
												<option value="0">Select</option>
												<option value="Regular">Regular</option>
												<option value="H1">H1</option>
												<option value="Narcotics">Narcotics</option>
										</select></td>
										<td><input type="text" class="form-control"
											id="product_name0" placeholder="Product Name"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><input type="text" class="form-control" id="pack0"
											placeholder="Pack"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><input type="text" class="form-control" id="mrp0"
											onblur="getcalsaleprice(0)" onmousemove="getcalsaleprice(0)"
											onmouseout="getcalsaleprice(0)" placeholder="MRP"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><input type="text" class="form-control"
											id="purchase_price0" placeholder="Rate"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><input type="text" class="form-control"
											id="sale_price0" placeholder="Sale Price"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><input type="text" class="form-control" id="mfg0"
											placeholder="MFG"
											style="background-color: rgba(245, 245, 245, 0.46);"></td>
										<td><select id="vat0" class="form-control chosen-select">
												<option value="0">Select</option>
												<option value="0">0%</option>
												<option value="5">5%</option>
												<option value="12">12%</option>
												<option value="18">18%</option>
												<option value="28">28%</option>
										</select></td>
										<td><textarea rows="2" class="form-control"
												id="description0" placeholder="Description"
												style="background-color: rgba(245, 245, 245, 0.46)"></textarea></td>
									</tr>
								</tbody>
							</table>

							<div class="col-lg-12 col-xs-12"
								style="padding: 0px; margin-top: 15px;">
								<div class="col-lg-6 col-md-6 text-left"></div>
								<div class="col-lg-6 col-md-6 text-right">
									<input value="Save" class="btn btn-primary" type="button"
										onclick="addNewItem()">
								</div>
							</div>



						</div>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default hidden"
							data-dismiss="modal">Close</button>
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




			
</body>
<script src="common/chosen_v1.1.0/chosen.jquery.js"
				type="text/javascript"></script>
			<script type="text/javascript">
				var config = {
					'.chosen-select' : {},
					'.chosen-select-deselect' : {
						allow_single_deselect : true
					},
					'.chosen-select-no-single' : {
						disable_search_threshold : 10
					},
					'.chosen-select-no-results' : {
						no_results_text : 'Oops, nothing found!'
					},
					'.chosen-select-width' : {
						width : "100%"
					}
				}
				for ( var selector in config) {
					$(selector).chosen(config[selector]);
				}

				// Apply an input mask which mkase sure the user 
				// limits the user to typing a month in the 
				//fromat specified in the MonthFormat option.
				$('#DigitalBush').MonthPicker({
					UseInputMask : true
				});
				$('#DigitalBushBoth').MonthPicker({
					UseInputMask : true,
					ValidationErrorMessage : 'Invalid Date!'
				});
			</script>
</html>