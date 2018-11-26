package bikeservicing.bikemaster;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimePickerActivity extends AppCompatActivity {

    EditText editTextDate;
    Calendar myCalendar = Calendar.getInstance();
    Spinner spinnerTimeSlot;
    String dateToSet, selectedItem;
    Button buttonOk;

    DatePickerDialog.OnDateSetListener date = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();

        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateToSet = sdf.format(myCalendar.getTime());
        editTextDate.setText(dateToSet);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        initialize();

        editTextDate.setInputType(InputType.TYPE_NULL);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DateTimePickerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateToSet==null)
                {
                    Toast.makeText(getApplicationContext(),"Select Date!!",Toast.LENGTH_LONG).show();
                }
                else
                {
                spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Toast.makeText(getApplicationContext(),"Date:" + dateToSet+", Time slot: " + String.valueOf(spinnerTimeSlot.getSelectedItem()), Toast.LENGTH_LONG).show();
            }}




        });









    }

    private void initialize() {

        editTextDate = findViewById(R.id.editTextDate);
        spinnerTimeSlot = findViewById(R.id.spinnerTimeSlot);
        buttonOk = findViewById(R.id.buttonOk);
    }


}
