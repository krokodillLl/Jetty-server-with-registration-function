package servlets;

import accounts.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputServlet extends HttpServlet {
    private final AccountService accountService;

    public InputServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if(login == null || password == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Incorrect data");
            return;
        }

        if(!accountService.isNotEmptyLoginToProfile() || accountService.getUserByLogin(login) == null
                || !accountService.getUserByLogin(login).getPassword().equals(password)) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Incorrect username or password");
            resp.getWriter().println("<meta http-equiv=\"Refresh\" content=\"3; URL=http://localhost:8080/authorisation\">");
            return;
        }

        accountService.addSession(req.getSession().getId(), accountService.getUserByLogin(login));

        resp.getWriter().println("<html> <body bgcolor=\"#deb887\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"0; URL=http://localhost:8080/afterLogging\"> </body> </html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
