package bms.engine;

import bms.module.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;

public class XmlHandler {

    private static void checkIfFileIsXml(File file){
        if (!file.exists())
            throw new Exceptions.FileNotFoundException();

        if (!file.getName().endsWith(".xml"))
            throw new Exceptions.IllegalFileTypeException();
    }

    static void createXmlFromObjects(String filePath, Class schemaClass, Object rootElement, String schemaFileName) throws JAXBException, SAXException {
        File xmlFile = new File(filePath);
        File schemaFile = new File("..\\engine\\resources\\" + schemaFileName);


        if (xmlFile.exists())
            throw new Exceptions.FileAlreadyExistException();

        JAXBContext jaxbContext = JAXBContext.newInstance(schemaClass);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Load schema for validation
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        jaxbMarshaller.setSchema(schema);

        jaxbMarshaller.marshal(rootElement, xmlFile);
    }


    protected static Object ObjectsFromXml(String filePath, Class schemaClass, String schemaFileName) throws JAXBException, SAXException{
        File xmlFile = new File(filePath);

        checkIfFileIsXml(xmlFile);

        Object objects = null;

        JAXBContext jaxbContext = JAXBContext.newInstance(schemaClass);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //Load schema for validation
        if(schemaFileName != null){
            File schemaFile = new File("..\\engine\\resources\\" + schemaFileName);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            jaxbUnmarshaller.setSchema(schema);
        }

        objects = jaxbUnmarshaller.unmarshal(xmlFile);

        return objects;
    }


    protected static void saveSystemState(Engine systemEngine, String filename) throws JAXBException {
        File xmlFile = new File(filename);

        JAXBContext jaxbContext = JAXBContext.newInstance(Engine.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(systemEngine, xmlFile);
    }


    protected static void loadSystemState(Engine systemEngine, String filename) throws Exceptions.ListCannotBeEmptyException {
        Engine engineFromFile;
        try {
            engineFromFile = (Engine) ObjectsFromXml("state.xml", Engine.class, null);

            for(BoatView boat : engineFromFile.getBoats())
                systemEngine.addBoat(new Boat(boat));

            for(MemberView member : engineFromFile.getMembers())
                systemEngine.addMember(new Member(member));

            for(ActivityView activity : engineFromFile.getActivities()){
                Activity newActivity = new Activity(activity.getName(), activity.getStartTime(), activity.getFinishTime());

                newActivity.setBoatType(activity.getBoatType());

                systemEngine.addActivity(newActivity);
            }

            for(ReservationView reservation : engineFromFile.getReservations())
            {
                Reservation newReservation = new Reservation(reservation.getActivity(), reservation.getActivityDate(),
                        reservation.getOrderDate(), reservation.getOrderedMemberID());

                newReservation.setParticipants(new ArrayList<Integer>(reservation.getParticipants()));
                newReservation.setBoatType(new ArrayList<Boat.Rowers>(reservation.getBoatType()));
                newReservation.setAllocatedBoatID(reservation.getAllocatedBoatID());

                systemEngine.addReservation(newReservation);
            }
        } catch (Exception e){
            throw new Exceptions.ListCannotBeEmptyException();
        }
    }




}
