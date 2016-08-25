package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import utilities.DatabaseHelper;
import utilities.JSONParser;
import utilities.Upload;
import utilities.User;

public class LaunchActivity extends AppCompatActivity {
    DatabaseHelper db;
    //public static String IP="http://192.168.0.25:5000/lightjob";
    //public static String IP="http://181.53.141.91:5000/lightjob";
    public static String IP="http://138.128.188.98/~lightjo1/app_backend/";
    //public static String IP="http://lightjob.org/app_backend/";
    private String token;
    private BroadcastReceiver mRegistrationBroadcastReciver;
    public LaunchActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //deleteDatabase("Lightjob");

        db=new DatabaseHelper(this);
        String userLoged=db.userExist();
/*
        if(userLoged!=null){
            //GET USER BY Mail
            String mail=userLoged.split(" ")[0];
            String pass=userLoged.split(" ")[1];
            new Http_AuthUser(mail,pass).execute();
            //new PostUser(u).execute();
            /*
            Intent goMenu=new Intent(LaunchActivity.this,MenuActivity.class);
            goMenu.putExtra("user", u);
            startActivity(goMenu);
            finish();
            *//*
        }else{
            //new PostUser(u).execute();

            Intent goToMain=new Intent(LaunchActivity.this,MainActivity.class);
            startActivity(goToMain);
            finish();

        }
*/      if(userLoged!=null){
            final String mail=userLoged.split(" ")[0];
            final String pass=userLoged.split(" ")[1];
            mRegistrationBroadcastReciver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                        token = intent.getStringExtra("token");
                        new Http_AuthUser(mail,pass,token).execute();
                    }else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                        Toast.makeText(getApplicationContext(),"GCM Token error",Toast.LENGTH_SHORT).show();
                    }else{

                    }
                }
            };

            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if(ConnectionResult.SUCCESS != resultCode){
                //Checck type of error
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                    Toast.makeText(getApplicationContext(),"Google play services is not install/enabled in this device",Toast.LENGTH_SHORT).show();
                    GooglePlayServicesUtil.showErrorNotification(resultCode,getApplicationContext());
                }else{
                    Toast.makeText(getApplicationContext(),"This device doesn't support for Google Play Services",Toast.LENGTH_SHORT).show();
                }
            }else{
                //Start Service
                Intent intent = new Intent(this,GCMRegistrationIntentService.class);
                startService(intent);
            }

            //GET USER BY Mail

            //new PostUser(u).execute();
            /*
            Intent goMenu=new Intent(LaunchActivity.this,MenuActivity.class);
            goMenu.putExtra("user", u);
            startActivity(goMenu);
            finish();
            */
        }else{
            //new PostUser(u).execute();

            Intent goToMain=new Intent(LaunchActivity.this,MainActivity.class);
            startActivity(goToMain);
            finish();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity","onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReciver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReciver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity","onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReciver);
    }

    //------------------------------------------------------------------------------------------------------------------
    // AUTHENTICATE USER
    //------------------------------------------------------------------------------------------------------------------
    private class Http_AuthUser extends AsyncTask<Void, Void, User>
    {
        String mail;
        String pass;
        String toke;

        public Http_AuthUser(String mail, String pass, String toke){
            this.mail=mail;
            this.pass=pass;
            this.toke=toke;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Void... params)
        {   User ret=null;
            JSONParser jp= new JSONParser();
            try
            {   String url=LaunchActivity.IP+"/authenticateUser.php";
                User us=jp.authenticateUser(url,mail,pass,token);
                ret=us;
                return ret;

            } catch (Exception e)
            {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return ret;
        }

        @Override
        protected void onPostExecute(User info)
        {
            if(info!=null){
                db.createUser(info.getEmail(),info.getPassword());
                Intent goMenu = new Intent(LaunchActivity.this, MenuActivity.class);
                goMenu.putExtra("user", info);
                startActivity(goMenu);
                finish();
            }
        }
    }

    }
