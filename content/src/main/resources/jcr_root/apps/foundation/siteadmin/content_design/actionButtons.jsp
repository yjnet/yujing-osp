<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<link rel="stylesheet" href="/etc/clientlibs/common/css/jquery-ui.css" />
  <script src="/etc/clientlibs/common/js/jquery-1.10.2.js"></script>
  <script src="/etc/clientlibs/common/js/jquery-ui-1.11.4.js"></script>
  
  <style>
	.table    { display: table }
	.tr       { display: table-row }
	.thead    { display: table-header-group }
	.tbody    { display: table-row-group }
	.tfoot    { display: table-footer-group }
	.col      { display: table-column }
	.colgroup { display: table-column-group }
	.td, .th   { display: table-cell }
	.caption  { display: table-caption }
	
	.table-cell {
		padding-top: 10px;
		padding-right: 20px;
	}
	
	.cell-title {
		font-weight: bold;
	}
	
	div.table-cell input {
		min-width:80px;
	}
  </style>
  
   <script>
  $(function() {
	$preSelected = null;
	$isCopy = true;
	
    $( ".dialog" ).dialog({
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 500
      }
    });
    
    $(".auto-close-dialog").dialog({
    	autoOpen: false,
        modal: true,
        show: {
            effect: "blind",
            duration: 300
          },
        open: function(event, ui){
         setTimeout("$('div.auto-close-dialog').dialog('close')", 1500);
        }
    });
    
    // create actions
   $( "#create-page-button" ).click(function() {
    	if (navT.path == null) {
    		warningMessage("Please select a place for page to create");
    	}
    	else {
      		$( ".create-page-dialog" ).dialog( "open" );
    	}
    });
    
    $( "#submit-create" ).click(function(e) {
    	//e.preventDefault(); 
    	$form = $(this).parents("form#create-page-dialog-form");
    	path = $form.find("#path-name-input").val().trim();
    	qstring = "jcr:title=" + encodeURIComponent($form.find("#title-input").val().trim());
    	
    	if (path.length == 0) {
    		path = $form.find("#title-input").val().trim().replace(/ /g, "_");
    	}
    	else {
    		qstring += "&jcr:path=" + encodeURIComponent(path);
    	}
    	qstring += "&sling:resourceType=" + $tmplChoice.selected;
    	var jqxhr = $.ajax( navT.path + "/" + path + ".create.node?" + qstring )
    	  .done(function() {
    			showMessage( "success" );
    	    	navT.path = null;
    	  })
    	  .fail(function() {
    	    	alert( "error" );
    	  })
    	  .always(function() {
    			$( ".create-page-dialog" ).dialog( "close" );
    			$preSelected = null;
    	  });
    });
    
    $( "#cancel-create" ).click(function(e) {
    	$( ".create-page-dialog" ).dialog( "close" );
    });
    
    
    // delete actions
    $( "#delete-page-button" ).click(function() {
    	if (navT.path == null) {
    		warningMessage("Please select a page to delete");
    	}
    	else {
    		$( "div#delete-page-dialog-div span#delete-path" ).html(navT.path);
      		$( ".delete-page-dialog" ).dialog( "open" );
    	}
    });
    
    $( "#confirm-delete" ).click(function(e) {
    	var jqxhr = $.ajax( navT.path + ".delete.node?" )
    	  .done(function() {
    		  showMessage( "success" );
    		  navT.path = null;
    	  })
    	  .fail(function() {
    		  alert( "error" );
    	  })
    	  .always(function() {
    		  $( ".delete-page-dialog" ).dialog( "close" );
    		  $preSelected = null;
    	  });       
    });
    
    $( "#cancel-delete" ).click(function(e) {
    	$( ".delete-page-dialog" ).dialog( "close" );
    });
    
    
    // copy
    $( "#copy-page-button" ).click(function(e) {
    	if (navT.path == null) {
    		warningMessage("Please select a page for copy");
    	}
    	else {
	    	$preSelected = navT.path;
	    	$isCopy = true;
	    	showMessage("Copy " + $preSelected + "  ...");
    	}
    });
    
    
    // cut
    $( "#cut-page-button" ).click(function(e) {
    	if (navT.path == null) {
    		warningMessage("Please select a page for copy");
    	}
    	else {
	    	$preSelected = navT.path;
	    	$isCopy = false;
	    	showMessage("Cut " + $preSelected + "  ...");
    	}
    });
    
    
 	// paste actions
    $( "#paste-page-button" ).click(function() {
    	if ($preSelected == null) {
    			warningMessage("Please select a page to paste at first");
    	}
    	else if (navT.path == null) {
    		if ($isCopy){
    			warningMessage("Please tell where to copy");
    		}
    		else {
    			warningMessage("Please tell where to move");
    		}
    	}
    	else if (navT.path == $preSelected) {
    		warningMessage("Please select a new place for paste");
    	}
    	else {
    		$( "div#paste-page-dialog-div div#paste-message" ).html((($isCopy)? "Copy":"Move") + " from " + $preSelected + " to " + navT.path + " ?");
      		$( ".paste-page-dialog" ).dialog( "open" );
    	}
    });
    
    $( "#confirm-paste" ).click(function(e) {
    	actionMode = ($isCopy)? "copy":"move";
    	
    	var jqxhr = $.ajax( $preSelected + "." + actionMode + ".node?yj:newPath=" + navT.path)
    	  .done(function() {
    		  showMessage( actionMode + " succeed!" );
    		  navT.path = null;
    	  })
    	  .fail(function() {
    		  alert( actionMode + " failed." );
    	  })
    	  .always(function() {
    		  $( ".paste-page-dialog" ).dialog( "close" );
    		  $preSelected = null;
    	  });       
    });
    
    $( "#cancel-paste" ).click(function(e) {
    	$( ".delete-page-dialog" ).dialog( "close" );
    });
    
    
    // message box actions
    function warningMessage(msg) {
    	$( "div.auto-close-dialog").attr("title", "Warning");
    	$( "div.auto-close-dialog span#message-dialog-span" ).html(msg);
    	$( "div.auto-close-dialog" ).dialog( "open" );
    }
    
    function showMessage(msg) {
    	$( "div.auto-close-dialog").attr("title", "Message");
    	$( "div.auto-close-dialog span#message-dialog-span" ).html(msg);
    	$( "div.auto-close-dialog" ).dialog( "open" );
    }
  });
  </script>

