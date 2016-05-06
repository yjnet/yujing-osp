<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp" %>
<script type="text/javascript" src="/etc/clientlibs/common/js/jquery-2.1.1.min.js"></script>
<style>

.yj-body {
	height:100%;
}

.yj-body-header {
	height: auto;
}


div.yj-flex-container {
    display: -webkit-flex;
    display: -ms-flexbox;
    display: flex;
    width: 100%;
    -webkit-flex-direction: row;
    flex-direction: row;
    background-color: lightgrey;
    -webkit-flex-wrap: wrap;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
}

.yj-body-content {
	@include display-flex;
	@include flex-wrap(wrap);
 }
 
div.yj-flex-item {
    background-color: cornflowerblue;
    overflow-x: auto;
    line-height: 30px;
}

.yj-menu-item {
	width: 280px;
	white-space: nowrap;
	overflow-x: auto;
	float:left;
	resize: horizontal;
    overflow: auto;
    
    position: relative;
    
}

.yj-content-item {
	flex: 1;
}
.yj-info-item {
	max-width: 100px;
	float: right;
    margin-left: 2px;
}

.yj-body-footer {
	height: 80px;
}

.yj-dragbar{
   background-color: lightgrey;
   height:100%;
   float: left;
   width: 3px;
   cursor: col-resize;
}
</style>



<div class="yj-body">
	<div class="yj-body-header">
		<sling:include replaceSelectors="content_design.header" />
		<div style="clear: both;"></div>
	</div>
	<div id='test'></div>
	<div>
		<sling:include replaceSelectors="content_design.actionButtons" />
		<div style="clear: both;"></div>
	</div>
	<div class="yj-flex-container yj-body-content">
		<div class="yj-flex-item yj-menu-item">
			<sling:include replaceSelectors="content_design.navTab" />
			<div style="clear: both;"></div>
		</div>
		
		<div class="yj-flex-item yj-content-item">
			<sling:include replaceSelectors="content_design.content" />
			<div style="clear: both;"></div>
		</div>
		<div class="yj-flex-item yj-info-item">
			<sling:include replaceSelectors="content_design.ad" />
			<div style="clear: both;"></div>
		</div>
	</div>
	
	<div class="yj-body-footer">
		<sling:include replaceSelectors="content_design.footer" />
		<div style="clear: both;"></div>
	</div>
</div>

<script>
var $sbody = jQuery.noConflict();
function updateHieght(){
	var h = $sbody(".yj-body").innerHeight() - $sbody("div.yj-body-header").outerHeight() - $sbody("div.yj-body-footer").outerHeight() - 23;

	$sbody("#test").text( $sbody(window).height() + ": " + h + " = " + $sbody(".yj-body").innerHeight() + " - " + $sbody("div.yj-body-header").outerHeight() + " - " + $sbody("div.yj-body-footer").outerHeight());
	
	$sbody("div.yj-body-content").find('div.yj-menu-item').each(function() {
		$sbody(this).height(h);
	});
}

$sbody(document).ready(function() {
	updateHieght();
	$sbody(window).resize(function() {
		updateHieght();
	});
});


</script>