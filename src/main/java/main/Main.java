package main;

import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AfterLoggingServlet;
import servlets.AuthorizationServlet;
import servlets.FirstSheetServlet;
import servlets.RegistrationServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("login", "password", "email"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/*");
        context.addServlet(new ServletHolder(new FirstSheetServlet()), "/*");
        context.addServlet(new ServletHolder(new RegistrationServlet(accountService)), "/registration");
        context.addServlet(new ServletHolder(new AuthorizationServlet(accountService)), "/authorisation");
        context.addServlet(new ServletHolder(new AfterLoggingServlet(accountService)), "/afterLogging");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        System.out.println("The server is started");
        server.join();
    }
}