<div style="display: none">
	<div class="auto-close-dialog" title="Tips">
		<span id="message-dialog-span"></span>
	</div>
	
	<div id="create-page-dialog-div" class="dialog create-page-dialog" title="New Page" style="width: 300px">
		<form id="create-page-dialog-form" method="get">
			<div>
				<sling:include replaceSelectors="content_design.templateList" />
			</div>
		
			<div style="margin-top: 30px; margin-right: 20px; margin-bottom: 10px; float: right;">
				<button id="submit-create">Submit</button>
				<button id="cancel-create">Cancel</button>
			</div>
		</form>
	</div>
	
	<div id="delete-page-dialog-div" class="dialog delete-page-dialog">
		<div>Delete <span id="delete-path"></span> ?</div>
		
		<div><button id="confirm-delete">Yes</button> <button id="cancel-delete">No</button></div>
	</div>
	
	<div id="paste-page-dialog-div" class="dialog paste-page-dialog">
		<div id="paste-message"></div>
		
		<div><button id="confirm-paste">Yes</button> <button id="cancel-paste">No</button></div>
	</div>
</div>

<button id="create-page-button">New Page</button>
<button id="delete-page-button">Delete</button>
<button id="copy-page-button">Copy</button>
<button id="cut-page-button">Cut</button>
<button id="paste-page-button">Paste</button>

  