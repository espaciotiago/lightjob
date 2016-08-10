package com.ufo.smartin.workid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class PresentationFragment extends Fragment {

    private EditText title;
    private EditText place;
    private EditText resume;

    public PresentationFragment() {
        // Required empty public constructor
    }

    public static PresentationFragment newInstance(String param1, String param2) {
        PresentationFragment fragment = new PresentationFragment();
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
        View view=inflater.inflate(R.layout.fragment_presentation, container, false);
        title=(EditText)view.findViewById(R.id.title);
        place=(EditText)view.findViewById(R.id.ubication);
        resume=(EditText)view.findViewById(R.id.resume);
        return view;
    }

    public EditText getTitle() {
        return title;
    }

    public void setTitle(EditText title) {
        this.title = title;
    }

    public EditText getPlace() {
        return place;
    }

    public void setPlace(EditText place) {
        this.place = place;
    }

    public EditText getResume() {
        return resume;
    }

    public void setResume(EditText resume) {
        this.resume = resume;
    }
}
