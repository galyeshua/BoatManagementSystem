package bms.menu;

import java.util.ArrayList;
import java.util.List;

import static bms.utils.ConsoleUtils.getNumberFromUser;

public class Menu {
    private String title;
    private List<Option> options;
    boolean exitMenu;


    public Menu(String title) {
        this.title = title;
        this.options = new ArrayList<Option>(5);
        this.exitMenu = false;
    }


    public void startLoop(){
        int choice = 0;

        while(exitMenu == false) {
            this.displayOptions();
            System.out.println("Choose number from the menu: ");
            choice = getNumberFromUser(0, options.size());
            this.execute(choice);
            System.out.println("");
        }
    }

    public void stopLoop(){
        exitMenu = true;
    }


    private void displayOptions(){
        System.out.println("    " + this.title);
        for (Option option : this.options)
            System.out.println("[" + options.indexOf(option) + "] " + option.getDescription());
    }

    private void execute(int optionIndex){
        Command commandOfOption = options.get(optionIndex).getCommand();
        commandOfOption.execute();
    }

    public void addOption(String description, Command command){
        this.options.add(new Option(description, command));
    }

}
