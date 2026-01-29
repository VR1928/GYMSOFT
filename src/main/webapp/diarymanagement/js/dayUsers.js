var pbodytmplate;
var pbodyeditedtmplate;
function showpartapmt(id,val){
	/*$('#loading').modal({
	    backdrop: 'static',
	    keyboard: false  
	})
	$('#loading').modal( "show" );
	rdddval = val;
	
	setCommonAction();
	$('#loading').modal( "hide" );*/
	

var url = "rddBookAppointmentAjax?val="+val+"";
	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showpartapmtRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}

function showpartapmtRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			setCommonAction();
			/*$('#loading').modal({
			    backdrop: 'static',
			    keyboard: false  
			})
			$('#loading').modal( "show" );
			//rdddval = val;
			
			setCommonAction();
			$('#loading').modal( "hide" );*/
		}
	}
	
}


function getDaySearch(){
	
	//set wraperdiv element
	 if(editAppointId!=0){
			getWraperDivChildren(wraperDivId);
	 }
	
	jQuery('.new').children().unwrap();
	
	 		actionType =  2;
			var date = document.getElementById("commencing").value;
			var id = document.getElementById('diaryUsersss').value;
			var locationid = document.getElementById('locationid').value;
			var count = 0;
		/*	for(i=1;i<=13;i++){
				$(document.getElementById(i)).css('border-bottom', '1px solid #DFD8D4');
				document.getElementById(i).ondblclick = '';
				for(j=0;j<=11;j++){
					count = 5*j;
					$(document.getElementById(count+'min'+i)).css('background-color', 'white');
					$(document.getElementById(count+'min'+i)).css('border-bottom', '');
					//$(document.getElementById(count+'min'+i)).css('border-bottom-style', '');
					document.getElementById(count+'min'+i).innerHTML = '';
					count = parseInt(count)+1;
				}
				
				
				//document.getElementById(i).innerHTML = "";
			}
			*/
				var tempDate = date.split("/");
				/*var mm = tempDate[1];
				var yy = tempDate[2];
				var dd = parseInt(tempDate[0]) + j;
				//date = dd + "/" + mm + "/" + yy;
				date = getCalDate(dd,mm,yy)*/
				
				date = tempDate[0] + "-" + tempDate[1] +  "-" + tempDate[2];
				
				setDayJsoData(id,date,locationid);
			
			
			
}


