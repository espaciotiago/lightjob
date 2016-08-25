package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.ByteArrayOutputStream;

import utilities.*;


public class MainActivity extends AppCompatActivity {

    private int contador=0;
    private ViewPager mPager;
    private Button login;
    private Button signup;
    private EditText username;
    private EditText password;

    private boolean Pasa=true;
    DatabaseHelper db;
    private String token;
    private BroadcastReceiver mRegistrationBroadcastReciver;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHelper(this);

        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);
        username=(EditText)findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);

        TestFragmentAdapter mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mRegistrationBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    token = intent.getStringExtra("token");
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

        new Thread(new Runnable(){
            public void run() {
                while(Pasa==true) {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new PasarPagina().execute();
                }
            }
        }).start();
        events();
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

    public void events(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Pasa = false;
                //AUTENTICATE USER
                new Http_AuthUser(username.getText().toString(),password.getText().toString()).execute();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSignUp = new Intent(MainActivity.this, SignupActivity.class);
                Pasa = false;
                startActivity(goSignUp);
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    

    //---------------------------------------------------------------------------------------------
    private class PasarPagina extends AsyncTask<Void, Void, Integer>
    {

        public PasarPagina(){
        }
        @Override
        protected Integer doInBackground(Void... params)
        {
            if (contador== 2) {
                contador = 0;
            } else {
                contador++;
            }
            return contador;
        }

        @Override
        protected void onPostExecute(Integer info) {
            mPager.setCurrentItem(info);
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // AUTHENTICATE USER
    //------------------------------------------------------------------------------------------------------------------
    private class Http_AuthUser extends AsyncTask<Void, Void, User>
    {
        String mail;
        String pass;
        ProgressDialog loading;

        public Http_AuthUser(String mail, String pass){
            this.mail=mail;
            this.pass=pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "Comprobando datos...",
                    "Puede tardar unos segundos", true, true);
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
            loading.dismiss();
            if(info!=null){
                db.createUser(info.getEmail(),info.getPassword());
                Intent goMenu = new Intent(MainActivity.this, MenuActivity.class);
                goMenu.putExtra("user", info);
                startActivity(goMenu);
                finish();
            }else{
                Toast.makeText(MainActivity.this, "Nombre de usuario o contraseña incorrecto",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
