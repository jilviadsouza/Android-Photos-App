package com.example.androidchess20;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import android.view.View;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class CreateAlbumDialogFragment extends DialogFragment {

    public static final String TITLE = "title";

    EditText albumName;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater linf = LayoutInflater.from(getActivity());
        final View inflator = linf.inflate(R.layout.create_album_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(inflator);
        albumName = (EditText) inflator.findViewById(R.id.albumName);

        Bundle bundle = getArguments();
        builder.setTitle(bundle.getString(TITLE));

                // Add action buttons
                builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // add the album
                        //AlbumName   = (EditText)findViewById(R.id.albumName);

                        if(albumName.getText().toString().isEmpty()){

                        }

                        String AlbumName = albumName.getText().toString();
                        MainActivity.albums.add(AlbumName);
                        //MainActivity.writeToPhotoFile(AlbumName,null,Store.context);
                        MainActivity.writeToAlbumFile(MainActivity.albums,Store.context);
                        File directory = new File(Store.context.getFilesDir()+File.separator+
                                "Albums"+File.separator+AlbumName);
                        //for first time use
                        if(!directory.exists())
                            directory.mkdir();
                        System.out.println(Store.context.getFilesDir()+File.separator+
                                "Albums"+File.separator+AlbumName);

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
