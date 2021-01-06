package com.example.androidchess20;

import java.io.*;
import java.util.*;

public class Photo {

    public String uri = "path";
    public Map<String, ArrayList<String>> tags;

    //default constructor
    public Photo() {
        tags = new HashMap<>();
    }

    //constructor, given an image
    public Photo(String path) {//Image img,
        this.uri = path;
        tags = new HashMap<>();
    }
    public Photo(String path,String tags){
        this.uri = path;

        if(tags.equals(" ")){
            this.tags = new HashMap<>();
            return;
        }
        String[] rawTag = tags.split(",");
        if(rawTag.length >= 1) {
            for(String a: rawTag) {
                String[] lessRaw = a.split("=");
                if(lessRaw.length%2 != 0) {
                    this.tags = new HashMap<>();
                    break;
                }
                int i = 0;
                while(i < lessRaw.length) {
                    addTag(lessRaw[i], lessRaw[i+1]);
                    i++;
                    i++;
                }
            }
        }else {
            this.tags = new HashMap<>();
        }
    }

    public void addTag(String type, String value) {
        //see if tag type is already there
        if(tags == null) {
            tags = new HashMap<>();
        }
        if(tags.containsKey(type)) {
            tags.get(type).add(value);
        }
        //new tag type, first add type and list to hash, then add value
        else {
            ArrayList<String> values = new ArrayList<>();
            tags.put(type, values);
            tags.get(type).add(value);
        }

    }

    public void editTag(String oldType,String newType) {
        ArrayList<String> temp = tags.get(oldType);
        tags.remove(oldType);
        tags.put(newType, temp);
    }

    public void editValue(String type, String oldValue, String newValue) {
        tags.get(type).remove(oldValue);
        tags.get(type).add(newValue);
    }

    public void removeTag(String type) {
        tags.remove(type);
    }

    public void removeValue(String type, String value) {

        tags.get(type).remove(value);
    }

    public String getAllTags() {
        String allTags = "";
        boolean hadNothing = true;
        Set<String> set = tags.keySet();
        Iterator<String> it = set.iterator();
        while(it.hasNext()) {
            hadNothing = false;
            String temp = it.next();
            ArrayList<String> values = tags.get(temp);
            for(int j=0;j<values.size();j++) {
                allTags += temp+"="+values.get(j)+",";
            }
        }
        if(allTags.length() > 1) {
            String allTag2 = allTags.substring(0, allTags.length()-1);
            return allTag2;
        }
        if(hadNothing == true){
            return " ";
        }
        return allTags;
    }
    public boolean getTag(String type,String value) {
        if(tags.containsKey(type) == true) {
            if(tags.get(type).contains(value) == true){
                return true;
            }
        }
        return false;
    }
}
