<%@include file="/libs/foundation/global.jsp" %>

<%
    String resourcePath = currentNode.getPath();
    Resource res = resourceResolver.getResource(resourcePath);
    ValueMap properties = res.adaptTo(ValueMap.class);
%>

<html>
<head>

	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
	<title>jQuery treeView</title>
	
	<link rel="stylesheet" href="/etc/clientlibs/yujing-osp/ui/tree-menu/css/jquery.treeview.css" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
	<script src="/etc/clientlibs/yujing-osp/ui/tree-menu/js/jquery.cookie.js" type="text/javascript"></script>
	<script src="/etc/clientlibs/yujing-osp/ui/tree-menu/js/jquery.treeview.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#browser").treeview({
				animated:"normal",
				persist: "cookie"
			});
			
			$("#samplebutton").click(function(){
				var branches = $("<li><span class='folder'>New Sublist</span><ul>" + 
					"<li><span class='file'>Item1</span></li>" + 
					"<li><span class='file'>Item2</span></li></ul></li>").appendTo("#browser");
				$("#browser").treeview({
					add: branches
				});
			});
		});
	</script>
	
	</head>
	
  <body>
   <h1><%= currentNode.getPath() %></h1>
   <h2><%= properties.get("title") %></h2>
   
   
<p><a href="#" id="samplebutton" onClick="return false">Click here</a> to add a New Folder node to the end of the existing tree</p>

<ul>
		<li><span class="folder">Folder 1</span>
			<ul id="browser" class="filetree">
				<li><span class="file">Item 1.1</span></li>
			</ul>
		</li>
		<li><span class="folder">Folder 2</span>
			<ul id="browser" class="filetree">
				<li><span class="folder">Subfolder 2.1</span>
					<ul id="folder21">
						<li><span class="file">File 2.1.1</span></li>
						<li><span class="file">File 2.1.2</span></li>
					</ul>
				</li>
				<li><span class="file">File 2.2</span></li>
			</ul>
		</li>
		<li class="closed"><span class="folder">Folder 3 (closed at start)</span>
			<ul id="browser" class="filetree">
				<li><span class="file">File 3.1</span></li>
			</ul>
		</li>
		<li><span class="file">File 4</span></li>
	</ul>
   
   
   <%
   javax.jcr.Session session = resourceResolver.adaptTo(javax.jcr.Session.class);
   javax.jcr.query.QueryManager queryManager = currentNode.getSession().getWorkspace().getQueryManager(); //session.getWorkspace().getQueryManager();

	    // query for all nodes with tag "JCR"
	    String queryStatement 
	   // = "/jcr:root/content/adaptto//*[tags='JCR']";
	   // = "SELECT * FROM nt:resource"; // CONTAINS(.,\'content\')
	   // = "SELECT * FROM nt:resource WHERE CONTAINS(jcr:path,\'content\') ";
	  //  = "SELECT * FROM yj:page";
	 //   = "/jcr:root//*[jcr:contains(., 's')] order by @jcr:score";
	 //   = "/jcr:root/content//* order by jcr:path";
	  //    = "/jcr:root/etc//*[ jcr:primaryType = 'sling:Folder' or jcr:primaryType = 'nt:file'] order by jcr:path ";
	  //    = "/jcr:root/etc//element[(*, nt:file)] order by jcr:path ";
	  = "/jcr:root//* order by jcr:path ";
	      

	    javax.jcr.query.Query query 
	    = queryManager.createQuery(queryStatement, javax.jcr.query.Query.XPATH);
	   // = queryManager.createQuery(queryStatement, javax.jcr.query.Query.SQL);

	    // iterate over results
	    javax.jcr.query.QueryResult result = query.execute();
	    javax.jcr.NodeIterator nodes = result.getNodes();
	   
	    StringBuilder output = new StringBuilder();
	    
	    String currentPath = "";
	    
	    while (nodes.hasNext()) {
	      javax.jcr.Node node = nodes.nextNode();
	      output.append("path=" + node.getPath() + "\n");
	    }
%>

<pre><%= output.toString() %></pre>
  </body>
</html>