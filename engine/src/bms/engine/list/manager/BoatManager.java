package bms.engine.list.manager;

import bms.engine.list.manager.Exceptions.BoatNotFoundException;
import bms.engine.list.manager.Exceptions.BoatAlreadyExistsException;

import java.util.*;

import bms.module.Boat;

import javax.xml.bind.annotation.XmlElement;

public class BoatManager {
    @XmlElement(name="boat", required = true)
    //private Map<Integer, Boat> boats = new HashMap<Integer, Boat>();
    private List<Boat> boats = new ArrayList<Boat>();


    public void addBoat(Boat boat) throws BoatAlreadyExistsException{

        validateBoatSerialNumber(boat.getSerialNumber());
        validateBoatName(boat.getName());

        //boats.put(boat.getSerialNumber(), boat);
        boats.add(boat);
    }


    public void deleteBoat(int serialNumber) throws BoatNotFoundException{
        Boat boat = getBoat(serialNumber);
        if (boat == null)
            throw new BoatNotFoundException();
        boats.remove(serialNumber);
    }


    public Collection<Boat> getBoats() {
        //return boats.values();
        return boats;
    }


    public Boat getBoat(int serialNumber) {
        //return boats.get(serialNumber);
        for(Boat boat : getBoats())
            if(boat.getSerialNumber()==serialNumber)
                return boat;
        return null;
    }


    public Boat getBoat(String name) {
        for(Boat boat : getBoats())
            if(boat.getName().equals(name))
                return boat;
        return null;
    }

    public void updateBoat(Boat newBoat) throws BoatNotFoundException {
        int serialNumber = newBoat.getSerialNumber();
        Boat currentBoat = getBoat(serialNumber);

        if (currentBoat == null)
            throw new BoatNotFoundException();

        if (!currentBoat.getName().equals(newBoat.getName()))
            validateBoatName(newBoat.getName());

        //boats.replace(serialNumber, newBoat);
        boats.set(boats.indexOf(getBoat(serialNumber)), newBoat);
    }

    private void validateBoatSerialNumber(int serialNumber) {
        if (getBoat(serialNumber) != null)
            throw new Exceptions.BoatAlreadyExistsException("Boat with Serial Number '" + serialNumber + "' already exist");
    }

    private void validateBoatName(String name) {
        if (getBoat(name) != null)
            throw new Exceptions.BoatAlreadyExistsException("Boat with name '" + name + "' already exist");
    }

    public void eraseAll(){
        boats.clear();
    }

}
