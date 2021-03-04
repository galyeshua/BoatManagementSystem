package servlets;

import bms.engine.BMSEngine;
import bms.module.MemberView;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name="MainServelt", urlPatterns = "/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getServletPath();
        //Gson gson = new Gson();

        BMSEngine engine = (BMSEngine)getServletContext().getAttribute("engine");

        Collection<MemberView> members = engine.getMembers();

        try (PrintWriter out = resp.getWriter()) {
            req.getSession().setAttribute("username", "GAL");
            resp.addCookie(new Cookie("param", "val"));

            out.println("<html>");

            out.println("<head></head>");

            out.println("<body>");
            for (MemberView m: members){
                out.println("<p>" + m.getName() + "</p>");
            }
            out.println("</body>");

            out.println("</html>");
        }

    }

}
