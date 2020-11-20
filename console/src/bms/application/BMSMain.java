package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import java.util.Scanner;

public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start(){
        BMSEngine engine = new Engine();

        System.out.println("Enter your name");
        String line = new Scanner(System.in).nextLine();

        String result = engine.foo(line);

        System.out.println(result);

    }

}
