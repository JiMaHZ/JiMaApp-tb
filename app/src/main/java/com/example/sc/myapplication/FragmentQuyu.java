package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.sc.parse.CustomerId;
import com.example.sc.parse.Data;
import com.example.sc.parse.Device;
import com.example.sc.parse.Value;
import com.example.sc.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sc.myapplication.FragmentShezhi.UPDATE_TEXT;

public class FragmentQuyu extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RvAdapter rvAdapter;
    public static final int UPDATE_TEXT=1;
    private List<String> datas = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private SharedPreferences.Editor editor;
    public static final String TAG = "FragmentQuyu";
    public String name[]=new String[32];
    public String names[]=new String[10];
    public String namess[]=new String[10];
    public String Ids[]=new String[10];
    public String json[]=new String[10];
    public String Id[]=new String[32];
    private Handler handler=new Handler();
    public String value[]=new String[100];
    private String[] region=new String[32];
    private String[] region1=new String[32];
    private String[] region2=new String[32];
    private String[] regions1=new String[32];
    private String[] regions2=new String[32];
    private String[] dvdata=new String[32];
    private String rg[]=new String[32];
    public int e=1,c=0,a=0,b=0;
    public int i = 0,i1=0,i2=0,i3=0,i4=0;

    JSONArray jArray2 = new JSONArray();
    JSONArray jArray3 = new JSONArray();
    JSONObject jsonObject1=new JSONObject();
    JSONObject jsonObject2=new JSONObject();
    JSONObject jsonObject3=new JSONObject();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String useraddress = "http://140.143.23.199:8080/api/auth/user";
        final String device = "http://140.143.23.199:8080/api/customer/";
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        final String token = pref.getString("token", "");
        HttpUtil.sendOkHttpRequest(useraddress, token, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.e(TAG, responseData);
                try {
                    Gson gson = new Gson();
                    CustomerId customerId = gson.fromJson(responseData, CustomerId.class);
                    final String id = customerId.getCustomerId().id;
                    Log.e(TAG, id);
                    String url = device + id + "/devices?limit=50";
                    HttpUtil.sendOkHttpRequest(url, token, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData1 = response.body().string();
                            Log.e(TAG, responseData1);
                            JsonObject jsonObject = new JsonParser().parse(responseData1).getAsJsonObject();
                            final JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                            Log.e(TAG, "jsonarray is " + jsonArray.toString());
                            Gson gson1 = new Gson();

                            for (JsonElement user : jsonArray) {
                                Data data = gson1.fromJson(user, new TypeToken<Data>() {
                                }.getType());
                                Log.e(TAG, data.getName() + "," + data.getId().getId());
                                //name[i] = data.getName();
                                //Id[i] = data.getId().getId();

                                String url1 = "http://140.143.23.199:8080/api/plugins/telemetry/DEVICE/" + data.getId().getId() + "/values/attributes/SERVER_SCOPE";

                                HttpUtil.sendOkHttpRequest(url1, token, new okhttp3.Callback() {

                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseData = response.body().string();
                                        Log.e(TAG, "responseData " + responseData.length());
                                        if (responseData.length() == 2) {
                                        } else {
                                            String add = String.valueOf(call.request().url());
                                            String[] devId = add.split("/");
                                            Log.e(TAG, devId[7]);
                                            Id[i2] = devId[7];
                                            Gson gson = new Gson();
                                            JsonParser parser = new JsonParser();
                                            JsonArray jsonArray = parser.parse(responseData).getAsJsonArray();
                                            Log.e(TAG, "jsonarray is " + jsonArray.toString());
                                            for (JsonElement user : jsonArray) {
                                                try {
                                                    Device device = gson.fromJson(user, new TypeToken<Device>() {
                                                    }.getType());
                                                    value[i2] = device.getValue();
                                                    Gson gson1 = new Gson();
                                                    Value value1 = gson1.fromJson(value[i2], Value.class);
                                                    region[i2] = value1.getRegion();
                                                    String[] rg1 = region[i2].split("[|]");
                                                    region2[i2] = rg1[1];
                                                    name[i2] = value1.getName();
                                                    region1[0] = rg1[0];
                                                    for (int i = 0, ii = 0; i < i2; i++) {
                                                        if (rg1[0].equals(region1[i])) {
                                                            i = i2;
                                                            i3 = 1;
                                                        } else {
                                                            ii++;
                                                        }
                                                        if (ii == i2) {
                                                            region1[i2] = rg1[0];
                                                            i3 = 0;
                                                        }
                                                    }
                                                    if (i3 == 0) {
                                                        datas.add(region1[i2]);
                                                        if (region1[i2].contains("区")) {
                                                            images.add("putao");
                                                        }
                                                        Message message = new Message();
                                                        message.what = UPDATE_TEXT;
                                                        handler.sendMessage(message);
                                                    }
                                                    Log.e(TAG, "region[i2] " + Arrays.toString(region1));
                                                    Log.e(TAG, "name[i2] " + name[i2]);
                                                    Log.e(TAG, "Id[i1] " + Id[i1]);
                                                    i2++;
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

          /*                                  names[0]=region[0];
                                            Ids[0]=Id[0];
                                            int i3=0;
                                            for (int ii=0;ii<i4;ii++){
                                                if(region[i4].equals(region[ii])){
                                                    Ids[ii]=Ids[ii]+","+Id[i4];
                                                    Log.e(TAG,"Ids "+ii+ Ids[ii]);
                                                    ii=i4;
                                                    i2++;
                                                    i3=1;
                                                }else {
                                                    i3=0;
                                                }
                                            }
                                            names[i1-i2]=region[i1];
                                            Log.e(TAG,"names"+ Arrays.toString(names));
                                            Log.e(TAG,"Ids"+ Arrays.toString(Ids));
                                            if(i3==0){
                                                String[] nm1=names[i1 - i2].split("[|]");
                                                rg[i1-i2]=nm1[0];
                                                datas.add(rg[i1-i2]);
                                                if (names[i1 - i2].contains("区")) {
                                                    images.add("putao");
                                                }
                                                Message message = new Message();
                                                message.what = UPDATE_TEXT;
                                                handler.sendMessage(message);
                                            }
                                            i1++;
*/
                                            //int i2=0;
                                                /*JSONArray jArray1 = new JSONArray();
                                                for (JsonElement user : jsonArray) {
                                                    Device device = gson.fromJson(user, new TypeToken<Device>() {
                                                    }.getType());
                                                    value[i2] = device.getValue();
                                                    Gson gson1 = new Gson();
                                                    Value value1 = gson1.fromJson(value[i2], Value.class);
                                                    category[i2] = value1.getCategory();
                                                        region[0] = value1.getRegion();
                                                        names[i2]=value1.getName();
                                                        String[] sd1 = region[0].split("[|]");
                                                        rg[i2]=sd1[0];
                                                        datas.add(sd1[0]);
                                                        if(sd1[0].contains("区"))
                                                        {images.add("putao");}
                                                        Message message = new Message();
                                                        message.what = UPDATE_TEXT;
                                                        handler.sendMessage(message);
                                                        i2++;
                                                }
                                                try {
                                                    jsonObject1.put(Id[i1],jArray1);
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }Log.e(TAG,"jb1 "+jsonObject1);*/
                                            //String[] name = json[i1].split("\"");
                                            //String name1=name[1];
                                            //String[] name2=name1.split("[|]");
                                            //namess[i1]=name2[0];
                                        }

                                    }
                                });
                                i++;
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_quyu, container,
                false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.relv);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.red, R.color.orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        //for (int i = 0; i < 5; i++) {
            //datas.add("标题" + i);
        //}
        //Log.e(TAG+"1", datas.toString());
        handler=new Handler(){
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE_TEXT:

                        rvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
        rvAdapter = new RvAdapter(datas,images);
        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentQuyu.this.getActivity()));
        recyclerView.setAdapter(rvAdapter);

        rvAdapter.setItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onTextClick(View view, int position) {
                Intent intent=new Intent(FragmentQuyu.this.getActivity(),DeviceActivity.class);
                intent.putExtra("region1", datas.get(position));
                intent.putExtra("Id",Arrays.toString(Id));
                intent.putExtra("region2",Arrays.toString(region2));
                intent.putExtra("name",Arrays.toString(name));
                startActivity(intent);

            }

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(FragmentQuyu.this.getActivity(),DeviceActivity.class);
                intent.putExtra("region1", datas.get(position));
                intent.putExtra("Id",Arrays.toString(Id));
                intent.putExtra("region2",Arrays.toString(region2));
                intent.putExtra("name",Arrays.toString(name));
                startActivity(intent);
            }
        });

        return view;
    }
}
