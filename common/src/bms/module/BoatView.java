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
    String getFormattedCode();
    BoatType getType();
    int getAllowedNumOfRowers();

    enum Paddles {SINGLE, DOUBLE}

    enum Rowers {
        ONE("1"), TWO("2"), FOUR("4"), EIGHT("8");
        private String num;

        Rowers(String num) {
            this.num = num;
        }
        public String getNum() {
            return this.num;
        }
    }

    enum BoatType {
        SINGLE("Single", false, Rowers.ONE, Paddles.DOUBLE), // 1X
        PAIR("Pair", false, Rowers.TWO, Paddles.SINGLE), // 2-
        COXED_PAIR("Coxed_Pair", true, Rowers.TWO, Paddles.SINGLE), // 2+
        DOUBLE("Double", false, Rowers.TWO, Paddles.DOUBLE), // 2X
        COXED_DOUBLE("Coxed_Double", true ,Rowers.TWO, Paddles.DOUBLE), // 2X+
        FOUR("Four", false, Rowers.FOUR, Paddles.SINGLE), // 4-
        COXED_FOUR("Coxed_Four", true, Rowers.FOUR, Paddles.SINGLE), // 4+
        QUAD("Quad", false, Rowers.FOUR, Paddles.DOUBLE), // 4X
        COXED_QUAD("Coxed_Quad", true, Rowers.FOUR, Paddles.DOUBLE), // 4X+
        EIGHT("Eight", true, Rowers.EIGHT, Paddles.SINGLE), // 8+
        OCTUPLE("Octuple", true, Rowers.EIGHT, Paddles.DOUBLE); // 8X+

        private String name;
        private boolean HasCoxswain;
        private Rowers NumOfRowers;
        private Paddles NumOfPaddles;

        BoatType(String name, boolean HasCoxswain, Rowers NumOfRowers, Paddles NumOfPaddles) {
            this.name = name;
            this.HasCoxswain = HasCoxswain;
            this.NumOfRowers = NumOfRowers;
            this.NumOfPaddles = NumOfPaddles;
        }

        public String getName() {
            return name;
        }
        public boolean HasCoxswain() {
            return HasCoxswain;
        }
        public Rowers getNumOfRowers() {
            return NumOfRowers;
        }
        public Paddles getNumOfPaddles() {
            return NumOfPaddles;
        }
        public static BoatType fromName(String name) {
            for (BoatType boatType: BoatType.values()) {
                if (boatType.getName().equals(name)) {
                    return boatType;
                }
            }
            throw new IllegalArgumentException(name);
        }
    }
}
