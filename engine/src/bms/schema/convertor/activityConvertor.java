package bms.schema.convertor;

import bms.module.Activity;
import bms.module.BoatView;
import bms.schema.generated.activity.Timeframe;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class activityConvertor {

    public static Activity activityFromSchemaActivity(bms.schema.generated.activity.Timeframe schemaActivity){
        Activity newActivity;

        LocalTime startTime = LocalTime.parse(schemaActivity.getStartTime());
        LocalTime finishTime = LocalTime.parse(schemaActivity.getEndTime());
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
