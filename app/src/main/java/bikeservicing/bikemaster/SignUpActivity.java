package bikeservicing.bikemaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextName, editTextPhone, editTextResidence;
    Button buttonRegister;
    String email, password, name, phone, residence, age;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                name = editTextName.getText().toString();
                phone = editTextPhone.getText().toString();
                residence = editTextResidence.getText().toString();

                HashMap<String,String> userInfo = new HashMap<>();
                userInfo.put("name",name);
                userInfo.put("email",email); userInfo.put("residence",residence); userInfo.put("age",age);
                userInfo.put("phone",phone);

                signUpUser(email,password,userInfo);

            }
        });




    }

    public void signUpUser(String email, String password, final HashMap<String,String> userInfo) {


        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    addUserToFirebasde(user,userInfo);

                    moveToLogin();


                }
            }
        });
    }

    public void addUserToFirebasde(FirebaseUser user, HashMap<String,String> userInfo) {

        String userID = user.getUid();
        DatabaseReference firebaseReference;
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseReference.child("Users").child(userID).child("Info").setValue(userInfo);
        Toast.makeText(SignUpActivity.this, "Added to firebase", Toast.LENGTH_LONG).show();



    }

    public void moveToLogin() {

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }


    public void initialize() {

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextResidence = findViewById(R.id.editTextResidence);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}
