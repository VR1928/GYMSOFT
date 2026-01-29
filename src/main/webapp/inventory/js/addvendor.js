var cell;
var row;
var index=0;


function addVendor(tableID) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	row=table.insertRow(rowCount);
	var counts = rowCount - 1;	
    
    var data="<tr><td></td><td><input type='text' id='name'></td><td><input type='text' id='address'></td><td><input type='text' id='email'/></td><td><input type='text' id='phone1'></td><td><input type='text' id='mobile_pri'></td><td><input type='button' class='btn btn-primary' value='Add Product' disabled></td> <td><input type='text' id='min_delivery_time'></td><td><a href='#' onclick='addvendors()' class='form-control'><i class='fa fa-save'></i></a></td><td><a href='#' onclick='reloadthis()' class='form-control'><i class='fa fa-times'></i></a></td></form></tr>"; 
    index++;  
    row.innerHTML=data;     
    
}


function addvendors() {

   var flag=false;
   var name=document.getElementById("name").value; 
   var address=document.getElementById("address").value;
   var email=document.getElementById("email").value;
   var phone1=document.getElementById("phone1").value;
   var mobile_pri=document.getElementById("mobile_pri").value;
   var min_delivery_time=document.getElementById("min_delivery_time").value;

    if(name.length<1){
            jAlert('error', 'Please Entet Name', 'Error Dialog');
            
    } else if(address.length<1) {
    
         jAlert('error', 'Please Enter Address', 'Error Dialog');
    } else if(email.length<5) {
         jAlert('error', 'Please Enter Valid Email', 'Error Dialog');
    } else if(phone1.length<5) {
         jAlert('error', 'Please Enter Valid Phone', 'Error Dialog');
    } else if(mobile_pri.length<10) {
         jAlert('error', 'Please Enter Valid Mobile', 'Error Dialog');
    } else if(min_delivery_time.length<1) {
         jAlert('error', 'Please Enter Delivery Time', 'Error Dialog');
    } else 
    {
   		document.myvenform.name.value=name;
   		document.myvenform.address.value=address;
   		document.myvenform.email.value=email;
   		document.myvenform.phone1.value=phone1;
   		document.myvenform.mobile_pri.value=mobile_pri;
   		document.myvenform.min_delivery_time.value=min_delivery_time;
   
   		document.myvenform.action='saveajaxvendorInventory';
   		document.myvenform.submit();
   }
}

var tempid=0;

function editvendor(id) {
	$('#dashboardloaderPopup').modal( "show" );
	tempid=id;	
	  document.getElementById("grnnoedit").value=id;
      var url="editvendorajaxInventory?id="+id+"";
   
      if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = editvendorRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
}

function editvendorRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
		
		       var str=req.responseText;
		       var data=str.split("~");
		       document.getElementById("tinno").value=data[0];
		       document.getElementById("name").value=data[1];
		       document.getElementById("email").value=data[2];
		       document.getElementById("phone").value=data[3];
		       document.getElementById("min_delivery").value=data[4];
		       document.getElementById("address").value=data[5]; 
		       document.getElementById("state").value=data[6];
		       document.getElementById("city").value=data[7];
		       document.getElementById("postcode").value=data[8];
		       document.getElementById("id").value=tempid;	  
		       document.getElementById("allprod").innerHTML= data[9]; 
		       document.getElementById("creditdays").value= data[10]; 
		       //lokesh
		       document.getElementById("bankname").value= data[11]; 
		       document.getElementById("bankbranch").value= data[12]; 
		       document.getElementById("ifsccode").value= data[13]; 
		       document.getElementById("accountno").value= data[14]; 
		       document.getElementById("drug").value= data[15]; 
		        $("#state").trigger("chosen:updated");
			    $(".chosen").chosen({allow_single_deselect: true});
		         $("#city").trigger("chosen:updated");
			    $(".chosen").chosen({allow_single_deselect: true});
			     $("#min_delivery").trigger("chosen:updated");
			    $(".chosen").chosen({allow_single_deselect: true});
		       
			    $('#dashboardloaderPopup').modal( "hide" );
		       
			   $('#addsupplier').modal( "show" );		 
         }
	}	 
}

function reloadthis() {

    document.reloadform.action="manageInventory";
    document.reloadform.submit();

}

function updatevendor(id) {

     var name=document.getElementById("name"+id).value;
     var address=document.getElementById("address"+id).value;
     var email=document.getElementById("email"+id).value;
     var phone=document.getElementById("phone"+id).value;
     var mobile=document.getElementById("mobile"+id).value;
     var delivery=document.getElementById("delivery"+id).value;

     var url="updatevendorajaxInventory?id="+id+"&name="+name+"&address="+address+"&email="+email+"&phone="+phone+"&mobile="+mobile+"&delivery="+delivery+"";
     
      if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = updatevendorRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
 
}


function updatevendorRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
		   
		    document.location.reload();
         }
	}	 
}


