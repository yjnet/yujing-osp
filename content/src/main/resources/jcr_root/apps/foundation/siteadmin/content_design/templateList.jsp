<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<script src="/etc/clientlibs/common/js/jquery-1.10.2.js"></script>
  
<style>
	div#new-page-templates {font-size: 90%;}
	div#new-page-templates .table    { display: table ;}
	div#new-page-templates .tr       { display: table-row;}
	div#new-page-templates .td, div#new-page-templates .th   { display: table-cell }
	
	div#new-page-templates div.td.table-cell {
		padding-top: 2px;
		padding-right: 3px;
        vertical-align: middle;
	}
    
    div#new-page-templates div.td.table-cell img {
        padding: 2px 0px 0px 5px;
        width: 30px;
        height: 30px;
	}
    
    div#new-page-templates div.td div.cell-title {
        font-weight: bold;
        padding-top: 6px;
    }
	
	div#new-page-templates div.table-cell input {
		min-width:60px;
	}
    
</style>
<div id="new-page-templates">
	<div class="table">
		<div class="tr">
			<div class="td table-cell">Title:</div>
			<div class="td table-cell" id="title"><input id="title-input" style="min-width: 40px;"></input></div>
		</div>
		<div class="tr">
			<div class="td table-cell">Name(path):</div>
			<div class="td table-cell" id="path-name"><input id="path-name-input"></input></div>
		</div>
	</div>
	<hr/>
	
	<div id="templates" class="table">
		<span style="margin-top: 10px; margin-bottom: 30px;">Select a template</span>
	</div>
	
	<script>
		var $tmpl = jQuery.noConflict();
		
		var $tmplChoice = $tmpl(document).ready(function() {
			$tmpl.ajaxSetup({async:false});
			var jqxhr = $tmpl.ajax( "/apps.prop.json?statement=/jcr:root/apps/*/templates/*&property=jcr:description&property=jcr:title&property=sling:resourceType" )
		  	  .done(function(data, textStatus, jqXHR) {
			  		for(var x=0; x<data.length; x++){
			  			if (data[x].name == ".content.xml" || data[x].name == "thumbnail.png") {
			  				continue;
			  			}
	                    var name = data[x].name;
	                    var jcr_title = data[x]['jcr:title'];
	                    var jcr_path = data[x]['jcr:path'];
	                    var jcr_description = data[x]['jcr:description'];
	                    var sling_resourceType = data[x]['sling:resourceType'];
	                    
	                    if (jcr_title == null || jcr_title == 'undefined') {
	                    	jcr_title = name.replace(/-/g, " ").replace(/_/g, " ");
	                    }
	                    
	                    if (jcr_description == null || jcr_description == 'undefined') {
	                    	jcr_description = "";
	                    }
	                    
                       	var template = $tmpl("<div class=\"tr\" resourceType=\"" + sling_resourceType + "\"></div>");
   	                    
   	                    var template1 =$tmpl("<div class=\"td table-cell\"></div>");
   	                    template1.append("<img src=\"" + jcr_path + "/thumbnail.png\" />");
   	                    template.append(template1);
   	                    
   	                    var template2 =$tmpl("<div></div>");
   	                    template2.append("<p class=\"cell-title\">" + jcr_title + "</p>");
   	                    template2.append("<p>" + jcr_description + "</p>");
   	                    template.append(template2);
   	                    
   	                    template.append("<div style=\"clear: both\"></div>");
   	                    
   	                    $tmpl("#templates").append(template);
	                }
			  	  })
			  	  .fail(function(jqXHR, textStatus, errorThrown) {
			  		  alert( "error" );
			  	  })
			  	  .always(function() {
			  		$tmpl("#templates").find("img").each(function(e) {
			  			var $img = $tmpl(this);
			  			var img_url = $img.attr("src");
			  			
			  			$tmpl.ajax({
			  			    url: img_url,
			  			    type:'HEAD',
			  			    error:
			  			        function(){
			  			    	$img.attr("src", "/etc/clientlibs/yujing-osp/ui/img/thumbnail.png");
			  			        },
			  			    success:
			  			        function(){
			  			            //do something cheerful :)
			  			        }
			  			});
			  		});
			
			  		
		  		    var $elem = $tmpl("div#templates.table div.tr").click(function() {
		  		        $elem.attr("style","");
		  		        $tmpl(this).attr("style", "background-color: yellow;");
		  		      	setTemplateChoice($tmpl(this).attr("resourceType"));
		  		    });
		  		    
			  	  });
		});
		
		$tmplChoice.selected = null;
	</script>
	
	<script type="text/javascript">
		function setTemplateChoice(sel) {
			$tmplChoice.selected = sel;
		}
	</script>
</div>
