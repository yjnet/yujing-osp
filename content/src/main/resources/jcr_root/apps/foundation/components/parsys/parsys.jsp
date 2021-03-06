<link href="parsys.css"/>

<div class='parsys-drop-zone' data-resource-path="<%= resource.getPath() %>">
  <%
    if (resource) {
      var nodes = request.resourceResolver.listChildren(resource);

      if (nodes && nodes.hasNext()) {
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
  <div class="place-holder parsys-default-drop-zone ui-state-highlight" data-resource-path="default-drop-zone">Drop components here</div>
</div>