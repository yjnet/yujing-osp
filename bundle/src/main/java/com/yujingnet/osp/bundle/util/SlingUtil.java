package com.yujingnet.osp.bundle.util;

import org.apache.sling.api.resource.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SlingUtil {
	
	/** disable construct from new */
    private SlingUtil() { }

	/**
     * Get an instance of the {@link SlingHttpServletRequest} from the servletRequest
     *
     * @param ServletRequest - servlet request
     * @return SlingHttpServletRequest
     */
    public static SlingHttpServletRequest getRequest(ServletRequest servletRequest) {
        SlingHttpServletRequest slingHttpServletRequest = null;
 
        if (servletRequest instanceof SlingHttpServletRequest) {
            slingHttpServletRequest = (SlingHttpServletRequest) servletRequest;
        } else {
 
            while (servletRequest instanceof ServletRequestWrapper) {
                servletRequest = ((ServletRequestWrapper) servletRequest).getRequest();
                if (servletRequest instanceof SlingHttpServletRequest) {
                    slingHttpServletRequest = (SlingHttpServletRequest) servletRequest;
                    break;
                }
            }
 
        }
 
        return slingHttpServletRequest;
    }
    
    /**
     * Get an instance of the {@link SlingHttpServletResponse} from the servletResponse
     *
     * @param ServletResponse - the servlet response
     * @return SlingHttpServletResponse
     */
    public static SlingHttpServletResponse getResponse(ServletResponse servletResponse) {
        SlingHttpServletResponse slingHttpServletResponse = null;
 
        if (servletResponse instanceof SlingHttpServletResponse) {
            slingHttpServletResponse = (SlingHttpServletResponse) servletResponse;
        } else {
 
            while (servletResponse instanceof ServletRequestWrapper) {
                servletResponse = ((SlingHttpServletResponseWrapper) servletResponse).getResponse();
                if (servletResponse instanceof SlingHttpServletRequest) {
                    slingHttpServletResponse = (SlingHttpServletResponse) servletResponse;
                    break;
                }
            }
 
        }
 
        return slingHttpServletResponse;
    }
    
    public static boolean isLocalhost(SlingHttpServletRequest inRequest) {
        boolean returnValue = false;
        String serverName = "";
 
        serverName = inRequest.getServerName();
        returnValue = "localhost".equalsIgnoreCase(serverName);
 
        return returnValue;
    }
     
    
    /**
     * Retrieve a reference to the Sling script helper
     *
     * @param ServletRequest - the servlet request
     * @return SlingScriptHelper - the Sling script helper
     */
    private static SlingScriptHelper getScriptHelper(ServletRequest inRequest) {
        SlingScriptHelper returnValue;
 
        SlingHttpServletRequest request = getRequest(inRequest);
        SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
        returnValue = bindings.getSling();
 
        return returnValue;
    }
 
    /**
     * Retrieve a reference to a Sling service
     *
     * @param ServletRequest - the servlet request
     * @param Class<T>       - the service class
     * @return <T> T - the Sling service
     */
    public static <T> T getService(ServletRequest inRequest, Class<T> clazz) {
        T service;
        SlingScriptHelper slingScriptHelper;
 
        slingScriptHelper = getScriptHelper(inRequest);
        service = clazz.cast(slingScriptHelper.getService(clazz));
 
        return service;
    }
     
    /**
     * Logout of the session
     * @param Session - the JCR session
     */
    public static void logout(Session session) {
        if (session != null) {
            try {
                if (session.hasPendingChanges()) {
                    logger.error("Closing session with pending changes");
                }
            } catch (RepositoryException e) {
                logger.error("Exception: " + e.toString());
            }
            session.logout();
        }
    }
     
    /**
     * Logout of the administrative resource resolver session
     * @param Session - the JCR Session
     * @param ResourceResolver - the administrative resource resolver
     */
    public static void logout(Session inSession, ResourceResolver resourceResolver) {
        logout(inSession);
        if (resourceResolver != null) {
            resourceResolver.close();
        }
    }
     
    /**
     * Get the request parameter value OR "" if it does not exist
     * @see #getRequestParameterValue(SlingHttpServletRequest, String, String)
     * @param SlingHttpServletRequest - the sling servlet request
     * @param String - the request parameter key
     * @return String - the value of the parameter
     */
    public static String getRequestParameterValue(SlingHttpServletRequest argRequest, String argParameterKey) {
        return getRequestParameterValue(argRequest, argParameterKey, "");
    }
     
    /**
     * Get the request parameter value OR "" if it does not exist
     * @param SlingHttpServletRequest - the sling servlet request
     * @param String - the request parameter key
     * @param String - the default value to use if not present in request
     * @return String - the value of the parameter
     */
    public static String getRequestParameterValue(SlingHttpServletRequest argRequest, String argParameterKey, String argDefaultValue) {
        String returnValue = argDefaultValue;
         
        RequestParameterMap requestParameterMap = argRequest.getRequestParameterMap();
         
        if (requestParameterMap.containsKey(argParameterKey)) {
            returnValue = requestParameterMap.getValue(argParameterKey).getString().trim();
        }
         
        return returnValue;
    }
    
    /**
     * Get a reference to the admin JCR session 
     * NOTE: (SHOULD ONLY BE CALLED FROM BEANS)
     * @param SlingHttpServletRequest - the sling servlet request
     * @return Session - the admin JCR session
     * @throws RepositoryException 
     */
    public static Session loginAdministrative(SlingHttpServletRequest argRequest) throws RepositoryException {
    	Session returnValue = null;
    	
    	SlingRepository slingRepository = getService(argRequest, SlingRepository.class);
    	returnValue = loginAdministrative(slingRepository);
    	
    	return returnValue;
    }
    
   
    public static Session loginAdministrative(SlingRepository argSlingRepository) throws RepositoryException {
    	return argSlingRepository.loginAdministrative(null);
    }
    
    /**
     * Get a reference to the admin resource resolver
     * FIXME: Find alternative to deprecated method
     * @param SlingScriptHelper - the sling script helper
     * @return ResourceResolver - the admin resource resolver
     * @throws LoginException 
     */
    public static ResourceResolver getAdministrativeResourceResolver(SlingHttpServletRequest argRequest) throws LoginException {
    	ResourceResolver returnValue = null;
    	
    	ResourceResolverFactory resourceResolverFactory = getService(argRequest, ResourceResolverFactory.class);
    	returnValue = resourceResolverFactory.getAdministrativeResourceResolver(null);
    	
    	return returnValue;
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(SlingUtil.class);
 
}
