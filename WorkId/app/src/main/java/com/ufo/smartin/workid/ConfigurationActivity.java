package com.ufo.smartin.workid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import utilities.DatabaseHelper;
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
                db.deleteUsers();
                Intent goToMain=new Intent(ConfigurationActivity.this,MainActivity.class);
                goToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMain);
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
}
