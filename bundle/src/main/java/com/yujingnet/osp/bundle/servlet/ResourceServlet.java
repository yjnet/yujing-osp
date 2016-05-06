package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.yujingnet.osp.bundle.util.ContentNodeName;
import com.yujingnet.osp.bundle.util.ContentNodeType;
import com.yujingnet.osp.bundle.util.UiConstants;
import com.yujingnet.osp.bundle.xml.Div;
import com.yujingnet.osp.bundle.xml.Li;
import com.yujingnet.osp.bundle.xml.ObjectFactory;
import com.yujingnet.osp.bundle.xml.Span;
import com.yujingnet.osp.bundle.xml.Ul;

@Component(metatype = true)
@Service(Servlet.class)
@Properties({ @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = {"yj:page", "nt:file", "nt:resource", "all-page", "all-node"}),
		@Property(name = "sling.servlet.extensions", value = "xml"),
		@Property(name = "sling.servlet.methods", value = { "GET", "POST" }) })
public class ResourceServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private ResourceResolver resourceResolver;
	private static String defaultPath = "/content";
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getParameter("anchor-path");
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		Hashtable<Integer, Li> hash = new Hashtable<Integer, Li>();
		Hashtable<String, Div> rootDiv = new Hashtable<String, Div>();

		response.setContentType("text/xml,application/rss+xml");
		
		try {
			if (resourceResolverFactory == null)
				resourceResolver = request.getResourceResolver();
			else
				resourceResolver = resourceResolverFactory.getResourceResolver(null);
		} catch (LoginException e) {
			throw new RuntimeException(e.toString(), e);
		}

		Session session = resourceResolver.adaptTo(Session.class);
		String selected = request.getRequestPathInfo().getSelectorString();
		
		if (selected == null || selected.trim().length() == 0)
			selected = "nt:resource";
		if (selected.equals("all-page"))
			selected = ContentNodeType.YJ_PAGE;
		
		try {
			final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			String queryStatement = "/jcr:root" + appendGroupId(request, path)
				+ ((selected.equals("all-node"))? "/*":("/element(*, " + selected +")")) + " order by jcr:path";
			Query query = queryManager.createQuery(queryStatement, javax.jcr.query.Query.XPATH);

			QueryResult result = query.execute();
			NodeIterator nodes = result.getNodes();

			Div div = new Div();
			div.setClazz(UiConstants.TREE_MENU_CLASS_NAME);
			div.setId(UiConstants.TREE_MENU_ID);
			div.setName("/");
			div.setQueryStatement(queryStatement);
			rootDiv.put("", div);
			
			long number = 0l;
			while (nodes.hasNext()) {
				if(appendElement(hash, nodes.nextNode(), true, rootDiv))
					number++;
			}

			div.setNodeNumber(number);
			
			// use Marshaller to convert the object to XML
			Marshaller marshaller = context.createMarshaller();
//			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

			ObjectFactory objectFactory = new ObjectFactory();
			JAXBElement<Div> je = objectFactory.createDiv(div);
			marshaller.marshal(je, out);
		} catch (RepositoryException e1) {
			throw new RuntimeException(e1.toString(), e1);

		} catch (JAXBException e2) {
			throw new RuntimeException(e2.toString(), e2);
		}

		out.flush();
	}

	private String appendGroupId(SlingHttpServletRequest request, String path) {
		String id = "";
		String ends = request.getRequestPathInfo().getSelectorString();
		if (ends == null || ends.trim().length() == 0)
			ends = "/";
		else
			ends = "." + ends;
		
		String requestPath = (request.getPathInfo().length() > 0)? request.getPathInfo().substring(0, request.getPathInfo().lastIndexOf(ends)) : "";
		
		// find out node path for retrieving
		if (path == null || path.trim().length() == 0) {
			if (requestPath.length() == 0)
				path = defaultPath + id + "/";
			else
				path = requestPath;
		}
		else {
			path = ((path.trim().startsWith("/"))? "":(requestPath+"/")) + path.trim() + (path.trim().endsWith("/")? "":"/");
		}

		if (path.startsWith(defaultPath+"/") && !path.startsWith(defaultPath+id+"/")) {
			return defaultPath + id + path.substring(defaultPath.length()) ;
		}

		return path;
	}
	
	private boolean isDiv(int level) {
		return level < 2;
	}

	private boolean appendElement(final Hashtable<Integer, Li> hash, Node node, boolean isOrdered, final Hashtable<String, Div> rootDiv)
			throws RepositoryException {
		if (!isOrdered || hash == null || node == null || node.getPath() == null || rootDiv == null || rootDiv.get("") == null)
			throw new RuntimeException("Get unsopport Node type");
		
		if (node.getPath() == null || node.getPath().equals("/") || node.getPath().startsWith("/jcr:system/"))
			return false;

		String[] paths = node.getPath().split("/");
		
		
		int hashIndex = 0;
		StringBuffer path = new StringBuffer();
		String parentPath = "";

		for (int i=0; i < paths.length; i++) {
			if (paths[i]==null || paths[i].trim().length() == 0)
				continue;

			hashIndex++;
			
			parentPath = path.toString();
			path.append("/" + paths[i]);
				
			if (isDiv(hashIndex) && rootDiv.containsKey(path.toString()))
				continue;
			else if (!isDiv(hashIndex) && hash.containsKey(hashIndex)) {
				if (hash.get(hashIndex) != null && path.toString().equals(hash.get(hashIndex).getName())) {
					continue;
				}
			
				hash.remove(hashIndex);
			}
			
			if (hash.containsKey(hashIndex+1)) {
				hash.remove(hashIndex+1);
			}

			if (isDiv(hashIndex)) {
				if (!rootDiv.containsKey(path.toString())) {
					Div parentDiv = rootDiv.get(parentPath);
					
					if (parentDiv == null)
						throw new RuntimeException("Missing div element for name: " + parentPath);
					
					// set <span>
					Span span = new Span();
					String type = resourceResolver.getResource(path.toString()).getResourceType();
					span.setValue(paths[i]);
					if (ContentNodeType.isFileType(type))
						span.setClazz(UiConstants.TREE_MENUE_CLASS_FOR_FILE_ICON);
					else
						span.setClazz(UiConstants.TREE_MENUE_CLASS_FOR_FOLDER_ICON);
					
					span.setValue(paths[i]);
					
					// set <div>
					Div div = new Div();
					div.setName(path.toString());
					div.getDivOrUlOrSpan().add(span);
					parentDiv.getDivOrUlOrSpan().add(div);
					
					rootDiv.put(path.toString(), div);
				}
				
				continue;
			}
			
			// set <li>
			Li li = new Li();
			li.setName(path.toString());
			
			// add <span> to <li>
			Span span = new Span();
			String type = resourceResolver.getResource(path.toString()).adaptTo(Node.class).getPrimaryNodeType().getName();
			span.setValue(paths[i]);
			if (ContentNodeType.isFileType(type))
				span.setClazz(UiConstants.TREE_MENUE_CLASS_FOR_FILE_ICON);
			else
				span.setClazz(UiConstants.TREE_MENUE_CLASS_FOR_FOLDER_ICON);
			li.getSpanOrUl().add(span);
			
			// add <li> to <div>
			if (isDiv(hashIndex-1)) {
				Div div;
				if (rootDiv.containsKey(parentPath))
					div = rootDiv.get(parentPath);
				else {
					div = new Div();
					div.setName(parentPath);
					rootDiv.put(parentPath, div);
				}
				
				Ul ul = new Ul();
				ul.setName(path.toString()+"/");
				ul.getLi().add(li);
				
				div.getDivOrUlOrSpan().add(ul);
				hash.put(hashIndex, li);
				continue;
			}
				
			// set <ul>
			Ul ul = null;
			
			// if parent contains <ul>, get it
			if (hash.containsKey(hashIndex-1)) {
				List<Object> list = hash.get(hashIndex-1).getSpanOrUl();

				for (int x=0; x<list.size(); x++) {
					if (list.get(x) instanceof Ul) {
						ul = (Ul) list.get(x);
					}
				}
			}
				
			// if parent doesn't have it, create a new one and add it to parent
			if (ul == null) {
				ul = new Ul();
				ul.setName(path.toString() + "/");
				if (hash.containsKey(hashIndex-1)) {
					hash.get(hashIndex-1).getSpanOrUl().add(ul);
				} else if (rootDiv.containsKey(parentPath))
					rootDiv.get(parentPath).getDivOrUlOrSpan().add(ul);
				
				else
					throw new RuntimeException("Missing parent div element for name: " + parentPath);
			}

			ul.getLi().add(li);
			hash.put(hashIndex, li);
		}
		
		return true;
	}

}
