package com.example.cassie_app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainParentActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    LinearLayout p;
    List<Post> posts;
    RecyclerView rv;
    RVAdapter adapter;
    TextView gold_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search);
        FloatingActionButton fab_pdf = (FloatingActionButton) findViewById(R.id.fab_pdf);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do search --> maybe use menu search bar
            }
        });
        fab_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });
        gold_count = (TextView)findViewById(R.id.gold_count);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        posts = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        if( !bundle.getString("parent").equals("login")) {
            loadData(bundle.getString("parent"));
        } else {
            findPupil();
        }
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_parent, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addContent(Document document, View view)
            throws DocumentException
    {
        try
        {
            view.buildDrawingCache();

            Bitmap bmp = view.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scalePercent(70);
            image.setAlignment(Image.MIDDLE);
            document.add(image);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    private void createPDF() {
        ArrayList<View> mPrintView = new ArrayList<>();

        View mRootView = findViewById(android.R.id.content);
        //Create a directory for your PDF
        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "MyApp");
        if (!pdfDir.exists()){
            pdfDir.mkdirs();
            System.out.println("Creating directory");
        }

        File pdfFile = new File(pdfDir, "myPdfFile.pdf");

        try {
            pdfFile.createNewFile();
            Document  document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            for (int childCount = rv.getChildCount(), i = 0; i < childCount; ++i) {
                final RecyclerView.ViewHolder holder = rv.getChildViewHolder(rv.getChildAt(i));
                addContent(document, holder.itemView);
            }
            document.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(MainParentActivity.this, BuildConfig.APPLICATION_ID + ".provider", pdfFile );
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void findPupil () {
            DatabaseReference parent = mDatabase.child("parents").child(mAuth.getCurrentUser().getUid());
            parent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pupil = (String) dataSnapshot.child("pupil_id").getValue();
                    loadData(pupil);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    private void loadData(String pupilid){
        final TextView nameView = findViewById(R.id.pupil_name);
        DatabaseReference pupil = mDatabase.child("pupils").child(pupilid);
        pupil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot feed = dataSnapshot.child("feed");
                String name = (String) dataSnapshot.child("name").getValue();
                nameView.setText(name);
                int count = 0;
                for (DataSnapshot p : feed.getChildren()) {
                    String content = (String) p.child("content").getValue();
                    String author = (String) p.child("author").getValue();
                    String image = (String) p.child("image").getValue();
                    String date = (String) p.child("date").getValue();
                    String area = (String) p.child("area").getValue();
                    boolean golden = (boolean) p.child("golden").getValue();
                    if (golden) count ++;
                    System.out.println("Author:" + author + " -  Content : " + content + "Area : " + area);
                    Post post = new Post(author, content, date , image, area, golden);
                    System.out.println("post" + post);
                    posts.add(post);
                }
                gold_count.setText(String.valueOf(count));
                RVAdapter adapter = new RVAdapter(posts);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

;
