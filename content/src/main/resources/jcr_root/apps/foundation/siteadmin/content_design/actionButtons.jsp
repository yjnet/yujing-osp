<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<link rel="stylesheet" href="/etc/clientlibs/common/css/jquery-ui.css" />
  <script src="/etc/clientlibs/common/js/jquery-1.10.2.js"></script>
  <script src="/etc/clientlibs/common/js/jquery-ui-1.11.4.js"></script>
  
  <style>
	.table    { display: table; width: 100%;}
	.tr       { display: table-row }
	.thead    { display: table-header-group }
	.tbody    { display: table-row-group }
	.tfoot    { display: table-footer-group }
	.col      { display: table-column }
	.colgroup { display: table-column-group }
	.td, .th   { display: table-cell}
	.caption  { display: table-caption }
	
	div#templates {
		padding-top: 10px;
		padding-bottom: 20px;
	}
	
	div.table-cell input {
		min-width:80px;
	}
	
	div.td.table-cell {
		padding-top: 2px;
		padding-right: 2px;
        padding-left: 3px;  
        vertical-align: top;

	}
    
    
    div.td.template-thumbnail-cell, div.td.table-cell img {
        padding: 2px 2px 2px 2px;
        width: 50px;
        height: 50px;
	}
    
    div.td div.cell-title {
        font-weight: bold;
        font-variant-caps:titling-caps;
        padding-top: 1px;
        white-space: nowrap;
    }
	
    div.td div.cell-description {
        font-weight: normal;
        font-size: 85%;
        padding-top: 7px;
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
   var $template = 'foundation/components/page/defaul-template';
   
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
    		addtemp();
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
    	
    	qstring += "&yj:template=" + $template;
    	// alert('path: ' + navT.path + "/" + path + ".create.node?" + qstring);
    	var jqxhr = $.ajax( navT.path + "/" + path + ".create.node?" + qstring )
    	  .done(function() {
    	   // alert( "success" );
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
	  	  //  alert( "success" );
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
	  		
	  	   // alert( "success" );
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
  
//  $(document).ready(function() {
//	  var $elem = $("div#templates div.tr").click(function() {
//		  alert("click " + $(this).attr("id"));
//		  $elem.attr("style", "");
//		  $(this).attr("style", "background-color: yellow;");
//		  $template = $(this).attr("id");
//	  });
//  });
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
				
				<div id="templates" class="table">
				<!-- 
					<div class="tr" id="foundation/components/page/defaul-template">
						<div class="td table-cell template-thumbnail-cell">
							<img src="/etc/clientlibs/yujing-osp/ui/img/template-default-thumbnail.png"/>
						</div>
						<div class="td table-cell">
							<div class="th cell-title">Default template</div>
							<div class="cell-description">basic template</div>
						</div>
					</div>
				-->
					
				</div>
			</div>
		
			<button id="create-submit">Create</button>
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

<script>
var $templ = jQuery.noConflict();

function valueFromCurrentOrJcrContent(data, name) {
	   var val = '';
	   if (data == null || data == 'undefined') {
		   return val;
	   }
	   
	   val = data[name];
	   if (val != null && val != 'undefined') {
		   return val;
	   }

	   if (data['jcr:content'] != null && typeof data['jcr:content'] == 'object') {
		   return data['jcr:content'][name];
	   }
	   
	   return val;
}

 function addtemp() {
        $templ.ajax( {
             url : "/content.prop.json?statement=//apps/foundation/" + ((navT.template == null || navT.template == 'undefined')? "themes":"templates") + "/element(*,yj:template)",
             type: "GET",
             async: false,
             success: function(data, textStatus, jqXHR)
                 {
            	 var templates = $templ('div#templates');
            	 templates.empty();
            	 
         	 	if (data != undefined && data.length > 0) {
                      for (var i in data) 
                      { 
                    	  var $elem = jQuery('<div/>', { 
                    		  'class': 'tr', 
                    		  'id': data[i]['path'] });
                    
                      
	                      var $elem1 = jQuery('<div/>', { 
	                		  'class': 'td table-cell template-thumbnail-cell'});
	                	 
         	 	
		         	 	 var $img = jQuery('<img/>', { 
		          		  		'src': '/etc/clientlibs/yujing-osp/ui/img/template-default-thumbnail.png'});
			          	
                    	  
		         	 	var $elem2 = jQuery('<div/>', { 
	                		  'class': 'td table-cell'});
		         	 	
		         	 	var $elem21 = jQuery('<div/>', { 
	                		  'class': 'th cell-title'});
		         	 	$elem21.html(data[i]['jcr:title']);
		         	 	
		         	 	var $elem22 = jQuery('<div/>', {
	                		  'class': 'cell-description'});
		         	 	$elem22.html(data[i]['jcr:description']);
		         	 
					        $elem1.append($img);
					        $elem.append($elem1);
					                    	 
					        $elem2.append($elem21);
					        $elem2.append($elem22);
					        $elem.append($elem2);
                    	  templates.append($elem);
                    	  templates.append('<div style="clear: both"></div>');
                     	
                     	// alert(templates.html());
                      }
                      
                      var $elem = $("div#templates div.tr").click(function() {
                		  $elem.attr("style", "");
                		  $(this).attr("style", "background-color: yellow;");
                		  $template = $(this).attr("id");
                	  });
         	 	}
                     
                 },
             error: function (jqXHR, textStatus, errorThrown)
                 {
                     alert('Error: ' + textStatus + ', jqXHR: ' + jqXHR.responseText );
                     //$templ("div#tree-menu").html('rendering content menu failed - ' + textStatus);
		    	    }
        } );
 }
</script>
   
  