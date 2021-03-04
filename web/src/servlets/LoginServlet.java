package servlets;

import bms.module.Member;
import com.google.gson.Gson;
import utils.JsonUtils.ResponseJson;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        User newUser = gson.fromJson(lines, User.class);
        ResponseJson responseJson = new ResponseJson();

        try {
            SessionUtils.setUser(req, resp, newUser.email, newUser.password);
        } catch (Member.InvalidUsernameOrPasswordException e) {
            responseJson.status = "error";
            responseJson.message = "Invalid email or password";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }

    }

    static class User{
        String email;
        String password;
    }

}
