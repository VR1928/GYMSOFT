<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<style>
.listing-item{
  list-style: none;
  display: inline-block;
  width: 100%;
  padding: 0;
  margin: 0;
}
li {
    display: list-item;
    text-align: -webkit-match-parent;
}
/*---------- c. experience -----------*/
.wrap-card {
  position: relative;
  box-shadow: 0px 0px 0px #818181;
  transition: all 0.2s ease 0s;
  -webkit-transform: translateY(0px) translateX(0px);
}
.wrap-card:before,
.wrap-card:after {
  display: inline-block;
  position: absolute;
  content: " ";
  width: 100%;
  height: 3px;
  background: #818181;
  -webkit-transform: scaleX(0);
  transition: all 0.2s ease;
}
.wrap-card:before {
  top: 0;
  left: 0;
  -webkit-transform-origin: 0 0;
}
.wrap-card:after {
  bottom: 0;
  left: 0;
  -webkit-transform-origin: 100% 0;
}

.wrap-card:hover:after,
.wrap-card:hover:before {
  -webkit-transform: scaleX(1);
}
.card {
  background: #ffffff;
  padding: 10px 15px 0px 15px;
  margin-bottom: 0px;
  border: 1px solid rgba(129, 129, 129, 0.1);
  height: 200px;
}
.card:before,
.card:after {
  display: inline-block;
  position: absolute;
  content: " ";
  width: 3px;
  height: 100%;
  background: #818181;
  -webkit-transform: scaleY(0);
  transition: all 0.2s ease 0.2s;
}
.card:before {
  top: 0;
  left: 0;
  -webkit-transform-origin: 0 100%;
}
.card:after {
  top: 0;
  right: 0;
  -webkit-transform-origin: 0 0;
}
.card:hover:after,
.card:hover:before {
  -webkit-transform: scaleY(1);
}
.iconcirclest{
	    text-align: center;
    margin-top: 0px;
    margin-bottom: 10px;
    font-size: 18px !important;
}
.imgiconst{
    width: 39%;
    margin-left: auto;
    margin-right: auto;
}
hr {
    margin-top: 10px;
    margin-bottom: 10px;
    border: 0;
    border-top: 1px solid #eee;
}
.setcheck{
	float: right;
    margin-top: -43px;
}
.rolaccess{
width: 9%;
    float: right;
    position: relative;
    margin-top: -33px;
}
p{
text-align: center !important;
}
</style>

<SCRIPT type="text/javascript">

   function dosubmit(){
   
         var jobtitle=document.getElementById("jobtitle").value;
         if(jobtitle=="0"){
         
                 alert("please select jobtitle");
         } else {
             document.widgetform.submit();
         }    
    
   }
   
   
   function getwidgets(val) {
       
       document.getElementById("pjobtitle").value=val;
       document.getElementById("mychangeform").submit();      
   }

</SCRIPT>

</head>
<body>
<section>

