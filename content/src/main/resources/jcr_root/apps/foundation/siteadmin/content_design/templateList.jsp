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
    
	.yj-template-item {
	    border-color: #FFFFFF rgb(255, 255, 255) rgb(238, 238, 238);
	    border-style: solid;
	    border-width: 1px;
	    color: #555555;
	    font-family: tahoma,arial,helvetica,sans-serif;
	    font-size: 11px;
	    /* font-size-adjust: none; */
	    font-stretch: normal;
	    font-style: normal;
	    font-variant: normal;
	    font-weight: normal;
	    line-height: normal;
	    padding: 7px 7px;
	    white-space: normal;
	}
	
	.yj-template-thumbnail {
	    float: left;
	    margin-right: 7px;
	    max-width: 128px;
	    width:50px;
	    height: 50px
	}
	
	.yj-template-title {
	    font-weight: bold;
	    white-space: nowrap;
	}
	
	.yj-template-description {
	    font-style: italic;
	}
	
	.yj-x-view-selected {
	    /* border-color: #d0d0d0; */
	    background: #CCCCCC no-repeat right bottom;
	    border: 1px solid;
	    border-radius: 10px;
	}
	    
	.yj-template-view {
	    padding: 4px;
	    /* position: absolute; */
	    border: 1px solid #B5B8C8;
	}
	
	.yj-x-panel {
	    border-color: #d0d0d0;
	}
	    
	.yj-x-panel-noborder {
	    border-color: #d0d0d0;
	    border-width: 0px;
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
	
	<div id="templates" class="yj-template-view yj-x-panel">
		<span style="margin-top: 10px; margin-bottom: 30px;">Select a template</span>
	</div>
	
	<script>
	var $selectedTemplate = null;
		var $tmpl = jQuery.noConflict();
		
		var $tmplChoice = $tmpl(document).ready(function() {
			var $resourceTypes = new Object();
			var $templateDiv = $tmpl("#templates");
			
			$tmpl.ajaxSetup({async:false});
			var jqxhr = $tmpl.ajax( "/apps.prop.json?statement=/jcr:root/apps/*/templates/*&property=jcr:description&property=jcr:title&property=template" )
		  	  .done(function(data, textStatus, jqXHR) {
			  		for(var x=0; x<data.length; x++){
			  			if (data[x].name == ".content.xml" || data[x].name == "thumbnail.png") {
			  				continue;
			  			}
	                    var name = data[x].name;
	                    var jcr_title = data[x]['jcr:title'];
	                    var jcr_path = data[x]['jcr:path'];
	                    var jcr_description = data[x]['jcr:description'];
	                    var sling_resourceType = data[x]['template'];
	                    
	                    if (jcr_title == null || jcr_title == 'undefined') {
	                    	jcr_title = name.replace(/-/g, " ").replace(/_/g, " ");
	                    }
	                    
	                    if (jcr_description == null || jcr_description == 'undefined') {
	                    	jcr_description = "";
	                    }
	                    
	                    $resourceTypes[x] = x;
	                    
                       	var template = $tmpl("<div class=\"yj-template-item" + ((x==0)? " yj-x-view-selected":"") + "\"></div>");
                       	template.append("<img class=\"yj-template-thumbnail\" src=\"" + jcr_path + "/thumbnail.png\" style=\"width:45px; height: 45px\"/>");
                       	template.append("<div class=\"yj-template-title\">" + jcr_title + "</div>");
                       	template.append("<div class=\"yj-template-description\">" + jcr_description + "</div>");
                       	template.append("<div style=\"clear:both\"></div>");
                       	
                       	$templateDiv.append(template);
	                }
			  	  })
			  	  .fail(function(jqXHR, textStatus, errorThrown) {
			  		  alert( "error" );
			  	  })
			  	  .always(function() {
			  		$selectedTemplate = $resourceTypes[0];
			  		
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
			
		  		    var $tempOpt = $tmpl("#templates div.yj-template-item").click(function(i, e) {
			  		    	$tempOpt.removeClass("yj-x-view-selected");
			  		    	$tmpl(this).addClass("yj-x-view-selected");
			  		    	setTemplateChoice($resourceTypes[$tmpl("#templates div.yj-template-item").index($tmpl(this))]);
			  		    });
		  		    
			  	  });
		});
		
	</script>
	
	<script type="text/javascript">
		function setTemplateChoice(sel) {
			$selectedTemplate = sel;
		}
	</script>
</div>
