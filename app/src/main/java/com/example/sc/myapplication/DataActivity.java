package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.sc.parse.App;
import com.example.sc.parse.Device;
import com.example.sc.parse.Value;
import com.example.sc.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.content.Context.MODE_PRIVATE;


public class DataActivity extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Rv2Cardview rvAdapter;
    private List<String> datas = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private String[] name = new String[700];
    private String[] type = new String[700];
    private String[] unit = new String[700];
    private String[] key = new String[700];
    private String[] ts = new String[700];
    private String[] time = new String[700];
    private String[] key12 = new String[700];
    private String[] name12 = new String[700];
    private String[] type12 = new String[700];
    private String[] unit12 = new String[700];
    private String devId = new String();
    private Handler handler = new Handler();
    JSONArray jsonArray1 = new JSONArray();
    public static final int UPDATE_TEXT = 1;
    private TextView tvOutput;
    private WebSocket mSocket;
    final static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
            .build();
    public String Id[] = new String[700];
    public String Idss[] = new String[700];
    public String value[] = new String[700];
    public String category[] = new String[700];
    public String[] strings = new String[700];
    public String region1, region2;
    int i1 = 1, i2 = 0, i = 0, i3 = 0, i4 = 0, i5 = 0, i6 = 0, i7 = 0, i8 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] Ids = getActivity().getIntent().getStringExtra("Id").split(",");
        Log.e("...", Ids[0]);
        region1 = getActivity().getIntent().getStringExtra("region1");
        region2 = getActivity().getIntent().getStringExtra("region2");
        Id[0] = Ids[0].substring(1);
        for (int i = 1; i < 10; i++) {
            if (Ids[i].contains("null")) {

            } else {
                Id[i1] = Ids[i].substring(1);
                i1++;
            }

        }
        Log.e("DeviceActivity", Id[0]);
        int e = getActivity().getIntent().getIntExtra("e", 0);
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        final String token = pref.getString("token", "");
        for (int ii = 0; ii < i1; ii++) {
            String url1 = App.baseURL + "/api/plugins/telemetry/DEVICE/" + Id[ii] + "/values/attributes/SERVER_SCOPE";
            Log.e("DeviceActivity", url1);
            HttpUtil.sendOkHttpRequest(url1, token, new okhttp3.Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.e("DeviceActivity", "l=" + responseData.length());
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(responseData).getAsJsonArray();
                    Log.e("DeviceActivity", jsonArray.toString());
                    for (JsonElement user : jsonArray) {
                        Device device = gson.fromJson(user, new TypeToken<Device>() {
                        }.getType());
                        //Log.e("DeviceActivity", device.getValue());
                        value[i] = device.getValue();
                        Gson gson1 = new Gson();
                        Value value1 = gson1.fromJson(value[i], Value.class);
                        category[i] = value1.getCategory();
                        String[] rg = value1.getRegion().split("[|]");
                        String rg1 = rg[0];
                        String rg2 = new String();
                        if (!value1.getRegion().contains("|")) {
                            rg2 = "default";
                        } else {
                            rg2 = rg[1];
                        }
                        if (rg1.equals(region1) && rg2.equals(region2) && category[i].equals("sensor")) {
                            String add = String.valueOf(call.request().url());
                            String[] devId = add.split("/");
                            for (int i = 0, ii = 0; i < i3; i++) {
                                if (devId[7].equals(Idss[i])) {
                                    i = i3;
                                } else {
                                    ii++;
                                }
                                if (ii == i3) {
                                    Idss[i3] = devId[7];
                                    i3++;
                                }
                            }
                            if (i3 == 0) {
                                i3++;
                                Idss[0] = devId[7];
                            }
                            name[i2] = value1.getName();
                            type[i2] = value1.getConfig().getType();
                            unit[i2] = value1.getConfig().getUnit();
                            key[i2] = device.getKey();
                            int key10 = Integer.parseInt(key[i2], 16);
                            int i111 = (key10 - 256) / 32;
                            int i222 = ((key10 - 256) % 32) / 4;
                            i5 = i111 * 8 + i222;
                            if (i5 > 511) {
                                i5 = i5 - 256;
                            }
                            name12[i5] = name[i2];
                            type12[i5] = type[i2];
                            unit12[i5] = unit[i2];
                            if (key[i2].equals("0100")) {
                                i7 = 1;
                            }
                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put(devId[7], key[i2]);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            //Log.e("jsonar", String.valueOf(jsonObject));
                            jsonArray1.put(jsonObject);
                            //Log.e("jsonar", String.valueOf(jsonArray1));
                            //Log.e("111", name[i2] + " " + type[i2] + " " + unit[i2]);
                            i2++;
                        }
                    }

                    i8 = 1;
                }

            });
            i++;
            for (int i = 0; i < 50; i++) {
                if (i8 == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }
        String url = App.baseWsURL + "/api/ws/plugins/telemetry?token=" + token;
        //tvOutput = (TextView) findViewById(R.id.output);
        Log.e("Data", url);
        /*for (int i = 0; i < e; i++) {
            String value[] = new String[10];
            value[i] = getActivity().getIntent().getStringExtra("valuee" + i);
            key[i] = getActivity().getIntent().getStringExtra("keye" + i);
            Gson gson = new Gson();
            Value value1 = gson.fromJson(value[i], Value.class);
            name[i] = value1.getName();
            Log.e("DataActivity", name[i]);
            type[i] = value1.getConfig().getType();
            Log.e("DataActivity", type[i]);
            unit[i] = value1.getConfig().getUnit();
            Log.e("DataActivity", unit[i]);
            Log.e("DataActivity", key[i]);
        }*/
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Thread.sleep(600);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        EchoWebSocketListener socketListener = new EchoWebSocketListener();

        //
        mOkHttpClient.newWebSocket(request, socketListener);
        if (DataActivity.this.getActivity().isDestroyed()) {
            mOkHttpClient.dispatcher().executorService().shutdown();
        }

    }


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_data, container,
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
                }, 3000);
            }
        });
        for (int i = 0; i < i2; i++) {
            title.add(" " + i);
            datas.add(" " + i);
            times.add(" " + i);

        }
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
        rvAdapter = new Rv2Cardview(title, datas, times);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataActivity.this.getActivity()));
        recyclerView.setAdapter(rvAdapter);
        rvAdapter.setItemClickListener(new Rv2Cardview.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onTextClick(View view, int position) {
            }
        });
        return view;
    }


    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            for (int ii = 0; ii < i3; ii++) {
                Log.e("i1", "i1 " + i3);
                Log.e("i1", "IDss " + Idss[ii]);
                mSocket = webSocket;
                //连接成功后，发送登录信息"{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"c70f09c0-3c4d-11e8-8280-65869ac1d365\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"c70f09c0-3c4d-11e8-8280-65869ac1d365\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}"
                final String message = "{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[ii] + "\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[ii] + "\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}";
                Log.e("Data", message);
                mSocket.send(message);

            }

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            output("receive bytes:" + bytes.hex());
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);
            Log.e("message", text);
            try {
                final String message = "{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[0] + "\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[0] + "\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}";

                if (text.contains("{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"")) {
                    String[] tx = text.split("\"");
                    devId = tx[9];
                    Log.e("devid", devId);
                }
                if (!text.contains("{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"") && !text.contains("\"errorCode\":0,\"errorMsg\":null,\"data\":{},\"latestValues\":{}") && !text.contains("{\"entityType\":\"DEVICE\",\"entityId\":\"null\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"null\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}")) {
                    Log.e("Data", "receive text:" + text);
                    //收到服务器端发送来的信息后，每隔60秒发送一次心跳包
                    Log.e("i2", "i2 " + i2);
                    ParseJSONWithJSONObject(text, devId);
                    for (int i = 0; i < i2; i++) {
                        //Log.e("Data",data.getData().getKey1()[i]);
                        //strings=data.getData().getKey();
                        //Log.e("Data", name[i] + " " + type[i] + " " + strings[i] + " " + unit[i]);
                        if (1 == 1) {
                            if (i7 == 1) {
                                title.set(i, (name12[i] + ""));
                                //type[i] + ":\n"
                                datas.set(i, (strings[i] + " " + unit12[i]));
                                times.set(i, (time[i]));
                            } else {
                                title.set(i, (name[i] + ""));
                                //type[i] + ":\n"
                                datas.set(i, (strings[i] + " " + unit[i]));
                                times.set(i, (time[i]));
                            }
                            Message message1 = new Message();
                            message1.what = UPDATE_TEXT;
                            handler.sendMessage(message1);
                        }
                    }
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mSocket.send(message);
                    }
                }, 300000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void ParseJSONWithJSONObject(String jsonData, String devId) {
            try {
                JSONObject json = new JSONObject(jsonData);
                JSONObject data = json.getJSONObject("data");
                String[] key1 = new String[700];
                Log.e("DATA", data.toString());
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    Log.e("jsonob", String.valueOf(jsonObject));
                    Log.e("jsonob", devId);
                    try {
                        key1[i] = jsonObject.getString(devId);
                        int key10 = Integer.parseInt(key1[i], 16);
                        int i111 = (key10 - 256) / 32;
                        int i222 = ((key10 - 256) % 32) / 4;
                        i6 = i111 * 8 + i222;
                        if (i6 > 511) {
                            i6 = i6 - 256;
                        }
                        key12[i6] = key1[i];
                        Log.e("key1[i]", key1[i]);
                        Log.e("ii", "i=" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (data.length() == 1) {
                    for (int i = i4; i < i2; i++) {
                        try {
                            String[] sd = new String[10];
                            if (i7 == 1) {
                                sd[i] = data.getString(key12[i]);
                            } else {
                                sd[i] = data.getString(key1[i]);
                            }
                            String[] t1 = sd[i].split(",");
                            ts[i] = t1[0].substring(2);
                            Long t = Long.valueOf(ts[i]);
                            Timestamp timestamp = new Timestamp(t);
                            String tsStr = "";
                            @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            try {
                                tsStr = sdf.format(timestamp);
                                time[i] = tsStr;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String[] sd1 = sd[i].split("\"");
                            strings[i] = sd1[1];
                            Log.e("DATA", sd1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                final int l = data.length();
                for (int i = i4; i < i2; i++) {
                    try {
                        String[] sd = new String[700];
                        if (i7 == 1) {
                            sd[i] = data.getString(key12[i]);
                        } else {
                            sd[i] = data.getString(key1[i]);
                        }

                        String[] t1 = sd[i].split(",");
                        ts[i] = t1[0].substring(2);
                        Long t = Long.valueOf(ts[i]);
                        Timestamp timestamp = new Timestamp(t);
                        String tsStr = "";
                        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        try {
                            tsStr = sdf.format(timestamp);
                            time[i] = tsStr;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String[] sd1 = sd[i].split("\"");
                        strings[i] = sd1[1];
                        Log.e("DATA", sd1[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i4++;
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            //  output("closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            output("closing:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            //   output("failure:" + t.getMessage());
        }
    }


    private void output(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tvOutput.setText(tvOutput.getText().toString() + "\n\n" + text);
            }
        });
    }


}


