package bms.module;

import java.time.LocalTime;

public interface ActivityView {
    int getId();

    String getName();

    LocalTime getStartTime();

    LocalTime getFinishTime();

    BoatView.BoatType getBoatType();

    boolean isOverlapping(ActivityView other);

}
