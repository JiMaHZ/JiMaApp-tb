package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sc.parse.App;
import com.example.sc.parse.Data;
import com.example.sc.parse.Device;
import com.example.sc.parse.Value;
import com.example.sc.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class DeviceActivity extends AppCompatActivity {
    FragmentHostCallback mHost;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private List<String> datas = new ArrayList<String>();
    private List<String> images = new ArrayList<String>();
    public String value[] = new String[32];
    public String key[] = new String[32];
    public String category[] = new String[32];
    public String[] rg2 = new String[32];
    public String region1;
    public String[] Id = new String[32];
    public String name;
    public int e = 1, c = 0, a = 0, i2 = 0, i3 = 0;
    private String[] region2 = new String[32];
    public static final int UPDATE_TEXT = 1;
    private Handler handler = new Handler();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        //getSupportActionBar().hide();
        name = getIntent().getStringExtra("name");
        region1 = getIntent().getStringExtra("region1");
        Log.e("DeviceActivity", region1);
        int i1 = 1;
        final String[] Ids = getIntent().getStringExtra("Id").split(",");
        Id[0] = Ids[0].substring(1);
        for (int i = 1; i < 10; i++) {
            if (Ids[i].contains("null")) {

            } else {
                Id[i1] = Ids[i].substring(1);
                i1++;
            }

        }
        Log.e("DeviceActivity", Arrays.toString(Id));
        Log.e(",,,", name);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String token = pref.getString("token", "");
        final String[] region = {new String()};
        Log.e("DeviceActivity", token);
        ImageButton back = (ImageButton) findViewById(R.id.ic_back);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) findViewById(R.id.relv);


        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.red, R.color.orange);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ic_back:
                        DeviceActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

        });
        for (int ii = 0; ii < i1; ii++) {
            String url = App.baseURL + "/api/plugins/telemetry/DEVICE/" + Id[ii] + "/values/attributes/SERVER_SCOPE";
            Log.e("DeviceActivity", url);
            HttpUtil.sendOkHttpRequest(url, token, new okhttp3.Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseData = response.body().string();
                    Log.e("DeviceActivity", responseData);
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(responseData).getAsJsonArray();
                    int i = 0;
                    Log.e("DeviceActivity", "jsonarray is " + jsonArray.toString());
                    for (JsonElement user : jsonArray) {
                        try {
                            Device device = gson.fromJson(user, new TypeToken<Device>() {
                            }.getType());
                            Log.e("DeviceActivity", device.getValue());
                            value[i] = device.getValue();
                            key[i] = device.getKey();

                            Log.e("Device", key[i]);
                            Gson gson1 = new Gson();
                            Value value1 = gson1.fromJson(value[i], Value.class);
                            category[i] = value1.getCategory();
                            String[] rg = value1.getRegion().split("[|]");
                            rg2[i] = rg[1];
                            region2[0] = rg2[0];
                            Log.e("rg1", rg[1]);
                            if (region1.equals(rg[0]) && !rg[1].isEmpty()) {
                                for (int ii = 0, iii = 0; ii < i2; ii++) {
                                    if (rg[1].equals(region2[ii])) {
                                        ii = i2;
                                        i3 = 1;
                                    } else {
                                        iii++;
                                    }
                                    if (iii == i2) {
                                        region2[i2] = rg[1];
                                        i3 = 0;
                                    }
                                }
                                if (i3 == 0) {
                                    if (region2[i2].contains("大棚")) {
                                        images.add("dapeng");
                                    }
                                    datas.add(region2[i2]);
                                    Message message = new Message();
                                    message.what = UPDATE_TEXT;
                                    handler.sendMessage(message);
                                }
                                i2++;
                            }
                            i++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        //for (int i = 0; i < 5; i++) {
        //   datas.add("标题" + i);
        // }
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXT:
                        rvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
        rvAdapter = new RvAdapter(datas, images);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rvAdapter);


        rvAdapter.setItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onTextClick(View view, int position) {
                Intent intent = new Intent(DeviceActivity.this, Main2Activity.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Arrays.toString(Id));
                intent.putExtra("region1", region1);
                intent.putExtra("region2", datas.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(DeviceActivity.this, Main2Activity.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Arrays.toString(Id));
                intent.putExtra("region1", region1);
                intent.putExtra("region2", datas.get(position));
                /*intent.putExtra("e",e);
                intent.putExtra("c",c);
                intent.putExtra("a",a);
                int ie=0,ic=0,ia=0,i=0;
                for(;ie<e||ic<c||ia<a;) {
                    if(value[position+i].contains("sensor")) {
                        intent.putExtra("valuee" + ie, value[position + i]);
                        intent.putExtra("keye" + ie, key[position + i]);
                        //intent.putExtra("Ide", Id);
                        ie++;
                        i++;
                    }else if (value[position+i].contains("controller")){
                        intent.putExtra("valuec" + ic, value[position + i]);
                        intent.putExtra("keyc" + ic, key[position + i]);
                        //intent.putExtra("Idc", Id);
                        ic++;
                        i++;
                    }else if(value[position+i].contains("amperemeter")){
                        intent.putExtra("valuea" + ia, value[position + i]);
                        intent.putExtra("keya" + ia, key[position + i]);
                        //intent.putExtra("Ida", Id);
                        ia++;
                        i++;
                    }
                }
*/
                startActivity(intent);
            }
        });
    }
}
