package bikeservicing.bikemaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class ServicePickerActivity extends AppCompatActivity {


    FloatingActionButton fab_pluse, fab_call, fab_logout;
    Animation fabopen, fabclose, fabanticlockwise, fabclockwise;
    String PhnNumnber = "9561175543";
    Button buttonGenerateRequest;
    int totalPrize = 0;
    boolean isOpen = false;
    int pid = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_picker);

        buttonGenerateRequest = findViewById(R.id.buttonGenerateRequest);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("UserId");
        System.out.println("UID: " + userID);



        buttonGenerateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicePickerActivity.this, DateTimePickerActivity.class);
                intent.putExtra("UserId", userID);
                startActivity(intent);
                finish();
            }
        });



        fab_pluse = (FloatingActionButton) findViewById(R.id.fab_pluse);
        fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);
        fabopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabclockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fab_pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fab_call.startAnimation(fabclose);
                    fab_logout.startAnimation(fabclose);
                    fab_pluse.startAnimation(fabanticlockwise);
                    fab_logout.setClickable(false);
                    fab_call.setClickable(false);
                    isOpen = false;
                } else {
                    fab_call.startAnimation(fabopen);
                    fab_logout.startAnimation(fabopen);
                    fab_pluse.startAnimation(fabclockwise);
                    fab_logout.setClickable(true);
                    fab_call.setClickable(true);
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
                Intent intent = new Intent(ServicePickerActivity.this, MainActivity.class);
                intent.putExtra("Forced",true);
                startActivity(intent);


            }
        });

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Create the intent.
                // Create the intent.
                //Intent callIntent = new Intent(Intent.ACTION_CALL);
                // Set the data for the intent as the phone number.
                //callIntent.setData(Uri.parse("tel:9561175543"));
                // If package resolves to an app, check for phone permission,
                // and send intent.


                if (ActivityCompat.checkSelfPermission(ServicePickerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    Toast.makeText(ServicePickerActivity.this, "Please provide permissions for making a call",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    String phone_no = "tel:9561175543";
                    Intent callintent=new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse(phone_no));
                    try
                    {
                        startActivity(callintent);
                        Toast.makeText(getApplicationContext(), "Calling...", Toast.LENGTH_LONG).show();
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(ServicePickerActivity.this, "Unable to make call", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }








            }
        });

       /* fab_call.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {






                return true;
            }
        });*/




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ServicePickerActivity.this, ServicePickerActivity.class));


    }
}
