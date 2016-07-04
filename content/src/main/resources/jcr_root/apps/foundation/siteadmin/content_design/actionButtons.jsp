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
		text-weight: bold;
		padding-top: 10px;
		padding-right: 20px;
	}
	
	div.table-cell input {
		min-width:80px;
	}
	
	.dialog {
	    -moz-border-radius: 0 0 8px 8px;
	    -webkit-border-radius: 0 0 8px 8px;
	    border-radius: 0 0 8px 8px; border-width: 0 8px 8px 8px;
	}
  </style>
  
   <script>
  $(function() {
	  var $firstSelectedNode = null;
	  var $dialogMsg = "";
	  var $isCut = false;
	  
	 function displayMsg(msg) {
		 $("div#msg-dialog-id span").html(msg);
		 $("div#msg-dialog-id").dialog( "open" );
	 };
	 
    $( ".dialog" ).dialog({
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 500
      }
    });
    
    $('.auto-close-dialog').dialog({
        autoOpen: false,
        show: "blind",
        hide: "blind",
        modal: true,
        open: function(event, ui) {
            setTimeout(function(){
                $('.auto-close-dialog').dialog('close');                
            }, 3000);
        }
    });
    
 
    $( "#create" ).click(function() {
    	if (navT.path == null) {
    		displayMsg("Please indicate where to create a new page");
    	}else {
    		$("#create-dialog").dialog( "open" );
    	}
    	
    	
    });
    
    $( "#delete" ).click(function() {
    	if (navT.path == null) {
    		displayMsg("Please select a page to delele");
    	}
    	else {
    		$("#del-navT-path").html(navT.path);
        	$( "#delete-dialog" ).dialog( "open" );
    	}
      });
    
    $( "#copy" ).click(function() {
    	if (navT.path == null) {
    		displayMsg("Please select a page to copy");
    	}
    	else {
	    	$firstSelectedNode = navT.path;
	    	$isCut = false;
	    	$("#copy-navT-path").html($firstSelectedNode);
	        $( "#copy-dialog" ).dialog( "open" );
    	}
      });
    
    $( "#cut" ).click(function() {
    	if (navT.path == null) {
    		displayMsg("Please select a page to cut");
    	}
    	else {
    		$firstSelectedNode = navT.path;
    		$isCut = true;
	    	$("#cut-navT-path").html($firstSelectedNode);
	        $( "#cut-dialog" ).dialog( "open" );
    	}
      });
    
    $( "#paste" ).click(function() {
    	if ($firstSelectedNode == null) {
    		displayMsg("Please select a page for copy or paste at first");
    	}
    	else if ($firstSelectedNode == navT.path) {
    		displayMsg("Please select a place to paste");
    	}
    	else {
	    	$("#from-navT-path").html($firstSelectedNode);
	    	$("#to-navT-path").html(navT.path);
	        $( "#paste-dialog" ).dialog( "open" );
	    }
      });
    
    $( "#create-submit" ).click(function(e) {
    	//e.preventDefault(); 
    	var path = $(this).parent().find("#path-name-input").val().trim();
    	var qstring = "jcr:title=" + encodeURIComponent($(this).parent().find("#title-input").val().trim());
    	
    	if (path.length == 0) {
    		path = $(this).parent().find("#title-input").val().trim().replace(/ /g, "_");
    	}
    	else {
    		qstring += "&jcr:path=" + encodeURIComponent(path);
    	}
    	
    	qstring += "&sling:resourceType=" + "foundation/components/page/defaul-template";
    	 
    	var jqxhr = $.ajax( navT.path + "/" + path + ".create.node?" + qstring )
    	  .done(function() {
    	    alert( "success" );
    	  })
    	  .fail(function() {
    	    alert( "error" );
    	  })
    	  .always(function() {
    		  $( "#create-dialog" ).dialog( "close" );
    	  });       
      });
    
    $("#delete-confirm").click(function() {
    	var jqxhr = $.ajax( navT.path + ".delete.node")
	  	  .done(function() {
	  	    alert( "success" );
	  	  })
	  	  .fail(function() {
	  	    alert( "error" );
	  	  })
	  	  .always(function() {
	  		  $( "#delete-dialog" ).dialog( "close" );
	  	  });  
    });
    
    $("#delete-cancel").click(function() {
		$( "#delete-dialog" ).dialog( "close" );
    });
    
    $("#paste-confirm").click(function() {
    	selector = ($isCut)? ".move":".copy";
    	
    	var jqxhr = $.ajax(navT.path + selector + ".node?yj:oldPath=" + $firstSelectedNode)
	  	  .done(function() {
	  		if ($isCut) {
	  			$firstSelectedNode = null;
			}
	  		
	  	    alert( "success" );
	  	  })
	  	  .fail(function() {
	  	    alert( "error" );
	  	  })
	  	  .always(function() {
	  		  $( "#paste-dialog" ).dialog( "close" );
	  	  });  
    });
    
    $("#paste-cancel").click(function() {
		$( "#paste-dialog" ).dialog( "close" );
    });
    
  });
  </script>
</head>
<body>
<div style="display: none">
	<div id="create-dialog" class="dialog" title="New Page" style="width: 300px">
		<form method="get">
			<div>
				<div class="table">
					<div class="tr">
						<div class="td table-cell">Title:</div>
						<div class="td table-cell" id="title"><input id="title-input" style="min-width: 40px;"></input></div>
					</div>
					<div class="tr">
						<div class="td table-cell">Name:</div>
						<div class="td table-cell" id="path-name"><input id="path-name-input"></input></div>
					</div>
				</div>
				
				<div class="table">
					<div class="tr">
						<div class="td table-cell">
							<img src="/etc/clientlibs/yujing-osp/ui/img/template-default-thumbnail.png" />
						</div>
						<div class="td table-cell">
							<p class="cell-title">Default template</p>
							<p>basic template</p>
						</div>
						<div style="clear: both"></div>
					</div>
				</div>
			</div>
		
			<button id="create-submit">Submit</button>
		</form>
	</div>
	
	<div id="msg-dialog-id" class="dialog auto-close-dialog" title="New Page" style="width: 300px">
		<span></span>
	</div>
	
	<div id="delete-dialog" class="dialog" title="Delete Confirmation" style="width: 300px">
		<div>Want to delete <span id="del-navT-path"></span> ?</div>
		
		<button id="delete-confirm">Yes</button> <button id="delete-cancel">No</button>
	</div>
	
	<div id="copy-dialog" class="auto-close-dialog" style="width: 300px">
		<div>Copy <span id="copy-navT-path"></span> ... </div>
	</div>
	
	<div id="cut-dialog" class="auto-close-dialog" style="width: 300px">
		<div>Cut <span id="cut-navT-path"></span> ... </div>
	</div>
	
	<div id="paste-dialog" class="dialog" title="Paste Confirmation" style="width: 300px">
		<div>Paste <span id="from-navT-path"></span> to <span id="to-navT-path"></span></div>
		
		<button id="paste-confirm">Yes</button> <button id="paste-cancel">No</button>
	</div>
</div>

<button id="create">New Page</button>
<button id="delete">Delete</button>
<button id="copy">Copy</button>
<button id="cut">Cut</button>
<button id="paste">Paste</button>
   
  