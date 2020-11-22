package bms.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtils {


    public static int getNumber(){
        Scanner scanner = new Scanner(System.in);
        int inputNumber = 0;
        boolean validInput = false;
        System.out.println("Enter a number");
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

}
