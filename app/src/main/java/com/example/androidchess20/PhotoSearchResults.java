package com.example.androidchess20;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhotoSearchResults extends AppCompatActivity {

    private GridView photoGrid;
    public static ArrayList<String> imageIDs = new ArrayList<>();

    //Integer[] imageIDs = new Integer[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("safsdvisdufsdnfisudf");
       // imageIDs.add("content://com.android.providers.media.documents/document/image%3A24");
        //for(Photo a:Store.seech){
        //    imageIDs.add(a.uri);
       // }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoGrid = (GridView) findViewById(R.id.grid_products);
        photoGrid.setAdapter(new ImageAdapterGridView(this));
        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

                Toast.makeText(getBaseContext(), "Grid Item " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();
            }
        });


    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context context;

        public ImageAdapterGridView(Context c) {
            context = c;
        }

        @Override
        public int getCount() {

            return imageIDs.size();//imageIDs.length;
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

            Uri uri = Uri.parse(imageIDs.get(position));
            imageView.setImageURI(uri);
            return imageView;
        }
    }
}