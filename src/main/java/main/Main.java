package main;

import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AfterLoggingServlet;
import servlets.InputServlet;
import servlets.FirstSheetServlet;
import servlets.AuthorisationServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();
        accountService.addNewUser(new UserProfile("login", "password", "email"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new FirstSheetServlet()), "/*");
        context.addServlet(new ServletHolder(new AuthorisationServlet(accountService)), "/authorisation");
        context.addServlet(new ServletHolder(new InputServlet(accountService)), "/input");
        context.addServlet(new ServletHolder(new AfterLoggingServlet(accountService)), "/afterLogging");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        System.out.println("The server started");
        server.join();
    }
}
