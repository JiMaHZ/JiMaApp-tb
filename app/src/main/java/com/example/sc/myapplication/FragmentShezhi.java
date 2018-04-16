package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.sc.parse.Customer;
import com.example.sc.parse.CustomerId;
import com.example.sc.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentShezhi extends Fragment {
    private TextView shezhi_tv;
    public static final String TAG = "MainActivity";
    public static final int UPDATE_TEXT=1;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText customername;
    private EditText customerphone;
    private EditText customeremail;
    private Button exit;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    customername.setText("用户 "+pref.getString("name",""));
                    customeremail.setText("邮箱 "+pref.getString("email",""));
                    customerphone.setText("电话 "+pref.getString("phone",""));
                    break;
                    default:
                        break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String useraddress="http://140.143.23.199:8080/api/auth/user";
        final String customeraddress="http://140.143.23.199:8080/api/customer/";
        pref=getContext().getSharedPreferences("data",MODE_PRIVATE);
        final String token=pref.getString("token","");
        Log.e(TAG,"token is "+token);
        HttpUtil.sendOkHttpRequest(useraddress,token,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response)throws IOException{
                String responseData =response.body().string();
                Log.e(TAG, responseData);
                try {
                    Gson gson=new Gson();
                    CustomerId customerId=gson.fromJson(responseData,CustomerId.class);
                    String id = customerId.getCustomerId().id;
                    Log.e(TAG,id);
                    String url=customeraddress+id;
                    Log.e(TAG,url);
                    HttpUtil.sendOkHttpRequest(url,token,new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response)throws IOException {
                            String responseData1 = response.body().string();
                            Log.e(TAG, responseData1);
                            Gson gson1=new Gson();
                            Customer customer=gson1.fromJson(responseData1,Customer.class);
                            String name=customer.getName();
                            String phone=customer.getPhone();
                            String email=customer.getEmail();
                            editor=pref.edit();
                            editor.putString("name",name);
                            editor.putString("phone",phone);
                            editor.putString("email",email);
                            editor.apply();
                            Message message=new Message();
                            message.what=UPDATE_TEXT;
                            handler.sendMessage(message);
                            Log.e(TAG,name+phone+email);

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_fragment_shezhi, container,
                false);
        customername=(EditText) view.findViewById(R.id.et_yonghu);
        customerphone=(EditText) view.findViewById(R.id.et_dianhua);
        customeremail=(EditText)view.findViewById(R.id.et_youxiang);
        exit=(Button)view.findViewById(R.id.btn_tuichu);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_tuichu:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(FragmentShezhi.this.getActivity());
                        dialog.setTitle("退出登录");
                        dialog.setMessage("是否退出登录");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(FragmentShezhi.this.getActivity(),LoginActivity.class);
                                startActivity(intent);
                                FragmentShezhi.this.getActivity().finish();
                            }
                        });
                        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                        break;
                        default:
                            break;
                }
            }
        });

        return view;
    }

}


