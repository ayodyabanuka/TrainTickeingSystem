package com.example.tts;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tts.Database.MyDatabaseHelper;
import com.example.tts.Model.Client;
import com.example.tts.Utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    TextView name,email,nic,mobile,deactivate;

    String nicsqlite;
    Button editProfile,logout;

    MyDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profileName);
        nic = view.findViewById(R.id.profileNIC);
        mobile = view.findViewById(R.id.profileNumber);
        email = view.findViewById(R.id.profileEmail);
        deactivate = view.findViewById(R.id.profileDeactivate);
        editProfile = view.findViewById(R.id.editProfile);
        logout = view.findViewById(R.id.logout);
        storeDataInArrays(view.getContext());
        GetDatafromAPI();


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(getActivity());
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> call = ApiClient.getUserService().UserDeactivate(nicsqlite);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {

                            Toast.makeText(getContext(),
                                    "Deactivated!", Toast.LENGTH_SHORT).show();


                        } else {
//                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                        recreate(getActivity());
                    }



                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(getActivity(), "Error!111", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
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

                    Client itemList = response.body();
                    name.setText(itemList.getFirstName());
                    nic.setText(itemList.getNic());
                    mobile.setText(itemList.getPhone());
                    email.setText(itemList.getEmail());


                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getActivity(), "Error!!!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}