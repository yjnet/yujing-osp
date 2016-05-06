package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yujingnet.osp.bundle.service.NodeManagementService;
import com.yujingnet.osp.bundle.util.ContentNodePropertyName;

@Component
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = {"create", "update", "move", "copy", "delete", "activate", "inactivate"}),
		@Property(name = "sling.servlet.extensions", value = "node"),
		@Property(name = "sling.servlet.methods", value = { "GET", "POST" }) })
public class NodeManagementServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

//	@Reference
//    private SlingRepository slingRepository;
	
	/** Logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(NodeManagementServlet.class);
    
    @Reference
    NodeManagementService service;
	
    /** {@inheritDoc} */
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    	doService(request, response);
    }
 
    /** {@inheritDoc} */
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    	doService(request, response);
    }
    
    private void doService(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = response.getWriter();
    	out.println("get service not null? " + (service!=null));
    	try {
    		int len = request.getPathInfo().length() - (request.getRequestPathInfo().getSelectorString().length() + 1) - (request.getRequestPathInfo().getExtension().length() + 1);
    		Hashtable<String, Object> param = getParam(request);
    		String nodePath = request.getPathInfo().substring(0, len);
			String newPath = param.containsKey(ContentNodePropertyName.YJ_NEW_PATH)? param.get(ContentNodePropertyName.YJ_NEW_PATH).toString() : null;
			ResourceResolver resourceResolver = request.getResourceResolver();
			
			Calendar calendar = Calendar.getInstance();
			Hashtable<String, Object> properties = new Hashtable<String, Object>();
			properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED, calendar);
			properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED_BY, resourceResolver.getUserID());
			properties.put(ContentNodePropertyName.YJ_LAST_REPLICATED, calendar);
			properties.put(ContentNodePropertyName.YJ_LAST_REPLICATED_BY, resourceResolver.getUserID());
			
			switch (request.getRequestPathInfo().getSelectorString()) {
				case "create": 
					service.createContentNode(resourceResolver, nodePath, param);
					break;
				case "update": 
					service.updateNodeProperties(resourceResolver, nodePath, param);
					break;
				
				case "move": 
					service.changeNodeName(resourceResolver, nodePath, newPath);
					break;
			
				case "copy": 
					service.copyNode(resourceResolver, nodePath, newPath);
					break;
			
				case "delete": 
					service.deleteNode(resourceResolver, nodePath);
					break;
			
				case "activate": 
					properties.put(ContentNodePropertyName.YJ_LAST_REPLICATION_ACTION, ContentNodePropertyName.ACTIVATE_REPLICATION);
					service.updateNodeProperties(resourceResolver, nodePath, properties);
					break;
			
				case "inactivate": 
					properties.put(ContentNodePropertyName.YJ_LAST_REPLICATION_ACTION, ContentNodePropertyName.INACTIVATE_REPLICATION);
					service.updateNodeProperties(resourceResolver, nodePath, properties);
					break;
			
				default:
					throw new ServletException("Not support selection: " + request.getRequestPathInfo().getSelectorString());
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(out);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
    }
    
    private Hashtable<String, Object> getParam(SlingHttpServletRequest request) {
    	if (request == null)
    		return null;
    	
    	Hashtable<String, Object> param = new Hashtable<String, Object>();
    	
    	@SuppressWarnings("unchecked")
		Enumeration<Object> e = request.getParameterNames();
    	
    	while (e.hasMoreElements()) {
    		String name = (String)e.nextElement();
    		param.put(name, request.getParameter(name));
    	}
    	return param;
    }

}
