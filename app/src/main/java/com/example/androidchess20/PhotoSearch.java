package com.example.androidchess20;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoSearch extends AppCompatActivity {

    Spinner TagTypeSpinner,TagTypeSpinner2;
    EditText TagValue,TagValue2;
    Button searchButton;
    Spinner junctionSpinner;

    public static final int SEARCH_RESULTS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_photo_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TagTypeSpinner = (Spinner) findViewById(R.id.tagTypeSpinnerSearch);
        TagValue = (EditText) findViewById(R.id.TagValueSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        junctionSpinner = (Spinner) findViewById((R.id.junctionSpinner));
        TagTypeSpinner2 = (Spinner) findViewById(R.id.tagTypeSpinnerSearch2);
        TagValue2 = (EditText) findViewById(R.id.TagValueSearch2);
        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tag_types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        TagTypeSpinner.setAdapter(adapter);
        TagTypeSpinner2.setAdapter(adapter);

        ArrayAdapter<String> junctionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.junction_types));
        junctionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        junctionSpinner.setAdapter(junctionAdapter);

    }
    //this method retrieves the tag type and value to be searched for and opens a new window with its results
    public void searchButtonFunction(View view){
        /*/error checks
        if(TagValue.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Enter a Tag Value", Toast.LENGTH_LONG).show();
            //finish();
        }else if(TagTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
            Toast.makeText(getBaseContext(), "Choose a Valid Tag Type", Toast.LENGTH_LONG).show();
            //finish();

        }if(junctionSpinner.getSelectedItem().toString().equalsIgnoreCase(("Choose a Junction Type…"))) {
            String type = TagTypeSpinner.getSelectedItem().toString();
            String value = TagValue.getText().toString();
            //Store.seech = Store.bum.Search(type+"="+value);

           // Photo one = new Photo("content://com.android.providers.media.documents/document/image%3A27");
            //ArrayList<Photo> temo = new ArrayList<>();
            PhotoSearchResults.imageIDs = Store.bum.Search(type+"="+value);
            //opens next window
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, PhotoSearchResults.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, SEARCH_RESULTS_CODE);

        }if(TagValue2.getText().toString().isEmpty()){
            //second tag is empty
        }
         if(TagTypeSpinner2.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
             //second spinner empty
        }else {
            String type = TagTypeSpinner.getSelectedItem().toString();
            String value = TagValue.getText().toString();
            String type2 = TagTypeSpinner2.getSelectedItem().toString();
            String value2 = TagValue2.getText().toString();
            String junc = junctionSpinner.getSelectedItem().toString();

            PhotoSearchResults.imageIDs = Store.bum.Search(type+"="+value,type2+"="+value2,junc);

            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, PhotoSearchResults.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, SEARCH_RESULTS_CODE);
        }
        /this means the user has not selected a junction
        */
        if(junctionSpinner.getSelectedItem().toString().equalsIgnoreCase(("Choose a Junction Type…"))){
            if(!TagValue.getText().toString().isEmpty() && !TagTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
                String type = TagTypeSpinner.getSelectedItem().toString();
                String value = TagValue.getText().toString();
                //Store.seech = Store.bum.Search(type+"="+value);

                // Photo one = new Photo("content://com.android.providers.media.documents/document/image%3A27");
                //ArrayList<Photo> temo = new ArrayList<>();
                PhotoSearchResults.imageIDs = Store.bum.Search(type+"="+value);
                //opens next window
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, PhotoSearchResults.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, SEARCH_RESULTS_CODE);
            }
        }
        else{
            if(!TagValue.getText().toString().isEmpty() && !TagTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
                if(!TagValue2.getText().toString().isEmpty() && !TagTypeSpinner2.getSelectedItem().toString().equalsIgnoreCase("Choose a Tag Type…")){
                    String type = TagTypeSpinner.getSelectedItem().toString();
                    String value = TagValue.getText().toString();
                    String type2 = TagTypeSpinner2.getSelectedItem().toString();
                    String value2 = TagValue2.getText().toString();
                    String junc = junctionSpinner.getSelectedItem().toString();

                    PhotoSearchResults.imageIDs = Store.bum.Search(type+"="+value,type2+"="+value2,junc);

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, PhotoSearchResults.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, SEARCH_RESULTS_CODE);
                }
            }

        }

        //tag type --> TagTypeSpinner.getSelectedItem().toString()
        //tag value --> TagValue.getText().toString()
        //junction value --> junctionSpinner.getSelectedItem().toString();



    }
}