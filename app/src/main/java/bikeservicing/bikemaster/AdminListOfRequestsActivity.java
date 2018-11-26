package bikeservicing.bikemaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AdminListOfRequestsActivity extends AppCompatActivity {

    CusInfo c = new CusInfo();
    DatabaseReference firebaseReference;
    ListView listViewRequests;
    HashMap<String, String> s = new HashMap<>();
    ArrayList<HashMap<String, String>> customerRequestsList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> customerNames,customerPhones, customerAddress, customerDates, customerTimes;
    FloatingActionButton buttonRefresh;
    ArrayAdapter<String> displayRequests,req1;
    TextView textViewName, textViewAddress, textViewPhone, textViewDate, textViewTime;
    Button buttonOkDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_of_requests);

        initialize();

        getDataFromFirebase();

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerRequestsList.clear();
                //req1 = new ArrayAdapter<String>(AdminListOfRequestsActivity.this, android.R.layout.simple_list_item_2,customerNames);
                displayRequests.notifyDataSetChanged();
                listViewRequests.setAdapter(displayRequests);
                getDataFromFirebase();
                Toast.makeText(getApplicationContext(),"Data refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDialog(position);
            }
        });




    }

    private void initialize() {

        listViewRequests = findViewById(R.id.listViewRequests);
        buttonRefresh = findViewById(R.id.buttonRefresh);
    }

    private void displayDialog(int position)
    {

        final Dialog dialog = new Dialog(AdminListOfRequestsActivity.this);
        dialog.setContentView(R.layout.customdialog);

        dialog.setTitle("Details");

        textViewName = (TextView) findViewById(R.id.textViewrName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewPhone =(TextView) findViewById(R.id.textViewPhoneNumber);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTimeSlot);

        textViewAddress.append(" " + customerAddress.get(position));
        textViewDate.append(" " + customerDates.get(position));
        textViewName.append(" " + customerNames.get(position));
        textViewPhone.append(" " + customerPhones.get(position));
        textViewTime.append(" " + customerTimes.get(position));

        buttonOkDialog = findViewById(R.id.buttonOkDialog);

        buttonOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

       Toast.makeText(getApplicationContext(),"Tapped at: " + position + " " +customerTimes.get(position) , Toast.LENGTH_SHORT).show();







    }

    private void getDataFromFirebase() {

        System.out.println("GetData called line 1");

        firebaseReference = FirebaseDatabase.getInstance().getReference().child("AdminToken").child("CustomerRequests");
        System.out.println("GetData called line 2");
        // firebaseReference.keepSynced(true);
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("GetData called, in onDataChange");
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    System.out.println("GetData called in first for");
                    System.out.println("Hi, " + d);


                    s = (HashMap<String, String>) d.getValue();
                    customerRequestsList.add(s);
                }

                Iterator<HashMap<String, String>> infos = customerRequestsList.iterator();


                initializeArraylists();





                for (int i = 0; i < customerRequestsList.size(); i++) {
                    String name = customerRequestsList.get(i).get("Name");
                    String address = customerRequestsList.get(i).get("Address");
                    String phoneNo = customerRequestsList.get(i).get("PhoneNo");
                    String dateToSet = customerRequestsList.get(i).get("Date");
                    String timeSlot = customerRequestsList.get(i).get("TimeSlot");

                    customerNames.add(name);
                    customerAddress.add(address);
                    customerPhones.add(phoneNo);
                    customerDates.add(dateToSet);
                    customerTimes.add(timeSlot);
                }

                displayRequests = new ArrayAdapter<String>(AdminListOfRequestsActivity.this, android.R.layout.simple_list_item_1, customerNames);

                listViewRequests.setAdapter(displayRequests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("GetData called in Cancelled");

            }
        });


    }

    private void initializeArraylists() {
        customerNames = new ArrayList<>();
        customerAddress = new ArrayList<>();
        customerDates = new ArrayList<>();
        customerPhones = new ArrayList<>();
        customerTimes = new ArrayList<>();
    }

}
