package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;

public class MainActivity extends AppCompatActivity {
    Button btnScan;
    Button instruction;
    TextView txtLogin;
    private final String LOGTAG="Scan QrCode";
    ProgressDialog progressDialog;
    QRcodeDatabaseHelper qrcodedatabaseHelper;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    QRcodeDatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new QRcodeDatabaseHelper(this);
        helper.getAll();
        btnScan=(Button)findViewById(R.id.ButtonScan);
        txtLogin=(TextView)findViewById(R.id.TextViewLogin);
        instruction=(Button)findViewById(R.id.ButtonInstruction);

        progressDialog= new ProgressDialog(this);
        qrcodedatabaseHelper = new QRcodeDatabaseHelper(MainActivity.this);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                        }
                                    }
        );

        instruction.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(MainActivity.this,AboutUs.class));
                                        }
                                    }
        );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            final String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(LOGTAG,"Have scan result in your app activity :"+ result);
            boolean isExist = qrcodedatabaseHelper.checkQRcodeExist(result);
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            if(isExist){
                Log.d(LOGTAG,"inside if exist");
                alertDialog.setTitle("Valid Ticket");
                alertDialog.setMessage("Consume Ticket?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                qrcodedatabaseHelper.consumeQRcode(result);
                                Toast.makeText(MainActivity.this, "Ticket Consumed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                alertDialog.setTitle("Invalid Ticket");
                alertDialog.setMessage("Invalid or Already Used");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
