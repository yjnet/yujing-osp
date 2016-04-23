<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp" %>
<script type="text/javascript" src="/etc/clientlibs/common/js/jquery-2.1.1.min.js"></script>
<style>

.yj-body {
	height:100%;
}

.yj-body-header {
	height: 50px;
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
    margin: 2px;
    line-height:1in;
}

.yj-menu-item {
	width: 250px;
	white-space: nowrap;
	overflow-x: auto;
	float:left;
	resize: horizontal;
    overflow: auto;
    
    cursor: move;
    position: relative;
    
}

.yj-content-item {
	flex: 1;
}
.yj-info-item {
	max-width: 100px;
	float: right;
}

.yj-body-footer {
	height: 80px;
}
</style>



<div class="yj-body">
	<div class="yj-body-header">
		<sling:include replaceSelectors="content_design.header" />
	</div>
	<div id='test'></div>
	
	<div class="yj-flex-container yj-body-content">
		<div class="yj-flex-item yj-menu-item">
		
			sling:include replaceSelectors="content_design.navTab" 
		</div>
		<div class="yj-flex-item yj-content-item">
			main
		</div>
		<div class="yj-flex-item yj-info-item">
			info
		</div>
	</div>
	
	<div class="yj-body-footer">
		<sling:include replaceSelectors="content_design.footer" />
	</div>
</div>

<script>
function updateHieght(){
	var h = $(".yj-body").innerHeight() - $("div.yj-body-header").outerHeight() - $("div.yj-body-footer").outerHeight() - 18;

	$("#test").text( $(window).height() + ": " + h + " = " + $(".yj-body").innerHeight() + " - " + $("div.yj-body-header").outerHeight() + " - " + $("div.yj-body-footer").outerHeight());
	
	$(".yj-body-content").find('div.yj-flex-item').each(function() {
		
		$(this).height(h);
	});
}

$(document).ready(function() {
	updateHieght();
	$(window).resize(function() {
		updateHieght();
	});
});


</script>