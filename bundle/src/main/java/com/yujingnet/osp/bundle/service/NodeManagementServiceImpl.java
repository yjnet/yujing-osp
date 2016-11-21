package com.yujingnet.osp.bundle.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceUtil;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import com.yujingnet.osp.bundle.util.ContentNodeName;
import com.yujingnet.osp.bundle.util.ContentNodePropertyName;
import com.yujingnet.osp.bundle.util.ContentNodeType;

@Service
@Component(immediate=true)
public class NodeManagementServiceImpl implements NodeManagementService {
	private static final String REP_EXCERPT = "rep:excerpt()";
	
	@Reference
    private SlingRepository slingRepository;
	
	@Reference
	private ResourceResolverFactory resourceResolverFactory;
	private ResourceResolver resourceResolver = null;
	
	/** Logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(NodeManagementService.class);
    

//    @Activate
	public void createContentNode(ResourceResolver resourceResolver, String nodePath, Hashtable<String, Object> param) throws LoginException, PersistenceException, RepositoryException  {
		logger.info(resourceResolver.getUserID() + " request create node: " + nodePath);
		
		String path = nodePath;
		
		if (path == null || path.lastIndexOf("/") < 0) {
			throw new PersistenceException("Invalid node path: " + path);
		}
		
		checkAuthority(resourceResolver, path);
		
		path = nodePath.substring(0, nodePath.lastIndexOf("/"));
		String childName = nodePath.substring(1+nodePath.lastIndexOf("/"));
		
		Resource resource = resourceResolver.getResource(path);
		
		if (resource == null || resource.getName() == null) {
			throw new PersistenceException("Invalid resource path: " + path);
		}
		
		boolean hasChild = (resource.getChild(childName) != null);
		
		int index = 0;
		if (hasChild) {
			index++;
			hasChild = (resource.getChild(childName + "_" + index) != null);
		}
		childName = childName + ((index>0)? ("_" + index) : ""); 
		
		Node currentNode = resource.adaptTo(Node.class);
		
		try {
			Session session = currentNode.getSession();
			
			if (path.length() == 0)
				path = "/";
			
			Calendar calendar = Calendar.getInstance();
			
			Node node = session.getNode(path);
			node.addNode(childName, ContentNodeType.YJ_PAGE);
			Node childNode = node.getNode(childName);
			childNode.setProperty(ContentNodePropertyName.JCR_CREATED, calendar);
			childNode.setProperty(ContentNodePropertyName.JCR_CREATED_BY, resourceResolver.getUserID());
			
			
			
			childNode.addNode(ContentNodeName.JCR_CONTENT, ContentNodeType.YJ_PAGE_CONTENT);
			
			Node jcrcontent = childNode.getNode(ContentNodeName.JCR_CONTENT);
			
			jcrcontent.setProperty(ContentNodePropertyName.JCR_CREATED, calendar);
			jcrcontent.setProperty(ContentNodePropertyName.JCR_CREATED_BY, resourceResolver.getUserID());
			jcrcontent.setProperty(ContentNodePropertyName.JCR_LAST_MODIFIED, calendar);
			jcrcontent.setProperty(ContentNodePropertyName.JCR_LAST_MODIFIED_BY, resourceResolver.getUserID());
			
			String templateName = param.containsKey(ContentNodePropertyName.YJ_TEMPLATE)? param.get(ContentNodePropertyName.YJ_TEMPLATE).toString() : null;
			
			if (templateName == null) {
				Node parent = (currentNode.getDepth() > 0)? currentNode.getParent() : null;
				
				while (parent != null && templateName == null) {
					if (parent.hasNode(ContentNodeName.JCR_CONTENT) && parent.getNode(ContentNodeName.JCR_CONTENT).hasProperty(ContentNodePropertyName.YJ_TEMPLATE)) {
						String val = parent.getNode(ContentNodeName.JCR_CONTENT).getProperty(ContentNodePropertyName.YJ_TEMPLATE).getString();
						if (val != null && val.trim().length() > 0) {
							templateName = val.trim();
						}
					}
					
					if (templateName == null && parent.getDepth() > 0 && parent.getParent() != null && parent.getParent().isNodeType(ContentNodeType.YJ_PAGE)) {
						parent = parent.getParent();
					}
					else {
						break;
					}
				}
				
				if (templateName == null && ContentNodePropertyName.getDefaultValue(ContentNodePropertyName.YJ_TEMPLATE) != null) {
					jcrcontent.setProperty(ContentNodePropertyName.YJ_TEMPLATE, JcrResourceUtil.createValue(ContentNodePropertyName.getDefaultValue(ContentNodePropertyName.YJ_TEMPLATE), session));
				}
				
				if (templateName != null) {
					jcrcontent.setProperty(ContentNodePropertyName.YJ_TEMPLATE, templateName);
				}
			}
			
			if (templateName != null) {
				Resource res = resourceResolver.getResource(templateName);
				Resource contentRes = resourceResolver.getResource(templateName+"/jcr:content");
				
				if (res != null) {
					Object resType = res.getValueMap().get(ContentNodePropertyName.SLING_RESOURCE_TYPE);
					if (resType != null) {
						jcrcontent.setProperty(ContentNodePropertyName.SLING_RESOURCE_TYPE, resType.toString());
					}
					else if (contentRes != null){
						resType = contentRes.getValueMap().get(ContentNodePropertyName.SLING_RESOURCE_TYPE);
						if (resType != null) {
							jcrcontent.setProperty(ContentNodePropertyName.SLING_RESOURCE_TYPE, resType.toString());
						}
					}
					
					Object resSuperType = res.getValueMap().get(ContentNodePropertyName.SLING_RESOURCE_SUPER_TYPE);
					if (resSuperType != null) {
						jcrcontent.setProperty(ContentNodePropertyName.SLING_RESOURCE_SUPER_TYPE, resSuperType.toString());
					}
					else if (contentRes != null){
						resSuperType = contentRes.getValueMap().get(ContentNodePropertyName.SLING_RESOURCE_SUPER_TYPE);
						if (resSuperType != null) {
							jcrcontent.setProperty(ContentNodePropertyName.SLING_RESOURCE_SUPER_TYPE, resSuperType.toString());
						}
					}
				}
			}
			
			String designPath = param.containsKey(ContentNodePropertyName.YJ_DESIGN_PATH)? param.get(ContentNodePropertyName.YJ_DESIGN_PATH).toString() : null;
			if (designPath == null && ContentNodePropertyName.getDefaultValue(ContentNodePropertyName.YJ_DESIGN_PATH) != null) {
				jcrcontent.setProperty(ContentNodePropertyName.YJ_DESIGN_PATH, JcrResourceUtil.createValue(ContentNodePropertyName.getDefaultValue(ContentNodePropertyName.YJ_DESIGN_PATH), session));
			}
			
			Enumeration<String> e = param.keys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				jcrcontent.setProperty(key, JcrResourceUtil.createValue(param.get(key), session));
			}
			
			session.save();
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
			
			throw new RepositoryException("Unable to create node[" + childName + "] under: " + path + " by " + resourceResolver.getUserID(), e);
		}
		
//		ValueMap readMap = resource.getValueMap();
//		logger.info(readMap.get("jcr:primaryType", ""));
//		   
//		ModifiableValueMap modMap = resource.adaptTo(ModifiableValueMap.class);
//		if(modMap != null){
//			Enumeration<String> en = param.keys();
//				
//			while (en.hasMoreElements()) {
//				String key = en.nextElement();
//				modMap.put(key, param.get(key));
//			}
//			
//			if (en != null) {
//				resource.getResourceResolver().commit();
//				logger.info("Successfully saved");
//			}
//		}
	}

//    @Activate
	public void updateNodeProperties(ResourceResolver resourceResolver, String nodePath, Hashtable<String, Object> param) throws Exception {
		checkAuthority(resourceResolver, nodePath);
		
		Resource jcrcontent = resourceResolver.getResource(nodePath);
		
		if (jcrcontent == null) {
			throw new RepositoryException("Non existing node path: " + nodePath);
		}
		
		String nodeType = jcrcontent.adaptTo(Node.class).getPrimaryNodeType().getName();
		
		if (ContentNodeType.YJ_PAGE.equals(nodeType)) {
			jcrcontent = resourceResolver.getResource(nodePath + "/" + ContentNodeName.JCR_CONTENT);
		}
		
		if (jcrcontent == null) {
			throw new RepositoryException("Invalid " + ContentNodeName.JCR_CONTENT + " node path: " + nodePath);
		}
		
		ModifiableValueMap properties = jcrcontent.adaptTo(ModifiableValueMap.class);
		properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED, Calendar.getInstance());
		properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED_BY, resourceResolver.getUserID());
		
		Enumeration<String> e = param.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			properties.put(key, param.get(key));
		}
		
		resourceResolver.commit();

	}
	
	public void changeNodeName(ResourceResolver resourceResolver, String moveFrom, String moveTo) throws Exception {
		copyNode(resourceResolver, moveFrom, moveTo);
		deleteNode(resourceResolver, moveFrom);
	}
	
	public void copyNode(ResourceResolver resourceResolver, String copyFrom, String copyTo) throws Exception {
		copyNode(resourceResolver, copyFrom, copyTo, Calendar.getInstance());
	}
	
	private void copyNode(ResourceResolver resourceResolver, String copyFrom, String copyTo, Calendar myModified) throws Exception {
		if ( copyTo == null || copyTo.trim().length() == 0) {
			throw new RepositoryException("New node path is not available");
		}
		
		checkAuthority(resourceResolver, copyFrom, getFullpath(copyFrom, copyTo));
		Resource oldPathResource = resourceResolver.getResource(copyFrom);
		
		ModifiableValueMap properties = oldPathResource.adaptTo(ModifiableValueMap.class);
		
		if (myModified == null) {
			myModified = Calendar.getInstance();
		}
		
		Object nodeLastModified = properties.get(ContentNodePropertyName.JCR_LAST_MODIFIED);
		
		if (nodeLastModified != null && nodeLastModified instanceof Calendar) {
			if (!((Calendar) nodeLastModified).before(myModified)) {
				return;
			}
		}
		
		properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED, myModified);
		properties.put(ContentNodePropertyName.JCR_LAST_MODIFIED_BY, resourceResolver.getUserID());
		
		String parent = (copyTo.startsWith("/"))? copyTo : copyFrom.substring(0, copyFrom.lastIndexOf("/"));
		String nodeName = (copyFrom.contains("/"))? copyFrom.substring(copyFrom.lastIndexOf("/")+1) : copyFrom;
		resourceResolver.create(resourceResolver.getResource(parent), nodeName, properties);
		
		if (oldPathResource.hasChildren()) {
			Iterable<Resource> resources = oldPathResource.getChildren();
			
			for (Resource res : resources) {
				copyNode(resourceResolver, copyFrom + "/" + res.getName(), copyTo + "/" + nodeName, myModified);
			}
		}
		resourceResolver.commit();
	}

//    @Activate
	public void deleteNode(ResourceResolver resourceResolver, String nodePath) throws PersistenceException {
		checkAuthority(resourceResolver, nodePath);
		
		Resource resource = resourceResolver.getResource(nodePath);
		try {
			resourceResolver.delete(resource);
			resourceResolver.commit();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to delete node on path " + nodePath + ": " + e.toString(), e.fillInStackTrace());
			throw e;
		}
		
	}

    //Deprecated Method using admin resolver
    //resolver = resolverFactory.getAdministrativeResourceResolver(null);
	ResourceResolver getResourceResolver() throws LoginException {
//		final Map<String, Object> authInfo = Collections.singletonMap(
//                ResourceResolverFactory.SUBSERVICE,
//                (Object) SERVICE_ACCOUNT_IDENTIFIER);
//
//        // Get the Service resource resolver
//        serviceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
//        
		
		if (resourceResolver != null && resourceResolver.isLive())
			return resourceResolver;
		
		Map<String, Object> param = new HashMap<String, Object>();
			
		param.put(ResourceResolverFactory.SUBSERVICE, getServiceName());
		  
		
		resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);

		return resourceResolver;
	}
	
	//If you are planning to use repository session instead of repository.loginAdministrative
    //session = repository.getService("writeService",null);
//	Session getSession() throws javax.jcr.LoginException, RepositoryException {
//		if (session != null && session.isLive())
//			return session;
//		
//		session =  slingRepository.loginService(getServiceName(), null);
//		
//		return session;
//	}
//	
//	void close() {
//		if(resourceResolver != null && resourceResolver.isLive()){
//			resourceResolver.close();
//		}
//		resourceResolver = null;
//		
//		if(session != null && session.isLive()){
//			session.logout();
//		}
//		session = null;
//	}
	
	String getServiceName() {
		String name = NodeManagementService.class.getName();
		if (name.lastIndexOf(".") > 0)
			name = name.substring(name.lastIndexOf(".") + 1);
		
		return name;
	}
	
	// the node properties in jSon format output to writer
	public void getNodeProperties(Writer writer, ResourceResolver resolver,
			String statement, String queryType, long skip, long count, List<String> properties, String exerptPath) {
		
		Iterator<Map<String, Object>> result = resolver.queryResources(statement, queryType);

		// Skip the first few results
		while (skip > 0 && result.hasNext()) {
			result.next();
			skip--;
		}

		try {
			final JSONWriter w = new JSONWriter(writer);
			w.array();

			// iterate through the result set and build the "json result"
			while (result.hasNext() && count != 0) {
				Map<String, Object> row = result.next();

				w.object();
				String path = row.get("jcr:path").toString();

				w.key("name");
				w.value(ResourceUtil.getName(path));

				// dump columns
				for (String colName : row.keySet()) {
					w.key(colName);
					String strValue = "";
					if (colName.equals(REP_EXCERPT)) {
						Object ev = row.get("rep:excerpt(" + exerptPath + ")");
						strValue = (ev == null) ? "" : ev.toString();
					} else {
						strValue = formatValue(row.get(colName));
					}
					w.value(strValue);
				}

				// load additional properties and add them to the result set
				if (!properties.isEmpty()) {
					Resource nodeRes = resolver.getResource(path);
					dumpProperties(w, nodeRes, properties);
					
					if ((!path.endsWith("/jcr:content")) && (!path.endsWith("/jcr:content/"))) {
						w.key("jcr:content");
						w.object();
						dumpProperties(w, resolver.getResource((path.endsWith("/"))? (path+"jcr:content"):(path+"/jcr:content")), properties);
						w.endObject();
					}
				}

				w.endObject();
				count--;
			}
			w.endArray();
		} catch (JSONException je) {
			try {
				writer.append(je.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw wrapException(je);
		}
	}
	
//	@Activate
	public void activate(ComponentContext ctx, Map<String, Object> configMap) {
//	    prepare(ctx, configMap);
	}

//	@Modified
	public void modified(ComponentContext ctx, Map<String, Object> configMap) {
//	    synchronized (this.preparationMonitor) {
//	        prepare(configMap);
//	    }
	}
	
	private String getFullpath(String base, String path) {
		if (path != null && path.indexOf("/") < 0 && base.lastIndexOf("/") > 0) {
			return (base.substring(0, base.lastIndexOf("/")) + "/" + path);
		}
		
		return path;
	}
	
	private void checkAuthority(ResourceResolver resourceResolver, String ... paths) throws PersistenceException {
		String validPrefix = "/content/" + (("admin".equals(resourceResolver.getUserID())? "":(resourceResolver.getUserID() + "/")));
		
		for (String path : paths) {
			if (path.endsWith("/") || !path.startsWith(validPrefix) || (path.lastIndexOf("/") +1) < validPrefix.length()) {
				throw new PersistenceException("Not authorized node path: '" + path + "'. Please create content node under '" + validPrefix + "' without ending with '/'");
			}
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
			else {
				w.key(property);
				w.value(null);
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
		} else if (value != null) {
			strValue = value.toString();
		} else {
			strValue = "";
		}

		return strValue;
	}
	
	private SlingException wrapException(Exception e) {
		logger.warn("Error in QueryServlet: " + e.toString(), e);
		return new SlingException(e.toString(), e);
	}
}
