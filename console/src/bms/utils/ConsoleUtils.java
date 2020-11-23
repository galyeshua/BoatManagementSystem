package bms.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtils {


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
}
