package bms.engine.list.manager;

import bms.engine.list.manager.Exceptions.MemberNotFoundException;
import bms.engine.list.manager.Exceptions.MemberAlreadyExistsException;

import bms.module.Boat;
import bms.module.Member;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;

public class MemberManager {
    @XmlElement(name="member", required = true)
    private List<Member> members = new ArrayList<Member>();
    //private Map<Integer, Member> members = new HashMap<Integer, Member>();

    public void addMember(Member member){
        String email = member.getEmail();
        int serialNumber = member.getSerialNumber();

        validateMemberSerialNumber(serialNumber);
        validateMemberEmail(email);

        //members.put(member.getSerialNumber(), member);
        members.add(member);
    }

    public void deleteMember(int serialNumber){
        Member member = getMember(serialNumber);
        if (member == null)
            throw new MemberNotFoundException();
        members.remove(serialNumber);
    }

    public Collection<Member> getMembers() {
        //return members.values();
        return members;
    }

    public Member getMember(int serialNumber) {
        //return members.get(serialNumber);
        for(Member member : getMembers())
            if(member.getSerialNumber()==serialNumber)
                return member;
        return null;
    }

    public Member getMember(String email) {
        for(Member member : getMembers())
            if(member.getEmail().equals(email))
                return member;
        return null;
    }

    public void updateMember(Member newMember) {
        int serialNumber = newMember.getSerialNumber();
        Member currentMember = getMember(serialNumber);

        if (currentMember == null)
            throw new Exceptions.MemberNotFoundException();

        if (!currentMember.getEmail().equals(newMember.getEmail()))
            validateMemberEmail(newMember.getEmail());

        //members.replace(serialNumber, newMember);
        members.set(members.indexOf(getMember(serialNumber)), newMember);
    }

    private void validateMemberSerialNumber(int serialNumber) {
        if (getMember(serialNumber) != null)
            throw new Exceptions.MemberAlreadyExistsException("Member with Serial Number '" + serialNumber + "' already exist");
    }

    private void validateMemberEmail(String email) {
        if (getMember(email) != null)
            throw new Exceptions.MemberAlreadyExistsException("Member with email '" + email + "' already exist");
    }

    public void eraseAll(){
        members.clear();
    }

}
