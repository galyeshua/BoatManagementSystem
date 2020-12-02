package bms.module;

public interface BoatView {

    int getSerialNumber();
    String getName();
    Rowers getNumOfRowers();
    Paddles getNumOfPaddles();
    boolean getPrivate();
    boolean getWide();
    boolean getHasCoxswain();
    boolean getMarine();
    boolean getDisabled();

    public enum Rowers {
        ONE("1"), TWO("2"), FOUR("4"), EIGHT("8");
        private String num;

        Rowers(String num) {
            this.num = num;
        }

        private String getNum() {
            return this.num;
        }
    }

    public enum Paddles {SINGLE, DOUBLE}
}
