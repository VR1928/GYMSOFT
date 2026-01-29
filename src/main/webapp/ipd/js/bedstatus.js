function changeBedtatus(id,status){
	var url = "statusBed?selectedid="+id+"&status="+status+" ";
	
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}   
               
    req.onreadystatechange = changeBedtatusREequest;
    req.open("GET", url, true); 
    
    req.send(null);

}

function changeBedtatusREequest(){
	if (req.readyState == 4) {
		if (req.status == 200) {
			
			
			goreferesh();
			
			//document.getElementById('alldataprisctable').innerHTML = req.responseText;
		}
	}

}