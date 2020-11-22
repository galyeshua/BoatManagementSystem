package bms.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String title;
    private List<Option> options;

    public Menu() {
        this.options = new ArrayList<Option>(5);


        this.options.add(new Option("Print Boats", new BoatCommands.PrintBoats()));
        this.options.add(new Option("sfsdfs", new BoatCommands.getBoat()));
    }

    public void show(){
        for (Option option : this.options)
            System.out.println("[" + options.indexOf(option) + "] " + option.getDescription());
    }

    public void execute(int optionIndex){
        Command command = options.get(optionIndex).getCommand();
        command.execute();
    }


}
