package bms.module.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");

    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v, formatter);
    }

    public String marshal(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
