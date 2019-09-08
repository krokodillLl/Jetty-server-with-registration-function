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

public class AuthorizationServlet extends HttpServlet {
    private final AccountService accountService;

    public AuthorizationServlet(AccountService accountService) {
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

        if(!accountService.getUserByLogin(login).getPassword().equals(password)) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Incorrect username or password");
            return;
        }

        accountService.addSession(req.getSession().getId(), accountService.getUserByLogin(login));

        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "authorisation.html"));
        StringBuilder sb = new StringBuilder();
        while(reader.ready()) {
            sb.append(reader.readLine());
        }

        reader.close();

        resp.getWriter().println(sb);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
