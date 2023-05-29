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
    
    @Override
    public String toString(){
        return name + ": " + value.toString();
    }
}
