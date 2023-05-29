package com.itsyoboichad;

public class JsonWriter {
    public String toJson(Replay replay) {
        String json = "{";

        System.out.println("Properties: *************** " + replay.properties.getJson());


        json += "},";
        return json;
    }
}
