package com.ufo.smartin.workid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utilities.Post;


public class SignupFragment extends Fragment {

    private EditText name;
    private EditText mail;
    private EditText password;
    private EditText re_password;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        View view=inflater.inflate(R.layout.fragment_signup, container, false);
        name=(EditText)view.findViewById(R.id.name);
        mail=(EditText)view.findViewById(R.id.email);
        password=(EditText)view.findViewById(R.id.password);
        re_password=(EditText)view.findViewById(R.id.repassword);
        return view;
    }

    public boolean completeInfo(){
        boolean complete=false;
        if(name.getText().toString().length()!=0 && mail.getText().length()!=0&&password.getText().toString().length()!=0 && re_password.getText().toString().length()!=0){
            complete=true;
        }
        return  complete;
    }

    public boolean passwordMatch(){
        boolean match=false;
        if(password.getText().toString().equals(re_password.getText().toString())) {
            match = true;
        }
        return match;
    }

    public void resetPasswords(){
        password.setText("");
        re_password.setText("");
    }

    public EditText getName() {
        return name;
    }

    public void setName(EditText name) {
        this.name = name;
    }

    public EditText getMail() {
        return mail;
    }

    public void setMail(EditText mail) {
        this.mail = mail;
    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public EditText getRe_password() {
        return re_password;
    }

    public void setRe_password(EditText re_password) {
        this.re_password = re_password;
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
