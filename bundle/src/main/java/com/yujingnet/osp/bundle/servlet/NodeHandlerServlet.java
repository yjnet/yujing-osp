package com.yujingnet.osp.bundle.servlet;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.jcr.JsonItemWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@SlingServlet(metatype = false, methods = { "POST", "GET" }, paths = { "/service/contentnode" })
public class NodeHandlerServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(NodeHandlerServlet.class);
	@Reference
	private ResourceResolverFactory factory;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();

		String nodePath = request.getParameter("nodePath");
		String processType = request.getParameter("processType");

		final ResourceResolver resolver = request.getResourceResolver();
		Resource resource = validateResource(resolver, nodePath, "yj:page");
		Node node = (resource == null) ? null : resource.adaptTo(Node.class);

		if (resource != null) {
			return ;
		}
		
		switch (processType) {
		case "add-component-node":
			Resource res = validateResource(resolver, (nodePath + (nodePath.endsWith("/") ? "" : "/") + "jcr:content"), "nt:unstructured");
			if (res != null && res.getPath().endsWith("/jcr:content")) {
				validateResource(resolver, (res.getPath() + "/par"), "nt:unstructured");
			}
			break;
		case "order-component-node":
			break;
		case "delete-component-node":
			break;
		case "update-component-node":
			break;

		default:

			RequestParameterMap parameterMap = request.getRequestParameterMap();

			/* Node properties to exclude from the JSON object. */
			final Set<String> propertiesToIgnore = new HashSet<String>() {
				private static final long serialVersionUID = 1L;

				{
					add("jcr:created");
					add("jcr:createdBy");
					add("jcr:versionHistory");
					add("jcr:predecessors");
					add("jcr:baseVersion");
					add("jcr:uuid");
				}
			};

			JsonItemWriter jsonWriter = new JsonItemWriter(propertiesToIgnore);

			try {
				/*
				 * Write the JSON to the PrintWriter with max recursion of 1
				 * level and tidy formatting.
				 */
				jsonWriter.dump(node, out, 1, true);
				response.setStatus(SlingHttpServletResponse.SC_OK);
			} catch (RepositoryException | JSONException e) {
				logger.error("Could not get JSON", e);
				response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	private Resource validateResource(ResourceResolver resolver, String path, String primaryType) {
		return validateResource(resolver, path, primaryType, new Properties());
	}

	private Resource validateResource(ResourceResolver resolver, String path, String primaryType, String[][] prop) {
		Properties p = new Properties();
		for (int i = 0; prop != null && i < prop.length; i++) {

			if (prop[i].length > 1) {
				p.put(prop[i][0], prop[i][1]);
			}
		}
		return validateResource(resolver, path, primaryType, p);
	}

	private Resource validateResource(ResourceResolver resolver, String path, String primaryType,
			Properties properties) {
		if (path == null) {
			return null;
		}

		Resource resource = resolver.getResource(path);
		Node node = (resource == null) ? null : resource.adaptTo(Node.class);

		if (resource == null && primaryType != null && path.lastIndexOf("/") > 0) {
			resource = resolver.getResource(path.substring(0, path.lastIndexOf("/")));
			node = (resource == null) ? null : resource.adaptTo(Node.class);

			if (node != null) {
				try {
					node.addNode(path.substring(1 + path.lastIndexOf("/")), primaryType);
					resource = resolver.getResource(path);
					node = (resource == null) ? null : resource.adaptTo(Node.class);
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				resource = null;
			}
		}

		if (properties != null && !properties.isEmpty() && resource != null && node != null) {
			Enumeration names = properties.propertyNames();

			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				if (properties.getProperty(name) != null) {
					try {
						node.setProperty(name, properties.getProperty(name));
					} catch (RepositoryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return resolver.getResource(path);

	}
}
