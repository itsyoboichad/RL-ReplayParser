package com.itsyoboichad;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.DataInputStream;
import java.util.Collections;

public class PropertyDictionary {
    public static Dictionary<String, Property> pd;

    public static Dictionary deserialize(DataInputStream data) {
        pd = new Hashtable<>();
        try {
            Property prop;
            int loopbreak = 100;
            do {
                prop = Property.Deserialize(data);
                // System.out.println(prop.name);
                pd.put(prop.name, prop);
                // loopbreak--;
            } while (!prop.name.equals("None"));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        return pd;
    }

    public int size() {
        return pd.size();
    }

    public Property get(int i) {
        if (i >= 0 && i < pd.size()) {
            String key = Collections.list(pd.keys()).get(i);
            return pd.get(key);
        } else {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + pd.size());
        }
    }

    public String getJson() {
        Enumeration keys = pd.keys();
        String s = "{";
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Property p = pd.get(key);

            String propString = "";
            propString += p.name;
            if (p.value != null) {
                propString += ": " + p.value;
            }

            // System.out.println(propString);
            s += propString + ", ";
        }
        s += "}";
        return s;
    }
}