function confirmDelete() {
 
      var t=confirm("Are you sure to Delete?");
      if(t==true){
         return true;
      } else {
         return false;
      }
  
 }		

 function getCities(val) {
     var url="getcitiesInventory?state="+val+"";
  
     if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
               
     req.onreadystatechange = getCitiesRequest;
     req.open("GET", url, true); 
              
     req.send(null);   
 
 }
 
 function getCitiesRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
		   
		   document.getElementById("citydiv").innerHTML=req.responseText;
         }
	}	 
}


	function isvalidVendor(){
	
	     var tinno =document.getElementById("tinno").value;
	     var name=document.getElementById("name").value;
	 	 var email =document.getElementById("email").value;
	 	 var phone= document.getElementById("phone").value;
	 	 var address= document.getElementById("address").value;
	 	 var state= document.getElementById("state").value;
	 	 var city= document.getElementById("city").value;
	 	 var postcode= document.getElementById("postcode").value; 
	     var id=document.getElementById("grnnoedit").value; 
	     var druglic=document.getElementById("drug").value; 
	
	     if(name==''){
	        
	          jAlert('error', "please enter supplier name!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
					return false;
	     }
	     /*else if(email==''){
	           jAlert('error', "please enter email!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	     return false;
	     } */
	     
	    /*  else if(phone==''){
	           jAlert('error', "please enter phone!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	     return false;
	     } */
	      if(address==''){
	           jAlert('error', "please enter address!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	       return false;
	     }
	     if(tinno==''){
		     
	          	jAlert('error', "please enter GST No.!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
					return false;
	     }
	      else if(druglic==''){
	           jAlert('error', "please enter Drug License!", 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
      return false;
	      }
	     
	     /* else if(state=='0'){
	      
	      		jAlert('error', "please select state!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);    
	     } else if(city=='0'){
	      
	      		jAlert('error', "please select city!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);    
					return false;
	     } else if(postcode==''){
	      
	      		jAlert('error', "please enter post code!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);    
					return false;
	     }*/ 
	     else {
	    	 	
	           var id=Number(document.getElementById("id").value);
	           
				             var product=0;
				     $('.case').each(function() { // loop through each checkbox
						
						 if(this.checked == true ){
						    	product=product+","+this.value;
						 }	
						
					});
	
					if(product==0){
					   
					     	jAlert('error', "please select at least one product!", 'Error Dialog');
							
								setTimeout(function() {
									$("#popup_container").remove();
									removeAlertCss();
								}, alertmsgduration);    
							    return false;
					    
					}else {
								 document.getElementById("productlist").value=product;
						           
						            if(id==0){
//	           	  						document.getElementById("saveform").submit();
	           	  					checkgstno(tinno);
	           						} else {
	           							checkgstnoforedit(tinno,id);
	           
//	                					document.getElementById("saveform").action="updatevendorInventory";
//	           							document.getElementById("saveform").submit();
	           						}
						           
			  }
			  
			  					 if(id==0){
			  						 checkgstno(tinno);
	           	  				 
	           						} else {
	           
	           							checkgstnoforedit(tinno,id);
//	                					document.getElementById("saveform").action="updatevendorInventory";
//	           							document.getElementById("saveform").submit();
	           						}
			  
	     }
	
	}
	function checkgstno(tinno) {
    var url="checkgstnoInventory?gstno="+tinno+"";
		  
	     if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		 }
		 else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		 }   
	               
	     req.onreadystatechange = checkgstnoRequest;
	     req.open("GET", url, true); 
	              
	     req.send(null); 	           	  				
	     }
	function checkgstnoRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
					if(req.responseText==1){
						jAlert('error', "GST NO Already exist!", 'Error Dialog');
						
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 
					}else{
						document.getElementById("savevend").style.visibility="hidden";
							document.getElementById("saveform").submit();
					}
					
				  
		         }
			}	 
		}

	function selectAll(obj){
	 
	     if(obj.checked==true){
	      		 $('.case').each(function() { // loop through each checkbox
			          this.checked=true;
				 });	
		 }
	      else {
	     	  $('.case').each(function() { // loop through each checkbox
			          this.checked=false;
				 });	
				
	     }
	     
	}


	function isValid(){
	}

	function checkgstnoforedit(tinno,id) {
	    var url="checkgstnoforeditInventory?gstno="+tinno+"&id="+id+"";
			  
		     if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			 }
			 else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			 }   
		               
		     req.onreadystatechange = checkgstnoforeditRequest;
		     req.open("GET", url, true); 
		              
		     req.send(null); 	           	  				
		     }
		function checkgstnoforeditRequest(){
			if (req.readyState == 4) {
					if (req.status == 200) {
						if(req.responseText==1){
							jAlert('error', "GST NO Already exist Use another GST No!", 'Error Dialog');
							
							setTimeout(function() {
								$("#popup_container").remove();
								removeAlertCss();
							}, alertmsgduration); 
						}else{
							document.getElementById("savevend").style.visibility="hidden";
							document.getElementById("saveform").action="updatevendorInventory";
							document.getElementById("saveform").submit();
						}
						
					  
			         }
				}	 
			}
		
		
		
		
		function getStateAjaxnew(val) {
			if(val!=0){
			var val=document.getElementById("city").options[document.getElementById('city').selectedIndex].text;
			}else{
				val=0;
			}
		     var url="getstateajaxtInventory?city="+val+"";
		  
		     if (window.XMLHttpRequest) {
		  req = new XMLHttpRequest();
		  }
		  else if (window.ActiveXObject) {
		  isIE = true;
		  req = new ActiveXObject("Microsoft.XMLHTTP");
		  }   
		               
		     req.onreadystatechange = getStateAjaxnewRequest;
		     req.open("GET", url, true); 
		              
		     req.send(null);   
		 
		 }
		 
		 function getStateAjaxnewRequest(){
		    if (req.readyState == 4) {
		  if (req.status == 200) {
		     document.getElementById("statediv").innerHTML=req.responseText;
		         }
		 }  
		}