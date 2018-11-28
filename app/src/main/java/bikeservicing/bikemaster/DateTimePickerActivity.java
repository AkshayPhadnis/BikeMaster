package bikeservicing.bikemaster;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateTimePickerActivity extends AppCompatActivity {

    EditText editTextDate;
    Calendar myCalendar = Calendar.getInstance();
    Spinner spinnerTimeSlot;
    String dateToSet, timeSlot, status, selectedDate;
    Button buttonOk;
    int cnt = 1;
    DatabaseReference firebaseReference, firebaseRef2;
    boolean alreadySubmitted=false;
    DatePickerDialog datePickerDialog;


    ArrayList<String> cusInfo = new ArrayList<>();


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
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateToSet = sdf.format(myCalendar.getTime());
        editTextDate.setText(dateToSet);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);


        initialize();
        //disable button here
        //call function here
        //Load into arraylist
        //And enable button

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("UserId");

        buttonOk.setEnabled(false);

        getDataFromFirebase(userID);
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        buttonOk.setEnabled(true);

        editTextDate.setInputType(InputType.TYPE_NULL);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePickerDialog  = new DatePickerDialog(DateTimePickerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

               datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
               datePickerDialog.show();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 {
                    if (dateToSet == null) {
                        Toast.makeText(getApplicationContext(), "Select Date!!", Toast.LENGTH_LONG).show();
                    } else {


                        spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                        //firebaseReference.child("Users").child(userID).child("Info").setValue();
                        //Toast.makeText(getApplicationContext(), "Added to firebase", Toast.LENGTH_LONG).show();

                        timeSlot = String.valueOf(spinnerTimeSlot.getSelectedItem());
                        status = "pending";

                        //Toast.makeText(getApplicationContext(),"Date:" + dateToSet+", Time slot: " + timeSlot + ", name:" + cusInfo.get(1) + ", phone: " + cusInfo.get(2) + ", address: " + cusInfo.get(3), Toast.LENGTH_LONG).show();
                        HashMap<String, String> serviceInfo = new HashMap<>();
                        serviceInfo.put("Date", dateToSet);
                        serviceInfo.put("Timeslot", timeSlot);
                        serviceInfo.put("Status", status);


                        firebaseReference.child("Users").child(userID).child("ServiceHistory").push().setValue(serviceInfo);

                        Toast.makeText(getApplicationContext(), "Request submitted", Toast.LENGTH_SHORT).show();
                        cnt++;


               /* {
                    Toast.makeText(getApplicationContext(), "Request already submitted", Toast.LENGTH_SHORT).show();
                }*/

                        String name = cusInfo.get(1);
                        String phoneNo = cusInfo.get(2);
                        String address = cusInfo.get(3);

                        System.out.println("Name: " + name + ", Phone: " + phoneNo + ", Address: " + address);

                        sendToAdmin(dateToSet, timeSlot, name, phoneNo, address, status);


                    }




                }

            }
        });




    }

    private void sendToAdmin(String dateToSet, String timeSlot, String name, String phoneNo, String address, String status) {

        firebaseRef2 = FirebaseDatabase.getInstance().getReference().child("AdminToken").child("CustomerRequests");

        HashMap<String, String> serviceDetails = new HashMap<>();

        serviceDetails.put("Name", name);
        serviceDetails.put("Address", address);
        serviceDetails.put("PhoneNo", phoneNo);
        serviceDetails.put("Date", dateToSet);
        serviceDetails.put("TimeSlot", timeSlot);
        serviceDetails.put("Status", status);

        firebaseRef2.push().setValue(serviceDetails);


    }

    private void getDataFromFirebase(String userID) {

        System.out.println("GetData Called by " + userID);
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Info");


        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String s;
                for (DataSnapshot usersSnap : dataSnapshot.getChildren()) {
                    s = usersSnap.getValue(String.class);
                    System.out.println("snap: " + s);
                    cusInfo.add(s);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void initialize() {

        editTextDate = findViewById(R.id.editTextDate);
        spinnerTimeSlot = findViewById(R.id.spinnerTimeSlot);
        buttonOk = findViewById(R.id.buttonOk);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(DateTimePickerActivity.this, ServicePickerActivity.class));

    }
}
