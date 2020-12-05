package bms.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtils {

    public static int getNumberFromUser(){
        Scanner scanner = new Scanner(System.in);
        int inputNumber = 0;
        boolean validInput = false;
        do {
            try{
                inputNumber = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException exception){
                System.out.println("This is not a number");
                validInput = false;
                scanner.nextLine();
            }
        } while(validInput == false);

        return inputNumber;
    }

    public static int getNumberFromUser(int min, int max) {
        int inputNumber = 0;
        boolean validInput = false;
        do {
            inputNumber = getNumberFromUser();
            validInput = true;

            if (inputNumber < min || inputNumber > max){
                System.out.println("Number out of range");
                validInput = false;
            }

        } while(validInput == false);

        return inputNumber;
    }

    public static int getNumberFromUser(int min) {
        int inputNumber = 0;
        boolean validInput = false;
        do {
            inputNumber = getNumberFromUser();
            validInput = true;

            if (inputNumber < min){
                System.out.println("Number out of range");
                validInput = false;
            }

        } while(validInput == false);

        return inputNumber;
    }


    public static String getStringFromUser() {
        Scanner scanner = new Scanner(System.in);
        String inputString = "";
        boolean validInput = false;
        do {
            try {
                inputString = scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException exception) {
                System.out.println("This is not a string");
                validInput = false;
                scanner.nextLine();
            }
        } while (validInput == false);

        return inputString;
    }


    public static boolean isTrue(String question){
        System.out.println(question);
        return getBoolFromUser();
    }

    public static Object chooseFromOptions(Object[] options) {
        System.out.println("Choose from the options:");
        int res = 0;
        int i = 0;
        for (Object obj :options) {
            System.out.println("[" + i++ + "] " + obj);
        }
        System.out.println("choose");
        res = getNumberFromUser(0, options.length - 1);
        return options[res];
    }

    private enum Answer {
        YES(true), NO(false);
        private boolean ans;

        Answer(boolean ans) {
            this.ans = ans;
        }
        private boolean getAns(){
            return this.ans;
        }
    }

    public static boolean getBoolFromUser() {
        Answer ans = (Answer) chooseFromOptions(Answer.values());
        return ans.getAns();
    }


    public static LocalTime getLocalTimeFromUser() {
        System.out.println("Enter hour:");
        int hour = getNumberFromUser(0, 23);
        System.out.println("Enter minute:");
        int minute = getNumberFromUser(0,59);
        return LocalTime.of(hour,minute);
    }

    public static LocalDate getLocalDateFromUser() {
        System.out.println("Enter day:");
        int dayOfMonth = getNumberFromUser(1, 31);
        System.out.println("Enter month:");
        int month = getNumberFromUser(1, 12);
        System.out.println("Enter year:");
        int year = getNumberFromUser(2020);
        return LocalDate.of(year,month,dayOfMonth);
    }

    public static LocalDateTime getLocalDateTimeFromUser(){
        LocalTime time = getLocalTimeFromUser();
        LocalDate date = getLocalDateFromUser();
        return LocalDateTime.of(date,time);

    }
}
