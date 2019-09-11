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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String sessionId = req.getSession().getId();
        if(!accountService.isNotEmptySessionIdToProfile() || accountService.getUserBySession(sessionId) == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Sign in to your account first");
            resp.getWriter().println("<html> <body bgcolor=\"#deb887\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/autharisation\"> </body> </html>");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "afterLogging.html"));
        StringBuilder sb = new StringBuilder();
        while(reader.ready()) {
            sb.append(reader.readLine());
        }
        sb.append("Hi, " + accountService.getUserBySession(sessionId).getLogin());

        reader.close();
        resp.getWriter().println(sb);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String arg = req.getParameter("");
        if(arg == null) {
            return;
        }
        if(arg.equals("DELETE")) {
            String sessionId = req.getSession().getId();

            if (accountService.getUserBySession(sessionId) == null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("<html> <body bgcolor=\"#deb887\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"0; " +
                        "URL=http://localhost:8080/\"> </body> </html>");
                return;
            }
            accountService.deleteSession(sessionId);
            BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "out.html"));
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
                sb.append(reader.readLine());
            }

            reader.close();
            resp.getWriter().println(sb);
        }

    }
}
