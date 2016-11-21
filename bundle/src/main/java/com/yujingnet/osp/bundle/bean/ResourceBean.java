package com.yujingnet.osp.bundle.bean;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;

public class ResourceBean {
	private String path;
	private String nodePath;
	private String selectedNode;
	private ResourceResolver resourceResolver;
	private Node currentNode;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getNodePath() {
		if (resourceResolver == null)
			return null;

		nodePath = resourceResolver.map(path);
		return nodePath;
	}
	public String getNodePath(String path) {
		if (resourceResolver == null)
			return null;

		nodePath = resourceResolver.map(path);
		return nodePath;
	}
	
	public String getSelectedNode() {
		return selectedNode;
	}
	public void setSelectedNode(String selectedNode) {
		this.selectedNode = selectedNode;
	}
	public ResourceResolver getResourceResolver() {
		return resourceResolver;
	}
	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}
	public Node getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	public Session getSession() {
		if (getCurrentNode() != null)
			try {
				return getCurrentNode().getSession();
			} catch (RepositoryException e) {
			}
		
		if (resourceResolver == null)
			return null;
		
		return resourceResolver.adaptTo(Session.class);
	}
}
