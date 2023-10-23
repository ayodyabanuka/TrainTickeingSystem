package com.example.tts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tts.Database.MyDatabaseHelper;
import com.example.tts.Model.Client;
import com.example.tts.Utils.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    String nicsqlite;
    TextInputEditText nic,firstname,lastname,mobile,email,password;
    MyDatabaseHelper myDB;
    Button update;
    Client itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        storeDataInArrays(EditProfileActivity.this);
        nic = findViewById(R.id.nic);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        update = findViewById(R.id.updateButton);

        GetDatafromAPI();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    register();
            }
        });


    }

    private void register() {
        Client client = new Client();
        client.setFirstName(firstname.getText().toString());
        client.setLastName(lastname.getText().toString());
        client.setEmail(email.getText().toString());
        client.setPhone(mobile.getText().toString());
        client.setNic(itemList.getNic().toString());
        if(password.getText().toString().isEmpty()){
            client.setPassword(itemList.getPassword().toString());
        }else {
            client.setPassword(password.getText().toString());
        }
        client.setActive(itemList.getActive());

        Call<Client> registerResponseCall = ApiClient.getUserService().userUpdate(client,nicsqlite);
        registerResponseCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, @NonNull Response<Client> response) {

                if(response.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this,"Updated!", Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(EditProfileActivity.this,"Failed", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    void storeDataInArrays(Context context){
        myDB = new MyDatabaseHelper(context);
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Fail!", Toast.LENGTH_SHORT).show();
        }else{

            if(cursor.moveToFirst()){
                do{
                    nicsqlite = cursor.getString(1);
                }while(cursor.moveToNext());
            }

        }
    }
    void GetDatafromAPI(){
        Call<Client> call = ApiClient.getUserService().GetUserByID(nicsqlite);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {

                    itemList = response.body();

                    firstname.setText(itemList.getFirstName());
                    lastname.setText(itemList.getLastName());
                    email.setText(itemList.getEmail());
                    mobile.setText(itemList.getPhone());
                    nic.setText(nicsqlite);

                } else {
                    Toast.makeText(EditProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}