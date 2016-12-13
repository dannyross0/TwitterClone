package com.example.sankum.ictwt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonlogin;
    private Button buttonRegister;
    private EditText editTextlogin;
    private EditText editTextpassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonlogin = (Button) findViewById(R.id.buttonlogin);
        buttonRegister = (Button)findViewById(R.id.buttonregister);
        editTextlogin = (EditText) findViewById(R.id.editTextlogin);
        editTextpassword = (EditText) findViewById(R.id.editTextpassword);

        buttonlogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "User Logged In: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Noboby Logged In", Toast.LENGTH_SHORT).show();
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    @Override
    public void onClick( View view){

        String email = editTextlogin.getText().toString();
        String password = editTextpassword.getText().toString();

        if(view == buttonlogin){
            signIn(email, password);

        } else if(view == buttonRegister){
            createAccount(email, password);
            signIn(email, password);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentpost = new Intent(MainActivity.this, Post.class);
        Intent intentread = new Intent(MainActivity.this, Read.class);


        if(mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.menupost) {

                startActivity(intentpost);

            } else if (item.getItemId() == R.id.menuread){

                startActivity(intentread);

            } else if (item.getItemId() == R.id.menulogout){

                mAuth.signOut();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                           // Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Choose Page To Go")
                                    .setNegativeButton("Go To Post", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intentPost = new Intent(MainActivity.this, Post.class);
                                            startActivity(intentPost);

                                        }
                                    })
                                    .setPositiveButton("Go To Read Posts", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intentRead = new Intent(MainActivity.this, Read.class);
                                            startActivity(intentRead);
                                        }
                                    }) .show();
                        }

                        // ...
                    }
                });


    }
 }
