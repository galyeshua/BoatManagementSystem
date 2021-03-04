package servlets;

import bms.engine.Engine;
import bms.exception.General;
import bms.module.Member;
import com.google.gson.Gson;
import org.xml.sax.SAXException;
import utils.ContextUtils;
import utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

@WebServlet(name="XmlServlet", urlPatterns = "/manage/xml")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class XmlServlet extends HttpServlet {
    Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        String type = req.getParameter("type");

        try(PrintWriter out = resp.getWriter()) {
            String xmlString = "";
            try {
                xmlString = getXmlStringByType(type, engine);

                if(xmlString == null){
                    responseJson.status = "error";
                    responseJson.message = "unknown file type";
                } else
                    responseJson.message = xmlString;

            } catch (JAXBException e) {
                responseJson.status = "error";
                responseJson.message = "Error: file is not valid. " + e.getLinkedException().getMessage();
            } catch (SAXException | DatatypeConfigurationException e) {
                responseJson.status = "error";
                responseJson.message = "General error";
            } catch (General.ListIsEmptyException e) {
                responseJson.status = "error";
                responseJson.message = "There is no info to export (list is empty).";
            }

            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ContextUtils.getEngine(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        Part filePart = req.getPart("uploadFile");
        boolean override = req.getParameter("override") != null && req.getParameter("override").equals("true");
        String type = req.getParameter("type");
        String fileContent = readFromInputStream(filePart.getInputStream());

        try (PrintWriter out = resp.getWriter()) {
            if(type == null){
                responseJson.status = "error";
                responseJson.message = "you must specify file type";
                out.println(gson.toJson(responseJson));
                return;
            }

            try{
                if(override)
                    uploadAndOverrideDataByType(engine, type, fileContent);
                else
                    uploadDataByType(engine, type, fileContent);

                if(engine.getXmlImportErrors().size() > 0){
                    responseJson.status = "warning";
                    responseJson.message = engine.getXmlImportErrors();
                }
            } catch (JAXBException e) {
                responseJson.status = "error";
                responseJson.message = "Error: file is not valid. " + e.getLinkedException().getMessage();
            } catch (SAXException e) {
                responseJson.status = "error";
                responseJson.message = "General error";
                e.printStackTrace();
            } catch (Member.IllegalValueException e) {
                responseJson.status = "error";
                responseJson.message = e.getMessage();
            }

            out.println(gson.toJson(responseJson));
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    public static String getXmlStringByType(String type, Engine engine)
            throws General.ListIsEmptyException, JAXBException, SAXException, DatatypeConfigurationException {
        if(type == null)
            return null;

        switch (type){
            case "boats":
                return engine.getXmlStringBoats();
            case "rowers":
                return engine.getXmlStringMembers();
            case "activities":
                return engine.getXmlStringActivities();
        }
        return null;
    }

    public static void uploadDataByType(Engine engine, String type, String fileContent) throws JAXBException, SAXException {
        switch (type){
            case "boats":
                engine.loadBoatsFromXmlString(fileContent);
                break;
            case "rowers":
                engine.loadMembersFromXmlString(fileContent);
                break;
            case "activities":
                engine.loadActivitiesFromXmlString(fileContent);
                break;
        }
    }

    public static void uploadAndOverrideDataByType(Engine engine, String type, String fileContent)
            throws JAXBException, SAXException, Member.IllegalValueException {
        switch (type){
            case "boats":
                engine.eraseAndLoadBoatsFromXmlString(fileContent);
                break;
            case "rowers":
                engine.eraseAndLoadMembersFromXmlString(fileContent);
                break;
            case "activities":
                engine.eraseAndLoadActivitiesFromXmlString(fileContent);
                break;
        }
    }
}
