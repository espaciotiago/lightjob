package com.ufo.smartin.workid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utilities.Reference;

public class ReferenceFragment extends Fragment {
    private static final String ARG_USER = "user";
    private ArrayList<Reference> mUser;

    private ListView referenceList;
    public ArrayList<Reference> listReferences;

    public ReferenceFragment() {
        // Required empty public constructor
    }

    public static ReferenceFragment newInstance(ArrayList<Reference> user) {
        ReferenceFragment fragment = new ReferenceFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser= (ArrayList<Reference>) getArguments().getSerializable(ARG_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reference, container, false);
        listReferences=new ArrayList<>();
        referenceList=(ListView)view.findViewById(R.id.references);
        loadList(mUser);
        referenceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    final int position, long arg) {
                Reference selected = (Reference) referenceList.getAdapter().getItem(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.delete_title))
                        .setMessage(getResources().getString(R.string.delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                listReferences.remove(position);
                                displayList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        return view;
    }

    public void displayList(){
        this.referenceList.setAdapter(new ListAdapter(getActivity(), listReferences));
    }

    public void newInfo(Reference item){
        listReferences.add(item);
        displayList();
    }

    public void loadList(ArrayList<Reference> list){
        listReferences=list;
        displayList();
    }

    public ArrayList<Reference> getList(){
        return listReferences;
    }

    public void emptyList(){
        listReferences.removeAll(listReferences);
    }


    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context context;
        private List<Reference> list;

        public ListAdapter(Context context, List<Reference> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                rowView = inflater.inflate(R.layout.reference_item, parent, false);
            }

            // Set data into the view.
            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView relation = (TextView) rowView.findViewById(R.id.relation);
            TextView occupation = (TextView) rowView.findViewById(R.id.occupation);
            TextView contact = (TextView) rowView.findViewById(R.id.contact);

            Reference item = this.list.get(position);
            name.setText(item.getName());
            if(item.getType()==Reference.FAMILIAR) {
                relation.setText(item.getRelation()+" ("+getResources().getString(R.string.familiar_reference)+")");
            }else{
                relation.setText("("+getResources().getString(R.string.personal_reference)+")");
            }
            occupation.setText(item.getOccupation());
            contact.setText(item.getContact());
            return rowView;
        }
    }
}
