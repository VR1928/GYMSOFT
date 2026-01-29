<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="_assets/newtheme/css/main.css">
<link rel="stylesheet" href="_assets/newtheme/css/responsive.css">

<style>
.form-control {
    border: 1px solid rgba(0, 0, 0, 0.1) !important;
}

	p {
    margin: 0 0 1.5px;
}
.table {
    width: 100%;
    max-width: 100%;
    margin-bottom: 5px;
}
.savebigbtn {
    height: 61px !important;
    font-size: 20px;
    background-color: #339966 !important;
    margin-bottom: 15px;
    line-height: 40px;
}
ul, ol {
    margin-top: 0;
    margin-bottom: 8.5px;
    list-style: none;
}
.savebigbtn {
    width: 13%;
    height: 61px !important;
    font-size: 20px;
    background-color: #339966 !important;
    margin-bottom: 15px;
    line-height: 40px;
}
ul, ol {
    margin-top: 0;
    margin-bottom: 8.5px;
    list-style: none;
    padding: 0px;
    margin: 0px;
}
</style>
<SCRIPT type="text/javascript" src="inventory/js/addproduct.js"></SCRIPT>
 <script type="text/javascript" src="common/js/pagination.js"></script>
<SCRIPT type="text/javascript" src="inventory/js/indentproduct.js"></SCRIPT>
<SCRIPT type="text/javascript" src="inventory/js/procurement.js"></SCRIPT>
<script >
$(document).ready(function() {

	$("#fromDate").datepicker({

		dateFormat : 'dd-mm-yy',
		yearRange: yearrange,
		minDate : '30-12-1880',
		changeMonth : true,
		changeYear : true,
		maxDate : '0',

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
</head>
<body>
<%LoginInfo loginInfo = LoginHelper.getLoginInfo(request);%>
<div class="col-lg-12 col-xs-12 col-md-12">
<form action="savegrnProcurement"id="grnwithposubmitid"  method="post">
<!-- <div class="row">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="padding:0px;">
			<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 text-center hidden" style="border-bottom: 1px solid #ddd;" id="letterHead">
				<h4>SHREE NARAYANA PHARMACY</h4>
				<h5>SHREE NARAYANA HOSPITAL (A unit of Health-Tech Chattisgarh Pvt.Ltd), NEAR GANJ MANDI, BEHIND SECTOR-5, DEVENDRA NAGAR, PANDRI, RAIPUR-492001 CHHATTISGARH</h5><h5>Website:http://www.snh.org.in, Email:info@snh.org.in, Contact : 0771-3001234/35/36</h5>
			</div>	
			<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 text-center">
				<h4><b>CREATE PO</b></h4>
			</div>
	</div>
</div> -->

<div class="row" style="background-color: rgba(239, 239, 239, 0.42);padding: 9px;border: 1px dashed #ddd;">
	<table class="table table-striped table-bordered" style="width:100%;">
         
          <tbody>
          	<tr>
          		<td style="border-top: none;width: 15%;">
        			<%-- <s:select list="warehouseList" theme="simple" name="warehouse" headerKey="0" id="warehouse" headerValue="Select Warehouse" onchange="setwarehouse(this.value)" listKey="id" listValue="name" cssClass="form-control chosen-select" /> --%>
          			<s:select list="warehouseList" theme="simple" name="warehouse" headerKey="0" id="warehouse" headerValue="Select Warehouse" onchange="setProductofStoreInPO(this.value)" listKey="id" listValue="name" cssClass="form-control chosen-select" />
          		</td>
          		<td id="categorydiv" style="border-top: none; width: 15%;" class="hidden">
        				<select class='form-control'>
        				 <option>Select Category </option>
        				</select>
          		</td>
          		<td id="subdiv" style="border-top: none;width: 15%;" class="hidden">
        				<select class='form-control'>
        				<option>Select SubCategory </option>
        				</select>
          		</td>
          		<td id="proddiv" style="border-top: none;width: 15%;">
        				<%-- <select name="" class="form-control chosen-select">
						    <option value="0">Select</option>
						</select> --%>
						<s:select list="productList" theme="simple" name="product_id" headerKey="0" id="product_id" headerValue="Select Product" onchange="setdiscRate(this.value)" listKey="id" listValue="data" cssClass="form-control chosen-select" />
          		</td>
          		<td id="vendid" style="border-top: none;width: 15%;">
          			<select name="" class="form-control chosen-select">
						    <option value="0">Select</option>
						    
						</select>
          		</td>
          		<td style="border-top: none;">
          			<input type="text" onchange="changeRateToCatalogue(this.value)"  class="form-control" id="rate" placeholder="Rate">
          		</td>
          		<td style="border-top: none;">
          			<input type="number" readonly="readonly" class="form-control" id="discount" placeholder="Discount">
          		</td>
          		<td style="border-top: none;">
          			<input type="number" class="form-control" id="qty" placeholder="Qty">
          		</td>
          		<td style="border-top: none;"><a href="#" onclick="saveNewGrn()" class="btn btn-success">Add</a></td>
          		<td style="border-top: none;"><a data-toggle="modal" href="#addnewproduct"
						aria-expanded="false" onclick="resetAddproductdata()"
						class="btn btn-success">New Product</a></td>
          	</tr>
          	</tbody>
         </table>
         
         
</div>
<div class="row" style="padding-top: 10px;min-height: 185px;">
<div class="form-inline" >
	<div class="form-group" style="width:15%;">
		<label><b>Date</b></label>
		<s:textfield readonly="true" name="fromdate" id="fromDate"
			cssClass="form-control" theme="simple" style="width:100%;" placeholder="from date"></s:textfield>
	</div>
	<div class="form-group" style="width:15%;">
		<label><b>Warehouse</b></label>
		<s:select list="warehouseList" theme="simple" name="warehouseid" headerKey="0" id="warehouseid" headerValue="Select Warehouse" listKey="id" listValue="name" cssClass="form-control chosen-select" />
	</div>
	<!-- <div class="form-group" style="width:2%;">
	</div> -->
	<div class="form-group hidden" style="width:15%;">
		<label><b>Is Agreement:</b></label><br>
		<input type="checkbox" name="isagreement" id="isagreement"> 
	</div> 
	<div class="form-group" style="width:12%;">
		
	</div> 
	<!-- <div class="form-group" style="width:15%;">
		<label><b>Is Agreement:</b></label><br>
		<input type="checkbox" name="isagreement" id="isagreement"> 
	</div>  -->
	<!-- <div class="form-group" style="width:15%;">
		<label><b>Search Product:</b></label><br>
		<input type="checkbox" name="isagreement" id="isagreement"> 
	</div>  -->
	<div class="form-group" style="width:15%;">
		<label></label><br>
		<h4><b>SELECTED PO QUE LIST</b></h4>
	</div> 
	<%if(loginInfo.isHidecalinpoprint()){ %>
		<div class="form-group" style="width:15%;float: right;">
			<label><b>Hide Calculation In Print</b></label>
			&nbsp;
			<!-- store 1 in procuremnt data  -->
			<!-- <input type="checkbox" checked="checked" name="hidecalinpoprint">  -->
			<s:checkbox checked="checked" name="hidecalinpoprint" ></s:checkbox>
		</div>
	<%}else{ %>
		<!-- store 0 in procuremnt data means it store data  -->
		<!-- <input type="checkbox" class="hidden"  name="hidecalinpoprint">  -->
		<s:checkbox cssClass="hidden" name="hidecalinpoprint" ></s:checkbox>
	<%} %>
	
</div>
		<!-- <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 text-center">
				<h4><b>SELECTED PO QUE LIST</b></h4>
		</div> -->
		<div class="fixedscroll1">
		<table class="table table-striped table-bordered" style="width:100%;" id="prodtableidnews">
          <thead>
           <tr>
            <th style="width: 3%;">Sr.no</th>
            <th style="width: 5%;">Indent N0</th>
            <th style="width: 7%;">Location</th>
            <th style="width: 4%;">Userid</th>
            <th style="width: 5%;">Product Code</th>
            <th style="width: 15%;">Product Name</th>
            <th style="width: 4%;">InStock</th>
            <th style="width: 5%;">Consumed</th>
            <th style="width: 6%;">Rate</th>
            <th style="width: 4%;">Discount</th>
            <th style="width: 4%;">Free</th>
            <th style="width: 6%;">GST</th>
            <th style="width: 4%;">Min Order</th>
            <th style="width: 4%;">Max Order</th>
            <th style="width: 13%;">Supplier</th>
            <th style="width: 7%;">Req Qty</th>
            <th style="width: 3%;"> 
            	<ul class="vertical default_list" id="">
						      <li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input type="checkbox" class="form-control" onclick="selectAllCheckBox(this)"/><i style="border-color: #fff;color: #fff;"></i> </label></li>
						</ul>
            
            	 </th>
            <!-- <th>Delete</th> -->
           </tr>
          </thead>
          <tbody id="tbodygrnwithpo">
          	   <% int countsss=0; %>
          	   <s:iterator value="selectedPOList">
          	     <tr>
          	       <td><%=countsss+1 %> <input type="hidden" value="<s:property value="id"/>" name="products[<%=countsss%>].id" />  </td>
          	       <td><s:property value="indent"/></td>
          	       <td><s:property value="from_location"/></td>
          	       <td><s:property value="req_userid"/></td>
          	       <td><s:property value="pro_code"/></td>  
          	       <td><s:property value="product_name"/>
          	         <input type="hidden" value="<s:property value="grn_with_po_tempid"/>" name="products[<%=countsss%>].grn_with_po_tempid" />
          	         <input type="hidden" value="<s:property value="product_id"/>" name="products[<%=countsss%>].product_id" />
          	         <input type="hidden" value="<s:property value="catalogueid"/>" id="catalogueid<%=countsss%>" name="products[<%=countsss%>].catalogueid" />
          	         <input type="hidden" value="<s:property value="maxorder"/>" id="maxorder<%=countsss%>" />
          	         <input type="hidden" value="<s:property value="minorder"/>" id="minorder<%=countsss%>" />
          	          <input type="hidden" value="<s:property value="purchase_price"/>" id="previouspurprice<%=countsss%>" />
          	        </td>
          	         
          	       <td><s:property value="stock"/></td>
          	       <td><s:property value="consumed"/> (<s:property value="fromdate"/> to <s:property value="todate"/>)</td>
          	       <td><input type="text" onchange="saveintotemppodata_rate(<%=countsss%>,'rate',this.value,<s:property value="grn_with_po_tempid"/>)" value='<s:property value="purchase_price"/>' id="rate<%=countsss%>"  name="products[<%=countsss%>].rate" /></td>
          	       <td><s:property value="discount"/></td>
          	       <td><s:property value="freeqty"/></td>
          	       <td>
          	       <%-- <s:property value="vat"/> --%>
          	        <select class="form-control" onchange="saveintotemppodata(<%=countsss%>,'gst',this.value,<s:property value="grn_with_po_tempid"/>)"   name='products[<%=countsss %>].vat' id='vat<%=countsss%>' >
                         <s:if test="vat==0">
                           <option value='0' selected="selected">0%</option>
                           <option value='5'>5%</option>
                           <option value='12'>12%</option>
                           <option value='18'>18%</option>
                           <option value='28'>28%</option>
                        </s:if>
                        <s:if test="vat==5">
                           <option value='0'>0%</option>
                           <option value='5' selected="selected">5%</option>
                           <option value='12'>12%</option>
                           <option value='18'>18%</option>
                           <option value='28'>28%</option>
                        </s:if>
                         <s:if test="vat==12">
                           <option value='0'>0%</option>
                           <option value='5'>5%</option>
                           <option value='12' selected="selected">12%</option>
                           <option value='18'>18%</option>
                           <option value='28'>28%</option>
                        </s:if>
                         <s:if test="vat==18">
                           <option value='0'>0%</option>
                           <option value='5'>5%</option>
                           <option value='12'>12%</option>
                           <option value='18' selected="selected">18%</option>
                           <option value='28'>28%</option>
                        </s:if>
                         <s:if test="vat==28">
                           <option value='0'>0%</option>
                           <option value='5'>5%</option>
                           <option value='12'>12%</option>
                           <option value='18'>18%</option>
                           <option value='28' selected="selected">28%</option>
                        </s:if>
				                                          
                    </select>
          	       
          	       </td>
          	       <td><s:property value="minorder"/></td>
          	       <td><s:property value="maxorder"/></td>
          	       <td>
          	       	<%-- <s:property value="vendor"/> --%>
          	        <select class="form-control chosen-select" onchange="saveintotemppodata(<%=countsss%>,'supplier',this.value,<s:property value="grn_with_po_tempid"/>)" name="products[<%=countsss%>].vendor_id" id="vendoridsss<%=countsss%>" >
          	             <option value="0">Select Supplier</option>
          	              <s:iterator value="vendorlist">
          	                 	<s:if test="grnpovendorid==vendor_id">
          	                 	  	<option selected="selected" value="<s:property value="id"/>"><s:property value="name"/></option>
          	                 	</s:if>
          	                 	<s:else>
          	                 			<option value="<s:property value="id"/>"><s:property value="name"/></option>
          	                 	</s:else>
          	             </s:iterator>
          	          </select> 
          	       </td>
          	       
          	       <td>
          	         <input type="number" onchange="saveintotemppodata_qty(<%=countsss%>,'req_qty',this.value,<s:property value="grn_with_po_tempid"/>)" name="products[<%=countsss%>].qty" class="form-control" id="qty<%=countsss%>" value="<s:property value="qty"/>" />
          	       </td>
          	       <td>
          	       <article>
						<ul class="vertical default_list" id="">
						   <s:if test="newpo==1">
						      <li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input name="products[<%=countsss%>].status" value="<%=countsss%>" type="checkbox" checked="checked" class="form-control case" /><i></i> </label></li>
						   </s:if>
						   <s:else>
						   		<li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input name="products[<%=countsss%>].status" value="<%=countsss%>" type="checkbox" class="form-control case" /><i></i> </label></li>
						   </s:else>
							
						</ul>
					</article>
          	       </td>
          	        <%--  <td><a href="#" onclick="deleteTempReq(this,'prodtableid',<s:property value="id"/>,<s:property value="catalogueid"/>)" ><i class="fa fa-times text-danger fa-2x" aria-hidden="true"></i></a></td> --%>
          	     </tr>
          	     <%countsss++; %>
          	   </s:iterator>
          	
          	</tbody> 
         </table>
		</div>
		 
		<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 text-center">
			<div class="col-lg-6 col-md-6 col-xs-6 col-sm-6 text-center">
				
			</div>
			<div class="col-lg-6 col-md-6 col-xs-6 col-sm-6 text-center">
				<a href="#" class="btn-success" onclick="return checkSubmit()" style="float: right;margin-top: 15px;margin-right: -13px;margin-bottom:5px;width: 174px;height: 40px;font-size: large;"  >Create PO</a>			
				<!-- <input type="submit" value="Create PO" class="btn-success" style="float: right;margin-top: 15px;margin-right: -13px;margin-bottom:5px;width: 174px;height: 40px;font-size: large;"  />			 -->
				<!-- <input type="submit" value="Indent Request" class="btn-success" style="float: right;margin-top: 15px;margin-right: 10px;margin-bottom:5px;width: 174px;height: 40px;font-size: large;"  /> -->			
         		
			</div>
		
		</div>
		 
		 <div>
		 	<hr width="100%" style="height:1px;border:none;color:#333;background-color:#333;">
		 </div>
         
         <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
				
			<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4" style="margin: 0;padding: 0;border: 0;outline: 0;">
				<div>
					<s:textfield name="searchtext" id="searchtext" style="width: 200px;" placeholder="Search Product/Generic Name" theme="simple" cssClass="form-control"></s:textfield>
					<a href="#" onclick="submitGrnWithPoSearch()" class="btn btn-primary">Go</a>
				</div>
				
			</div>
			<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4 text-center">
				<h4><b>PO QUE LIST</b></h4>
			</div>
			<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4" style="margin: 0;padding: 0;border: 0;outline: 0;" >
					<div class="form-inline" style="text-align: right"> 
						<div class="form-control"> 
						<a href="#" onclick="deleteMultipleTempPo()"  class="btn btn-primary"   >Delete Multiple</a>
						</div>
						<div class="form-control" > 
							<a href="#" onclick="addToPOSelectedList()"  class="btn btn-primary"  >Add To Selected List</a>
					 	</div>
				 	</div>
			</div>
			</div>
		</div>
		<div class="fixedscroll">
		<table class="table table-striped table-bordered" style="width:100%;" id="prodtableid">
          <thead>
           <tr>
            <th style="width: 3%;">Sr.no</th>
            <th style="width: 5%;">Indent N0</th>
            <th style="width: 8%;">Location</th>
            <th style="width: 5%;">Userid</th>
            <th style="width: 5%;">Product Code</th>
            <th style="width: 20%;">Product Name</th>
            <th style="width: 4%;">InStock</th>
            <th style="width: 6%;">Rate</th>
            <th style="width: 4%;">Discount</th>
            <th style="width: 4%;">Free</th>
            <th style="width: 6%;">GST</th>
            <th style="width: 4%;">Min Order</th>
            <th style="width: 4%;">Max Order</th>
            <th style="width: 15%;">Supplier</th>
            <th style="width: 7%;">Req Qty</th>
            <th style="width: 3%;"> 
            	<ul class="vertical default_list" id="">
						      <li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input type="checkbox" class="form-control" onclick="selectAllCheckBoxPO(this)"/><i style="border-color: #fff;color: #fff;"></i> </label></li>
						</ul>
            
            	 </th>
            <th>Delete</th>
           </tr>
          </thead>
          <tbody>
          	   <%  int i=0; %>
          	   <s:iterator value="requestedPOList">
          	     <tr>
          	       <td><%=i+1 %>  <input type="hidden" id="catalogueidold<%=i%>" value="<s:property value="catalogueid"/>">
          	       	<input type="hidden" value="<s:property value="id"/>" id="temppoid<%=i%>" />
          	       </td>
          	       <td><s:property value="indent"/>
          	       <td><s:property value="from_location"/>
          	       <td><s:property value="req_userid"/>
          	       <td><s:property value="pro_code"/></td>  
          	       <td><s:property value="product_name"/></td>
          	       <td><s:property value="stock"/></td>
          	       <td><s:property value="purchase_price"/></td>
          	       <td><s:property value="discount"/></td>
          	       <td><s:property value="freeqty"/></td>
          	       <td><s:property value="vat"/>%</td>
          	       <td><s:property value="minorder"/></td>
          	       <td><s:property value="maxorder"/></td>
          	       <td>
          	       	<s:property value="vendor"/>
          	          <%-- <select class="form-control chosen-select" id="vendorid<%=i%>" >
          	             <option value="0">Select Supplier</option>
          	             <s:iterator value="vendorlist">
          	                 	<s:if test="grnpovendorid==vendor_id">
          	                 	  	<option selected="selected" value="<s:property value="id"/>"><s:property value="name"/></option>
          	                 	</s:if>
          	                 	<s:else>
          	                 			<option value="<s:property value="id"/>"><s:property value="name"/></option>
          	                 	</s:else>
          	             </s:iterator>
          	          </select> --%>
          	       </td>
          	       <td><s:property value="qty"/></td>
          	       <td>
          	       <article>
						<ul class="vertical default_list" id="">
						  <%--  <s:if test="newpo==1">
						      <li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input value="<%=i%>" type="checkbox" checked="checked" class="form-control dcase" /><i></i> </label></li>
						   </s:if>
						   <s:else>
						   		<li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input value="<%=i%>" type="checkbox" class="form-control dcase" /><i></i> </label></li>
						   </s:else> --%>
						   <li><label class="checkbox checkbox-custom-alt m-0 mt-5"><input value="<%=i%>" type="checkbox" class="form-control dcase" /><i></i> </label></li>
						</ul>
					</article>
          	       </td>
          	         <td><a href="#" onclick="deleteTempReq(this,'prodtableid',<s:property value="id"/>,<s:property value="catalogueid"/>)" ><i class="fa fa-times text-danger fa-2x" aria-hidden="true"></i></a></td>
          	     </tr>
          	     <%i++; %>
          	   </s:iterator>
          	
          	</tbody> 
         </table>
		</div>
   </form> 
	<form action="requestedpoProcurement" id="searchgrnwithpo">
		<s:hidden name="searchtext" id="searchtextsave"></s:hidden>
	</form>    
</div>




    
    <s:form action="requestedpoProcurement" name="paginationForm" id="paginationForm" theme="simple">
							    
									<div class="col-lg-12 col-md-12" style="padding:0px;">
									<div class="col-lg-4 col-md-4 text-left" style="padding:0px;">
										Total:<label class="text-info"><s:property value="totalRecords" /></label>
									</div>
									<jsp:include page="/common/pages/pagination.jsp"></jsp:include>
								</div>
								 <s:hidden name="searchtext"></s:hidden>
							</s:form> 
    
</div>

<!-- Add New Product Modal -->
		<div id="addnewproduct" class="modal fade" role="dialog" style="padding-top: 10px;min-height: 185px;">
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
        				<label>Select Supplier</label>
        					<s:select theme="simple" id="vendorid"
						 list="vendorList"
						listKey="id" listValue="name" headerKey="0"
						headerValue="Select Supplier"
						cssClass="form-control chosen-select" />
        				</div>
        			</div>	
						
					<div class="col-lg-12 col-md-12">
        				<div class="form-group">
        					<label>Select Category</label>
        					<s:select list="categoryList" theme="simple" onchange="getsubproducttype(this.value)" cssClass="form-control chosen-select" id="productcategoryid" name="categoryid" listKey="id" listValue="name" headerKey="0" headerValue="Select Category" ></s:select>
        				</div>
        			</div>
        			<div class="col-lg-12 col-md-12">
        				<div class="form-group" id="subprodtypediv" >
        					<label>Sub Category</label>
        					<%-- <s:select list="subcategoryList" theme="simple" cssClass="form-control chosen-select" id="subcategory_id" name="subcategory_id" listKey="id" listValue="name" headerKey="0" headerValue="Select SubCategory" ></s:select> --%>
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
									<%if(loginInfo.isAuto_generic_name()){ %>
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
							onclick="savenewCatalogue(1)" style="margin-top: 15px;">Save</button>
						<button type="button" class="btn btn-primary hidden" style="margin-top: 15px;"
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
  
  <script type="text/javascript" src="_assets/newtheme/js/vendor/slimscroll/jquery.slimscroll.min.js"></script>

<script>
				             $(function() {
								  $('.fixedscroll').slimScroll({
								   		height : '150px',
								   		railVisible: true,
										alwaysVisible: true
								  });
				 				});
				             
				             $(function() {
								  $('.fixedscroll1').slimScroll({
								   		height : '275px',
								   		railVisible: true,
										alwaysVisible: true
								  });
				 				});
 				 
             </script>

<script>
	 function myPrint() {
            window.print();
        }
</script>

</body>
</html>