package bms.engine;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import bms.engine.list.manager.*;

import bms.module.*;
import bms.schema.generated.activity.Activities;
import bms.schema.generated.activity.Timeframe;
import bms.schema.generated.boat.Boats;
import bms.schema.generated.member.Members;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import static bms.schema.convertor.activityConvertor.activityFromSchemaActivity;
import static bms.schema.convertor.activityConvertor.schemaActivityFromActivity;
import static bms.schema.convertor.boatConvertor.boatFromSchemaBoat;
import static bms.schema.convertor.boatConvertor.schemaBoatFromBoat;
import static bms.schema.convertor.memberConvertor.memberFromSchemaMember;
import static bms.schema.convertor.memberConvertor.schemaMemberFromMember;


public class Engine implements BMSEngine{
    Member currentUser = null;
    BoatManager boats = new BoatManager();
    MemberManager members = new MemberManager();
    ActivityManager activities = new ActivityManager();
    ReservationManager reservations = new ReservationManager();

    List<String> xmlImportErrorList;

    private static void checkIfFileIsXml(File file){
        if (!file.exists())
            throw new Exceptions.FileNotFoundException();

        if (!file.getName().endsWith(".xml"))
            throw new Exceptions.IllegalFileTypeException();
    }

    private void createXmlFromObjects(String filePath, Class schemaClass, Object rootElement){
        File file = new File(filePath);
        if (file.exists())
            throw new Exceptions.FileAlreadyExistException();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(schemaClass);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(rootElement, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private Object ObjectsFromXml(String filePath, Class schemaClass) {
        File file = new File(filePath);
        checkIfFileIsXml(file);

        Object objects = null;
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(schemaClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Load schema for validation

            objects = jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return objects;
    }

        @Override
    public List<String> getXmlImportErrors(){
        return Collections.unmodifiableList(xmlImportErrorList);
    }




    @Override
    public MemberView getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(MemberView currentUser) {
        this.currentUser = new Member(currentUser);
    }







    @Override
    public void addBoat(Boat newBoat) throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException {
        boats.addBoat(newBoat);
    }

    @Override
    public void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException {
        boats.deleteBoat(serialNumber);
    }

    @Override
    public Collection<BoatView> getBoats() {
        return Collections.unmodifiableCollection(boats.getBoats());
    }

    @Override
    public Collection<BoatView> getAvailableBoats(){
        return Collections.unmodifiableCollection(
                boats.getBoats()
                        .stream()
                        .filter(b -> !b.getDisabled())
                        .filter(b -> !b.getPrivate())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<BoatView> getAvailableBoats(LocalDate date, Activity activity){
        List<ReservationView> approvedReservations = getApprovedReservationsByDate(date)
                .stream()
                .filter(r -> r.getAllocatedBoatID() != null)
                .filter(r -> r.getActivity().isOverlapping(activity))
                .collect(Collectors.toList());

        List<BoatView> availableBoats = new ArrayList<BoatView>(getAvailableBoats());
        for(BoatView boat : getAvailableBoats()){
            for (ReservationView res : approvedReservations)
                if(boat.getSerialNumber() == res.getAllocatedBoatID())
                    availableBoats.remove(boat);
        }

        return availableBoats;
    }

    @Override
    public BoatView getBoat(int serialNumber)  {
        return boats.getBoat(serialNumber);
    }

    @Override
    public BoatView getBoat(String name)  {
        return boats.getBoat(name);
    }

    public void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException {
        boats.updateBoat(newBoat);
    }


    @Override
    public void loadBoatsFromFile(String filePath) throws Exceptions.IllegalFileTypeException{
        xmlImportErrorList = new ArrayList<String>();

        Boats boats = (Boats) ObjectsFromXml(filePath, Boats.class);
        for (bms.schema.generated.boat.Boat schemaBoat : boats.getBoat()){
            try{
                Boat newBoat = boatFromSchemaBoat(schemaBoat);
                addBoat(newBoat);
            } catch (Exceptions.BoatAlreadyExistsException e) {
                xmlImportErrorList.add(e.getMessage());
            }
        }
    }

    @Override
    public void eraseAndLoadBoatsFromFile(String filePath){
        reservations.eraseAll();
        boats.eraseAll();
        loadBoatsFromFile(filePath);
    }


    @Override
    public void saveBoatsToFile(String filePath) throws Exceptions.IllegalFileTypeException{
        Boats boatsRootElement = new Boats();

        for (Boat systemBoat : boats.getBoats())
            boatsRootElement.getBoat().add(schemaBoatFromBoat(systemBoat));

        createXmlFromObjects(filePath, Boats.class, boatsRootElement);
    }








    @Override
    public void addMember(Member newMember) throws Exceptions.MemberAlreadyExistsException{
        members.addMember(newMember);
    }

    @Override
    public void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException {
        members.deleteMember(serialNumber);
    }

    @Override
    public Collection<MemberView> getMembers() {
        return Collections.unmodifiableCollection(members.getMembers());
    }

    @Override
    public Collection<MemberView> getMembers(String name) {
        return Collections.unmodifiableCollection(
                members.getMembers()
                        .stream()
                        .filter(m -> m.getName().equals(name))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public MemberView getMember(int serialNumber) {
        return members.getMember(serialNumber);
    }

    @Override
    public MemberView getMember(String email) {
        return members.getMember(email);
    }

    @Override
    public void updateMember(Member newMember) throws Exceptions.MemberNotFoundException{
        members.updateMember(newMember);
    }

    @Override
    public void loadMembersFromFile(String filePath) {
        xmlImportErrorList = new ArrayList<String>();

        Members members = (Members) ObjectsFromXml(filePath, Members.class);

        for (bms.schema.generated.member.Member schemaMember : members.getMember()){
            try{
                Member newMember = memberFromSchemaMember(schemaMember);
                addMember(newMember);
            } catch (Exceptions.MemberAlreadyExistsException e) {
                xmlImportErrorList.add(e.getMessage());
            }
        }
    }

    @Override
    public void eraseAndLoadMembersFromFile(String filePath) {
        reservations.eraseAll();
        members.eraseAll();
        loadMembersFromFile(filePath);
    }

    @Override
    public void saveMembersToFile(String filePath) throws Exceptions.IllegalFileTypeException, DatatypeConfigurationException {
        Members membersRootElement = new Members();

        for (Member systemMember : members.getMembers())
            membersRootElement.getMember().add(schemaMemberFromMember(systemMember));

        createXmlFromObjects(filePath, Members.class, membersRootElement);
    }






    @Override
    public void addActivity(Activity activity) throws Exceptions.ActivityAlreadyExistsException {
        activities.addActivity(activity);
    }

    @Override
    public void deleteActivity(int id) throws Exceptions.ActivityAlreadyExistsException {
        activities.deleteActivity(id);
    }

    @Override
    public Collection<ActivityView> getActivities() {
        return Collections.unmodifiableCollection(activities.getActivities());
    }

    @Override
    public ActivityView getActivity(int id) {
        return activities.getActivity(id);
    }


    public void updateActivity(Activity newActivity)
            throws Exceptions.ActivityNotFoundException  {
        activities.updateActivity(newActivity);
    }

    @Override
    public void loadActivitiesFromFile(String filePath) throws Exceptions.IllegalFileTypeException{
        xmlImportErrorList = new ArrayList<String>();

        Activities activities = (Activities) ObjectsFromXml(filePath, Activities.class);
        for (Timeframe timeframe : activities.getTimeframe()){
            try{
                Activity newActivity = activityFromSchemaActivity(timeframe);
                addActivity(newActivity);
            } catch (Exceptions.ActivityAlreadyExistsException e) {
                xmlImportErrorList.add(e.getMessage());
            }
        }
    }

    @Override
    public void eraseAndLoadActivitiesFromFile(String filePath){
        reservations.eraseAll();
        activities.eraseAll();
        loadActivitiesFromFile(filePath);
    }


    @Override
    public void saveActivitiesToFile(String filePath) throws Exceptions.IllegalFileTypeException{
        Activities activitiesRootElement = new Activities();

        for (Activity activity : activities.getActivities())
            activitiesRootElement.getTimeframe().add(schemaActivityFromActivity(activity));

        createXmlFromObjects(filePath, Activities.class, activitiesRootElement);
    }




    private void allocatePrivateBoatIfExist(Reservation reservation){
        // need to be change to stream and to boat type

        for(Integer memberID : reservation.getParticipants()){
            MemberView member = getMember(memberID);
            if(member.getHasPrivateBoat()){
                BoatView boat = getBoat(member.getBoatSerialNumber());
                if (boat.getAllowedNumOfRowers() == reservation.getParticipants().size())
                    reservation.setAllocatedBoatID(boat.getSerialNumber());
            }
        }
    }

    @Override
    public void addReservation(Reservation newReservation)
            throws Exceptions.IllegalReservationValueException, Exceptions.ReservationAlreadyExistsException{
        try{
            reservations.addReservation(newReservation);
            allocatePrivateBoatIfExist(newReservation);
        } catch (Exceptions.MemberAlreadyInApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Exceptions.IllegalReservationValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
    }

    @Override
    public void splitReservation(int id, List<Integer> newParticipantList){
        try{
            Reservation newReservation = new Reservation(getReservation(id));
            newReservation.setParticipants(newParticipantList);
            newReservation.allocateNewId();
            reservations.addReservation(newReservation);
        } catch (Exceptions.MemberAlreadyInApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Exceptions.IllegalReservationValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
    }


    @Override
    public void deleteReservation(int id)
    throws Exceptions.ReservationAlreadyExistsException{
        reservations.deleteReservation(id);
    }


    @Override
    public Collection<ReservationView> getReservations() {
        return Collections.unmodifiableCollection(reservations.getReservations());
    }


    private Stream<Reservation> getReservationsForCurrentUser(){
        int userID = currentUser.getSerialNumber();
        return reservations.getReservations()
                .stream()
                .filter(r -> r.getParticipants().contains(userID) || r.getOrderedMemberID()==userID);
    }


    @Override
    public Collection<ReservationView> getFutureUnapprovedReservationsForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isEqual(LocalDate.now()) || r.getActivityDate().isAfter(LocalDate.now() ))
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getFutureReservationsForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isEqual(LocalDate.now()) || r.getActivityDate().isAfter(LocalDate.now() ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsHistoryForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isBefore(LocalDate.now() ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                reservations.getReservations()
                        .stream()
                        .filter(r -> r.getActivityDate().equals(date))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                reservations.getReservations()
                        .stream()
                        .filter(r -> r.getActivityDate().isEqual(startDate) || r.getActivityDate().isAfter(startDate))
                        .filter(r -> r.getActivityDate().isBefore(finishDate))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getApprovedReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                getReservationsByDate(date)
                        .stream()
                        .filter(ReservationView::getIsApproved)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getApprovedReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                getReservationsForWeek(startDate)
                        .stream()
                        .filter(ReservationView::getIsApproved)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getUnapprovedReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                getReservationsByDate(date)
                        .stream()
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getUnapprovedReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                getReservationsForWeek(startDate)
                        .stream()
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void unapproveReservation(int id){
        Reservation reservation = reservations.getReservation(id);
        validateUserRole();

        if(reservation==null)
            throw new Exceptions.ReservationNotFoundException();

        if (reservation.getIsApproved())
            reservation.setAllocatedBoatID(null);
    }

    @Override
    public ReservationView getReservation(int id) { return reservations.getReservation(id);    }

    @Override
    public void updateReservation(Reservation newReservation) throws Exceptions.ReservationNotFoundException{
        Reservation safeReservation = newReservation;

        int reservationID = newReservation.getId();
        Reservation oldReservation = reservations.getReservation(reservationID);

        validateUserRole();
        if (!oldReservation.isMemberInReservation(currentUser.getSerialNumber()))
            throw new Exceptions.MemberAccessDeniedException();

        if (!currentUser.getManager()){
            safeReservation = new Reservation(oldReservation);

            safeReservation.setActivity(newReservation.getActivity());
            safeReservation.setActivityDate(newReservation.getActivityDate());
            safeReservation.setBoatType(newReservation.getBoatType());
            safeReservation.setParticipants(newReservation.getParticipants());
        }

        if (!safeReservation.equals(newReservation))
            throw new Exceptions.MemberAccessDeniedException();

        try{
            reservations.updateReservation(safeReservation);
        } catch (Exceptions.MemberAlreadyInApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Exceptions.IllegalReservationValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
    }


    @Override
    public void approveReservation(int reservationID, int boatID)
            throws Exceptions.BoatAlreadyAllocatedException, Exceptions.IllegalReservationValueException{
        Reservation currentReservation= reservations.getReservation(reservationID);
        Boat boat = boats.getBoat(boatID);

        int allowedNumOfRowers = boat.getAllowedNumOfRowers();

//        if(!getAvailableBoats(currentReservation.getActivityDate(), currentReservation.getActivity()).contains(boatID))
//            throw new Exceptions.BoatAlreadyAllocatedException();

        if (currentReservation.getParticipants().size() != allowedNumOfRowers)
            throw new Exceptions.IllegalReservationValueException("incorrect number of rowers. you have " +
                    currentReservation.getParticipants().size() + " rowers and you need " + allowedNumOfRowers +
                    " rowers for this boat");

        currentReservation.setAllocatedBoatID(boatID);
    }

    private void validateUserRole(){
        if (!currentUser.getManager())
            throw new Exceptions.MemberAccessDeniedException();
    }

}
