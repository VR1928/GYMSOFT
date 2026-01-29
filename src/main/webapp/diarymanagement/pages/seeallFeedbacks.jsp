<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>
<script type="text/javascript" src="diarymanagement/js/feedback.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css">
<div class="row details row details mainheader">
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
</div><center><h4><i class="fa fa-comments"></i> &nbsp;All FeedBacks</h4></center>
</div>

<div class="row ">
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
		<br>	
		<s:form action="seeallFeedbacksClient" theme="simple"   align="center"  id = "feedbackform">
		
		<div class="form-inline">
			<div class="form-group">
			<div class="form-group">
		
				<s:select name="treatmenttype" id="treatmenttype" 
				list="#{'opd':'OPD','ipd':'IPD','hd':'HD'}"
				cssClass="form-control chosen-select" onchange="getlist(this.value)"></s:select>
			</div>
			</div>
			</div>
			</s:form>
		
				
					<br><%int i=0; %>
						<table class="my-table xlstable " style="width: 100%">
						<tr><th>sr no</th>
						<th> Date</th>
						<th> Patient name</th>
						
						<th> Remarks</th>
						<th> Over All Rating</th>
						<th>print</th>
						</tr>
						<s:iterator value="feedbackbypatient">
						<tr>
						<td><%=++i %></td>
						<td><s:property value="date"/></td>
						<td><s:property value="clientName"/></td>
						
						<td><s:property value="feedbackname"/></td>
					<td><meter value="<s:property value='rating'/>" min="0" max="<s:property value='total'/>" low="<s:property value="total/2"/>"></meter>  <s:property value="rating"/> / <s:property value="total"/></td>
						<td><a href="#" onclick="opencPopup('printfeedbackOFPatientClient?id=<s:property value="feedbackparentid"/>&clientname=<s:property value="clientName"/>&date=<s:property value="date"/>&feedback=<s:property value="feedbackname"/>')"><i class="fa fa-print" aria-hidden="true"></i></a></td>
						</tr>
						</s:iterator>
						</table>
			
			</div>
			</div>								