<div>
    <s:form theme="simple" action="updateDiaryMangent" method="post" name="widgetform">
	<div class="row">
	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12" style="margin-top: 10px;">
		<p class="text-justify">Welcome to Smart HIS!!! Now you can set Widgets manually and this will set in main dashboard..</p>
		<div class="rolaccess">
		<s:select list="jobTitleList" id="jobtitle" name="jobtitle" cssClass="form-control" headerValue="Select Job Title" headerKey="0" onchange="getwidgets(this.value)"></s:select>
		</div>
	</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="padding-left: 0px;padding-right: 0px;">
			<ul class="listing-item">
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                              OPD
                            </h2>
                            <p class="job">
                             <img src="dashboardicon/stethoscope.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="opd"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Manage Out Patient Location
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             IPD
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/hospital-bed.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="ipd"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Manage In-Patient Location
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             OT
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/OT.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="ot"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Occupational Therapy
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Casualty
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/casualty.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="casualty"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                            	Casualty Dashboard
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             EMR
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/electrocardiogram.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="emr"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Electronic Medical Record 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Pac's
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/pacs.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="packs"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Packs 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Discharge
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/discharge.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="discharge"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Discharge Dashboard 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Medicine
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/pills-1.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="medicine"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Medicine Dashboard 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Pharmacy
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/pharmacy.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="pharmacy"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Pharmacy Dashboard 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Investigation
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/microscope.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="investigation"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Investigation Dashboard
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Blood Bank
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/blood-donation.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="bloodbank"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Manage Blood Bank
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Accounts
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/accounts.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="accounts"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                View Account 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             MRD
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/mrd.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="mrd"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Mrd dashborad 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Payroll
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/payroll.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="payroll"></s:checkbox>  
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Employee Payroll Management 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Expenses
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/expensis.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="expenses"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Expenses Management 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Inventory
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/inventory.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="inventory"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Inventory Management
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             MIS
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/mis.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="mis"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Management Information System 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Consultants
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/ambulance.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="consultants"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Manage Visiting Consultant
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Patient
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/clints.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="patient"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Add/Edit/Manage Patient 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Appointment Finder
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/shirt.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="appointmentfinder"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Search Patient Future Appointment 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                  
					
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Analytics
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/investgraph.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="investigation_chart"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Investigation Chart
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Sheduler
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/staff.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="sheduler"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Sheduler 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             House Keeping
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/waste.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="housekeeping"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                  House Keeping
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Dieteary
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/diet.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="dietery"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Dietary 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Cafeteria
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/chef.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="cafeteria"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Cafeteria 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Packages
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/packages.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="packages"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Packages 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Ambulance
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/ambulancevisit.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="ambulance"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Ambulance 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Bank Deposite
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/bank.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="bank_deposite"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Bank Deposite 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Account Reconcilation
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/reconciliation.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="account_reconcilation"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Account Reconcilation 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             		Marketing
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/marketing.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="marketing"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                	Marketing Dashboard 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Voice Recorder
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/voicerecoder.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="voice_recording"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Voice Recorder Dashboard 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    
                    
                    
                    
                    
                    
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Setting
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/settings.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="setting"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Setting 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <!-- Akash -->
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             TPA
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/tpa.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="tpa"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                TPA
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Quality
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/KPI.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="nabh_quality"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Quality 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Indent
                            </h2>
                            <p class="job">
                              <img src="dashboardicon/indent_request.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="indent"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Indent 
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                     <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                             Doctor OPD
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/stethoscope.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="doctor_opd"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                                Doctor OPD
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
        			<li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                            Cathlab
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/cathlab.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="cathlab"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                               Cathlab
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                            My HR
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/ambulance.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="myhr"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                               My HR
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                            Day Care
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/DayCare.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="daycare"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                               Day Care
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                            Emergency Label
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/emergencylbl.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="emergency_lbl"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                               Emergency Label
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                    
                    <li>
                      <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="padding-left: 0px;padding-right: 0px;">
                        <div class="wrap-card">
                          <div class="card">
                            <h2 class="iconcirclest">
                            Medicine Barcode
                            </h2>
                            <p class="job">
                             	<img src="dashboardicon/barcode_icon.png" class="img-responsive imgiconst"></img>
                            </p>
                            <p class="company">
                              <div class="checkbox setcheck">
							    <label>
							      <s:checkbox name="medicine_barcode"></s:checkbox> 
							    </label>
							  </div>
                            </p>
                            <hr>
                            <div class="text-detail">
                              <p class="text-justify">
                               Medicine Barcode
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                  </ul>
		</div>
		
		<div class="col-lg-12 col-sm-12 col-xs-12" style="margin-bottom:10px;">
		<div class="text-right">
			<a href="#" onclick="dosubmit()" class="btn btn-primary" tupe="button">Save Changes</a>
		</div>
		</div>
	</div>
	</s:form>
</div>

</section>

      <s:form id="mychangeform" action="getwidgetDiaryMangent">
      
         <s:hidden name="jobtitle" id="pjobtitle"></s:hidden>
      </s:form>      


</body>
</html>