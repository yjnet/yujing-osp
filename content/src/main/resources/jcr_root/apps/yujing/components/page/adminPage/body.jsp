<%@ include file="/libs/foundation/global.jsp" %>
<body>
 <script src="/etc/widgets/components/jquery-2.1.3.min.js"></script>
  <script src="/etc/widgets/components/jquery-ui.min.js"></script>
  <script src="/etc/widgets/components/jstree.js"></script>
  <script src="/etc/widgets/components/parsys.js"></script>
  <script src="/etc/widgets/components/sidekick.js"></script>


  <p>This is a page with one embedded parsys (apps/yujing/components/page/adminPage/body.jsp).</p>

<!-- to include components drop on to pages -->
<%
	try {
		if ( currentNode.hasNode(currentNode.getPath()+"/par") ) {
		  sling.include(currentNode.getPath() + "/par");
		  sling.include(currentNode.getPath(), "replaceSelectors=footer");
		}
	} catch ( Exception e ) { }

try {
	if ( currentNode.hasNode(currentNode.getPath()+"/jcr:content") ) {
	  	sling.include(currentNode.getPath() + "/jcr:content");
	}
} catch ( Exception e ) { }
%>

  <link rel="stylesheet" href="/etc/widgets/components/jquery-ui.css" />
  <link rel="stylesheet" href="/etc/widgets/components/sidekick.css" />
  <link rel="stylesheet" href="/etc/widgets/components/parsys.css" />
  <link rel="stylesheet" href="/etc/widgets/components/themes/default/style.css" />
  
 




</body>