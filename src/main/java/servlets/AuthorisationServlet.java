package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import mail.EmailSender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AuthorisationServlet extends HttpServlet {
    private final AccountService accountService;
    private final EmailSender sender;

    public AuthorisationServlet(AccountService accountService) {
        this.accountService = accountService;
        this.sender = new EmailSender("SomeEmailGmail@gmail.com", "SomePasswordForGmail");
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "authorisation.html"));
        StringBuilder stb = new StringBuilder();
        while(reader.ready()) {
            stb.append(reader.readLine());
        }
        reader.close();

        resp.getWriter().println(stb);

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String echo = req.getParameter("key");

        if(echo != null && login != null) {
            accountService.getUserByLogin(login).setEcho(echo);
        }

        if(login == null || password == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if(accountService.getUserByLogin(login) != null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("This login already exists");
            resp.getWriter().println("<html> <body bgcolor=\"#deb887\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/\"> </body> </html>");
            return;
        }

        accountService.addNewUser(new UserProfile(login, password, email));

        String key = accountService.getUserByLogin(login).getKey();
        sender.send("localhost",  "Please follow the link \r\n http://localhost:8080/authorisation?key=" + key + "&login=" + login, email);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
