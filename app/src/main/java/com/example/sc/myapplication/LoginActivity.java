package com.example.sc.myapplication;



import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private SharedPreferences rememberpref;
    private SharedPreferences.Editor remembereditor;
    public static final String TAG = "LoginActivity";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private EditText et_user;
    private EditText et_pwd;
    private Button login;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rememberpref= PreferenceManager.getDefaultSharedPreferences(this);
        et_user=(EditText)findViewById(R.id.et_login_username);
        et_pwd=(EditText)findViewById(R.id.et_login_password);
        login=(Button)findViewById(R.id.bt_login);
        rememberPass=(CheckBox) findViewById(R.id.remember_pass);
        boolean isRember=rememberpref.getBoolean("remember_pass",false);
        if(isRember){
            String account=rememberpref.getString("account","");
            String password=rememberpref.getString("password","");
            et_user.setText(account);
            et_pwd.setText(password);
            rememberPass.setChecked(true);
        }
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
        final String username = et_user.getText().toString();
        final String password = et_pwd.getText().toString();

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
                    String responseData=response.body().string();
                    if (response.isSuccessful()) {
                        remembereditor=rememberpref.edit();
                        if(rememberPass.isChecked()){
                            remembereditor.putBoolean("remember_pass",true);
                            remembereditor.putString("account",username);
                            remembereditor.putString("password",password);
                        }else {
                            remembereditor.clear();
                        }
                        remembereditor.apply();
                        Gson gson =new Gson();
                        Person person=gson.fromJson(responseData,Person.class);
                        String token=person.token;
                        String refreshToken=person.refreshToken;
                        Log.e(TAG,token);
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

