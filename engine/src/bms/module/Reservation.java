package bms.module;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private Member member;
    private LocalDateTime activityDate; //
    private String activityTime; //
    private String boatType;
    private List<Member> participants; //
    private LocalDateTime orderDate;
    private Member orderMember;
}
