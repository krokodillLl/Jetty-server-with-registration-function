package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RegistrationServlet  extends HttpServlet {
    private final AccountService accountService;

    public RegistrationServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if(login == null || password == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if(accountService.getUserByLogin(login) != null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("This login already exists");
            return;
        }

        accountService.addNewUser(new UserProfile(login, password, email));

        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "registration.html"));
        StringBuilder sb = new StringBuilder();
        while(reader.ready()) {
            sb.append(reader.readLine());
        }
        sb.append("user " +  login + " successfully registered");
        reader.close();

        resp.getWriter().println(sb);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
