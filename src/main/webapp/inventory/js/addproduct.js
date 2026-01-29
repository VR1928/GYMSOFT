
var tindex=0;
function changeEntry(val){

      
      document.getElementById("tmanual_entry").value=val;
      document.getElementById("manualentryform").submit();     
 

}
var totaltax=0;
var totalrs=0;
function calTax(){
    totaltax=0;
    for(var i=0;i<=tid;i++){
        
          totaltax=totaltax+Number(document.getElementById("tax"+i+"").value);
    }    
    	document.getElementById("totaltax").value=totaltax; 
     calTotal();
}


function calTotal(){
    totalrs=0;
    for(var i=0;i<=tid;i++){
        
          totalrs=totalrs+Number(document.getElementById("mrp"+i+"").value);
    }   
    totalrs=totalrs+totaltax;
    
    document.getElementById("totalrs").innerHTML=totalrs;
 
}





var cell;
var row;
function addproductBarcode(tableId,pid) {
  
    var table = document.getElementById(tableId);
	var rowCount = table.rows.length;
	
	tindex=rowCount-2;
	row=table.insertRow(rowCount);
	var counts = rowCount - 1;
    var url = "getbarcodeproductProduct?rowcount="+tindex+"&productid="+pid+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = addproductBarcodeRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function addproductBarcodeRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
		
		     var str=req.responseText;
		     var data=str.split("~");
	          row.innerHTML=data[0];
	          
	          
	          $("#bqty"+tindex+"").keypress(function (e) {
			     //if the letter is not digit then display error and don't type anything
			     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			        //display error message
			        $("#errmsg").html("Digits Only").show().fadeOut("slow");
			               return false;
			    }
			   });
			   
			   caltotalamt(tindex,data[1]);
			    
			   
	          
         }
	}	 
}


 function caltotalamt(index,mrp){
 
         var d=Number(document.getElementById("totalrs").innerHTML);
         
         d=d+Number(mrp); 
         
         document.getElementById("totalrs").innerHTML=d;
           
 }
 
 function changeqty(){

    var total=0;
   
    for(var i=0;i<=tindex;i++){
       
         var mrptxt=document.getElementById("mrp"+i+"").innerHTML;
         var tmp=mrptxt.split(" ");
         var mrp=Number(tmp[1]);
         var qty=Number(document.getElementById("bqty"+i+"").value);
         var temp=mrp*qty;
         total=total+temp;   
    }
 
    document.getElementById("totalrs").innerHTML=total; 
 }
 




function gettotalamt(id,val,pid){
   
      tindex=id;
      var url = "getsumqtyProduct?qty="+val+"&pid="+pid+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = gettotalamtRequest;
    req.open("GET", url, true); 
              
    req.send(null);
     
}


function gettotalamtRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		      document.getElementById("t"+tindex+"").innerHTML="Rs. "+req.responseText+"";
		      
		      changeqty();
         }
	}	 
}


function savebarodeproc() {

      document.getElementById("barodeproc").submit();

}


function getvendor(val,index){
    tindex=index; 
    var url = "getvendorsProduct?brandid="+val+"&index="+index+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = getvendorRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function getvendorRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		      document.getElementById("listvendor"+tindex+"").innerHTML=req.responseText;
		      
         }
	}	 
}


function getproduct(val,index){
    tindex=index;
     $("#dashboardloaderPopup").modal('show');
    var url = "getvendorproductProduct?vendorid="+val+"&index="+index+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = getproductRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}

function getproductRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		      document.getElementById("listproduct"+tindex+"").innerHTML=req.responseText;
		      $("#product"+tindex).trigger("chosen:updated");
			  $(".chosen").chosen({allow_single_deselect: true});
			  $("#dashboardloaderPopup").modal('hide');
         } 
	}	 
}

var totalcount=1;

function addmoreproduct(tableId) {

	totalcount++;
    var table = document.getElementById(tableId);
    var rowCount = table.rows.length;
	row=table.insertRow(rowCount);
	var counts = rowCount - 1;
	tindex=counts;
	
    var url = "addnewproductProduct?count="+counts+""; 
     if (window.XMLHttpRequest) {
	    	req = new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
	 }   
	 req.onreadystatechange = addmoreproductRequest;
	 req.open("GET", url, true); 
     req.send(null);

}
function addmoreproductRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		     row.innerHTML=req.responseText;
		      
		      if(tindex>0){
		           
		           var vendid=document.getElementById("vendor0").value;
		           document.getElementById("vendor0").readonly='true';
		           document.getElementById("vendor"+tindex).value=vendid;
		           document.getElementById("vendor"+tindex).readonly='true';
		           var products= document.getElementById("product0").innerHTML;
		           document.getElementById("product"+tindex).innerHTML =products;
		           
		      }
		      $("#vendor"+tindex).trigger("chosen:updated");
			  $(".chosen").chosen({allow_single_deselect: true});
		      
         }
	}	 
}



function isProductValid(){

    var flag=true;  
   
    for(var i=0;i<totalcount;i++){
       
         var vendor= document.getElementById("vendor"+i).value;
         var product= document.getElementById("product"+i).value;
         
         if(vendor=="0"){
             flag=false;
             jAlert('error', "Please select supplier!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
		    break;			
         }
         else if(product=="0"){
         		
         		 flag=false;
             jAlert('error', "Please select product!", 'Error Dialog');
				
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
         	 break;	
         }
    }
    
    return flag;
}


function viewSupplierHist(vendorid){

	var fromdate= document.getElementById("fromdate").value;
	var todate= document.getElementById("todate").value;
	
    var url = "viewsupplierHistoryProduct?vendorid="+vendorid+"&fromdate="+fromdate+"&todate="+todate+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = viewSupplierHistRequest;
    req.open("GET", url, true); 
    req.send(null);
     	 
}

function viewSupplierHistRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		     var data=req.responseText;
		     var str=data.split("~");
			 
			 document.getElementById("allTable").innerHTML= str[0];		     
		     document.getElementById("headtitle").innerHTML=str[1];
		     document.getElementById("disptitle").innerHTML=str[1];
			  $("#history").modal('show');
						     
         }
	}	 
}


function deleteRow(tableID) {

        var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		table.deleteRow(rowCount-1);		
        
}


function editProd(pid){

    var url = "editstockProduct?id="+pid+"";
    if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = editProdRequest;
    req.open("GET", url, true); 
    req.send(null);

}

function editProdRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
		     var data=req.responseText;
		     var str=data.split("~");
			 
			 document.getElementById("id").value= str[0];		     
		     document.getElementById("genericname").value=str[1];
		     document.getElementById("product_name").value=str[2];
		     document.getElementById("mrp").value=str[3];
		     document.getElementById("purchase_price").value=str[4];
		     document.getElementById("sale_price").value=str[5];
		     document.getElementById("stock").value=str[6];
		     document.getElementById("gst").value=str[6];
		     document.getElementById("batch_no").value=str[8];
		     document.getElementById("mfg").value=str[9];
		     document.getElementById("shelf").value=str[10];
		     /* $('#medicine_shedule').val(str[11]);
		      $('#medicine_type').val(str[12]);
		     $('#location').val(str[13]);*/
		     document.getElementById("hsnno").value=str[14];
		     document.getElementById("pack").value=str[15];
		     document.getElementById("category_id").value=str[16];
		     document.getElementById("subcategory_id").value=str[17];
		     document.getElementById("medicine_shedule").value=str[18];
		     document.getElementById("description").value=str[19];
		     document.getElementById("minorder").value=str[20];
		     document.getElementById("maxorder").value=str[21];
		     document.getElementById("productcode").value=str[22];
		     
		     
		     if(document.getElementById("isautogenericname")){
		    	 document.getElementById("mfg").className ="";
			     document.getElementById("genericname").className ="";
			    
			     document.getElementById("mfg").className ="form-control chosen";
			     document.getElementById("genericname").className ="form-control chosen";
				
			     $("#genericname").trigger("chosen:updated");
			  	 $(".chosen").chosen({allow_single_deselect: true});
			  	 
			  	 $("#mfg").trigger("chosen:updated");
			  	   $(".chosen").chosen({allow_single_deselect: true});
		     }
		    
		  	 
		  	  
		     
		     $("#editcat").modal('show');
						     
         }
	}	 
}


function confirmdelete() {

    var t=confirm("Do you Want to Delete?");
    if(t==true){
    
       return true;
    } else {
    
       return false;
    }
 
}

function deleteCheck() {

      var ids="0";
      $('.case').each(function() { 
			if(this.checked == true){
			 
			     ids=ids+","+this.value;
			} 
									
		});
		
	 var url="deletecheckCatalogue?data="+ids+"";	
	  if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = deleteCheckRequest;
    req.open("GET", url, true); 
    req.send(null);  	
		
}

function deleteCheckRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	          
	           window.location.reload();
		    
         }
	}	 
}

function addToCart(){
	 var ids="0";
      $('.case').each(function() { 
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
									
		});
		
	 var url="addtocartCatalogue?data="+ids+"";	
	  if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = addToCartRequest;
    req.open("GET", url, true); 
    req.send(null);  	
		
}

function addToCartRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			$('.case').each(function() { 
			if(this.checked == true){
				 this.checked = false;    
			} 
		});
	       document.getElementById("cartdiv").innerHTML=req.responseText;
		 }
	}	 
}


function showCartPopUp(){
	
	var url = "showtransfermedicineCatalogue";

	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showCartPopUpRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function showCartPopUpRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("carttbody").innerHTML = data[0];
				document.getElementById("tcount").value = data[1];
				$('#cart').modal( "show" );	
	         }
		}
	}
	
	
function deleteCartData(i){
	var url = "deleteTempCatalogue?i="+i+"";
	if(window.XMLHttpRequest){
		req = new XMLHttpRequest();
	}
	else if(window.ActiveObject){
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}
	req.onreadystatechange = deleteCartDataRequest;
	req.open("GET",url,true);
	req.send(null);
}
function deleteCartDataRequest(){
	if(req.readyState == 4){
		if(req.status == 200){
		 	document.getElementById("carttbody").innerHTML = req.responseText;
		}
	}
}


function transferProduct(){
	var tcount = document.getElementById("tcount").value ;
	var rowCount = $('#tabletrcount >tbody >tr').length;
	
	var url= "transferproductCatalogue?count="+tcount+"";
	if(rowCount=='0'){
		jAlert('error', "Please select at least one medicine!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	}else{
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = transferProductRequest;
    req.open("GET", url, true); 
    req.send(null);
  }
}
 function transferProductRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {	  
			location.reload();
		}
	}
}




