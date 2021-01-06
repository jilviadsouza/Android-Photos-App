package com.example.androidchess20;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;



public class AlbumDisplay extends AppCompatActivity {


    public static final String ALBUM_INDEX = "albumIndex";
    private static final int SELECT_PICTURE = 1;
    public static final String DATA = "_data";
    public static final int MAIN_ACTIVITY = 3;
    public static final int PHOTO_DISPLAY_CODE = 1;


    private String selectedImagePath;
    private String filemanagerstring;

    private int albumIndex;
    public static GridView photoGrid;
    //ArrayList<Photo> images;

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
        for(Photo a:photos){
            build += a.uri+"\t"+a.getAllTags()+"\n";
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
    final int request_code = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Store.imageIDs = new String[1000];
        Store.aContext = this;
        Store.imageIDs2 = new ArrayList<>();
       // Store.count = 0;
        //System.out.println(Store.bum.photos[0].uri);
       // Photo[] ug = null;
        ArrayList<Photo> ug2 = null;
        try {
            ug2 = readFromPhotoFile(Store.bum.getName(), Store.context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Store.bum.photos2 = ug2;
       // Photo[] oto = Store.bum.photos;
        ArrayList<Photo> oto2 = Store.bum.photos2;

       // while(oto[Store.count] != null){
       ////     Store.imageIDs[Store.count] = oto[Store.count].uri;
      //      Store.count++;
      //  }
        for(Photo a:oto2){
            Store.imageIDs2.add(a.uri);
        }




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photoGrid = (GridView) findViewById(R.id.grid_products);
        photoGrid.setAdapter(new ImageAdapterGridView(this));
        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

                PhotoDisplay.photoIndex = position;
                showPhoto();
                Toast.makeText(getBaseContext(), "Grid Item " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showPhoto(){
        Bundle bundle = new Bundle();
        String value = Store.context.getFilesDir()+"";
        String value2 = Store.bum.getName();
        Intent intent = new Intent(this, PhotoDisplay.class);
        intent.putExtra("context",value);
        intent.putExtra("name",value2);
        intent.putExtras(bundle);
        startActivityForResult(intent, PHOTO_DISPLAY_CODE);
    }

    //show menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_display_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action for menu button
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_photo:
                //call add photo method
                addPhoto();

                return true;
            case R.id.action_rename_album:
                //call rename album method
               renameAlbum();
                return true;
            case R.id.action_delete_album:
                //call delete album method
                deleteAlbum();
                //Store.delete();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void renameAlbum(){

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.create_album_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.rename_album);
        alert.setView(inflator);

        final EditText et1 = (EditText) inflator.findViewById(R.id.albumName);


        alert.setPositiveButton(R.string.rename_album, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                String AlbumName = et1.getText().toString();
                Store.rename(AlbumName);
                MainActivity.albumListView.setAdapter(new ArrayAdapter<String>(Store.context, R.layout.album, MainActivity.albums));

                finish();
                //Intent intent = new Intent(AlbumDisplay.this, MainActivity.class);
                //startActivity(intent);
                if (et1.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Enter an Album Name", Toast.LENGTH_LONG).show();

                } else {
                    AlbumName = et1.getText().toString();
                //Store.bum.setName(AlbumName);
                System.out.println(AlbumName);
            }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }

    public void deleteAlbum(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.delete_album);
        alert.setMessage("Are You Sure?");

        alert.setPositiveButton(R.string.delete_album, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                //delete album
                try {
                    Store.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.albumListView.setAdapter(new ArrayAdapter<String>(Store.context, R.layout.album, MainActivity.albums));
                finish();
               // Intent intent = new Intent(AlbumDisplay.this, MainActivity.class);
               // startActivity(intent);
                finish();
                Intent intent = new Intent(AlbumDisplay.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }

    public void addPhoto(){

        //from
        Intent intent;

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, request_code);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code &&resultCode == RESULT_OK) {
           // if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
                //Store.imageIDs[0] = selectedImageUri.toString();
                selectedImagePath = getPath(selectedImageUri);
               // System.out.println(Store.imageIDs[0]);
               // Store.imageIDs[Store.count] = selectedImageUri.toString();
                Store.imageIDs2.add(selectedImageUri.toString());
                Store.bum.addPhoto(new Photo(selectedImageUri.toString()));

                writeToPhotoFile(Store.bum.getName(),Store.bum.photos2,Store.context);
                Store.count++;
                photoGrid.setAdapter(new ImageAdapterGridView(this));
                //finish();
               // Intent intent;
                //intent = new Intent(AlbumDisplay.this,AlbumDisplay.class);
                //startActivity(intent);
                if (selectedImagePath != null) {
                    // IF LOCAL IMAGE, NO MATTER IF ITS DIRECTLY FROM GALLERY (EXCEPT PICASSA ALBUM),
                    // OR OI/ASTRO FILE MANAGER. EVEN DROPBOX IS SUPPORTED BY THIS BECAUSE DROPBOX DOWNLOAD THE IMAGE
                    // IN THIS FORM - file:///storage/emulated/0/Android/data/com.dropbox.android/...
                    System.out.println("local image");
                } else {
                    System.out.println("picasa image!");
                    loadPicasaImageFromGallery(selectedImageUri);
               //}
            }
        }
    }


    // NEW METHOD FOR PICASA IMAGE LOAD
    private void loadPicasaImageFromGallery(final Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columnIndex != -1) {
                new Thread(new Runnable() {
                    // NEW THREAD BECAUSE NETWORK REQUEST WILL BE MADE THAT WILL BE A LONG PROCESS & BLOCK UI
                    // IF CALLED IN UI THREAD
                    public void run() {
                        try {
                            Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            // THIS IS THE BITMAP IMAGE WE ARE LOOKING FOR.
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        cursor.close();
    }


    public String getPath(Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        else
            return uri.getPath();               // FOR OI/ASTRO/Dropbox etc
    }



    public static class ImageAdapterGridView extends BaseAdapter{
        private Context context;

        public ImageAdapterGridView(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            //int count = 0;
           // for(int i =0;i<Store.imageIDs.length;i++){
            //    if(Store.imageIDs[i] != null){
           //         count++;
            //    }
           // }
            //return count;//imageIDs.length;
            return Store.imageIDs2.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           /* ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(context);
                mImageView.setLayoutParams(new GridView.LayoutParams(150, 150));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView; */
            ImageView imageView;
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.album_grid_item, parent, false);
                imageView = (ImageView) convertView.findViewById(R.id.image);
            }else {
                imageView = (ImageView) convertView;
            }

           //Uri uri = Uri.parse(Store.imageIDs[position]);
            Uri uri = Uri.parse(Store.imageIDs2.get(position));
           // System.out.println(imageIDs[position] + "Thhiss");
           imageView.setImageURI(uri);
           // imageView.setImageResource(imageIDs[position]);
            return imageView;
        }
    }
}