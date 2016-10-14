


//@ sourceURL=sizeLimitations.js


$(document).ready(function() {
	//Internationalize content
	document.getElementById("topbar1").innerHTML = window.i18n.msgStore['home'];
	document.getElementById("topbar2").innerHTML = window.i18n.msgStore['adminUI-SearchEngineAdmin'];
	document.getElementById("topbar3").innerHTML = window.i18n.msgStore['adminUI-SizeLimitation'];
	document.getElementById("labelhl").innerHTML = window.i18n.msgStore['labelhl']+" : ";
	document.getElementById("submithl").innerHTML = window.i18n.msgStore['confirm'];
	document.getElementById("hlname").innerHTML = window.i18n.msgStore['limitHL'];

	document.getElementById("labelindexhl").innerHTML = window.i18n.msgStore['labelindexhl']+" : ";
	document.getElementById("submitindexhl").innerHTML = window.i18n.msgStore['confirm'];
	document.getElementById("hlindexname").innerHTML = window.i18n.msgStore['limitindexHL'];
	
	//Disable the input and submit
	$('#submithl').attr("disabled", true);
	$('#maxhl').attr("disabled", true);
	//If the semaphore was for this page and the user leaves it release the semaphores
	//On refresh
	$(window).bind('beforeunload', function(){  								
		if(document.getElementById("submithl")!==null){
			if(!document.getElementById("submithl").getAttribute('disabled')){
				  cleanSem("hl.maxAnalyzedChars");
			}
		}
	 });
	$(window).bind('beforeunload', function(){  								
		if(document.getElementById("submitindexhl")!==null){
			if(!document.getElementById("submitindexhl").getAttribute('disabled')){
				  cleanSem("maxLength");
			}
		}
	 });
	//If the user loads an other page
	$("a").click(function(e){
		if(e.target.className==="ajax-link" || e.target.className==="ajax-link active-parent active"){
			if(document.getElementById("submithl")!==null){
				if(!document.getElementById("submithl").getAttribute('disabled')){
					  cleanSem("hl.maxAnalyzedChars");
				}
			}
		}
	});
	//Get hl.maxAmaxAnalyzedChars value
	get("hl.maxAnalyzedChars", "hl");
	
	//Sert the button to call the function set with the hl.maxAnalyzedChars parameter
	$("#submithl").click(function(e){
		e.preventDefault();
		set("hl.maxAnalyzedChars", "hl");
	});
	
	//If the user loads an other page
	$("a").click(function(e){
		if(e.target.className==="ajax-link" || e.target.className==="ajax-link active-parent active"){
			if(document.getElementById("submitindexhl")!==null){
				if(!document.getElementById("submitindexhl").getAttribute('disabled')){
					  cleanSem("maxLength");
				}
			}
		}
	});
	//Get hl.maxAmaxAnalyzedChars value
	get("maxLength", "indexhl");
	//Sert the button to call the function set with the hl.maxAnalyzedChars parameter
	$("#submitindexhl").click(function(e){
		e.preventDefault();
		set("maxLength", "indexhl");
	});
});
//Call the get function with the correct parameter
function get(typeConf, type){
	document.getElementById("max"+type).value = "";
	$.ajax({			//Ajax request to the doGet of the ModifyNodeContent servlet
        type: "GET",
        url: "./../admin/ModifyNodeContent",
        data : "type="+typeConf+"&attr=name",
        //if received a response from the server
        success: function( data, textStatus, jqXHR) {	
        	//If the semaphore was already acquired
        	if(data === "File already in use"){
        		//Print it and disable the input and submit
        		document.getElementById("answer"+type).innerHTML = window.i18n.msgStore['usedFile'];
        		$('#submit'+type).attr("disabled", true);
        		$('#max'+type).attr("disabled", true);
        	}//If they're was an error
        	else if(data.toString().indexOf("Error code : ")!==-1){
        		//print it and disable the input and submit
        		document.getElementById("globalAnswer").innerHTML = data;
        		$('#submit'+type).attr("disabled", true);
        		$('#max'+type).attr("disabled", true);
        	}else{		//else add the options to the select
        		document.getElementById("max"+type).value = data;    
        		$('#submit'+type).attr("disabled", false);
        		$('#max'+type).attr("disabled", false);
        	}
        }
 	});
}
function set(typeConf, type){
	var value = document.getElementById("max"+type).value;
	if(value>0 && value % 1 === 0){
		$.ajax({			//Ajax request to the doGet of the ModifyNodeContent servlet to modify the solrconfig file
        	type: "POST",
        	url: "./../admin/ModifyNodeContent",
        	data : "type="+typeConf+"&value="+value+"&attr=name",
        	//if received a response from the server
        	success: function( data, textStatus, jqXHR) {	
        		//If the semaphore was already acquired
        		if(data === "File already in use"){
        			//Print it and disable the input and submit
        			document.getElementById("answer"+type).innerHTML = window.i18n.msgStore['usedFile'];
        			$('#submit'+type).attr("disabled", true);
        			$('#max'+type).attr("disabled", true);
        		}//If they're was an error
        		else if(data.toString().indexOf("Error code : ")!==-1){        		
        			//print it and disable the input and submit
        			document.getElementById("globalAnswer").innerHTML = data;
        			$('#submit'+type).attr("disabled", true);
    	    		$('#max'+type).attr("disabled", true);
   		     	}else{		//else add the options to the select
   		     		document.getElementById("answer"+type).innerHTML = window.i18n.msgStore['modifDone']
    	    	}
    	    }
 		});
	}else{
		document.getElementById("answer"+type).innerHTML = window.i18n.msgStore['inputMust'];
	}
}
function cleanSem(type){
	$.ajax({			//Ajax request to the doGet of the ModifyNodeContent servlet to release the semaphore
        type: "GET",
        url: "./../admin/ModifyNodeContent",
        data : "sem=sem&type="+type
	 });
}
