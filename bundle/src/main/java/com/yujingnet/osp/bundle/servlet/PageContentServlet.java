package com.yujingnet.osp.bundle.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

@Component
@Service(value = javax.servlet.Servlet.class)
@Properties({@Property(name = "service.description", value = "Default Page Servlet"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.resourceTypes", value = "yj:page"),
		@Property(name = "sling.servlet.extensions", value = "html")})
public class PageContentServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException, ServletException {
		forward(req, resp);
	}

	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException, ServletException {
		forward(req, resp);
	}
	
	private void forward(SlingHttpServletRequest req, SlingHttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcherOptions options = new RequestDispatcherOptions();
		Enumeration<String> e = req.getParameterNames();
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			options.put(key, req.getParameter(key.toString()));
		}
//		RequestDispatcher dispatcher = req.getRequestDispatcher("/content/project/abc/us/en/homepage.html", options);
//		dispatcher.forward(wrappedRequest, slingResponse);
//		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(toRedirectPath("jcr:content", req));
		RequestDispatcher dispatcher = req.getRequestDispatcher(toRedirectPath("jcr:content", req), options);
		
		dispatcher.forward(req,resp);
//		resp.sendRedirect(toRedirectPath("jcr:content", req));
	}


	protected String toRedirectPath(String targetPath, SlingHttpServletRequest request) {

		// if the target path is an URL, do nothing and return it unmodified
		final RequestPathInfo rpi = request.getRequestPathInfo();

		// make sure the target path is absolute
		final String rawAbsPath;
		if (targetPath.startsWith("/")) {
			rawAbsPath = targetPath;
		} else {
			rawAbsPath = request.getResource().getPath() + "/" + targetPath;
		}

		final StringBuilder target = new StringBuilder();

		// and ensure the path is normalized, us unnormalized if not possible
		final String absPath = ResourceUtil.normalize(rawAbsPath);
		if (absPath == null) {
			target.append(rawAbsPath);
		} else {
			target.append(absPath);
		}

		// append current selectors, extension and suffix
		if (rpi.getExtension() != null) {

			if (rpi.getSelectorString() != null) {
				target.append('.').append(rpi.getSelectorString());
			}

			target.append('.').append(rpi.getExtension());

			if (rpi.getSuffix() != null) {
				target.append(rpi.getSuffix());
			}
		}

		// append current querystring
//		if (request.getQueryString() != null) {
//			target.append('?').append(request.getQueryString());
//		}

		// return the mapped full path
		return request.getResourceResolver().map(request, target.toString());
	}

}