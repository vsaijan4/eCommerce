package com.ecommerce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyServer {
	public static void main(String[] args) throws Exception {
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");

		Server jettyServer = new Server(9030);
		
	    ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
	    jerseyServlet.setInitOrder(0);
	    jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.ecommerce.controller");
		jerseyServlet.setInitParameter("javax.ws.rs.Application", JerseyApplication.class.getCanonicalName());

		jettyServer.setHandler(context);
		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}
}