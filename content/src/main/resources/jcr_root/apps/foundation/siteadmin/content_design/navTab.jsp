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
		    error: function (jqXHR, textStatus, errorThrown)
				    {
		    			alert('error: ' + textStatus);
			    	}
		});
		
		function setActions(element) {
			$menu(element).treeview();
			
			$spans = $menu(element + " span").dblclick(function(e) {
				 e.preventDefault(); 
				 $spans.css("background-color", "");
				 $menu(this).css("background-color", "yellow");
				 navT.path = $menu(this).parent().attr("name");
				    
				 url = navT.path + ".html"; 
				 window.open(url, '_blank');   
			 });
			 
			var $cols = $menu(element + " span").click(function(e) {
			    $cols.css("background-color", "");
			    $menu(this).css("background-color", "yellow");
			    navT.path = $menu(this).parent().attr("name");
			    
			    var linkUrl ="/content.prop.json?statement=/" +  navT.path + "//*";
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
								row.push(data[i]['jcr:path']);
								row.push(data[i]['jcr:primaryType']);
								row.push(data[i]['jcr:score']);
								dataSet.push(row);
                              }
				    		$menu('div.ui-layout-center').html('<table id="siteadmin-table" class="table-display"></table>');
				    		
				    		 var table_config = {
                            		   columns: [
                                                 { title: "name" },
                                                 { title: "jcr:path" },
                                                 { title: "jcr:primaryType" },
                                                 { title: "jcr:score" }
                                             ]
                               };
                               var table = $menu('#siteadmin-table').DataTable(table_config);
                               table.clear();
                               table.rows.add(dataSet);
                               table.draw();
					    	}
					    	else if (jQuery.type(data) == 'string') {
					        	alert("string: " + data);
					    	}
					    	else {
					    		alert('not support type: ' + jQuery.type(data));
					    	}
					    },
			    error: function (jqXHR, textStatus, errorThrown)
					    {
			    			alert('error: ' + textStatus);
				    	}
				});
			});
		}
	});
	
	navT.path = null;

//	-->
</script>

<div id="content-menu" class="yj-body-main-item" path="0">Web Content<div>child 1</div><div>child 2</div><span>child 3</span></div>
