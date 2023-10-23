package com.example.tts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tts.Model.Client;
import com.example.tts.Utils.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    Button Register;
    TextView LoginHere;
    TextInputEditText email,nic,firstName,lastName,password,contactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginHere = findViewById(R.id.loginHere);
        Register =findViewById(R.id.registerButton);
        nic = findViewById(R.id.nicRegister);
        firstName = findViewById(R.id.firstnameRegister);
        lastName = findViewById(R.id.lastnameRegister);
        password = findViewById(R.id.passwordRegister);
        contactNo = findViewById(R.id.contactNoRegister);
        email = findViewById(R.id.emailRegister);


        LoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || nic.getText().toString().equals("") || contactNo.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }else{

                    register();

                }
            }
        });

    }

    private void register() {
        Client client = new Client();
        client.setFirstName(firstName.getText().toString());
        client.setLastName(lastName.getText().toString());
        client.setNic(nic.getText().toString());
        client.setPhone(contactNo.getText().toString());
        client.setPassword(password.getText().toString());
        client.setEmail(email.getText().toString());
        client.setActive(true);

        Call<Client> registerResponseCall = ApiClient.getUserService().userRegister(client);
        registerResponseCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, @NonNull Response<Client> response) {

                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Register Successful", Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                    },700);

                }else{
                    Toast.makeText(RegisterActivity.this,"Login Failed", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}