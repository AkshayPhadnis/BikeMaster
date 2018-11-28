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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class ServicePickerActivity extends AppCompatActivity {


    FloatingActionButton fab_pluse, fab_call, fab_logout;
    Animation fabopen, fabclose, fabanticlockwise, fabclockwise;
    Button buttonGenerateRequest;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_picker);

        buttonGenerateRequest = findViewById(R.id.buttonGenerateRequest);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("UserId");



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
                startActivity(intent);
                finish();
                System.exit(0);


            }
        });

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String phone_no = String.format("tel: %s","9561175543");
                    Intent callintent=new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse(phone_no));
                    try
                    {
                        startActivity(callintent);
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(ServicePickerActivity.this, "Unable to make call", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ServicePickerActivity.this, MainActivity.class));
        finish();
        System.exit(0);


    }
}
