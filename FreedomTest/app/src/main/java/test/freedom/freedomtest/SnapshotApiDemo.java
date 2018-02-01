package test.freedom.freedomtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.List;

public class SnapshotApiDemo extends AppCompatActivity {

    GoogleApiClient client;
    private final int MY_PERMISSION_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);

        //creating instance of the awareness

        client = new GoogleApiClient.Builder(SnapshotApiDemo.this)
                .addApi(Awareness.API)
                .build();
        client.connect();


        callingSnapshots();



    }

    private void callingSnapshots() {


        getuserActivity();

        getHeadphoneStatus();

//checking the permission for location
        if (ContextCompat.checkSelfPermission(SnapshotApiDemo.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SnapshotApiDemo.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);

        } else {

            getLocation();
        }


        //cheking the permsission for weather

        if (ContextCompat.checkSelfPermission(SnapshotApiDemo.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    SnapshotApiDemo.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            getWeather();
        }

//cheking the permission for place location
        if (ContextCompat.checkSelfPermission(SnapshotApiDemo.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    SnapshotApiDemo.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            getPlace();
        }

    }

    private void getPlace() {
        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Awareness.SnapshotApi.getPlaces(client)
                .setResultCallback(new ResultCallback<PlacesResult>() {
                    @Override
                    public void onResult(@NonNull final PlacesResult placesResult) {
                        if (!placesResult.getStatus().isSuccess()) {
                            Toast.makeText(SnapshotApiDemo.this, "Could not get places.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.current_place_container);
                        linearLayout.removeAllViews();
                        if (placeLikelihoodList != null) {
                            for (int i = 0; i < 5 && i < placeLikelihoodList.size(); i++) {
                                PlaceLikelihood p = placeLikelihoodList.get(i);

                                View v = LayoutInflater.from(SnapshotApiDemo.this).inflate(R.layout.abc_possible_location, linearLayout, false);
                                ((TextView) v.findViewById(R.id.location_name)).setText(p.getPlace().getName());
                                ((TextView) v.findViewById(R.id.location_address)).setText(p.getPlace().getAddress());
                                linearLayout.addView(v);
                            }
                        } else {
                            Toast.makeText(SnapshotApiDemo.this, "Could not get nearby places.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getWeather() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Awareness.SnapshotApi.getWeather(client)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (!weatherResult.getStatus().isSuccess()) {
                            Log.d("ashu", "Could not get weather.");
                            return;
                        }
                        TextView we = (TextView) findViewById(R.id.weather);
                        Weather weather = weatherResult.getWeather();

                        String temp = "Temperature: " + weather.getTemperature(Weather.CELSIUS)
                                + "\nHumidity: " + weather.getHumidity();
                        we.setText(temp);

                    }
                });

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Awareness.SnapshotApi.getLocation(client)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (!locationResult.getStatus().isSuccess()) {
                            Log.e("ashu", "Could not get location.");
                            return;
                        }

                        TextView lati = (TextView) findViewById(R.id.lati);

                        Location location = locationResult.getLocation();

                        String temp = "Latitude: " + Double.toString(location.getLatitude())
                                + "\nLongitude: " + Double.toString(location.getLongitude());

                        lati.setText(temp);


                    }
                });

    }

    private void getHeadphoneStatus() {

        Awareness.SnapshotApi.getHeadphoneState(client)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        if (!headphoneStateResult.getStatus().isSuccess()) {
                            Log.d("ashu", "Could not get headphone status");
                            return;
                        }
                        HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();

                        TextView head = (TextView) findViewById(R.id.headphone_status);

                        if (headphoneState.getState() == HeadphoneState.PLUGGED_IN) {
                            head.setText("Headphones are plugged in.");

                        } else {
                            head.setText("Headphones are NOT plugged in.");

                        }
                    }
                });

    }

    private void getuserActivity() {

        Awareness.SnapshotApi.getDetectedActivity(client)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {

                            Log.d("ashu", "activity status negative");
                            return;
                        }
                        ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity detectedActivity = ar.getMostProbableActivity();

                        TextView activityName = (TextView) findViewById(R.id.user_activity);
                        switch (detectedActivity.getType()) {
                            case DetectedActivity.IN_VEHICLE:
                                activityName.setText("In vehicle");
                                break;
                            case DetectedActivity.ON_BICYCLE:
                                activityName.setText("On bicycle");
                                break;
                            case DetectedActivity.ON_FOOT:
                                activityName.setText("On foot");
                                break;
                            case DetectedActivity.RUNNING:
                                activityName.setText("Running");
                                break;
                            case DetectedActivity.STILL:
                                activityName.setText("Still");
                                break;
                            case DetectedActivity.TILTING:
                                activityName.setText("Tilting");
                                break;
                            case DetectedActivity.UNKNOWN:
                                activityName.setText("Unknown");
                                break;
                            case DetectedActivity.WALKING:
                                activityName.setText("Walking");
                                break;

                        }

                    }
                });

    }

}

