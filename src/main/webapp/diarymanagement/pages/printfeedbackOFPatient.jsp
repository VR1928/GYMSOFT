<%@page import="com.apm.DiaryManagement.eu.entity.Client"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>

<style>
@media print {
	.hh{
	color:white !important;
	}
}
</style>
<script type="text/javascript" src="diarymanagement/js/feedback.js"></script>
<script type="text/javascript" src="assesmentForms/js/jquery.table2excel.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css">
	<div class="print-visible hidden-md hidden-lg" style="height: 135px;">
		<div id="newpost" class="col-lg-12 col-md-12 col-xs-12 col-sm-12 borderbot">
			<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="padding-left:0px;padding-right:0px;">
				 <link href="common/css/printpreview.css" rel="stylesheet" />
			<%@ include file="/accounts/pages/letterhead.jsp" %>
			</div>
		</div>
	</div>
<div class="row details row details mainheader">
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

</div><center><h4 class='hh'><i class="fa fa-comments"></i> &nbsp;FeedBack Print</h4></center>
</div>

<div class="row ">

	<%Client clientDetails=(Client)request.getAttribute("clientData"); %>
	<%Client feedbackDetails=(Client)request.getAttribute("FeedBack"); %>			
	
	<%feedbackDetails.setAdmissiondate(DateTimeUtils.isNull(feedbackDetails.getAdmissiondate())); %>
			
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">

	
		<div class="form-inline">
	
		<div align="right" class="form-inline"><a type="button" class="btn btn-info hidden-print"  title="Print" onclick="printpage()">Print</a></div>
<br>	
<div class="col-lg-6 col-md-6 col-xs-6 col-sm-6">
<label>Client Name :</label>	<span><s:property value='client'/></span><br>
<label>Age / Gender:</label>	<span><%=clientDetails.getAge1() %>/ <%=clientDetails.getGender() %></span>
<br><label>Address:</label>	<span><%=clientDetails.getAddress() %>, <%=clientDetails.getTown() %>, <%=clientDetails.getCounty() %></span>
<br><label>Contact No :</label>	<span><%=clientDetails.getMobNo() %></span>
<br><label>Date :</label>	<span><s:property value='date'/></span>
</div>
<div class="col-lg-6 col-md-6 col-xs-6 col-sm-6">
<label>UHID :</label>	<span><%=clientDetails.getAbrivationid() %></span>

<%if(feedbackDetails.getIpdid().equals("0")){ %>
<br><label>OPD ID :</label>	<span><%=feedbackDetails.getOpdid() %></span>
<%}else{ %>
<br><label>IPD ID :</label>	<span><%=feedbackDetails.getIpdid() %></span>
<%if(feedbackDetails.getAdmissiondate().contains("-")){ %>
	<br><label>Admission Date :</label>	<span><%= DateTimeUtils.getCommencingDate1(feedbackDetails.getAdmissiondate().split(" ")[0])  %> <%=feedbackDetails.getAdmissiondate().split(" ")[1] %></span>
<%} %>
<%} %>
</div>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
<table  class="my-table xlstable table-striped" style="width: 100% ;border:1px">
<tr >
<td style="background-color:#87bdd8"><b>Feedback Template</b></td>
<td style="background-color:#87bdd8"><b>ratings</b></td>
</tr >
<s:iterator value="feedbacklist">
<tr style="border:1px #dfd8d4 solid !important">
<td><s:property value="feedbackname"/></td>

<td><meter value="<s:property value="rating"/>" min="0" max="5"></meter>  <s:property value="rating"/> / 5</td>
</tr>
</s:iterator>
</table>
</div>
</div>
<br>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 ">
<p>Thank you <strong><s:property value="client"/></</strong> for taking the time to fill our feedback form. By providing your valuable feedback you are helping us to understand what we do better and what improvements  we need to implement </p>
<p> Your Remark was :<strong>  <s:property value="manualfeedback"/></strong></p>
<p><b>Date: </b>  <s:property value="date"/></p>
</div>
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 print-visible hidden-md hidden-lg" align='right'>
<br>
<br>
<br>
<br>
</br>
<p><h3>Signature</h3></p>

<p></p>
<p>Information within feedback form will be used for service improvement only and will be confidential</p>
</div>
</div>

</div>