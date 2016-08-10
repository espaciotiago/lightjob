package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import utilities.Post;
import utilities.User;

public class MenuActivity extends AppCompatActivity{

    private static final int NUM_PAGES = 2;

    private Toolbar toolbar;
    private Menu menu;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private User user;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user=(User)getIntent().getSerializableExtra("user");
        setTitle(user.getName().split(" ")[0]);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.post_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.category_unselected));
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() != 0) {
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.category_selected);
                } else {
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setIcon(R.drawable.post_selected);
                }
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() != 0) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.category_unselected);
                } else {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.post_unselected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        progress = new ProgressDialog(this,R.style.StyledDialog);
        //progress.setMessage("Actualizando datos, por favor espere...");
        progress.setInverseBackgroundForced(true);
        progress.setCancelable(false);
        progress.show();
        new HttpRequestTaskPrueba().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_main, this.menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        }

        else if (id == R.id.action_profile) {
            Intent goToCompleteProfile = new Intent(getApplicationContext(), CompleteProfileActivity.class);
            goToCompleteProfile.putExtra("user",user);
            startActivityForResult(goToCompleteProfile, 1);
        }

        else if (id == R.id.action_configuration) {
            Intent goToConfiguration=new Intent(getApplicationContext(),ConfigurationActivity.class);
            goToConfiguration.putExtra("user",user);
            startActivityForResult(goToConfiguration, 1);
        }
        /*
        if(id==R.id.action_jobs){
            View i = toolbar.findViewById(R.id.action_jobs);
            Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clicked);
            i.startAnimation(anima);
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putSerializable("user", user);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            user=(User)data.getSerializableExtra("user");
        }
    }

    //--------------------------------------------------------------------------------------------------------
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("POSITION ", position + "");
            if(position<1) {
                return new PostsFragment();
            }else{
                return new CategoriesFragment();
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

    //---------------------------------------------------------------------------------------------------------------
    /**
    * HTTP PRUEBA
    */
    //---------------------------------------------------------------------------------------------------------------

    private class HttpRequestTaskPrueba extends AsyncTask<Void, Void, String>
    {
        public HttpRequestTaskPrueba(){}
        @Override
        protected String doInBackground(Void... params)
        {
            String ret="Ok";
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String info)
        {
            if(info.equals("Ok")){
                ArrayList<Post> listPosts=new ArrayList<>();
                listPosts.add(new Post(R.drawable.ufo, "Desarrollador de Backend para Apps",
                        "Se busca ingeniero o tecnico con habilidades para el desarrollo de Backend para aplicaciones móviles (Android)"
                                +" y Web.\n Experiencia requerida: más de un (1) año.\n Conocimientos amplios en:"+"" +
                                "\n PHP\n MySQL\n Linux\n Apache",
                        "UFO Mobile","Cali-Colombia",4));
                PostsFragment su=(PostsFragment)mPager.getAdapter().instantiateItem(mPager,0);
                su.loadPosts(listPosts);
                progress.dismiss();
            }
        }

    }


}
