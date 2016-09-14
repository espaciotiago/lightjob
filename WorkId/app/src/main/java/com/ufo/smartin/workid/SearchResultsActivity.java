package com.ufo.smartin.workid;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utilities.JSONParser;
import utilities.Post;
import utilities.User;

public class SearchResultsActivity extends ActionBarActivity {

    private List<Post> auxList;
    private List<Post> listPosts;
    private LinearLayout layout;
    private ListView posts;
    private User user;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //----------------------------------------------------------------------------------------------------------------
        auxList=new ArrayList<>();
        //----------------------------------------------------------------------------------------------------------------
        listPosts=new ArrayList<>();
        posts=(ListView)findViewById(R.id.posts);
        layout= (LinearLayout)findViewById(R.id.layout);
        user = (User)getIntent().getSerializableExtra("user");
        Intent intent = getIntent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            user = (User)appData.getSerializable("user");
            Log.d("Result search",user.toString());
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            /*
            listPosts=doMySearch(query);
            if(listPosts.isEmpty()||listPosts==null){
                TextView notFound=new TextView(this);
                notFound.setText(getResources().getString(R.string.not_found));
                layout.addView(notFound);
            }
            */
            progress = new ProgressDialog(SearchResultsActivity.this,R.style.StyledDialog);
            //progress.setMessage("Actualizando datos, por favor espere...");
            progress.setInverseBackgroundForced(true);
            progress.setCancelable(false);
            progress.show();
            new Http_OffersSearch(query).execute();

        }else{
            String myCategory = intent.getStringExtra("category");
            Log.d("USER SERACH RES",user.toString());
            /*
            int myCategory=intent.getIntExtra("category", -1);
            Log.d("CAT",myCategory+"");
            listPosts=doMySearchByCategory(myCategory);
            if(listPosts.isEmpty()||listPosts==null){
                TextView notFound=new TextView(this);
                notFound.setText(getResources().getString(R.string.not_found));
                layout.addView(notFound);
            }
            */
            progress = new ProgressDialog(SearchResultsActivity.this,R.style.StyledDialog);
            //progress.setMessage("Actualizando datos, por favor espere...");
            progress.setInverseBackgroundForced(true);
            progress.setCancelable(false);
            progress.show();
            Log.d("CATEGORY",myCategory);
            new Http_OffersBy(myCategory).execute();
        }

        posts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                Post selected = (Post) posts.getAdapter().getItem(position);
                Intent goToDescription = new Intent(getApplicationContext(),PostDescriptionActivity.class);
                goToDescription.putExtra("post", selected);
                goToDescription.putExtra("user", user);
                startActivity(goToDescription);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putSerializable("user", user);
        startSearch(null, false, appData, false);
        return true;
    }

    public ArrayList<Post> doMySearch(String query){
        ArrayList<Post> response=new ArrayList<>();

        for(int i=0;i<auxList.size();i++){
            String bu = auxList.get(i).getDescription();
            String ti=auxList.get(i).getTitle();
            if(bu.toLowerCase().contains(query.toLowerCase()) || ti.toLowerCase().contains(query.toLowerCase())){
                response.add(auxList.get(i));
            }
        }
        return response;
    }

    public ArrayList<Post> doMySearchByCategory(int category){
        ArrayList<Post> response=new ArrayList<>();

        for(int i=0;i<auxList.size();i++){
            String cat=auxList.get(i).getCategory();
            if(cat.equals(category)){
                response.add(auxList.get(i));
            }
        }
        return response;
    }
    //----------------------------------------------------------------------------------------------------------------------------
    public class PostAdapter extends BaseAdapter {

        private Context context;
        private List<Post> posts;

        public PostAdapter(Context context, List<Post> posts) {
            this.context = context;
            this.posts = posts;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int position) {
            return posts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (convertView == null) {
                // Create a new view into the list.
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.post_item, parent, false);
            }

            // Set data into the view.
            ImageView image = (ImageView) rowView.findViewById(R.id.image);
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView description = (TextView) rowView.findViewById(R.id.description);

            Post item = this.posts.get(position);
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            //image.setImageResource(item.getImage());

            return rowView;
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    /**
     * HTTP SEARCH
     */
    //---------------------------------------------------------------------------------------------------------------

    private class Http_OffersSearch extends AsyncTask<Void, Void, ArrayList<Post>>
    {
        private String query;

        public Http_OffersSearch(String query){
            this.query = query;
        }
        @Override
        protected ArrayList<Post> doInBackground(Void... params)
        {
            String url=LaunchActivity.IP+"/getOffersSearch.php";
            ArrayList<Post> ret;
            JSONParser jp = new JSONParser();
            ret = jp.getOffersBy(url,query);

            return ret;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> info)
        {
            Log.d("CATEGORY",info.toString());
            if(info!=null) {
                if (!info.isEmpty()) {
                    listPosts = info;
                    posts.setAdapter(new PostAdapter(getApplicationContext(), listPosts));
                }else{
                    TextView notFound=new TextView(getApplicationContext());
                    notFound.setText(getResources().getString(R.string.not_found));
                    layout.addView(notFound);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Error obteniendo publicaciones", Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

    }

    //---------------------------------------------------------------------------------------------------------------
    /**
     * HTTP OFFERS BY
     */
    //---------------------------------------------------------------------------------------------------------------

    private class Http_OffersBy extends AsyncTask<Void, Void, ArrayList<Post>>
    {
        private String query;

        public Http_OffersBy(String query){
            this.query = query;
        }
        @Override
        protected ArrayList<Post> doInBackground(Void... params)
        {
            String url=LaunchActivity.IP+"getOffersBy.php";
            Log.d("URL",url);
            ArrayList<Post> ret;
            JSONParser jp = new JSONParser();
            ret = jp.getOffersBy(url,query);

            return ret;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> info)
        {
            if(info!=null) {
                if (!info.isEmpty()) {
                    listPosts = info;
                    posts.setAdapter(new PostAdapter(getApplicationContext(), listPosts));
                }else{
                    TextView notFound=new TextView(getApplicationContext());
                    notFound.setText(getResources().getString(R.string.not_found));
                    layout.addView(notFound);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Error obteniendo publicaciones", Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

    }
}
