package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FirstSheetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader("pages_html" + File.separator + "firstSheet.html"));
        StringBuilder sb = new StringBuilder();
        while(reader.ready()) {
            sb.append(reader.readLine());
        }
        reader.close();
        resp.getWriter().println(sb);
    }
}
