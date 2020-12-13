package bms.module.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public LocalTime unmarshal(String v) {
        return LocalTime.parse(v, formatter);
    }

    public String marshal(LocalTime localTime) {
        return localTime.format(formatter);
    }
}
