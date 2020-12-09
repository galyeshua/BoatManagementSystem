package bms.module;

import java.time.LocalTime;

public interface ActivityView {
    int getId();

    String getName();

    LocalTime getStartTime();

    LocalTime getFinishTime();

    String getBoatType();

    boolean isOverlapping(ActivityView other);
}
