package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTeacherActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView listview ;
    ArrayList<String> ListViewItems = new ArrayList<String>();
    ArrayList<String> uids = new ArrayList<String>();
    String schoolid;

    SparseBooleanArray sparseBooleanArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_all);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("All clicked");
                selectAll();
            }
        });

        FloatingActionButton fab_done = (FloatingActionButton) findViewById(R.id.fab_done);
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        listview = (ListView)findViewById(R.id.list_unconfirmed);

        final DatabaseReference schoolidRef = mDatabase.child("teachers").child( mAuth.getCurrentUser().getUid()).child("school_id");
        schoolidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schoolid = ((String)dataSnapshot.getValue());
                loadData((String)dataSnapshot.getValue());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    public void createList(){
        //get teacher names

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (AdminTeacherActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, ListViewItems );

        System.out.println(ListViewItems.toArray());

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                sparseBooleanArray = listview.getCheckedItemPositions();

                String ValueHolder = "" ;

                int i = 0 ;

                while (i < sparseBooleanArray.size()) {

                    if (sparseBooleanArray.valueAt(i)) {

                        ValueHolder += ListViewItems.get(sparseBooleanArray.keyAt(i)) + ",";
                    }

                    i++ ;
                }

                ValueHolder = ValueHolder.replaceAll("(,)*$", "");

            }
        });
    }

    public void loadData(String schoolid){
        DatabaseReference school = mDatabase.child("schools").child(schoolid);
        school.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot teacher : dataSnapshot.getChildren()) {
                    System.out.println("Tiarnan: " + teacher.getKey().toString());
                    if ((boolean)teacher.getValue() == false){
                        uids.add(teacher.getKey().toString());
                    }
                }
                getTeachers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTeachers(){
        DatabaseReference teachers = mDatabase.child("teachers");
        teachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String uid: uids){
                    System.out.print("Tiarnan uid" + uid);
                    ListViewItems.add(dataSnapshot.child(uid).child("name").getValue().toString());
                }
                createList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void selectAll(){
        for ( int i=0; i < listview.getAdapter().getCount(); i++) {
            listview.setItemChecked(i, true);
        }
    }

    public void confirm(){
        System.out.println("done clicked");
        sparseBooleanArray = listview.getCheckedItemPositions();

        String ValueHolder = "" ;
        String KeyHolder = "";

        int j = 0 ;

        while (j < sparseBooleanArray.size()) {

            if (sparseBooleanArray.valueAt(j)) {

                ValueHolder += ListViewItems.get(sparseBooleanArray.keyAt(j)) + ",";
                KeyHolder += uids.get(sparseBooleanArray.keyAt(j)) + ",";
            }

            j++ ;
        }

        ValueHolder = ValueHolder.replaceAll("(,)*$", "");
        KeyHolder = KeyHolder.replaceAll("(,)*$", "");

        String[] uids = KeyHolder.split(",");
        for (String uid: uids) {
            DatabaseReference ref = mDatabase.child("teachers").child(uid).child("confirmed").getRef();
            ref.setValue(true);
            DatabaseReference school = mDatabase.child("schools").child(schoolid).child(uid).getRef();
            school.setValue(true);
        }
        Intent i = new Intent(AdminTeacherActivity.this, AdminSuccess.class);
        AdminTeacherActivity.this.startActivity(i);

    }

}
