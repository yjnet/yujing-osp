package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.apache.sling.jcr.resource.JcrResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yujingnet.osp.bundle.util.ContentNodePropertyName;

/**
 * A SlingSafeMethodsServlet that renders the search results as JSON data
 *
 * @scr.component immediate="true" metatype="no"
 * @scr.service interface="javax.servlet.Servlet"
 * 
 * @scr.property name="service.description" value="Default Query Servlet"
 * @scr.property name="service.vendor" value="The Apache Software Foundation"
 * 
 *               Use this as the default query servlet for json get requests for
 *               Sling
 * @scr.property name="sling.servlet.resourceTypes"
 *               value="sling/servlet/default"
 * @scr.property name="sling.servlet.extensions" value="json"
 * @scr.property name="sling.servlet.selectors" value="query"
 */
@Component
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Default Query Servlet"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = "json"),
		@Property(name = "sling.servlet.selectors", value = {"prop", "props"}),
		@Property(name = "sling.servlet.prefix", intValue = -1) })
public class JsonPropertiesQueryServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(JsonPropertiesQueryServlet.class);

	public static final String PATH = "path";
	public static final String SUBPATH = "subpath";
	
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	/** Search clause */
	public static final String STATEMENT = "statement";

	/** Query type */
	public static final String QUERY_TYPE = "queryType";

	/** Result set offset */
	public static final String OFFSET = "offset";

	/** Number of rows requested */
	public static final String ROWS = "rows";

	/** property to append to the result */
	public static final String PROPERTY = "property";

	/** exerpt lookup path */
	public static final String EXCEPT_PATH = "exceptPath";

	/** rep:exerpt */
	private static final String REP_EXCERPT = "rep:excerpt()";

	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException {
		String selector = req.getRequestPathInfo().getSelectorString();
		
		if (!"props".equals(selector)) {
			dumpResult(req, resp);
		}
		else {
			getProperties(req, resp);
		}
		
	}

	private void getProperties(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException {

		ResourceResolver resourceResolver = req.getResourceResolver();
		int postOffset = ((req.getRequestPathInfo().getExtension() == null) ? 0
				: (req.getRequestPathInfo().getExtension().length() + 1))
				+ ((req.getRequestPathInfo().getSelectorString() == null) ? 0
						: (req.getRequestPathInfo().getSelectorString().length() + 1));

		String resourcePath = (req.getParameter(PATH) != null) ? req.getParameter(PATH)
				: req.getPathInfo().substring(0, req.getPathInfo().length() - postOffset);


		resp.setContentType(req.getResponseContentType());
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Node-Path", resourcePath);

		long count = -1;
		if (req.getParameter(ROWS) != null) {
			count = Long.parseLong(req.getParameter(ROWS));
		}

		List<String> properties = new ArrayList<String>();
		if (req.getParameterValues(PROPERTY) != null) {
			for (String property : req.getParameterValues(PROPERTY)) {
				properties.add(property);
			}
		}

		String subPath = "jcr:content";
		if (req.getParameter(SUBPATH) != null) {
			subPath = req.getParameter(SUBPATH);
		}

		final JSONWriter w = new JSONWriter(resp.getWriter());
		
		try {
			w.object();
			replyJson(w, resourceResolver, resourcePath, count, properties, subPath);
			
			Resource resource = resourceResolver.getResource(resourcePath);
			
			if (resource != null && resource.hasChildren()) {
				for (Resource res :resource.getChildren()) {
						w.key(ResourceUtil.getName(res.getPath()));
						w.object();
						replyJson(w, resourceResolver, res.getPath(), count, properties, "jcr:content");
						w.endObject();
				}
			}
			
			w.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e.fillInStackTrace());
		}
		
	}
	
	private void replyJson(JSONWriter w, ResourceResolver resourceResolver, String resourcePath, long count, List<String> properties, String subPath)
			throws IOException {
		try {
			
			Resource res = resourceResolver.getResource(resourcePath);
			
			if (res == null || res.getValueMap() == null) {
				return;
			}
			
			ValueMap valueMap = res.adaptTo(ValueMap.class);
			w.key("name");
			w.value(ResourceUtil.getName(resourcePath));
			w.key("path");
			w.value(resourcePath);
			w.key("hasChildren");
			w.value(res.hasChildren());
			
			if (properties != null && !properties.isEmpty()) {
				for (String key : properties) {
					w.key(key);
					w.value(formatValue(valueMap.getOrDefault(key, "")));
					
					count --;
					if (count==0) {
						break;
					}
				}
			}
			else {
				for (String key : valueMap.keySet()) {
					w.key(key);
					w.value(formatValue(valueMap.getOrDefault(key, "")));
					
					count --;
					if (count==0) {
						break;
					}
				}
			}
			
			
			if (subPath != null && subPath.trim().length() > 0) {
				res = resourceResolver.getResource(resourcePath + "/" + subPath.trim());
				
				if (res != null) {
					w.key(ResourceUtil.getName(subPath.trim()));
					w.object();
					replyJson(w, resourceResolver, res.getPath(), count, properties, "jcr:content");
					w.endObject();
				}
			}
			
		} catch (JSONException je) {
			throw wrapException(je);
		}
	}
	
	
	/**
	 * Dumps the result as JSON object.
	 *
	 * @param req
	 *            request
	 * @param resp
	 *            response
	 * @throws IOException
	 *             in case the search will unexpectedly fail
	 */
	private void dumpResult(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException {

		ResourceResolver resolver = req.getResourceResolver();
		int postOffset = ((req.getRequestPathInfo().getExtension() == null) ? 0
				: (req.getRequestPathInfo().getExtension().length() + 1))
				+ ((req.getRequestPathInfo().getSelectorString() == null) ? 0
						: (req.getRequestPathInfo().getSelectorString().length() + 1));

		String statement = (req.getParameter(STATEMENT) != null) ? req.getParameter(STATEMENT)
				:("/jcr:root" + req.getPathInfo().substring(0, req.getPathInfo().length() - postOffset));
		
		String queryType = (req.getParameter(QUERY_TYPE) != null && req.getParameter(QUERY_TYPE).equals(Query.SQL))
				? Query.SQL : Query.XPATH;

		Iterator<Resource> results = resolver.findResources(statement, queryType);

		if (req.getParameter(OFFSET) != null) {
			long skip = Long.parseLong(req.getParameter(OFFSET));
			while (skip > 0 && results.hasNext()) {
				results.next();
				skip--;
			}
		}

		resp.setContentType(req.getResponseContentType());
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("queryType", queryType);
		resp.setHeader("statement", statement);

		long count = -1;
		if (req.getParameter(ROWS) != null) {
			count = Long.parseLong(req.getParameter(ROWS));
		}

		List<String> properties = new ArrayList<String>();
		if (req.getParameterValues(PROPERTY) != null) {
			for (String property : req.getParameterValues(PROPERTY)) {
				properties.add(property);
			}
		}

		String exerptPath = "";
		if (req.getParameter(EXCEPT_PATH) != null) {
			exerptPath = req.getParameter(EXCEPT_PATH);
		}

		replyJsonArray(resp, resolver, results, count, properties, exerptPath);
	}

	private void replyJsonArray(SlingHttpServletResponse resp, ResourceResolver resolver,
			Iterator<Resource> results, long count, List<String> properties, String exerptPath)
			throws IOException {
		try {
			final JSONWriter w = new JSONWriter(resp.getWriter());
			w.array();

			// iterate through the child resources and build the "json results"
			while (results.hasNext() && count != 0) {
				Resource res = results.next();
				
				if (res != null) {
					w.object();
					replyJson(w, resolver, res.getPath(), count, properties, "jcr:content");
					w.endObject();
				}
				count--;
			}
			w.endArray();
		} catch (JSONException je) {
			throw wrapException(je);
		}
	}

	private void dumpProperties(JSONWriter w, Resource nodeRes, List<String> properties) throws JSONException {

		// nothing to do if there is no resource
		if (nodeRes == null) {
			return;
		}

		ResourceResolver resolver = nodeRes.getResourceResolver();
		for (String property : properties) {
			Resource prop = resolver.getResource(nodeRes, property);
			if (prop != null) {
				String strValue;
				Value value = prop.adaptTo(Value.class);
				if (value != null) {
					strValue = formatValue(value);
				} else {
					strValue = prop.adaptTo(String.class);
					if (strValue == null) {
						strValue = "";
					}
				}
				w.key(property);
				w.value(strValue);
			}
		}

	}

	private String formatValue(Value value) {
		try {
			return formatValue(JcrResourceUtil.toJavaObject(value));
		} catch (RepositoryException re) {
			// might log
		}
		return "";
	}

	private String formatValue(Object value) {
		String strValue;
		if (value instanceof InputStream) {
			// binary value comes as a LazyInputStream
			strValue = "[binary]";

			// just to be clean, close the stream
			try {
				((InputStream) value).close();
			} catch (IOException ignore) {
			}
		} else if (value != null && (value instanceof Date)) {
			strValue = dateFormat.format(value);
		} else if (value != null && (value instanceof GregorianCalendar)) {
			strValue = dateFormat.format(((GregorianCalendar)value).getTime());
		}else if (value != null) {
			strValue = value.toString();
		}else {
			strValue = "";
		}

		return strValue;
	}

	/**
	 * @param e
	 * @throws org.apache.sling.api.SlingException
	 *             wrapping the given exception
	 */
	private SlingException wrapException(Exception e) {
		log.warn("Error in QueryServlet: " + e.toString(), e);
		return new SlingException(e.toString(), e);
	}
}