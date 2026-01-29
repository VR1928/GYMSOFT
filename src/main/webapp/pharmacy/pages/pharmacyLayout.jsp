
<%@page import="com.apm.main.common.constants.Constants"%>
<%@page import="com.apm.common.web.common.helper.LoginHelper"%>
<%@page import="com.apm.common.web.common.helper.LoginInfo"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--Mobile first-->
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">

<!--IE Compatibility modes-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>HIS</title>
<link rel="icon" href="Design/images/icon.ico">



<link href="common/BootstrapNew/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="common/BootstrapNew/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="_assets/newtheme/css/main.css">
<link href="common/css/Style.css" rel="stylesheet" type="text/css" />
<link href="common/css/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="common/css/responsive.css" rel="stylesheet" type="text/css" />

<script src="common/js/jquery.js" type="text/javascript"></script>
<script src="common/js/jquery.alerts.js" type="text/javascript"></script>
<link href="common/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />


<link href="common/chosen_v1.1.0/chosen.css" rel="stylesheet" type="text/css" />
<link href="common/BootstrapNew/fullcalendar-2.2.3/fullcalendar-2.2.3/lib/cupertino/jquery-ui.min.css" rel="stylesheet" />

<!-- vendor css files -->
<link rel="stylesheet" href="_assets/newtheme/css/vendor/animate.css">
<link rel="stylesheet" href="_assets/newtheme/css/vendor/font-awesome.min.css">
<!-- <link rel="stylesheet" href="_assets/newtheme/js/vendor/animsition/css/animsition.min.css"> -->
<!-- project main css files -->

<link rel="stylesheet" type="text/css" href="dist/styles/jquery.stickynotif.min.css" />
<link rel="stylesheet" type="text/css" href="_assets/newtheme/js/vendor/preloader/css/Circleloader.css" />
      
  
  
   <link href="_assets/newtheme/css/mbile.css" rel="stylesheet" type="text/css" />
 <script src="_assets/newtheme/js/ui.js" type="text/javascript"></script>   
      
<!--/ stylesheets -->
<!-- <script src="_assets/newtheme/js/vendor/modernizr/modernizr-2.8.3-respond-1.4.2.min.js"></script> -->
<script src="common/js/jquery.js" type="text/javascript"></script>

<script src="popupdialog/dialog/js/jquery-1.10.2.js" type="text/javascript"></script>

<script src="popupdialog/dialog/js/jquery-ui.min.js" type="text/javascript"></script> 
<script src="common/BootstrapNew/js/bootstrap.min.js" type="text/javascript"></script> 

<script src="popupdialog/dialog/js/jquery.ui.core.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.widget.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.datepicker.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.dialog.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.draggable.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.droppable.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.sortable.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.mouse.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.button.js"></script>
<script src="popupdialog/dialog/js/jquery.ui.position.js"></script>
<!-- <script src="popupdialog/dialog/js/jquery.ui.tooltip.js"></script> -->
<script src="common/chosen_v1.1.0/chosen.jquery.js" type="text/javascript"></script>
<script src="common/js/jquery/additional-validation-methods.js" type="text/javascript"></script>
<script src="common/js/jquery/jquery.validate.js" type="text/javascript"></script>

<script type="text/javascript" src="common/js/fullscreen.js"></script>
<script type="text/javascript" src="common/js/masterValidators.js"></script>
<script type="text/javascript" src="common/js/jquery/bootstrap-growl-master/bootstrap-growl.min.js"></script>
<!-- <script src="common/BootstrapNew/bootstrap/js/jquery.scrollTo.min.js"></script> -->
<!-- <script src="common/BootstrapNew/js/jquery.nicescroll.js"></script> -->

<script src="dist/jquery.stickynotif.min.js"></script>
<script src="common/blockui-master/jquery.blockUI.js" type="text/javascript"></script>
<script src="common/BootstrapNew/fullcalendar-2.2.3/fullcalendar-2.2.3/lib/moment.min.js"></script>
<script src="common/BootstrapNew/fullcalendar-2.2.3/fullcalendar-2.2.3/fullcalendar.min.js"></script>
<script src="_assets/newtheme/js/vendor/jRespond/jRespond.min.js"></script>
<script src="_assets/newtheme/js/vendor/sparkline/jquery.sparkline.min.js"></script>
<script src="_assets/newtheme/js/vendor/slimscroll/jquery.slimscroll.min.js"></script>
<script src="_assets/newtheme/js/vendor/animsition/js/jquery.animsition.min.js"></script>
<script src="_assets/newtheme/js/main.js"></script>

<!-- 
    <script src="_assets/newtheme/js/vendor/preloader/js/Circleloader.js"></script>
 	

--><!--/ modernizr -->
<style>
    #header {
        background-color: #339966;
            }
    .texthead {
           font-size: 15px;
            }
            .table.table {
    margin-top: 0 !important;
    margin-bottom: 0 !important;
    color: RGBA(85, 85, 85, 0.85);
    border: 1px solid #efefef;
}
</style>

</head>
<body id="his" class="appWrapper hz-menu">



<!-- Modal -->
<div class="modal fade" id="shortcutkeystips" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Shortcut TIPS</h5>
        <button style="margin-top: -26px;" type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

<ul>
<h4>
 <li>Alt + S  -    For Save </li>

<div style="height: 6px;"></div>
 
<li> Alt + P   - For Add New Patient</li>
<div style="height: 6px;"></div>
 
<li> Alt + A  -    For  Search Patient</li>
<div style="height: 6px;"></div>
 
<li> Go to bar code  Press Alt B</li>
<div style="height: 6px;"></div>
 
<li>Alt N -    For  New Sale</li>
<div style="height: 6px;"></div>
 
<li>Alt C  -    For Clear Balance</li>
<div style="height: 6px;"></div>
 
<li>Alt + Left Arrow -    For  Back</li>
<div style="height: 6px;"></div>
 
<li>Clt + R  -    For Refresh Page</li>
<div style="height: 6px;"></div>
 
<li>Alt + D  -    For Go to Discount </li>
<div style="height: 6px;"></div>
 
<li>Alt + X  -    For Close Popup </li>
<div style="height: 6px;"></div>
 
<li>Crtl + W  -    For Close Window Popup </li>

</h4>
</ul>


      </div>
    <!--   <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div> -->
    </div>
  </div>
</div>




<div id="demo-content">
<!--<div id="loader-wrapper">
            <div id="loader"></div>
            <div class="loader-section section-left"></div>
            <div class="loader-section section-right"></div>
        </div>
--><div id="wrap content">
  <section id="header">
  	<tiles:insertAttribute name="header" />
  </section>
	
	<div id="masterbodycontainer">
	<!-- 	<div class="row"> -->
			<div class = "col-lg-12 hidden-print" style="padding-top:0px;" id="masterbodydiv">
			
			</div>
      			<!--sidebar start-->
       			<div id="controls">
           			<tiles:insertAttribute name="menu" />
           			
           			
				</div>
				
				<section id="content">
                     <tiles:insertAttribute name="body" />
         		 </section>
			
		<!-- </div> -->
	</div>


	 </div>
	</div>
</body>
</html>



<div class="modal fade" style="background: rgba(255, 255, 255, 0.93);" id="dashboardloaderPopup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="">
				<div class="modal-body text-center">
					<img src="common/images/hourglass1.gif" class="img-responsive" style="margin-left:auto;margin-right:auto;"></img>
					
				</div>
			</div>
		</div>
	</div>	
