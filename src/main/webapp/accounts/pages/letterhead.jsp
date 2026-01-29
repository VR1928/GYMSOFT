		<%@page import="com.apm.common.utils.DateTimeUtils"%>
<%@taglib uri="/struts-tags" prefix="s" %>
	
	<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
	<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
	<%LoginInfo letterreq = LoginHelper.getLoginInfo(request);%>
<%String logowidth="";
String bghlogo="";
 if(letterreq.isBalgopal()==true){
	logowidth="logowidth";
	bghlogo="bghlogo";
} 
%>
<%-- <script>
window.onload =function(){ 
	 var clinic=document.getElementById("balgopal").value;
     if(clinic=='true'){
    	 if (window.matchMedia("(orientation: landscape)").matches) {
    		 document.getElementById("mainlogoclinic").className="col-lg-2 col-md-2 col-sm-2 col-xs-2 logoimg bghlogo1";
    		}
    	 
    	 if (window.matchMedia("(orientation: portrait)").matches) {
    			document.getElementById("mainlogoclinic").className="col-lg-2 col-md-2 col-sm-2 col-xs-2 logoimg bghlogo";
    		}
     }
}
</script> --%>
<style>
		.logoste{
			width: 100%;
	    	margin-left:auto;
	    	margin-right:auto;
		}
		.logoste1{
			width: 100%;
	    	margin-left:auto;
	    	margin-right:auto;
	    	padding-right: 0px;
		}
		.bghlogo{
    width: 147px !important;
    margin-top: -33px !important;

		}
		.bghlogo1{
    width: 147px !important;
    margin-top: -33px !important;

		}
		@media print {
		
		.bghlogo{
    width: 13% !important;
    margin-top: -5px !important;

		}
		.bghlogo1{
    width: 18% !important;
    margin-top: -5px !important;
	padding-top: 10px !important;
		}
		    .logoste1 {
			    margin-top: 6px !important;
			}
			
			
			#ltterimg{  margin-left: -11px ;}
			
			.logowidth {
			    width: 150% !important;
			}
		}
@media (orientation: landscape) {
 .bghlogo{
    width: 9% !important;
    margin-top: -5px !important;
  }
	</style>
		
	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" id="ltterimg">
		<input type="hidden" value="<%=letterreq.isBalgopal() %>" id="balgopal">
			<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2 logoimg <%=bghlogo %>"  id="mainlogoclinic">
			<div id="newletter" class="<%=logowidth %>">
			<% if(letterreq.getIslogo().equals("0")){%>
				<img class="img-responsive logoste hidden-print d-print-none" src="liveData/clinicLogo/<s:property value="clinicLogo"/>" />
			
				<% }else{%>
			
				<img class="img-responsive logoste1" src="liveData/clinicLogo/<s:property value="clinicLogo"/>" />
				<% }%>
				</div>
			</div>
			
			<%LoginInfo lll = LoginHelper.getLoginInfo(request); %>
		<div class="col-lg-10 col-md-10 col-sm-10 col-xs-10" id="lttertext"<%if(lll.isBalgopal()){ %>style="margin-left: 8px"<%} %>>
	
			<div class="clinicname" id="clinicnamenew">
			<% if(lll.isBalgopal()){%>
				<%String balgopalcname = (String)session.getAttribute("balgopalcname"); %>
				<%balgopalcname= DateTimeUtils.isNull(balgopalcname); %>
				<%=balgopalcname.toString()%>
				
			<%}else{ %>
				<s:property value = "clinicName"/>
			<%} %>
			
			
			
			</div>
			<S:IF TEST="CLINICOWNER!='' ">
				<DIV CLASS="CLIQUALIF"><S:PROPERTY VALUE = "CLINICOWNER"/> <S:PROPERTY VALUE = "OWNER_QUALIFICATION"/> </DIV>
				<s:if test="clinicOwner!='' ">
				
				<div class="cliqualif" style="font-weight: lighter;"><s:property value = "clinicOwner"/> </div>
			</s:if>
			</S:IF>
			<div class="clicniaddress">
			<% if(lll.isBalgopal()){%>
				<%String balgopaladdress = DateTimeUtils.isNull((String)session.getAttribute("balgopaladdress")); %>
				<%=balgopaladdress.toString()%>
			<%}else{ %>
			
				<s:iterator value="locationAdressList">
					<s:property value = "address"/> <br>
				</s:iterator>
			<% }%>
				
			</div>
			<div class="bottext">Phone:<s:property value ="landLine"/>,	 Email: <s:property value = "clinicemail"/>
				
				<s:property value = "owner_qualification"/>
				<s:if test="websiteUrl!='' ">
					<span style="white-space: nowrap;">Website: <s:property value = "websiteUrl"/></span>
				</s:if>
			 </div>
		
		</div>
		<a href="#"  class="hidden-print d-print-none hidekar" onclick="showhidecliniclogo()" style="margin-right:10px;float:right;background-color: grey;color: #fff;padding: 0px 5px 0px 5px;">Hide Logo</a>
	</div>
	<script>
    function showhidecliniclogo()
     {
           var div = document.getElementById("newletter");
    if(div.className==""){
    	div.className="hidden";
    }else{
    	div.className="";
    }
    }
  </script>		
	