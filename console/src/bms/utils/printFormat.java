package bms.utils;

import bms.module.BoatView;
import bms.module.MemberView;
import bms.module.ReservationView;
import bms.utils.menu.Commands;

public class printFormat {

    public static void printReservation(ReservationView reservation){
        String line;
        line = "Reservation for " + reservation.getActivityDate()
                + " (" + reservation.getActivity().getStartTime() + "-"
                + reservation.getActivity().getFinishTime() + ").";
        line += " Participants: ";
                    for (Integer memberID : reservation.getParticipants())
        line += "[" + Commands.engine.getMember(memberID).getName() + "]  ";


        line += ".requested boat type: ";
        for (BoatView.Rowers numOfRowers : reservation.getBoatType())
            line += "[" + numOfRowers + "]  ";


        if (reservation.getIsApproved())
        line += "- Approved" + " (Boat: " + Commands.engine.getBoat(reservation.getAllocatedBoatID()).getFormattedCode() +
                ", " + Commands.engine.getBoat(reservation.getAllocatedBoatID()).getName() + ")";
                    else
        line += "- Not approved";

                    System.out.println(line);
                    System.out.println("    " + "Order by " + Commands.engine.getMember(reservation.getOrderedMemberID()).getName()
                            + " on " + reservation.getOrderDate());
    }


    public static void printParticipantsShort(Integer memberID){
        MemberView member = Commands.engine.getMember(memberID);
        String line;
        line = "Name: " + member.getName();
        if (member.getAge() != null)
            line += ", Age:" + member.getAge();

        line += ", (Level: " + member.getLevel() + ")";
        System.out.println(line);
    }

    public static void printMemberForManager(MemberView member){
        String line;
        line = "Member Serial Number: " + member.getSerialNumber()
                + ", Name: " + member.getName()
                + ", email: " + member.getEmail()
                + ((member.getAge() != null) ? ", age: " + member.getAge() : "")
                + ", level: " + member.getLevel();
        if (member.getManager())
            line += " (Manager)";

        System.out.println(line);

        line = "    ";
        line += "joinDate: " + member.getJoinDate() + " (Expire: " + member.getExpireDate() +  ")";
        if(member.getHasPrivateBoat())
            line += ", Has private boat. Boat ID: " + member.getBoatSerialNumber();
        line += ((member.getPhoneNumber() != null) ? ", phoneNumber: " + member.getPhoneNumber() : "")
                + ((member.getNotes() != null) ? ", notes: " + member.getNotes() : "");
        System.out.println(line);
    }


    public static void printBoatForManager(BoatView boat){
        String line;
        line = "Boat Serial Number: " + boat.getSerialNumber()
                + ", Name: " + boat.getName()
                + ", Code: " + boat.getFormattedCode() + "  ";
        if(boat.getDisabled())
            line += "[Disabled] ";
        if(boat.getPrivate())
            line += "[Private] ";
        System.out.println(line);
    }


}
