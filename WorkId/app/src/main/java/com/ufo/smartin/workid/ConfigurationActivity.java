package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import utilities.DatabaseHelper;
import utilities.JSONParser;
import utilities.User;

public class ConfigurationActivity extends AppCompatActivity {

    private TextView chgPass;
    private TextView exit;
    private TextView faq;
    private Menu menu;
    private User user;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new DatabaseHelper(this);

        user=(User)getIntent().getSerializableExtra("user");
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);

        chgPass=(TextView)findViewById(R.id.change_pass);
        chgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogFragment newFragment;
                FragmentManager fragmentManager = getSupportFragmentManager();
                newFragment = ChangePasswordDialogFragment.newInstance(user.getEmail(),user.getPassword());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            }
        });

        exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerrar sesion
                new Http_deleteToken(user.getEmail()).execute();
            }
        });

        faq = (TextView) findViewById(R.id.faq);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaqDialogFragment newFragment;
                FragmentManager fragmentManager = getSupportFragmentManager();
                newFragment = new FaqDialogFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
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

    //------------------------------------------------------------------------------------------------------------------
    // DELETE TOKEN
    //------------------------------------------------------------------------------------------------------------------
    private class Http_deleteToken extends AsyncTask<Void, Void, String>
    {
        String mail;

        public Http_deleteToken(String mail){
            this.mail=mail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params)
        {   String ret=null;
            JSONParser jp= new JSONParser();
            try
            {   String url=LaunchActivity.IP+"/deleteToken.php";
                String us=jp.deleteToken(url,mail);
                ret=us;
                return ret;

            } catch (Exception e)
            {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String info)
        {
            if(info!=null){
                db.deleteUsers();
                Intent goToMain=new Intent(ConfigurationActivity.this,MainActivity.class);
                goToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMain);
            }else{
                Toast.makeText(getApplicationContext(), "Error cerrando sesion, intentelo más tarde",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