function setDayJsoData(diaryUserId,date,locationid){

	$.ajax({
		url: "diarymanagement/pages/JQGridMasterNotAvailableSlot.jsp?diaryUserId="+diaryUserId+"&date="+date+"&locationid="+locationid+" " ,
		dataType : "json",
		success : function(json) {
			//alert("Your JSON : " + JSON.stringify(json));
			
			var data = JSON.parse(JSON.stringify(json));        
                     $.each(data,function(row,store)  {    
                        $.each(store,function(key,value) {
                            //id = value.id;
                            //name = value.name;
                           // address = value.address;
                            //age = value.age;
                           // alert(id + " : " + name + "  : " +address + " : " + age); 
                          	
                          	
                          	
                          globalEndTime = value.endtime;
                          	
                          
                          		
                         		var col = 1;
                          		//start time diff
                          		var stdiff = parseInt(value.starttime) - value.clinicstime;
                          		stdiff = (parseInt(stdiff) * 1) + parseInt(col);
                          		var tempStdiff = stdiff;
                          		
                          		//end time diff
                          		var etdiff = parseInt(value.endtime) - value.clinicstime;
                          		etdiff = (parseInt(etdiff) * 1) + parseInt(col);
                          		
                          		var i=0;
                          		var j=0;
                          		
                          		var stemp = value.starttime.split(":");
                          		var etemp = value.endtime.split(":");
                          		var slength = parseInt(stemp[1])/5;
                          		var elength = parseInt(etemp[1])/5;
                          		
                          		var action = "edit";
                          		
                          		
                          		var timediff = getTimeDifference(value.starttime,value.endtime);
                          		var diffemp = timediff.split(":");
                          		var hour = parseInt(diffemp[0])*60;
                          		timediff = hour + parseInt(diffemp[1]);
                          		var divlength = timediff/5;
                          		
                          		var size = 1;
                          		
                          		//$('#1').attr('colspan', 3);
								for (i=parseInt(stdiff);i<parseInt(etdiff);i=(i+size)){
									//$(document.getElementById(i)).css('border-bottom', '0px');
									$(document.getElementById(i)).css('font-size', '12px');
									document.getElementById(i).className = 'cssClass';
									document.getElementById(i).title = value.id+"/"+diaryUserId;
									document.getElementById(i).onclick = "";
									//document.getElementById(i).ondblclick = function() {
									//	$(this).MessageBox(value.weekfullname,0,action,value.id,stdiff,value.starttime,value.endtime,value.apmtduration,value.location,value.onlinebooking,time);
									//}
									
								}
								
								
									var jcount = 0;
									var divtime = 0;
									
									divlength = divlength - 1;
									
									for(j=0;j<=divlength;j++){
										jcount = 5*parseInt(slength);
										
										document.getElementById(jcount+'min'+stdiff).className = 'cssClass1';
										//aqua
										
										//$(document.getElementById(jcount+'min'+stdiff)).css('background-color', '#fcf8e3');
										$(document.getElementById(jcount+'min'+stdiff)).css('background-color', value.color);
										
											$(document.getElementById('30min'+stdiff)).css('background-image', 'url("diarymanagement/img/line.png")');
											$(document.getElementById('30min'+stdiff)).css('background-repeat', 'repeat-x');
										
											$(document.getElementById(jcount+'min'+stdiff)).css('background-color', value.color);
											
											newTitle = "";
											mytitle = "";
											var tmtitle = document.getElementById(jcount+'min'+stdiff).title;
											tmtitle = tmtitle.split(' ');
											if(tmtitle.length > 1){
												document.getElementById(jcount+'min'+stdiff).title = tmtitle[0];
											}
											
											 mytitle = document.getElementById(jcount+'min'+stdiff).title;
											 newTitle = mytitle + ' ' + '('+value.locationName+')';
											document.getElementById(jcount+'min'+stdiff).title = newTitle;
											
											
											$('#'+jcount+'min'+stdiff).click(function(){
											      //Some code
												$(this).MessageBox(value.id,value.starttime,value.endtime,value.apmtduration,value.location,value.diaryUser,diaryUserId,this.title);
												
												document.getElementById('diciplineName').value = value.discipline;
												isnewopd = 0;
												$( "#appointment" ).modal( "show" );
												clearFiledCommonAction();
											
												
												document.getElementById('client').value = "";
												document.getElementById('notes').value = "";
												
												document.getElementById('apmtType').value = 0;
												$("#apmtType").trigger("chosen:updated");
												$(".chosen").chosen({allow_single_deselect: true});
												
												document.getElementById('condition').value = value.discipline;
												$("#condition").trigger("chosen:updated");
												$(".chosen").chosen({allow_single_deselect: true});
												
												var tstr = '<select name="treatmentEpisode" id="treatmentEpisode" class="form-control showToolTip chosen"><option value="0">Select Treatment Episode</option></select>'
												document.getElementById('treatmentepisodeajax').innerHTML = tstr;
												$("#treatmentEpisode").trigger("chosen:updated");
												$(".chosen").chosen({allow_single_deselect: true});
												
												
												//$('#appointment').dialog('option', 'title', 'New Appointment');
												editAppointId = 0;
												editCommencing = value.commencing;
												slotstarttime = value.starttime;
												document.getElementById('date').value = value.commencing;
												document.getElementById('blockdate').value = value.commencing;
											
											//set cookie commencing
											document.cookie = "cookiecommencing=" + commencing;
											document.cookie = "cookiePractitionerId=" + diaryUserId;
											 });
											
										/*	document.getElementById(jcount+'min'+stdiff).onclick = function() {
												alert('hello')
														$(this).MessageBox(value.id,value.starttime,value.endtime,value.apmtduration,value.location,value.diaryUser,diaryUserId,this.title);
														
														document.getElementById('diciplineName').value = value.discipline;
														
														$( "#appointment" ).modal( "show" );
														clearFiledCommonAction();
													
														
														document.getElementById('client').value = "";
														document.getElementById('notes').value = "";
														
														document.getElementById('apmtType').value = 0;
														$("#apmtType").trigger("chosen:updated");
														$(".chosen").chosen({allow_single_deselect: true});
														
														document.getElementById('condition').value = 0;
														$("#condition").trigger("chosen:updated");
														$(".chosen").chosen({allow_single_deselect: true});
														
														var tstr = '<select name="treatmentEpisode" id="treatmentEpisode" class="form-control showToolTip chosen"><option value="0">Select Treatment Episode</option></select>'
														document.getElementById('treatmentepisodeajax').innerHTML = tstr;
														$("#treatmentEpisode").trigger("chosen:updated");
														$(".chosen").chosen({allow_single_deselect: true});
														
														
														//$('#appointment').dialog('option', 'title', 'New Appointment');
														editAppointId = 0;
														editCommencing = value.commencing;
														slotstarttime = value.starttime;
														document.getElementById('date').value = value.commencing;
														document.getElementById('blockdate').value = value.commencing;
													
													//set cookie commencing
													document.cookie = "cookiecommencing=" + commencing;
													document.cookie = "cookiePractitionerId=" + diaryUserId;
													
												
											
										}*/
									/*	document.getElementById(jcount+'min'+stdiff).onclick = function() {
											setGlobalData(value.id,value.starttime,value.endtime,value.apmtduration,value.location,value.diaryUser,diaryUserId,value.commencing,this.title);
										}*/
											
											$('#'+jcount+'min'+stdiff).click(function(){
												setGlobalData(value.id,value.starttime,value.endtime,value.apmtduration,value.location,value.diaryUser,diaryUserId,value.commencing,this.title);
											});
										slength = parseInt(slength) + 1;
										if(jcount==55){
											slength = 0;
											divlength = parseInt(divlength) - j;
											j = 0;
											stdiff = parseInt(stdiff) + 1;
											
										}
										
										
									}
									
								setDayUserjsonAvailableData(value.id,value.starttime,value.endtime,value.apmtduration,value.location,value.diaryUser,diaryUserId,col,value.practitionerid,1,value.commencing,value.clinicstime);
								
								
							
							
							
							
                        });        
                     });
			
		}
	});
			
			
}	



