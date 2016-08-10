package com.ufo.smartin.workid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by smartin on 07/03/2016.
 */
public class AcademicFormationDialogFragment extends DialogFragment {

    private Button submit;
    private Spinner degree;
    private Spinner discipline;
    private EditText place;
    private EditText academic;
    private EditText iniDate;
    private EditText endDate;
    private LinearLayout place_layout;
    OnaAddSelected mListener;
    private Calendar calendar;
    private int year=1994, month=0, day=1;

    public interface OnaAddSelected {
        public void onArticleSelectedListener(String degree, String discipline,String place, String academic,String inDate, String endDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view=inflater.inflate(R.layout.dialog_add_academic_formation, container, false);
        place_layout=(LinearLayout)view.findViewById(R.id.place_lay);
        place=(EditText)view.findViewById(R.id.place);
        academic=(EditText)view.findViewById(R.id.academic);
        iniDate=(EditText)view.findViewById(R.id.start_date);
        iniDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    new DatePickerDialog(getActivity(), myDateListener, year, month, day).show();
                    return true;
                }
                return false;
            }
        });
        endDate=(EditText)view.findViewById(R.id.end_date);
        endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    new DatePickerDialog(getActivity(), myDateListenerEnd, year, month, day).show();
                    return true;
                }
                return false;
            }
        });
        degree=(Spinner)view.findViewById(R.id.degree);
        String[] degrees =getResources().getStringArray(R.array.degrees);
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, degrees);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree.setAdapter(degreeAdapter);
        degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = degree.getSelectedItem().toString();
                if (selected.equals("Experto empírico")) {
                    place_layout.setVisibility(View.GONE);
                } else {
                    place_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        discipline=(Spinner)view.findViewById(R.id.disciplines);
        String[] disciplines =getResources().getStringArray(R.array.disciplines);
        ArrayAdapter<String> disciplineAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, disciplines);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discipline.setAdapter(disciplineAdapter);
        submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!degree.getSelectedItem().toString().equals("Titulación")) {
                    if (degree.getSelectedItem().toString().equals("Experto empírico")) {
                        if(!discipline.getSelectedItem().toString().equals("Disciplina académica") &&
                                !academic.getText().toString().equals("") &&
                                !iniDate.getText().toString().equals("") &&
                                !endDate.getText().toString().equals("")) {


                            mListener.onArticleSelectedListener(degree.getSelectedItem().toString(),
                                    discipline.getSelectedItem().toString(),
                                    place.getText().toString(),
                                    academic.getText().toString(), iniDate.getText().toString(),
                                    endDate.getText().toString());
                            dismiss();
                        }else{
                            Toast.makeText(getActivity(),"Debe llenar todos los campos para continuar",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(!discipline.getSelectedItem().toString().equals("Disciplina académica") &&
                                !place.getText().toString().equals("") && !academic.getText().toString().equals("") &&
                                !iniDate.getText().toString().equals("") &&
                                !endDate.getText().toString().equals("")) {


                            mListener.onArticleSelectedListener(degree.getSelectedItem().toString(),
                                    discipline.getSelectedItem().toString(),
                                    place.getText().toString(),
                                    academic.getText().toString(), iniDate.getText().toString(),
                                    endDate.getText().toString());
                            dismiss();
                        }else{
                            Toast.makeText(getActivity(),"Debe llenar todos los campos para continuar",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getActivity(),"Debe llenar todos los campos para continuar",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnaAddSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        iniDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private DatePickerDialog.OnDateSetListener myDateListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDateEnd(arg1, arg2+1, arg3);
        }
    };

    private void showDateEnd(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }



}
