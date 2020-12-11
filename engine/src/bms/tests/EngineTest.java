package bms.tests;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.Member;
import bms.module.MemberView;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EngineTest {

    @Test
    public void getListOfMembersShouldBeEmpty(){
        BMSEngine engine = new Engine();
        Collection<MemberView> members = engine.getMembers();

        assertEquals(0, members.size());
    }

//    @Test
//    public void getListOfMembersAfterAddMemberSizeShuldBeOne(){
//        BMSEngine engine = new Engine();
//        Collection<MemberView> members = engine.getMembers();
//
//        engine.addMember(123, "Gal", 23, "PRO", Member.Level.BEGINNER,
//                LocalDate.of(2020, 12, 20), LocalDate.of(2021, 12, 20),
//                false, 0, "054-5454545", "gal@gmail.com",
//                "1234", true);
//
//        assertEquals(1, members.size());
//    }

}
