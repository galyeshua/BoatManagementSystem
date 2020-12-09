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
    public static class MemberAccessDeniedException extends RuntimeException{

    }




    public static class ActivityNotFoundException extends RuntimeException { }
    public static class ActivityAlreadyExistsException extends RuntimeException {
        public ActivityAlreadyExistsException(String message) {
            super(message);
        }
    }
    public static class IllegalActivityValueException extends RuntimeException{
        public IllegalActivityValueException(String message) {
            super(message);
        }
    }



    public static class ReservationNotFoundException extends RuntimeException { }
    public static class ReservationAlreadyExistsException extends RuntimeException {
        public ReservationAlreadyExistsException(String message) {
            super(message);
        }
    }
    public static class EmptyReservationListException extends RuntimeException { }
    public static class IllegalReservationValueException extends RuntimeException{
        public IllegalReservationValueException(String message) {
            super(message);
        }
    }
    public static class ReservationAlreadyApprovedException extends RuntimeException {
        public ReservationAlreadyApprovedException(String message) {
            super(message);
        }
    }

    public static class MemberAlreadyInApprovedReservationsException extends RuntimeException {
        int memberID;
        public MemberAlreadyInApprovedReservationsException(int memberID) {
            super();
            this.memberID=memberID;
        }

        public int getMemberID() {
            return memberID;
        }
    }

}
