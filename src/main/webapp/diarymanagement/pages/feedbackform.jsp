<%@page import="com.apm.DiaryManagement.eu.entity.Client"%>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Date"%>


<script type="text/javascript" src="diarymanagement/js/feedback.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css">
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>

<%request.setCharacterEncoding("UTF-8");response.setCharacterEncoding("UTF-8"); %>
<style>

body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box}
/* Full-width input fields */
input[type=text], input[type=password] {
    width: 100%;
    padding: 15px;
    margin: 5px 0 22px 0;
    display: inline-block;
    border: none;
    background: #f1f1f1;
}
.modal-content {
    background-color: #fefefe;
    margin: 2% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 100%; /* Could be more or less, depending on screen size */
    
    }

.form-group{    
margin-left: 5px;
margin-right: 5px;
    
    }
    .signupbtn {
    padding: 14px 20px;
    background-color: #f44336;
    }
p {
   
    font-size:12px;
}
.radio-green-gap [type="radio"].with-gap:checked+label:before {
    border-color: #00C851;
}

.radio-green-gap [type="radio"]:checked+label:after {
    border-color: #00C851;
    background-color: #00C851;
}      
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<div class="row details row details mainheader">
<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
</div><center><h4><i class="fa fa-comments"></i> &nbsp; FeedbacK Form</h4></center>
</div>
	<div class="row ">
		<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
		<br>									
<s:form action="feedbackFormClient" theme="simple"   align="center"  id = "feedbackform">


<div class="form-inline">
			<div class="form-group">
			<div class="form-group">
		
				<s:select name="treatmenttype" id="treatmenttype" 
				list="#{'opd':'OPD','ipd':'IPD','hd':'HD'}"
				cssClass="form-control chosen-select" onchange="getlist(this.value)"></s:select>
			</div>
			
			<div class="form-group">
				<s:select name="patient" id="patientlist" list="patientlist" listKey="id" listValue="fullname"  data-live-search="true" cssClass="form-control chosen-select" headerKey="0" headerValue="All Patients" theme="simple" onchange="getPatient(this.value)"></s:select>
			</div>
					
					<div class="form-group hidden">
					<s:submit cssClass="btn btn-primary" value="go"></s:submit>
					</div>
					<div class="form-group">
					 <button type="button" class="btn btn-success"  onclick="opencPopup('seeallFeedbacksClient')">See All feedBacks</button>
				
					</div>
			
			</div>
			</div>
			
			</s:form>
			<div class="form-group ">
					<button  onclick="allfeedbackpatients()" class="btn btn-primary">Patients</button>
					</div>	
			<p><strong>Dear Patient,</strong></p>
			<p>You are one of the respected patient of our clinic. Please rank our service and our performance for helping us to serve you better. </p>
			<p>The ranking may be done on 1 to 5 scale where:</p>
			<p>1 -<b> Very Poor</b>  &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;        2 -<b> Poor </b>   &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;    3 -<b> Satisfactory</b>
		 &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;  4 -<b> Good  </b>    &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;  &nbsp;&nbsp; &nbsp; &nbsp;    5 - <b>Very Good </b></p>
			<p>[Please give your free and frank opinion. This information will be kept confidential and shall be used to improve our performance]</p>
			<s:form action="savefeedbackClient" theme="simple"  cssClass="modal-content "   id = "invoicerportfrm">
			<table class="my-table xlstable " style="width: 100%">
			<tr></tr>
			<tr>
			<th width="5%"> &nbsp;&nbsp;Sr. no</th>
			<th>Feedback Template</th>
			<th>Rating</th>
			
			<s:hidden name="feedbackids" id="feedbackids"/>
			<s:hidden name="patient" id="patient"/>
				<s:hidden name="treatmenttype" id="treatmenttype1"/>
			
			</tr>
			<tr><td></td>
			<td></td>
			<td><table width="100%"> <tr ><td>5 </td> <td> 4   </td>  <td>3  </td> <td> 2   </td><td>  1</td></tr></table> </td>
			</tr>
			<%ArrayList<Client> list=(ArrayList<Client>)request.getAttribute("feedbacklist");
			int val=0;
			%>
			<% int i=0; %>
				<s:iterator value="feedbacklist">
			<tr><td> &nbsp;&nbsp;<%=++i %></td>
				<%-- <td><s:property value="feedbackname"/><input type="hidden" name='feedback<s:property value="feedbackid"/>' value='<s:property value="feedbackname"/>'/></td> --%>
				 <td><%=list.get(val).getFeedbackname().toString() %><input type="hidden" name='feedback<s:property value="feedbackid"/>' value='<s:property value="feedbackname"/>'/></td> 
				<td><table width="100%">
				<tr>
			<td><input type="radio" class="" name="rating<s:property value="feedbackid"/>" value="5"></td>
      				<td>	 <input type="radio" name="rating<s:property value="feedbackid"/>" value="4"></td>
      				<td>	 <input type="radio" name="rating<s:property value="feedbackid"/>" value="3"></td>
      				<td>	  <input type="radio" name="rating<s:property value="feedbackid"/>" value="2"></td>
      					<td>  	<input type="radio" name="rating<s:property value="feedbackid"/>" value="1"> </td>	
   					</tr>
   					<%val++; %>
   					</table>
     				
      					
  </td>
			</tr>
			</s:iterator>
		</table>
		<br>
		 <div class="form-group">
    <label for="inputlg">Your Valued Remark, Suggestion For Improving Our Performance </label>
    <input class="form-control input-lg" id="inputlg" type="text" name="manualfeedback">
  </div>
		<br>
<center><s:submit value="Save FeedBack" align="center" theme="simple" cssClass="btn btn-primary" style="margin:5px" onclick="return checkifChecked()"></s:submit></center>
	
</s:form>	
</div>
<br>
<br>
<script src="common/chosen_v1.1.0/chosen.jquery.js" type="text/javascript"></script>
  <script type="text/javascript">
    var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }
  </script>

</div>
