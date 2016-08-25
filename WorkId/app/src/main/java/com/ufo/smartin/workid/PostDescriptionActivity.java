package com.ufo.smartin.workid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import utilities.Post;

public class PostDescriptionActivity extends AppCompatActivity {

    private TextView title;
    private TextView company;
    private TextView place;
    private TextView description;
    private TextView salary;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        final Post myPost=(Post)i.getSerializableExtra("post");
        title = (TextView) findViewById(R.id.title);
        company = (TextView) findViewById(R.id.company);
        place = (TextView) findViewById(R.id.place);
        description = (TextView) findViewById(R.id.description);
        salary= (TextView) findViewById(R.id.salary);
        send = (Button) findViewById(R.id.send);

        title.setText(myPost.getTitle());
        company.setText(myPost.getCompany());
        place.setText(myPost.getPlace());
        description.setText(myPost.getDescription());
        salary.setText(myPost.getSalary());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.cv_sent)+" "+myPost.getCompany(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
