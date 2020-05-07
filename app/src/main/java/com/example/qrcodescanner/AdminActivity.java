package com.example.qrcodescanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private QRcodeDatabaseHelper helper;
    private static Uri fileURI;
    Button filech;
    Button imp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        helper = new QRcodeDatabaseHelper(this);

        filech=(Button)findViewById(R.id.file_chooser_btn);

        imp=(Button)findViewById(R.id.import_btn);

        filech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importRecords(v);
            }
        });
    }


    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            fileURI = null;
            if (resultData != null) {
                fileURI = resultData.getData();
                Log.d("FILECHOSEN", "Uri: " + fileURI.getPath());

                //show filename
                TextView filename = (TextView) findViewById(R.id.filename_view);
                String[] segments = fileURI.getPath().split("/");
                filename.setText(segments[segments.length -1]);
                //enable import button
                Button importButton = (Button) findViewById(R.id.import_btn);
                importButton.setEnabled(true);
            }
        }
    }

   /* public void chooseFile(View view) {
        performFileSearch();
    }
    */

    public void importRecords(View view) {
        TextView filename = (TextView) findViewById(R.id.filename_view);
        ProgressDialog pd = new ProgressDialog(this);

        if(filename.getText() != null){
            try{
                pd.setMessage("Importing");
                pd.show();
                helper.populateDB(fileURI, this);
                Toast.makeText(this, "Added records", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(this, "Sorry, Couldn't read file.", Toast.LENGTH_LONG).show();
            }
        }
    }
}