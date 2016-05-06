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
  </style>
  
   <script>
  $(function() {
    $( "#dialog" ).dialog({
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 500
      }
    });
 
    $( "#opener" ).click(function() {
      $( "#dialog" ).dialog( "open" );
    });
    
    $( "#submit" ).click(function(e) {
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
    		  $( "#dialog" ).dialog( "close" );
    	  });       
      });
  });
  </script>
</head>
<body>
 
<div id="dialog" title="New Page" style="width: 300px">
	<form method="get">
		<div>
			<div class="table">
				<div class="tr">
					<div class="td table-cell">Title:</div>
					<div class="td table-cell" id="title"><input id="title-input" style="min-width: 40px;"></input></div>
				</div>
				<div class="tr">
					<div class="td table-cell">Path Name:</div>
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
	
		<button id="submit">Submit</button>
	</form>
</div>
 
<button id="opener">New Page</button>
 
  
  