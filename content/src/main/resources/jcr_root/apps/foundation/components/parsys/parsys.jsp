<%@page import="java.util.Iterator"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %>
<%@ include file="/libs/foundation/global.jsp" %>
<link href="parsys.css"/>

<div class='parsys-drop-zone' data-resource-path="<%= resource.getPath() %>">

  <%
    if (resource != null) {
    	Iterator<Resource> nodes = slingRequest.getResourceResolver().listChildren(resource);

      if (nodes != null && nodes.hasNext()) {
        while (nodes.hasNext()) {
        	Resource child = nodes.next();
            %>
              <div data-resource-path="<%= child.getPath() %>">
                <% sling.include(child); %>
              </div>
            <%
        }
      }
    }
  %>
  
  <div><sling:include resourceType="/libs/foundation/components/parsys" /></div>
</div>