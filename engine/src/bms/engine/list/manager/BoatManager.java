package bms.engine.list.manager;

import bms.module.Boat.NotFoundException;
import bms.module.Boat.AlreadyExistsException;

import java.util.*;

import bms.module.Boat;

import javax.xml.bind.annotation.XmlElement;

public class BoatManager {
    @XmlElement(name="boat", required = true)
    private List<Boat> boats = new ArrayList<Boat>();


    public void addBoat(Boat boat) throws AlreadyExistsException {

        validateBoatSerialNumber(boat.getSerialNumber());
        validateBoatName(boat.getName());

        boats.add(boat);
    }


    public void deleteBoat(int serialNumber) throws NotFoundException {
        Boat boat = getBoat(serialNumber);
        if (boat == null)
            throw new NotFoundException();
        boats.remove(serialNumber);
    }


    public Collection<Boat> getBoats() {
        return boats;
    }


    public Boat getBoat(int serialNumber) {
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

    public void updateBoat(Boat newBoat) throws NotFoundException, AlreadyExistsException {
        int serialNumber = newBoat.getSerialNumber();
        Boat currentBoat = getBoat(serialNumber);

        if (currentBoat == null)
            throw new NotFoundException();

        if (!currentBoat.getName().equals(newBoat.getName()))
            validateBoatName(newBoat.getName());

        boats.set(boats.indexOf(getBoat(serialNumber)), newBoat);
    }

    private void validateBoatSerialNumber(int serialNumber) throws AlreadyExistsException {
        if (getBoat(serialNumber) != null)
            throw new AlreadyExistsException("Boat with Serial Number '" + serialNumber + "' already exist");
    }

    private void validateBoatName(String name) throws AlreadyExistsException {
        if (getBoat(name) != null)
            throw new AlreadyExistsException("Boat with name '" + name + "' already exist");
    }

    public void eraseAll(){
        boats.clear();
    }

}
