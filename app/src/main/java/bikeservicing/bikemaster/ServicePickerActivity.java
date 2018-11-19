package bikeservicing.bikemaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ServicePickerActivity extends AppCompatActivity {

    Button buttonGenerateRequest;
    int totalPrize=0;
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
            }
        });




    }



}
