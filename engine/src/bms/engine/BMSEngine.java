package bms.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import bms.engine.list.manager.Exceptions;
import bms.module.*;

import javax.xml.datatype.DatatypeConfigurationException;


public interface BMSEngine {

    List<String> getXmlImportErrors();
    MemberView getCurrentUser();
    void setCurrentUser(MemberView currentUser);


    void addBoat(Boat newBoat) throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException;
    void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException;
    Collection<BoatView> getBoats();
    Collection<BoatView> getAvailableBoats();
    Collection<BoatView> getAvailableBoats(LocalDate date, Activity activity);
    BoatView getBoat(int serialNumber);
    BoatView getBoat(String name);
    void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException;
    void loadBoatsFromFile(String filePath);
    void eraseAndLoadBoatsFromFile(String filePath);
    void saveBoatsToFile(String filePath);


    void addMember(Member newMember) throws Exceptions.MemberAlreadyExistsException;
    void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException;
    Collection<MemberView> getMembers();
    Collection<MemberView> getMembers(String name);
    MemberView getMember(int serialNumber);
    MemberView getMember(String email);
    void updateMember(Member newMember) throws Exceptions.MemberNotFoundException;
    void loadMembersFromFile(String filePath);
    void eraseAndLoadMembersFromFile(String filePath);
    void saveMembersToFile(String filePath) throws DatatypeConfigurationException;


    void addActivity(Activity newActivity) throws Exceptions.ActivityAlreadyExistsException;
    void deleteActivity(int id) throws Exceptions.ActivityNotFoundException;
    Collection<ActivityView> getActivities();
    ActivityView getActivity(int id);
    void updateActivity(Activity newActivity) throws Exceptions.ActivityNotFoundException;
    void loadActivitiesFromFile(String filePath);
    void eraseAndLoadActivitiesFromFile(String filePath);
    void saveActivitiesToFile(String filePath);


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
    Collection<ReservationView> getUnapprovedReservationsByDate(LocalDate date);
    Collection<ReservationView> getUnapprovedReservationsForWeek(LocalDate startDate);
    void unapproveReservation(int id);
    ReservationView getReservation(int id);
    public void updateReservation(Reservation newReservation) throws Exceptions.ReservationNotFoundException;
    public void approveReservation(int reservationID, int boatID);


}
