package com.example.sankum.ictwt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Post extends Activity implements View.OnClickListener{

    private EditText editTextpost;
    private Button buttonpost;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Post.this, "User Logged In: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Post.this, "Noboby Logged In", Toast.LENGTH_SHORT).show();
                }

            }
        };

        buttonpost = (Button)findViewById(R.id.buttonpost);
        editTextpost = (EditText) findViewById(R.id.editTextpost);

        buttonpost.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentmain = new Intent(Post.this, MainActivity.class);
        Intent intentread = new Intent(Post.this, Read.class);


        if (mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.menupost) {

                Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show();

            } else if (item.getItemId() == R.id.menuread) {

                startActivity(intentread);

            } else if (item.getItemId() == R.id.menulogout) {

                mAuth.signOut();
                startActivity(intentmain);
            }


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataPosts = database.getReference("posts");
        DatabaseReference dataNewPosts = dataPosts.push();


        String post = editTextpost.getText().toString();
        String email = mAuth.getCurrentUser().getEmail();

        Tweet tweet = new Tweet (post, email);

        dataNewPosts.setValue(tweet);




    }
}

