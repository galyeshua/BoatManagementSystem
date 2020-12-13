package bms.schema.convertor;

import bms.engine.list.manager.Exceptions;
import bms.module.Member;
import bms.schema.generated.member.RowingLevel;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;


public class memberConvertor {

    private static int parseInteger(String number){
        try{
            return Integer.parseInt(number);
        } catch (NumberFormatException e){
            throw new Exceptions.IllegalMemberValueException("cannot read number '" + number + "' (it must be a number)");
        }
    }


    private static LocalDate localDateFromXMLGregorianCalendar(XMLGregorianCalendar time){
        return LocalDate.of(time.getYear(), time.getMonth(), time.getDay());
    }

    private static XMLGregorianCalendar XMLGregorianCalendarFromLocalDate(LocalDate localDate) throws DatatypeConfigurationException {
        GregorianCalendar gcal = GregorianCalendar.from(ZonedDateTime.from(localDate.atStartOfDay(ZoneId.systemDefault())));
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        return xcal;
    }

    public static Member memberFromSchemaMember(bms.schema.generated.member.Member schemaMember){
        Member newMember;

        int SerialNumber = parseInteger(schemaMember.getId());
        String name = schemaMember.getName();
        String email = schemaMember.getEmail();
        String password = schemaMember.getPassword();

        newMember = new Member(SerialNumber, name, email, password);

        if(schemaMember.getAge() != null)
            newMember.setAge(schemaMember.getAge());

        if(schemaMember.getLevel() != null)
            newMember.setLevel(Member.Level.fromName(schemaMember.getLevel().value()));

        if(schemaMember.isManager() != null)
            newMember.setManager(schemaMember.isManager());

        if(schemaMember.getPrivateBoatId() != null)
            newMember.setBoatSerialNumber(parseInteger(schemaMember.getPrivateBoatId()));

        if(schemaMember.getPhone() != null)
            newMember.setPhoneNumber(schemaMember.getPhone());

        if(schemaMember.getComments() != null)
            newMember.setNotes(schemaMember.getComments());

        if(schemaMember.getJoined() != null)
            newMember.setJoinDate(localDateFromXMLGregorianCalendar(schemaMember.getJoined()));

        if(schemaMember.getMembershipExpiration() != null)
            newMember.setExpireDate(localDateFromXMLGregorianCalendar(schemaMember.getMembershipExpiration()));

        return newMember;
    }

    public static bms.schema.generated.member.Member schemaMemberFromMember(Member member) throws DatatypeConfigurationException {
        bms.schema.generated.member.Member schemaMember = new bms.schema.generated.member.Member();

        schemaMember.setId(String.valueOf(member.getSerialNumber()));
        schemaMember.setName(member.getName());
        schemaMember.setPassword(member.getPassword());
        schemaMember.setEmail(member.getEmail());
        schemaMember.setHasPrivateBoat(member.getHasPrivateBoat());
        schemaMember.setJoined(XMLGregorianCalendarFromLocalDate(member.getJoinDate()));
        schemaMember.setMembershipExpiration(XMLGregorianCalendarFromLocalDate(member.getExpireDate()));
        schemaMember.setManager(member.getManager());
        schemaMember.setLevel(RowingLevel.fromValue(member.getLevel().getName()));

        if (member.getAge() != null)
            schemaMember.setAge(member.getAge());

        if (member.getNotes() != null)
            schemaMember.setComments(member.getNotes());

        if (member.getBoatSerialNumber() != null)
            schemaMember.setPrivateBoatId(String.valueOf(member.getBoatSerialNumber()));

        if (member.getPhoneNumber() != null)
            schemaMember.setPhone(member.getPhoneNumber());

        return schemaMember;
    }
}
