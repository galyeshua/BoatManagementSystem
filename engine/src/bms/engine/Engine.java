package bms.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bms.engine.list.manager.*;

import bms.exception.General;
import bms.module.*;
import bms.schema.generated.activity.Activities;
import bms.schema.generated.activity.Timeframe;
import bms.schema.generated.boat.Boats;
import bms.schema.generated.member.Members;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;

import static bms.engine.XmlHandler.ObjectsFromXmlString;
import static bms.engine.XmlHandler.xmlStringFromObjects;
import static bms.schema.convertor.activityConvertor.activityFromSchemaActivity;
import static bms.schema.convertor.activityConvertor.schemaActivityFromActivity;
import static bms.schema.convertor.boatConvertor.boatFromSchemaBoat;
import static bms.schema.convertor.boatConvertor.schemaBoatFromBoat;
import static bms.schema.convertor.memberConvertor.memberFromSchemaMember;
import static bms.schema.convertor.memberConvertor.schemaMemberFromMember;

@XmlRootElement(name="data")
public class Engine implements BMSEngine{
    Member currentUser = null;

    @XmlElement(required = true)
    BoatManager boats = new BoatManager();

    @XmlElement(required = true)
    MemberManager members = new MemberManager();

    @XmlElement(required = true)
    ActivityManager activities = new ActivityManager();

    @XmlElement(required = true)
    ReservationManager reservations = new ReservationManager();

    List<String> xmlImportErrorList = new ArrayList<String>();


