package bms.menu;

public class BoatCommands {


    public static class PrintBoats implements Command
    {
        @Override
        public void execute() {
            System.out.println("in print boats");
        }
    }


    public static class getBoat implements Command
    {
        @Override
        public void execute() {
            System.out.println("in print boats2");
        }
    }


}
