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

import utilities.*;


public class ProfessionalExperienceFragment extends Fragment {
    private static final String ARG_USER = "user";
    private ArrayList<ProfessionalExperience> mUser;

    private ListView professionalsList;
    public ArrayList<ProfessionalExperience> listProfessionals;
    public ProfessionalExperienceFragment() {
        // Required empty public constructor
    }

    public static ProfessionalExperienceFragment newInstance(ArrayList<ProfessionalExperience> user) {
        ProfessionalExperienceFragment fragment = new ProfessionalExperienceFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser= (ArrayList<ProfessionalExperience>) getArguments().getSerializable(ARG_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_professional_experience, container, false);
        listProfessionals=new ArrayList<>();
        professionalsList=(ListView)view.findViewById(R.id.professionals);
        loadList(mUser);
        professionalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    final int position, long arg) {
                ProfessionalExperience selected = (ProfessionalExperience) professionalsList.getAdapter().getItem(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.delete_title))
                        .setMessage(getResources().getString(R.string.delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                listProfessionals.remove(position);
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
        this.professionalsList.setAdapter(new ListAdapter(getActivity(), listProfessionals));

    }

    public void newInfo(ProfessionalExperience item){
        listProfessionals.add(item);
        displayList();
    }

    public void loadList(ArrayList<ProfessionalExperience> list){
        listProfessionals=list;
        displayList();
    }

    public ArrayList<ProfessionalExperience> getList(){
        return listProfessionals;
    }

    public void emptyList(){
        listProfessionals.removeAll(listProfessionals);
    }

    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context context;
        private List<ProfessionalExperience> list;

        public ListAdapter(Context context, List<ProfessionalExperience> list) {
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
                rowView = inflater.inflate(R.layout.professional_item, parent, false);
            }

            // Set data into the view.
            TextView pos = (TextView) rowView.findViewById(R.id.position);
            TextView period = (TextView) rowView.findViewById(R.id.period);
            TextView place = (TextView) rowView.findViewById(R.id.place);


            ProfessionalExperience item = this.list.get(position);
            pos.setText(item.position);
            period.setText("("+item.iniDate+"-"+item.endDate+")");
            place.setText(item.place);
            return rowView;
        }
    }
}
