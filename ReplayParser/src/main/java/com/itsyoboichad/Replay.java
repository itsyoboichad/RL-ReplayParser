package com.itsyoboichad;

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Replay {
    int headerLength;
    int headerCrc;
    int engineVersion;
    int licenseVersion;
    int netVersion;
    String TAGame_replay_Soccar_TA;

    public Replay(File replayFile) {
        try {
            FileInputStream file = new FileInputStream(replayFile);
            DataInputStream data = new DataInputStream(file);

            headerLength = readInt(data);
            headerCrc = readInt(data);
            engineVersion = readInt(data);
            licenseVersion = readInt(data);
            if (engineVersion >= 868 && licenseVersion >= 18)
                netVersion = readInt(data);
            TAGame_replay_Soccar_TA = readString(data);

            //TODO: Parse Properties with PropertyList

            file.close();
            data.close();
        } catch (Exception e) {
            //TODO: handle constructor exception
        }

    }

    public static int readInt(DataInputStream data) {
        try {
            return Integer.reverseBytes(data.readInt());
        } catch (IOException io) {
            System.out.println("Error reading integer: " + io.getMessage());
            return 0;
        }
    }

    public static String readString(DataInputStream data) {
        try {
            int length = readInt(data);
            if (length > 0) {
                if (length < 1701867377) {
                    byte[] bytes = new byte[length];
                    data.read(bytes);
                    return new String(bytes, 0, length - 1, Charset.forName("windows-1252"));
                } else {
                    System.out.println("String is too long");
                    return null;
                }
            } else if (length < 0) {
                System.out.println("length is negative!");
                byte[] bytes = data.readNBytes(length * -2);
                return new String(bytes, 0, (length * -2), Charset.forName("windows-1252"));
            }
        } catch (IOException io) {
            System.out.println("Error reading string: " + io.getMessage());
        }

        return null;
    }
}
