package com.itsyoboichad;

import java.io.File;
import com.google.gson.Gson;
public class Main {
    public static void main(String[] args) {
        Replay replay = new Replay(new File("C:/Users/Chand/Documents/My Games/Rocket League/TAGame/Demos/62AC62EA490651F26D4CEC9636A0235A.replay"));
        String replayJson = new Gson().toJson(replay);
        // System.out.println(replayJson);
    }
}