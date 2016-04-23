package com.yujingnet.osp.bundle.util;

//import org.apache.commons.lang3.StringUtils; 
//import org.apache.commons.lang3.time.DateUtils; 
//import org.slf4j.Logger; 
//import org.slf4j.LoggerFactory; 
// 
//import javax.jcr.LoginException;
//import javax.jcr.Node;
//import javax.jcr.NodeIterator; 
//import javax.jcr.RepositoryException; 
//import javax.jcr.Session; 
//import javax.jcr.query.InvalidQueryException; 
//import javax.jcr.query.Query; 
//import javax.jcr.query.QueryManager;
//import javax.jcr.query.QueryResult;
//import javax.jcr.query.qom.QueryObjectModel; 
// 
//import java.util.ArrayList; 
//import java.util.Arrays; 
//import java.util.Calendar; 
//import java.util.Collection; 
//import java.util.Collections; 
//import java.util.HashSet; 
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set; 
 
public class QueryUtil { 

//   private static Logger log = LoggerFactory.getLogger(QueryUtil.class); 
//
//   public static NodeIterator search(String workspace, String statement, String language) throws InvalidQueryException, RepositoryException{
//	    Session session = MgnlContext.getJCRSession(workspace);
//	    QueryManager manager = session.getWorkspace().getQueryManager();
//	    Query query = manager.createQuery(statement, language);
//
//	    return NodeUtil.filterDuplicates(query.execute().getNodes());
//	}
//
//   public Node queryForVanityUrlNode(final String vanityUrl, final String siteName) {
//	    Node node = null;
//
//	    try {
//	        Session jcrSession = MgnlContext.getJCRSession(VanityUrlModule.WORKSPACE);
//	        QueryManager queryManager = jcrSession.getWorkspace().getQueryManager();
//	        Query query = queryManager.createQuery(QUERY, JCR_SQL2);
//	        query.bindValue(PN_VANITY_URL, new StringValue(vanityUrl));
//	        query.bindValue(PN_SITE, new StringValue(siteName));
//	        QueryResult queryResult = query.execute();
//	        NodeIterator nodes = queryResult.getNodes();
//	        if (nodes.hasNext()) {
//	            node = nodes.nextNode();
//	        }
//	    } catch (RepositoryException e) {
//	        LOGGER.error("Error message.", e);
//	    }
//
//	    return node;
//	}
//   
//   public String queryJcrContent(Session session) throws RepositoryException {
//
//	    // get query manager
//	    QueryManager queryManager = session.getWorkspace().getQueryManager();
//
//	    // query for all nodes with tag "JCR"
//	    Query query = queryManager.createQuery("/jcr:root/content/adaptto//*[tags='JCR']", Query.XPATH);
//
//	    // iterate over results
//	    QueryResult result = query.execute();
//	    NodeIterator nodes = result.getNodes();
//	    StringBuilder output = new StringBuilder();
//	    while (nodes.hasNext()) {
//	      Node node = nodes.nextNode();
//	      output.append("path=" + node.getPath() + "\n");
//	    }
//
//	    return output.toString();
//	  }
//   
//   protected final List<Node> getCollectionNodes(Session session) throws RepositoryException {
//	    StringTemplate queryTemplate = QUERY_GROUP.getInstanceOf(GET_COLLECTION_NODES_QUERY_TEMPLATE);
//	    QueryManager queryManager = session.getWorkspace().getQueryManager();
//	    Query query = queryManager.createQuery(queryTemplate.toString(), Query.XPATH);
//	    QueryResult queryResult = query.execute();
//	    List<Node> elements = new ArrayList<Node>();
//	    NodeIterator nodeIterator = queryResult.getNodes();
//	    while (nodeIterator.hasNext()) {
//	        elements.add(nodeIterator.nextNode());
//	    }
//	    return elements;
//	}
//   
//   public QueryResult getQueryResult(Node currentNode) throws RepositoryException {
//	   QueryManager queryManager = currentNode.getSession().getWorkspace().getQueryManager();
//	   String queryStatement = "";
//	   if(currentNode.getPath().equals("/")) {
//	     queryStatement = ROOT_SQL_QUERY;
//	   } else {
//	     queryStatement = StringUtils.replace(
//	    		 VERSION_SQL_QUERY,"$0",currentNode.getPath());
//	   }
//	   Query query = queryManager.createQuery(queryStatement, Query.SQL);
//	   return query.execute();
//	 }
}

