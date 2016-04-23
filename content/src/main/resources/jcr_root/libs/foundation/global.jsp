<%@page session="false" import="javax.jcr.*,
        org.apache.sling.api.resource.Resource,
        org.apache.sling.api.resource.ValueMap,
        java.util.ResourceBundle"
%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="yj" uri="http://yujingnet.com/osp/yj"%>
<sling:defineObjects />
<% ResourceBundle resourceBundle = slingRequest.getResourceBundle(slingRequest.getLocale()); %>
<%

    // add more initialization code here

%>