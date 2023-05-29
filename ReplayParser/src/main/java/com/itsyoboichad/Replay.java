package com.itsyoboichad;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class Replay {
    PropertyDictionary properties;
    int part1Length;
    // Check if these need to be long, or if they can just be ints
    int part1Crc;
    int engineVersion; // aka major_version in boxcars
    int licenseVersion; // aka minor_version in boxcars
    int netVersion;
    // Does this have any meaning?
    String TAGame_replay_Soccar_TA;

    public Replay(String filePath) {
        try{
            FileInputStream fileIn = new FileInputStream(filePath);
            DataInputStream dataIn = new DataInputStream(fileIn);

            parseReplayHeader(dataIn);

            dataIn.close();
            fileIn.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    void parseReplayHeader(DataInputStream data) {
        try {
            this.part1Length = Integer.reverseBytes(data.readInt());
            System.out.println("Header length: " + this.part1Length);
            this.part1Crc = Integer.reverseBytes(data.readInt());
            System.out.println("Part 1 Crc: " + this.part1Crc);
            this.engineVersion = Integer.reverseBytes(data.readInt());
            System.out.println("Engine version: " + this.engineVersion);
            this.licenseVersion = Integer.reverseBytes(data.readInt());
            System.out.println("License version: " + this.licenseVersion);
            if (engineVersion >= 868 && licenseVersion >= 18) {
                this.netVersion = Integer.reverseBytes(data.readInt());
                System.out.println("NetVersion: " + this.netVersion);
            }

            TAGame_replay_Soccar_TA = StringSerializer.ReadString(data);
            System.out.println("TAGame_replay_Soccar_TA: " + TAGame_replay_Soccar_TA);

            properties = new PropertyDictionary();
            properties.deserialize(data);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
        }
    }
}
