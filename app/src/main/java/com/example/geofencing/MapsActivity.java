package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.example.geofencing.Model.Prevalent;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng> areaAtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);

                        initArea();

                        settingGeoFire();
                        


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        Toast.makeText(MapsActivity.this, "Please enable location permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


    }

    private void initArea() {

        String getLoc = getIntent().getStringExtra("location");
        String getLoc2 = getIntent().getStringExtra("location2");

        double arrLatitude = Double.parseDouble(getLoc);
        double arrLongitude = Double.parseDouble(getLoc2);
        String getName = getIntent().getStringExtra("className");

        areaAtt = new ArrayList<>();

        areaAtt.add(new LatLng(arrLatitude, arrLongitude));



//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyy");
//        String tarikh = sdf.format(calendar.getTime());
//
//        Calendar time = Calendar.getInstance();
//        SimpleDateFormat stf2 = new SimpleDateFormat("HHmm");
//        String currentTime = stf2.format(calendar.getTime());
//
//        final DatabaseReference finalref;
//        finalref = FirebaseDatabase.getInstance().getReference();
//
//        String currentMatric = Prevalent.currentOnlineUser.getMatric();
//
//        String dbChild = getName + " " + tarikh + " " + currentTime;
//
//        finalref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                if (!(snapshot).child("History").child(currentMatric).child(dbChild).exists())
//                {
//
//                    HashMap<String, Object> dbDataMap = new HashMap<>();
//                    dbDataMap.put("className", getName);
//                    dbDataMap.put("date", tarikh);
//                    dbDataMap.put("time", currentTime);
//
//                    finalref.child("History").child(currentMatric).child(dbChild).updateChildren(dbDataMap)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    Toast.makeText(MapsActivity.this, "Your class history have been recorded", Toast.LENGTH_SHORT).show();
//
//                                }
//                            });
//
//
//                }
//
//                else
//                {
//                    Toast.makeText(MapsActivity.this, "Error uploading history", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //areaAtt.add(new LatLng(37.4333, -122.0693));


//        FirebaseDatabase.getInstance().getReference("AttendanceLocation").push().setValue(areaAtt).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                Toast.makeText(MapsActivity.this, "Location updated", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MapsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void settingGeoFire() {

        myLocationRef = FirebaseDatabase.getInstance().getReference("AttendanceLocation");
        geoFire = new GeoFire(myLocationRef);

    }

    private void buildLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (mMap != null) {
                    if (currentUser != null) currentUser.remove();
                    currentUser = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()))
                            .title("You"));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser.getPosition(), 12.0f));

                    geoFire.setLocation("You", new GeoLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()))
                                    .title("You"));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser.getPosition(), 12.0f));

                        }
                    });
                }
            }
        };

    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (fusedLocationProviderClient != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            for (LatLng latLng : areaAtt)
            {
                mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(10)
                        .strokeColor(Color.RED)
                        .fillColor(0x220000FF)
                        .strokeWidth(5.0f)
                );

                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 0.05f);
                geoQuery.addGeoQueryEventListener(MapsActivity.this);
            }


    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        sendNotification("Attendance App", String.format("You are now attend class A", key));

        String getName = getIntent().getStringExtra("className");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyy");
        String tarikh = sdf.format(calendar.getTime());

        Calendar time = Calendar.getInstance();
        SimpleDateFormat stf2 = new SimpleDateFormat("HHmm");
        String currentTime = stf2.format(time.getTime());

        //db only

        Calendar dbtime = Calendar.getInstance();
        SimpleDateFormat sdf2DB = new SimpleDateFormat("dd MMMM yyyy");
        String currentDatedb = sdf2DB.format(dbtime.getTime());

        Calendar dbdate = Calendar.getInstance();
        SimpleDateFormat sdf3DB = new SimpleDateFormat("HH:mm");
        String currentTimedb = sdf3DB.format(dbdate.getTime());

        final DatabaseReference finalref;
        finalref = FirebaseDatabase.getInstance().getReference();

        String currentMatric = Prevalent.currentOnlineUser.getMatric();

        String dbChild = getName + " " + tarikh + " " + currentTime;

        finalref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!(snapshot).child("History").child(currentMatric).child(dbChild).exists())
                {

                    HashMap<String, Object> dbDataMap = new HashMap<>();
                    dbDataMap.put("className", getName);
                    dbDataMap.put("date", currentDatedb);
                    dbDataMap.put("time", currentTimedb);

                    finalref.child("History").child(currentMatric).child(dbChild).updateChildren(dbDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(MapsActivity.this, "Your class history have been recorded", Toast.LENGTH_SHORT).show();

                                }
                            });


                }

                else
                {
                    Toast.makeText(MapsActivity.this, "Error uploading history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @Override
    public void onKeyExited(String key) {

        sendNotification("Attendance App", String.format("You're now leaving class A", key));

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

        Toast.makeText(MapsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void sendNotification(String title, String content) {

        String NOTIFICATION_CHANNEL_ID = "Attendance geofence location";
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            //cuba try test config notification

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[] {0,1000,500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));


        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);
    }
}