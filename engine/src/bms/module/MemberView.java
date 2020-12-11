package bms.module;

import java.time.LocalDate;

public interface MemberView {
    int getSerialNumber();

    String getName();

    Integer getAge();

    String getNotes();

    Member.Level getLevel();

    LocalDate getJoinDate();

    LocalDate getExpireDate();

    boolean getHasPrivateBoat();

    Integer getBoatSerialNumber();

    String getPhoneNumber();

    String getEmail();

    String getPassword();

    boolean getManager();

    enum Level {
        BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), ADVANCED("Advanced");

        private String name;

        Level(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
        public static MemberView.Level fromName(String name) {
            for (MemberView.Level level: MemberView.Level.values()) {
                if (level.getName().equals(name)) {
                    return level;
                }
            }
            throw new IllegalArgumentException(name);
        }
    }
}
