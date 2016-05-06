package com.yujingnet.osp.bundle.util;

public class ContentNodeType {
	public static final String YJ_TEMPLATE = "yj:template";
	public static final String YJ_PAGE = "yj:page";
	public static final String YJ_PAGE_CONTENT = "yj:pageContent";
	public static final String YJ_COMPONENT = "yj:component";
	public static final String YJ_USER = "yj:user";
	public static final String YJ_GROUP = "yj:group";
	
	public static final String MIX_VERSIONABLE = "mix:versionable";
	
	public static final String JCR_PRIMARY_TYPE = "jcr:primaryType";
    
	public static final String NT_RESOURCE = "nt:resource";
	public static final String NT_FOLDER = "nt:folder";
    public static final String NT_FILE = "nt:file";
    public static final String NT_UNSTRUCTURED = "nt:unstructured";

    public static boolean isFileType(String type) {
    	return YJ_PAGE.equals(type) || NT_FILE.equals(type) || YJ_TEMPLATE.equals(type);
    }
    
    public static boolean isFolderType(String type) {
    	return NT_FOLDER.equals(type);
    }
    
    public static boolean isComponentType(String type) {
    	return YJ_COMPONENT.equals(type);
    }
}
