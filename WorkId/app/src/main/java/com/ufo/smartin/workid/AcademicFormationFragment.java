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

import utilities.AcademicFormation;

public class AcademicFormationFragment extends Fragment{

    private static final String ARG_USER = "user";
    private ArrayList<AcademicFormation> mUser;

    private ListView academicList;
    public ArrayList<AcademicFormation> listAcademics;
    public AcademicFormationFragment() {
        // Required empty public constructor
    }

    public static AcademicFormationFragment newInstance(ArrayList<AcademicFormation> user) {
        AcademicFormationFragment fragment = new AcademicFormationFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser= (ArrayList<AcademicFormation>) getArguments().getSerializable(ARG_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_academic_formation, container, false);
        listAcademics=new ArrayList<>();
        academicList=(ListView)view.findViewById(R.id.academics);
        loadList(mUser);
        academicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    final int position, long arg) {
                AcademicFormation selected = (AcademicFormation) academicList.getAdapter().getItem(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.delete_title))
                        .setMessage(getResources().getString(R.string.delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                listAcademics.remove(position);
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
        this.academicList.setAdapter(new ListAdapter(getActivity(), listAcademics));
    }

    public void newInfo(AcademicFormation item){
        listAcademics.add(item);
        displayList();
    }

    public void loadList(ArrayList<AcademicFormation> list){
        listAcademics=list;
        displayList();
    }

    public ArrayList<AcademicFormation> getList(){
        return listAcademics;
    }

    public void emptyList(){
        listAcademics.removeAll(listAcademics);
    }


    //----------------------------------------------------------------------------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context context;
        private List<AcademicFormation> list;

        public ListAdapter(Context context, List<AcademicFormation> list) {
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
                rowView = inflater.inflate(R.layout.academic_item, parent, false);
            }

            // Set data into the view.
            TextView academic = (TextView) rowView.findViewById(R.id.academic);
            TextView period = (TextView) rowView.findViewById(R.id.period);
            TextView place = (TextView) rowView.findViewById(R.id.place);
            TextView degree = (TextView) rowView.findViewById(R.id.degree);

            AcademicFormation item = this.list.get(position);
            academic.setText(item.discipline+"-"+item.academic);
            period.setText("("+item.iniDate+"-"+item.endDate+")");
            place.setText(item.place);
            degree.setText(item.degree);
            return rowView;
        }
    }

}
