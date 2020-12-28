package bms.engine;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import bms.module.*;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;


public interface BMSEngine {
    List<String> getXmlImportErrors();


    boolean validateUserLogin(String email, String password);
    void loginUser(MemberView currentUser);
    void saveState() throws JAXBException;
    void loadState() throws Member.IllegalValueException, Member.AlreadyExistsException;

    void addBoat(Boat newBoat) throws Boat.AlreadyExistsException, Boat.IllegalValueException;
    void deleteBoat(int serialNumber) throws Boat.NotFoundException, Boat.AlreadyAllocatedException;
    Collection<BoatView> getBoats();
    Collection<BoatView> getAllAvailableBoats();
    Collection<BoatView> getAllAvailableBoats(LocalDate date, Activity activity);
    Collection<BoatView> getUnprivateAvailableBoats(LocalDate date, Activity activity);
    boolean boatIsAvailable(int boatSerialNumber, LocalDate date, Activity activity);
    BoatView getBoat(int serialNumber);
    BoatView getBoat(String name);
    void updateBoat(Boat newBoat) throws Boat.NotFoundException, Boat.AlreadyAllocatedException, Boat.BelongsToMember, Boat.AlreadyExistsException, Boat.IllegalValueException;
    void loadBoatsFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadBoatsFromFile(String filePath) throws JAXBException, SAXException;
    void saveBoatsToFile(String filePath) throws JAXBException, SAXException;
    boolean boatHaveFutureReservations(int boatSerialNumber);

    void addMember(Member newMember) throws Member.AlreadyExistsException, Member.IllegalValueException;
    void deleteMember(int serialNumber) throws Member.NotFoundException, Member.AlreadyHaveApprovedReservationsException, Member.AccessDeniedException;
    Collection<MemberView> getMembers();
    Collection<MemberView> getMembers(String name);
    MemberView getMember(int serialNumber);
    MemberView getMember(String email);
    void updateMember(Member newMember) throws Member.NotFoundException, Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyAllocatedException;
    void loadMembersFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadMembersFromFile(String filePath) throws JAXBException, SAXException, Member.IllegalValueException;
    void saveMembersToFile(String filePath) throws DatatypeConfigurationException, JAXBException, SAXException;
    boolean memberHaveFutureReservations(int memberSerialNumber);

    void addActivity(Activity newActivity) throws Activity.AlreadyExistsException, Activity.IllegalValueException;
    void deleteActivity(int id) throws Activity.NotFoundException;
    Collection<ActivityView> getActivities();
    ActivityView getActivity(int id);
    void updateActivity(Activity newActivity) throws Activity.NotFoundException, Activity.AlreadyExistsException, Activity.IllegalValueException;
    void loadActivitiesFromFile(String filePath) throws JAXBException, SAXException;
    void eraseAndLoadActivitiesFromFile(String filePath) throws JAXBException, SAXException;
    void saveActivitiesToFile(String filePath) throws JAXBException, SAXException;

    void addReservation(Reservation newReservation) throws Reservation.IllegalValueException, Reservation.AlreadyExistsException, Reservation.NotFoundException, Reservation.AlreadyApprovedException, Member.AlreadyExistsException;
    void splitReservation(int id, List<Integer> newParticipantList) throws Member.AlreadyExistsException, Reservation.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.NotFoundException, Reservation.AlreadyExistsException;
    void deleteReservation(int id) throws Reservation.NotFoundException, Reservation.AlreadyApprovedException;
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
    void unapprovedReservation(int id) throws Member.AccessDeniedException, Reservation.NotFoundException;
    ReservationView getReservation(int id);
    public void updateReservation(Reservation newReservation) throws Reservation.NotFoundException, Member.AccessDeniedException, Member.AlreadyExistsException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException;
    public void approveReservation(int reservationID, int boatID) throws Reservation.IllegalValueException, Boat.AlreadyAllocatedException;
}
