package com.grpprj.submit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class NewPrjActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_prj);

        initSpinners();
    }

    private void initSpinners(){
        String[] type = getResources().getStringArray(R.array.lecturer_array);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.supervisor_dropdown,
                        type);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.txtSupervisor);
        editTextFilledExposedDropdown.setAdapter(adapter);

    }
}