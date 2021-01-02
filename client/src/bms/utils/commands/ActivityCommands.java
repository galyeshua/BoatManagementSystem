package bms.utils.commands;

import bms.exception.General;
import bms.module.Activity;
import bms.module.ActivityView;
import bms.module.BoatView;
import bms.utils.MenuUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static bms.utils.InputUtils.*;
import static bms.utils.printFormatUtils.printErrorsAfterLoading;
import static bms.xml.Convertor.*;

public class ActivityCommands {

    public static Command chooseAndEditActivity() {
        return new Command() {
            ActivityView activity;
            int id;

            private void chooseActivityToUpdate() throws Activity.NotFoundException {
                if(MenuUtils.engine.getActivities().isEmpty())
                    throw new General.ListIsEmptyException();

                printActivities().execute();
                System.out.println("choose activity to edit");
                ArrayList<ActivityView> activities = new ArrayList<ActivityView>(MenuUtils.engine.getActivities());

                int activityIndex = getNumberFromUser(0, activities.size() - 1);
                id = activities.get(activityIndex).getId();
                activity = MenuUtils.engine.getActivity(id);

                if (activity == null)
                    throw new Activity.NotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseActivityToUpdate();
                    new MenuUtils.openEditActivityMenu(id).execute();
                } catch (Activity.NotFoundException e){
                    System.out.println("Activity not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No activities");
                }
            }
        };
    }

    public static Command addActivity() {
        return new Command() {
            private String name;
            private LocalTime startTime;
            private LocalTime finishTime;
            private BoatView.BoatType boatType;


            private void askForValues(){
                boatType = null;

                System.out.println("Enter Name:");
                name = getStringFromUser();
                System.out.println("Enter startTime:");
                startTime = getLocalTimeFromUser();
                System.out.println("Enter finishTime:");
                finishTime = getLocalTimeFromUser();
                System.out.println("do you want to specify boat type?");
                if(getBoolFromUser())
                    boatType = (BoatView.BoatType) chooseFromOptions(BoatView.BoatType.values());
            }

            @Override
            public void execute() {
                try{
                    askForValues();
                    Activity activity = new Activity(name,startTime,finishTime);
                    activity.setBoatType(boatType);
                    MenuUtils.engine.addActivity(activity);
                    System.out.println("Activity created successfully");
                } catch (Activity.AlreadyExistsException | Activity.IllegalValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printActivities() {
        return new Command() {
            @Override
            public void execute() {
                if (MenuUtils.engine.getActivities().isEmpty())
                    System.out.println("No Activities");
                else {
                    int i = 0;
                    for (ActivityView activity : MenuUtils.engine.getActivities()) {
                        System.out.println("[" + i + "] " + activity.getStartTime() + " - " + activity.getFinishTime()
                                + ". Activity name: " + activity.getName() + ((activity.getBoatType() != null) ? " (Boat type: " + activity.getBoatType() + ")" : ""));
                        i++;
                    }
                }
            }
        };
    }

    public static Command deleteActivity() {
        return new Command() {
            int id;

            private void chooseActivity(){
                if(MenuUtils.engine.getActivities().isEmpty())
                    throw new General.ListIsEmptyException();

                printActivities().execute();
                System.out.println("choose activity to delete");
                List<ActivityView> activities = new ArrayList<ActivityView>(MenuUtils.engine.getActivities());
                int activityIndex = getNumberFromUser(0, activities.size() - 1);
                id = activities.get(activityIndex).getId();
            }

            @Override
            public void execute() {
                try{
                    chooseActivity();
                    MenuUtils.engine.deleteActivity(id);
                    System.out.println("Activity deleted successfully");
                } catch (Activity.NotFoundException e){
                    System.out.println("Activity not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No activities");
                }
            }
        };
    }

    public static Command editActivityName(int id) {
        return new Command() {
            String name;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(MenuUtils.engine.getActivity(id));
                    name = getStringFromUser();
                    newActivity.setName(name);
                    MenuUtils.engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityStartTime(int id) {
        return new Command() {
            LocalTime startTime;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(MenuUtils.engine.getActivity(id));
                    startTime = getLocalTimeFromUser();
                    newActivity.setStartTime(startTime);
                    MenuUtils.engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityFinishTime(int id) {
        return new Command() {
            LocalTime finishTime;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(MenuUtils.engine.getActivity(id));
                    finishTime = getLocalTimeFromUser();
                    newActivity.setFinishTime(finishTime);
                    MenuUtils.engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityBoatType(int id) {
        return new Command() {
            BoatView.BoatType boatType;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    boatType = null;
                    newActivity = new Activity(MenuUtils.engine.getActivity(id));
                    System.out.println("Do you want to specify boat type?");
                    if(getBoolFromUser())
                        boatType = (BoatView.BoatType) chooseFromOptions(BoatView.BoatType.values());
                    newActivity.setBoatType(boatType);
                    MenuUtils.engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command exportActivitiesToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    if(isFileExists(filePath))
                        throw new General.FileAlreadyExistException();
                    saveXmlFromString(MenuUtils.engine.getXmlStringActivities(), filePath);
                    System.out.println("successfully saved");
                } catch (General.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (JAXBException e) {
                    System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no activities to export (activity list is empty).");
                }
            }
        };
    }

    public static Command addActivitiesFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    MenuUtils.engine.loadActivitiesFromXmlString(stringFromXmlFilePath(filePath));
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

    public static Command replaceActivitiesFromFile() {
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
                        MenuUtils.engine.eraseAndLoadActivitiesFromXmlString(stringFromXmlFilePath(filePath));
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
