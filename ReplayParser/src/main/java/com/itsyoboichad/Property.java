package com.itsyoboichad;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Property {
    public String name;
    public String type;
    public int dataLength;
    public int unknown;
    public Object value;
    public Property(DataInputStream data) {
        try {
            name = Replay.readString(data);
            if (!name.equals("None")){
                type = Replay.readString(data);

                dataLength = Replay.readInt(data);
                unknown = Replay.readInt(data);

                if (type.equals("ArrayProperty")) {
                    // TODO: fix this shit
//                    List<Property> propertyList = new ArrayList<Property>();
//                    int length = Replay.readInt(data);
//
//                    for (int i = 0; i < length; i++) {
//                        propertyList.add();
//                    }
                } else {
                    if (type.equals("IntProperty")) {
                        value = Replay.readInt(data);
                    } else if (type.equals("StrProperty")) {
                        value = Replay.readString(data);
                    } else if (type.equals("FloatProperty")) {
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
                        value = buffer.getFloat();
                    } else if (type.equals("ByteProperty")) {
                        value = new EnumPropertyValue(data);
                    } else if (type.equals("BoolProperty")) {
                        value = data.readBoolean();
                    } else if (type.equals("QWordProperty")) {
                        value = Long.reverseBytes(data.readLong());
                    } else {
                        System.out.println("Found property type not handled: " + type);
                    }
                }

            }
        } catch (IOException io) {
            System.out.println("Error reading property: " + io.getMessage());
        }
    }

    public class EnumPropertyValue {
        public String type;
        public String value;

        public EnumPropertyValue(DataInputStream data) {
            type = Replay.readString(data);
            value = Replay.readString(data);
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
