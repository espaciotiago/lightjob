package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import utilities.JSONParser;
import utilities.Post;
import utilities.User;

public class PostDescriptionActivity extends AppCompatActivity {

    private TextView title;
    private TextView company;
    private TextView place;
    private TextView description;
    private TextView salary;
    private Button send;

    Post myPost;
    User user;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        myPost=(Post)i.getSerializableExtra("post");
        user=(User)i.getSerializableExtra("user");
        Log.d("USER",user.getName() + "," + user.getEmail());
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
                progress = new ProgressDialog(PostDescriptionActivity.this,R.style.StyledDialog);
                //progress.setMessage("Actualizando datos, por favor espere...");
                progress.setInverseBackgroundForced(true);
                progress.setCancelable(false);
                progress.show();
                new HttpRequestTask_EnviarCV().execute();
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

    //----------------------------------------------------------------------------------------------
    /*****    ENVIO DE HOJA DE VIDA *****/
    private class HttpRequestTask_EnviarCV extends AsyncTask<Void, Void, String>
    {
        public HttpRequestTask_EnviarCV(){}
        @Override
        protected String doInBackground(Void... params)
        {
            String url=LaunchActivity.IP+"/correo.php";
            JSONParser jp = new JSONParser();
            String ret = jp.enviarHojaDeVida(url,user.getEmail(),user.getName(),myPost.getTitle(),myPost.getCompanyMail());

            return ret;
        }

        @Override
        protected void onPostExecute(String info)
        {
            progress.dismiss();
            if(info !=null) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.cv_sent) + " " + myPost.getCompany(), Toast.LENGTH_SHORT).show();
            }else{
                Log.e("ERROR","error");
            }
        }

    }

}
