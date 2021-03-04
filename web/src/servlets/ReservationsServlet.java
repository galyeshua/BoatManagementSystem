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
import javax.swing.text.DateFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static utils.SessionUtils.getCurrentUser;

@WebServlet(name="ReservationsServlet", urlPatterns = {"/user/reservations", "/manage/reservations",
        "/manage/reservations/split"})
public class ReservationsServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getServletPath().startsWith("/user/"))
            processGetRequestForUser(req, resp);

        if(req.getServletPath().startsWith("/manage/"))
            processGetRequestForManager(req, resp);

    }

    private void processGetRequestForUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        String forTime = req.getParameter("for_time");
        String onlyUnapproved = req.getParameter("only_unapproved");
        userReservationAction action = userReservationAction.calculateAction(forTime, onlyUnapproved);

        Collection<ReservationView> reservations = null;

        switch (action){
            case HISTORY:
                reservations = engine.getReservationsHistoryForCurrentUser();
                break;
            case FUTURE_ALL:
                reservations = engine.getFutureReservationsForCurrentUser();
                break;
            case FUTURE_UNAPPROVED:
                reservations = engine.getFutureUnapprovedReservationsForCurrentUser();
                break;
        }

        responseJson.message = reservations;

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    enum userReservationAction{
        FUTURE_UNAPPROVED, FUTURE_ALL, HISTORY;

        public static userReservationAction calculateAction(String forTime, String onlyUnapproved) {
            if(forTime != null && forTime.toLowerCase().equals("history"))
                return HISTORY;
            else if(onlyUnapproved != null && onlyUnapproved.toLowerCase().equals("true"))
                return FUTURE_UNAPPROVED;
            else
                return FUTURE_ALL;
        }
    }


    private void processGetRequestForManager(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        String dateString = req.getParameter("date");
        String isForWeekString = req.getParameter("is_for_week");
        String reservationStatusString = req.getParameter("status");
        managerReservationAction action = managerReservationAction.calculateAction(dateString, isForWeekString, reservationStatusString);
        LocalDate date;

        try{
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e){
            date = LocalDate.now();
        }

        Collection<ReservationView> reservations = null;

        switch (action){
            case ALL_RESERVATIONS_WEEK:
                reservations = engine.getReservationsForWeek(date);
                break;
            case ALL_RESERVATIONS_DAY:
                reservations = engine.getReservationsByDate(date);
                break;
            case UNAPPROVED_RESERVATIONS_WEEK:
                reservations = engine.getUnapprovedReservationsForWeek(date);
                break;
            case UNAPPROVED_RESERVATIONS_DAY:
                reservations = engine.getUnapprovedReservationsByDate(date);
                break;
            case APPROVED_RESERVATIONS_WEEK:
                reservations = engine.getApprovedReservationsForWeek(date);
                break;
            case APPROVED_RESERVATIONS_DAY:
                reservations = engine.getApprovedReservationsByDate(date);
                break;
        }

        responseJson.message = reservations;

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }


    enum managerReservationAction{
        ALL_RESERVATIONS_WEEK, ALL_RESERVATIONS_DAY,
        UNAPPROVED_RESERVATIONS_WEEK, UNAPPROVED_RESERVATIONS_DAY,
        APPROVED_RESERVATIONS_WEEK, APPROVED_RESERVATIONS_DAY;

        enum status{
            All, UNAPPROVED, APPROVED
        }

        public static managerReservationAction calculateAction(String dateString, String isForWeekString, String reservationStatusString) {

            if(dateString == null || isForWeekString == null || reservationStatusString == null)
                return ALL_RESERVATIONS_WEEK;

            boolean isForWeek = isForWeekString.toLowerCase().equals("true");
            status reservationStatus = reservationStatusString.toLowerCase().equals("all") ? status.All :
                    (reservationStatusString.toLowerCase().equals("approved") ? status.APPROVED : status.UNAPPROVED);

            switch (reservationStatus){
                case All:
                    if(isForWeek)
                        return ALL_RESERVATIONS_WEEK;
                    return ALL_RESERVATIONS_DAY;
                case APPROVED:
                    if(isForWeek)
                        return APPROVED_RESERVATIONS_WEEK;
                    return APPROVED_RESERVATIONS_DAY;
                case UNAPPROVED:
                    if(isForWeek)
                        return UNAPPROVED_RESERVATIONS_WEEK;
                    return UNAPPROVED_RESERVATIONS_DAY;
            }
            return ALL_RESERVATIONS_WEEK;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseReservation jsonReservation = gson.fromJson(lines, responseReservation.class);

        try{
            Reservation newReservation = reservationFromResponseReservation(jsonReservation, req, true);
            engine.addReservation(newReservation);
        } catch (Activity.IllegalValueException | Member.AlreadyExistsException | Reservation.IllegalValueException |
                Reservation.AlreadyApprovedException | Reservation.AlreadyExistsException | Boat.IllegalValueException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Reservation.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Reservation not found";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        if(req.getServletPath().startsWith("/manage/reservations/split"))
            responseJson = processPutSplitRequest(req, resp);
        else
            responseJson = processPutGeneralRequest(req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    private JsonUtils.ResponseJson processPutSplitRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseReservation jsonReservation = gson.fromJson(lines, responseReservation.class);

        try {
            engine.splitReservation(jsonReservation.id, jsonReservation.participants);
        } catch (Member.AlreadyExistsException | Reservation.IllegalValueException |
                Reservation.AlreadyExistsException | Reservation.AlreadyApprovedException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Reservation.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Reservation not found";
        }

        return responseJson;
    }

    private JsonUtils.ResponseJson processPutGeneralRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        boolean isUserRequest = req.getServletPath().startsWith("/user/");

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseReservation jsonReservation = gson.fromJson(lines, responseReservation.class);

        try{
            Reservation baseReservation = new Reservation(engine.getReservation(jsonReservation.id));
            Reservation reservationJson = reservationFromResponseReservation(jsonReservation, req, isUserRequest);

            if(!isUserRequest)
                findChangesAndSendNotifications(req, baseReservation, reservationJson);

            baseReservation.setBoatType(reservationJson.getBoatType());
            baseReservation.setParticipants(reservationJson.getParticipants());
            baseReservation.setActivity(reservationJson.getActivity());
            baseReservation.setActivityDate(reservationJson.getActivityDate());

            engine.updateReservation(baseReservation);
        } catch (Boat.IllegalValueException | Activity.IllegalValueException | Reservation.IllegalValueException |
                Reservation.AlreadyApprovedException | Member.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Reservation.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Reservation not found";
        } catch (Member.AccessDeniedException e) {
            responseJson.status = "error";
            responseJson.message = "You dont have permission to do that";
        } catch (Notification.CannotBeEmpty cannotBeEmpty) {
            responseJson.status = "error";
            responseJson.message = "Message cannot be empty";
        }

        return responseJson;
    }

    private static void findChangesAndSendNotifications(HttpServletRequest req, Reservation originalReservation, Reservation newReservation) throws Notification.CannotBeEmpty {
        Engine engine = ContextUtils.getEngine(req);
        String message;

        if(!originalReservation.getParticipants().equals(newReservation.getParticipants())){
            message = "The manager changed the participants list (reservation for " + engine.getReservation(originalReservation.getId()).getActivityDate() + ")";
            NotificationUtils.sendNotificationToReservationParticipants(req, originalReservation.getId(), message);
        }

        if(!originalReservation.getBoatType().equals(newReservation.getBoatType())){
            message = "The manager changed the requested boat list (reservation for " + engine.getReservation(originalReservation.getId()).getActivityDate() + ")";
            NotificationUtils.sendNotificationToReservationParticipants(req, originalReservation.getId(), message);
        }

        if(!originalReservation.getActivity().equals(newReservation.getActivity())){
            message = "The manager changed the reservation activity (reservation for " + engine.getReservation(originalReservation.getId()).getActivityDate() + ")";
            NotificationUtils.sendNotificationToReservationParticipants(req, originalReservation.getId(), message);
        }

        if(!originalReservation.getActivityDate().equals(newReservation.getActivityDate())){
            message = "The manager changed the reservation activity date (from "
                    + engine.getReservation(originalReservation.getId()).getActivityDate() + " to "
                    + engine.getReservation(newReservation.getId()).getActivityDate() + ")";
            NotificationUtils.sendNotificationToReservationParticipants(req, originalReservation.getId(), message);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        boolean isUserRequest = req.getServletPath().startsWith("/user/");

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseReservation reservationJson= gson.fromJson(lines, responseReservation.class);

        try {
            ReservationView tmpReservation = engine.getReservation(reservationJson.id);

            engine.deleteReservation(reservationJson.id);
            if(!isUserRequest){
                String message = "Your reservation for " + tmpReservation.getActivityDate() + " has been deleted by the manager";
                NotificationUtils.sendNotificationToReservationParticipants(req, tmpReservation, message);
            }
        } catch (Reservation.AlreadyApprovedException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Reservation.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Reservation not found";
        } catch (Notification.CannotBeEmpty cannotBeEmpty) {
            responseJson.status = "error";
            responseJson.message = "Message cannot be empty";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }


    Reservation reservationFromResponseReservation(responseReservation jsonReservation, HttpServletRequest req, boolean isUserRequest)
            throws Member.AlreadyExistsException, Boat.IllegalValueException, Activity.IllegalValueException {
        Engine engine = ContextUtils.getEngine(req);
        Activity activity;
        if(jsonReservation.activity.getStartTime() == null)
            activity = new Activity(engine.getActivity(jsonReservation.activity.getId()));
        else
            activity = new Activity(jsonReservation.activity.getStartTime(), jsonReservation.activity.getFinishTime());

        Reservation newReservation = new Reservation(activity, jsonReservation.activityDate, LocalDateTime.now(), getCurrentUser(req).getSerialNumber());

        // only for non manager request
        if(isUserRequest && jsonReservation.isForCurrentUser)
            newReservation.addParticipant(getCurrentUser(req).getSerialNumber());

        for (Integer memberID: jsonReservation.participants)
            newReservation.addParticipant(memberID);

        for (BoatView.Rowers boatType: jsonReservation.boatType)
            newReservation.addBoatType(boatType);

        return newReservation;
    }

    public static class responseReservation{
        private Integer id;
        private Activity activity;
        private LocalDate activityDate;
        private List<BoatView.Rowers> boatType;
        private boolean isForCurrentUser;
        private List<Integer> participants;

        @Override
        public String toString() {
            return "responseReservation{" +
                    "id=" + id +
                    ", activity=" + activity +
                    ", activityDate=" + activityDate +
                    ", boatType=" + boatType +
                    ", isForCurrentUser=" + isForCurrentUser +
                    ", participants=" + participants +
                    '}';
        }
    }

}