function showTransferPopup(parentid){
	
	var url = "showtransferdmedicineProduct?parentid="+parentid+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showTransferPopupRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function showTransferPopupRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("fromlocation").innerHTML = data[0];
				document.getElementById("issuedate").innerHTML = data[1];
				document.getElementById("tolocation").innerHTML = data[2];
				document.getElementById("tbodyid").innerHTML = data[3];
				document.getElementById("requestdate").innerHTML = data[1];
				document.getElementById("issueno").innerHTML = data[4];
				document.getElementById("hospitaltitlediv").innerHTML = data[5];
				document.getElementById("username").innerHTML = data[6];
				document.getElementById("userdatetime").innerHTML = data[7];
				$('#cart').modal( "show" );	
	         }
		}
	}
	
	function showRequestPopup(parentid){
	var url = "showtransferdmedicineProduct?parentid="+parentid+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showRequestPopupRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function showRequestPopupRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("fromlocation2").innerHTML = data[0];
				document.getElementById("issuedate2").innerHTML = data[1];
				document.getElementById("tolocation2").innerHTML = data[2];
				document.getElementById("tbodyid2").innerHTML = data[3];
				document.getElementById("requestdate2").innerHTML = data[1];
				document.getElementById("issueno2").innerHTML = data[4];
				document.getElementById("hospitaltitlediv2").innerHTML = data[5];
				document.getElementById("username2").innerHTML = data[6];
				document.getElementById("userdatetime2").innerHTML = data[7];
				document.getElementById("parentid22").value = data[4];
				$('#cart2').modal( "show" );	
	         }
		}
	}

function showCartPopUp2(){
	
	var url = "showreqmedicinefrtransferCatalogue";

	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showCartPopUp2Request;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
	function showCartPopUp2Request(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("carttbody").innerHTML = data[0];
				document.getElementById("tcount").value = data[1];
				$('#cart').modal( "show" );	
	         }
		}
	}
	
	function addToCart2(){
	 var ids="0";
      $('.case').each(function() { 
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
									
		});
		
	 var url="addtocart2Catalogue?data="+ids+"";	
	  if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = addToCart2Request;
    req.open("GET", url, true); 
    req.send(null);  	
		
}

function addToCart2Request(){
if (req.readyState == 4) {
		if (req.status == 200) {
			$('.case').each(function() { 
			if(this.checked == true){
				 this.checked = false;    
			} 
									
		});
	           document.getElementById("cartdiv2").innerHTML=req.responseText;
		 }
	}	 
}

function showRequestPopupForAprove(parentid,val){
	var url = "showrequestedmedicinefraproveProduct?parentid="+parentid+"&val="+val+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showRequestPopupForAproveRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function showRequestPopupForAproveRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("fromlocation4").innerHTML = data[0];
				document.getElementById("issuedate4").innerHTML = data[1];
				document.getElementById("tolocation4").innerHTML = data[2];
				document.getElementById("tbodyid4").innerHTML = data[3];
				document.getElementById("requestdate4").innerHTML = data[1];
				document.getElementById("issueno4").innerHTML = data[4];
				document.getElementById("hospitaltitlediv4").innerHTML = data[5];
				document.getElementById("username4").innerHTML = data[6];
				document.getElementById("userdatetime4").innerHTML = data[7];
				document.getElementById("parentid24").value = data[4];
				document.getElementById("buttondiv").innerHTML = data[8];
				$('#cart4').modal( "show" );	
	         }
		}
	}

function showRequestPopupCheckAvaibility(parentid,val){
 var url = "showrequestedmedicineProduct?parentid="+parentid+"&val="+val+"";

 if (window.XMLHttpRequest) {
   req = new XMLHttpRequest();
  }
  else if (window.ActiveXObject) {
   isIE = true;
   req = new ActiveXObject("Microsoft.XMLHTTP");
  }   
                
     req.onreadystatechange = showRequestPopupCheckAvaibilityRequest;
     req.open("GET", url, true); 
               
     req.send(null);

 }
function showRequestPopupCheckAvaibilityRequest(){
 if (req.readyState == 4) {
   if (req.status == 200) {
   var str = req.responseText;
    var data = str.split("~");
    document.getElementById("fromlocation3").innerHTML = data[0];
    document.getElementById("issuedate3").innerHTML = data[1];
    document.getElementById("tolocation3").innerHTML = data[2];
    document.getElementById("tbodyid3").innerHTML = data[3];
    document.getElementById("requestdate3").innerHTML = data[1];
    document.getElementById("issueno3").innerHTML = data[4];
    document.getElementById("hospitaltitlediv3").innerHTML = data[5];
    document.getElementById("username3").innerHTML = data[6];
    document.getElementById("userdatetime3").innerHTML = data[7];
    document.getElementById("parentid23").value = data[4];
    document.getElementById("btndiv").innerHTML = data[8];
    document.getElementById("requestuser3").innerHTML = data[9];
    document.getElementById("notes").value=data[11];
    document.getElementById("indentno3").innerHTML=data[12];
    document.getElementById("receivedbyid").innerHTML=data[13];
    document.getElementById("noteTextBox").innerHTML=data[14];
    if(data[15]=='hideit'){
    	document.getElementById("deletecart3").className='table hidden';
    	document.getElementById("deletetbodyid3").innerHTML='';
    }else{
    	document.getElementById("deletecart3").className='table';
    	document.getElementById("deletetbodyid3").innerHTML=data[15];
    }
    $('#cart3').modal( "show" ); 
    }
  }
 }
function aproveTransferLog(){
 var parentid = document.getElementById("parentid23").value;
 var url = "updateAproveTransferLogPharmacy?parentid="+parentid+"&aprove=1";
  if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();1
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = aproveTransferLogRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function aproveTransferLogRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
			 }
		}
	}
	
	
function rejectTransferLog(){
 var parentid = document.getElementById("parentid23").value;
	var notes= document.getElementById("notes").value;
	if(notes==''){
			jAlert('error', "Please enter note!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 	
	}else{
		var x = confirm("Are You Sure!!");
		if(x){
			$("#dashboardloaderPopup").modal('show');
			var url = "updaterejectTransferLogPharmacy?parentid="+parentid+"&notes="+notes+"&reject=1";
			if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = rejectTransferLogRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);
		}
		
	}
}
function rejectTransferLogRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
			 }
		}
	}
	
	
function showCheckAvailabilityPopUp(parentid){
	var url = "getAllAvailableMedicineProduct?parentid="+parentid+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = showCheckAvailabilityPopUpRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function showCheckAvailabilityPopUpRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var str = req.responseText;
			var data = str.split("~");
			document.getElementById("reqtbody").innerHTML = data[0];
			document.getElementById("parentid5").innerHTML = data[1];
			document.getElementById("hospitaltitlediv5").innerHTML = data[2];
			document.getElementById("checkavailtbody").innerHTML = data[3];
			$('#checkavailabilitymodel').modal( "show" );	
	   }
	}
}



function addToTransferTemp(){
	var parentid = document.getElementById("parentid5").value;
	 var ids="0";
      $('.case').each(function() { 
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
									
		});
		
	var url="addToTransferTempProduct?data="+ids+"&parentid="+parentid+"";	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = addToTransferTempRequest;
    req.open("GET", url, true); 
    req.send(null);  		
}

function addToTransferTempRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				var str = req.responseText;
				var data = str.split("~");
				transferTempory(data[0],data[1]);
		 }
	}	 
}


function transferTempory(prodid,parentid){

	var url = "transferTemporyProduct?prodid="+prodid+"&parentid="+parentid+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = transferTemporyRequest;
	    req.open("GET", url, true); 
	    req.send(null);
	}
function transferTemporyRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("transfertbody").innerHTML = data[0];
				document.getElementById("tcount").value = data[1];
				document.getElementById("reqtbody2").innerHTML = data[2];
				$('#transfermodel').modal( "show" );	
	         }
		}
	}
	
	
function finalaproveTransferLog(parentid){
 	//var parentid = document.getElementById("parentid23").value;
	var url = "updatefinalAproveTransferLogProduct?parentid="+parentid+"";
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();1
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	    req.onreadystatechange = finalaproveTransferLogRequest;
	    req.open("GET", url, true); 
	    req.send(null);
	}

function finalaproveTransferLogRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				window.location.reload();
			 }
		}
	}
	
	
function showPrintRequestMedPopUp(parentid){
	var url = "getAllAvailableMedicineProduct?parentid="+parentid+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = showPrintRequestMedPopUpRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function showPrintRequestMedPopUp(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var str = req.responseText;
			var data = str.split("~");
			document.getElementById("reqtbody").innerHTML = data[0];
			document.getElementById("parentid5").innerHTML = data[1];
			document.getElementById("hospitaltitlediv5").innerHTML = data[2];
			document.getElementById("checkavailtbody").innerHTML = data[3];
			$('#checkavailabilitymodel').modal( "show" );	
	   }
	}
}


function checkOtherLocation(){
	var parentid = document.getElementById("parentid").value;
	var count = document.getElementById("count").value;
	var check1 = document.getElementById("checkbox1").checked;
	var url = "checkOtherLocationProduct?parentid="+parentid+"&count="+count+"&check1="+check1+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = checkOtherLocationRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function checkOtherLocationRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var str = req.responseText;
			var data = str.split("~");
			document.getElementById("otherlocationid").innerHTML = data[0];
			document.getElementById("count").innerHTML = data[1];
	   }
	}
}


function deleteRequetedRow(r) {
    var i = r.parentNode.parentNode.rowIndex;
   	document.getElementById("prodtableid").deleteRow(i);
}



function lastRequestedPopup(parentid,val){
	var url = "lastRequestedPopupProduct?parentid="+parentid+"&val="+val+"";

	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = lastRequestedPopupRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);

	}
function lastRequestedPopupRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				var data = str.split("~");
				document.getElementById("lastfromlocation").innerHTML = data[0];
				document.getElementById("lastrequestdate").innerHTML = data[1];
				document.getElementById("lastreqtbody").innerHTML = data[2];
				document.getElementById("lastissueno").innerHTML = data[3];
				document.getElementById("lastparentid").innerHTML = data[3];
				document.getElementById("lasthospitalnamediv").innerHTML = data[4];
				document.getElementById("lasttbodyid").innerHTML = data[5];
				document.getElementById("lastbuttondiv").innerHTML = data[6];
				document.getElementById("lastrequestuser").innerHTML = data[7];
				document.getElementById("lastindentno").innerHTML = data[8];
				document.getElementById("lasttfoot").innerHTML = data[9];
				$('#lastcart').modal( "show" );	
	         }
		}
	}
	
	
	function getwarehouseCategory(val){
	
	   var url="getcategorywarehouseProduct?id="+val+"";
	        if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = getwarehouseCategoryRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	
	}
	function getwarehouseCategoryRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			       document.getElementById("categorydiv").innerHTML=req.responseText;
			        $("#category_id").trigger("chosen:updated");
			  		$(".chosen").chosen({allow_single_deselect: true});
	         }
		}
	}



function checkNotGreterThanStock(val){
	var reqqty = document.getElementById("reqqty"+val).value;
	var stock = document.getElementById("stock"+val).value;
	if(parseInt(reqqty)>parseInt(stock)){
		jAlert('error', "You are enter requested qty greater than stock!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
		document.getElementById("reqqty"+val).value='0';
	}
}

function addOtherToTempPo(prodid,val,reqstock,catid){
	$("#dashboardloaderPopup").modal('show');
	var parentid = document.getElementById("parentid").value;
	var instock = document.getElementById("stock"+val).value;
	//var reqstock = document.getElementById("reqqty"+val).value;
	var url = "addOtherToTempPoProduct?parentid="+parentid+"&instock="+instock+"&reqstock="+reqstock+"&prodid="+prodid+"&catid="+catid+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = addOtherToTempPoRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function addOtherToTempPoRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			var str = req.responseText;
			var data = str.split("~");
			document.getElementById("potbodyid").innerHTML = data[0];
			$("#dashboardloaderPopup").modal('hide');
			if(data[1]==0){
				 jAlert('error', "Already added to PO list!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
					
			}
	   }
	}
}


function createPoOnTransfer(){

	var x = confirm("Are you sure you want to add to PO?");
	if (x){
		 $("#dashboardloaderPopup").modal('show');
		var parentid = document.getElementById("parentid").value;
		document.getElementById("delivertopoque").className="hidden";
		var url = "createPoOnTransferProduct?parentid="+parentid+"";
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
		req.onreadystatechange = createPoOnTransferRequest;
		req.open("GET", url, true); 
		req.send(null);
	}
}
function createPoOnTransferRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			window.location = 'transferdashboardProduct';
	   }
	}
}


