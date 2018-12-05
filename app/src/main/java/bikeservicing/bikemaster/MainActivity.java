package bikeservicing.bikemaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonSignup;
    FirebaseAuth mAuth;
    String instanceToken, email, password;
    Intent intent1;

    private ProgressDialog progressBar;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#000000"));
        pDialog.setTitleText("Logging in");
        pDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            pDialog.show();
            System.out.println("Email: " + user.getEmail() + ", UID: " + user.getUid());
            if (user.getEmail().compareTo("akshayphadnis1994@gmail.com") == 0) {
                intent1 = new Intent(MainActivity.this, AdminListOfRequestsActivity.class);
                intent1.putExtra("UserId", user.getUid());
                startActivity(intent1);
            } else {
                //getLoginScreen();
                intent1 = new Intent(MainActivity.this, ServicePickerActivity.class);
                intent1.putExtra("UserId", user.getUid());
                startActivity(intent1);


            }
            Toast.makeText(getApplicationContext(), "Hi " + user.getEmail() + ", we directly logged you in.", Toast.LENGTH_LONG).show();

            this.finish();

        } else {
            //Toast.makeText(getApplicationContext(), "User null", Toast.LENGTH_LONG).show();
            getLoginScreen();
        }



    }


    public void signIn() {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    instanceToken = FirebaseInstanceId.getInstance().getToken();
                    Log.println(Log.INFO, "mytag", "Token obtained:" + instanceToken);

                    FirebaseUser user = mAuth.getCurrentUser();
                    addTokenToFirebase(instanceToken, user);


                    Toast.makeText(getApplicationContext(), "Sign in successfull", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void addTokenToFirebase(String instanceToken, FirebaseUser signedInUser) {

        String email = signedInUser.getEmail();
        String userID = signedInUser.getUid();
        String name = " ";

        if (email.equals("akshayphadnis1994@gmail.com"))//E.g. Akshay is admin and will get customer's notifications
        {
            name = "Akshay";

            HashMap<String, String> tokenDetails = new HashMap<>();

            tokenDetails.put("AdminName", name);
            tokenDetails.put("Token", instanceToken);

            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

            Log.d("myTag", "Writing FCM Token to Firebase");

            firebaseDatabase.child("AdminToken").child(name).setValue(tokenDetails);


            //now for user
            HashMap<String, String> adminInfo = new HashMap<>();
            adminInfo.put("name", name);
            adminInfo.put("residence", "Pune");
            firebaseDatabase.child("Users").child(userID).child("info").setValue(adminInfo);
            firebaseDatabase.child("Users").child(userID).child("token").setValue(instanceToken);

            Intent intent = new Intent(MainActivity.this, AdminListOfRequestsActivity.class);
            intent.putExtra("UserId", userID);
            startActivity(intent);
            MainActivity.this.finish();

        } else//user is not admin
        {
            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

            Log.d("myTag", "Writing FCM Token to Firebase");
            Log.d("UserId: ", userID + ", " + instanceToken);

            //firebaseDatabase.child("Users").child(userID).child("Token").setValue(instanceToken);

            Intent intent = new Intent(MainActivity.this, ServicePickerActivity.class);
            intent.putExtra("UserId", userID);
            startActivity(intent);
        }


    }

    public void initialize() {

        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

    }

    public void getLoginScreen() {


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (email.length() == 0) {//editTextEmail.getText().clear();
                    editTextEmail.setError("Invalid Email Address");
                } else if (password.length() < 6) {//editTextPassword.getText().clear();
                    editTextPassword.setError("Password too short!!!");
                } else
                    signIn();

                pDialog.show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
        System.exit(0);
    }


}
