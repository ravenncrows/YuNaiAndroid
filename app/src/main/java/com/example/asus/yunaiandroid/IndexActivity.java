package com.example.asus.yunaiandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.yunaiandroid.action.UserAction;
import com.example.asus.yunaiandroid.entity.User;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndexActivity extends AppCompatActivity {
    public static final String API_URL = "http://192.168.1.34:8080";
    private static Retrofit retrofit = null;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
    public void createUser(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final UserAction.Caller userAction = retrofit.create(UserAction.Caller.class);

        EditText username = (EditText) findViewById(R.id.username_editor);
        if(username.getText().toString().equals("")){
            user = new User (getSaltString());}
        else{
            user = new User(username.getText().toString());}
                Call<User> call = userAction.createUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("Debug",response.body().toString());
                        User u = response.body();
                        Log.d("Debug", u.getName());
                        linkToMain();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Fail",t.getMessage());
                    }
                });


    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    private void linkToMain(){
        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
    }
}
