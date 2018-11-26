package bikeservicing.bikemaster;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class ServicePickerActivity extends AppCompatActivity {


    FloatingActionButton fab_pluse, fab_call, fab_logout;
    Animation fabopen, fabclose, fabanticlockwise, fabclockwise;
    String PhnNumnber = "9561175543";
    Button buttonGenerateRequest;
    int totalPrize = 0;
    boolean isOpen = false;
    int pid=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_picker);

        buttonGenerateRequest = findViewById(R.id.buttonGenerateRequest);

        buttonGenerateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicePickerActivity.this, DateTimePickerActivity.class);
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


            }
        });
      /*  fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Create the intent.
                // Create the intent.
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                // Set the data for the intent as the phone number.
                callIntent.setData(Uri.parse("tel:9561175543"));
                // If package resolves to an app, check for phone permission,
                // and send intent.

                callatruntimepermission();





    }
});

    }

    private void callatruntimepermission() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},pid);
        }
        else
        {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            // Set the data for the intent as the phone number.
            callIntent.setData(Uri.parse("tel:9561175543"));
            startActivity(callIntent);
        }
    }

    public void onRequestPermissionsResult(int RequestCode, @NonNull  String[] permission,@NonNull int[] grantResult)
    {
        super.onRequestPermissionsResult(RequestCode,permission,grantResult);

        if(RequestCode==pid)
        {
            if(grantResult[0]==PackageManager.PERMISSION_GRANTED)
            {
                callatruntimepermission();
            }
        }*/
    }


}
