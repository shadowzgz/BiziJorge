package com.seas.a10.bizijorge.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.seas.a10.bizijorge.R;

import java.util.Calendar;


public class fAvisoAdmin extends Fragment implements DatePickerDialog.OnDateSetListener {

    //region variables
    TextView tvAvisoFechaCaducidad;
    Spinner avisoSpinner;
    //endregion

    //region constructores
    public fAvisoAdmin() {
        // Required empty public constructor
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_f_aviso_admin, container, false);

        tvAvisoFechaCaducidad = v.findViewById(R.id.tvAvisoFechaCaducidad);
        avisoSpinner = v.findViewById(R.id.spAvisoTipo);
        setSpinnerData();
        tvAvisoFechaCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        return v;
    }

    //Ponemos los datos en el desplegable
    public void setSpinnerData(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.avisoTipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        avisoSpinner.setAdapter(adapter);

    }

    //recogemos los datos del calendario
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //Colocamos la fecha seleccionada en el textView
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int mo = (int)month;
        mo = mo+1;

        String date = "month/day/year: " + mo + "/" + dayOfMonth + "/" + year;
        tvAvisoFechaCaducidad.setText(date);
    }
}
