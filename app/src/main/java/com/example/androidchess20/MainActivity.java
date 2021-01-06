package com.example.androidchess20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;




import java.util.*;
import java.io.*;


//test comment 1, 2:21 //
public class MainActivity extends AppCompatActivity {

    //start
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

    public static void writeToPhotoFile(String album,ArrayList<Photo> photos, Context context){
        File directory = new File(context.getFilesDir()+File.separator+"Albums"+File.separator+album);
        //for first time use
        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(directory, "ListOfPhotos.txt");
        //for first time use
        if(!newFile.exists()){
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String build = "";
        if(photos != null) {
            for (Photo a : photos) {
                build += a.uri + "\t" + a.getAllTags() + "\n";
            }
            try {
                FileOutputStream fOut = new FileOutputStream(newFile);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fOut);
                outputWriter.write(build);
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //end


    public static String[] readFromAlbumFile(Context context) throws IOException {
        File directory = new File(context.getFilesDir()+File.separator+"Albums");
        File newFile = new File(directory, "ListOfAlbums.txt");
        //System.out.println(context.getFilesDir()+File.separator+"Albums");

                //char[] array = new char[50];
        //String s = new String(array);
        StringBuffer str = new StringBuffer();

        FileInputStream fOut = new FileInputStream(newFile);
        InputStreamReader inputWriter=new InputStreamReader(fOut);

        char[] a = new char[1];
        int data = inputWriter.read(a);

        while (data != -1)
        {
            str.append(a);
            data = inputWriter.read(a);
        }
        inputWriter.close();
        String[] temp = str.toString().split("\n");

        ArrayList<Album> album = new ArrayList<>();
        //creating Arraylist for app instance
        for(int i=0;i<temp.length;i++){
            album.add(new Album(temp[i]));
        }
        Store.objectAlbum= album;
        return temp;
    }

    public static /*Photo[]*/ArrayList<Photo> readFromPhotoFile(String album,Context context) throws IOException {
        File directory = new File(context.getFilesDir()+File.separator+"Albums"+File.separator+album);
        File newFile = new File(directory, "ListOfPhotos.txt");
        //char[] array = new char[50];
        //String s = new String(array);
        StringBuffer str = new StringBuffer();

        FileInputStream fOut = new FileInputStream(newFile);
        InputStreamReader inputWriter=new InputStreamReader(fOut);



        char[] a = new char[1];
        int data = inputWriter.read(a);

        while (data != -1) {
            str.append(a);
            data = inputWriter.read(a);

        }
        fOut.close();
        inputWriter.close();
        //Photo[] oto = new Photo[1000];
        ArrayList<Photo> oto2 = new ArrayList<>();
        if(!str.toString().equals("")) {
            String[] temp = str.toString().split("\n");
            for(int i = 0;i<temp.length;i++){
                String[] arr = temp[i].split("\t");
                //oto[i] = new Photo(arr[0],arr[1]);
                oto2.add(new Photo(arr[0],arr[1]));
            }
            return oto2;
        }
        return oto2;
    }

    public static void delete(String album,Context context) throws IOException {

        File dir = new File(context.getFilesDir()+File.separator+"Albums"+
                File.separator+album);
        FileUtils.deleteDirectory(dir);
    }

    public static void loadAlbums() throws IOException {
        for(int i = 0;i<Store.objectAlbum.size();i++){
            Store.objectAlbum.get(i).photos2 = readFromPhotoFile(Store.objectAlbum.get(i).getName(),Store.context);
        }
    }

    public static ListView albumListView;
    public static ArrayList<String> albums;


    public static final int ALBUM_DISPLAY_CODE = 1;

    @Override
     protected void onCreate(Bundle savedInstanceState) {

        //ArrayList<Photo> aaa = new ArrayList<>();
       // aaa.add(new Photo("content://com.android.providers.media.documents/document/image%3A26"));
        //ArrayList<String> bumd = new ArrayList<>();
        //bumd.add("stock");
        //bumd.add("memes");
        //writeToAlbumFile(bumd,this);
        String[] a = null;
        Store.context = this;
        try {
           a = readFromAlbumFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        //String one = a[0];
        albums = new ArrayList<>();
        for(String b:a){
            albums.add(b);
        }
        try {
            loadAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        }
        albumListView = findViewById(R.id.album_list);
        albumListView.setAdapter(new ArrayAdapter<String>(this, R.layout.album, albums));

        //listener to open albums when clicked

        albumListView.setOnItemClickListener((p, V, pos, id) ->
                        showAlbum(pos));
    }

    //show menu
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.create_album_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action for menu button
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_album:
                //call add album method
                addAlbum();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //add album code
    //CHANGES: i made the dialog box in place so that i could put a check for albumName being empty
    //that means we are not using CreateAlbumDialogFragment anymore. Hallelujah!
    private void addAlbum(){
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.create_album_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.create);
        alert.setView(inflator);

        final EditText albumName = (EditText) inflator.findViewById(R.id.albumName);

        alert.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            { // add the album
                if(albumName.getText().toString().isEmpty()){
                    //Toast.makeText(getBaseContext(), "Enter an Album Name", Toast.LENGTH_LONG).show();

                }else {

                    String AlbumName = albumName.getText().toString();
                    MainActivity.albums.add(AlbumName);
                    //MainActivity.writeToPhotoFile(AlbumName,null,Store.context);
                    writeToAlbumFile(MainActivity.albums, Store.context);
                    writeToPhotoFile(AlbumName,null,Store.context);
                    //System.out.println(AlbumName);
                }

            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alert.show();

       /* Bundle bundle = new Bundle();
        bundle.putString(CreateAlbumDialogFragment.TITLE, "Create a New Album!");
        DialogFragment newFragment = new CreateAlbumDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "albumNameTextField");
        return;*/

    }

    //show album code
    private void showAlbum(int index){

        Bundle bundle = new Bundle();
        String album = albums.get(index);

        for(Album a:Store.objectAlbum){
            if(a.getName().equals(album)){
                Store.bum = a;
                Store.name = a.getName();
                //System.out.println(a.photos[0]);
            }
        }


        bundle.putInt(AlbumDisplay.ALBUM_INDEX, index);
        Intent intent = new Intent(this, AlbumDisplay.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ALBUM_DISPLAY_CODE);

    }


}