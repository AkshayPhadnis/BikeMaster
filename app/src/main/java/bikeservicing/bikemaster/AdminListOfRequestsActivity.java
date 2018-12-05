package bikeservicing.bikemaster;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AdminListOfRequestsActivity extends AppCompatActivity {

    FloatingActionButton fab_pluse, fab_call, fab_logout;
    Animation fabopen, fabclose, fabanticlockwise, fabclockwise;
    Button buttonGenerateRequest;

    boolean isOpen = false;

    CusInfo c = new CusInfo();
    DatabaseReference firebaseReference;
    ListView listViewRequests;
    HashMap<String, String> s = new HashMap<>();
    ArrayList<HashMap<String, String>> customerRequestsList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> customerNames, customerPhones, customerAddress, customerDates, customerTimes;
    FloatingActionButton buttonRefresh;
    ArrayAdapter<String> displayRequests, req1;
    TextView textViewName, textViewAddress, textViewPhone, textViewDate, textViewTime;
    Button buttonAcceptDialog, buttonRejectDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_of_requests);

        initialize();


        getDataFromFirebase();

        fab_pluse = (FloatingActionButton) findViewById(R.id.fab_pluse);
        //fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);
        fabopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabclockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);


        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerRequestsList.clear();
//                customerNames.clear();
                //req1 = new ArrayAdapter<String>(AdminListOfRequestsActivity.this, android.R.layout.simple_list_item_2,customerNames);
                // req1.notifyDataSetChanged();
                listViewRequests.setAdapter(req1);
                getDataFromFirebase();
                Toast.makeText(getApplicationContext(), "Data refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        fab_pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    buttonRefresh.startAnimation(fabclose);
                    fab_logout.startAnimation(fabclose);
                    fab_pluse.startAnimation(fabanticlockwise);
                    fab_logout.setClickable(false);
                    buttonRefresh.setClickable(false);
                    isOpen = false;
                } else {
                    buttonRefresh.startAnimation(fabopen);
                    fab_logout.startAnimation(fabopen);
                    fab_pluse.startAnimation(fabclockwise);
                    fab_logout.setClickable(true);
                    buttonRefresh.setClickable(true);
                    isOpen = true;
                }
            }
        });


        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), "User Sign out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminListOfRequestsActivity.this, MainActivity.class);
               // intent.putExtra("Forced",true);
                startActivity(intent);
                /*finish();
                System.exit(0);*/
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

    private void displayDialog(int position) {

        final Dialog dialog = new Dialog(AdminListOfRequestsActivity.this);
        System.out.println("Dialog created");
        dialog.setContentView(R.layout.customdialog);
        System.out.println("Content view set");
        dialog.setTitle("Details");
        System.out.println("Title set");


        textViewName = dialog.findViewById(R.id.textViewrName);
        textViewAddress = dialog.findViewById(R.id.textViewAddress);
        textViewPhone = dialog.findViewById(R.id.textViewPhoneNumber);
        textViewDate = dialog.findViewById(R.id.textViewDate);
        textViewTime = dialog.findViewById(R.id.textViewTimeSlot);


        textViewAddress.append(" " + customerAddress.get(position));
        textViewDate.append(" " + customerDates.get(position));
        textViewName.append(" " + customerNames.get(position));
        textViewPhone.setText(customerPhones.get(position));
        textViewTime.append(" " + customerTimes.get(position));


        buttonAcceptDialog = dialog.findViewById(R.id.buttonAcceptDialog);


        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(AdminListOfRequestsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    Toast.makeText(AdminListOfRequestsActivity.this, "Please provide permissions for making a call",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    String phone_no = String.format("tel: %s",textViewPhone.getText().toString());
                    Intent callintent=new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse(phone_no));
                    try
                    {
                        startActivity(callintent);
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(AdminListOfRequestsActivity.this, "Unable to make call", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dialog.dismiss();
            }
        });


        dialog.show();
        Toast.makeText(getApplicationContext(), "Tapped at: " + position + " " + customerTimes.get(position), Toast.LENGTH_SHORT).show();


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
                req1 = displayRequests;
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






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminListOfRequestsActivity.this, MainActivity.class));
        finish();
        System.exit(0);


    }
}
