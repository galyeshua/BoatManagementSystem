package bms.engine.list.manager;

import java.time.LocalDateTime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bms.module.Boat;

public class BoatManager {

    public Map<Integer, Boat> boats = new HashMap<Integer, Boat>();

    public void add(Boat boat) {
        boats.put(boat.getSerialNumber(), boat);
    }

    public void delete(Integer serialNumber) {
       boats.remove(serialNumber);
    }

    public Collection<Boat> getAll() {
        return boats.values();
    }

    public Boat get(Integer serialNumber) {
        return boats.get(serialNumber);
    }

    public void update(Boat obj) {
        boats.replace(obj.getSerialNumber(), obj);

    }
}
