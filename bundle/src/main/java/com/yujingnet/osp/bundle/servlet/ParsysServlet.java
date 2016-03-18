package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.jcr.LoginException;
import javax.jcr.Node;
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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import com.yujingnet.osp.bundle.util.BundleConstant;
import com.yujingnet.osp.bundle.util.SlingUtil;

@Component(metatype = true)
@Service(Servlet.class)
@Properties({ @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = "insert"),
		@Property(name = "sling.servlet.extensions", value = "html"),
		@Property(name = "sling.servlet.methods", value = "GET") })
public class ParsysServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY) //(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY_UNARY)
	protected SlingRepository repository;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		
		
		Resource res = request.getResource();
		out.println("get Resource: " + (res==null));
		
		ValueMap properties = res.adaptTo(ValueMap.class);
		out.println("get properties size: " + properties.size());

		String resourcePath = res.getPath();
		out.println("get resourcePath: " + resourcePath);
		
//		// req is the SlingHttpServletRequest
//		ResourceResolver resourceResolver = request.getResourceResolver();
//		// Resource res = resourceResolver.getResource(resourcePath);
//
//		out.println("get resourceResolver: " + (resourceResolver));
/* 	get resourceResolver: org.apache.sling.resourceresolver.impl.ResourceResolverImpl@2a91daf8
	javax.jcr.RepositoryException: Not a relative path: /content
*/		
		Session session;
		try {
			session = SlingUtil.loginAdministrative(this.repository); //repository.login();
			out.println("get session: " + (session==null));
			
			Node rootNode = session.getRootNode();
			out.println("get rootNode: " + (rootNode==null));
			out.println("get rootNode name: " + rootNode.getName());
			out.println("get rootNode path: " + rootNode.getPath());
			
			if (resourcePath.startsWith("/"))
				resourcePath= resourcePath.substring(1);
			
			out.println("add node: " +resourcePath+"/_jcr_content");
			Node contentNode = rootNode.addNode(resourcePath+"/yj_content", BundleConstant.YJ_PAGE_CONTENT);
			
			String[] paths = resourcePath.split("/");
			out.println("get path: " + paths[paths.length-1]);
			
		
			contentNode.setProperty(BundleConstant.SLING_RESOURCE_TYPE, paths[paths.length-1]);
			out.println("set node property: ");
			Node componentNode = contentNode.addNode("my_component", BundleConstant.YJ_COMPONENT);
			componentNode.setProperty(BundleConstant.SLING_RESOURCE_TYPE, "ok");

			setLastModifiedStamp(contentNode);
			setLastModifiedStamp(componentNode);
			
			session.save();
//			session.logout();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(out);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(out);
		} // repository.loginAdministrative(null);

		out.println("Done: ");
	}
	
	private void setLastModifiedStamp(Node node) throws RepositoryException {
        Calendar todaysDate = Calendar.getInstance();
        todaysDate.setTime(new Date());
        node.setProperty("jcr:lastModified", todaysDate);
        node.setProperty("jcr:lastModifiedBy", "admin");
    }
}
