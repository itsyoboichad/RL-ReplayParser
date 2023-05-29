package com.itsyoboichad;
import java.util.*; // TODO: pear down which imports I actually need

public class PropertyList {
    public List<Property> list;
    public PropertyList(){
        list = new ArrayList<Property>();
    }

    public void add(Property prop) {
        list.add(prop);
    }
}
