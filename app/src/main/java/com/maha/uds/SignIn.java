package com.maha.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class SignIn extends AppCompatActivity {

    private EditText email_text;
    private EditText password_text;
    private Button signin_btn;
    private TextView joinUs_btn;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        setUIview();
        //setup listener to join us button
        joinUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,MotherOrBabysitter.class));
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidate()) {
                    String email = email_text.getText().toString();
                    String password = password_text.getText().toString();
                    mProgressDialog.setMessage("signing in..");
                    mProgressDialog.show();
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mProgressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        checkEmailVer();

                                    } else {
                                        Toast.makeText(SignIn.this, "Wrong email or password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    private void setUIview() {

        email_text = (EditText) findViewById(R.id.SignIn_email_text);
        password_text = (EditText) findViewById(R.id.SignIn_password_text);
        joinUs_btn = (TextView) findViewById(R.id.joinUs_btn);
        signin_btn = (Button) findViewById(R.id.signIn_btn);
    }
    //check email and password
    private boolean isValidate() {
        boolean cheack = false;
        // reading data from text field
        final String email = email_text.getText().toString();
        final String password = password_text.getText().toString();
        // if fields were empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignIn.this, "All fields are required ", Toast.LENGTH_SHORT).show();
        } else {
            cheack = true;
        }
        return cheack;
    }
    //check if the email verified or not
    public void checkEmailVer(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            if (user.isEmailVerified()){
                Usertype();
            }else{
                Toast.makeText(SignIn.this,"make sure your email is verified",Toast.LENGTH_SHORT).show();
        }
        }
    }
    //direct the user to his homePage
    public void Usertype(){
        final String email = email_text.getText().toString();
        // in case the user is a mother
        if(email.contains("@aou.edu.sa")){
            Toast.makeText(SignIn.this,"Welcome again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this,MotherHome.class));
        }else{
            Toast.makeText(SignIn.this,"Welcome again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this,BabysitterHome.class));
        }
    }




}