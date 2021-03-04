package servlets;

import bms.engine.Engine;
import bms.module.*;
import com.google.gson.Gson;
import notifications.Notification;
import utils.ContextUtils;
import utils.JsonUtils;
import utils.NotificationUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@WebServlet(name="AllocationServlet", urlPatterns = {"/manage/allocation/boats", "/manage/allocation/approve", "/manage/allocation/unapprove"})
public class AllocationServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();
        ReservationView reservation = null;

        if(req.getServletPath().startsWith("/manage/allocation/boats")) {
            String reservationId = req.getParameter("id");
            try {
                reservation = engine.getReservation(Integer.parseInt(reservationId));
                if(reservation == null){
                    responseJson.status = "error";
                    responseJson.message = "reservation not found";
                }
            } catch (Exception e) {
                responseJson.status = "error";
                responseJson.message = "you must specified reservation id";
            }

            if (reservation != null) {
                boatsForReservation boats = new boatsForReservation();
                boats.setAllAvailableBoatsForTime(engine.getUnprivateAvailableBoats(reservation.getActivityDate(), reservation.getActivity()));
                boats.setSuitableBoatsForReservation(engine.findSuitableBoatByLevelOfParticipants(reservation));
                responseJson.message = boats;
            }
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        if(req.getServletPath().startsWith("/manage/allocation/approve"))
            responseJson = approveReservation(req, resp);

        if(req.getServletPath().startsWith("/manage/allocation/unapprove"))
            responseJson = unapproveReservation(req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    private JsonUtils.ResponseJson approveReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Reservation reservationJson = gson.fromJson(lines, Reservation.class);

        try {
            engine.approveReservation(reservationJson.getId(), reservationJson.getAllocatedBoatID());
            String message = "Your reservation for " + engine.getReservation(reservationJson.getId()).getActivityDate() + " approved by the manager";
            NotificationUtils.sendNotificationToReservationParticipants(req, reservationJson.getId(), message);
        } catch (Reservation.IllegalValueException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Boat.AlreadyAllocatedException e) {
            responseJson.status = "error";
            responseJson.message = "Boat already allocated";
        } catch (Notification.CannotBeEmpty cannotBeEmpty) {
            responseJson.status = "error";
            responseJson.message = "Message cannot be empty";
        }

        return responseJson;
    }

    private JsonUtils.ResponseJson unapproveReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Reservation reservationJson = gson.fromJson(lines, Reservation.class);

        try {
            engine.unapprovedReservation(reservationJson.getId());
            String message = "Your reservation for " + engine.getReservation(reservationJson.getId()).getActivityDate() + " was canceled by the manager";
            NotificationUtils.sendNotificationToReservationParticipants(req, reservationJson.getId(), message);
        } catch (Member.AccessDeniedException e) {
            responseJson.status = "error";
            responseJson.message = "You dont have permissions to do that";
        } catch (Reservation.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Reservation Not found";
        } catch (Notification.CannotBeEmpty cannotBeEmpty) {
            responseJson.status = "error";
            responseJson.message = "Message cannot be empty";
        }

        return responseJson;
    }

    public static class boatsForReservation{
        Integer reservationId;
        private Collection<BoatsServlet.responseBoat> suitableBoatsForReservation;
        private Collection<BoatsServlet.responseBoat> allAvailableBoatsForTime;

        public void setSuitableBoatsForReservation(Collection<BoatView> boatViews) {
            Collection<BoatsServlet.responseBoat> boats = new ArrayList<BoatsServlet.responseBoat>();
            boatViews.forEach(boatView -> boats.add(new BoatsServlet.responseBoat(boatView)));
            this.suitableBoatsForReservation = boats;
        }

        public void setAllAvailableBoatsForTime(Collection<BoatView> boatViews) {
            Collection<BoatsServlet.responseBoat> boats = new ArrayList<BoatsServlet.responseBoat>();
            boatViews.forEach(boatView -> boats.add(new BoatsServlet.responseBoat(boatView)));
            this.allAvailableBoatsForTime = boats;
        }
    }
}
