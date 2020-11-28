package bms.utils;

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

    public static boolean getBoolFromUser() {
        Scanner scanner = new Scanner(System.in);
        String inputString = "";
        boolean inputBool = true;
        boolean validInput = false;
        do {
            inputString = getStringFromUser();
            validInput = true;
            if(inputString.equals("Y")){
                inputBool = true;
            } else if (inputString.equals("N")){
                inputBool = false;
            } else {
                validInput = false;
            }
        } while (validInput == false);

        return inputBool;
    }

    public static boolean isTrue(String question){
        System.out.println(question);
        return getBoolFromUser();
    }

    public static Object chooseFromOptins(Object[] options) {
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



}
