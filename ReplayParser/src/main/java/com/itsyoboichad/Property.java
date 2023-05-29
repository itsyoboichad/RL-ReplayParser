package com.itsyoboichad;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Collections;
import java.nio.ByteBuffer;
import com.google.gson.Gson;

public class Property {
    public String name;
    public String type;
    public int dataLength;
    public int unknown; // Such mystery
    public Object value;

    public Property() {}

    public Property(String name) {
        this.name = name;
    }

    public Property(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static Property Deserialize(DataInputStream dataInput) {
        
        Property p = new Property();
        try {
            String _name = StringSerializer.ReadString(dataInput);
            System.out.print(_name);
            if (_name.equals("None")) {
                System.out.println();
                return new Property(_name);
            }

            String _type = StringSerializer.ReadString(dataInput);

            if (_type.equals("ArrayProperty")) {
                p = new ArrayProperty(_name, _type);
            } else {
                p.name = _name;
                p.type = _type;
            }

            p.dataLength = Integer.reverseBytes(dataInput.readInt());
            p.unknown = Integer.reverseBytes(dataInput.readInt());

            p.DeserializeValue(dataInput);

        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return p;
    }

    public void DeserializeValue(DataInputStream dataInput) {
        try {
            if (type.equals("IntProperty")) {
                value = Integer.reverseBytes(dataInput.readInt());
                System.out.println(" " + value);
            } else if (type.equals("StrProperty") || type.equals("NameProperty")) {
                value = StringSerializer.ReadString(dataInput);
                System.out.println(" " + value);
            } else if (type.equals("FloatProperty")) {
                ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
                buffer.putFloat(dataInput.readFloat());
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
                
                System.out.println(" " + value);
            } else if (type.equals("ByteProperty")) {
                value = EnumPropertyValue.deserialize(dataInput);
                System.out.println(" " + value);
            } else if (type.equals("BoolProperty")) {
                value = dataInput.readBoolean();
                System.out.println(" " + value);
            } else if (type.equals("QWordProperty")) {
                value = Long.reverseBytes(dataInput.readLong());
                System.out.println(" " + value);
            } else {
                System.out.println(" Have not handled yet: " + type);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public ArrayList getList(){
        if (value instanceof ArrayList) {
            return (ArrayList<Dictionary<String, Property>>)value;
        }

        return null;
    }

    @Override
    public String toString() {
        String s = name;
        if (value != null) {
            s += value;
        }
        return s;
    }
}