function setDayUserjsonAvailableData(diaryuserid,starttime,endtime,appointmentduration,location,diaryusername,userid,col,practitionerid,size,commencing,clinicstime){

	

	$.ajax({
		url: "diarymanagement/pages/JQGridMasterAvailableData.jsp?diaryuserid="+diaryuserid+"&practitionerid="+practitionerid+"&rdddval="+rdddval+" " ,
		dataType : "json",
		success : function(json) {
			//alert("Your JSON : " + JSON.stringify(json));
			
			
			
			  var data = JSON.parse(JSON.stringify(json));        
                     $.each(data,function(row,store)  {    
                        $.each(store,function(key,value) {
                            //id = value.id;
                            //name = value.name;
                           // address = value.address;
                            //age = value.age;
                           // alert(id + " : " + name + "  : " +address + " : " + age); 
                          	
                          
                          		var stdiff = parseInt(value.starttime) - clinicstime;
                          		stdiff = (parseInt(stdiff) * size) + parseInt(col);
                          		var tempStdiff = stdiff;
                          		
                          		
                          
                          //set appointment type color
                          		var stemp = value.starttime.split(":");
                          		var etemp = value.endtime.split(":");
                          		var slength = parseInt(stemp[1])/5;
                          		var elength = parseInt(etemp[1])/5;
                          		
                          		var timediff = getTimeDifference(value.starttime,value.endtime);
                          		var diffemp = timediff.split(":");
                          		var hour = parseInt(diffemp[0])*60;
                          		timediff = hour + parseInt(diffemp[1]);
                          		var divlength = timediff/5;
                          		
                          		var stdiff = tempStdiff;
                          			
                          		var tdhtmldata = "";
                          		
                          		var max=10;
                          		var min=4;
                          		
                          		var random = value.id;
                          		random = random + 's';
                          		
                          		var style = document.createElement('style');
                          		style.type = 'text/css';
                          		style.innerHTML = '.cssClass'+random+' { min-height:20px; min-width:99px; cursor:pointer; }';
                          		document.getElementsByTagName('head')[0].appendChild(style);
                          		
                          		var jcount = 0;
									
									divlength = divlength - 1;
									
									for(j=0;j<=divlength;j++){
										jcount = 5*parseInt(slength);
										
										document.getElementById(jcount+'min'+stdiff).className = 'cssClass'+random;
										if(jcount==30){
											$(document.getElementById('30min'+stdiff)).css('background-image', '');
										}
										
										
										
										if(value.status == 0){
											if(value.iscompleted == true){
												$(document.getElementById(jcount+'min'+stdiff)).css('background-color', '#81AB6D');
												
											
											}else if(value.dna==true){
												$(document.getElementById(jcount+'min'+stdiff)).css('background-color', '#F96467');
												
											}
											else if(value.otid>0){
												$(document.getElementById(jcount+'min'+stdiff)).css('background-color', 'rgb(173, 220, 255)');
												
											}
											
											else{
												$(document.getElementById(jcount+'min'+stdiff)).css('background-color', '#FCBA63');
											}
											
											/*document.getElementById(jcount+'min'+stdiff).ondblclick = function() {
												wraperDivId = 'new'+random;
												setModifyPopup(value.status,value.starttime,value.endtime,value.duration,value.clientname,value.notes,value.apmttype,value.id,value.arrivedstatus,value.dna,userid,value.clientId,value.commencing,value.practitionerEmail,value.clientEmail,value.charge,commencing,starttime,value.treatmentepisodeid,value.iscompleted);
												
											}*/
										}else if(value.status == 1){
											$(document.getElementById(jcount+'min'+stdiff)).css('background-color', 'rgb(236, 147, 147)');
											/*document.getElementById(jcount+'min'+stdiff).ondblclick = function() {
												wraperDivId = 'new'+random;
												setModifyPopup(value.status,value.starttime,value.endtime,value.duration,value.clientname,value.notes,value.apmttype,value.id,value.arrivedstatus,value.dna,userid,value.clientId,value.commencing,value.practitionerEmail,value.clientEmail,value.charge,commencing,starttime,value.treatmentepisodeid,value.iscompleted);
												
											}*/
											
										}else{
											document.getElementById(jcount+'min'+stdiff).onclick = function() {
												setGlobalData(diaryuserid,starttime,endtime,appointmentduration,location,diaryusername,userid,commencing);
												 
											}
										}
										
										
										  if(value.chargecompleted==true){
												$(document.getElementById(jcount+'min'+stdiff)).css('background-color', '#cccccc');
											
											}
									
										
										slength = parseInt(slength) + 1;
										
										if(jcount==55){
											slength = 0;
											divlength = parseInt(divlength) - j;
											j = 0;
											stdiff = parseInt(stdiff) + size;
											tdhtmldata = document.getElementById(stdiff).innerHTML;
										}
										
										
									}
									
									
									//wrap all
									var zp = '<div style="width:100px;height:100px;position:absolute;background-color:yellow;top:20px;left:20px;z-index:0;opacity:0.5;border:1px solid #333333;" onclick="zproperty()">z-index 0</div>'
									 $( ".cssClass"+random ).wrapAll( "<div id='new"+random+"'  class='new' ></div>");
									 var resizewidth = $('#thtestid').width();
									 var resultwidth = parseInt(resizewidth)+6;
									// alert(resultwidth);
									// resultwidth = resultwidth/4;
									$(document.getElementById('new'+random)).css('width', resultwidth);
									 if(tdhtmldata!=""){
										//alert(tdhtmldata)
										 //changeWrapedDivId('new'+random);
										 document.getElementById(stdiff).innerHTML = tdhtmldata; 
										 initilizeTdElement(stdiff,diaryuserid,starttime,endtime,appointmentduration,location,diaryusername,userid,commencing);
										 
										 if(stdiff!=6){
											// $(document.getElementById('new'+random)).css('position', 'absolute');
										 }
										
										 //$(document.getElementById('new'+random)).css('width', '95%');
										 
										 
										 //reinitilize wraped element
										 renitilizeWrapdedEvent(stdiff,'new'+random);
										 
									 }
									 
									 
									
									// $( "#new"+random ).resizable();
									/* document.getElementById('new'+random).onclick = function() {

										 $( "#appointment" ).modal( "hide" );
											wraperDivId = 'new'+random;
											setModifyPopup(value.status,value.starttime,value.endtime,value.duration,value.clientname,value.notes,value.apmttype,value.id,value.arrivedstatus,value.dna,userid,value.clientId,value.commencing,value.practitionerEmail,value.clientEmail,value.charge,commencing,starttime,value.treatmentepisodeid,value.iscompleted,diaryuserid,value.apmttypetext,value.location,value.conditionid,value.tptypeid,value.tpnameid,value.whopay);
											
										}*/
									
									 //modify popup
									 if(value.iscompleted == false){
										
										 document.getElementById('new'+random).onclick = function() {

											 $( "#appointment" ).modal( "hide" );
												wraperDivId = 'new'+random;
												 loc = value.locid;
												 
												 if(value.otid==0){
													setModifyPopup(value.status,value.starttime,value.endtime,value.duration,value.clientname,value.notes,value.apmttype,value.id,value.arrivedstatus,value.dna,userid,value.clientId,value.commencing,value.practitionerEmail,value.clientEmail,value.charge,commencing,starttime,value.treatmentepisodeid,value.iscompleted,diaryuserid,value.apmttypetext,value.location,value.conditionid,value.tptypeid,value.tpnameid,value.whopay,value.tpname,value.imagename,value.height,value.weight,value.bmi,value.pulse,value.sysbp,value.diabp,value.planed,value.procedure,value.surgeon,value.anesthesia,value.ipdno,value.wardid,value.asisdoclist,value.sugarfasting,value.postmeal,value.temprature,value.spo,value.bsa);
												 }else{
												 	
												 	showotmodifypopuop(value.id,value.clientId,value.clientname,value.location,value.whopay,value.imagename,value.height,value.weight,value.bmi,value.pulse,value.sysbp,value.diabp,value.temprature,value.spo,value.bsa);
												 }
											}
									 }
									 else if(value.iscompleted == true){
										 wraperDivId = 'new'+random;
										

										 patientId = value.clientId;
										 editClientName = value.clientname;
										 editwhopay = value.whopay;
										 editTreatmentEpisode = value.treatmentepisodeid;
										
										 document.getElementById('new'+random).onclick = function() {
											 wraperDivId = 'new'+random;

											 $( "#appointment" ).modal( "hide" );	
											 
											 if(value.chargecompleted==false){
												 wraperDivId = 'new'+random;
												 
												 //editCompleteApmt(value.id);
												 patientId = value.clientId;
												 pppid = value.clientId;
												 pppcname  = value.clientname;
												 pppwhopay  = value.whopay;
												 ppptepisode  = value.treatmentepisodeid;
												 
												 editAppointId = value.id;
												 editwhopay = value.whopay; //
												 editClientName = value.clientname;
												 loc = value.locid;
												 diaryuserId = userid;
												 editcondition_id = value.conditionid;
												 editTreatmentEpisode = value.treatmentepisodeid;
												 openCompleteActionPopup(value.id,height,weight,bmi,pulse,sysbp,diabp,temprature,spo,bsa);
											 }else{
												 
												
												  wraperDivId = 'new'+random;
												 
												 patientId = value.clientId;
												 pppid = value.clientId;
												 pppcname  = value.clientname;
												 pppwhopay  = value.whopay;
												 ppptepisode  = value.treatmentepisodeid;
												 
												 editAppointId = value.id;
												 editwhopay = value.whopay;
												 editClientName = value.clientname;
												 loc = value.locid;
												 diaryuserId = userid;
												 editcondition_id = value.conditionid;
												 editTreatmentEpisode = value.treatmentepisodeid;
													//document.getElementById('modifyClient2').innerHTML = '<a href="printProfileClient?selectedid='+patientId+'" target="blank">'+editClientName+'</a> (<a href = "ClientLog?id='+patientId+'" target="blank"> Log </a>)';
												 document.getElementById('modifyClient2').innerHTML = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4"><a href="#" class="leftproimage" onclick="openClientPrintPopup('+patientId+')"><img src="popicons/avatar2.png" style="width: 100% !important;padding: 5px; "/><p style="color:#fff !important; font-size: 12px !important;padding: 0px 0px !important;margin-top: 6px !important;">'+editClientName+'</p></a><a href="#" class="belowimgiconset" title="Edit Client Record" onclick="openEditClientPrintPopup('+patientId+')"><img src="popicons/edit.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Edit</p></a> <a href = "#" class="belowimgiconset" title="Log" onclick="openClientLogPopup('+patientId+')"><img src="popicons/log.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Log</p></a>  <a href = "#" class="belowimgiconset" title="EMR" onclick="redircttoNewEmr('+patientId+','+diaryuserId+','+editcondition_id+')"><img src="popicons/emr.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">EMR</p></a>  <a href = "#" class="belowimgiconset" title="Assessment Form" onclick="openAssesmentFormPopup('+patientId+','+diaryuserId+','+editAppointId+')"><img src="popicons/asmnt.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Forms</p></a></div>';
													
													
												 //jAlert('info', 'Invoice has been created for this appointment', 'Info Dialog');
												 $( "#recordpaymentpopup" ).modal( "show" );
											 }
											 
										
										 }
									 }

									 else{

										 document.getElementById('new'+random).onclick = function() {
											 
											 $( "#appointment" ).modal( "hide" );
											 
										 }
									 }
									
                          			
									
                          
                       		
                          	//write text 
								var stext = parseInt(stemp[1]);
                          		var etext = parseInt(etemp[1]);
                          		
                          		
                          		
                          		var showtext =  "";
                          		var otconfirmed = "";
                          		
                          		
								if(value.otid>0){
									value.clientname = value.clientname + ' ' + '(OT in '+value.otname+') '
								}
								if(value.otmsg==1){
									otconfirmed = "<span title='OT Confirmed By "+value.otaccname+"' style='float:left;padding-left: 5px;margin-top: 0px;'>OT</span>";
								}
                          		
                          		if(value.whopay=='Client'){
                          			if(value.notes!=""){
                          				if(value.bal>0){
                          					showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<span class='selbal' title='Balance="+currencySign+value.bal+"'>"+currencySign+"</span><br> "  + " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+" "+ '<img src="common/images/note.ico" title="'+value.notes+'" "> ';
                          				}else{
                          					showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")'style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<br> "  + " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+" "+ '<img src="common/images/note.ico" title="'+value.notes+'" ">';
                          				}
                          				
                          			}else{
                          				if(value.bal>0){
                          					showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<span class='selbal' title='Balance="+currencySign+value.bal+"'>"+currencySign+"</span><br> "  + " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+"<br>";
                          				}else{
                          					showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<br> "  + " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+"<br>";
                          				}
                          				
                          			}
                          			
                          		}else{
                          			if(value.notes!=""){
                          				showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<br> " +  " "+value.usedsession+" " + ' '+ " "+value.apmttypetext+"" + '<img src="common/images/note.ico" title="'+value.notes+'" ">';
                          			}else{
                          				showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-heartbeat' aria-hidden='true'></i> ("+value.token+")</span> &nbsp; <span title='Request Prescription' onclick='showopdpriscription()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-medkit' aria-hidden='true'></i></span> &nbsp; <span title='Request Investigation' onclick='showopdInvestigation()' style='float:left;padding-left: 5px;margin-top: 0px;'><i class='fa fa-info-circle' aria-hidden='true'></i></span>" +otconfirmed + value.clientname + "<br> " +  " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+"<br> ";
                          			}
                          		}
                          		
                          		
                          		if(value.isreportsent==1){
                          			showtext = "<img src='common/images/r-icon.png' style='width:13px;' title='Report Sent'>" + " " + showtext;
                          		}
                          	
                          			
                          			//if no notes
                          			if(value.status == 0){
    									//document.getElementById(etext+'min'+tempStdiff).innerHTML = '<img src="src="common/images/nIcon.png">';
    									if(value.arrivedstatus == 1){
    										document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:#398C10">' +  showtext  +  '</div>';
    									}else if(value.arrivedstatus == 2){
    										document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:#FFFFFF">' +  showtext  +  '</div>';
    									}else{
    										
    										if(value.firstapmt==1){
    											document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:black"> <img style="width:12px; height:11px; padding-right: 2px" src="common/images/nIcon.png">' +  showtext  +  '</div>';
    										}else{
    										
    											document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:black">' +  showtext  +  '</div>';
    										}
    										
    									}
    									
    								}else{
    									if(value.notes!=""){
    										document.getElementById(stext+'min'+tempStdiff).innerHTML =  '<div class="green" style="color:black">' + value.reasonforblock + ","+value.starttime+"-" + ""+value.endtime+" " + '<img align="" src="common/images/note.ico" title="'+value.notes+'" ">'+ '</div>';
    									}else{
    										document.getElementById(stext+'min'+tempStdiff).innerHTML =  '<div class="green" style="color:black">' + value.reasonforblock + ","+value.starttime+"-" + ""+value.endtime+" " +  '</div>';
    									}
    									
    									//document.getElementById(stext+'min'+tempStdiff).title = value.notes + "," + "["+value.apmttype+"]";
    								}
    								
    								if(value.status==0 && value.dna==true){
    									showtext = "<span title='EMR' onclick='redircttoNewEmr("+value.clientId+","+userid+","+value.conditionid+")'>(E)</span> " + value.clientname + "<br> "  + " "+value.usedsession+"" + ' '+ " "+value.apmttypetext+"";
    									
    									//document.getElementById(stext+'min'+tempStdiff).style.color = "Red";
    									if(value.dnanotes!=""){
    										document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:black">' +  showtext + '<img  src="common/images/note.ico" title="'+value.dnanotes+'" ">' + '</div>';
    									}else{
    										document.getElementById(stext+'min'+tempStdiff).innerHTML = '<div class="green" style="color:black">' +  showtext +  '</div>';
    									}
    									
    								}
                          		
		                          		
										
								
                        
                          	
                      });        
                     });
			
		}
	});
}




