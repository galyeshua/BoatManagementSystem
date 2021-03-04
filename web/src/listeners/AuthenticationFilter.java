package listeners;

import com.google.gson.Gson;
import utils.JsonUtils;
import utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "Authentication Filter", urlPatterns = {"/user/*", "/manage/*", "/index.html"})
public class AuthenticationFilter implements Filter {
    Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean isUserLoggedIn = SessionUtils.getCurrentUser(request) != null;

        if (isUserLoggedIn) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if(request.getServletPath().startsWith("/manage") || request.getServletPath().startsWith("/user")){

                JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

                responseJson.status = "expired";
                responseJson.message = "Your session is not active. please login.";

                try(PrintWriter out = servletResponse.getWriter()) {
                    out.println(gson.toJson(responseJson));
                }
            } else{
                response.sendRedirect("/BMS/login.html");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
