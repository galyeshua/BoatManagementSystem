package bms.engine.list.manager;

import bms.engine.list.manager.Exceptions.MemberNotFoundException;
import bms.engine.list.manager.Exceptions.MemberAlreadyExistsException;

import bms.module.Boat;
import bms.module.Member;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemberManager {
    private Map<Integer, Member> members = new HashMap<Integer, Member>();

    public void addMember(Member member){
        String email = member.getEmail();
        int serialNumber = member.getSerialNumber();

        if (getMember(serialNumber) != null)
            throw new MemberAlreadyExistsException("A member with SerialNumber '" + serialNumber + "' already exists");

        if (getMember(email) != null)
            throw new MemberAlreadyExistsException("A member with email '" + email + "' already exists");

        members.put(member.getSerialNumber(), member);
    }

    public void deleteMember(int serialNumber){
        Member member = getMember(serialNumber);
        if (member == null)
            throw new MemberNotFoundException();
        members.remove(serialNumber);
    }

    public Collection<Member> getMembers() {
        return members.values();
    }

    public Member getMember(int serialNumber) {
        return members.get(serialNumber);
    }

    public Member getMember(String email) {
        for(Member member : getMembers())
            if(member.getEmail().equals(email))
                return member;
        return null;
    }

    public void setMemberName(int serialNumber, String name) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setName(name);
    }

    public void setMemberAge(int serialNumber, int age) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setAge(age);
    }

    public void setMemberNotes(int serialNumber, String notes) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setNotes(notes);
    }

    public void setMemberLevel(int serialNumber, Member.Level level) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setLevel(level);
    }

    public void setMemberPrivateBoat(int serialNumber, boolean hasPrivateBoat) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setHasPrivateBoat(hasPrivateBoat);
    }

    public void setMemberBoatSerialNumber(int serialNumber,int boatSerialNumber){
        Member member = getMember(serialNumber);
        member.setBoatSerialNumber(boatSerialNumber);
    }


    public void setMemberPhone(int serialNumber, String phone) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setPhoneNumber(phone);
    }

    public void setMemberEmail(int serialNumber, String email) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setEmail(email);
    }

    public void setMemberPassword(int serialNumber, String password) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setPassword(password);
    }

    public void setMemberRole(int serialNumber, boolean role) {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Exceptions.MemberNotFoundException();
        member.setManager(role);
    }

}
