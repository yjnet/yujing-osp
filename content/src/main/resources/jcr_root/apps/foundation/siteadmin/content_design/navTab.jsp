<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>

<div id="content-root">Web Content</div>

<script>
	$(document).ready(function() {
		alert('ready');
		
		$("div#content-root").click(function() {
			alert('click juery');
			listChildren($(this));
		});
		
		function listChildren(element) {
			alert('click listChildren');
			$("<div>test</div>").appendTo(element);
		
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