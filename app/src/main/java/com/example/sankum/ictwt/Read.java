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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Read extends Activity implements View.OnClickListener{

    private TextView textViewone;
    private TextView textViewtwo;
    private TextView textViewthree;
    private TextView textViewfour;
    private TextView textViewfive;
    private Button buttonrefresh;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Read.this, "User Logged In: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Read.this, "Noboby Logged In", Toast.LENGTH_SHORT).show();
                }

            }
        };

        textViewone = (TextView) findViewById(R.id.textViewone);
        textViewtwo = (TextView) findViewById(R.id.textViewtwo);
        textViewthree = (TextView) findViewById(R.id.textViewthree);
        textViewfour = (TextView) findViewById(R.id.textViewfour);
        textViewfive = (TextView) findViewById(R.id.textViewfive);

        buttonrefresh = (Button) findViewById(R.id.buttonrefresh);

        buttonrefresh.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentmain = new Intent(Read.this, MainActivity.class);
        Intent intentpost = new Intent(Read.this, Post.class);


        if (mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.menupost) {

                startActivity(intentpost);

            } else if (item.getItemId() == R.id.menuread) {

                Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show();

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
        DatabaseReference dataPosts = database.getReference();

        dataPosts.child("posts").orderByKey().limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Tweet tweet = dataSnapshot.getValue(Tweet.class);
                String val = tweet.email + ": " + tweet.post;
                textViewone.setText(val);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
