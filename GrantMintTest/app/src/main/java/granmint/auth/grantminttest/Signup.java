package granmint.auth.grantminttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private static EditText emailid, password,name;
    private static Button singupbt;
    private static CheckBox show_hide_password;
    private static LinearLayout signuplayout;
    private static Animation shakeAnimation;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // getting the object of firebase authentication
        mAuth = FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.reg_progressBar);
        emailid = findViewById(R.id.signup_emailid);
        name=findViewById(R.id.fullname);
        password = findViewById(R.id.signup_password);
        singupbt = findViewById(R.id.signupBtn);
        show_hide_password = findViewById(R.id.show_hide_password);
        signuplayout = findViewById(R.id.signup_layout);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);


        // getting the object of the firebase database
        database = FirebaseDatabase.getInstance();
         myRef = database.getReference("name");



        show_hide_password.setTextColor(getResources().getColorStateList(R.color.textview_selector));
        setListeners();


    }


    private void setListeners() {
        singupbt.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password

        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change checkbox text


                                // show password
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change checkbox text

                            // hide password

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());

                        }

                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupBtn:
                checkValidation();
                break;

        }
    }

    private void checkValidation() {

        // Get emailid ,name and password
        String stringemail = emailid.getText().toString();
        String stringpassword = password.getText().toString();
        final String stringname=name.getText().toString();

        // Check patter for email id
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        // Check for both field is empty or not
        if (stringemail.equals("") || stringemail.length() == 0
                || stringpassword.equals("") || stringpassword.length() == 0 || stringname.equals("") || stringname.length()==0) {
            signuplayout.startAnimation(shakeAnimation);
            Toast.makeText(getApplicationContext(),"Enter all credentials.", Toast.LENGTH_SHORT).show();

        }
        // Check if email id is valid or not
        else if (stringemail.matches(emailPattern)) {
            progressBar.setVisibility(View.VISIBLE);
// input of the email and password and name for the startup

            mAuth.createUserWithEmailAndPassword(stringemail, stringpassword)
                    .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {

                                //signup fails due to some error
                                Toast.makeText(Signup.this, "Signup failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //signup is successfully done

                                //storing the name in database
                                myRef.setValue(stringname);

                                //seding the verification link to the user email id
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if (task.isSuccessful())

                                        {
                                            //if the verification link is sent successfully
                                            Toast.makeText(Signup.this, "Verify your email", Toast.LENGTH_LONG).show();
                                        }

                                        else {
                                            //if the verification link is not sent

                                            Toast.makeText(Signup.this, "Verification error ",Toast.LENGTH_LONG).show();
                                        }



                                    }
                                });
                                startActivity(new Intent(Signup.this, Profile.class));
                                finish();
                            }
                        }
                    });

        }
        else {
            Toast.makeText(getApplicationContext(),"Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
