package com.example.androidchess20;
import java.io.*;
import java.util.*;
public class Album {

    public String name;
    public static Photo[] photos;
    public static ArrayList<Photo> photos2;
    int count = 0;
    int size = 999;

    //hello
    public Album(String name) {// ArrayList<Photo> photo) {
        this.name = name;
        //this.photos = photo;
        //photos = new ArrayList<Photo>();

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPhoto(Photo photo) {
        //photos.add(photo);
       // if(count == size){
        //    //make bigger array
       // }
       // photos[count] = photo;
        photos2.add(photo);
        //count++;
    }

    public void removePhoto(int index) {
       // for(int i = index;i<size-1;i++){
       //     photos[i] = photos[i+1];
       // }
        //size--;
        photos2.remove(index);
    }

    public Photo getPhoto(int index) {
        //return photos.get(index);
        return photos2.get(index);
    }

//	public ArrayList<Photo> getPhotos(){
    //	return photos;
//	}



    public static ArrayList<String> Search(String one) {
        String[] input = one.split("=");
        ArrayList<String> matching = new ArrayList<>();
        for(Photo a : photos2) {
            ArrayList<String> temp = a.tags.get(input[0]);
            if(temp != null) {
                for (String d : temp) {
                    if (d.contains(input[1])) {
                        matching.add(a.uri);
                        break;
                    }
                }
            }
        }
        return matching;
    }

    public static ArrayList<String> Search(String one, String two, String junction) {
        String[] input = one.split("=");
        String[] input2 = two.split("=");
        ArrayList<String> matching = new ArrayList<>();
        if(junction.compareTo("AND") == 0) {
            for(Photo a : photos2) {
                ArrayList<String> temp = a.tags.get(input[0]);
                ArrayList<String> temp2 = a.tags.get(input2[0]);
                if(temp != null && temp2 != null) {
                    for (String d : temp) {
                        if (d.contains(input[1])) {
                            for(String e: temp2){
                                if(e.contains(input2[1])){
                                    matching.add(a.uri);
                                    break;
                                }
                            }
                        }break;
                    }
                }
            }
            return matching;
        }
        //OR
        else {
            for(Photo a : photos2) {
                ArrayList<String> temp = a.tags.get(input[0]);
                ArrayList<String> temp2 = a.tags.get(input2[0]);
                if(temp == null && temp2 == null){
                    continue;
                }
                else if(temp != null && temp2 == null){
                    for (String d : temp) {
                        if (d.contains(input[1])) {
                            matching.add(a.uri);
                            break;
                        }
                    }
                }
                else if(temp == null && temp2 != null){
                    for (String d : temp2) {
                        if (d.contains(input[1])) {
                            matching.add(a.uri);
                            break;
                        }
                    }
                }
                else if(temp != null && temp2 != null){
                    for (String d : temp) {
                        if (d.contains(input[1])) {
                            matching.add(a.uri);
                            break;
                        }
                    }
                    for (String d : temp2) {
                        if (d.contains(input[1])) {
                            matching.add(a.uri);
                            break;
                        }
                    }
                }
            }
        }


        return matching;
    }

    //Given a photo, find the index to which it belongs in an album
    public int findIndexByPhoto(Photo photo) {
        for (int i = 0; i < photos.length; i++)
            if (photos[i].equals(photo))
                return i;
        return -1;
    }

}