function showRequestMedicine(parentid,val){
	 var url = "showRequestMedicineProduct?parentid="+parentid+"&val="+val+"";

	 if (window.XMLHttpRequest) {
	   req = new XMLHttpRequest();
	  }
	  else if (window.ActiveXObject) {
	   isIE = true;
	   req = new ActiveXObject("Microsoft.XMLHTTP");
	  }   
	                
	     req.onreadystatechange = showRequestMedicineRequest;
	     req.open("GET", url, true); 
	               
	     req.send(null);

	 }
	function showRequestMedicineRequest(){
	 if (req.readyState == 4) {
	   if (req.status == 200) {
	   var str = req.responseText;
	    var data = str.split("~");
	    document.getElementById("infromlocation").innerHTML = data[0];
	    document.getElementById("inissuedate").innerHTML = data[1];
	    document.getElementById("intolocation").innerHTML = data[2];
	    document.getElementById("intbodyid").innerHTML = data[3];
	    document.getElementById("inrequestdate").innerHTML = data[1];
	    document.getElementById("inissueno").innerHTML = data[4];
	    document.getElementById("inhospitaltitlediv").innerHTML = data[5];
	    document.getElementById("inusername").innerHTML = data[6];
	    document.getElementById("inuserdatetime").innerHTML = data[7];
	    document.getElementById("inparentid").value = data[4];
	    document.getElementById("inadminnote").innerHTML=data[8];
	    $('#incart').modal( "show" ); 
	          }
	  }
	 }
	
	
	
	
	function checkOtherLocationAfterPo(){
		var parentid = document.getElementById("parentid").value;
		var count = document.getElementById("count").value;
		var check1 = document.getElementById("checkbox1").checked;
		var url = "checkOtherLocationAfterPOProduct?parentid="+parentid+"&count="+count+"&check1="+check1+"";
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
		req.onreadystatechange = checkOtherLocationAfterPORequest;
		req.open("GET", url, true); 
		req.send(null);
	}
	function checkOtherLocationAfterPORequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				var str = req.responseText;
				var data = str.split("~");
				document.getElementById("otherlocationid").innerHTML = data[0];
				document.getElementById("count").innerHTML = data[1];
		   }
		}
	}
	
	function checkNotGreterThanStock2(val){
		var reqqty = document.getElementById("tqty"+val).value;
		var stock = document.getElementById("stock"+val).value;
		if(parseInt(reqqty)>parseInt(stock)){
			jAlert('error', "Transfer can't be processed as requested qty not in stock!", 'Error Dialog');	
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 
			document.getElementById("tqty"+val).value='0';
		}
	}
	
	function deleteCartProductTemp(r){
		var i = parseInt(r)+1;
    	 //var i = r.parentNode.parentNode.rowIndex;
    	document.getElementById("tabletrcount").deleteRow(i);
    	var total=Number(document.getElementById("tcount").value);
    	total--; 
		document.getElementById("tcount").value=total;	 
}
	
	function deleteCartProductTemp2(r,mid){
		var i = parseInt(r)+1;
   	 	//var i = r.parentNode.parentNode.rowIndex;
		document.getElementById("tabletrcount").deleteRow(i);
		var total=Number(document.getElementById("tcount").value);
		total--; 
		document.getElementById("tcount").value=total;
		
		var url="deleteCartProductTempCatalogue?data="+mid+"";	
		  if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = deleteCartProductTemp2Request;
	    req.open("GET", url, true); 
	    req.send(null);  	
			
	}

	function deleteCartProductTemp2Request(){
		if (req.readyState == 4) {
			
			
		}	 
	}
	
	
	function getsubCategories(id) {
		
	      var url="getsubcategoriesProcurement?id="+id+"";
	     
	      if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		 }
		 else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		 }   
	               
	     req.onreadystatechange = getsubCategoriesRequest;
	     req.open("GET", url, true); 
	              
	     req.send(null);   
		
	}
	function getsubCategoriesRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				 
				   document.getElementById("subdiv").innerHTML=req.responseText;
		           $("#subcategory_id").trigger("chosen:updated");
				   $(".chosen").chosen({allow_single_deselect: true});   
				
		   }
		}
	}

	function roundTwo(val){
		 
		   val =Math.round(val * 100) / 100;
		   //val=Math.floor(val);
		   return val;
		}

	
	function calSalePerUnitCatalogue(){
		
		  var pack= Number(document.getElementById("pack").value);
		  var mrp= Number(document.getElementById("mrp").value);
		  
		  var sale_price= parseFloat(mrp/pack);
	      sale_price =  roundTwo(sale_price);	  
	      document.getElementById("sale_price").value= sale_price;
	}
	
	function submitForm(){
	
		
		var categoryid= document.getElementById("category_id").value;
		var subcategoryid= document.getElementById("subcategory_id").value;
		var prodname= document.getElementById("product_name").value;
		var mrp= document.getElementById("mrp").value;
		var purchase_price= document.getElementById("purchase_price").value;
		var sale_price= document.getElementById("sale_price").value;
		var pack= document.getElementById("pack").value;
		var comment=document.getElementById("comment").value;
		var hsnno = document.getElementById("hsnno").value;
		var gst = document.getElementById("gst").value;
		var prodtype = document.getElementById("medicine_shedule").value;
		var mfg = document.getElementById("mfg").value;
		var minorder = document.getElementById("minorder").value;
		var maxorder = document.getElementById("maxorder").value;
		if(categoryid==0){
			  jAlert('error', "Please select category!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		} else if(subcategoryid==0){
			  jAlert('error', "Please select sub category!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(prodname==''){
			  jAlert('error', "Please enter product name!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		} else if(mrp=='' || mrp=='0'){
			  	jAlert('error', "Please Enter MRP!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(parseFloat(mrp)<0){
		  	jAlert('error', "Please enter valid MRP!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
		} else if(purchase_price=='' || purchase_price=='0'){
			  jAlert('error', "Please enter purchase price!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(parseFloat(purchase_price)<0){
			  jAlert('error', "Please enter valid purchase price!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		} else if(sale_price=='' || sale_price=='0'){
			  jAlert('error', "Please enter sale price!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(parseFloat(sale_price)<0){
			  jAlert('error', "Please enter valid sale price!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		} else if(pack=='' || pack=='0'){
			  jAlert('error', "Please enter pack!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(!checkNumberOrNotofaddproduct(pack)){
			  jAlert('error', "Please enter valid pack!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		} else if(comment==''){
			  jAlert('error', "Please enter remark!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		}else if(Number(minorder)<0 || minorder==''){
			jAlert('error', "Please enter valid min order!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			return false;		
		}else if(Number(maxorder)<0 || maxorder==''){
			jAlert('error', "Please enter valid max order!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			return false;		
		}else {
			$("#dashboardloaderPopup").modal('show');
			document.getElementById("myform").submit();
		}	
		
     }
function submitForm1(){
			var stock1 = document.getElementById("stock").value;
			var mrp = document.getElementById("mrp").value;
			var pack = document.getElementById("pack").value;
			var purchase_price = document.getElementById("purchase_price").value;
			var batch_no = document.getElementById("batch_no").value;
			var expiry_date = document.getElementById("expiry_date").value;
			var flag =false;
			if(stock1==''){
				flag=true;
			}
	        var stock= parseInt(document.getElementById("stock").value);
	        if(flag){
	        	 jAlert('error', "Stock cant be blank!", 'Error Dialog');
					
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	        }else if(stock<0){
	        	 jAlert('error', "Stock cant be less than 0!", 'Error Dialog');
					
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
	        }else if(pack=='' || pack=='0'){
	        	jAlert('error', "Please enter pack!", 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	        }else if(mrp=='' || mrp=='0'){
	        	jAlert('error', "Please enter MRP!", 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	        } else if(purchase_price=='' || purchase_price=='0'){
	        	jAlert('error', "Please enter purchase price!", 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	        } else if(batch_no==''){
	        	jAlert('error', "Please enter batch number!", 'Error Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	        }else if(expiry_date==''){
	        	jAlert('error', "Please select expiry date!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
	        }else {
	        	document.getElementById("myform").submit();
	        }
		
     }
     
	
	var hadaccessgolbale=0;
	 function editStock(pid,hadaccess){
		 hadaccessgolbale = hadaccess;
		 
		 var url = "editproductProduct?id="+pid+"";
		    if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		    req.onreadystatechange = editStockRequest;
		    req.open("GET", url, true); 
		    req.send(null);
		 
	 } 
	 function editStockRequest(){
			if (req.readyState == 4) {
				if (req.status == 200) {
					
					 var data=req.responseText;
				     var str=data.split("~~");
					 
					 document.getElementById("id").value= str[0];		     
				     document.getElementById("genericname").value=str[1];
				     document.getElementById("product_name").value=str[2];
				     document.getElementById("mrp").value=str[3];
				     document.getElementById("purchase_price").value=str[4];
				     document.getElementById("sale_price").value=str[5];
				     document.getElementById("gst").value=str[6];
				     document.getElementById("batch_no").value=str[8];
				     //document.getElementById("mfg").value=str[7];
				     document.getElementById("stock").value=str[9];
				     document.getElementById("shelfid").value=str[10];
				     //document.getElementById("medicine_shedule").value=str[11];
				    // document.getElementById("hsnno").value=str[14];
				     document.getElementById("pack").value=str[15];
				     //document.getElementById("expiry_date").value=str[16];
				     document.getElementById("expiry_date").value=str[17];
				     document.getElementById("edit_barcode").value=str[18];
				     if(hadaccessgolbale==0){
				    	  	 document.getElementById("genericname").readOnly = true;
						     document.getElementById("product_name").readOnly = true;
						     document.getElementById("mrp").readOnly = true;
						     document.getElementById("purchase_price").readOnly = true;
						     document.getElementById("sale_price").readOnly = true;
						     document.getElementById("gst").readOnly = true;
						     document.getElementById("batch_no").readOnly = true;
						     document.getElementById("stock").readOnly = true;
						     document.getElementById("pack").readOnly = true;
						     document.getElementById("expiry_date").readOnly = true;
						     document.getElementById("edit_barcode").readOnly = true;
				     }
					  $("#editcat").modal('show');
					  
			   }
			}
		}
	 
	 function addToReturn(){
		 var ids="0";
	      $('.case').each(function() { 
				if(this.checked == true){
				    ids=ids+","+this.value;
				} 
										
			});
			
		 var url="addtoreturnCatalogue?data="+ids+"";	
		  if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = addToReturnRequest;
	    req.open("GET", url, true); 
	    req.send(null);  	
			
	}

	function addToReturnRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				$('.case').each(function() { 
				if(this.checked == true){
					 this.checked = false;    
				} 
			});
		       document.getElementById("returndiv").innerHTML=req.responseText;
			 }
		}	 
	}
	
	function showReturnPopUp(){
		
		var url = "showreturnmedicineCatalogue";

		
		if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = showReturnPopUpRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);

		}
		function showReturnPopUpRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
				var str = req.responseText;
					var data = str.split("~");
					document.getElementById("returncarttbody").innerHTML = data[0];
					document.getElementById("returncount").value = data[1];
					$('#returncart').modal( "show" );	
		         }
			}
		}
function confirmTransfer(){
	$("#dashboardloaderPopup").modal('show');
	var y;
 	$('.ajclass').each(function() { 
 		y =validateTransferStock(this.value);
	});
		if (y){
			 $("#dashboardloaderPopup").modal('hide');
			var x = confirm("Are You Sure!!");
			if(x){
				$("#dashboardloaderPopup").modal('show');
				document.getElementById("divbtn").innerHTML='';
				document.getElementById("transferproductform").submit();
			}else{
				return false;
			}
  	}else{
  		 $("#dashboardloaderPopup").modal('hide');
		return false;
	}
}

function validateTransferStock(val){
	var location = document.getElementById("tlocation"+val).value;
	var tqty = document.getElementById("tqty"+val).value;
	if(location=='0' || location==''){
		jAlert('error', "Please select location!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
		return false;		
	}else if(tqty==''){
		jAlert('error', "Please enter quantity!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		return false;		
	}else if(tqty=='0'){
		jAlert('error', "Please enter quantity!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		return false;		
	}else if(!checkNumberOrNotofaddproduct(tqty)){
		jAlert('error', "Please enter valid quantity!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		return false;		
	}else{
		return true;
	}
}
		
	function confirmReturn()
		{	
		$("#dashboardloaderPopup").modal('show');
			var y;
		 	$('.akash').each(function() { 
		 		y =validateReturnStock(this.value);
			});
			
	  		if (y){
	  			$("#dashboardloaderPopup").modal('hide');
	  			var x = confirm("Are You Sure!!");
	  			if(x){
	  				$("#dashboardloaderPopup").modal('show');
	  				document.getElementById("returnbtndiv").innerHTML='';
	  				document.getElementById("returnproductform").submit();
	  			}else{
	  				return false;
	  			}
	      	}else{
	      		$("#dashboardloaderPopup").modal('hide');
	    		return false;
			}
	}
	
	
	
	function validateReturnStock(val){
		var return_tlocation = document.getElementById("return_tlocation"+val).value;
		var return_tqty = document.getElementById("return_tqty"+val).value;
		var stock = document.getElementById("return_stock"+val).value;
		if(return_tlocation=='0' || return_tlocation==''){
			jAlert('error', "Please select location!", 'Error Dialog');	
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 
			return false;		
		}else if(return_tqty==''){
			jAlert('error', "Please enter quantity!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			return false;		
		}else if(return_tqty=='0'){
			jAlert('error', "Please enter quantity!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			return false;		
		}else if(!checkNumberOrNotofaddproduct(return_tqty)){
			jAlert('error', "Please enter valid quantity!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			return false;		
		}else if(parseInt(return_tqty)>parseInt(stock)){
			jAlert('error', "Transfer can't be processed as requested qty not in stock!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
			document.getElementById("return_tqty"+val).value='0';
			return false;		
		} else{
			return true;
		}
	}
	
	function getsubCategoriesCatalogue(id) {
		
	      var url="getsubcategoriesProcurement?id="+id+"";
	     
	      if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		 }
		 else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		 }   
	               
	     req.onreadystatechange = getsubCategoriesCatalogueRequest;
	     req.open("GET", url, true); 
	              
	     req.send(null);   
		
	}
	function getsubCategoriesCatalogueRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				 
				   document.getElementById("sublist").innerHTML=req.responseText;
				   $("#subcategory_id").trigger("chosen:updated");
				   $(".chosen").chosen({allow_single_deselect: true}); 
		   }
		}
	}
	
	function deleteReturnProduct(r,mid,val1){
		//var i = parseInt(r)+1;
   	 	//var i = r.parentNode.parentNode.rowIndex;
		//document.getElementById("tabletreturncount").deleteRow(i);
		var i = r.parentNode.parentNode.rowIndex;
		document.getElementById("tabletreturncount").deleteRow(i);
		var total=Number(document.getElementById("returncount").value);
		total--; 
		document.getElementById("returncount").value=total;
		
		var url="deletereturnproductCatalogue?data="+mid+"";	
		  if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = deleteReturnProductRequest;
	    req.open("GET", url, true); 
	    req.send(null);  	
			
	}

	function deleteReturnProductRequest(){
		if (req.readyState == 4) {
			
			
		}	 
	}
	
	
	function selectAllLocation(val)
	{	
		var location = document.getElementById("tlocation"+val).value;
		$('.ajclass').each(function() { 
			if(this.value==val){
				
			}else {
				document.getElementById("tlocation"+this.value).value = location;
				
			}
		});	
	}
	
function addToReturnQue() {
		
		var ids="0"; 
		$('.case').each(function(){
			
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
		});
		var url="addtoreturnProduct?data="+ids+"";
		
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = addToReturnQueRequest;
	    req.open("GET", url, true); 
	    req.send(null);  
	    $("#dashboardloaderPopup").modal('show');
		
	}
	function addToReturnQueRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				$("#dashboardloaderPopup").modal('hide');
				$('.case').each(function(){
					 this.checked=false;
				});
				var str = req.responseText;
				if(str=='0'){
					jAlert('success', "Products Added to Return Que!", 'Success Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);     
				}else{
					jAlert('error', ""+str+"", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, 5000); 
				}
				            
			}
		}	 
	}
	
	 function addToIssue(){
		 var ids="0";
	      $('.case').each(function() { 
				if(this.checked == true){
				    ids=ids+","+this.value;
				} 
										
			});
			
		 var url="addtoissueCatalogue?data="+ids+"";	
		  if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = addToIssueRequest;
	    req.open("GET", url, true); 
	    req.send(null);  	
			
	}

	function addToIssueRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
				$('.case').each(function() { 
				if(this.checked == true){
					 this.checked = false;    
				} 
			});
		       document.getElementById("issuediv").innerHTML=req.responseText;
			 }
		}	 
	}
	
	function setissuetotransfer(id){
		var isfromcathlab = document.getElementById("isfromcathlab").value;
		var url = "showissuemedicineCatalogue?isfromcathlab=0&id="+id+"&isfrombimkit=1";

		
		if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = setissuetotransferRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);

		}
		function setissuetotransferRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
				var str = req.responseText;
					var data = str.split("~");
					document.getElementById("issuebody").innerHTML = data[0];
					document.getElementById("issuecount").value = data[1];
					var isfrombimkit = data[2];
					var clientname = data[3];
					var tempid = data[4];
					var clientid = data[5];
					if(isfrombimkit=='1'){
						document.getElementById("isfrombimkit").value =isfrombimkit;
						document.getElementById("issuepatientid").value =clientid;
						document.getElementById("issuepatientname").value =clientname;
						document.getElementById("bomkittemplate").innerHTML = data[6];
						 document.getElementById("cathtempid").className="form-control chosen";
						 $("#category_id").trigger("chosen:updated");
					  	 $(".chosen").chosen({allow_single_deselect: true});
					  	//document.getElementById("bomnewproddiv").style.display ='none';
					  	//document.getElementById("bomnewprodadddiv").style.display ='none';
					  	document.getElementById("issuecomment").value = data[7];
					}
					document.getElementById("consumeheadingdiv").innerHTML ='<h4 class="modal-title">Consume Bom Kit</h4>';
					$('#issuecart').modal( "show" );	
		         }
			}
		}
	
	function openbomkitpouup() {
		//document.getElementById("bomnewproddiv").style.display ='';
	  	//document.getElementById("bomnewprodadddiv").style.display ='';
	  	document.getElementById("consumeheadingdiv").innerHTML ='<h4 class="modal-title">Issue Bom Kit</h4>';
		$('#issuecart').modal( "show" );	
	}
	
	function setcathTempValue(val){
		if(val=='0'){
			document.getElementById("issuebody").innerHTML = "";
			document.getElementById("issuecount").value = "0";
		}else{
			showIssuePopUp(val);
		}
	}
	
	function showIssuePopUp(val){
		var isfromcathlab = document.getElementById("isfromcathlab").value;
		var url = "showissuemedicineCatalogue?isfromcathlab="+isfromcathlab+"&val="+val+"&isfrombimkit=0";

		
		if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = showIssuePopUpRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);

		}
		function showIssuePopUpRequest(){
		if (req.readyState == 4) {
				if (req.status == 200) {
				var str = req.responseText;
					var data = str.split("~");
					document.getElementById("issuebody").innerHTML = data[0];
					document.getElementById("issuecount").value = data[1];
					$('#issuecart').modal( "show" );	
		         }
			}
		}
		function confirmIssue(){
			$("#dashboardloaderPopup").modal('show');
			var y;
		 	$('.issueclass').each(function() { 
		 		y =validateIssueStock(this.value);
			});
			var flag =false;
			var flag1= false;
			var flag2= false;
		 	var hisuserfilter= document.getElementById("hisuserfilter").value;
			
		 	if(hisuserfilter=='0'){
		 		var issuepatientid = document.getElementById("issuepatientid").value;
		 		if(issuepatientid=='0'){
		 			flag =true;
		 		}
		 	}else if(hisuserfilter=='1'){
		 		var issueuserid = document.getElementById("issueuserid").value;
		 		if(issueuserid=='0'){
		 			flag1 =true;
		 		}
		 	}else{
		 		var hisdepartmentfilter = document.getElementById("hisdepartmentfilter").value;
		 		if(hisdepartmentfilter=='0'){
		 			flag2 =true;
		 		}
		 	}
		 	
			if(flag){
				$("#dashboardloaderPopup").modal('hide');
				jAlert('error', "Please select patient!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
			}else if(flag1){
				$("#dashboardloaderPopup").modal('hide');
				jAlert('error', "Please select user!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
			}else if(flag2){
				$("#dashboardloaderPopup").modal('hide');
				jAlert('error', "Please select department!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
			}else if (y){
				$("#dashboardloaderPopup").modal('hide');
				var x = confirm("Are You Sure!!");
				if(x){
					$("#dashboardloaderPopup").modal('show');
					document.getElementById("issuedivbtn").innerHTML='';
					document.getElementById("issueproductform").submit();
				}else{
					$("#dashboardloaderPopup").modal('hide');
					return false;
				}
		  	}else{
		  		$("#dashboardloaderPopup").modal('hide');
				return false;
			}
		}
		
		function confirmIssueCathLab(){
			var y=true;;
		 	$('.issueclass').each(function() { 
		 		if(y==false){
		 			
		 		}else{
		 			y =validateIssueStock(this.value);
		 		}
		 	});
			var flag =false;
			var issuepatientid = document.getElementById("issuepatientid").value;
	 		if(issuepatientid=='0'){
	 			flag =true;
	 		}
			if(flag){
				jAlert('error', "Please select patient!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
			}else if (y){
				var x = confirm("Are You Sure!!");
				if(x){
					document.getElementById("issuedivbtn").innerHTML='';
					document.getElementById("issueproductform").submit();
				}else{
					return false;
				}
		  	}else{
				return false;
			}
		}
		
		function validateIssueStock(val){
			//var location = document.getElementById("issuetlocation"+val).value;
			var tqty = document.getElementById("issuetqty"+val).value;
			
			var reqqty = document.getElementById("issuetqty"+val).value;
			var stock = document.getElementById("issuestock"+val).value;
			
			var isfromcathlab= document.getElementById("isfromcathlab").value;
			if(parseInt(reqqty)>parseInt(stock)){
				jAlert('error', "Requested qty not in stock!", 'Error Dialog');	
						setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
				}, alertmsgduration); 
				document.getElementById("issuetqty"+val).value='0';
				return false;		
			}else if(tqty==''){
				jAlert('error', "Please enter quantity!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
				return false;		
			}else if(tqty=='0'){
				jAlert('error', "Please enter quantity!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
				return false;		
			}else if(!checkNumberOrNotofaddproduct(tqty)){
				jAlert('error', "Please enter valid quantity!", 'Error Dialog');	
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration); 
				return false;		
			}else{
				return true;		
			}
		}
		var row;
		function addnewcathlabrow(tableId){
			  
		     var table = document.getElementById(tableId);
			 var rowCount = table.rows.length;
			 var pid = document.getElementById("cathproductid").value;
			 var flag = false;
			 var count = document.getElementById("issuecount").value;
			 $('.issueprodclass').each(function() {
		         var i=this.value;
		         if(i==pid){
		        	 flag = true;
		         }
		     });
			 if(pid=='0'){
				 jAlert('error', "Please select product!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
			}else if(flag==false){
				 row=table.insertRow(rowCount);
				 //row=table.insertRow(count++);
				 //rowCount--;
				 count = parseInt(count)+1;
				 var url="addnewcathlabtempCatalogue?count="+count+"&pid="+pid+"";
			   
			     if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				 }
				 else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				 }   
			               
			     req.onreadystatechange = addnewcathlabrowRequest;
			     req.open("GET", url, true); 
			              
			     req.send(null);   
			 }else{
				 jAlert('error', "Already added this item!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
			 }
			 
		}
		function addnewcathlabrowRequest(){
			if (req.readyState == 4) {
				if (req.status == 200) {
					var str = req.responseText;
					var data = str.split("~");
					row.innerHTML=data[0];
					document.getElementById("issuecount").value =data[1];
				}
			}	 
		}
		function deleteBOMKITProduct(r,val1,val2) {
		    var i = r.parentNode.parentNode.rowIndex;
		    document.getElementById("tableissuecount").deleteRow(i);
		}
		
		function deleteIssueProduct(r,mid){
			//var i = parseInt(r)+1;
	   	 	var i = r.parentNode.parentNode.rowIndex;
			document.getElementById("tableissuecount").deleteRow(i);
			var total=Number(document.getElementById("issuecount").value);
			//total--; 
			document.getElementById("issuecount").value=total;
			var isfromcathlab= document.getElementById("isfromcathlab").value;
			if(isfromcathlab=='1'){
				
			}else{
				var url="deleteissueproductCatalogue?data="+mid+"";	
				  if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			    req.onreadystatechange = deleteIssueProductRequest;
			    req.open("GET", url, true); 
			    req.send(null);  	
			}
			
				
		}

		function deleteIssueProductRequest(){
			if (req.readyState == 4) {
				
				
			}	 
		}
		
		
		/*function selectAllLocation(val)
		{	
			var location = document.getElementById("tlocation"+val).value;
			$('.ajclass').each(function() { 
				if(this.value==val){
					
				}else {
					document.getElementById("tlocation"+this.value).value = location;
					
				}
			});	
		}*/
	
		function checkNotGreterThanStock3(val){
			var reqqty = document.getElementById("issuetqty"+val).value;
			var stock = document.getElementById("issuestock"+val).value;
			 var isfromcathlab = document.getElementById("isfromcathlab").value;
			   if(isfromcathlab=='1'){
				   
			   }else{
				if(parseInt(reqqty)>parseInt(stock)){
					jAlert('error', "Transfer can't be processed as requested qty not in stock!", 'Error Dialog');	
								setTimeout(function() {
									$("#popup_container").remove();
									removeAlertCss();
								}, alertmsgduration); 
					document.getElementById("issuetqty"+val).value='0';
				}
			 }
		}
		var issuetoindentval = "";
		function getpatientpop(val){
			
			var url = "showAllpatientListCatalogue?val="+val+"";
			issuetoindentval = val;
			
			if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = getpatientpopRequest;
			    req.open("GET", url, true); 
			              
			    req.send(null);

			}
			function getpatientpopRequest(){
			if (req.readyState == 4) {
					if (req.status == 200) {
					
						document.getElementById("allPatient").innerHTML = req.responseText;
						$('#clientSearchDiv').modal( "show" );	
			         }
				}
			}
			
			/*function setPatientForTransfer(name,id,gender,val) {
				document.getElementById("issuetlocation"+val).value = name;
				document.getElementById("issueclientid"+val).value = id;
				document.getElementById("ival").value = val;
				$('#clientSearchDiv').modal( "hide" );	 		
			}*/
			
			function setPatientForTransferNew(id,val){
				
				var url = "getPatientDetailsCatalogue?id="+id+"&val="+val+"";

				
				if (window.XMLHttpRequest) {
						req = new XMLHttpRequest();
					}
					else if (window.ActiveXObject) {
						isIE = true;
						req = new ActiveXObject("Microsoft.XMLHTTP");
					}   
				               
				    req.onreadystatechange = setPatientForTransferNewRequest;
				    req.open("GET", url, true); 
				              
				    req.send(null);

				}
				function setPatientForTransferNewRequest(){
				if (req.readyState == 4) {
						if (req.status == 200) {
							var str = req.responseText;
							var data = str.split("~");
							/*document.getElementById("issuetlocation"+data[2]).value = data[0];
							document.getElementById("issueclientid"+data[2]).value = data[1];
							document.getElementById("ival").value = data[2];*/
							document.getElementById("issuepatientname").value = data[0];
							document.getElementById("issuepatientid").value = data[1];
							$('#clientSearchDiv').modal( "hide" );	 	
				         }
					}
				}
			
			
			function searchPatient(){
				var searchText = document.getElementById("searchText1").value;
				var ival = document.getElementById("ival").value;
				ival = issuetoindentval;
				var url = "searchParticularPatientCatalogue?searchText="+searchText+"&val="+ival+"";

				if (window.XMLHttpRequest) {
						req = new XMLHttpRequest();
					}
					else if (window.ActiveXObject) {
						isIE = true;
						req = new ActiveXObject("Microsoft.XMLHTTP");
					}   
				               
				    req.onreadystatechange = searchPatientRequest;
				    req.open("GET", url, true); 
				              
				    req.send(null);

				}
				
			function searchPatientRequest(){
				if (req.readyState == 4) {
						if (req.status == 200) {
							document.getElementById("allPatient").innerHTML = req.responseText;
				         }
					}
				}
			
			
			var obj;
			function chkNameExist(d) {
				obj=d;
				var url="catnameexistCatalogue?name="+d.value+"";
				if (window.XMLHttpRequest) {
					req = new XMLHttpRequest();
				}
				else if (window.ActiveXObject) {
					isIE = true;
					req = new ActiveXObject("Microsoft.XMLHTTP");
				}   
			               
			    req.onreadystatechange = chkNameExistRequest;
			    req.open("GET", url, true); 
			    req.send(null);
				
			}
			function chkNameExistRequest(){
				if (req.readyState == 4) {
						if (req.status == 200) {
							 
							 var res= req.responseText;
							 if(res=="1"){
								    
								 obj.value='';
								 jAlert('error', "Product Name Exist!", 'Error Dialog');	
									setTimeout(function() {
										$("#popup_container").remove();
										removeAlertCss();
									}, alertmsgduration); 
							 }	
							 
				         }
					}
				}
			
			function chkNameExistIncatalogue(d) {
				var warehouse=document.getElementById("warehouse").value;
				if(warehouse=='' || warehouse=='0'){
					 jAlert('error', "Please select warehouse!", 'Error Dialog');	
						setTimeout(function() {
							$("#popup_container").remove();
							removeAlertCss();
						}, alertmsgduration); 
						document.getElementById("prodname").value ='';
				 }else{
						obj=d;
						var url="catnameexistnewCatalogue?name="+d.value+"&warehouse="+warehouse+"";
						if (window.XMLHttpRequest) {
							req = new XMLHttpRequest();
						}
						else if (window.ActiveXObject) {
							isIE = true;
							req = new ActiveXObject("Microsoft.XMLHTTP");
						}   
					               
					    req.onreadystatechange = chkNameExistInCatalogueRequest;
					    req.open("GET", url, true); 
					    req.send(null);
				 }
			}
			function chkNameExistInCatalogueRequest(){
				if (req.readyState == 4) {
						if (req.status == 200) {
							 
							 var res= req.responseText;
							 if(res=="1"){
								    
								 obj.value='';
								 jAlert('error', "Product Name Exist!", 'Error Dialog');	
									setTimeout(function() {
										$("#popup_container").remove();
										removeAlertCss();
									}, alertmsgduration); 
							 }	
							 
				         }
					}
				}

			
 function checkMaxorder(i,d){
	 
	  var maxorder= Number(document.getElementById("maxorder"+i).value);
	  var minorder= Number(document.getElementById("minorder"+i).value);
	  if(d>maxorder){
		  
		  jAlert('Success', "Product is Crossing Max Order Guidelines > "+maxorder+" !!!", 'Success Dialog');
			
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, 2000); 
	  }else if(d<minorder){
		  jAlert('Success', "Product is Below Min Order Guidelines > "+minorder+" !!!", 'Success Dialog');
		  setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, 2000); 
	  }
	 
 }
 
 function deilverproductfromstore(){
	 $("#dashboardloaderPopup").modal('show');
		var flagstatus = false;
		$('.checkclass').each(function() {
	        var i=this.value;
	        if(!flagstatus){
	        	var qty = document.getElementById("reqqty"+i).value;
	        	var stock = document.getElementById("stock"+i).value;
	        	var isnumberqty = checkNumberOrNotofaddproduct(qty);
	        	if(qty=='' && flagstatus==false){
	        		flagstatus=true;
	        		 jAlert('error', "Please enter quantity!", 'Error Dialog');	
	        			setTimeout(function() {
	        				$("#popup_container").remove();
	        				removeAlertCss();
	        			}, alertmsgduration); 
	        	}else if(!isnumberqty && flagstatus==false){
	        		flagstatus=true;
	        		 jAlert('error', "Please enter valid qunatity!", 'Error Dialog');	
	        			setTimeout(function() {
	        				$("#popup_container").remove();
	        				removeAlertCss();
	        			}, alertmsgduration); 
	        	}else if(qty=='0' && flagstatus==false){
	        		flagstatus=true;
	        		 jAlert('error', "Please enter quantity greater than 0!", 'Error Dialog');	
	        			setTimeout(function() {
	        				$("#popup_container").remove();
	        				removeAlertCss();
	        			}, alertmsgduration); 
	        	}else if(parseInt(qty)>parseInt(stock)){
	        		flagstatus=true;
					jAlert('error', "You are enter requested qty greater than stock!", 'Error Dialog');	
								setTimeout(function() {
									$("#popup_container").remove();
									removeAlertCss();
								}, alertmsgduration); 
					document.getElementById("reqqty"+val).value='0';
				}
	        }
	     });
		
		if(!flagstatus){
			 $("#dashboardloaderPopup").modal('hide');
			 var x = confirm("Are you sure you want to deliver?");
			 if (x){
				 $("#dashboardloaderPopup").modal('show');
				 document.getElementById("btnhideid").innerHTML ='';
				 document.getElementById("deliverproductsubmit").submit();
			 }
			
		}else{
			 $("#dashboardloaderPopup").modal('hide');
		}
	 
 }
 
 //Akash 02 Jun 2018
function getpatientpopForIssue(){
	var val='';
	var url = "showAllpatientListCatalogue?val="+val+"";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = getpatientpopRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function getpatientpopRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			document.getElementById("allPatient").innerHTML = req.responseText;
			$('#clientSearchDiv').modal( "show" );	
		}
	}
}

function setPatientForTransferJson(id,val){
	var dataObj={
		  	"id":""+id+"",
		  	"val":""+val+"",
	};
	var data1 =  JSON.stringify(dataObj);
	$.ajax({
	   url : "getpatientlistfrissueCatalogue",
	   data : data1,
	   dataType : 'json',
	   contentType : 'application/json',
	   type : 'POST',
	   async : true,
	   success : function(data) {
		   var condition= data.val1;
		   document.getElementById("issuepatientname").value=data.pname;
		   document.getElementById("issuepatientid").value=data.clientid;
		   var isfromcathlab = document.getElementById("isfromcathlab").value;
		   if(isfromcathlab=='1'){
			   
		   }else{
			   document.getElementById("issueprocediv").innerHTML=data.procedurelist;
			   $("#issueproceid").trigger("chosen:updated");
			   $(".chosen").chosen({allow_single_deselect: true});  
		   }
		  
		   $('#clientSearchDiv').modal( "hide" );	
	   },
	   error : function(result) {
		   jAlert('error', "Something wrong!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	   }
	});
}

function setpatientuserlist(val){
	if(val=='0'){
		 document.getElementById("issuechangediv").innerHTML='<input type="hidden" class="form-control" name="issuepatientid" value="0" id="issuepatientid"> <input type="text" readonly="readonly" placeholder="Select Patient/User" class="form-control" id="issuepatientname" onclick="getpatientpopForIssue()">';
		 document.getElementById("issueprocediv").innerHTML='<select id="issueproceid" name="issueproceid" class="from-control chosen"><option value="0">Select Procedure</option></select>';
		 $("#issueproceid").trigger("chosen:updated");
		   $(".chosen").chosen({allow_single_deselect: true});
	}else if(val=='1'){
		var dataObj={
			  	"val":""+val+"",
		};
		var data1 =  JSON.stringify(dataObj);
		$.ajax({
		   url : "getuserlistfrissueCatalogue",
		   data : data1,
		   dataType : 'json',
		   contentType : 'application/json',
		   type : 'POST',
		   async : true,
		   success : function(data) {
			   var condition= data.val1;
			   document.getElementById("issuechangediv").innerHTML=data.userlist;
			   document.getElementById("issueprocediv").innerHTML="";
			   $("#issueuserid").trigger("chosen:updated");
			   $(".chosen").chosen({allow_single_deselect: true}); 
		   },
		   error : function(result) {
			   jAlert('error', "Something wrong!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		   }
		});
	}else{
		var dataObj={
			  	"val":""+val+"",
		};
		var data1 =  JSON.stringify(dataObj);
		$.ajax({
		   url : "getdepartmentfrissueCatalogue",
		   data : data1,
		   dataType : 'json',
		   contentType : 'application/json',
		   type : 'POST',
		   async : true,
		   success : function(data) {
			   var condition= data.val1;
			   document.getElementById("issuechangediv").innerHTML=data.userlist;
			   document.getElementById("issueprocediv").innerHTML="";
			   $("#hisdepartmentfilter").trigger("chosen:updated");
			   $(".chosen").chosen({allow_single_deselect: true}); 
		   },
		   error : function(result) {
			   jAlert('error', "Something wrong!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		   }
		});
	}
	
}

//checkProductCodeExist

function checkProductCodeExist(count,val){
		var dataObj={
			  	"val":""+val+"",
		};
		var data1 =  JSON.stringify(dataObj);
		$.ajax({
		   url : "checkproductcodeexistCatalogue",
		   data : data1,
		   dataType : 'json',
		   contentType : 'application/json',
		   type : 'POST',
		   async : true,
		   success : function(data) {
			  var condition= data.val1;
			  if(condition=='1'){
				  jAlert('error', "Product code already exist!", 'Error Dialog');
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
					document.getElementById("pro_code"+count).value="";
			  }
		   },
		   error : function(result) {
			   jAlert('error', "Something wrong!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
		   }
		});
}


function addToCathTemp(){
	 var ids="0";
     $('.case').each(function() { 
			if(this.checked == true){
			    ids=ids+","+this.value;
			} 
									
		});
		
	 var url="addtocathtempCatalogue?data="+ids+"";	
	  if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
   req.onreadystatechange = addToCathTempRequest;
   req.open("GET", url, true); 
   req.send(null);  	
		
}

function addToCathTempRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			$('.case').each(function() { 
			if(this.checked == true){
				 this.checked = false;    
			} 
		});
	       document.getElementById("cathtempdiv").innerHTML=req.responseText;
		 }
	}	 
}


function showCathTempPopUp(){
	var url = "showcathtempmedicineCatalogue";
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
	req.onreadystatechange = showCathTempPopUpRequest;
	req.open("GET", url, true); 
	req.send(null);
}
function showCathTempPopUpRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
		var str = req.responseText;
			var data = str.split("~");
			document.getElementById("cathempbody").innerHTML = data[0];
			document.getElementById("cathtempcount").value = data[1];
			$('#cathtempcart').modal( "show" );	
		}
	}
}


function deleteCathTempProduct(r,mid){
	var i = parseInt(r)+1;
	 	//var i = r.parentNode.parentNode.rowIndex;
	document.getElementById("tablecathcount").deleteRow(i);
	var total=Number(document.getElementById("cathtempcount").value);
	total--; 
	document.getElementById("cathtempcount").value=total;
	
	var url="deletecathtempproductCatalogue?data="+mid+"";	
	  if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = deleteCathTempProductRequest;
    req.open("GET", url, true); 
    req.send(null);  	
		
}

function deleteCathTempProductRequest(){
	if (req.readyState == 4) {
		
		
	}	 
}

function checkNotGreterThanStock4(val){
	var reqqty = document.getElementById("cathtemptqty"+val).value;
	var stock = document.getElementById("cathtempstock"+val).value;
	if(parseInt(reqqty)>parseInt(stock)){
		jAlert('error', "Transfer can't be processed as requested qty not in stock!", 'Error Dialog');	
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
		document.getElementById("cathtemptqty"+val).value='0';
	}
}

function confirmCathTemp(){
	var y;
 	$('.cathtempclass').each(function() { 
 		y =validateCathTempStock(this.value);
	});
 	
	var flag =false;
	var flag1= false;
 	var cathtempprocedure= document.getElementById("cathtempprocedure").value;
	var cathtemplatename = document.getElementById("cathtemplatename").value;
 	
	if(cathtempprocedure=='0'){
		jAlert('error', "Please  select procedure!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
	}else if(cathtemplatename==''){
		jAlert('error', "Please  enter template name!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
	}else if (y){
		var x = confirm("Are You Sure!!");
		if(x){
			document.getElementById("cathtempdivbtn").innerHTML='';
			document.getElementById("cathtempproductform").submit();
		}else{
			return false;
		}
  	}else{
		return false;
	}
}

function validateCathTempStock(val){
	var tqty = document.getElementById("cathtemptqty"+val).value;

	if(tqty==''){
		jAlert('error', "Please enter quantity!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		return false;		
	}else if(tqty=='0'){
		jAlert('error', "Please enter quantity!", 'Error Dialog');	
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
		return false;		
	}else{
		return true;
	}
}

function getcathtemplatedata(id){
	var dataObj={
		  	"id":""+id+"",
	};
	var data1 =  JSON.stringify(dataObj);
	$.ajax({
	   url : "getcathtemplatedataCatalogue",
	   data : data1,
	   dataType : 'json',
	   contentType : 'application/json',
	   type : 'POST',
	   async : true,
	   success : function(data) {
		  document.getElementById("cathtempcount").value=data.count;
		  document.getElementById("cathempbody").innerHTML=data.list;
		  document.getElementById("updatecathprocedurelist").innerHTML=data.procedurelist;
		  document.getElementById("cathtemplatename").value=data.templatename;
		  document.getElementById("cathtempdeleteids").value="0";
		  document.getElementById("cathtempparentid").value=id;
		  document.getElementById("cathtempcomment").value=data.remark;
		  $("#cathtempprocedure").trigger("chosen:updated");
		   $(".chosen").chosen({allow_single_deselect: true}); 
		  $('#cathtempcart').modal( "show" );	
	   },
	   error : function(result) {
		   jAlert('error', "Something wrong!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	   }
	});
}

function addnewcathlabtemplaterow(tableId){
	 var table = document.getElementById(tableId);
	 var rowCount = table.rows.length;
	 var pid = document.getElementById("cathproductid").value;
	 var flag = false;
	 var count = document.getElementById("cathtempcount").value;
	 var newrow;
	 $('.cathtempproclass').each(function() {
         var i=this.value;
         if(i==pid){
        	 flag = true;
         }
     });
	
	 if(pid=='0'){
		 jAlert('error', "Please select product!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
	}else if(flag==false){
		 newrow=table.insertRow(rowCount);
		 count = parseInt(count)+1;
		 var dataObj={
				  	"count":""+count+"",
				  	"pid":""+pid+"",
		 };
			var data1 =  JSON.stringify(dataObj);
			$.ajax({
			   url : "addcathtemplatedataCatalogue",
			   data : data1,
			   dataType : 'json',
			   contentType : 'application/json',
			   type : 'POST',
			   async : true,
			   success : function(data) {
				   newrow.innerHTML=data.list5;
				  document.getElementById("cathtempcount").value =data.count2;
			   },
			   error : function(result) {
				   jAlert('error', "Something wrong!", 'Error Dialog');
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration);
			   }
			});
	 }else{
		 jAlert('error', "Already added this item!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration); 
	 }
}

function deleteBomKitTemplate(r,val1,val2) {
    var i = r.parentNode.parentNode.rowIndex;
    document.getElementById("tablecathcountnew").deleteRow(i);
    var data = document.getElementById("cathtempdeleteids").value;
    data= data+","+val1;
    document.getElementById("cathtempdeleteids").value =data;
}
 
//Akash 07 Sep 2018
var bomkitid="0";
var bomkitclientid="0";
function showAddchargePopupCath(id,clientid){
	bomkitid=id;
	bomkitclientid =clientid;
	var url = "truncateBookAppointmentAjax";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
    req.onreadystatechange = showAddchargePopupCathRequest;
    req.open("GET", url, true); 
    req.send(null);
}

function showAddchargePopupCathRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			addOutoCathlabCharge();
		}
	}
	
}


function addOutoCathlabCharge(){
	var url = "setchargesofbomkitCatalogue?bomkitid="+bomkitid+"&ipdclientid="+bomkitclientid+" ";
	   
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = addOutoCathlabChargeRequest;
    req.open("GET", url, true); 
              
    req.send(null);
}
function addOutoCathlabChargeRequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			setcathipdopdCashDesk();
			$('#addchargepopupcath').modal( "show" );
		}
	}
}

//ipd and opd add charges

function setcathipdopdCashDesk(){
	var selectedUser = bomkitclientid;
	var cookiecommencing = '';
	var cookieSelectedAppointmentid = 0;
	var url = "cashDeskBookAppointmentAjax?selectedUser="+selectedUser+"&date="+cookiecommencing+"&apmtSlotId="+cookieSelectedAppointmentid+"" ;
		
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = setcathipdopdCashDeskRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
}

	function setcathipdopdCashDeskRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				   document.getElementById("cashDesk31").innerHTML = req.responseText;
				   document.getElementById('chargeTotal3').value = document.getElementById('hiddenTotal').value;
			}
		}
	}
	
	function confirmedDelete1(id){
		//alert(id);
		
	  	var url = "deleteCashDeskCompleteApmt?selectedid="+id+"";
	  
	 
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = confirmedDeleteRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
		
	}

	function confirmedDeleteRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				setcathipdopdCashDesk();
			}
		}
	}
	
	var opdcash = '';
	function createChargeAndUpdateAccount(action){
		opdcash = action;
		var clientid = bomkitclientid; 
		var practitionerid = "0";
		var clientName = "0";
		var practitionerName = "";
		var appointmentid = "0";
		var tratmentepisodeid = "0";
		var treatmenntsessions = "0";
		var location = "1";
		
		if(location==0){
			jAlert('error', 'Please select location.', 'Error Dialog');
			
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
		}else{
			var ipd = 0;
			var url = "updateAccountCompleteApmt?appointmentid="+appointmentid+"&clientid="+clientid+"&practitionerid="+practitionerid+"&clientName="+clientName+"&practitionerName="+practitionerName+"&action="+action+"&tratmentepisodeid="+tratmentepisodeid+"&treatmenntsessions="+treatmenntsessions+"&location="+location+"&ipd="+ipd+" ";
			   
			if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = createChargeAndUpdateAccountRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);
		}
	}
		
	function createChargeAndUpdateAccountRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				updateBomKITStatus(bomkitid);
			}
		}
	}
	
	var opdcash = '';
	function updateBomKITStatus(bomkitid){
			var url = "updatebomkitstatusCatalogue?bomkitid="+bomkitid+"";
			   
			if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			}
			else if (window.ActiveXObject) {
				isIE = true;
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}   
		               
		    req.onreadystatechange = updateBomKITStatusRequest;
		    req.open("GET", url, true); 
		              
		    req.send(null);
	}
		
	function updateBomKITStatusRequest(){
		if (req.readyState == 4) {
			if (req.status == 200) {
				jAlert('success', 'Charge added successfully.', 'Success Dialog');
				
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				
				window.location.reload();
			}
		}
	}

