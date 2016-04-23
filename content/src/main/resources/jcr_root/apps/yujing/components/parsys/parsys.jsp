<div class='parsys-drop-zone' data-resource-path="<%= resource.getPath() %>">
  <%
  String contentPath = currentNode.getPath();
  Resource contentResource = resourseResolve.getResource(getPath() + "/jcr:content");
  
    if (contentResource != null && contentResource.hasChild()) {
      NodeList nodes = request.getResourceResolver().listChildren(contentResource);

      if (nodes != null && nodes.hasNext()) {
        while (nodes.hasNext()) {
          var child = nodes.next();
            %>
              <div data-resource-path="<%= child.getPath() %>">
                <% sling.include(child); %>
              </div>
            <%
        }
      }
    }
  %>
  <div class="parsys-default-drop-zone ui-state-highlight" data-resource-path="default-drop-zone">Drop components here</div>
</div>