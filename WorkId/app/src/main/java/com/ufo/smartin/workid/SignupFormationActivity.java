package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import utilities.*;

public class SignupFormationActivity extends AppCompatActivity
        implements AcademicFormationDialogFragment.OnaAddSelected,
        ProfessionalExperienceDialogFragment.OnaAddSelectedP,
        ReferenceDialogFragment.OnaAddSelected{

    private static final int NUM_PAGES = 4;
    private com.getbase.floatingactionbutton.FloatingActionButton fab;

    public ArrayList<AcademicFormation> listAcademics;
    public ArrayList<ProfessionalExperience> listProfessionals;
    public ArrayList<Reference> listReferences;

    private Menu menu;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private User user;

    DatabaseHelper db;

    private boolean create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_formation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        db=new DatabaseHelper(this);

        Intent i = getIntent();
        user=(User)i.getSerializableExtra("user");
        create=i.getBooleanExtra("create",false);
        Log.d("CREATE",create+"");
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);

        listAcademics=getAllAcademic(user);
        listProfessionals=getAllPros(user);
        listReferences=getAllReferences(user);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.degree_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profesional_unselected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.relation));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.upgrade_unselected));

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.degree_selected);
                    tabLayout.getTabAt(tab.getPosition() + 1).setIcon(R.drawable.profesional_unselected);
                    tabLayout.getTabAt(tab.getPosition() + 2).setIcon(R.drawable.relation);
                    tabLayout.getTabAt(tab.getPosition() + 3).setIcon(R.drawable.upgrade_unselected);
                    menu.getItem(0).setIcon(R.drawable.next);
                    fab.setVisibility(View.VISIBLE);
                    AcademicFormationFragment su=(AcademicFormationFragment)mPager.getAdapter().instantiateItem(mPager,0);
                    listAcademics=su.getList();
                    su.loadList(listAcademics);
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.profesional_selected);
                    tabLayout.getTabAt(tab.getPosition() - 1).setIcon(R.drawable.degree_unselected);
                    tabLayout.getTabAt(tab.getPosition() + 1).setIcon(R.drawable.relation);
                    tabLayout.getTabAt(tab.getPosition() + 2).setIcon(R.drawable.upgrade_unselected);
                    menu.getItem(0).setIcon(R.drawable.next);
                    fab.setVisibility(View.VISIBLE);
                    ProfessionalExperienceFragment su=(ProfessionalExperienceFragment)mPager.getAdapter().instantiateItem(mPager,1);
                    listProfessionals=su.getList();
                    su.loadList(listProfessionals);
                } else if(tabLayout.getSelectedTabPosition() == 2){
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.relation_selected);
                    tabLayout.getTabAt(tab.getPosition() - 1).setIcon(R.drawable.profesional_unselected);
                    tabLayout.getTabAt(tab.getPosition() - 2).setIcon(R.drawable.degree_unselected);
                    tabLayout.getTabAt(tab.getPosition() + 1).setIcon(R.drawable.upgrade_unselected);
                    menu.getItem(0).setIcon(R.drawable.next);
                    fab.setVisibility(View.VISIBLE);
                    ReferenceFragment su=(ReferenceFragment)mPager.getAdapter().instantiateItem(mPager,2);
                    listReferences=su.getList();
                    su.loadList(listReferences);
                } else if(tabLayout.getSelectedTabPosition() == 3){
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.upgrade_selected);
                    tabLayout.getTabAt(tab.getPosition() - 1).setIcon(R.drawable.relation);
                    tabLayout.getTabAt(tab.getPosition() - 2).setIcon(R.drawable.profesional_unselected);
                    tabLayout.getTabAt(tab.getPosition() - 3).setIcon(R.drawable.degree_unselected);
                    menu.getItem(0).setIcon(null);
                    fab.setVisibility(View.GONE);
                }
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_academics);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 0) {
                    AcademicFormationDialogFragment newFragment;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newFragment = new AcademicFormationDialogFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

                } else if (mPager.getCurrentItem() == 1) {
                    ProfessionalExperienceDialogFragment newFragment;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newFragment = new ProfessionalExperienceDialogFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

                } else if (mPager.getCurrentItem() == 2) {
                    ReferenceDialogFragment newFragment;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newFragment = new ReferenceDialogFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_signup_formation, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finish) {
            if(mPager.getCurrentItem()==0) {
                mPager.setCurrentItem(1);
            }else if(mPager.getCurrentItem()==1){
                mPager.setCurrentItem(2);
            }else if(mPager.getCurrentItem()==2) {
                mPager.setCurrentItem(3);
            }else if(mPager.getCurrentItem()==3) {
                if (create ==false) {
                    user.setAcademics(listAcademics);
                    user.setProfessionals(listProfessionals);
                    user.setReferences(listReferences);
                    //UPLOAD USER: Update
                    //db.createUser(user.getEmail(),user.getPassword());
                    new Http_UpdateUser(user).execute();
                } else {
                    //UPLOAD USER: create
                    new Http_CreateUser(user).execute();
                }
            }
            //finish();
        }
        else if(id == android.R.id.home){
            if(mPager.getCurrentItem()==0) {
                onBackPressed();
            }else if(mPager.getCurrentItem()==1){
                mPager.setCurrentItem(0);
            }else if(mPager.getCurrentItem()==2) {
                mPager.setCurrentItem(1);
            }else if(mPager.getCurrentItem()==3) {
                mPager.setCurrentItem(2);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onArticleSelectedListener(String degree,String discipline ,String place, String academic, String inDate, String endDate) {
        AcademicFormation newItem= new AcademicFormation(degree,discipline,place,academic,inDate,endDate);
        listAcademics.add(newItem);
        AcademicFormationFragment su=(AcademicFormationFragment)mPager.getAdapter().instantiateItem(mPager,0);
        su.loadList(listAcademics);
    }

    @Override
    public void onArticleSelectedListenerP(String place, String position, String inDate, String endDate) {
        ProfessionalExperience newItem= new ProfessionalExperience(place,position,inDate,endDate);
        listProfessionals.add(newItem);
        ProfessionalExperienceFragment su=(ProfessionalExperienceFragment)mPager.getAdapter().instantiateItem(mPager,1);
        su.loadList(listProfessionals);
    }

    @Override
    public void onArticleSelectedListener(int type, String name, String relation, String ocupation, String contact) {
        Reference newItem= new Reference(type,name,relation,ocupation,contact);
        listReferences.add(newItem);
        ReferenceFragment su=(ReferenceFragment)mPager.getAdapter().instantiateItem(mPager,2);
        su.loadList(listReferences);
    }

    public ArrayList<AcademicFormation> getAllAcademic(User use){
        ArrayList<AcademicFormation> aca=new ArrayList<>();
        if(user!=null) {
            //-------------------------------------------------------------------
            aca=use.getAcademics();
            //-------------------------------------------------------------------
        }
        return aca;
    }

    public ArrayList<ProfessionalExperience> getAllPros(User user){
        ArrayList<ProfessionalExperience> aca=new ArrayList<>();
        if(user!=null) {
            //-------------------------------------------------------------------
            aca= user.getProfessionals();
            //-------------------------------------------------------------------
        }
        return aca;
    }

    public ArrayList<Reference> getAllReferences(User user){
        ArrayList<Reference> aca=new ArrayList<>();
        if(user!=null) {
            //-------------------------------------------------------------------
            aca= user.getReferences();
            //-------------------------------------------------------------------
        }
        return aca;
    }

    //--------------------------------------------------------------------------------------------------------
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("POSITION ", position + "");
            if(position==0) {
                Fragment af= AcademicFormationFragment.newInstance(listAcademics);
                return af;
            }else if(position==1){
                Fragment pe=ProfessionalExperienceFragment.newInstance(listProfessionals);
                return pe;
            }
            else if(position==2){
                Fragment re=ReferenceFragment.newInstance(listReferences);
                return  re;
            }
            else if(position==3){
                Fragment re=UpgradeAccountFragment.newInstance(user.getEmail(),user.isAccount());
                return  re;
            }
            return null;
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
    // CREATE USER
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
            loading = ProgressDialog.show(SignupFormationActivity.this, "Creando usuario...", "Puede tardar unos segundos",true,true);
        }
        @Override
        protected String doInBackground(Void... params)
        {   String ret="404";
            JSONParser jp= new JSONParser();
            try
            {   String url=LaunchActivity.IP+"/createUser.php";
                ret=jp.uploadUser(url,u);
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
                Intent finishSignup = new Intent(SignupFormationActivity.this, MainActivity.class);
                finishSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(finishSignup);
            }else{
                Toast.makeText(SignupFormationActivity.this,"Ya hay un usuario con esta cuenta de correo",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // UPDATE USER
    //------------------------------------------------------------------------------------------------------------------
    private class Http_UpdateUser extends AsyncTask<Void, Void, String>
    {
        User u;
        ProgressDialog loading;
        public Http_UpdateUser(User u){
            this.u=u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(SignupFormationActivity.this, "Actualizando datos...", "Puede tardar unos segundos",true,true);
        }
        @Override
        protected String doInBackground(Void... params)
        {   String ret="404";
            JSONParser jp= new JSONParser();
            try
            {   String url=LaunchActivity.IP+"/updateUser.php";
                ret=jp.uploadUser(url,u);
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
                Intent finishSignup = new Intent(SignupFormationActivity.this, MenuActivity.class);
                finishSignup.putExtra("user", user);
                finishSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(finishSignup);
            }else{
                Toast.makeText(SignupFormationActivity.this,"Ya hay un usuario con esta cuenta de correo",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