function ConfirmDeleteTemplate(){
  		var x = confirm("Are you sure you want to delete?");
  		if (x)
      		return true;
  		else
    		return false;
}

function showNotificationPopUp(){
	
	var url = "showponotificationProcurement";

	
	if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	               
	    req.onreadystatechange = showNotificationPopUpRequest;
	    req.open("GET", url, true); 
	              
	    req.send(null);
	   
	}
function showNotificationPopUpRequest(){
	if (req.readyState == 4) {
			if (req.status == 200) {
			var str = req.responseText;
				document.getElementById("notificationbody").innerHTML = str;
				if(str==""){
					
				}else{
				$("#notificationpopup").modal('show');
				
				setTimeout(function() {
					$("#notificationpopup").modal('hide');
					removeAlertCss();
				}, 3000);
	         }
		}
	}
}

function checkReturnQtyNotGreater(val) {
	var stock = Number(document.getElementById("stock"+val).value);
	var qty = Number(document.getElementById("qty"+val).value);
	if(qty>stock){
		jAlert('error', 'Enter return quantity greater than stock!!!', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		document.getElementById("qty"+val).value= stock;
	}
}

function deleteReturnTempReq(r,table,id,catalogueid) {

    var d=window.confirm("Are you sure to Delete this Entry!");
    
    if(d==true){
    	
    	 var i = r.parentNode.parentNode.rowIndex;
         document.getElementById(table).deleteRow(i);
         
         var url="deletereturngrnProcurement?id="+id+"";  	  
         if (window.XMLHttpRequest) {
    		req = new XMLHttpRequest();
    	 }
    	 else if (window.ActiveXObject) {
    		isIE = true;
    		req = new ActiveXObject("Microsoft.XMLHTTP");
    	 }   
                   
         req.onreadystatechange = deleteReturnTempReqRequest;
         req.open("GET", url, true); 
                  
         req.send(null);  
    } 
    	
 }
 function deleteReturnTempReqRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
	
         }
	}	 
}
 
