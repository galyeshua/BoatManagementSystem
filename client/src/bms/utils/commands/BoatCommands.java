package bms.utils.commands;

import bms.exception.General;
import bms.module.Boat;
import bms.module.BoatView;
import bms.utils.MenuUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

import java.io.IOException;

import static bms.utils.InputUtils.*;
import static bms.utils.InputUtils.getBoolFromUser;
import static bms.utils.printFormatUtils.printBoatForManager;
import static bms.utils.printFormatUtils.printErrorsAfterLoading;
import static bms.xml.Convertor.*;

public class BoatCommands {

    public static Command printBoats(){
        return new Command() {
            @Override
            public void execute() {
                if (MenuUtils.engine.getBoats().isEmpty())
                    System.out.println("No Boats");
                else {
                    int i = 0;
                    for (BoatView boat : MenuUtils.engine.getBoats()) {
                        System.out.print("[" + i + "] ");
                        printBoatForManager(boat);
                        i++;
                    }
                }
            }
        };
    }

    public static Command addBoat(){
        return new Command() {
            Boat boat;

            private void askForRequiredDataAndCreateBoat() throws Boat.IllegalValueException {
                System.out.println("Enter Serial Number:");
                int serialNumber = getNumberFromUser(1);
                System.out.println("Enter Boat name:");
                String boatName = getStringFromUser();
                System.out.println("Choose boat type:");
                Boat.BoatType boatType = (Boat.BoatType) chooseFromOptions(Boat.BoatType.values());

                boat = new Boat(serialNumber, boatName, boatType);
            }

            private void askForOptionalValues(){
                System.out.println("is private?");
                boat.setPrivate(getBoolFromUser());
                System.out.println("is Wide?");
                boat.setWide(getBoolFromUser());
                System.out.println("is Marine?");
                boat.setMarine(getBoolFromUser());
                System.out.println("is Disabled?");
                boat.setDisabled(getBoolFromUser());
            }

            @Override
            public void execute() {
                try{
                    askForRequiredDataAndCreateBoat();
                    askForOptionalValues();
                    MenuUtils.engine.addBoat(boat);
                    System.out.println("Boat created successfully");
                } catch (Boat.AlreadyExistsException | Boat.IllegalValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command deleteBoat() {
        return new Command() {
            int serialNumber;

            private void chooseBoat(){
                if(MenuUtils.engine.getBoats().isEmpty())
                    throw new General.ListIsEmptyException();

                printBoats().execute();
                System.out.println("choose boat to delete by serialNumber");
                serialNumber = getNumberFromUser();
            }

            @Override
            public void execute() {

                try{
                    chooseBoat();
                    MenuUtils.engine.deleteBoat(serialNumber);
                    System.out.println("Boat deleted successfully");
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("The boat is allocated for an approved reservation");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No Boats");
                }

            }
        };
    }

    public static Command chooseAndEditBoat() {
        return new Command() {
            int serialNumber;
            BoatView boat;

            private void chooseBoatToUpdate() throws Boat.NotFoundException, Boat.AlreadyAllocatedException {
                if(MenuUtils.engine.getBoats().isEmpty())
                    throw new General.ListIsEmptyException();

                System.out.println("All the boats:");
                printBoats().execute();
                System.out.println("choose boat to edit by entering a serial number ");
                serialNumber = getNumberFromUser();
                boat = MenuUtils.engine.getBoat(serialNumber);
                if (boat == null)
                    throw new Boat.NotFoundException();

                if(MenuUtils.engine.boatHaveFutureReservations(boat.getSerialNumber()))
                    throw new Boat.AlreadyAllocatedException();
            }

            @Override
            public void execute() {
                try{
                    chooseBoatToUpdate();
                    new MenuUtils.openEditBoatMenu(serialNumber).execute();
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No boats");
                } catch (Boat.AlreadyAllocatedException e) {
                    System.out.println("Cannot edit Boat with allocated reservation");
                }
            }
        };
    }

    public static Command editBoatName(int serialNumber) {
        return new Command() {
            String boatName;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    boatName = getStringFromUser();
                    newBoat.setName(boatName);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command editBoatNumOfPaddles(int serialNumber) {
        return new Command() {
            Boat.Paddles numOfPaddles;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    numOfPaddles = (BoatView.Paddles) chooseFromOptions(BoatView.Paddles.values());
                    newBoat.setNumOfPaddles(numOfPaddles);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command editBoatPrivate(int serialNumber) {
        return new Command() {
            boolean isPrivate;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    System.out.println("Does the boat private?");
                    isPrivate = getBoolFromUser();
                    newBoat.setPrivate(isPrivate);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command editBoatCoxswain(int serialNumber) {
        return new Command() {
            boolean hasCoxswain;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    hasCoxswain = getBoolFromUser();
                    newBoat.setHasCoxswain(hasCoxswain);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command editBoatMarine(int serialNumber) {
        return new Command() {
            boolean isMarine;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    isMarine = getBoolFromUser();
                    newBoat.setMarine(isMarine);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command editBoatDisabled(int serialNumber) {
        return new Command() {
            boolean isDisabled;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(MenuUtils.engine.getBoat(serialNumber));
                    isDisabled = getBoolFromUser();
                    newBoat.setDisabled(isDisabled);
                    MenuUtils.engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }

    public static Command viewAvailableBoats() {
        return new Command() {
            @Override
            public void execute() {
                System.out.println("All the boats that not Disabled:");
                int i=0;
                for(BoatView boat : MenuUtils.engine.getAllAvailableBoats()) {
                    System.out.print("[" + i + "] ");
                    printBoatForManager(boat);
                    i++;
                }
            }
        };
    }

    public static Command exportBoatsToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    if(isFileExists(filePath))
                        throw new General.FileAlreadyExistException();
                    saveXmlFromString(MenuUtils.engine.getXmlStringBoats(), filePath);
                    System.out.println("successfully saved");
                } catch (General.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (JAXBException e) {
                    System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no boats to export (boat list is empty).");
                }
            }
        };
    }

    public static Command addBoatsFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    MenuUtils.engine.loadBoatsFromXmlString(stringFromXmlFilePath(filePath));
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

    public static Command replaceBoatsFromFile() {
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
                        MenuUtils.engine.eraseAndLoadBoatsFromXmlString(stringFromXmlFilePath(filePath));
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
            }
        };
    }
}
