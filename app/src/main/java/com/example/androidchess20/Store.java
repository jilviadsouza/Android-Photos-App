package com.example.androidchess20;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Store {
    public static void writeToAlbumFile(ArrayList<String> albums, Context context){
        File directory = new File(context.getFilesDir()+File.separator+"Albums");
        //for first time use
        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(directory, "ListOfAlbums.txt");
        //for first time use
        if(!newFile.exists()){
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String build = "";
        for(String album:albums){
            build += album+"\n";
        }
        try  {
            FileOutputStream fOut = new FileOutputStream(newFile);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(build);
            outputWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ArrayList<Album> objectAlbum;
    public static Album bum;
    public static Context context;
    public static Context aContext;
    public static int count;
    public static String[] imageIDs;
    public static ArrayList<String> imageIDs2;
    public static ArrayList<Photo> seech;
    public static String name;

    public static void delete() throws IOException {
       // stringAlbum.remove(bum.getName());
        MainActivity.albums.remove(bum.getName());
        objectAlbum.remove(bum);
        MainActivity.delete(bum.getName(),context);
        writeToAlbumFile(MainActivity.albums,context);
    }

    public static void rename(String rename){
        int i = MainActivity.albums.indexOf(bum.getName());

        File sourceFile = new File(context.getFilesDir()+File.separator+"Albums"+File.separator+bum.getName());
        File destFile = new File(context.getFilesDir()+File.separator+"Albums"+File.separator+rename);

        MainActivity.albums.set(i,rename);
        i = objectAlbum.indexOf(bum);
        objectAlbum.get(i).setName(rename);
        bum = objectAlbum.get(i);
        writeToAlbumFile(MainActivity.albums,context);


        if (sourceFile.renameTo(destFile)) {
            System.out.println("File renamed successfully");
        } else {
            System.out.println("Failed to rename file");
        }
    }
}


