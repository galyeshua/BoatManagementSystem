package bms.schema.convertor;

import bms.engine.list.manager.Exceptions;
import bms.module.Activity;
import bms.module.BoatView;
import bms.schema.generated.activity.Timeframe;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class activityConvertor {

    private static LocalTime parseLocalTime(String time){
        try{
            return LocalTime.parse(time);
        } catch (DateTimeParseException e){
            throw new Exceptions.IllegalActivityValueException("cannot read time '" + time + "' (Time Format should be HH:mm)");
        }
    }

    public static Activity activityFromSchemaActivity(bms.schema.generated.activity.Timeframe schemaActivity){
        Activity newActivity;
        LocalTime startTime, finishTime;

        startTime = parseLocalTime(schemaActivity.getStartTime());
        finishTime = parseLocalTime(schemaActivity.getEndTime());
        String name = schemaActivity.getName();

        newActivity = new Activity(name, startTime, finishTime);

        if(schemaActivity.getBoatType() != null){
            BoatView.BoatType boatType = BoatView.BoatType.fromName(schemaActivity.getBoatType().value());
            newActivity.setBoatType(boatType);
        }

        return newActivity;
    }


    public static bms.schema.generated.activity.Timeframe schemaActivityFromActivity(Activity activity){
        Timeframe timeframe = new Timeframe();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        timeframe.setName(activity.getName());
        timeframe.setStartTime(activity.getStartTime().format(formatter));
        timeframe.setEndTime(activity.getFinishTime().format(formatter));

        if (activity.getBoatType() != null){
            bms.schema.generated.activity.BoatType generatedType = bms.schema.generated.activity.BoatType.fromValue(activity.getBoatType().getName());
            timeframe.setBoatType(generatedType);
        }

        return timeframe;
    }

}
