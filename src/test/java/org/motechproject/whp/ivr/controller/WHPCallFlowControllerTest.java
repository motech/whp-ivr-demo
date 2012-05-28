package org.motechproject.whp.ivr.controller;

import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WHPCallFlowControllerTest {
    public void shouldGetRootURL() throws Exception{

        org.mortbay.jetty.Server server = new org.mortbay.jetty.Server(8000);
        WebAppContext context = new WebAppContext("whp-ivr", "/");
        context.setWar("whp.ivr/src/main/webapp");

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:META-INF/spring/applicationContext.xml");

        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
        context.addServlet(servletHolder, "/*");
        context.setServer(server);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
