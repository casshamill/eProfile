package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class CreateClassActivity extends AppCompatActivity {

    ListView listview ;
    ArrayList<String> ListViewItems = new ArrayList<String>();
    ArrayList<Long> pupilDates = new ArrayList<Long>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                fab.setVisibility(View.INVISIBLE);
                Intent i = new Intent(CreateClassActivity.this, AddPupilPopup.class);
                CreateClassActivity.this.startActivityForResult(i, 1);
            }
        });

        FloatingActionButton fab_done = (FloatingActionButton) findViewById(R.id.fab_done);
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postToDb();
                System.out.println("addding to db");
                Toast.makeText(CreateClassActivity.this, "Class Created", Toast.LENGTH_LONG );
                Intent i = new Intent(CreateClassActivity.this, MainTeacherActivity.class);
                CreateClassActivity.this.startActivity(i);
            }
        });
        listview = (ListView)findViewById(R.id.list_new_pupils);
        createList();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setVisibility(View.VISIBLE);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String name = data.getStringExtra("NAMEVALUE");
                long dob = data.getLongExtra("DOBVALUE", -1);
                pupilDates.add(dob);
                ListViewItems.add(name);
                createList();
            }
        }
    }

    public void createList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (CreateClassActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, ListViewItems);

        System.out.println(ListViewItems.toArray());
        listview.setAdapter(adapter);
    }

    public void postToDb(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth;
        //create class to get UID
        DatabaseReference classRef = mDatabase.child("classes").getRef();
        DatabaseReference newClass = classRef.push();
        String classID = newClass.getKey();

        //add pupils to pupil list
        DatabaseReference pupilRef = mDatabase.child("pupils").getRef();
        mAuth = FirebaseAuth.getInstance();
        ArrayList<String> uids = new ArrayList<String>();
        for (int i = 0; i < ListViewItems.size(); i++){
            DatabaseReference newRef = pupilRef.push();
            newRef.setValue(new Pupil(ListViewItems.get(i), pupilDates.get(i), classID));
            uids.add(newRef.getKey());
        }

        // add pupil names to class list
        for (int i = 0; i < ListViewItems.size(); i++){
            newClass.child(uids.get(i)).setValue(ListViewItems.get(i));
        }
        DatabaseReference teacherClassRef = mDatabase.child("teachers").child(mAuth.getCurrentUser().getUid()).child("class_id").getRef();
        teacherClassRef.setValue(newClass.getKey());
    }
}
