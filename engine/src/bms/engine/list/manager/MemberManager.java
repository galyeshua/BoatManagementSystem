package bms.engine.list.manager;


import bms.module.Member;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemberManager {

    private Map<Integer, Member> members = new HashMap<Integer, Member>();

    public void add(Member member) {
        members.put(member.getSerialNumber(), member);
    }

    public void delete(Integer serialNumber) {
        members.remove(serialNumber);
    }

    public Collection<Member> getAll() {
        return members.values();
    }

    public Member get(Integer serialNumber) {
        return members.get(serialNumber);
    }

    public void update(Member obj) {
        members.replace(obj.getSerialNumber(), obj);

    }

}
