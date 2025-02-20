package bms.tests;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.engine.XmlHandler;
import bms.module.*;
import org.junit.Test;

import javax.xml.bind.JAXBException;
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
        Engine engine = new Engine();
        Collection<MemberView> members = engine.getMembers();

        assertEquals(0, members.size());

        deleteDatabase();
    }

    @Test
    public void getListOfMembersAfterAddMemberSizeShouldBeOne() throws Member.IllegalValueException, Member.AlreadyExistsException {
        Engine engine = new Engine();

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);
        Collection<MemberView> members = engine.getMembers();

        assertEquals(1, members.size());

        deleteDatabase();
    }

    @Test(expected = Member.AlreadyExistsException.class)
    public void addTwoMemberWithSameDetailsShouldThrowException() throws Member.IllegalValueException, Member.AlreadyExistsException {
        Engine engine = new Engine();
        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);
        engine.addMember(m);

        deleteDatabase();
    }

    @Test
    public void getMemberThatNotExistsShouldReturnNull() {
        Engine engine = new Engine();

        assertNull(engine.getMember(1));

        deleteDatabase();
    }

    @Test
    public void addAndGetMemberShouldReturnMemberViewObject() throws Member.AlreadyExistsException, Member.IllegalValueException {
        Engine engine = new Engine();

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);

        MemberView returnedMember = engine.getMember(1);
        assertEquals(returnedMember, engine.getMember("gal@gmail.com"));
        assertEquals("gal", returnedMember.getName());
        assertEquals("gal@gmail.com", returnedMember.getEmail());
        assertTrue(returnedMember.checkPassword("1234"));

        deleteDatabase();
    }

    @Test
    public void updateMemberNameShouldChangedTheName() throws Member.AlreadyExistsException, Member.IllegalValueException, Member.NotFoundException, Boat.AlreadyAllocatedException {
        Engine engine = new Engine();

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
        Engine engine = new Engine();

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
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);
        engine.setCurrentUser(managerUser.getSerialNumber());

        Member m = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(m);

        assertEquals("gal", engine.getMember(1).getName());
        engine.deleteMember(1);
        assertNull(engine.getMember(1));

        deleteDatabase();
    }


    @Test(expected = Member.AccessDeniedException.class)
    public void deleteLoggedInUserShouldReturnAccessDenied() throws Member.AccessDeniedException, Member.NotFoundException, Member.AlreadyHaveApprovedReservationsException, Member.AlreadyExistsException, Member.IllegalValueException {
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);
        engine.setCurrentUser(managerUser.getSerialNumber());

        engine.deleteMember(100);

        deleteDatabase();
    }

    @Test
    public void getMembersByNameWithUsersShouldReturnListOfTwo() throws Member.IllegalValueException, Member.AlreadyExistsException {
        Engine engine = new Engine();

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
        Engine engine = new Engine();

        Collection<MemberView> members = engine.getMembers("gal");
        assertTrue(members.isEmpty());

        deleteDatabase();
    }


    @Test
    public void addReservationForUserWithPrivateBoatShouldApprovedAutomatically() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException {
        Engine engine = new Engine();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Boat galsBoat = new Boat(5, "5", BoatView.BoatType.SINGLE);
        galsBoat.setPrivate(true);
        engine.addBoat(galsBoat);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setBoatSerialNumber(5);
        engine.addMember(gal);

        engine.setCurrentUser(gal.getSerialNumber());

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
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Boat galsBoat = new Boat(5, "5", BoatView.BoatType.SINGLE);
        galsBoat.setPrivate(true);
        engine.addBoat(galsBoat);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setBoatSerialNumber(5);
        engine.addMember(gal);

        engine.setCurrentUser(gal.getSerialNumber());

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
    public void addReservationForManagerAndUpdateReservationShouldBeFine() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException, Member.AccessDeniedException, Reservation.ListCannotBeEmptyException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);

        engine.setCurrentUser(gal.getSerialNumber());

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(100);
        engine.addReservation(galsReservation);

        Reservation updatedReservation = new Reservation(galsReservation);
        updatedReservation.addParticipant(2);

        engine.setCurrentUser(managerUser.getSerialNumber());
        engine.updateReservation(updatedReservation);

        List<Integer> participents = new ArrayList<>();
        participents.add(1);
        participents.add(100);
        participents.add(2);
        assertEquals(participents, engine.getReservation(updatedReservation.getId()).getParticipants());

        deleteDatabase();
    }

    @Test
    public void addReservationForUserAndUpdateReservationShouldBeFine() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException, Member.AccessDeniedException, Reservation.ListCannotBeEmptyException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);

        engine.setCurrentUser(gal.getSerialNumber());

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        Reservation updatedReservation = new Reservation(galsReservation);
        updatedReservation.addParticipant(100);

        engine.setCurrentUser(maya.getSerialNumber());
        engine.updateReservation(updatedReservation);

        List<Integer> participents = new ArrayList<>();
        participents.add(1);
        participents.add(2);
        participents.add(100);
        assertEquals(participents, engine.getReservation(updatedReservation.getId()).getParticipants());

        deleteDatabase();
    }

    @Test
    public void managerUpdateReservationOfOthersShouldBeFine() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException, Member.AccessDeniedException, Reservation.ListCannotBeEmptyException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);

        Member liron = new Member(3, "liron", "liron@gmail.com", "1234");
        engine.addMember(liron);

        engine.setCurrentUser(gal.getSerialNumber());

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        Reservation updatedReservation = new Reservation(galsReservation);
        updatedReservation.addParticipant(3);

        engine.setCurrentUser(managerUser.getSerialNumber());
        engine.updateReservation(updatedReservation);

        List<Integer> participents = new ArrayList<>();
        participents.add(1);
        participents.add(2);
        participents.add(3);
        assertEquals(participents, engine.getReservation(updatedReservation.getId()).getParticipants());

        deleteDatabase();
    }

    @Test
    public void ownerUpdateReservationShouldBeFine() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException, Member.AccessDeniedException, Reservation.ListCannotBeEmptyException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);

        Member liron = new Member(3, "liron", "liron@gmail.com", "1234");
        engine.addMember(liron);

        engine.setCurrentUser(gal.getSerialNumber());
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(2);
        galsReservation.addParticipant(3);
        engine.addReservation(galsReservation);

        Reservation updatedReservation = new Reservation(galsReservation);
        updatedReservation.addParticipant(100);

        engine.setCurrentUser(gal.getSerialNumber());
        engine.updateReservation(updatedReservation);

        List<Integer> participents = new ArrayList<>();
        participents.add(2);
        participents.add(3);
        participents.add(100);
        assertEquals(participents, engine.getReservation(updatedReservation.getId()).getParticipants());

        deleteDatabase();
    }

    @Test(expected = Member.AccessDeniedException.class)
    public void userUpdateReservationOfOthersShouldThrowException() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Activity.IllegalValueException, Member.AccessDeniedException, Reservation.ListCannotBeEmptyException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        engine.addMember(maya);

        Member liron = new Member(3, "liron", "liron@gmail.com", "1234");
        engine.addMember(liron);

        engine.setCurrentUser(gal.getSerialNumber());

        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.FOUR);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        Reservation updatedReservation = new Reservation(galsReservation);
        updatedReservation.addParticipant(100);

        engine.setCurrentUser(liron.getSerialNumber());
        engine.updateReservation(updatedReservation);

        deleteDatabase();
    }

    @Test
    public void addTwoReservationsThatOneWithPrivateBoatShouldApprovedOneAutomatically() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.AlreadyExistsException, Boat.IllegalValueException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException {
        Engine engine = new Engine();
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

        engine.setCurrentUser(gal.getSerialNumber());
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
        bennysReservation.addBoatType(BoatView.Rowers.EIGHT);
        engine.addReservation(bennysReservation);


        Iterator<ReservationView> reservations = engine.getReservations().iterator();
        assertTrue(reservations.next().getIsApproved());
        assertFalse(reservations.next().getIsApproved());

    deleteDatabase();
    }


    @Test
    public void addReservationWithAllBegginerMembersShouldSuggestWideBoat() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.IllegalValueException, Boat.AlreadyExistsException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Boat publicBoat1 = new Boat(24, "wide boat", BoatView.BoatType.COXED_DOUBLE);
        publicBoat1.setWide(true);
        engine.addBoat(publicBoat1);
        Boat publicBoat2 = new Boat(13, "13", BoatView.BoatType.PAIR);
        engine.addBoat(publicBoat2);
        Boat publicBoat3 = new Boat(100, "100", BoatView.BoatType.PAIR);
        engine.addBoat(publicBoat3);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setLevel(MemberView.Level.BEGINNER);
        engine.addMember(gal);
        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        maya.setLevel(MemberView.Level.BEGINNER);
        engine.addMember(maya);
        Member benny = new Member(3, "benny", "benny@gmail.com", "1234");
        benny.setLevel(MemberView.Level.BEGINNER);
        engine.addMember(benny);

        engine.setCurrentUser(gal.getSerialNumber());
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addBoatType(BoatView.Rowers.TWO);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        galsReservation.addParticipant(3);
        engine.addReservation(galsReservation);

        List<BoatView> suggestedBoats = new ArrayList<BoatView>(engine.findSuitableBoatByLevelOfParticipants(galsReservation));
        assertEquals(1, suggestedBoats.size());
        assertEquals(publicBoat1, suggestedBoats.get(0));

        deleteDatabase();
    }

    @Test
    public void addReservationWithAllAdvancedMembersShouldSuggestNothing() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.IllegalValueException, Boat.AlreadyExistsException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Boat publicBoat1 = new Boat(24, "wide boat", BoatView.BoatType.COXED_DOUBLE);
        publicBoat1.setWide(true);
        engine.addBoat(publicBoat1);
        Boat publicBoat2 = new Boat(13, "13", BoatView.BoatType.PAIR);
        engine.addBoat(publicBoat2);
        Boat publicBoat3 = new Boat(100, "100", BoatView.BoatType.PAIR);
        engine.addBoat(publicBoat3);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(gal);
        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        maya.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(maya);
        Member benny = new Member(3, "benny", "benny@gmail.com", "1234");
        benny.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(benny);

        engine.setCurrentUser(gal.getSerialNumber());
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addBoatType(BoatView.Rowers.TWO);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        galsReservation.addParticipant(3);
        engine.addReservation(galsReservation);

        List<BoatView> suggestedBoats = new ArrayList<BoatView>(engine.findSuitableBoatByLevelOfParticipants(galsReservation));
        assertEquals(0, suggestedBoats.size());

        deleteDatabase();
    }


    @Test
    public void addTwoReservationsOnSameTimeAndSameRequirementsShouldSuggestOnlyOnFirst() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.IllegalValueException, Boat.AlreadyExistsException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Boat.AlreadyAllocatedException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setLevel(MemberView.Level.ADVANCED);
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        maya.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(maya);

        Member benny = new Member(3, "benny", "benny@gmail.com", "1234");
        benny.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(benny);

        Boat publicBoat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        engine.addBoat(publicBoat1);

        engine.setCurrentUser(gal.getSerialNumber());
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addBoatType(BoatView.Rowers.TWO);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        engine.setCurrentUser(benny.getSerialNumber());
        Reservation bennysReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                24);
        bennysReservation.addBoatType(BoatView.Rowers.FOUR);
        bennysReservation.addBoatType(BoatView.Rowers.TWO);
        bennysReservation.addParticipant(100);
        bennysReservation.addParticipant(24);
        engine.addReservation(bennysReservation);

        List<BoatView> suggestedBoats = new ArrayList<BoatView>(engine.findSuitableBoatByLevelOfParticipants(galsReservation));
        assertEquals(1, suggestedBoats.size());
        assertEquals(publicBoat1, suggestedBoats.get(0));

        engine.approveReservation(galsReservation.getId(), publicBoat1.getSerialNumber());

        suggestedBoats = new ArrayList<BoatView>(engine.findSuitableBoatByLevelOfParticipants(bennysReservation));
        assertEquals(0, suggestedBoats.size());

        deleteDatabase();
    }

    @Test(expected = Boat.AlreadyAllocatedException.class)
    public void addTwoReservationsOnSameTimeAndSameRequirementsShouldThrowException() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.IllegalValueException, Boat.AlreadyExistsException, Activity.IllegalValueException, Reservation.AlreadyApprovedException, Reservation.IllegalValueException, Reservation.NotFoundException, Reservation.AlreadyExistsException, Boat.AlreadyAllocatedException {
        Engine engine = new Engine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setLevel(MemberView.Level.ADVANCED);
        managerUser.setManager(true);
        engine.addMember(managerUser);

        Member gal = new Member(1, "gal", "gal@gmail.com", "1234");
        gal.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(gal);

        Member maya = new Member(2, "maya", "maya@gmail.com", "1234");
        maya.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(maya);

        Member benny = new Member(3, "benny", "benny@gmail.com", "1234");
        benny.setLevel(MemberView.Level.ADVANCED);
        engine.addMember(benny);

        Boat publicBoat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        engine.addBoat(publicBoat1);

        engine.setCurrentUser(gal.getSerialNumber());
        Reservation galsReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                1);
        galsReservation.addBoatType(BoatView.Rowers.ONE);
        galsReservation.addBoatType(BoatView.Rowers.TWO);
        galsReservation.addParticipant(1);
        galsReservation.addParticipant(2);
        engine.addReservation(galsReservation);

        engine.setCurrentUser(benny.getSerialNumber());
        Reservation bennysReservation = new Reservation(new Activity(LocalTime.parse("10:00", timeFormatter), LocalTime.parse("11:00", timeFormatter)),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                24);
        bennysReservation.addBoatType(BoatView.Rowers.FOUR);
        bennysReservation.addBoatType(BoatView.Rowers.TWO);
        bennysReservation.addParticipant(100);
        bennysReservation.addParticipant(24);
        engine.addReservation(bennysReservation);

        engine.approveReservation(galsReservation.getId(), publicBoat1.getSerialNumber());
        engine.approveReservation(bennysReservation.getId(), publicBoat1.getSerialNumber());

        deleteDatabase();
    }

    @Test
    public void checkIfPrivateBoatAllocatedToMemberShouldReturnTrue() throws Boat.AlreadyExistsException, Boat.IllegalValueException, Member.AlreadyExistsException, Member.IllegalValueException {
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);

        Boat boat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        boat1.setPrivate(true);
        engine.addBoat(boat1);

        managerUser.setBoatSerialNumber(boat1.getSerialNumber());
        engine.addMember(managerUser);

        assertTrue(engine.isPrivateBoatAllocatedToMember(boat1.getSerialNumber()));
        deleteDatabase();
    }

    @Test
    public void checkIfNotPrivateBoatAllocatedToMemberShouldReturnFalse() throws Boat.AlreadyExistsException, Boat.IllegalValueException, Member.AlreadyExistsException, Member.IllegalValueException {
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);

        Boat boat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        engine.addBoat(boat1);

        engine.addMember(managerUser);

        assertFalse(engine.isPrivateBoatAllocatedToMember(boat1.getSerialNumber()));
        deleteDatabase();
    }

    @Test
    public void checkIfPrivateBoatAllocatedToMemberShouldReturnFalse() throws Boat.AlreadyExistsException, Boat.IllegalValueException, Member.AlreadyExistsException, Member.IllegalValueException {
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);

        Boat boat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        boat1.setPrivate(true);
        engine.addBoat(boat1);

        engine.addMember(managerUser);

        assertFalse(engine.isPrivateBoatAllocatedToMember(boat1.getSerialNumber()));
        deleteDatabase();
    }


    @Test(expected = Boat.BelongsToMember.class)
    public void deletePrivateBoatThatAllocatedToMemberShouldThrowException() throws Member.IllegalValueException, Member.AlreadyExistsException, Boat.IllegalValueException, Boat.AlreadyExistsException, Boat.AlreadyAllocatedException, Boat.NotFoundException, Boat.BelongsToMember {
        Engine engine = new Engine();

        Member managerUser = new Member(100, "managerUser", "managerUser@gmail.com", "1234");
        managerUser.setManager(true);

        Boat boat1 = new Boat(24, "wide boat", BoatView.BoatType.DOUBLE);
        boat1.setPrivate(true);
        engine.addBoat(boat1);

        managerUser.setBoatSerialNumber(boat1.getSerialNumber());
        engine.addMember(managerUser);

        engine.deleteBoat(boat1.getSerialNumber());

        deleteDatabase();
    }

}

