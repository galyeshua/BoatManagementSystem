package servlets;

import bms.engine.Engine;
import bms.module.Boat;
import bms.module.BoatView;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@WebServlet(name = "BoatsServlet", urlPatterns = {"/manage/boats"})
public class BoatsServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        Collection<responseBoat> boats = new ArrayList<responseBoat>();
        engine.getBoats().forEach(boatView -> boats.add(new responseBoat(boatView)));

        responseJson.message = boats;

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseBoat jsonBoat = gson.fromJson(lines, responseBoat.class);

        try {
            Boat newBoat = boatFromResponseBoat(jsonBoat);
            engine.addBoat(newBoat);
        } catch (Boat.IllegalValueException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Boat.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = "Boat Already Exists";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseBoat jsonBoat = gson.fromJson(lines, responseBoat.class);

        try {
            Boat newBoat = boatFromResponseBoat(jsonBoat);
            newBoat.setHasCoxswain(jsonBoat.hasCoxswain);
            newBoat.setNumOfPaddles(jsonBoat.numOfPaddles);
            engine.updateBoat(newBoat);
        } catch (Boat.AlreadyExistsException | Boat.IllegalValueException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Boat.BelongsToMember e) {
            responseJson.status = "error";
            responseJson.message = "The boat belongs to a member, please edit member's private boat first";
        } catch (Boat.AlreadyAllocatedException e) {
            responseJson.status = "error";
            responseJson.message = "Boat already allocated to another reservation";
        } catch (Boat.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Boat not found";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        responseBoat jsonBoat = gson.fromJson(lines, responseBoat.class);

        try {
            engine.deleteBoat(jsonBoat.serialNumber);
        } catch (Boat.AlreadyAllocatedException e) {
            responseJson.status = "error";
            responseJson.message = "Boat already allocated to another reservation";
        } catch (Boat.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Boat not found";
        } catch (Boat.BelongsToMember e) {
            responseJson.status = "error";
            responseJson.message = "The boat belongs to a member, please edit member's private boat first";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    Boat boatFromResponseBoat(responseBoat jsonBoat) throws Boat.IllegalValueException {
        Boat newBoat = new Boat(jsonBoat.serialNumber, jsonBoat.name, jsonBoat.boatType);
        newBoat.setDisabled(jsonBoat.isDisabled);
        newBoat.setMarine(jsonBoat.isMarine);
        newBoat.setPrivate(jsonBoat.isPrivate);
        newBoat.setWide(jsonBoat.isWide);
        return newBoat;
    }

    public static class responseBoat{
        public responseBoat(BoatView b){
            this.serialNumber = b.getSerialNumber();
            this.name = b.getName();
            this.boatType = b.getType();
            this.isPrivate = b.getPrivate();
            this.isWide = b.getWide();
            this.isMarine = b.getMarine();
            this.isDisabled = b.getDisabled();
            this.boatCode = b.getFormattedCode();
            this.numOfRowers = b.getNumOfRowers();
            this.numOfPaddles = b.getNumOfPaddles();
            this.hasCoxswain = b.getHasCoxswain();
        }

        private int serialNumber;
        private String name;
        private BoatView.BoatType boatType;
        private Boolean isPrivate;
        private Boolean isWide;
        private Boolean isMarine;
        private Boolean isDisabled;
        private String boatCode;
        private BoatView.Rowers numOfRowers;
        private BoatView.Paddles numOfPaddles;
        private Boolean hasCoxswain;
    }

}
