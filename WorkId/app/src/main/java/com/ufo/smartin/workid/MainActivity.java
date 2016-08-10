package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                User us=jp.authenticateUser(url,mail,pass,"");
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
