/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher.client;

import com.opensymphony.util.FileManager;

import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.dispatcher.ApplicationMap;
import com.opensymphony.webwork.dispatcher.SessionMap;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequest;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.interceptor.component.ComponentInterceptor;
import com.opensymphony.xwork.interceptor.component.ComponentManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Server side receiver of connections from {@link TransportHttp
 * TransportHttp}. You must configure this servlet in your <code>web.xml</code>
 * file. For example:<BR><BR>
 *
 * <code>
 *   &lt;servlet&gt;<BR>
 *       &lt;servlet-name&gt;client&lt;/servlet-name&gt;<BR>
 *       &lt;servlet-class&gt;com.opensymphony.webwork.dispatcher.client.ServerHttpDispatcher&lt;/servlet-class&gt;<BR>
 *   &lt;/servlet&gt;<BR><BR>
 *
 *  &lt;servlet-mapping&gt;<BR>
 *       &lt;servlet-name&gt;client&lt;/servlet-name&gt;<BR>
 *       &lt;url-pattern&gt;ClientDispatcher&lt;/url-pattern&gt;<BR>
 *   &lt;/servlet-mapping&gt;<BR><BR>
 * </code>
 *
 * @version $Id$
 * @author Ben Alex (<a href="mailto:ben.alex@acegi.com.au">ben.alex@acegi.com.au</a>)
 * @author Philipp Meier (meier@meisterbohne.de)
 * @author Rickard �berg (rickard@middleware-company.com)
 * @author Matt Baldree (matt@smallleap.com)
 */
public class ServerHttpDispatcher extends HttpServlet implements WebWorkStatics {
    //~ Static fields/initializers /////////////////////////////////////////////

    protected static final Log log = LogFactory.getLog(ServerHttpDispatcher.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    Integer maxSize;
    String saveDir;

    //~ Methods ////////////////////////////////////////////////////////////////

    public static String getNamespaceFromServletPath(String servletPath) {
        servletPath = servletPath.substring(0, servletPath.lastIndexOf("/"));

        return servletPath;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        //check for configuration reloading
        if ("true".equalsIgnoreCase(Configuration.getString("webwork.configuration.xml.reload"))) {
            FileManager.setReloadingConfigs(true);
        }

        //load multipart configuration
        //saveDir
        saveDir = Configuration.getString("webwork.multipart.saveDir").trim();

        if (saveDir.equals("")) {
            File tempdir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            log.warn("Unable to find 'webwork.multipart.saveDir' property setting. Defaulting to javax.servlet.context.tempdir");

            if (tempdir != null) {
                saveDir = tempdir.toString();
            }
        } else {
            File multipartSaveDir = new File(saveDir);

            if (!multipartSaveDir.exists()) {
                multipartSaveDir.mkdir();
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("saveDir=" + saveDir);
        }

        //maxSize
        try {
            String maxSizeStr = Configuration.getString("webwork.multipart.maxSize");

            if (maxSizeStr != null) {
                try {
                    maxSize = new Integer(maxSizeStr);
                } catch (NumberFormatException e) {
                    maxSize = new Integer(Integer.MAX_VALUE);
                    log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
                }
            } else {
                maxSize = new Integer(Integer.MAX_VALUE);
                log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
            }
        } catch (IllegalArgumentException e1) {
            maxSize = new Integer(Integer.MAX_VALUE);
            log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
        }

        if (log.isDebugEnabled()) {
            log.debug("maxSize=" + maxSize);
        }
    }

    /**
 * Service a request.
 * Read ClientActionInvocation from request and execute action.
 */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Servicing request");
        }

        //wrap request if needed
        try {
            request = wrapRequest(request);
        } catch (IOException e) {
            String message = "Could not wrap servlet request with MultipartRequestWrapper!";
            log.error(message, e);
            throw new ServletException(message, e);
        }

        // Get action
        String servletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");

        if (servletPath == null) {
            servletPath = request.getServletPath();
        }

        ObjectInputStream in = new ObjectInputStream(request.getInputStream());
        final ClientRequestInvocation invocation;

        try {
            invocation = (ClientRequestInvocation) in.readObject();

            if (log.isDebugEnabled()) {
                log.debug("Have a ClientRequestInvocation object");
            }
        } catch (ClassNotFoundException cnfe) {
            String message = "Could not deserialise ClientActionInvocation!";
            log.error(message, cnfe);
            throw new ServletException(message, cnfe);
        }

        // Path is always original path, even if it is included in page with another path
        //String actionPath = request.getServletPath();
        //actionPath = getNamespaceFromServletPath(actionPath);
        String actionName = getActionName(invocation.getRemoteActionName());
        String actionPath = getNamespaceFromServletPath(invocation.getRemoteActionName());

        if (log.isDebugEnabled()) {
            log.debug("Executing action: " + actionName + " with namespace: " + actionPath);
        }

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, invocation.getParameters());
        extraContext.put(ActionContext.SESSION, new SessionMap(request));
        extraContext.put(ActionContext.APPLICATION, new ApplicationMap(getServletContext()));
        extraContext.put(HTTP_REQUEST, request);
        extraContext.put(HTTP_RESPONSE, response);
        extraContext.put(SERVLET_CONFIG, getServletConfig());
        extraContext.put(ComponentInterceptor.COMPONENT_MANAGER, request.getAttribute(ComponentManager.COMPONENT_MANAGER_KEY));
        extraContext.put(SERLVET_DISPATCHER, this);

        try {
            ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy(actionPath, actionName, extraContext);
            request.setAttribute("webwork.valueStack", proxy.getInvocation().getStack());
            proxy.execute();
        } catch (Exception e) {
            try {
                response.setContentType("text/html");
                response.setLocale(Configuration.getLocale());

                PrintWriter writer = response.getWriter();
                writer.write("Error executing action: " + e.getMessage());
                writer.println("<pre>\n");
                e.printStackTrace(response.getWriter());
                writer.print("</pre>\n");

                log.error("Could not execute action", e);
            } catch (IOException e1) {
            }
        }
    }

    /**
 * Determine action name by extracting last string and removing
 * extension. (/.../.../Foo.action -> Foo)
 */
    String getActionName(String name) {
        // Get action name ("Foo.action" -> "Foo" action)
        int beginIdx = name.lastIndexOf("/");
        int endIdx = name.lastIndexOf(".");

        return name.substring(((beginIdx == -1) ? 0 : (beginIdx + 1)), (endIdx == -1) ? name.length() : endIdx);
    }

    /**
 * Wrap servlet request with the appropriate request. It will check to
 * see if request is a multipart request and wrap in appropriately.
 *
 * @param request
 * @return wrapped request or original request
 */
    private HttpServletRequest wrapRequest(HttpServletRequest request) throws IOException {
        // don't wrap more than once
        if (request instanceof MultiPartRequestWrapper) {
            return request;
        }

        if (MultiPartRequest.isMultiPart(request)) {
            request = new MultiPartRequestWrapper(request, saveDir, maxSize.intValue());
        }

        return request;
    }
}