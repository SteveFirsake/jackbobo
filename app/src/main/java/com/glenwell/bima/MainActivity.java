package com.glenwell.bima;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements

        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PermissionCallback,
        ErrorCallback {

    TextSwitcher textSwitcher;
    Animation slide_in_left, slide_out_right;
    Button btnlogin;
    static double longitude;
    View View;
    static double latitude;
    Button btnfinda;
    public static final int confail = 9000;
    Button btnpackg;
    Button btnconta;
    GPSTracker gps;
    Button btnnewsf;
    LocationRequest mlr;
    GoogleApiClient mgac;
    Location cl;
    private static final int REQUEST_PERMISSIONS = 20;
    private final static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private final static int REQUEST_LOCATION = 2;
    TextView title;
    String say = "0.0";
    String sax = "0.0";
    Button btnwebba;

    protected void createlocreq(){
        mlr = new LocationRequest();
        mlr.setInterval(2000);
        mlr.setFastestInterval(1000);
        mlr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    String[] TextToSwitched = { "Easy Mobile Banking", "Simply the best", "Talk to us", "Rewards and Waivers",
            "We care", "Friendly packages", "Committed to you" };

    int curIndex;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0,0);
        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        btnlogin = (Button) findViewById(R.id.loga);
        btnfinda = (Button) findViewById(R.id.finda);
        btnpackg = (Button) findViewById(R.id.packega);
        btnconta = (Button) findViewById(R.id.contacta);
        btnnewsf = (Button) findViewById(R.id.newsa);
        btnwebba = (Button) findViewById(R.id.weba);
        title = (TextView)findViewById(R.id.title);

        gps = new GPSTracker(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        title.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnlogin.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnfinda.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnpackg.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnconta.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnnewsf.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        //btnwebba.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);



        final Context context = this;
        slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        textSwitcher.setInAnimation(slide_in_left);
        textSwitcher.setOutAnimation(slide_out_right);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory(){

            @Override
            public View makeView() {
                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(20);
                textView.setTextColor(Color.rgb(255,204,51));
                //textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);
                // textView.setShadowLayer(10, 10, 10, Color.rgb(255,204,51));
                return textView;
            }});

        curIndex = 0;
        textSwitcher.setText(TextToSwitched[curIndex]);

        createlocreq();


        Timer timer = new Timer("desired_name");
        timer.schedule(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {

                        if(curIndex == TextToSwitched.length-1){
                            curIndex = 0;
                            textSwitcher.setText(TextToSwitched[curIndex]);
                        }else{
                            textSwitcher.setText(TextToSwitched[++curIndex]);
                        }

                        if (latitude!=0.0 && longitude!=0.0 ){
                            updateUI();
                            gps.getLocation();
                        }else{
                            updateUI();
                            gps.getLocation();
                        }


                    }



                });



            }



        }, 0, 3000);

        if (!isgpsa(this)) {
            Toast.makeText(this, "Google Play Services is out-of-date/disabled on your phone", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                ) {
            reqPermission();
        }



        mgac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


btnnewsf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        startActivity(intent);

    }
});



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(MainActivity.this)
                .setErrorCallback(MainActivity.this)
                .request(REQUEST_PERMISSIONS);
    }

    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied. Some app functionality will not work.", Toast.LENGTH_LONG).show();
    }

    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions accepted for this app to work.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private boolean isgpsa(Activity activity) {
        // TODO Auto-generated method stub
        /*int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // if (ConnectionResult.SUCCESS == status) {
            // return true;
        // } else {
            // GooglePlayServicesUtil.getErrorDialog(status, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            // return false;
        // }*/


        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;



    }


    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        startLocupdates();

    }

    @SuppressWarnings({"MissingPermission"})
    private void startLocupdates() {
        // TODO Auto-generated method stub

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            PendingResult<Status> pr = LocationServices.FusedLocationApi.requestLocationUpdates(mgac, mlr, this);

            if (latitude != 0.0 && longitude != 0.0) {
                //sitato.setText("Locator Status : Detected");

            } else {
                //sitato.setText("Locator Status : Scanning");
            }

        }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mgac.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        if (arg0.hasResolution()) {

            try {
                arg0.startResolutionForResult(this, confail);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }

        } else {
            diambaidno(View);
        }
    }


    public void onDisconnected() {
        // TODO Auto-generated method stub
        updateUI();
    }

    public void diambaidno(View v) {
        final Dialog mbott = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        mbott.setContentView(R.layout.mbaind_nonet3);
        mbott.setCanceledOnTouchOutside(false);
        mbott.setCancelable(false);
        WindowManager.LayoutParams lp = mbott.getWindow().getAttributes();
        lp.dimAmount = 0.85f;
        mbott.getWindow().setAttributes(lp);
        mbott.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mbott.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button mbaok = (Button) mbott.findViewById(R.id.mbabtn1);
        mbaok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbott.dismiss();
            }
        });
        mbott.show();
    }



    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        cl = arg0;
        updateUI();
    }


    private void updateUI() {
        // TODO Auto-generated method stub


        if (null != cl) {
            latitude = cl.getLatitude();
            longitude = cl.getLongitude();

            sax = String.valueOf(longitude);
            say = String.valueOf(latitude);
        }
    }

    public void onResume() {
        super.onResume();

        if (mgac.isConnected()) {
            startLocupdates();
        }

        //new HttpAsyncTask0().execute(new String[]{URL2});
    }

    public void onStart() {
        super.onStart();
        mgac.connect();
    }

    public void onPause() {
        super.onPause();
        try {

            if (mgac.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mgac, this);
                mgac.disconnect();
            }
        } catch (Exception x) {

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
