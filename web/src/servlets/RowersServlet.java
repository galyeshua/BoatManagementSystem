package servlets;

import bms.engine.Engine;
import bms.module.Boat;
import bms.module.Member;
import bms.module.MemberView;
import com.google.gson.Gson;
import utils.ContextUtils;
import utils.JsonUtils;
import utils.SessionUtils;

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

import static utils.SessionUtils.getCurrentUser;

@WebServlet(name = "RowersServlet", urlPatterns = {"/user/rowers", "/manage/rowers"})
public class RowersServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            processGetRequestForUser(req, resp);

        if(req.getServletPath().startsWith("/manage/"))
            processGetRequestForManager(req, resp);
    }


    private void processGetRequestForUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        JsonUtils.ResponseJson responseJson;

        String currentUserInfo = req.getParameter("currentUserInfo");

        if(currentUserInfo != null && currentUserInfo.toLowerCase().equals("true"))
            responseJson = infoOfCurrentUser(req, resp);
        else
            responseJson = infoOfUsers(req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    private JsonUtils.ResponseJson infoOfCurrentUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        Member currentUser = getCurrentUser(req);
        if(currentUser != null)
            responseJson.message = currentUser;
        else{
            responseJson.status = "error";
            responseJson.message = "no active session found";
        }

        return responseJson;
    }

    private JsonUtils.ResponseJson infoOfUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        String name = req.getParameter("name");

        Collection<MemberView> members;

        if(name != null)
            members = engine.getMembers(name);
        else
            members = engine.getMembers();

        Collection<memberNameAndSn> viewMembers = new ArrayList<memberNameAndSn>(members.size());
        for(MemberView m: members)
            viewMembers.add(new memberNameAndSn(m));

        responseJson.message = viewMembers;

        return responseJson;
    }

    private void processGetRequestForManager(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        Collection<MemberView> members = engine.getMembers();
        responseJson.message = members;

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

        Member jsonMember = gson.fromJson(lines, Member.class);

        try {
            Member newMember = memberFromJson(jsonMember, req);
            engine.addMember(newMember);
        } catch (Member.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = "Member Already Exists";
        } catch (Member.IllegalValueException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
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

        Member jsonMember = gson.fromJson(lines, Member.class);

        try {
            Member newMember = memberFromJson(jsonMember, req);
            engine.updateMember(newMember);
        } catch (Member.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Member Not Found";
        } catch (Member.IllegalValueException | Member.AlreadyExistsException e) {
            responseJson.status = "error";
            responseJson.message = e.getMessage();
        } catch (Boat.AlreadyAllocatedException e) {
            responseJson.status = "error";
            responseJson.message = "Cannot edit private boat while the member has approved reservation";
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

        Member jsonMember = gson.fromJson(lines, Member.class);

        try {
            engine.deleteMember(jsonMember.getSerialNumber());
        } catch (Member.NotFoundException e) {
            responseJson.status = "error";
            responseJson.message = "Member not found";
        } catch (Member.AlreadyHaveApprovedReservationsException e) {
            responseJson.status = "error";
            responseJson.message = "Cannot delete member with approved reservations";
        } catch (Member.AccessDeniedException e) {
            responseJson.status = "error";
            responseJson.message = "Access Denied. You cannot do this operation";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }


    Member memberFromJson(Member jsonMember, HttpServletRequest req) throws Member.IllegalValueException {
        Engine engine = ContextUtils.getEngine(req);
        boolean editProfile = req.getServletPath().startsWith("/user/");
        MemberView originalMember = editProfile ? getCurrentUser(req) : engine.getMember(jsonMember.getSerialNumber());
        Member newMember;

        if(jsonMember.getPassword() == null)
            jsonMember.setPassword(originalMember.getPassword());

        if(editProfile){
            newMember = getCurrentUser(req);

            newMember.setPhoneNumber(jsonMember.getPhoneNumber());
            newMember.setEmail(jsonMember.getEmail());
            newMember.setName(jsonMember.getName());
            newMember.setPassword(jsonMember.getPassword());
        } else{
            newMember = new Member(jsonMember.getSerialNumber(), jsonMember.getName(), jsonMember.getEmail(), jsonMember.getPassword());

            newMember.setAge(jsonMember.getAge());
            newMember.setNotes(jsonMember.getNotes());
            newMember.setLevel(jsonMember.getLevel());
            newMember.setBoatSerialNumber(jsonMember.getBoatSerialNumber());
            newMember.setPhoneNumber(jsonMember.getPhoneNumber());
            newMember.setManager(jsonMember.getManager());
        }

        return newMember;
    }

    public static class memberNameAndSn{
        public memberNameAndSn(MemberView member){
            this.name = member.getName();
            this.serialNumber = member.getSerialNumber();
        }

        private String name;
        private int serialNumber;
    }

}
