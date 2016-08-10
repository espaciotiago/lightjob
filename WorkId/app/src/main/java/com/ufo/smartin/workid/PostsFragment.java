package com.ufo.smartin.workid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utilities.Post;


public class PostsFragment extends Fragment {

    private ListView posts;

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posts, container, false);
        posts=(ListView)view.findViewById(R.id.posts);
        posts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                Post selected = (Post) posts.getAdapter().getItem(position);
                Intent goToDescription = new Intent(getActivity(), PostDescriptionActivity.class);
                goToDescription.putExtra("post", selected);
                startActivity(goToDescription);
            }
        });
        return view;
    }

    public void loadPosts(ArrayList<Post> posts){
        this.posts.setAdapter(new PostAdapter(getActivity(), posts));
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
            image.setImageResource(item.getImage());

            return rowView;
        }
    }
}
