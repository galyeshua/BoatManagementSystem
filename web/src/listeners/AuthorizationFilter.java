package listeners;

import com.google.gson.Gson;
import utils.AuthorizationUtils;
import utils.JsonUtils;
import utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "Authorization Filter", urlPatterns = "*")
public class AuthorizationFilter implements Filter {
    Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();
        boolean isManager = AuthorizationUtils.isManager(request);
        if(((HttpServletRequest) servletRequest).getServletPath().startsWith("/manage/")){
            if(!isManager){
                try(PrintWriter out = servletResponse.getWriter()) {
                    responseJson.status = "error";
                    responseJson.message = "Access denied";
                    out.println(gson.toJson(responseJson));
                }
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
