<%@page session="false"%>
<%@page import="java.util.HashSet,
                    java.util.Set,
                    com.day.cq.commons.jcr.JcrConstants,
                    com.day.cq.wcm.api.WCMMode,
                    com.day.cq.wcm.api.components.IncludeOptions,
                    com.day.cq.wcm.foundation.Paragraph,
                    com.day.cq.wcm.foundation.ParagraphSystem" %><%
%>
<%@include file="/libs/foundation/global.jsp"%><%
    
    ParagraphSystem parSys = ParagraphSystem.create(resource, slingRequest);
    String newType = resource.getResourceType() + "/new";

    boolean hasColumns = false;
    String idsClass = "";
	int breakCount = 0;
    for (Paragraph par: parSys.paragraphs()) {
           if (editContext != null) {
            editContext.setAttribute("currentResource", par);
        }
        idsClass = "";
        switch (par.getType()) {
            case START:
               breakCount = 0;
               if (hasColumns) {
                    // close in case missing END
                    %></div></div><%
                }
				 if (editContext != null) {
                    // draw 'edit' bar
                    Set<String> addedClasses = new HashSet<String>();
                    addedClasses.add("section");
                    addedClasses.add("colctrl-start");
                    IncludeOptions.getOptions(request, true).getCssClassNames().addAll(addedClasses);
                    %><sling:include resource="<%= par %>"/><%
                }
                // open outer div
                %><div class="row-fluid"><%// open column div
                     if (par.getBaseCssClass().equals("span2575")) {
                         idsClass= "span3";
                     }else if (par.getBaseCssClass().equals("span7525")) {
                         idsClass="span9";
                     }else if (par.getBaseCssClass().equals("span3367")) {
                         idsClass="span4";
                     }else if (par.getBaseCssClass().equals("span1633")) {
                         idsClass="span2";
	                 }else if (par.getBaseCssClass().equals("span6733")) {
	                     idsClass="span8";
	                 }%>
	              <div class="<%=par.getBaseCssClass()%> &nbsp; <%=idsClass%>"><%
                hasColumns = true;
                break;
            case BREAK:
                if (editContext != null) {
                    // draw 'new' bar
                    IncludeOptions.getOptions(request, true).getCssClassNames().add("section");
                    %><sling:include resource="<%= par %>" resourceType="<%= newType %>"/><%
                }
                ++breakCount;
                // open next column div
                          if (par.getBaseCssClass().equals("span2575")) {
                         idsClass= "span9";
                          } else if (par.getBaseCssClass().equals("span7525")) {
                              idsClass="span3";
                          } else if (par.getBaseCssClass().equals("span3367")) {
                              idsClass="span8";
                          }else if (par.getBaseCssClass().equals("span1633")) {
                        	  if (breakCount % 2 == 0) {
                                  idsClass="span2";
                        	  } else {
                                  idsClass="span4";
                        	  }
                 		  }else if (par.getBaseCssClass().equals("span6733")) {
                              idsClass="span4";
                  		  }
                %></div><div class="<%=par.getBaseCssClass()%> &nbsp; <%=idsClass%>"><%
                break;
            case END:
                if (editContext != null) {
                    // draw new bar
                    IncludeOptions.getOptions(request, true).getCssClassNames().add("section");
                    %><sling:include resource="<%= par %>" resourceType="<%= newType %>"/><%
                }
                if (hasColumns) {
                    // close divs and clear floating
                    %></div></div><div style="clear:both"></div><%
                    hasColumns = false;
                }
                if (editContext != null && WCMMode.fromRequest(request) == WCMMode.EDIT) {
                    // draw 'end' bar
                    IncludeOptions.getOptions(request, true).getCssClassNames().add("section");
                    %><sling:include resource="<%= par %>"/><%
                }
                break;
            case NORMAL:
                // include 'normal' paragraph
                IncludeOptions.getOptions(request, true).getCssClassNames().add("section");
                
                // draw anchor if needed
                if (currentStyle.get("drawAnchors", false)) {
                    String path = par.getPath();
                	path = path.substring(path.indexOf(JcrConstants.JCR_CONTENT)
                            + JcrConstants.JCR_CONTENT.length() + 1);
                	String anchorID = path.replace("/", "_").replace(":", "_");
                    %><a name="<%= anchorID %>" style="visibility:hidden"></a><%
                }
                %><sling:include resource="<%= par %>"/><%
                break;
        }
    }
    if (hasColumns) {
        // close divs in case END missing. and clear floating
        %></div></div><div style="clear:both"></div><%
    }
    if (editContext != null) {
        editContext.setAttribute("currentResource", null);
        // draw 'new' bar
        IncludeOptions.getOptions(request, true).getCssClassNames().add("section");
        %><cq:include path="*" resourceType="<%= newType %>"/><%
    }
%>
