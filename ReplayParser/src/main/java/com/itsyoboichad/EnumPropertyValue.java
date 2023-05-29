package com.itsyoboichad;

import java.io.DataInputStream;

public class EnumPropertyValue {
    public String type;
    public String value;

    public static EnumPropertyValue deserialize(DataInputStream data) {
        EnumPropertyValue epv = new EnumPropertyValue();
        epv.type = StringSerializer.ReadString(data);
        epv.value = StringSerializer.ReadString(data);

        return epv;
    }

    @Override
    public String toString() {
        return value;
    }
}
