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

public class AfterLoggingServlet extends HttpServlet {

    private final AccountService accountService;

    public AfterLoggingServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession().getId();
        if(accountService.getUserBySession(sessionId) == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("first authorisation");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "afterLogging.html"));
        StringBuilder sb = new StringBuilder();
        sb.append(reader.readLine() + "Hi, " + accountService.getUserBySession(sessionId).getLogin());
        while(reader.ready()) {
            sb.append(reader.readLine());
        }

        reader.close();

        resp.getWriter().println(sb);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();

        if(accountService.getUserBySession(sessionId) == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        accountService.deleteSession(sessionId);
        resp.getWriter().println("Goodbye");
    }

}
