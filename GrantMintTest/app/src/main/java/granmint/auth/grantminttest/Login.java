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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private  EditText emailid, password;
    private  Button loginButton;
    private  CheckBox show_hide_password;
    private  TextView signUp;
    private  LinearLayout loginLayout;
    private  Animation shakeAnimation;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar =(ProgressBar)findViewById(R.id.log_progressBar);
        emailid = findViewById(R.id.login_emailid);
        password = findViewById(R.id.login_password);
        signUp = findViewById(R.id.createAccount);
        loginButton = findViewById(R.id.loginBtn);
        show_hide_password = findViewById(R.id.show_hide_password);
        loginLayout = findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);

        // getting the object of firebase authentication

        auth = FirebaseAuth.getInstance();

        // checking the user is previously logged in or not
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Profile.class));
            finish();
        }


        show_hide_password.setTextColor(getResources().getColorStateList(R.color.textview_selector));
        signUp.setTextColor(getResources().getColorStateList(R.color.textview_selector));

        setListeners();


    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change checkbox text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change checkbox text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.createAccount:
                Intent create=new Intent(Login.this,Signup.class);
                startActivity(create);
                break;
        }
    }

    private void checkValidation() {
        // Get email id and password
        String stringemail = emailid.getText().toString();
        String stringpassword = password.getText().toString();

        // Check patter for email id
        /*Pattern p = Pattern.compile(Utils.regEx);*/
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        /*Matcher m = p.matcher(getEmailId);*/

        // Check for both field is empty or not
        if (stringemail.equals("") || stringemail.length() == 0
                || stringpassword.equals("") || stringpassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(getApplicationContext(),"Enter both credentials.", Toast.LENGTH_SHORT).show();

        }
        // Check if email id is valid or not
        else if (stringemail.matches(emailPattern)) {
            //Toast.makeText(getActivity(), "Do Login", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);

            //authenticate user
            auth.signInWithEmailAndPassword(stringemail, stringpassword)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    password.setError("Password too short");
                                } else {
                                    Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_LONG).show();
                                }
                            } else {


                                    Intent intent = new Intent(Login.this, Profile.class);
                                    startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
