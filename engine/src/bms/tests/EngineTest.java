package bms.tests;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.*;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class EngineTest {
    private void deleteDatabase(){
        File db = new File("database.xml");
        if(db.exists())
            db.delete();
    }

    @Test
    public void getListOfMembersShouldBeEmpty(){
        BMSEngine engine = new Engine();
        Collection<MemberView> members = engine.getMembers();

        assertEquals(0, members.size());

        deleteDatabase();
    }

    @Test
    public void getListOfMembersAfterAddMemberSizeShouldBeOne() throws Member.IllegalValueException, Member.AlreadyExistsException {
        BMSEngine engine = new Engine();

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);
        Collection<MemberView> members = engine.getMembers();

        assertEquals(1, members.size());

        deleteDatabase();
    }

    @Test(expected = Member.AlreadyExistsException.class)
    public void addTwoMemberWithSameDetailsShouldThrowException() throws Member.IllegalValueException, Member.AlreadyExistsException {
        BMSEngine engine = new Engine();
        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);
        engine.addMember(m);

        deleteDatabase();
    }

    @Test
    public void getMemberThatNotExistsShouldReturnNull() {
        BMSEngine engine = new Engine();

        assertNull(engine.getMember(1));

        deleteDatabase();
    }

    @Test
    public void addAndGetMemberShouldReturnMemberViewObject() throws Member.AlreadyExistsException, Member.IllegalValueException {
        BMSEngine engine = new Engine();

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);

        MemberView returnedMember = engine.getMember(1);
        assertEquals(returnedMember, engine.getMember("gal@gmail.com"));
        assertEquals("gal", returnedMember.getName());
        assertEquals("gal@gmail.com", returnedMember.getEmail());
        assertEquals("1234", returnedMember.getPassword());

        deleteDatabase();
    }

    @Test
    public void updateMemberNameShouldChangedTheName() throws Member.AlreadyExistsException, Member.IllegalValueException, Member.NotFoundException, Boat.AlreadyAllocatedException {
        BMSEngine engine = new Engine();

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);

        MemberView returnedMember = engine.getMember(1);
        assertEquals("gal", returnedMember.getName());

        Member tmpMember = new Member(returnedMember);
        tmpMember.setName("newName");
        engine.updateMember(tmpMember);

        assertEquals("newName", engine.getMember(1).getName());

        deleteDatabase();
    }

    @Test(expected = Member.AlreadyExistsException.class)
    public void updateMemberEmailOfAnotherUserShouldReturnAlreadyExist() throws Member.AlreadyExistsException, Member.IllegalValueException, Member.NotFoundException, Boat.AlreadyAllocatedException {
        BMSEngine engine = new Engine();

        Member m1 = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m1);
        Member m2 = new Member(2, "admin", "admin@gmail.com", "1234");
        engine.addMember(m2);

        Member tmpMember = new Member(engine.getMember(1));
        tmpMember.setEmail("admin@gmail.com");
        engine.updateMember(tmpMember);

        deleteDatabase();
    }

    @Test
    public void getMemberAfterDeletingShouldReturnNull() throws Member.AccessDeniedException, Member.NotFoundException, Member.AlreadyHaveApprovedReservationsException, Member.AlreadyExistsException, Member.IllegalValueException {
        BMSEngine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);
        engine.setCurrentUser(managerUser);

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);

        assertEquals("gal", engine.getMember(1).getName());
        engine.deleteMember(1);
        assertNull(engine.getMember(1));

        deleteDatabase();
    }


    @Test(expected = Member.AccessDeniedException.class)
    public void deleteLoggedInUserShouldReturnAccessDenied() throws Member.AccessDeniedException, Member.NotFoundException, Member.AlreadyHaveApprovedReservationsException, Member.AlreadyExistsException, Member.IllegalValueException {
        BMSEngine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);
        engine.setCurrentUser(managerUser);

        engine.deleteMember(100);

        deleteDatabase();
    }

    @Test
    public void getMembersByNameWithUsersShouldReturnListOfTwo() throws Member.IllegalValueException, Member.AlreadyExistsException {
        BMSEngine engine = new Engine();

        Member m1 = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m1);
        Member m2 = new Member(2, "gal", "gal2@gmail.com", "1234");
        engine.addMember(m2);
        Member m3 = new Member(3, "Benny", "benny@gmail.com", "1234");
        engine.addMember(m3);

        Collection<MemberView> membersByName = engine.getMembers("gal");
        Iterator<MemberView> members = membersByName.iterator();
        assertEquals(members.next(), m1);
        assertEquals(members.next(), m2);
        assertFalse(members.hasNext());

        deleteDatabase();
    }

    @Test
    public void getMembersByNameWithoutUsersShouldReturnEmptyList() {
        BMSEngine engine = new Engine();

        Collection<MemberView> members = engine.getMembers("gal");
        assertTrue(members.isEmpty());

        deleteDatabase();
    }


    @Test
    public void addReservationForUserWithPrivateBoatShouldApprovedAutomatically() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException {
        BMSEngine engine = new Engine();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Boat galsBoat = new Boat(5, "5", BoatView.BoatType.SINGLE);
        galsBoat.setPrivate(true);
        engine.addBoat(galsBoat);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setBoatSerialNumber(5);
        engine.addMember(gal);

        engine.setCurrentUser(gal);

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
        1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addParticipant(1);
        engine.addReservation(galsReservation);

        Iterator<ReservationView> reservations = engine.getReservations().iterator();
        assertTrue(reservations.next().getIsApproved());

        deleteDatabase();
    }


    @Test
    public void addReservationForUserWithPrivateBoatOfDifferentTypeShouldNotApproved() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException {
        BMSEngine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Boat galsBoat = new Boat(5, "5", BoatView.BoatType.SINGLE);
        galsBoat.setPrivate(true);
        engine.addBoat(galsBoat);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setBoatSerialNumber(5);
        engine.addMember(gal);

        engine.setCurrentUser(gal);

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(1);
        engine.addReservation(galsReservation);

        Iterator<ReservationView> reservations = engine.getReservations().iterator();
        assertFalse(reservations.next().getIsApproved());

        deleteDatabase();
    }


    @Test
    public void addTwoReservationsThatOneWithPrivateBoatShouldApprovedOneAutomatically() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException {
        BMSEngine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Boat galsBoat = new Boat(5, "5", BoatView.BoatType.DOUBLE);
        galsBoat.setPrivate(true);
        engine.addBoat(galsBoat);
        Boat publicBoat1 = new Boat(24, "24", BoatView.BoatType.COXED_DOUBLE);
        engine.addBoat(publicBoat1);
        Boat publicBoat2 = new Boat(13, "13", BoatView.BoatType.PAIR);
        engine.addBoat(publicBoat2);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setBoatSerialNumber(5);
        engine.addMember(gal);
        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);
        Member benny = new Member(3, "benny", "benny@gmail.com", "1234");
        engine.addMember(benny);

        engine.setCurrentUser(gal);
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addBoatType(BoatView.Rowers.TWO);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        Reservation bennysReservation = new Reservation(new Activity(LocalTime.parse("12:00", timeFormatter), LocalTime.parse("13:00", timeFormatter)),
                LocalDate.now().plusDays(3),
                LocalDateTime.now(),
                3);
        bennysReservation.addParticipant(3);
        engine.addReservation(bennysReservation);


        Iterator<ReservationView> reservations = engine.getReservations().iterator();
        assertTrue(reservations.next().getIsApproved());
        assertFalse(reservations.next().getIsApproved());

    deleteDatabase();
    }

}

