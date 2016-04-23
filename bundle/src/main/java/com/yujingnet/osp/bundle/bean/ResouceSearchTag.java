package com.yujingnet.osp.bundle.bean;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.*;

public class ResouceSearchTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	
	@Reference
	private ResourceResolverFactory resourceResolverFactory;
	
	protected String elemSep =  ",";
    protected String lineSep = ";";
 
    public int doEndTag() throws JspException 
    {
       String bodyText = bodyContent.getString();
       ResourceResolver resourceResolver = null;
       
       try {
    	   resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
    	   pageContext.getOut().print(bodyText);
       }
       catch (Exception e) {
       	throw new JspTagException(e.getMessage());
       }

       StringBuffer tableOut = new StringBuffer(); 
       StringTokenizer bodyTk = new StringTokenizer(bodyText, lineSep, false);
       
       tableOut.append("<div>");
       while (bodyTk.hasMoreTokens())
       {
           String str = (String)bodyTk.nextToken();
           
           Resource resource = resourceResolver.getResource(str);
           resource.getPath();
           
           StringTokenizer token = new StringTokenizer(str, elemSep, false);
           tableOut.append("<ul>");
           while(token.hasMoreTokens())
           {
        	   String path = (String)token.nextToken();
        	   if (path != null) {
        		   path = path.trim();
        	   }
        	   
        	   String name = (path.lastIndexOf("/")> 0)? path.substring(path.lastIndexOf("/")+1):path;
        			   
                tableOut.append("<il id=\""+ path + "\">"); 
                tableOut.append(name); 
                tableOut.append("</il>"); 
           }
           tableOut.append("</ul>");
       } 
      tableOut.append("</div>");
       
      try {
       	pageContext.getOut().print(tableOut);
       }
       catch (Exception e) {
       	throw new JspTagException(e.getMessage());
       } 
       return EVAL_PAGE;        
    }

    public void release() 
    {
       elemSep = ",";
       lineSep = ";";
    }
}
