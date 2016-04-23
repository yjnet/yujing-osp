package com.yujingnet.osp.bundle.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.jcr.Session;
//import com.day.cq.search.result.SearchResult;
public interface SearchNode {
//	public SearchResult getSearchResult(Session argSession, Map<String, String> argQueryDescription, long argStart, long argResultsPerPage) throws Exception;
	
	public static final String TOPIC_WEB_RESOURCE_CREATED = "org/apache/sling/webresource/CREATED";

	public static final String TOPIC_WEB_RESOURCE_DELETED = "org/apache/sling/webresource/DELETED";

	public static final String COMPILE_EVENT = "org/apache/sling/webresource/COMPILE";

	public static final String COMPILE_ALL_EVENT = "org/apache/sling/webresource/COMPILEALL";

	public List<String> getSourceWebResources(String webResourceName);

	public String getWebResourcePathLookup(String webResourceName);

	public Set<String> getAllWebResourceNames();

	public Collection<String> getAllWebResourcePaths();
}
