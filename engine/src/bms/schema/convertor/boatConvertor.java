package bms.schema.convertor;

import bms.module.Boat;
import bms.module.BoatView;

public class boatConvertor {
    public static Boat boatFromSchemaBoat(bms.schema.generated.boat.Boat schemaBoat){
        Boat newBoat;

        int SerialNumber = Integer.parseInt(schemaBoat.getId());
        String name = schemaBoat.getName();
        Boat.BoatType boatType = BoatView.BoatType.fromName(schemaBoat.getType().value());

        newBoat = new Boat(SerialNumber, name, boatType);

        if(schemaBoat.isCostal() != null)
            newBoat.setMarine(schemaBoat.isCostal());
        if(schemaBoat.isOutOfOrder() != null)
            newBoat.setDisabled(schemaBoat.isOutOfOrder());
        if(schemaBoat.isPrivate() != null)
            newBoat.setPrivate(schemaBoat.isPrivate());
        if(schemaBoat.isWide() != null)
            newBoat.setWide(schemaBoat.isWide());

        return newBoat;
    }

    public static bms.schema.generated.boat.Boat schemaBoatFromBoat(Boat boat){
        bms.schema.generated.boat.Boat schemaBoat = new bms.schema.generated.boat.Boat();
        schemaBoat.setId(String.valueOf(boat.getSerialNumber()));
        schemaBoat.setName(boat.getName());
        schemaBoat.setPrivate(boat.getPrivate());
        schemaBoat.setWide(boat.getWide());
        schemaBoat.setHasCoxswain(boat.getHasCoxswain());
        schemaBoat.setCostal(boat.getMarine());
        schemaBoat.setOutOfOrder(boat.getDisabled());
        bms.schema.generated.boat.BoatType generatedType = bms.schema.generated.boat.BoatType.fromValue(boat.getType().getName());
        schemaBoat.setType(generatedType);
        return schemaBoat;
    }
}
