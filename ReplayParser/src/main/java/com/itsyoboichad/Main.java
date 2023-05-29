package com.itsyoboichad;

import java.io.File;
import com.google.gson.Gson;
public class Main {
    public static void main(String[] args) {
        Replay replay = new Replay("C:/Users/Chand/Documents/My Games/Rocket League/TAGame/Demos/F341BFBF42049A60C1CEA4B398CC308C.replay");
        // String replayJson = new Gson().toJson(replay);
        String replayJson = new JsonWriter().toJson(replay);
        System.out.println("Printing json: -------------------------------------------------------------------------\n" + replayJson);
    }
}