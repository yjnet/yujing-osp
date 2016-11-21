<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<link rel="stylesheet" href="/etc/clientlibs/yujing-osp/ui/tree-menu/css/jquery.treeview.css" />
	<script src="/etc/clientlibs/yujing-osp/ui/tree-menu/js/jquery.cookie.js" type="text/javascript"></script>
	<script src="/etc/clientlibs/yujing-osp/ui/tree-menu/js/jquery.treeview.js" type="text/javascript"></script>
	<script src="/etc/clientlibs/common/js/jquery.dataTables.min.js" type="text/javascript"></script>
	

<style>
.filetree span.file, .filetree span.folder {
	background-size: 12px;
}

div#content-menu {
	line-height: 12px;
	font-size: 90%;
	background: white;
    margin: 3px;
    padding: 3px 3px 3px 5px;
    height: 98%;
}
:target {
    border: 2px solid #D4D4D4;
    background-color: #e5eecc;
}
</style>
<script>
var $nav = jQuery.noConflict();
jQuery(document).ready(function() {
		
		$nav("div#content-menu").click(function() {
			
		//	listChildren($nav(this));
		});
		
		function listChildren(element) {
			var n = $nav(element).attr("path");
			$nav(element).empty();
			n = n+1;
			$nav(element).attr("path", n);
			$nav(element).text("<%= currentNode.getPath() %>");
			$nav("<div path=\"" + n + "\">test " + n + "</div>").appendTo(element);
		}
	});
	
	function setCookie(cname, cvalue, exdays) {
	    var d = new Date();
	    d.setTime(d.getTime() + (exdays*24*60*60*1000));
	    var expires = "expires="+ d.toUTCString();
	    document.cookie = cname + "=" + cvalue + "; " + expires;
	}
	
	function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length,c.length);
	        }
	    }
	    return "";
	}
	
	function checkCookie(cookieName) {
	    var cValue = getCookie(cookieName);
	    if (cValue != "") {
	        alert("get stored path: " + cValue);
	    } else {
	    	cValue = prompt("Please enter user's content path:", "");
	        if (user != "" && user != null) {
	            setCookie(cookieName, cValue, 1);
	        }
	    }
	}
</script>

<script>
//<!--
var $menu = jQuery.noConflict();
var navT = jQuery(document).ready(function(){
	   $menu.ajax({
		    url : "/content/.yj:page.xml",
		    type: "GET",
		    success: function(data, textStatus, jqXHR)
				    {
				    	if (jQuery.isXMLDoc(data)) {
				    		var elem = $menu("div#content-menu");
				    		elem.empty();
				    		elem.html((new XMLSerializer()).serializeToString(data));
				    	}
				    	else if (jQuery.type(data) == 'string') {
				        	$menu("div#content-menu").html(data);
				    	}
				    	else {
				    		alert('not support type: ' + jQuery.type(data));
				    	}
				    	setActions("#tree-menu-ui-id");
				    },
		    error: function (xhr, textStatus, errorThrown)
				    {
		    	if (false && xhr.responseText != "") {

		            var jsonResponseText = $.parseJSON(xhr.responseText);
		            var jsonResponseStatus = '';
		            var message = '';
		            $.each(jsonResponseText, function(name, val) {
		                if (name == "ResponseStatus") {
		                    jsonResponseStatus = $.parseJSON(JSON.stringify(val));
		                     $.each(jsonResponseStatus, function(name2, val2) {
		                         if (name2 == "Message") {
		                             message = val2;
		                         }
		                     });
		                }
		            });

		            alert(message);
		        }
		    		alert('error: ' + textStatus + ' - ' + xhr.responseText);
			    	}
		});
		
	   function valueFromCurrentOrJcrContent(data, name) {
		   var val = '';
		   if (data == null || data == 'undefined') {
			   return val;
		   }
		   
		   val = data[name]
		   if (val != null && val != 'undefined') {
			   return val;
		   }
		   
		   if (data['jcr:content'] != null && typeof data['jcr:content'] == 'object') {
			   return data['jcr:content'][name];
		   }
		   
		   return val;
	   }
	   
		function setActions(element) {
			$menu(element).treeview();
			
			$spans = $menu(element + " span").dblclick(function(e) {
				 e.preventDefault(); 
				 $spans.css("background-color", "");
				 $menu(this).css("background-color", "yellow");
				 navT.path = $menu(this).parent().attr("name");
				 navT.template = $menu(this).parent().attr("template");
				    
				 url = navT.path + ".html"; 
				 window.open(url, '_blank');   
			 });
			 
			var $cols = $menu(element + " span").click(function(e) {
			    $cols.css("background-color", "");
			    $menu(this).css("background-color", "yellow");
			    navT.path = $menu(this).parent().attr("name");
			    navT.template = $menu(this).parent().attr("template");
			    
			    var linkUrl ="/content.prop.json?statement=/" +  navT.path + "/element(*,yj:page)";
	            $menu.ajax({
			    url : linkUrl,
			    type: "GET",
			    success: function(data, textStatus, jqXHR)
					    {
				    	if (jQuery.type(data) == 'array') {
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
				    		$menu('div.ui-layout-center').html('<table id="siteadmin-table" class="table-display"></table>');
				    		
				    		 var table_config = {
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
                               };
				    		 $menu('#siteadmin-table').dataTable.ext.errMode = 'none';
                               var table = $menu('#siteadmin-table').DataTable(table_config);
                               table.clear();
                               table.rows.add(dataSet);
                               table.draw();
					    	}
					    	else if (jQuery.type(data) == 'string') {
					        	alert("wrong data format - string: " + data);
					    	}
					    	else {
					    		alert('not support type: ' + jQuery.type(data));
					    	}
					    },
			    error: function (jqXHR, textStatus, errorThrown)
					    {
			    			alert('error: ' + textStatus + ' - ' + jqXHR.responseText);
				    	}
				});
			});
		}
	});
	
	navT.path = null;
	navT.template = null;

//	-->
</script>

<div id="content-menu" class="yj-body-main-item" path="0">Web Content<div>child 1</div><div>child 2</div><span>child 3</span></div>
