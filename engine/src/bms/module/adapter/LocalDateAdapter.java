package bms.module.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LocalDate unmarshal(String v) {
        return LocalDate.parse(v, formatter);
    }

    public String marshal(LocalDate localDate) {
        return localDate.format(formatter);
    }
}





