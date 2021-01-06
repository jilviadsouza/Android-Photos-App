package com.example.androidchess20;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PhotoDisplay extends AppCompatActivity  {

    TextView tagDisplay;
    ImageButton previousButton, nextButton;
    ImageSwitcher imageSwitcher;

    public static final int PHOTO_SEARCH_CODE = 1;

    public static int photoIndex = 0;
    //Integer[] images = {R.drawable.ru_camp, R.drawable.yu_camp, R.drawable.emoji1,
   //                     R.drawable.emoji2, R.drawable.emoji3, R.drawable.bridge

   // };

    //ArrayList<String> images;
    String tag = "location: New Brunswick, person: Anime";

    //
    public static String[] readFromAlbumFile(String dir) throws IOException {
        File directory = new File(dir+File.separator+"Albums");
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
    //
    //
    public static void loadAlbums(String dir) throws IOException {
        for(int i = 0;i<Store.objectAlbum.size();i++){
            Store.objectAlbum.get(i).photos2 = readFromPhotoFile(Store.objectAlbum.get(i).getName(),dir);
        }
    }
    //
    //
    public static /*Photo[]*/ArrayList<Photo> readFromPhotoFile(String album,String dir) throws IOException {
        File directory = new File(dir+File.separator+"Albums"+File.separator+album);
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
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = null;
        String value2 = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("context");
            value2 = extras.getString("name");
            //The key argument here must match that used in the other activity
        }
        System.out.println(value2);
        try {
            readFromAlbumFile(value);
            loadAlbums(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Album a:Store.objectAlbum){
            if(a.getName().equals(value2)){
                Store.bum = a;
                System.out.println(Store.bum.photos2.get(0).uri);
                Store.name = a.getName();
                //System.out.println(a.photos[0]);
            }
        }


        //////////////////////
        ArrayList<Photo> ug2 = null;
        try {
            ug2 = readFromPhotoFile(value2,value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Store.bum.photos2 = ug2;
        // Photo[] oto = Store.bum.photos;
        ArrayList<Photo> oto2 = Store.bum.photos2;

        //////////////////////////



        ArrayList<String> temp = new ArrayList<>();
        for(Photo a:Store.bum.photos2){
            temp.add(a.uri);
        }
        Store.imageIDs2 = temp;

        setContentView(R.layout.activity_photo_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        previousButton = (ImageButton) findViewById(R.id.previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.full_image_view);
        tagDisplay = (TextView) findViewById(R.id.tagDisplay);


        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView= new ImageView(getApplicationContext());
                Uri uri = Uri.parse(Store.imageIDs2.get(photoIndex));
                imageView.setImageURI(uri);

                return imageView;            }
        });
        imageSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        imageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);

        //we have to make the photoIndex selected from the previous display to show up here
        //as in, display the photo the user clicked on, not starting from the beginning of the album like we previously did

        updateSlideshow();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(photoIndex>0) {
                    photoIndex--;
                }
                else if(photoIndex<0) {
                    photoIndex = 0;
                }
                Uri uri = Uri.parse(Store.imageIDs2.get(photoIndex));
                imageSwitcher.setImageURI(uri);
                updateSlideshow();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(photoIndex<Store.imageIDs2.size()) {
                    photoIndex++;
                }
                if(photoIndex>=Store.imageIDs2.size()) {
                     photoIndex = Store.imageIDs2.size() - 1;
                }
                Uri uri = Uri.parse(Store.imageIDs2.get(photoIndex));
                imageSwitcher.setImageURI(uri);
                updateSlideshow();


            }
        });


    }
    //updates tags of each photoIndex, tried to make the button unclickable when its on the first or last picture but failed
    public void updateSlideshow(){

        tagDisplay.setText(Store.bum.getPhoto(photoIndex).getAllTags()); //TODO change to images[photoIndex].getAllTags()

        //this doesnt work and i don't know why!!
       /* if(photoIndex == 0 && previousButton.isClickable()){
            previousButton.setClickable(false);
        }

        if(photoIndex == images.length-1 && nextButton.isClickable()){
            nextButton.setClickable(false);
        } */
    }

    //show menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_display_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action for menu button
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_tag:
                //call add tag method
                addTag();


                return true;
            case R.id.action_move_photo:
                //call move photo method

                movePhoto();

                return true;
            case R.id.action_delete_photo:
                //call delete photo method
                deletePhoto();




                return true;
            case R.id.action_search:
                //call search photo method
                searchPhoto();

                return true;
            case R.id.action_delete_tag:
                deleteTag();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addTag(){
        //pop up dialog to add tags
        //changes should reflect in the tag display text view.
        //stays on the same page after dialog is done

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.tag_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.add_tag);
        alert.setView(inflator);

        final Spinner tagTypeSpinner = (Spinner) inflator.findViewById(R.id.tagTypeSpinner);
        final EditText et1 = (EditText) inflator.findViewById(R.id.TagValue);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PhotoDisplay.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tag_types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        tagTypeSpinner.setAdapter(adapter);



        alert.setPositiveButton(R.string.add_tag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(et1.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "Enter a Tag Value", Toast.LENGTH_LONG).show();
                }else if(tagTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
                    Toast.makeText(getBaseContext(), "Choose a Valid Tag Type", Toast.LENGTH_LONG).show();

                }else {
                    //add tag here
                    //tag type --> tagTypeSpinner.getSelectedItem().toString()
                    String TagValue = et1.getText().toString();

                    System.out.println(TagValue + "Heeeee");
                    Store.bum.getPhoto(photoIndex).addTag(
                            tagTypeSpinner.getSelectedItem().toString(),
                            TagValue);
                    AlbumDisplay.writeToPhotoFile(Store.bum.getName(), Store.bum.photos2, Store.context);
                    updateSlideshow();
                    //tag value --> TagValue (declared on line 174)
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

    public void movePhoto(){

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.move_photo_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.move_photo);
        alert.setMessage("Choose Album");
        alert.setView(inflator);

        final Spinner movePhotoSpinner = (Spinner) inflator.findViewById(R.id.movePhotoSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PhotoDisplay.this,
                android.R.layout.simple_spinner_item, MainActivity.albums);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        movePhotoSpinner.setAdapter(adapter);

        alert.setPositiveButton(R.string.move_photo, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                //move photo here
                String albumSelected = movePhotoSpinner.getSelectedItem().toString();
                try {
                    ArrayList<Photo> moving = AlbumDisplay.readFromPhotoFile(albumSelected,Store.context);
                    moving.add(Store.bum.photos2.get(photoIndex));
                    AlbumDisplay.writeToPhotoFile(albumSelected,moving,Store.context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                delete2Photo();
                //no error check here because an album will always be selected by default
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();

        }
    public void delete2Photo(){
        //delete photo
        Store.bum.photos2.remove(photoIndex);
        //images.remove(photoIndex);
        Store.imageIDs2.remove(photoIndex);
        AlbumDisplay.writeToPhotoFile(Store.bum.getName(), Store.bum.photos2, Store.context);
        AlbumDisplay.photoGrid.setAdapter(new AlbumDisplay.ImageAdapterGridView(Store.aContext));
        if(Store.imageIDs2.size() == 0){
            finish();
            Intent intent = new Intent(PhotoDisplay.this, AlbumDisplay.class);
            startActivity(intent);
        }
    }



        public void deletePhoto(){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle(R.string.delete);
            alert.setMessage("Are You Sure?");

            alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)
                {
                    //delete photo
                    Store.bum.photos2.remove(photoIndex);
                    //images.remove(photoIndex);
                    Store.imageIDs2.remove(photoIndex);
                    AlbumDisplay.writeToPhotoFile(Store.bum.getName(), Store.bum.photos2, Store.context);
                    AlbumDisplay.photoGrid.setAdapter(new AlbumDisplay.ImageAdapterGridView(Store.aContext));
                    if(Store.imageIDs2.size() == 0){
                        finish();
                        Intent intent = new Intent(PhotoDisplay.this, AlbumDisplay.class);
                        startActivity(intent);
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

        public void searchPhoto(){
        //opens a new activity window

            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, PhotoSearch.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, PHOTO_SEARCH_CODE);
        }

        public void deleteTag(){

            LayoutInflater linf = LayoutInflater.from(this);
            final View inflator = linf.inflate(R.layout.move_photo_dialog, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle(R.string.delete_tag);
            alert.setMessage("Choose Tag");
            alert.setView(inflator);

            final Spinner tagsSpinner = (Spinner) inflator.findViewById(R.id.movePhotoSpinner);

            //array of tags
            String[] tagsList = tagDisplay.getText().toString().split(",");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PhotoDisplay.this,
                    android.R.layout.simple_spinner_item, tagsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            tagsSpinner.setAdapter(adapter);

            alert.setPositiveButton(R.string.delete_tag, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //delete tag
                    String tag = tagsSpinner.getSelectedItem().toString();
                    String temp[] = tag.split("=");
                    Store.bum.getPhoto(photoIndex).removeValue(temp[0],temp[1]);
                    MainActivity.writeToPhotoFile(Store.bum.getName(),Store.bum.photos2,Store.context);
                    updateSlideshow();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            alert.show();

        }



}