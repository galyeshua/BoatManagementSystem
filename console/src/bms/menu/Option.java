package bms.menu;

import java.util.concurrent.Callable;

public class Option {
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
