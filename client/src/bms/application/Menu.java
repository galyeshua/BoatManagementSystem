package bms.application;

import bms.utils.commands.Command;

import java.util.ArrayList;
import java.util.List;

import static bms.utils.InputUtils.getNumberFromUser;

public class Menu {

    class Option {
        private String description;
        private Command func;

        public Option(String description, Command func) {
            this.description = description;
            this.func = func;
        }

        public String getDescription() {
            return description;
        }
        public Command getCommand(){
            return func;
        }
    }

    private String title;
    private List<Option> options;
    public boolean exitMenu;


    public Menu(String title) {
        this.title = title;
        this.options = new ArrayList<Option>(5);
        this.exitMenu = false;
    }

    public void startLoop(){
        int choice = 0;

        while(!exitMenu) {
            this.displayOptions();
            System.out.println("Choose number from the menu: ");
            choice = getNumberFromUser(0, options.size() - 1);
            this.execute(choice);
            System.out.println("");
        }
    }

    private void displayOptions(){
        System.out.println("    " + this.title);
        for (Option option : this.options)
            System.out.println("[" + options.indexOf(option) + "] " + option.getDescription());

    }

    private void execute(int optionIndex){
        Option option = options.get(optionIndex);
        Command commandOfOption = option.getCommand();
        commandOfOption.execute();
    }

    public Command stopLoop(){
        return new Command() {
            @Override
            public void execute() {
                exitMenu = true;
            }
        };
    }

    public Command ExitApp(){
        return new Command() {
            @Override
            public void execute() {
                System.out.println("BYE");
                System.exit(0);
            }
        };
    }

    public void addOption(String description, Command command){
        Option option = new Option(description, command);
        this.options.add(option);
    }


}