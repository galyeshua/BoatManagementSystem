package bms.engine;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import bms.engine.list.manager.Exceptions;
import bms.module.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;


public interface BMSEngine {

    List<String> getXmlImportErrors();
//    MemberView getCurrentUser();

    boolean validateUserLogin(String email, String password);
    void loginUser(MemberView currentUser);


    void saveState() throws JAXBException;
    void loadState() throws JAXBException, SAXException;

    void addBoat(Boat newBoat) throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException;
    void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException;
    Collection<BoatView> getBoats();
    Collection<BoatView> getAllAvailableBoats();
    Collection<BoatView> getAllAvailableBoats(LocalDate date, Activity activity);
    Collection<BoatView> getUnprivateAvailableBoats(LocalDate date, Activity activity);
    boolean boatIsAvailable(int boatSerialNumber, LocalDate date, Activity activity);
    BoatView getBoat(int serialNumber);
    BoatView getBoat(String name);
    void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException, Exceptions.BoatAlreadyAllocatedException, Exceptions.BoatBelongsToMember;
    void loadBoatsFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadBoatsFromFile(String filePath) throws JAXBException, SAXException;
    void saveBoatsToFile(String filePath) throws JAXBException, SAXException;
    boolean boatHaveFutureReservations(int boatSerialNumber);



    void addMember(Member newMember) throws Exceptions.MemberAlreadyExistsException;
    void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException;
    Collection<MemberView> getMembers();
    Collection<MemberView> getMembers(String name);
    MemberView getMember(int serialNumber);
    MemberView getMember(String email);
    void updateMember(Member newMember) throws Exceptions.MemberNotFoundException;
    void loadMembersFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadMembersFromFile(String filePath) throws JAXBException, SAXException;
    void saveMembersToFile(String filePath) throws DatatypeConfigurationException, JAXBException, SAXException;
    boolean memberHaveFutureReservations(int memberSerialNumber);



    void addActivity(Activity newActivity) throws Exceptions.ActivityAlreadyExistsException;
    void deleteActivity(int id) throws Exceptions.ActivityNotFoundException;
    Collection<ActivityView> getActivities();
    ActivityView getActivity(int id);
    void updateActivity(Activity newActivity) throws Exceptions.ActivityNotFoundException;
    void loadActivitiesFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadActivitiesFromFile(String filePath) throws JAXBException, SAXException;
    void saveActivitiesToFile(String filePath) throws JAXBException, SAXException;



    void addReservation(Reservation newReservation);
    void splitReservation(int id, List<Integer> newParticipantList);
    void deleteReservation(int id);
    Collection<ReservationView> getReservations();
    Collection<ReservationView> getFutureUnapprovedReservationsForCurrentUser();
    Collection<ReservationView> getFutureReservationsForCurrentUser();
    Collection<ReservationView> getReservationsHistoryForCurrentUser();
    Collection<ReservationView> getReservationsByDate(LocalDate date);
    Collection<ReservationView> getReservationsForWeek(LocalDate startDate);
    Collection<ReservationView> getApprovedReservationsByDate(LocalDate date);
    Collection<ReservationView> getApprovedReservationsForWeek(LocalDate startDate);
    Collection<ReservationView> getAllFutureApprovedReservations();
    Collection<ReservationView> getUnapprovedReservationsByDate(LocalDate date);
    Collection<ReservationView> getUnapprovedReservationsForWeek(LocalDate startDate);
    void unapprovedReservation(int id);
    ReservationView getReservation(int id);
    public void updateReservation(Reservation newReservation) throws Exceptions.ReservationNotFoundException;
    public void approveReservation(int reservationID, int boatID);


}
