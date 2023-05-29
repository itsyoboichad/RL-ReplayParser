package com.itsyoboichad;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Dictionary;
import java.util.Collections;

public class ArrayProperty extends Property implements Iterable<Iterable<Property>> {

    public ArrayProperty(String name, String type) {
        super(name, type);
    }

    @Override
    public void DeserializeValue(DataInputStream data) {
        try {
            if (name.equals("None")) {
                value = name;
                System.out.println("Name is none");
                return;
            }
            if (type.equals("ArrayProperty")) {
                var propertyLists = new ArrayList<Dictionary<String, Property>>();
                int length = Integer.reverseBytes(data.readInt());

                System.out.println("Name: " + super.name + ", type: " + super.type);

                for (int i = 0; i < length; i++) {
                    var properties = PropertyDictionary.deserialize(data);
                    propertyLists.add(properties);
                }
                value = propertyLists;
            }
            else {
                System.out.println("There's an issue");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        
    }

    @Override
    public Iterator<Iterable<Property>> iterator() {
        return new ArrayPropertyIterator();
    }

    @Override
    public String toString() {
        if (value.getClass() == ArrayProperty.class) {
            return ((PropertyDictionary)value).getJson();
        }
        else{
            return "oops";
        }
    }

    private class ArrayPropertyIterator implements Iterator<Iterable<Property>> {
        private int currentIndex = 0;
        private ArrayList<Dictionary<String, Property>> propertyLists;

        public ArrayPropertyIterator() {
            this.propertyLists = (ArrayList<Dictionary<String, Property>>) value;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < propertyLists.size();
        }

        @Override
        public Iterable<Property> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Dictionary<String, Property> properties = propertyLists.get(currentIndex++);
            ArrayList<Property> propertyArrayList = Collections.list(properties.elements());

            return propertyArrayList;
        }
    }
}