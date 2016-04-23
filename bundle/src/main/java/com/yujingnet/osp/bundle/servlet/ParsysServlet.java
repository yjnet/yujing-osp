package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
//import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import com.yujingnet.osp.bundle.util.ContentNodeName;
import com.yujingnet.osp.bundle.util.ContentNodePropertyName;
import com.yujingnet.osp.bundle.util.ContentNodeType;
import com.yujingnet.osp.bundle.util.SlingUtil;

@Component(metatype = true)
@Service(Servlet.class)
@Properties({ @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = "insert"),
		@Property(name = "sling.servlet.extensions", value = "page"),
		@Property(name = "sling.servlet.methods", value = "GET") })
public class ParsysServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY) //(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY_UNARY)
	protected SlingRepository repository;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		
//		showInfo(request, out);

		insertContentPageNode(out, request);

		out.println("Done: ");
	}

	private void insertContentPageNode(PrintWriter out, SlingHttpServletRequest request) {
		RequestPathInfo pathInfo = request.getRequestPathInfo();
		if (pathInfo == null || pathInfo.getSuffix() == null || pathInfo.getSuffix().replaceAll("/", "").replaceAll(" ", "").length() == 0)
			throw new RuntimeException("Invalid uri formatting - wrong suffix value: " + pathInfo.getSuffix());
		
		out.println("sling:resourceType: " + pathInfo.getResourcePath());
		
		Resource res = request.getResource();
		if (res == null || res.getPath() == null)
			throw new RuntimeException("Resource or Resource path missing");
		
		String resourcePath = res.getPath();
		if (resourcePath == null || !resourcePath.startsWith("/content/"))
			throw new RuntimeException("The web " + pathInfo.getExtension() + " must be under content directory");
		
		ValueMap properties = res.adaptTo(ValueMap.class);
		if (properties != null & properties.size() > 0) {
			Iterator<String> it = properties.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				out.println("Properties: " + key + ": " + properties.get(key));
			}
		}

		if (pathInfo.getSuffix() == null || pathInfo.getSuffix().replaceAll("/", "").replaceAll("-", "").replaceAll(":", "").replaceAll("_", "").trim().length()==0) {
			throw new RuntimeException("New " + pathInfo.getExtension() + " name not found");
		}
		
		out.println("getSuffix: " + pathInfo.getSuffix());
		String nodeName = pathInfo.getSuffix();
		if (nodeName.indexOf("?")>0)
			nodeName = nodeName.substring(0, nodeName.indexOf("?"));
		
		String[] paths = nodeName.split("/");
		for (int i = paths.length-1; i>=0; i--) {
			if (paths[i].trim().length() > 0) {
				nodeName = paths[i].trim().replaceAll(" ", "_").replaceAll(":", "_");
				break;
			}
		}
		
		out.println("Add node: " + resourcePath + "/" + nodeName);
		
		Session session = null;
		try {
			Date date = new Date();
			session =  SlingUtil.loginAdministrative(this.repository);
			if (session == null)
				throw new RuntimeException("Cannot login to SlingHttpServlet session");
			
			Node currentNode = session.getNode(resourcePath);
			out.println("get rootNode: " + (currentNode==null));
			out.println("get rootNode name: " + currentNode.getName());
			out.println("get rootNode path: " + currentNode.getPath());
			
			out.println("add a new node to: " + currentNode.getPath());
			Node pageNode = addNode(currentNode, nodeName, ContentNodeType.YJ_PAGE);
			
			out.println("add a new conent node to: " + pageNode.getPath());
			Node contentNode = addNode(pageNode, ContentNodeName.JCR_CONTENT, ContentNodeType.YJ_PAGE_CONTENT);
		
			contentNode.setProperty(ContentNodePropertyName.SLING_RESOURCE_TYPE, paths[paths.length-1]);
			out.println("set node property: ");
			
			Node componentNode = contentNode.addNode(ContentNodeName.getNodeName(contentNode, "apps/yujing/components/test", out));
			componentNode.setProperty(ContentNodePropertyName.SLING_RESOURCE_TYPE, "ok");

			setNodeProperties(contentNode, request);
			setNodeProperties(componentNode, request);
			
			session.save();
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(out);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(out);
		} // repository.loginAdministrative(null);
		finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	private Node addNode(Node node, String nodeName) throws RepositoryException {
		return addNode(node, nodeName, null);
	}
	
	private Node addNode(Node node, String nodeName, String nodeType) throws RepositoryException {
		if (node.hasNode(nodeName))
			return node.getNode(nodeName);
		
		if (nodeType == null || nodeType.trim().length() ==0)
			return node.addNode(nodeName);
		
		return node.addNode(nodeName, nodeType);
	}
	
	private void setNodeProperties(Node node, SlingHttpServletRequest request) throws RepositoryException {
        Calendar todaysDate = Calendar.getInstance();
        todaysDate.setTime(new Date());
        
        if (node.getProperty(ContentNodePropertyName.JCR_CREATED) == null)
        	node.setProperty(ContentNodePropertyName.JCR_CREATED, todaysDate);
        
        node.setProperty(ContentNodePropertyName.JCR_LAST_MODIFIED, todaysDate);
        
        if (request.getUserPrincipal() != null && request.getUserPrincipal().getName() != null)
        	node.setProperty(ContentNodePropertyName.JCR_CREATED_BY, request.getUserPrincipal().getName());
    }
	
	private void showInfo(SlingHttpServletRequest request, PrintWriter out) {
		RequestPathInfo info = request.getRequestPathInfo();
		out.println("info getExtension: " + info.getExtension());
		out.println("info getResourcePath: " + info.getResourcePath());
		out.println("info getSelectorString: " + info.getSelectorString());
		out.println("info getSuffix: " + info.getSuffix());
		out.println("info getSelectors length: " + info.getSelectors().length);
		
		out.println("request getAuthType: " + request.getAuthType());
		out.println("request getPathInfo: " + request.getPathInfo());
		out.println("request getPathTranslated: " + request.getPathTranslated());
		out.println("request getQueryString: " + request.getQueryString());
		out.println("request getPathTranslated: " + request.getPathTranslated());
		out.println("request getLocalAddr: " + request.getLocalAddr());
		out.println("request getRemoteAddr: " + request.getRemoteAddr());
		out.println("request getScheme: " + request.getScheme());
		out.println("request getServerName: " + request.getServerName());
		out.println("request getServletPath: " + request.getServletPath());
		out.println("request getContentType: " + request.getContentType());
		out.println("request getContextPath: " + request.getContextPath());
		out.println("request getResponseContentType: " + request.getResponseContentType());
		out.println("request getCharacterEncoding: " + request.getCharacterEncoding());
		out.println("request getContentLength: " + request.getContentLength());
		
		if (info.getSuffixResource() != null) {
		out.println("getSuffixResource().getName: " + info.getSuffixResource().getName());
		out.println("getSuffixResource().getPath: " + info.getSuffixResource().getPath());
		out.println("getResourceSuperType: " + info.getSuffixResource().getResourceSuperType());
		out.println("getResourceType: " + info.getSuffixResource().getResourceType());
		out.println("listChildren: " + info.getSuffixResource().listChildren().hasNext());
		out.println("getParent().getPath(): " + info.getSuffixResource().getParent().getPath());
		}
		
	}
	
	
}