    public void saveState() {
        try{
            XmlHandler.saveSystemState(this, "database.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void loadState()  {
        try{
            XmlHandler.loadSystemState(this, "database.xml");
        } catch (Reservation.ListCannotBeEmptyException e){
            try {
            Member firstMember = new Member(1, "admin", "admin@bms.com", "admin");
            firstMember.setManager(true);
                addMember(firstMember);
            } catch (Member.AlreadyExistsException | Member.IllegalValueException ignored) { ;
            }
        }
    }

    @Override
    public List<String> getXmlImportErrors(){
        return Collections.unmodifiableList(xmlImportErrorList);
    }

    private boolean validateUserLogin(String email, String password){
        Member member = members.getMember(email);
        return member!=null && member.getPassword().equals(password);
    }

    public void setCurrentUser(int userSerialNumber) {
        this.currentUser = members.getMember(userSerialNumber);
    }

    private void validateUserRole() throws Member.AccessDeniedException {
        if (!currentUser.getManager())
            throw new Member.AccessDeniedException();
    }

    @Override
    public void addBoat(Boat newBoat) throws Boat.AlreadyExistsException, Boat.IllegalValueException {
        boats.addBoat(newBoat);
        saveState();
    }

    @Override
    public void deleteBoat(int serialNumber) throws Boat.NotFoundException, Boat.AlreadyAllocatedException {
        if(boatHaveFutureReservations(serialNumber))
            throw new Boat.AlreadyAllocatedException();
        boats.deleteBoat(serialNumber);
        saveState();
    }

    @Override
    public Collection<BoatView> getBoats() {
        return Collections.unmodifiableCollection(boats.getBoats());
    }

    @Override
    public Collection<BoatView> getAllAvailableBoats(){
        return Collections.unmodifiableCollection(
                boats.getBoats()
                        .stream()
                        .filter(b -> !b.getDisabled())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<BoatView> getAllAvailableBoats(LocalDate date, Activity activity){
        return Collections.unmodifiableCollection(
                getAllAvailableBoats()
                        .stream()
                        .filter(b -> boatIsAvailable(b.getSerialNumber(), date, activity))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<BoatView> getUnprivateAvailableBoats(LocalDate date, Activity activity){
        return Collections.unmodifiableCollection(
                getAllAvailableBoats(date, activity)
                        .stream()
                        .filter(b -> !b.getPrivate())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean boatIsAvailable(int boatSerialNumber, LocalDate date, Activity activity){
        // return true is boat is undisabled and Available at specific time

        List<ReservationView> approvedReservations = getApprovedReservationsByDate(date)
                .stream()
                .filter(r -> r.getAllocatedBoatID() != null)
                .filter(r -> r.getActivity().isOverlapping(activity))
                .collect(Collectors.toList());

        for(ReservationView reservation : approvedReservations)
            if(reservation.getAllocatedBoatID() == boatSerialNumber)
                return false;

        return true;
    }

    @Override
    public BoatView getBoat(int serialNumber)  {
        return boats.getBoat(serialNumber);
    }

    @Override
    public BoatView getBoat(String name)  {
        return boats.getBoat(name);
    }

    public void updateBoat(Boat newBoat) throws Boat.NotFoundException, Boat.AlreadyAllocatedException, Boat.BelongsToMember, Boat.AlreadyExistsException, Boat.IllegalValueException {
        if(boatHaveFutureReservations(newBoat.getSerialNumber()))
            throw new Boat.AlreadyAllocatedException();

        for (Member member : members.getMembers()) {
            if (member.getHasPrivateBoat()) {
                if (member.getBoatSerialNumber() == newBoat.getSerialNumber())
                    throw new Boat.BelongsToMember();
            }
        }
        boats.updateBoat(newBoat);
        saveState();
    }



    @Override
    public void loadBoatsFromXmlString(String fileContent) throws JAXBException, SAXException {
        xmlImportErrorList = new ArrayList<String>();

        Boats boats = (Boats) ObjectsFromXmlString(fileContent, Boats.class, "resources/boats.xsd");

        for (bms.schema.generated.boat.Boat schemaBoat : boats.getBoat()){
            try{
                Boat newBoat = boatFromSchemaBoat(schemaBoat);
                addBoat(newBoat);
            } catch (Boat.AlreadyExistsException | Boat.IllegalValueException e) {
                int elementNumber = (int)boats.getBoat().indexOf(schemaBoat) + 1;
                xmlImportErrorList.add("Element " + elementNumber + ": " + e.getMessage());
            }
        }
        saveState();
    }

    @Override
    public void eraseAndLoadBoatsFromXmlString(String fileContent) throws JAXBException, SAXException {
        reservations.eraseAll();
        boats.eraseAll();
        loadBoatsFromXmlString(fileContent);
    }

    @Override
    public String getXmlStringBoats() throws JAXBException, SAXException, General.ListIsEmptyException {
        Boats boatsRootElement = new Boats();

        if (boats.getBoats().isEmpty())
            throw new General.ListIsEmptyException();

        for (Boat systemBoat : boats.getBoats())
            boatsRootElement.getBoat().add(schemaBoatFromBoat(systemBoat));

        System.out.println(boatsRootElement.getBoat().size());

        return xmlStringFromObjects(Boats.class, boatsRootElement, "resources/boats.xsd");
    }

    @Override
    public boolean boatHaveFutureReservations(int boatSerialNumber){
        for(ReservationView reservation : getAllFutureApprovedReservations())
            if(reservation.getAllocatedBoatID() == boatSerialNumber)
                return true;

        return false;
    }

    private void validatePrivateBoatDoesntChangedForUser(Member newMember) throws Boat.AlreadyAllocatedException {
        if(memberHaveFutureReservations(newMember.getSerialNumber())){
            Member originalMember = members.getMember(newMember.getSerialNumber());
            boolean isHasPrivateChanged = originalMember.getHasPrivateBoat() != newMember.getHasPrivateBoat();
            boolean isBoatSerialNumberChanged = originalMember.getBoatSerialNumber() != newMember.getBoatSerialNumber();

            if(isHasPrivateChanged || isBoatSerialNumberChanged)
                throw new Boat.AlreadyAllocatedException();
        }
    }

    private void validateMemberPrivateBoat(Member newMember) throws Member.IllegalValueException {
        if (newMember.getHasPrivateBoat()){
            Boat boat = boats.getBoat(newMember.getBoatSerialNumber());
            if(boat == null )
                throw new Member.IllegalValueException("Boat with id '" + newMember.getBoatSerialNumber() + "' does not exist");
            if(!boat.getPrivate())
                throw new Member.IllegalValueException("Boat with id '" + newMember.getBoatSerialNumber() + "' is not private");
        }
    }

    @Override
    public void addMember(Member newMember) throws Member.AlreadyExistsException, Member.IllegalValueException {
        validateMemberPrivateBoat(newMember);
        members.addMember(newMember);
        saveState();
    }

    @Override
    public void deleteMember(int serialNumber) throws Member.NotFoundException, Member.AlreadyHaveApprovedReservationsException, Member.AccessDeniedException {
        if(memberHaveFutureReservations(serialNumber))
            throw new Member.AlreadyHaveApprovedReservationsException();
        if(currentUser.getSerialNumber()==serialNumber)
            throw new Member.AccessDeniedException();
        members.deleteMember(serialNumber);
        saveState();
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
    public MemberView getMember(String email, String password) {
        if (validateUserLogin(email, password))
            return getMember(email);
        return null;
    }

    @Override
    public void updateMember(Member newMember) throws Member.NotFoundException, Member.IllegalValueException,
            Member.AlreadyExistsException, Boat.AlreadyAllocatedException {

        validateMemberPrivateBoat(newMember);
        validatePrivateBoatDoesntChangedForUser(newMember);

        members.updateMember(newMember);
        saveState();
    }

    @Override
    public void loadMembersFromXmlString(String fileContent) throws JAXBException, SAXException {
        xmlImportErrorList = new ArrayList<String>();

        Members members = (Members) ObjectsFromXmlString(fileContent, Members.class, "resources/members.xsd");

        for (bms.schema.generated.member.Member schemaMember : members.getMember()){
            try{
                Member newMember = memberFromSchemaMember(schemaMember);
                addMember(newMember);
            } catch (Member.AlreadyExistsException | Member.IllegalValueException e) {
                int elementNumber = (int)members.getMember().indexOf(schemaMember) + 1;
                xmlImportErrorList.add("Element " + elementNumber + ": " + e.getMessage());
            }
        }
        saveState();
    }

    @Override
    public void eraseAndLoadMembersFromXmlString(String fileContent) throws JAXBException, SAXException, Member.IllegalValueException {
        reservations.eraseAll();
        members.eraseAll();
        try {
            addMember(currentUser);
        } catch (Member.AlreadyExistsException ignored) { }
        loadMembersFromXmlString(fileContent);
    }

    @Override
    public String getXmlStringMembers() throws JAXBException, SAXException, DatatypeConfigurationException, General.ListIsEmptyException {
        Members membersRootElement = new Members();

        if (members.getMembers().isEmpty())
            throw new General.ListIsEmptyException();

        for (Member systemMember : members.getMembers())
            membersRootElement.getMember().add(schemaMemberFromMember(systemMember));

        return xmlStringFromObjects(Members.class, membersRootElement, "resources/members.xsd");
    }

    @Override
    public boolean memberHaveFutureReservations(int memberSerialNumber){

        for(ReservationView reservation : getAllFutureApprovedReservations())
            if(reservation.isMemberInReservation(memberSerialNumber) || reservation.getOrderedMemberID()==memberSerialNumber)
                return true;

        return false;
    }

    @Override
    public void addActivity(Activity activity) throws Activity.AlreadyExistsException, Activity.IllegalValueException {
        activities.addActivity(activity);
        saveState();
    }

    @Override
    public void deleteActivity(int id) throws Activity.NotFoundException {
        activities.deleteActivity(id);
        saveState();
    }

    @Override
    public Collection<ActivityView> getActivities() {
        return Collections.unmodifiableCollection(activities.getActivities());
    }

    @Override
    public ActivityView getActivity(int id) {
        return activities.getActivity(id);
    }


    public void updateActivity(Activity newActivity) throws Activity.NotFoundException, Activity.AlreadyExistsException, Activity.IllegalValueException {
        activities.updateActivity(newActivity);
        saveState();
    }

    @Override
    public void loadActivitiesFromXmlString(String fileContent) throws JAXBException, SAXException {
        xmlImportErrorList = new ArrayList<String>();

        Activities activities = (Activities) ObjectsFromXmlString(fileContent, Activities.class, "resources/activities.xsd");
        for (Timeframe timeframe : activities.getTimeframe()){
            try{
                Activity newActivity = activityFromSchemaActivity(timeframe);
                addActivity(newActivity);
            } catch (Activity.AlreadyExistsException | Activity.IllegalValueException e) {
                int elementNumber = (int)activities.getTimeframe().indexOf(timeframe) + 1;
                xmlImportErrorList.add("Element " + elementNumber + ": " + e.getMessage());
            }
        }
        saveState();
    }

    @Override
    public void eraseAndLoadActivitiesFromXmlString(String fileContent) throws JAXBException, SAXException {
        reservations.eraseAll();
        activities.eraseAll();
        loadActivitiesFromXmlString(fileContent);
    }

    @Override
    public String getXmlStringActivities() throws JAXBException, SAXException, General.ListIsEmptyException {
        Activities activitiesRootElement = new Activities();

        if (activities.getActivities().isEmpty())
            throw new General.ListIsEmptyException();

        for (Activity activity : activities.getActivities())
            activitiesRootElement.getTimeframe().add(schemaActivityFromActivity(activity));

        return xmlStringFromObjects(Activities.class, activitiesRootElement, "resources/activities.xsd");
    }

    @Override
    public Collection<BoatView> getAllAvailableBoatsForReservation(ReservationView reservation){
        return Collections.unmodifiableCollection(getAllAvailableBoats()
                .stream()
                .filter(b -> b.getAllowedNumOfRowers() == reservation.getParticipants().size())
                .filter(b -> reservation.getBoatType().contains(b.getNumOfRowers()))
                .collect(Collectors.toList()));
    }

    private BoatView findSuitPrivateBoatOfParticipants(ReservationView reservation){
        List<BoatView> suitPrivateBoats = getAllAvailableBoatsForReservation(reservation)
                .stream()
                .filter(BoatView::getPrivate)
                .collect(Collectors.toList());

        for (Integer memberID : reservation.getParticipants()){
            MemberView member = getMember(memberID);
            if (member != null && member.getHasPrivateBoat()){
                BoatView boat = getBoat(member.getBoatSerialNumber());
                if(suitPrivateBoats.contains(boat))
                    return boat;
            }
        }
        return null;
    }

    private void allocatePrivateBoatIfExist(Reservation reservation){
        BoatView boat = findSuitPrivateBoatOfParticipants(reservation);
        if (boat != null) {
            reservation.setAllocatedBoatID(boat.getSerialNumber());
        }
        saveState();
    }

    @Override
    public void addReservation(Reservation newReservation)
            throws Reservation.IllegalValueException, Reservation.AlreadyExistsException,
            Reservation.NotFoundException, Reservation.AlreadyApprovedException, Member.AlreadyExistsException {
        try{
            allocatePrivateBoatIfExist(newReservation);
            reservations.addReservation(newReservation);
        } catch (Member.AlreadyHaveApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Reservation.IllegalValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
        saveState();
    }

    @Override
    public void splitReservation(int id, List<Integer> newParticipantList) throws Member.AlreadyExistsException,
            Reservation.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.NotFoundException,
            Reservation.AlreadyExistsException {

        try{
            Reservation newReservation = new Reservation(getReservation(id));
            newReservation.setParticipants(newParticipantList);
            newReservation.allocateNewId();
            reservations.addReservation(newReservation);
        } catch (Member.AlreadyHaveApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Reservation.IllegalValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
        saveState();
    }

    @Override
    public void deleteReservation(int id) throws Reservation.NotFoundException, Reservation.AlreadyApprovedException {
        reservations.deleteReservation(id);
        saveState();
    }

    @Override
    public Collection<ReservationView> getAllFutureApprovedReservations(){
        return Collections.unmodifiableCollection(
                reservations.getReservations().stream()
                .filter(r -> r.getActivityDate().isEqual(LocalDate.now()) || r.getActivityDate().isAfter(LocalDate.now()))
                .filter(ReservationView::getIsApproved)
                .collect(Collectors.toList()));
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
    public void unapprovedReservation(int id) throws Member.AccessDeniedException, Reservation.NotFoundException {
        Reservation reservation = reservations.getReservation(id);
        validateUserRole();

        if(reservation==null)
            throw new Reservation.NotFoundException();

        if (reservation.getIsApproved()){
            reservation.setAllocatedBoatID(null);
        }

        saveState();
    }

    @Override
    public ReservationView getReservation(int id) { return reservations.getReservation(id);    }

    @Override
    public void updateReservation(Reservation newReservation) throws Reservation.NotFoundException,
            Member.AccessDeniedException, Member.AlreadyExistsException, Reservation.AlreadyApprovedException,
            Reservation.IllegalValueException {

        Reservation safeReservation = newReservation;
        int reservationID = newReservation.getId();
        Reservation oldReservation = reservations.getReservation(reservationID);

        validateUserRole();

        if (!currentUser.getManager()){
            safeReservation = new Reservation(oldReservation);

            safeReservation.setActivity(newReservation.getActivity());
            safeReservation.setActivityDate(newReservation.getActivityDate());
            safeReservation.setBoatType(newReservation.getBoatType());
            safeReservation.setParticipants(newReservation.getParticipants());
        }

        if (!safeReservation.equals(newReservation))
            throw new Member.AccessDeniedException();

        try{
            reservations.updateReservation(safeReservation);
        } catch (Member.AlreadyHaveApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Reservation.IllegalValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
        saveState();
    }

    @Override
    public void approveReservation(int reservationID, int boatID)
            throws Reservation.IllegalValueException {

        Reservation currentReservation= reservations.getReservation(reservationID);
        Boat boat = boats.getBoat(boatID);

        int allowedNumOfRowers = boat.getAllowedNumOfRowers();

        if (currentReservation.getParticipants().size() != allowedNumOfRowers)
            throw new Reservation.IllegalValueException("incorrect number of rowers. you have " +
                    currentReservation.getParticipants().size() + " rowers and you need " + allowedNumOfRowers +
                    " rowers for this boat");

        currentReservation.setAllocatedBoatID(boatID);
        saveState();
    }
}
