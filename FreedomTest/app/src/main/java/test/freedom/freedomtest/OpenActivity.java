package test.freedom.freedomtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OpenActivity extends AppCompatActivity {


    Button snap,fence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);


        snap=(Button)findViewById(R.id.bt_snapshot);
        fence=(Button)findViewById(R.id.bt_fence);

        //demo of snapshot api

        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OpenActivity.this,SnapshotApiDemo.class));

            }
        });


//demo of fence api

        fence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OpenActivity.this,FenceApiDemo.class));
            }
        });
    }
}
