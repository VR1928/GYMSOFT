
<%@taglib uri="/struts-tags" prefix="s" %>

		<style>
		p {
    margin: 0 0 0px;
    font-size: 12px;
    font-weight: bold;
}
			.paddniltopase{
				padding-top:50px;
			}
			label {
    margin-bottom: 1px;
}
		
.setimg{
    width: 100%;
    margin-left: auto;
    margin-right: auto;
        height: 40px;
     }
     
      @media print
   {
      p {
    margin: 0 0 0px;
    font-size: 9px;
    font-weight: bold;
}
.setprintlbl{
	margin-top: 70px;
}

   }     
		</style>


<form action="saveemrgencydataDiaryMangent" method="post">
	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 setprintlbl paddinnil">
    	<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4">
    	<div class="form-group">
        			<label>Title</label><label class="text-danger">*</label>
					<s:textfield name="emrgency_title" cssClass="showToolTip form-control" data-toggle="tooltip"
						title="Enter Title" placeholder="Enter Title"
						 />
        			</div>
    	 </div>
    </div>
	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 setprintlbl paddinnil">
    	<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4">
    			<div class="form-group">
        			<label>Text</label><label class="text-danger">*</label>
					<s:textarea name="emrgency_data" cssClass="showToolTip form-control" data-toggle="tooltip"
						title="Enter Data" placeholder="Enter Data"/>
        			</div>
    	</div>
    	
    </div>
    
    <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 setprintlbl paddinnil">
    	<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4">
    			<div class="form-group">
        			<label class="text-danger">Note:</label>
					<label class="text-danger">Use ~ sign to show in next line in print. For eg. texting data~tested data.</label>
        			</div>
    	</div>
    	
    </div>
    
    <div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 setprintlbl paddinnil">
    	<div class="col-lg-4 col-md-4 col-xs-4 col-sm-4">
    	<div class="form-group">
        	<button type="submit" class="btn btn-primary" style="margin-left: 150px;">Save and print</button>
        </div>
    	 </div>
    </div>
</form>	
    	
<script>
function printDiv(divName) {
     var printContents = document.getElementById(divName).innerHTML;
     var originalContents = document.body.innerHTML;

     document.body.innerHTML = printContents;

     window.print();

     document.body.innerHTML = originalContents;
}
</script>
      	
       			
	