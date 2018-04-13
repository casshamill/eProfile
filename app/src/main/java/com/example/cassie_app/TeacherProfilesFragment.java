package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherProfilesFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView listview ;
    ArrayList<String> ListViewItems = new ArrayList<String>();
    ArrayList<String> uids = new ArrayList<String>();
    String parent = "";

    SparseBooleanArray sparseBooleanArray;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teacher_profiles, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        listview = (ListView)view.findViewById(R.id.list);

        DatabaseReference classid = mDatabase.child("teachers").child( mAuth.getCurrentUser().getUid()).child("class_id");
        classid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadData((String)dataSnapshot.getValue());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return view;
    }

    public void createList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),
                        android.R.layout.simple_list_item_single_choice,
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
                ValueHolder = (String) listview.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), MainParentActivity.class);
                i.putExtra("parent", uids.get(position));
                TeacherProfilesFragment.this.startActivity(i);
            }
        });
    }

    public void loadData(String class_id){
        DatabaseReference class_ = mDatabase.child("classes").child(class_id);
        class_.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
        for ( int i=0; i < listview.getAdapter().getCount(); i++) {
            listview.setItemChecked(i, true);
        }
    }
}
