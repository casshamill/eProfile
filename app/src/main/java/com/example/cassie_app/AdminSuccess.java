package com.example.cassie_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminSuccess extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_success);

        Button AdminHome = (Button) findViewById(R.id.returnadmin_btn);

        AdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminSuccess.this, AdminTeacherActivity.class);
                AdminSuccess.this.startActivity(i);
            }
        });
    }
}