//new opd script
function showpopupfornewopd(){
	
	
	
	document.getElementById('apmtwithz').innerHTML = $("#diaryUsersss option:selected").text();
	
	var ndate = document.getElementById('commencing').value;
	var nduserid = document.getElementById('diaryUsersss').value;
	
	var url = "newopdBookAppointmentAjax?ndate="+ndate+"&nduserid="+nduserid+" ";
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = showpopupfornewopdRequest;
    req.open("GET", url, true); 
              
    req.send(null);

	
}

function showpopupfornewopdRequest(){
	
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			openedb = 'opd';
			isnewopd = 1;
			
			var data = req.responseText;
			var tmp = data.split('~');
			
			var slotid = tmp[0];
			var stime = tmp[1];
			var etime = tmp[2];
			var duration = tmp[3];
			var location = tmp[4];
			var diaryUser = tmp[5];
			
			var title = tmp[7];
			var commencing = tmp[8];
			var diaryUserId = tmp[9];
			
			
			$(this).MessageBox(slotid,stime,etime,duration,location,diaryUser,diaryUserId,title);
			
			document.getElementById('diciplineName').value = location;
			
			$( "#appointment" ).modal( "show" );
			clearFiledCommonAction();

			
			document.getElementById('client').value = "";
			document.getElementById('notes').value = "";
			
			document.getElementById('apmtType').value = 0;
			$("#apmtType").trigger("chosen:updated");
			$(".chosen").chosen({allow_single_deselect: true});
			
			document.getElementById('condition').value = location;
			$("#condition").trigger("chosen:updated");
			$(".chosen").chosen({allow_single_deselect: true});
			
			var tstr = '<select name="treatmentEpisode" id="treatmentEpisode" class="form-control showToolTip chosen"><option value="0">Select Treatment Episode</option></select>'
			document.getElementById('treatmentepisodeajax').innerHTML = tstr;
			$("#treatmentEpisode").trigger("chosen:updated");
			$(".chosen").chosen({allow_single_deselect: true});
			
			
			//$('#appointment').dialog('option', 'title', 'New Appointment');
			editAppointId = 0;
			editCommencing = commencing;
			slotstarttime = stime;
			document.getElementById('date').value = commencing;
			document.getElementById('blockdate').value = commencing;

		//set cookie commencing
		document.cookie = "cookiecommencing=" + commencing;
		document.cookie = "cookiePractitionerId=" + diaryUserId;
			
		}
	}
	
	
}


