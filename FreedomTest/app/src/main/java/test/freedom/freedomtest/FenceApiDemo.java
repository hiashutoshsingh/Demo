package test.freedom.freedomtest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.DetectedActivity;

public class FenceApiDemo extends AppCompatActivity {

    private static final String DURING_FENCE_KEY = "DURING_FENCE_KEY";
    private static final String STARTING_FENCE_KEY = "STARTING_FENCE_KEY";
    private static final String STOPPING_FENCE_KEY = "STOPPING_FENCE_KEY";

    public static final int STATUS_DURING = 0;
    public static final int STATUS_STARTING = 1;
    public static final int STATUS_STOPPING = 2;
    public static final int STATUS_DEFAULT = 3;


    RelativeLayout relativeLayout;
    TextView mDetectedActivityText;

    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private DetectedActivityFenceReceiver receiver;

    private MyFenceReceiver fenceReceiver;

    public static final String FENCE_RECEIVER_ACTION ="FENCE_RECEIVER_ACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fence);

         relativeLayout =(RelativeLayout)findViewById (R.id.layout_activity_fence);
         mDetectedActivityText=(TextView)findViewById (R.id.text_detected_activity) ;

         //making the object of awareness
         mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

    receiver = new DetectedActivityFenceReceiver();

        fenceReceiver = new MyFenceReceiver();

    //mulitple context
    Intent intent = new Intent(FENCE_RECEIVER_ACTION);
    mPendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
}

//registering the fence
    @Override
    protected void onStart() {
        super.onStart();
        registerFences();
        registerReceiver(receiver, new IntentFilter(FENCE_RECEIVER_ACTION));
        registerReceiver(fenceReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));
    }


    //unregistering fence
    @Override
    protected void onStop() {
        super.onStop();
        unregisterFences();
        unregisterReceiver(receiver);
        unregisterReceiver(fenceReceiver);
    }


    private void registerFences() {

        AwarenessFence headphoneFence = DetectedActivityFence.during(DetectedActivity.WALKING);
        AwarenessFence startWalking = DetectedActivityFence.starting(DetectedActivity.WALKING);
        AwarenessFence stopWalkingFence = DetectedActivityFence.stopping(DetectedActivity.WALKING);


        // cheking the status of fence
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(DURING_FENCE_KEY, headphoneFence, mPendingIntent)
                        .addFence(STARTING_FENCE_KEY, startWalking, mPendingIntent)
                        .addFence(STOPPING_FENCE_KEY, stopWalkingFence, mPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {

                            Snackbar.make(relativeLayout, "Fence Registered", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(relativeLayout,
                                    "Fence Not Registered",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void unregisterFences() {

        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(DURING_FENCE_KEY)
                        .removeFence(STARTING_FENCE_KEY)
                        .removeFence(STOPPING_FENCE_KEY)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Snackbar.make(relativeLayout,
                        "Fence Removed",
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Snackbar.make(relativeLayout,
                        "Fence Not Removed",
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }


    // status of user is determined and according to that headhpone status is given

    private void setHeadphoneState(int headphoneState) {
        switch (headphoneState) {
            case STATUS_DEFAULT:
                mDetectedActivityText.setText(R.string.text_still);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDetectedActivityText.setText(R.string.text_start_walking);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDetectedActivityText.setText(R.string.text_walking);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDetectedActivityText.setText(R.string.text_stopping);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDetectedActivityText.setText(R.string.text_still);

                                            }
                                        }, 2500);
                                    }
                                }, 4000);
                            }
                        }, 2000);
                    }
                }, 5000);

                break;
            case STATUS_DURING:
                mDetectedActivityText.setText(R.string.text_walking);
                break;
            case STATUS_STARTING:
                mDetectedActivityText.setText(R.string.text_start_walking);
                break;
            case STATUS_STOPPING:
                mDetectedActivityText.setText(R.string.text_stopping);
                break;
        }
    }


    // callback is made when the user activity is detected

class DetectedActivityFenceReceiver extends BroadcastReceiver {


    // receiving the status of the user and then chekcing the headphone status
    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);

        if (TextUtils.equals(fenceState.getFenceKey(), DURING_FENCE_KEY)) {
            switch(fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    setHeadphoneState(STATUS_DURING);
                    break;
                case FenceState.FALSE:
                    setHeadphoneState(STATUS_DEFAULT);
                    break;
                case FenceState.UNKNOWN:
                    
                    Toast.makeText(context,"Oops, your headphone status is unknown!",Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (TextUtils.equals(fenceState.getFenceKey(), STARTING_FENCE_KEY)) {
            switch(fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    setHeadphoneState(STATUS_STARTING);
                    break;
                case FenceState.FALSE:
                    break;
                case FenceState.UNKNOWN:
                    Toast.makeText(context,"Oops, your headphone status is unknown!",Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (TextUtils.equals(fenceState.getFenceKey(), STOPPING_FENCE_KEY)) {
            switch(fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    setHeadphoneState(STATUS_STOPPING);
                    break;
                case FenceState.FALSE:
                    break;
                case FenceState.UNKNOWN:
                    Toast.makeText(context,"Oops, your headphone status is unknown!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

    public class MyFenceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView head=(TextView)findViewById(R.id.fence_headphone_status);
            FenceState fenceState = FenceState.extract(intent);

            if (TextUtils.equals(fenceState.getFenceKey(), DURING_FENCE_KEY)) {
                switch(fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        head.setText("Headphones are plugged in.");
                        break;
                    case FenceState.FALSE:
                        head.setText("Headphones are NOT plugged in.");
                        break;
                    case FenceState.UNKNOWN:
                        head.setText("The headphone fence is in an unknown state.");
                        break;
                }
            }
        }
    }


}