function validateReturnQueSubmit() {
	var flag = false;
	var flag2= false;
	$('.case').each(function() {
		if (this.checked == true) {
			flag = true;
			var val = this.value;
			var stock = Number(document.getElementById("stock"+val).value);
			var qty =document.getElementById("qty"+val).value;
			var isnumberqty = checkNumberOrNotofaddproduct(qty);
			if(qty==''){
				flag2 = true;
				 jAlert('error', "please enter quantity!", 'Error Dialog');
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
			}else if(qty=='0'){
				flag2 = true;
				 jAlert('error', "please enter quantity!", 'Error Dialog');
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
			}else if(!isnumberqty){
				flag2 = true;
				 jAlert('error', "please enter valid quantity!", 'Error Dialog');
					setTimeout(function() {
						$("#popup_container").remove();
						removeAlertCss();
					}, alertmsgduration); 
			}if(qty>stock){
				flag2 = true;
				jAlert('error', 'Enter return quantity greater than stock!!!', 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
				/*document.getElementById("qty"+val).value= stock;*/
			}
		}
	});
	
	if(!flag){
		jAlert('error', 'Please select at least one product!!!', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		return false;
	}else if(flag2){
		return false;
	}else{
		return true;
	}
}
var grnreturnidss = 0;
function openAproveReturnProductPopupNew(returngrnid){
	grnreturnidss =returngrnid;
	var url="getretrurnnetamtProduct?returngrnid="+returngrnid+"";
    if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
	}
	  else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
       
		req.onreadystatechange = openAproveReturnProductPopupNewRequest;
		req.open("GET", url, true); 
      
		req.send(null);    

}

function openAproveReturnProductPopupNewRequest(){
if (req.readyState == 4) {
	if (req.status == 200) {
    	var data=req.responseText;
		document.getElementById("aprovereturnbaltext").innerText=data;
		document.getElementById("aprovereturnbal").value=data;
		document.getElementById("aprovereturnid").value=grnreturnidss;
		$('#clearaproveamt').modal( "show" );
	}
}
}

function aprovebalance() {
	var aprovereturnbal = Number(document.getElementById("aprovereturnbal").value);
	var totalenteredpayamt = document.getElementById("totalenteredpayamt").value;
	if(totalenteredpayamt==''){
		jAlert('error', 'Enter aprroved amount!!!', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else if(totalenteredpayamt=='0'){
		jAlert('error', 'Enter approved amount!!!', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else if(Number(totalenteredpayamt)>aprovereturnbal){
		jAlert('error', 'Enter amount greater than return grn amount!!!', 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
	}else{
		$('#clearaproveamt').modal( "hide" );
		   document.getElementById("formaprove").submit();
	}
}

function getadjustmentdata(id){
	 var dataObj={
			  	"id":""+id+"",
	 };
	var data1 =  JSON.stringify(dataObj);
	$.ajax({
	   url : "getadjustmentdataProduct",
	   data : data1,
	   dataType : 'json',
	   contentType : 'application/json',
	   type : 'POST',
	   async : true,
	   success : function(data) {
		  document.getElementById("adj_productid").value =data.productid;
		  document.getElementById("adj_productname").innerHTML =data.productname;
		  document.getElementById("adj_pre_stock").value =data.stock;
		  document.getElementById("adj_change_stock").value =0;
		  document.getElementById("adj_change_stock_indi").value =0;
		  $("#adjustmentmodel").modal('show');
	   },
	   error : function(result) {
		   jAlert('error', "Something wrong!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
	   }
	}); 
}

function resetadjustmentfield() {
	  document.getElementById("adj_change_stock").value =0;
	  document.getElementById("adj_change_stock_indi").value =0;
}

function calculateChangeAdjData(val){
	var pre_stock = Number(document.getElementById('adj_pre_stock').value);
	var adj_type = document.getElementById('adjustment_type').value;
	if(adj_type=='0'){
		document.getElementById("adj_change_stock_indi").value =0;
		jAlert('error', "Please select adjustment type!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		iserror =  false;
	}else if(val=='' || val=='0'){		
		 jAlert('error', "Please enter change qty!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			iserror =  false;
	}else if(Number(val)<0){
		  document.getElementById("adj_change_stock_indi").value =0;
		jAlert('error', "Please enter change qty non-negative!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		iserror =  false;
	}else{
		var qty = Number(val); 
		
		if(adj_type=='1'){
			document.getElementById('adj_change_stock').value=  pre_stock - qty;
			var totalchangeqty=pre_stock - qty;
			if(totalchangeqty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='2'){
			document.getElementById('adj_change_stock').value=  qty + pre_stock;
			var totalchangeqty=qty + pre_stock;
			if(totalchangeqty<=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be greater than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='3'){
			document.getElementById('adj_change_stock').value= pre_stock - qty;
			var totalchangeqty=pre_stock - qty;
			if(totalchangeqty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='4'){
			document.getElementById('adj_change_stock').value= pre_stock - qty;
			var totalchangeqty=pre_stock - qty;
			if(totalchangeqty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}
	}
}

function submitadjustmentadata() {
	$("#dashboardloaderPopup").modal('show');
	var pre_stock = Number(document.getElementById('adj_pre_stock').value);
	var adj_type = document.getElementById('adjustment_type').value;
	var val = document.getElementById('adj_change_stock').value;
	var adj_comment = document.getElementById('adj_comment').value;
	var enterqty = document.getElementById("adj_change_stock_indi").value;
	var iserror = true;
	if(!checkNumberOrNotofaddproduct(enterqty)){
		jAlert('error', "Please enter valid change qty!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		iserror =  false;
	}else if(!checkNumberOrNotofaddproduct(val)){
		jAlert('error', "Please enter valid total qty!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		iserror =  false;
	}if(adj_type=='0'){
		jAlert('error', "Please select adjustment type!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration);
		iserror =  false;
	}else if(enterqty=='' || enterqty=='0'){
		 jAlert('error', "Please enter change qty!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			iserror =  false;
	}else if(val==''){
		 jAlert('error', "Please enter change qty!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			iserror =  false;
	}else if(adj_comment==''){
		 jAlert('error', "Please enter remark!", 'Error Dialog');
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
			iserror =  false;
	}else{
		var qty = Number(val); 
		if(adj_type=='1'){
			if(qty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='2'){
			if(qty<=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be greater than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='3'){
			if(qty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}else if(adj_type=='4'){
			if(qty>=pre_stock){
				iserror =  false;
				jAlert('error', "Entered change qty must be less than original stock!", 'Error Dialog');
				setTimeout(function() {
					$("#popup_container").remove();
					removeAlertCss();
				}, alertmsgduration);
			}
		}
	}
	
	if(iserror){
			$("#dashboardloaderPopup").modal('hide');
			var x = confirm("Are You Sure!!");
			if(x){
				$("#dashboardloaderPopup").modal('show');
				document.getElementById("adjustmyform").submit();
			}
	 	   
	 	}
}

function checkNumberOrNotofaddproduct(inputtxt)
{
	 var numbers = /^[0-9]+$/;  
     
     if(inputtxt.match(numbers))  {
        return true;
     } else {
          return false;
     }
} 


function addtopurchaseque(){
	
	var test= false;
	if(document.getElementById("location")){
		var loctioid = document.getElementById("location").value;
		if(loctioid==''){
			test= true;
			
		}else if(loctioid=='0'){
			test= true;
			
		}
	}
	if(test){
		jAlert('error', "Please select location from top!", 'Error Dialog');
		setTimeout(function() {
			$("#popup_container").remove();
			removeAlertCss();
		}, alertmsgduration); 
	}else{
		 var ids="0";
	     $('.case').each(function() { 
				if(this.checked == true){
				    ids=ids+","+this.value;
				} 
										
		});
		var url="addtopurchasequeCatalogue?data="+ids+"";	
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			isIE = true;
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}   
	    req.onreadystatechange = addtopurchasequeRequest;
	    req.open("GET", url, true); 
	    req.send(null);  
	}		
}

function addtopurchasequeRequest(){
if (req.readyState == 4) {
		if (req.status == 200) {
			$('.case').each(function() { 
				if(this.checked == true){
					 this.checked = false;    
				} 
			});
			var str = req.responseText;
			var data = str.split("~");
			document.getElementById("addtopurchasebody").innerHTML = data[0];
			$('#addtopurchaseordermodel').modal( "show" );	
		 }
	}	 
}

function confirmaddtopurchaseque(){
	$("#dashboardloaderPopup").modal('show');
	var flag = false;
	var isslected = false;
 	$('.addtopoclass').each(function() {
 		isslected = true;
 		var tqty = document.getElementById("addtopo_qty"+this.value).value;
 		if(tqty=='0'){
 			jAlert('error', "Please enter quantity!", 'Error Dialog');	
 			setTimeout(function() {
 				$("#popup_container").remove();
 				removeAlertCss();
 			}, alertmsgduration); 
 			flag = true;	
 		}else if(!checkNumberOrNotofaddproduct(tqty)){
 			jAlert('error', "Please enter valid quantity!", 'Error Dialog');	
 			setTimeout(function() {
 				$("#popup_container").remove();
 				removeAlertCss();
 			}, alertmsgduration); 
 			flag = true;	
 		}
	});
 	if(!isslected){
 		$("#dashboardloaderPopup").modal('hide');
 		jAlert('error', "Please select at least one product!", 'Error Dialog');	
			setTimeout(function() {
				$("#popup_container").remove();
				removeAlertCss();
			}, alertmsgduration);
 	}else if (!flag){
		$("#dashboardloaderPopup").modal('hide');
		var x = confirm("Are You Sure!!");
		if(x){
			$("#dashboardloaderPopup").modal('show');
			document.getElementById("addtopurchaseorderbtn").innerHTML='';
			document.getElementById("addtopurchaseorderform").submit();
		}else{
			return false;
		}
  	}else{
  		$("#dashboardloaderPopup").modal('hide');
		return false;
	}
}