//new opd display

function showdisplaynewopd(){
	
	var ndate = document.getElementById('commencing').value;
	var nduserid = document.getElementById('diaryUsersss').value;
	
	var url = "newdisplayBookAppointmentAjax?ndate="+ndate+"&nduserid="+nduserid+" ";
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = showdisplaynewopdRequest;
    req.open("GET", url, true); 
              
    req.send(null);

	
}

function showdisplaynewopdRequest(){
	
	if (req.readyState == 4) {
		if (req.status == 200) {
			 $('#example').DataTable().destroy();
			document.getElementById('newopdbodyid').innerHTML = req.responseText;
			bootstrapplugin();
		}
	}
		
}

function goforvdyocall(){
	openDisplayPopup('vdyoBookAppointmentAjax');
}



function shownewdto(apmtid,pid,conid,clientid,indx,popstatus,bodytmplate,bodyeditedtmplate){
	pbodytmplate = bodytmplate;	
	pbodyeditedtmplate = bodyeditedtmplate;
	
//	var opdmodifylog=document.getElementById('opdmodifylogin').value;
//	if(opdmodifylog!='true'){
		document.getElementById('opdmoddivid').style.display = 'none';
//	}
	
	 patientId = clientid;
	 pppid = clientid;
	 pppcname  = document.getElementById('ncname'+indx).value;
	 pppwhopay  = document.getElementById('nwhopay'+indx).value;
	 ppptepisode  = 0;
	 
	 editAppointId = apmtid;
	 editwhopay = document.getElementById('nwhopay'+indx).value;;
	 editClientName = document.getElementById('ncname'+indx).value;
	 loc = 1;
	 diaryuserId = pid;
	 editcondition_id = conid;
	 editTreatmentEpisode = 0
		
	 document.cookie = "cookiecommencing=" + document.getElementById('commencing').value;;
	 document.cookie = "cookiePractitionerId=" + pid;
	 
	 document.cookie = "cookieClientId=" + pppid;
	 document.cookie = "cookieUserName=" + editClientName;
	 document.cookie = "cookieSelectedAppointmentid=" + editAppointId;
	 document.cookie = "cookieTreatmentEpidodeSessions=" + 0;
	 document.cookie = "complocationid=" + loc;
	 document.cookie = "cookiePractitionerId=" + pid;
		
		
	 
	if(popstatus==1){
		
		document.getElementById('modifyClient').innerHTML = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4"><a href="#" class="leftproimage" onclick="openClientPrintPopup('+patientId+')"><img src="popicons/avatar2.png" style="width: 100% !important;padding: 5px; "/><p style="color:#fff !important; font-size: 12px !important;padding: 0px 0px !important;margin-top: 6px !important;">'+editClientName+'</p></a><a href="#" class="belowimgiconset" title="Edit Client Record" onclick="openEditClientPrintPopup('+patientId+')"><img src="popicons/edit.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Edit</p></a> <a href = "#" class="belowimgiconset" title="Log" onclick="openClientLogPopup('+patientId+')"><img src="popicons/log.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Log</p></a>  <a href = "#" class="belowimgiconset" title="EMR" onclick="redircttoNewEmr('+patientId+','+diaryuserId+','+editcondition_id+')"><img src="popicons/emr.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">EMR</p></a>  <a href = "#" class="belowimgiconset" title="Assessment Form" onclick="openAssesmentFormPopup('+patientId+','+diaryuserId+','+editAppointId+')"><img src="popicons/asmnt.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Forms</p></a></div>';
		$( "#modifyPopup" ).modal( "show" );
	}
  if(popstatus==2){
		 	
		 document.getElementById('modifyClient1').innerHTML = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4"><a href="#" class="leftproimage" onclick="openClientPrintPopup('+patientId+')"><img src="popicons/avatar2.png" style="width: 100% !important;padding: 5px; "/><p style="color:#fff !important; font-size: 12px !important;padding: 0px 0px !important;margin-top: 6px !important;">'+editClientName+'</p></a><a href="#" class="belowimgiconset" title="Edit Client Record" onclick="openEditClientPrintPopup('+patientId+')"><img src="popicons/edit.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Edit</p></a> <a href = "#" class="belowimgiconset" title="Log" onclick="openClientLogPopup('+patientId+')"><img src="popicons/log.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Log</p></a>  <a href = "#" class="belowimgiconset" title="EMR" onclick="redircttoNewEmr('+patientId+','+diaryuserId+','+editcondition_id+')"><img src="popicons/emr.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">EMR</p></a>  <a href = "#" class="belowimgiconset" title="Assessment Form" onclick="openAssesmentFormPopup('+patientId+','+diaryuserId+','+editAppointId+')"><img src="popicons/asmnt.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Forms</p></a></div>';
			$( "#completeActionPopup" ).modal( "show" );
		}
  if(popstatus==3){
		 document.getElementById('modifyClient2').innerHTML = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4"><a href="#" class="leftproimage" onclick="openClientPrintPopup('+patientId+')"><img src="popicons/avatar2.png" style="width: 100% !important;padding: 5px; "/><p style="color:#fff !important; font-size: 12px !important;padding: 0px 0px !important;margin-top: 6px !important;">'+editClientName+'</p></a><a href="#" class="belowimgiconset" title="Edit Client Record" onclick="openEditClientPrintPopup('+patientId+')"><img src="popicons/edit.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Edit</p></a> <a href = "#" class="belowimgiconset" title="Log" onclick="openClientLogPopup('+patientId+')"><img src="popicons/log.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Log</p></a>  <a href = "#" class="belowimgiconset" title="EMR" onclick="redircttoNewEmr('+patientId+','+diaryuserId+','+editcondition_id+')"><img src="popicons/emr.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">EMR</p></a>  <a href = "#" class="belowimgiconset" title="Assessment Form" onclick="openAssesmentFormPopup('+patientId+','+diaryuserId+','+editAppointId+')"><img src="popicons/asmnt.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Forms</p></a></div>';
			$( "#recordpaymentpopup" ).modal( "show" );
		}
  if(popstatus==4){
		 document.getElementById('modifyClient2').innerHTML = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4"><a href="#" class="leftproimage" onclick="openClientPrintPopup('+patientId+')"><img src="popicons/avatar2.png" style="width: 100% !important;padding: 5px; "/><p style="color:#fff !important; font-size: 12px !important;padding: 0px 0px !important;margin-top: 6px !important;">'+editClientName+'</p></a><a href="#" class="belowimgiconset" title="Edit Client Record" onclick="openEditClientPrintPopup('+patientId+')"><img src="popicons/edit.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Edit</p></a> <a href = "#" class="belowimgiconset" title="Log" onclick="openClientLogPopup('+patientId+')"><img src="popicons/log.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Log</p></a>  <a href = "#" class="belowimgiconset" title="EMR" onclick="redircttoNewEmr('+patientId+','+diaryuserId+','+editcondition_id+')"><img src="popicons/emr.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">EMR</p></a>  <a href = "#" class="belowimgiconset" title="Assessment Form" onclick="openAssesmentFormPopup('+patientId+','+diaryuserId+','+editAppointId+')"><img src="popicons/asmnt.png" class="setsizeimg"/><p style="color: rgb(61, 61, 61) !important; font-size: 11px !important;">Forms</p></a></div>';
		 $('#modifydnapopup').modal( "show" );
		}
	
  setHISBMIData(clientid,indx);  
	
}

	
function opentoipd(){
	checkPatientadmittedNew(patientId);
}

function checkPatientadmittedNew(clientid){
	
	//var url = "admittedIpd?clientid="+clientid+"";
	var url = "admittedIpdAjax?clientid="+clientid+"";
	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = checkPatientadmittedNewRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null)
	
}


function checkPatientadmittedNewRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var bedid = req.responseText;
			if(bedid==0){
				 document.getElementById("toipd").style.display = "block";
			}else{
				jAlert('error', 'Patient Already admitted.', 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}
	}
}
	




