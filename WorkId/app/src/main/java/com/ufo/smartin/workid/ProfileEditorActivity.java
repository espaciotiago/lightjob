package com.ufo.smartin.workid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import utilities.JSONParser;
import utilities.User;

public class ProfileEditorActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;

    private Menu menu;
    //private Spinner degree;

    private User user;
    private EditText name;
    private EditText location;
    private EditText title;
    private EditText resume;

    private ImageView profilePicture;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user=(User)getIntent().getSerializableExtra("user");
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);

        name=(EditText)findViewById(R.id.name);
        location=(EditText)findViewById(R.id.location);
        title=(EditText)findViewById(R.id.title);
        resume=(EditText)findViewById(R.id.resume);

        name.setText(user.getName());
        location.setText(user.getLocation());
        title.setText(user.getTitle());
        resume.setText(user.getResume());

        /*
        degree=(Spinner)findViewById(R.id.degree);
        String[] degrees=getResources().getStringArray(R.array.degrees);
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, degrees);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree.setAdapter(degreeAdapter);
        */

        profilePicture=(ImageView) findViewById(R.id.profile_picture);
        if(!user.getImage().equals("")&&user.getImage()!=null){
            Bitmap imag = null;
            byte[] decodedString = Base64.decode(user.getImage(), Base64.DEFAULT);
            imag = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(imag);
        }
        profilePicture.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        showFileChooser();
                        break;
                }
                return true;
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_signup, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_next:
                Intent goToEditProfile=new Intent(getApplicationContext(),SignupFormationActivity.class);
                user.setName(name.getText().toString());
                user.setLocation(location.getText().toString());
                user.setTitle(title.getText().toString());
                user.setResume(resume.getText().toString());
                goToEditProfile.putExtra("user", user);
                goToEditProfile.putExtra("create", false);
                startActivityForResult(goToEditProfile,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,240,240,false);
                profilePicture.setImageBitmap(bitmap2);
                user.setImage(getStringImage(bitmap2));

                //--------------------------------------------------------------------------------
                //new HttpRequestTask_uploadImage(user.getEmail(),getStringImage(bitmap)).execute();
                //--------------------------------------------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            user=(User)data.getSerializableExtra("user");
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //------------------------------------------------------------------------------------------------------------------
    // CONEXION DE PRUEBA
    //------------------------------------------------------------------------------------------------------------------
    private class HttpRequestTask_uploadImage extends AsyncTask<Void, Void, String>
    {
        String userMail;
        String image;
        public HttpRequestTask_uploadImage(String usu, String img){
            userMail=usu;
            image=img;
        }
        @Override
        protected String doInBackground(Void... params)
        {   String ret="404";
            try
            {
                final String url = "http://192.168.0.17:5000/upload.php";//Example:http://172.30.162.239:4567/list
                JSONParser jParser = new JSONParser();
                String noti = jParser.uploadImage(url,userMail,image,null);
                ret=noti;
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
            Log.d("TAG INFO",info);
        }
    }

}
