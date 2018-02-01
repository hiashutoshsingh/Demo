package granmint.auth.grantminttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView name;
    private Button logout;
    private FirebaseAuth auth;
    private String userStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting the object of the firebase authentication
        auth = FirebaseAuth.getInstance();
        //getting the objects of the firebase database
        database = FirebaseDatabase.getInstance();
        //retreiving the value which is stored is the string "name"
        myRef = database.getReference("name");

        name=findViewById(R.id.id_name);
        logout=findViewById(R.id.id_logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//logging out
                auth.signOut();

               startActivity(new Intent(Profile.this, Login.class));
            }
        });


//cheking the user of status whether they have verified the email or not

            userStatus= String.valueOf(auth.getCurrentUser().isEmailVerified());


            //user has not verified the email
            Toast.makeText(Profile.this,"Verify your email ", Toast.LENGTH_SHORT).show();
            name.setText("Verify your email");


// the user email status is reload after it's verifying
            auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


// if the status is true
                    if (userStatus =="true")

                    {
//if they have verified the email
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // retreving the value of current user

                                String value = dataSnapshot.getValue(String.class);
                                name.setText("Hello my name is: "+value);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.d("ashu", "Failed to read value.", error.toException());
                            }
                        });

                    }

                    else {

                        name.setText("Verify your mail");
                    }

                }
            });

        }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
