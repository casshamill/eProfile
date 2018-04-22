package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class PupilListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView listview ;
    ArrayList<String> ListViewItems = new ArrayList<String>();
    ArrayList<String> uids = new ArrayList<String>();
    String parent = "";
    boolean select_all = false;
    private String class_id;

    SparseBooleanArray sparseBooleanArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_list);
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

        Bundle bundle = getIntent().getExtras();
        parent = bundle.getString("parent");



        FloatingActionButton fab_next = (FloatingActionButton) findViewById(R.id.fab_continue);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Continue clicked");
                continue_clicked();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        listview = (ListView)findViewById(R.id.list);
        if (parent.equals("edit")) {
            listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }

        final DatabaseReference classid = mDatabase.child("teachers").child( mAuth.getCurrentUser().getUid()).child("class_id");
        classid.addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              class_id = (String) dataSnapshot.getValue();
                                              loadData((String)dataSnapshot.getValue());
                                          }


                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }

                                      });


    }

    private void continue_clicked() {

        boolean empty = true;
        for (int j = 0; j < sparseBooleanArray.size(); j++){
            if (sparseBooleanArray.valueAt(j) == true) {
                empty = false;
                break;
            }
        }
        if (empty) return;

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

        if (parent.equals("delete")) {
            deletePupils(KeyHolder);
            finish();
            return;
        }
        //check whether called by upload photo, upload text, edit pupil or delete pupil.
        Intent i = null;
        switch (parent) {
            case "text" :
                i = new Intent(PupilListActivity.this, UploadText.class);
                break;
            case "camera" :
                i = new Intent(PupilListActivity.this, UploadPhoto.class);
                break;
            case "edit" :
                i = new Intent(PupilListActivity.this, EditPupilActivity.class);
                break;
            default: //file
                i = new Intent(PupilListActivity.this, UploadPhoto.class);
        }
        try {
            System.setErr(new PrintStream(new File("log.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        i.putExtra("selected", ValueHolder );
        i.putExtra("uids", KeyHolder);
        i.putExtra("file", parent);
        System.out.println("Starting activity ");
        PupilListActivity.this.startActivity(i);
        System.out.println("Ending activity ");

    }

    private void deletePupils(String keys){
        // remove from class list keep profile
        DatabaseReference classRef = mDatabase.child("classes").child(class_id);
        String[] pupils = keys.split(",");
        for (String pupilid : pupils){
            classRef.child(pupilid).removeValue();
        }
    }

    public void createList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (PupilListActivity.this,
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

    public void loadData(String class_id){
        DatabaseReference class_ = mDatabase.child("classes").child(class_id);
        class_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListViewItems.clear();
                uids.clear();
                for (DataSnapshot pupil : dataSnapshot.getChildren()) {
                    ListViewItems.add((String)pupil.getValue());
                    uids.add(pupil.getKey().toString());
                    System.out.println((String)pupil.getKey());
                }
                createList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void selectAll(){
        select_all = !select_all;
        for ( int i=0; i < listview.getAdapter().getCount(); i++) {
            listview.setItemChecked(i, select_all);
        }
    }
}
