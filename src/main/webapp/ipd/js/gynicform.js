/**
 * 
 */

var visit_reason_ids="0";
var selected="0";

function openVisitPopup(d){
	
	   document.getElementById("reason").value=''; 
	   document.getElementById("quality").value='';
	   document.getElementById("periodicity").value='';
	   document.getElementById("influence").value='';
	   document.getElementById("since").value='';
	   document.getElementById("days").value='';
	   document.getElementById("notes").value='';	
	
	 document.getElementById('reasonvisit').value =d;
	 $('#rvisit').modal( "show" );
	
}


function addReasonForVisit(){
	   
	    $('#dashboardloaderPopup').modal( "show" );
	    var reasonvisit= document.getElementById("reasonvisit").value;
	    var clientid= document.getElementById("clientid").value;
	    var reason= document.getElementById("reason").value; 
	    var quality= document.getElementById("quality").value;
	    var periodicity =document.getElementById("periodicity").value;
	    var influence= document.getElementById("influence").value;
	    var si= document.getElementById("since").value;
	    var days= document.getElementById("days").value;
	    var since =si+" "+days;
	    var notes= document.getElementById("notes").value;
	    
	    var url="savereasonforvisitobjIpd?clientid="+clientid+"&reason="+reason+"&quality="+quality+"&periodicity="+periodicity+"&influence="+influence+"&since="+since+"&notes="+notes+"&reasonvisit="+reasonvisit+"";
	    if (window.XMLHttpRequest) {
	    	req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = addReasonForVisitRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	
}

function addReasonForVisitRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
						          
				    var str =req.responseText;
				    var data= str.split("~");
				    visit_reason_ids=document.getElementById("visit_reason_ids").value;
				    if(visit_reason_ids=='' || visit_reason_ids==' '){
				    	visit_reason_ids='0';
				    }
				    visit_reason_ids =visit_reason_ids+","+data[1];
					document.getElementById("scrolltable").innerHTML=data[0];
					document.getElementById("visit_reason_ids").value= visit_reason_ids;
					$('#rvisit').modal( "hide" );
					$('#dashboardloaderPopup').modal( "hide" );
					
			}
		}	 
	}


    function calEdd(lmp){
    	
    	calTT1(lmp);
    	calTT2(lmp);
    	calinfluenza_vaccine(lmp);
    	
    	var datestr= lmp.split("-");
		var day= Number(datestr[0]);
		var month= Number(datestr[1])-1;
		var year= Number(datestr[2]);
		
        var date = new Date(year,month,day); ////Remember, months are 0 based in JS
        var newdate = new Date();
        newdate.setDate(date.getDate() + 280);
        
        var dd = newdate.getDate();
        var mm = newdate.getMonth() + 1;
        var y = newdate.getFullYear();
        
        if(dd<10){
            dd='0'+dd;
        } 
        if(mm<10){
            mm='0'+mm;
        } 

        var someFormattedDate = dd + '-' + mm + '-' + y;
        document.getElementById('edd').value = someFormattedDate;
    }
    
function calTT1(lmp){
    	
    	var datestr= lmp.split("-");
		var day= Number(datestr[0]);
		var month= Number(datestr[1])-1;
		var year= Number(datestr[2]);
		
        var date = new Date(year,month,day); ////Remember, months are 0 based in JS
        var newdate = new Date();
        newdate.setDate(date.getDate() + 140);
        
        var dd = newdate.getDate();
        var mm = newdate.getMonth() + 1;
        var y = newdate.getFullYear();
        
        if(dd<10){
            dd='0'+dd;
        } 
        if(mm<10){
            mm='0'+mm;
        } 

        var someFormattedDate = dd + '-' + mm + '-' + y;
        document.getElementById('tt_dose1').value = someFormattedDate;
    }
    
function calTT2(lmp){
	
	var datestr= lmp.split("-");
	var day= Number(datestr[0]);
	var month= Number(datestr[1])-1;
	var year= Number(datestr[2]);
	
    var date = new Date(year,month,day); ////Remember, months are 0 based in JS
    var newdate = new Date();
    newdate.setDate(date.getDate() + 168);
    
    var dd = newdate.getDate();
    var mm = newdate.getMonth() + 1;
    var y = newdate.getFullYear();
    
    if(dd<10){
        dd='0'+dd;
    } 
    if(mm<10){
        mm='0'+mm;
    } 

    var someFormattedDate = dd + '-' + mm + '-' + y;
    document.getElementById('tt_dose2').value = someFormattedDate;
}

function calinfluenza_vaccine(lmp){
	
	var datestr= lmp.split("-");
	var day= Number(datestr[0]);
	var month= Number(datestr[1])-1;
	var year= Number(datestr[2]);
	
    var date = new Date(year,month,day); ////Remember, months are 0 based in JS
    var newdate = new Date();
    newdate.setDate(date.getDate() + 196);
    
    var dd = newdate.getDate();
    var mm = newdate.getMonth() + 1;
    var y = newdate.getFullYear();
    
    if(dd<10){
        dd='0'+dd;
    } 
    if(mm<10){
        mm='0'+mm;
    } 

    var someFormattedDate = dd + '-' + mm + '-' + y;
    document.getElementById('influenza_vaccine').value = someFormattedDate;
}


function switchgynicfomr(d){
	
	 if(d=='1'){
		 	//obs
		  document.getElementById("formtype").value="1";
	 } else if(d=='2'){
		 	//gynic
		 document.getElementById("formtype").value="2";
	 } else {
		 	//infertility
		 document.getElementById("formtype").value="3";
	 }
	 
	 document.getElementById("ipdadmissionfrm").action="gynicassesmentformIpd";
	 document.getElementById("ipdadmissionfrm").submit();
	
}


function validdata(){
	
	    var diagosis=0;
	    $('.classD').each(function() { 
			if(this.checked == true){
				diagosis=diagosis+","+this.value;
			}
		});
	    
	    document.getElementById("finaldiagnosis").value=diagosis;
	    document.getElementById("ipdadmissionfrm").submit();
      	    
}



