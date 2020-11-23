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

    public static class exitApp implements Command
    {
        @Override
        public void execute() {
            System.out.println("BYE");
            System.exit(0);
        }
    }

    public static class exitMenuLoop implements Command
    {
        private Menu menu;

        public exitMenuLoop(Menu menu) {
            this.menu = menu;
        }

        @Override
        public void execute() {
            menu.stopLoop();
        }
    }

    public static class openSubMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Sub Menu");

            subMenu.addOption("Back", new BoatCommands.exitMenuLoop(subMenu));
            subMenu.addOption("Option 2", new BoatCommands.PrintBoats());
            subMenu.addOption("Option 3", new BoatCommands.PrintBoats());

            subMenu.startLoop();
        }
    }



}
