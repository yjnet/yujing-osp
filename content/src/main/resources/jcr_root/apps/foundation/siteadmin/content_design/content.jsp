<script src="/etc/clientlibs/common/js/jquery-1.12.0.min.js" type="text/javascript"></script>
<script src="/etc/clientlibs/common/js/jquery.dataTables.min.js" type="text/javascript"></script>
<style>
div.content-main {
	line-height: 12px;
	font-size: 100%;
	background: #e6f0ff;
    margin: 3px;
    padding: 15px 15px 10px 15px;
}
table.dataTable thead th, table.dataTable thead td, table.dataTable.table-display tbody td {
	padding: 2px 2px 2px 2px;
	padding-left: 5px;
	white-space: nowrap;
}

table.table-display {
	font-size: inherit;
	padding: inherit;
	font-size: 95%;
}

table.table-display th {
	padding: inherit;
	padding-left: 10px;
}

div.dataTables_info {
	margin-top: 5px;
	font-size: 85%;
}

a.paginate_button, div.dataTables_paginate, div.paging_simple_numbers {
	font-size: 95%;
	padding: 0.2em 0.3em !important;
	margin-top: 3px;
}

table.table-display tr:nth-child(even) {
	background: #E0E0F8; 
	background-color: #E0E0F8;
}
</style>


<div class="content-main yj-body-main-item">
<div>
	
    <script>
    	var $prop = jQuery.noConflict();
    	
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
    	
         $prop(document).ready(function() {
                $prop.ajax({
                     url : "/content.prop.json?statement=//content/element(*,yj:page)",
                     type: "GET",
                     async: false,
                     success: function(data, textStatus, jqXHR)
                         {
                    	 	$menu('div.ui-layout-center').html('<table id="siteadmin-table" class="table-display"></table>');
                    	 
                    	 	if (data != undefined && data.length > 0) {
	                     		var dataSet = [];
	                             for (var i in data) 
	                             { 
	                             	row = [];
	                             	row.push(data[i]['name']);
	                             	row.push(valueFromCurrentOrJcrContent(data[i], 'jcr:title'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'lastPublished'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'jcr:lastModified'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'jcr:lastModifiedBy'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'status'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'yj:template'));
									row.push(valueFromCurrentOrJcrContent(data[i], 'impressions'));
									row.push(data[i]['path']);
									row.push(valueFromCurrentOrJcrContent(data[i], 'sling:resourceType'));
									dataSet.push(row);
	                             }
	                             $prop('#siteadmin-table').dataTable.ext.errMode = 'none';
	                             $prop('#siteadmin-table').DataTable( {
	                                 data: dataSet,
	                                 columns: [
                                               { title: "Name" },
                                               { title: "Title" },
                                               { title: "Publish" },
                                               { title: "Modified" },
                                               { title: "Author" },
                                               { title: "Status" },
                                               { title: "Template" },
                                               { title: "Impressions" },
                                               { title: "Path" },
                                               { title: "Type" }
                                           ]
	                             } );
                    	 	}
                             
                         },
                     error: function (jqXHR, textStatus, errorThrown)
                         {
                             alert('Error: ' + textStatus + ', jqXHR: ' + jqXHR.responseText );
                             //$prop("div#tree-menu").html('rendering content menu failed - ' + textStatus);
 			    	    }
 		          });
           } );
</script>
    
</div>


    

<!-- DIVs for the INNER LAYOUT -->

<div class="ui-layout-center">
	<table id="siteadmin-table" class="table-display"></table>
</div>

</div> 