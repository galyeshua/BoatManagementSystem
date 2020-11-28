package bms.engine.list.manager;

public class Exceptions {
    public static class BoatNotFoundException extends RuntimeException { }
    public static class BoatAlreadyExistsException extends RuntimeException {
        public BoatAlreadyExistsException(String message) {
            super(message);
        }
    }
    public static class IllegalBoatValueException extends RuntimeException{
        public IllegalBoatValueException(String message) {
            super(message);
        }
    }

    public static class MemberNotFoundException extends RuntimeException { }
    public static class MemberAlreadyExistsException extends RuntimeException {
        public MemberAlreadyExistsException(String message) {
            super(message);
        }
    }
    public static class IllegalMemberValueException extends RuntimeException{
        public IllegalMemberValueException(String message) {
            super(message);
        }
    }

}