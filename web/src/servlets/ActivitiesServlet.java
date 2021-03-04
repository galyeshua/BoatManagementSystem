package servlets;

import bms.engine.Engine;
import bms.module.*;
import com.google.gson.Gson;
import utils.ContextUtils;
import utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.stream.Collectors;

@WebServlet(name = "ActivitiesServlet", urlPatterns = {"/user/activities", "/manage/activities"})
public class ActivitiesServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // for user and manager

        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        Collection<ActivityView> activities = engine.getActivities();

        responseJson.message = activities;

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            return;

        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Activity jsonActivity = gson.fromJson(lines, Activity.class);

        try {
            Activity newActivity = new Activity(jsonActivity.getName(), jsonActivity.getStartTime(), jsonActivity.getFinishTime());

            if(jsonActivity.getBoatType() != null)
                newActivity.setBoatType(jsonActivity.getBoatType());

            engine.addActivity(newActivity);
        } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            return;

        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Activity jsonActivity = gson.fromJson(lines, Activity.class);

        try {
            Activity newActivity = new Activity(engine.getActivity(jsonActivity.getId()));

            newActivity.setName(jsonActivity.getName());
            newActivity.setStartTime(jsonActivity.getStartTime());
            newActivity.setFinishTime(jsonActivity.getFinishTime());

            if(jsonActivity.getBoatType() != null)
                newActivity.setBoatType(jsonActivity.getBoatType());

            engine.updateActivity(newActivity);
        } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Activity.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Activity not found";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            return;

        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Activity jsonActivity = gson.fromJson(lines, Activity.class);

        try {
            engine.deleteActivity(jsonActivity.getId());
        } catch (Activity.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Activity not found";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

}
