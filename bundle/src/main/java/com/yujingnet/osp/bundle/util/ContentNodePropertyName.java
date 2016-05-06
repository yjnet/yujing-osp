package com.yujingnet.osp.bundle.util;

import java.util.Hashtable;

public class ContentNodePropertyName {
	public static final String SLING_RESOURCE_TYPE = "sling:resourceType";
	public static final String SLING_RESOURCE_SUPER_TYPE = "sling:resourceSuperType";

    public static final String JCR_DESCRIPTION = "jcr:description";
    public static final String JCR_TITLE = "jcr:title";
    public static final String JCR_DATA = "jcr:data";
    
    public static final String JCR_LAST_MODIFIED = "jcr:lastModified";
    public static final String JCR_LAST_MODIFIED_BY = "jcr:lastModifiedBy";
    public static final String JCR_CREATED = "jcr:created";
    public static final String JCR_CREATED_BY = "jcr:createdBy";
    public static final String JCR_PRIMARY_TYPE = "jcr:primaryType";
    public static final String JCR_MIME_TYPE = "jcr:mimeType";
    public static final String JCR_UUID = "jcr:uuid";
    public static final String JCR_PATH = "jcr:path";
    
    public static String COMPONENT_GROUP = "componentGroup";
    public static String ALLOWED_PARENTS = "allowedParents";
    public static String ALLOWED_CHILDREN = "allowedChildren";
    
    public static String YJ_PRIMARY_TYPE = "yj:primaryType";
    public static String YJ_NEW_PATH = "yj:newPath";
    public static String YJ_TEMPLATE = "yj:template";
    public static String YJ_DESIGN_PATH = "yj:designPath";
    public static String YJ_LAST_REPLICATED = "yj:lastReplicated";
    public static String YJ_LAST_REPLICATED_BY = "yj:lastReplicatedBy";
    public static String YJ_LAST_REPLICATION_ACTION = "yj:lastReplicationAction";
    public static String ACTIVATE_REPLICATION = "Activate";
    public static String INACTIVATE_REPLICATION = "Deactivate";
    
    
    private static Hashtable<String, Object> defaultValues = getDefaultValues();
    
    public static Object getDefaultValue(String propertyName) {
    	return defaultValues.get(propertyName);
    }
    
    private static Hashtable<String, Object> getDefaultValues() {
    	Hashtable<String, Object> intiValues = new Hashtable<String, Object>();
    	
    	return intiValues;
    }
}
