package com.yujingnet.osp.bundle.util;

import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class ContentNodeName {
	public static final String JCR_CONTENT = "jcr:content";
	public static final String CONTENT = "content";
	
	
	public static String getNodeName(Node contentNode, String sourceType) {
		return getNodeName(contentNode, sourceType, new PrintWriter(System.out));
	}
	
	public static String getNodeName(Node contentNode, String sourceType, PrintWriter out) {
		String[] names = sourceType.split("/");
		String name = names[names.length -1];
		
		try {
			NodeIterator it = contentNode.getNodes();
			out.println("FIND node number: " + it.getSize());
			while (it.hasNext()) {
				Node node = it.nextNode();
				out.println("get node name: " + node.getName() + " (" + node.getIndex() + ")");
			}
			
			int ind = 1;
			String ext = "";
			while (contentNode.hasNode(name+ext)) {
				ext = "_" + ind;
				ind ++;
			}
			name = name + ext;
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("RETURN node name: " + name);
		return name;
	}
}
