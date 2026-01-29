

 function getcharges(val) {
			
			var flag=false;
			var wardid=document.getElementById("wardid").value;
			var tpid=document.getElementById("tpid").value;
			var payee=document.getElementById("payee").value;
			
			if(payee=="Select Payee"){
			     jAlert('error', "please select payee!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			    flag=true;
			} /*else if(payee=="0" && wardid=="0"){
			
				jAlert('error', "please select ward!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			    flag=true;    
			
			}*/ else if(payee=="1" && tpid=="0"){
			
			     jAlert('error', "please select third party!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			    flag=true;
			}
			
			
			if(flag==false && val=='INVESTIGATION'){
				
				document.getElementById("sinv").className="form-group";
				return false;
			} else {
				document.getElementById("sinv").className="form-group hidden";
			    
			}
			
			
			
			
			
			 if(flag==false){
			 
					var url = "getchargesThirdParty?chargetype="+val+"&tpid="+tpid+"&wardid="+wardid+"";
				
				if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			    req.onreadystatechange = getchargesRequest;
			    req.open("GET", url, true); 
			    
			    req.send(null);   		 
			 
			 }
		
  }
		
  function getchargesRequest() {
  
		  if (req.readyState == 4) {
				if(req.status == 200) {					
						
						document.getElementById("scroll").innerHTML=req.responseText;
						
			 }
		 }
  }
  
  
  
  function getinvestigationcharges(invtype){
  
               var wardid=document.getElementById("wardid").value;
			   var tpid=document.getElementById("tpid").value;
  
  		       var url = "getinvnamesThirdParty?invstype="+invtype+"&tpid="+tpid+"&wardid="+wardid+"";
				
				if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			    req.onreadystatechange = getinvestigationchargesRequest;
			    req.open("GET", url, true); 
			    
			    req.send(null);   		 	
			      
       
      
  }
  function getinvestigationchargesRequest() {
			
		  if (req.readyState == 4) {
				if(req.status == 200) {					
						
						document.getElementById("scroll").innerHTML=req.responseText;
						
			 }
		 }
  }
  
  
  
  
  
  function saveAllCharges(){

	var tempcharges="0";
	var flag=false;
	var index=0;
	var tflag=false;
	
	var payee=document.getElementById("payee").value;
	var wardid=document.getElementById("wardid").value;
	var chargeType=document.getElementById("chargeType").value;
	var invstype=document.getElementById("invstype").value;
	
	if(payee=='Select Payee'){
	     tflag=true;
	      jAlert('error', "please select payee!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	}/*else if(payee==0 && wardid==0){
	
	     tflag=true;
	      jAlert('error', "please select Ward!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	
	}*/ else if(chargeType=="0"){
	
			 tflag=true;
	      jAlert('error', "please Select Charge Type!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	}else if(chargeType=="5" && invstype=="0"){
	
			 tflag=true;
	      jAlert('error', "please Select Investigation Type!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	}
	
	
	
	if(tflag==false){
	
	    	 $('.case').each(function() { // loop through each checkbox
			if (this.checked == true) {
			
			 var d=this.value;
			 
			 tempcharges=tempcharges+","+d;
			 var mrp=document.getElementById("mrp"+d).value;
			 var code=document.getElementById("code"+d).value;
			 var standardcharge= document.getElementById("standardcharge"+d).value;
			 var hour= document.getElementById("hour"+d).value;
			 var ratio= document.getElementById("ratio"+d).value; 
		 // 	var standardcharge = 0;
	
			 if(mrp==''){
			 
			     jAlert('error', "please enter mrp!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			    flag=true;
			 }
			 else if(standardcharge=='1' && wardid=="0") {
			 	 
			 	   jAlert('error', "please select ward for standard charge!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			     flag=true;
			 }
			 
			 /* else if(code=='') {
			 	 
			 	   jAlert('error', "please enter code!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			     flag=true;
			 }*/
		
		   index++;	 				
		}
	});	
	
  }
	
	
	
     
   
  
   document.getElementById("notes").value=tempcharges;
  
  	if(tflag==false){
  	
  	 		if(index>0){
  	
	  	      if(flag==false){
	  				 
	  				 document.getElementById("tpchargeform").submit();     
	  	      } 
  	       
	  	} else {
	  	
	  	     jAlert('error', "please select at least one charge!", 'Error Dialog');
					
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration);
	  	} 
	  
  	}
  
  }
  
  
  
  
  function settpchanges(payee){
  
       if(payee=="1"){
           
         	document.getElementById("hstp").className="";
         	document.getElementById("dstp").className="";  
           
       } else {
       
        	document.getElementById("hstp").className="hidden";
         	document.getElementById("dstp").className="hidden";  
         
       }
       
       if(payee=="0"){
    	   document.getElementById("perdiv").className="form-group";
       }else{
    	   document.getElementById("perdiv").className="form-group hidden"; 
       }
  }
  
  
  function setOnOff(obj,indx) {
  
        if(obj.checked==true){
        
             document.getElementById("standardcharge"+indx).value=1;
             document.getElementById("ch2"+indx).disabled='';
         
        } else {
        	 document.getElementById("standardcharge"+indx).value=0;
        	 document.getElementById("ch2"+indx).disabled='disabled';
        }
  
  }
  
  function setOnVal(obj,indx) {
  
        if(obj.checked==true){
        		
            	document.getElementById("onoff"+indx).value=1;
        }
        else {
        		document.getElementById("onoff"+indx).value=0;
        }
  
  }
  
 function calculateandincrese() {
	 var data = document.getElementById("peramount").value;
	 if(data==''){
		 jAlert('error', "please enter percentage value through which charges increase!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	 }else{
		 $('.case').each(function() { // loop through each checkbox
				if (this.checked == true) {
					var d=this.value;
					var data2 = document.getElementById("mrp"+d).value;
					if(data2==''){
						data2 = 0;
					}
					var val =  data2 * (data/100);
					var summ = Number(val)+Number(data2);
					document.getElementById("mrp"+d).value = summ;
				}
			 });	
	 }
	
}
  
  
  
  