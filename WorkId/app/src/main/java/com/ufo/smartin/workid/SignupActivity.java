package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import utilities.AcademicFormation;
import utilities.JSONParser;
import utilities.ProfessionalExperience;
import utilities.Reference;
import utilities.User;

public class SignupActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://simplifiedcoding.16mb.com/ImageUpload/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private static final int NUM_PAGES = 2;

    private ImageView profilePicture;

    private ImageView camera;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Menu menu;

    private Bitmap bitmap;
    private Uri filePath;

    private String ima="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        camera=(ImageView)findViewById(R.id.camera);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        profilePicture=(ImageView) findViewById(R.id.profile_picture);
        profilePicture.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("img_View11", "img_View11 _1");
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        Log.i("img_View11", "img_View11 _4");
                        showFileChooser();
                        break;
                }
                return true;
            }
        });
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
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            if(mPager.getCurrentItem()==0) {
                SignupFragment su=(SignupFragment)mPager.getAdapter().instantiateItem(mPager,mPager.getCurrentItem());
                boolean complete=su.completeInfo();
                boolean match=su.passwordMatch();
                if(complete==true && match==true) {
                    mPager.setCurrentItem(1);
                }else if(match==false && complete==true){
                    Toast.makeText(this, getResources().getString(R.string.passwords_no_match), Toast.LENGTH_SHORT).show();
                    su.resetPasswords();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.incomplete_info), Toast.LENGTH_SHORT).show();
                }
            }else{
                SignupFragment su=(SignupFragment)mPager.getAdapter().instantiateItem(mPager,0);
                PresentationFragment pr=(PresentationFragment)mPager.getAdapter().instantiateItem(mPager,mPager.getCurrentItem());
                boolean complete=su.completeInfo();
                boolean match=su.passwordMatch();
                if(complete==true && match==true) {
                    //CREATE USER Basic
                    String name=su.getName().getText().toString();
                    String email=su.getMail().getText().toString();
                    String pass=su.getPassword().getText().toString();
                    String location=pr.getPlace().getText().toString();
                    String title=pr.getTitle().getText().toString();
                    String resume=pr.getResume().getText().toString();
                    String account="false";
                    ArrayList<AcademicFormation> academics=new ArrayList<>();
                    ArrayList<ProfessionalExperience> professionals=new ArrayList<>();
                    ArrayList<Reference> references=new ArrayList<>();
                    User user = new User(name,email,pass,location,title,resume,
                            ima,account,academics,professionals,references);
                    //new Http_CreateUser(user).execute();
                    Intent goSignupFormation=new Intent(SignupActivity.this,SignupFormationActivity.class);
                    goSignupFormation.putExtra("user", user);
                    goSignupFormation.putExtra("create", true);
                    startActivityForResult(goSignupFormation,1);
                    //startActivity(goSignupFormation);
                }else if(match==false && complete==true){
                    Toast.makeText(this, getResources().getString(R.string.passwords_no_match), Toast.LENGTH_SHORT).show();
                    su.resetPasswords();
                    mPager.setCurrentItem(0);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.incomplete_info), Toast.LENGTH_SHORT).show();
                    mPager.setCurrentItem(0);
                }
            }
        }
        return super.onOptionsItemSelected(item);
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
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,240,240,false);
                profilePicture.setImageBitmap(bitmap2);
                ima=getStringImage(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.setImageResource(R.drawable.camera_w);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //--------------------------------------------------------------------------------------------------------
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("POSITION ", position + "");
            SignupFragment su=new SignupFragment();
            if(position<1) {
                return su;
            }else{
                return new PresentationFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // CONEXION DE PRUEBA
    //------------------------------------------------------------------------------------------------------------------
    private class Http_CreateUser extends AsyncTask<Void, Void, String>
    {
        User u;
        ProgressDialog loading;
        public Http_CreateUser(User u){
            this.u=u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(SignupActivity.this, "Creando usuario...", "Puede tardar unos segundos",true,true);
        }
        @Override
        protected String doInBackground(Void... params)
        {   String ret="404";
            JSONParser jp= new JSONParser();
            try
            {   String url="http://192.168.0.17:5000/createUser.php";
                ret=jp.createUser(url,u.getEmail(),u.getPassword(),u.getName(),u.getImage(),u.getLocation(),u.getTitle(),u.getResume(),u.isAccount()+"");
                Log.e("PASS", u.getPassword());
                Log.i("TAG", ret);
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
            loading.dismiss();
            if(info.contains("200")){
                Intent goSignupFormation=new Intent(SignupActivity.this,SignupFormationActivity.class);
                goSignupFormation.putExtra("user", u);
                startActivityForResult(goSignupFormation,1);
            }else{
                Toast.makeText(SignupActivity.this,"Ya hay un usuario con esta cuenta de correo",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}
