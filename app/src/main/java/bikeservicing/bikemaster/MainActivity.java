package bikeservicing.bikemaster;

import android.content.Intent;
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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonSignup;
    FirebaseAuth mAuth;
    String instanceToken,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        initialize();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if( email.length()==0)
                {//editTextEmail.getText().clear();
                    editTextEmail.setError("Invalid Email Address");
                }

                else if(password.length()<6)
                {//editTextPassword.getText().clear();
                    editTextPassword.setError("Password too short!!!");
                }
                else
                    signIn();
            }
        });
    }

    public void signIn() {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    instanceToken = FirebaseInstanceId.getInstance().getToken();
                    Log.println(Log.INFO, "mytag", "Token obtained:" + instanceToken);

                    FirebaseUser user = mAuth.getCurrentUser();
                    addTokenToFirebase(instanceToken, user);

                    Toast.makeText(getApplicationContext(), "Sign in successfull", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Authentication failed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void addTokenToFirebase(String instanceToken, FirebaseUser signedInUser) {

        String email = signedInUser.getEmail();
        String userID = signedInUser.getUid();
        String name = " ";

        if(email.equals("akshayphadnis1994@gmail.com"))//E.g. Akshay is admin and will get customer's notifications
        {
            name = "Akshay";

            HashMap<String,String> tokenDetails = new HashMap<>();

            tokenDetails.put("AdminName",name);
            tokenDetails.put("Token",instanceToken);

            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

            Log.d("myTag" , "Writing FCM Token to Firebase");

            firebaseDatabase.child("AdminToken").child(name).setValue(tokenDetails);


            //now for user
            HashMap<String,String> adminInfo = new HashMap<>();
            adminInfo.put("name",name); adminInfo.put("residence","Pune");
            firebaseDatabase.child("Users").child(userID).child("info").setValue(adminInfo);
            firebaseDatabase.child("Users").child(userID).child("token").setValue(instanceToken );

            Intent intent = new Intent(MainActivity.this,AdminListOfRequestsActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else//user is not admin
        {
            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

            Log.d("myTag" , "Writing FCM Token to Firebase");

            firebaseDatabase.child("Users").child(userID).child("Token").setValue(instanceToken);

            Intent intent = new Intent(MainActivity.this,ServicePickerActivity.class);
            startActivity(intent);
            MainActivity.this.finish();


    }

    public void initialize() {

        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup =findViewById(R.id.buttonSignup);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
    }


}
