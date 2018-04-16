package com.example.sc.myapplication;



import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;




import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private EditText et_user;
    private EditText et_pwd;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        et_user=(EditText)findViewById(R.id.et_login_username);
        et_pwd=(EditText)findViewById(R.id.et_login_password);
        login=(Button)findViewById(R.id.bt_login);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_login:
                        actionLogin();
                        break;
                    default:
                        break;
                }
            }
        });

        }

    private void actionLogin() {
        String username = et_user.getText().toString();
        String password = et_pwd.getText().toString();

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        Log.e(TAG,"requestBody is"+jsonObject);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://140.143.23.199:8080/api/auth/login")
                            .header("Content-Type", "application/json")
                            .post(requestBody)
                            .build();
                    Response response=client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData=response.body().string();
                        Gson gson =new Gson();
                        Person person=gson.fromJson(responseData,Person.class);
                        Log.e(TAG,"responseData is "+responseData);
                        String token=person.token;
                        String refreshToken=person.refreshToken;
                        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString("token",token);
                        editor.putString("refreshToken",refreshToken);
                        editor.apply();
                        //Log.e(TAG, "token is "+person.token);
                        //Log.e(TAG, "refreshtoken is "+person.refreshToken);
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.finish();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public class Person{
        private String token;
        private String refreshToken;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRefreshtoken() {
            return refreshToken;
        }

        public void setRefreshtoken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

}

