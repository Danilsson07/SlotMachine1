package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btLogin;
    EditText etLoginName, etPassword;

    TextView tV1;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
   }
   /*
        btLogin= (Button)findViewById(R.id.button1);
        etLoginName = (EditText)findViewById(R.id.editText1);
        etPassword = (EditText)findViewById(R.id.editText2);

       btLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                // need to be personalised (redirect to slotGame view)
                if (etLoginName.getText().toString().equals("admin") &&
                    etPassword.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"Redirection" , Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Wrong Credential", Toast.LENGTH_LONG ).show();

                }
            }
        });


    }*/
}
