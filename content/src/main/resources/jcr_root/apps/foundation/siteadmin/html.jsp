<%@include file="/libs/foundation/global.jsp" %>

<%
    String resourcePath = currentNode.getPath();
    Resource res = resourceResolver.getResource(resourcePath);
    ValueMap properties = res.adaptTo(ValueMap.class);
%>

<html>
<c:choose>
	<c:when test="${slingRequest.resourceResolver.userID != 'anonymous'}">
		<head>
			<sling:include replaceSelectors="content_design.head" />
		</head>
		<body>
			<sling:include replaceSelectors="content_design.body" />
		</body>
	</c:when>
	<c:otherwise>
		<%-- Not login use CDN for styling --%>
		<head>
			<title>Service Login</title>
			<link rel="stylesheet" href="/etc/clientlibs/common/css/bootstrap-3.3.1.min.css">
		</head>
		<body>
			<div>require login for user: "${slingRequest.resourceResolver.userID}"</div>
			<sling:include replaceSelectors="login" />
			<script src="/etc/clientlibs/common/js/jquery-2.1.1.min.js"></script>
			<script src="/etc/clientlibs/common/js/bootstrap-3.3.1.min.js"></script>
			<script>
			$("#loginModal").on('shown.bs.modal', function() {
				$('#login-form #j_username').focus();
			});
			$('#login-form').on('submit', function(event) {
				event.preventDefault();
				$.post('/j_security_check', $(this).serialize(), function(data) {
					$('#login-form .alert').addClass('hide');
					window.location.reload(true);
				}).fail(function() {
					$('#login-form .alert').removeClass('hide').fadeOut(500).fadeIn(1000);
					
				})
			});
			$('#loginModal').modal('show');
			</script>
		</body>
	</c:otherwise>
</c:choose>
</html>