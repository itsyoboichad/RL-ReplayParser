package com.itsyoboichad;

import java.io.DataInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringSerializer {
    public static String ReadString(DataInputStream dataInput) {
        try {
            int length = dataInput.readInt();
            length = Integer.reverseBytes(length);
            // System.out.println("*** Length of string to read: " + length);
            
            if (length > 0) {
                byte[] bytes = new byte[length];
                int byteCount = dataInput.read(bytes);
                if (length < 1701867377) {
                    String read = new String(bytes, 0, length - 1, Charset.forName("windows-1252"));
                    // System.out.println("\nReading string: " + read);
                    return read;
                }
                else {
                    System.out.println("String is too long");
                    return null;
                }
            } else if (length < 0) {
                System.out.println("Oh cool, we did that funky thing that has a negative length");
                byte[] bytes = dataInput.readNBytes(length * -2);
                return new String(bytes, 0, (length * -2) - 2, Charset.forName("windows-1252"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    
        return null;
    }
    
}
