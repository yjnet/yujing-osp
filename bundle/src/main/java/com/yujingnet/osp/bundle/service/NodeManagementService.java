package com.yujingnet.osp.bundle.service;

import java.util.Hashtable;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;

public interface NodeManagementService {
	void createContentNode(ResourceResolver resourceResolver, String nodePath, Hashtable<String, Object> param) throws Exception;
	void updateNodeProperties(ResourceResolver resourceResolver, String nodePath, Hashtable<String, Object> param) throws Exception;
	void changeNodeName(ResourceResolver resourceResolver, String moveFrom, String moveTo) throws Exception;
	void copyNode(ResourceResolver resourceResolver, String copyFrom, String copyTo) throws Exception;
	void deleteNode(ResourceResolver resourceResolver, String nodePath) throws PersistenceException;
}
