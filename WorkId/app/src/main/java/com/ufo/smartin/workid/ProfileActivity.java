package com.ufo.smartin.workid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import utilities.User;

public class ProfileActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://simplifiedcoding.16mb.com/ImageUpload/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePicture;
    private Bitmap bitmap;
    private Uri filePath;

    private FloatingActionButton fab_show;
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_config;
    private FloatingActionButton fab_pdf;

    private User user;

    private TextView name;
    private TextView location;
    private TextView title;
    private TextView resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user=(User)getIntent().getSerializableExtra("user");

        name=(TextView)findViewById(R.id.name);
        location=(TextView)findViewById(R.id.location);
        title=(TextView)findViewById(R.id.title);
        resume=(TextView)findViewById(R.id.resume);

        name.setText(user.getName());
        location.setText(user.getLocation());
        title.setText(user.getTitle());
        resume.setText(user.getResume());

        profilePicture=(ImageView) findViewById(R.id.profile_picture);
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

        fab_show=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.complete_profile);
        fab_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCompleteProfile = new Intent(getApplicationContext(), CompleteProfileActivity.class);
                goToCompleteProfile.putExtra("user",user);
                startActivityForResult(goToCompleteProfile, 1);
            }
        });

        fab_edit=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEditProfile = new Intent(getApplicationContext(), ProfileEditorActivity.class);
                goToEditProfile.putExtra("user",user);
                startActivityForResult(goToEditProfile, 2);
            }
        });

        fab_config=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.settings);
        fab_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToConfiguration=new Intent(getApplicationContext(),ConfigurationActivity.class);
                goToConfiguration.putExtra("user",user);
                startActivityForResult(goToConfiguration, 2);
            }
        });

        fab_pdf=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.pdf);
        fab_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pdf_generated), Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
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
