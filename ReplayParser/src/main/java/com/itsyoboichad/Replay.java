package com.itsyoboichad;

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

import javax.swing.text.html.HTMLDocument.HTMLReader.TagAction;

public class Replay {
    int headerLength;
    int headerCrc; // This is reading wrong but probably doesn't matter unless I want to use checksums
    int engineVersion;
    int licenseVersion;
    int netVersion;
    String gameType;

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
            gameType = readString(data);

            System.out.printf("Header size: %d%nheader crc: %d%nengine version: %d%n", headerLength, headerCrc, engineVersion);
            System.out.printf("license version: %d%nnet version: %d%ngame type: %s%n", licenseVersion, netVersion, gameType);
            
            PropertyList properties = new PropertyList();
            deserializePropertyList(properties, data);

            file.close();
            data.close();
        } catch (Exception e) {
            //TODO: handle constructor exception
        }

    }

    public static void deserializePropertyList(PropertyList list, DataInputStream data) {
        try {
            Property prop;
            System.out.println("\nStart of property list: \n");
            do {
                prop = new Property();
                prop.name = readString(data);
                System.out.println("\nProperty name: " + prop.name);
                if (!prop.name.equals("None")){
                    prop.type = readString(data);
                    System.out.println("Type: " + prop.type);
                    prop.dataLength = readInt(data);
                    System.out.println("Data length: " + prop.dataLength);
                    prop.unknown = readInt(data);
                    // Initialize value
                    if (prop.type.equals("IntProperty")) {
                        prop.value = readInt(data);
                    } else if (prop.type.equals("StrProperty")) {
                        prop.value = readString(data);
                    } else if (prop.type.equals("FloatProperty")) {
                        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
                        buffer.putFloat(data.readFloat());
                        buffer.flip();
                        byte[] bytes = new byte[Float.BYTES];
                        buffer.get(bytes);
                        for (int i = 0; i < bytes.length / 2; i++) {
                            byte temp = bytes[i];
                            bytes[i] = bytes[bytes.length - i - 1];
                            bytes[bytes.length - i - 1] = temp;
                        }
                        buffer.clear();
                        buffer.put(bytes);
                        buffer.flip();
                        prop.value = buffer.getFloat();
                    } else if (prop.type.equals("ByteProperty")) {
                        System.out.println("Enum property here");
                    } else if (prop.type.equals("BoolProperty")) {
                        prop.value = data.readBoolean();
                    } else if (prop.type.equals("QWordProperty")) {
                        prop.value = Long.reverseBytes(data.readLong());
                    } else if (prop.type.equals("ArrayProperty")) {
                        prop.value = new PropertyList();
                        deserializePropertyList((PropertyList)prop.value, data);
                    }
                }
                
                if (prop.value != null)
                    System.out.println("value: " + prop.value);

                list.add(prop);
            } while (!prop.name.equals("None"));
        } catch (Exception e) {
            System.out.println("Error deserializing property: " + e.getMessage());
        }
    }

    public static int readInt(DataInputStream data) {
        try {
            int read = Integer.reverseBytes(data.readInt());
            System.out.println("Reading integer: " + read);
            return read;
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
                    String read = new String(bytes, 0, length - 1, Charset.forName("windows-1252"));
                    System.out.println("String read: " + read);
                    return read;
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
