package bms.module;

import java.time.LocalDate;

public interface MemberView {
    int getSerialNumber();

    String getName();

    int getAge();

    String getNotes();

    Member.Level getLevel();

    LocalDate getJoinDate();

    LocalDate getExpireDate();

    boolean getHasPrivateBoat();

    int getBoatSerialNumber();

    String getPhoneNumber();

    String getEmail();

    String getPassword();

    boolean getManager();

    public enum Level { BEGINNER, MODERATE, PROFESSIONAL }
}
