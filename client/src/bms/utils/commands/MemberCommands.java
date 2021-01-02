package bms.utils.commands;

import bms.exception.General;
import bms.module.Boat;
import bms.module.Member;
import bms.module.MemberView;
import bms.utils.MenuUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import java.io.IOException;
import java.util.Collection;

import static bms.utils.InputUtils.*;
import static bms.utils.printFormatUtils.printMemberForManager;
import static bms.utils.printFormatUtils.printErrorsAfterLoading;
import static bms.xml.Convertor.saveXmlFromString;
import static bms.xml.Convertor.stringFromXmlFilePath;
import static bms.xml.Convertor.isFileExists;

public class MemberCommands {

    public static Command chooseAndEditMember() {
        return new Command() {
            int serialNumber;
            MemberView member;

            private void chooseMemberToUpdate() throws Member.NotFoundException {
                if(MenuUtils.engine.getMembers().isEmpty())
                    throw new General.ListIsEmptyException();

                System.out.println("All the members:");
                printMembers().execute();
                System.out.println("Choose member to edit by entering a Serial Number");
                serialNumber = getNumberFromUser();
                member = MenuUtils.engine.getMember(serialNumber);
                if (member == null)
                    throw new Member.NotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseMemberToUpdate();
                    new MenuUtils.openEditMemberMenu(serialNumber).execute();
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No members");
                }
            }
        };
    }

    public static Command addMember() {
        return new Command() {
            Member member;

            private void askForRequiredDataAndCreateMember() throws Member.IllegalValueException {
                System.out.println("Enter Serial Number:");
                int serialNumber = getNumberFromUser(1);
                System.out.println("Enter name:");
                String name = getStringFromUser();
                System.out.println("Enter email:");
                String email = getStringFromUser();
                System.out.println("Enter password:");
                String password = getStringFromUser();

                member = new Member(serialNumber, name, email, password);
            }

            private void askForOptionalValues() throws Member.IllegalValueException {
                System.out.println("Enter Age:");
                member.setAge(getNumberFromUser(1));
                System.out.println("Enter Level:");
                member.setLevel((Member.Level) chooseFromOptions(Member.Level.values()));
                System.out.println("is manager?");
                member.setManager(getBoolFromUser());
                System.out.println("Enter Notes:");
                member.setNotes(getStringFromUser());
                System.out.println("Enter phone number:");
                member.setPhoneNumber(getStringFromUser());

                member.setBoatSerialNumber(null);
                System.out.println("Does member have private boat?");
                if (getBoolFromUser()){
                    System.out.println("Enter Boat Serial Number:");
                    int boatSerialNumber = getNumberFromUser(1);
                    member.setBoatSerialNumber(boatSerialNumber);
                }
            }

            @Override
            public void execute() {
                try{
                    askForRequiredDataAndCreateMember();
                    askForOptionalValues();
                    MenuUtils.engine.addMember(member);
                    System.out.println("Member created successfully");
                } catch (Member.AlreadyExistsException | Member.IllegalValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printMembers() {
        return new Command() {
            @Override
            public void execute() {
                Collection<MemberView> members = MenuUtils.engine.getMembers();

                if (members.isEmpty())
                    System.out.println("No Members");
                else {
                    int i = 0;
                    for (MemberView member : members) {
                        System.out.print("[" + i + "] ");
                        printMemberForManager(member);
                        i++;
                    }
                }
            }
        };
    }

    public static Command deleteMember() {
        return new Command() {
            int serialNumber;

            private void chooseMember(){
                if(MenuUtils.engine.getMembers().isEmpty())
                    throw new General.ListIsEmptyException();

                printMembers().execute();
                System.out.println("choose member to delete (by Serial number)");
                serialNumber = getNumberFromUser();
            }

            @Override
            public void execute() {
                try{
                    chooseMember();
                    MenuUtils.engine.deleteMember(serialNumber);
                    System.out.println("Member deleted successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No members");
                } catch (Member.AccessDeniedException e){
                    System.out.println("You dont have permissions to do that");
                } catch (Member.AlreadyHaveApprovedReservationsException e){
                    System.out.println("Cannot delete member with approved reservation");
                }

            }
        };
    }

    public static Command editMemberName(int serialNumber) {
        return new Command() {
            String name;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    name = getStringFromUser();
                    newMember.setName(name);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberAge(int serialNumber) {
        return new Command() {
            int age;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    age = getNumberFromUser(16, 99);
                    newMember.setAge(age);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberNotes(int serialNumber) {
        return new Command() {
            String notes;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    notes = getStringFromUser();
                    newMember.setNotes(notes);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberLevel(int serialNumber) {
        return new Command() {
            Member.Level level;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    level = (Member.Level) chooseFromOptions(Member.Level.values());
                    newMember.setLevel(level);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberPrivateBoat(int serialNumber) {
        return new Command() {
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    System.out.println("Does member have private boat?");

                    if (getBoolFromUser()){
                        System.out.println("Enter Boat Serial Number:");
                        int boatSerialNumber = getNumberFromUser(1);
                        newMember.setBoatSerialNumber(boatSerialNumber);
                    } else
                        newMember.setBoatSerialNumber(null);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberPhone(int serialNumber) {
        return new Command() {
            String phone;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    phone = getStringFromUser();
                    newMember.setPhoneNumber(phone);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberEmail(int serialNumber) {
        return new Command() {
            String email;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    email = getStringFromUser();
                    newMember.setEmail(email);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberPassword(int serialNumber) {
        return new Command() {
            String password;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    password = getStringFromUser();
                    newMember.setPassword(password);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command editMemberRole(int serialNumber) {
        return new Command() {
            boolean role;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(MenuUtils.engine.getMember(serialNumber));
                    role = getBoolFromUser();
                    newMember.setManager(role);
                    MenuUtils.engine.updateMember(newMember);
                    System.out.println("Updated successfully");
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit private boat while the member has approved reservation");
                }
            }
        };
    }

    public static Command exportMembersToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    if(isFileExists(filePath))
                        throw new General.FileAlreadyExistException();
                    saveXmlFromString(MenuUtils.engine.getXmlStringMembers(), filePath);
                    System.out.println("successfully saved");
                } catch (General.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (DatatypeConfigurationException e){
                    System.out.println(e.getMessage());
                } catch (JAXBException e) {
                    System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no members to export (member list is empty).");
                }

            }
        };
    }

    public static Command addMembersFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    MenuUtils.engine.loadMembersFromXmlString(stringFromXmlFilePath(filePath));
                    System.out.println("Data loaded successfully");
                    printErrorsAfterLoading();
                } catch (General.IllegalFileTypeException e){
                    System.out.println("File must be in xml format");
                } catch (General.FileNotFoundException e){
                    System.out.println("Cant find file " + filePath);
                } catch (JAXBException e) {
                    System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Command replaceMembersFromFile() {
        return new Command() {
            String filePath;
            boolean areYouSure;
            @Override
            public void execute() {
                System.out.println("Warning: this operation will erase all reservations. Do you want to continue?");
                areYouSure = getBoolFromUser();

                if (areYouSure){
                    System.out.println("Enter file path");
                    filePath = getStringFromUser();
                    try{
                        MenuUtils.engine.eraseAndLoadMembersFromXmlString(stringFromXmlFilePath(filePath));
                        System.out.println("Data loaded successfully");
                        printErrorsAfterLoading();
                    } catch (General.IllegalFileTypeException e){
                        System.out.println("File must be in xml format");
                    } catch (General.FileNotFoundException e){
                        System.out.println("Cant find file " + filePath);
                    } catch (JAXBException e) {
                        System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (Member.IllegalValueException e) {
                        System.out.println("Problem with current user: " + e.getMessage());
                    }
                }
            }
        };
    }
}
