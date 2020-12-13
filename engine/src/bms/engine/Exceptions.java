package bms.engine;

public class Exceptions {

    public static class ListCannotBeEmptyException extends Exception { }
    public static class IllegalFileTypeException extends RuntimeException { }
    public static class FileNotFoundException extends RuntimeException { }
    public static class FileAlreadyExistException extends RuntimeException { }

    public static class EmptyReservationListException extends RuntimeException { }
}