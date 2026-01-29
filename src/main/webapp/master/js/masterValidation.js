$(document).ready(function(){
	var validator = $("#master_form").validate({
		
		onsubmit: true,
		rules : {
			occupation : {
				required : true
				
			},	
			
			jobTitle : {
				required : true
			},
			reference : {
				required : true
			}
			
			
		
		},
		messages : {
				
				
			occupation : {
			required : error.occupation.required,
			},	
			
			jobTitle : {
				required : error.jobTitle.required,
			},	
			reference : {
				required : error.reference.required,
			},	
			
		
	},
		
	});
	
	
	
	
});	