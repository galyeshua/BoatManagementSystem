package bms.engine.list.manager;

import bms.module.Member;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;

public class MemberManager {
    @XmlElement(name="member", required = true)
    private List<Member> members = new ArrayList<Member>();

    public void addMember(Member member) throws Member.AlreadyExistsException {
        String email = member.getEmail();
        int serialNumber = member.getSerialNumber();

        validateMemberSerialNumber(serialNumber);
        validateMemberEmail(email);

        members.add(member);
    }

    public void deleteMember(int serialNumber) throws Member.NotFoundException {
        Member member = getMember(serialNumber);
        if (member == null)
            throw new Member.NotFoundException();
        members.remove(member);
    }

    public Collection<Member> getMembers() {
        return members;
    }

    public Member getMember(int serialNumber) {
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

    public void updateMember(Member newMember) throws Member.NotFoundException, Member.AlreadyExistsException {
        int serialNumber = newMember.getSerialNumber();
        Member currentMember = getMember(serialNumber);

        if (currentMember == null)
            throw new Member.NotFoundException();

        if (!currentMember.getEmail().equals(newMember.getEmail()))
            validateMemberEmail(newMember.getEmail());

        members.set(members.indexOf(currentMember), newMember);
    }

    private void validateMemberSerialNumber(int serialNumber) throws Member.AlreadyExistsException {
        if (getMember(serialNumber) != null)
            throw new Member.AlreadyExistsException("Member with Serial Number '" + serialNumber + "' already exist");
    }

    private void validateMemberEmail(String email) throws Member.AlreadyExistsException {
        if (getMember(email) != null)
            throw new Member.AlreadyExistsException("Member with email '" + email + "' already exist");
    }

    public void eraseAll(){
        members.clear();
    }

}
