package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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

import com.example.sc.parse.App;
import com.example.sc.parse.Value;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.content.Context.MODE_PRIVATE;

public class ControlActivity extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Rv3Cardview rvAdapter;
    private Handler handler = new Handler();
    public static final int UPDATE_TEXT = 1;
    public String[] keyc = new String[10];
    private String[] namec = new String[10];
    private String[] typec = new String[10];
    private String[] namea = new String[10];
    public String[] keya = new String[10];
    public String[] unita = new String[10];
    public String[] devicetw = new String[10];
    public String[] devicete = new String[10];
    public String[] stringsc = new String[10];
    public String[] stringstw = new String[10];
    public String[] onstringste = new String[10];
    public String[] maxonstringste = new String[10];
    public String[] offstringste = new String[10];
    public String[] maxoffstringste = new String[10];
    public String[] maxstringstw = new String[10];
    public String[] control_addrtw = new String[10];
    public String[] control_addrte = new String[10];
    public String[] ontw = new String[10];
    public String[] stoptw = new String[10];
    public String[] cur_keytw = new String[10];
    public String[] max_cur_keytw = new String[10];
    public String[] onte = new String[10];
    public String[] stopte = new String[10];
    public String[] offte = new String[10];
    public String[] on_cur_keyte = new String[10];
    public String[] off_cur_keyte = new String[10];
    public String[] max_on_cur_keyte = new String[10];
    public String[] max_off_cur_keyte = new String[10];
    private List<String> datas = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> control_addrtw1 = new ArrayList<>();
    private List<String> control_addrte1 = new ArrayList<>();
    //private String[] title = new String[10];
    //private String[] datas =new String[10];
    int tw = 0, te = 0, ii = 2;
    public String Id;
    public String token;
    private WebSocket mSocket;
    final static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
            .build();


    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        token = pref.getString("token", "");
        Id = getActivity().getIntent().getStringExtra("Ide");
        String url = App.baseWsURL + "/api/ws/plugins/telemetry?token=" + token;
        Log.e(".", url);
        int c = getActivity().getIntent().getIntExtra("c", 0);
        int a = getActivity().getIntent().getIntExtra("a", 0);

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
        for (int i = 0; i < c; i++) {
            String valuec[] = new String[10];
            valuec[i] = getActivity().getIntent().getStringExtra("valuec" + i);
            keyc[i] = getActivity().getIntent().getStringExtra("keyc" + i);
            Gson gson = new Gson();
            Value valuec1 = gson.fromJson(valuec[i], Value.class);
            namec[i] = valuec1.getName();
            Log.e("ControlActivity", namec[i]);
            typec[i] = valuec1.getConfig().getType();
            Log.e("ControlActivity", typec[i]);
            Log.e("ControlActivity", keyc[i]);
            if (typec[i].equals("两态可控")) {
                devicetw[tw] = namec[i];
                control_addrtw[tw] = valuec1.getConfig().getControl_addr();
                ontw[tw] = valuec1.getConfig().getOn();
                stoptw[tw] = valuec1.getConfig().getStop();
                cur_keytw[tw] = valuec1.getConfig().getCur_key();
                max_cur_keytw[tw] = valuec1.getConfig().getMax_cur_key();
                tw++;
            } else if (typec[i].equals("三态可控")) {
                devicete[te] = namec[i];
                control_addrte[te] = valuec1.getConfig().getControl_addr();
                Log.e("control", valuec1.getConfig().getOn());
                //onte[te]=valuec1.getConfig().getOn();
                offte[te] = valuec1.getConfig().getOff();
                stopte[te] = valuec1.getConfig().getStop();
                on_cur_keyte[te] = valuec1.getConfig().getOn_cur_key();
                off_cur_keyte[te] = valuec1.getConfig().getOff_cur_key();
                max_on_cur_keyte[te] = valuec1.getConfig().getMax_on_cur_key();
                max_off_cur_keyte[te] = valuec1.getConfig().getMax_off_cur_key();
                te++;

            }
        }
        for (int i = 0; i < te + tw; i++) {
            title.add(" " + i);
            datas.add(" " + i);

        }
        for (int i = 0; i < a; i++) {
            String valuea[] = new String[10];
            valuea[i] = getActivity().getIntent().getStringExtra("valuea" + i);
            keya[i] = getActivity().getIntent().getStringExtra("keya" + i);
            Gson gson = new Gson();
            Value valuea1 = gson.fromJson(valuea[i], Value.class);
            namea[i] = valuea1.getName();
            unita[i] = valuea1.getConfig().getUnit();
            Log.e("DataActivity", unita[i]);
            Log.e("ControlActivity", namea[i]);
            Log.e("ControlActivity", keya[i]);
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        ControlActivity.EchoWebSocketListener socketListener = new ControlActivity.EchoWebSocketListener();

        //
        mOkHttpClient.newWebSocket(request, socketListener);
        if (ControlActivity.this.getActivity().isDestroyed()) {
            mOkHttpClient.dispatcher().executorService().shutdown();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_control, container,
                false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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
        rvAdapter = new Rv3Cardview(ii, datas, title, control_addrtw1, control_addrte1, Id, token);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(rvAdapter);

        return view;
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;
            //连接成功后，发送登录信息"{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"1d9c28a0-3b17-11e8-800a-65869ac1d365\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"1d9c28a0-3b17-11e8-800a-65869ac1d365\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}
            final String message = "{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Id + "\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Id + "\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}";
            Log.e(".", message);
            mSocket.send(message);
            //output("连接成功！");

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            //output("receive bytes:" + bytes.hex());
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);
            final String message = "{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Id + "\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Id + "\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}";
            if (!text.equals(message) && !text.contains("{\"subscriptionId\":1,\"errorCode\":0,\"errorMsg\":null,\"data\":{},\"latestValues\":{}}")) {
                Log.e("Data", "receive text:" + text);
                //收到服务器端发送来的信息后，每隔60秒发送一次心跳包
                Gson gson = new Gson();
                int a = getActivity().getIntent().getIntExtra("a", 0);
                ParseJSONWithJSONObject(text);
                for (int i = 0; i < tw; i++) {
                    //Log.e("Data",data.getData().getKey1()[i]);
                    //strings=data.getData().getKey();
                    Log.e("Control", devicetw[i] + " " + "当前电流 " + stringstw[i] + " " + unita[i]);
                    Log.e("Control", devicetw[i] + " " + "最大电流 " + maxstringstw[i] + " " + unita[i]);
                    title.set(i, (devicetw[i]));
                    datas.set(i, ("当前电流 " + stringstw[i] + " " + unita[i] + "\n" + "最大电流 " + maxstringstw[i] + " " + unita[i]));
                    control_addrtw1.add(control_addrtw[i]);
                    ii = 1;
                    Message message1 = new Message();
                    message1.what = UPDATE_TEXT;
                    handler.sendMessage(message1);
                    //title.add(devicetw[i]);
                    //datas.set(i, (name[i] + " " + type[i] + " " + strings[i] + " " + unit[i]));
                }
                for (int i = tw; i < te + tw; i++) {
                    Log.e("Control", devicete[i - tw] + " " + "正转当前电流 " + onstringste[i - tw] + " " + unita[i - tw]);
                    Log.e("Control", devicete[i - tw] + " " + "正转最大电流 " + maxonstringste[i - tw] + " " + unita[i - tw]);
                    Log.e("Control", devicete[i - tw] + " " + "反转当前电流 " + offstringste[i - tw] + " " + unita[i - tw]);
                    Log.e("Control", devicete[i - tw] + " " + "反转最大电流 " + maxoffstringste[i - tw] + " " + unita[i - tw]);
                    title.set(i, (devicete[i - tw]));
                    datas.set(i, ("正转当前电流 " + onstringste[i - tw] + " " + unita[i - tw] + "\n" + "正转最大电流 " + maxonstringste[i - tw] + " " + unita[i - tw] + "\n" + "反转当前电流 " + offstringste[i - tw] + " " + unita[i - tw] + "\n" + "反转最大电流 " + maxoffstringste[i - tw] + " " + unita[i - tw]));
                    control_addrte1.add(control_addrte[i - tw]);
                    ii = 0;
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mSocket.send(message);
                    }
                }, 300000);
            }
        }

        private void ParseJSONWithJSONObject(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                JSONObject data = json.getJSONObject("data");
                Log.e("Control", data.toString());
                if (data.length() == 1) {
                    for (int i = 0; i < tw; i++) {
                        try {
                            String[] sdtw = new String[10];
                            sdtw[i] = data.getString(cur_keytw[i]);
                            String[] sdtw1 = sdtw[i].split("\"");
                            stringstw[i] = sdtw1[1];
                            Log.e("Control", sdtw1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < tw; i++) {
                        try {
                            String[] sdtw = new String[10];
                            sdtw[i] = data.getString(max_cur_keytw[i]);
                            String[] sdtw1 = sdtw[i].split("\"");
                            maxstringstw[i] = sdtw1[1];
                            Log.e("Control", sdtw1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < te; i++) {
                        try {
                            String[] sdte = new String[10];
                            sdte[i] = data.getString(on_cur_keyte[i]);
                            String[] sdte1 = sdte[i].split("\"");
                            onstringste[i] = sdte1[1];
                            Log.e("Control", sdte1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < te; i++) {
                        try {
                            String[] sdte = new String[10];
                            sdte[i] = data.getString(max_on_cur_keyte[i]);
                            String[] sdte1 = sdte[i].split("\"");
                            maxonstringste[i] = sdte1[1];
                            Log.e("Control", sdte1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < te; i++) {
                        try {
                            String[] sdte = new String[10];
                            sdte[i] = data.getString(off_cur_keyte[i]);
                            String[] sdte1 = sdte[i].split("\"");
                            offstringste[i] = sdte1[1];
                            Log.e("Control", sdte1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < te; i++) {
                        try {
                            String[] sdte = new String[10];
                            sdte[i] = data.getString(max_off_cur_keyte[i]);
                            String[] sdte1 = sdte[i].split("\"");
                            maxoffstringste[i] = sdte1[1];
                            Log.e("Control", sdte1[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (int i = 0; i < tw; i++) {
                    String[] sdtw = new String[10];
                    sdtw[i] = data.getString(cur_keytw[i]);
                    String[] sdtw1 = sdtw[i].split("\"");
                    stringstw[i] = sdtw1[1];
                    Log.e("Control", sdtw1[1]);

                }
                for (int i = 0; i < tw; i++) {
                    String[] sdtw = new String[10];
                    sdtw[i] = data.getString(max_cur_keytw[i]);
                    String[] sdtw1 = sdtw[i].split("\"");
                    maxstringstw[i] = sdtw1[1];
                    Log.e("Control", sdtw1[1]);
                }
                for (int i = 0; i < te; i++) {
                    String[] sdte = new String[10];
                    sdte[i] = data.getString(on_cur_keyte[i]);
                    String[] sdte1 = sdte[i].split("\"");
                    onstringste[i] = sdte1[1];
                    Log.e("Control", sdte1[1]);
                }
                for (int i = 0; i < te; i++) {
                    String[] sdte = new String[10];
                    sdte[i] = data.getString(max_on_cur_keyte[i]);
                    String[] sdte1 = sdte[i].split("\"");
                    maxonstringste[i] = sdte1[1];
                    Log.e("Control", sdte1[1]);
                }
                for (int i = 0; i < te; i++) {
                    String[] sdte = new String[10];
                    sdte[i] = data.getString(off_cur_keyte[i]);
                    String[] sdte1 = sdte[i].split("\"");
                    offstringste[i] = sdte1[1];
                    Log.e("Control", sdte1[1]);
                }
                for (int i = 0; i < te; i++) {
                    String[] sdte = new String[10];
                    sdte[i] = data.getString(max_off_cur_keyte[i]);
                    String[] sdte1 = sdte[i].split("\"");
                    maxoffstringste[i] = sdte1[1];
                    Log.e("Control", sdte1[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            //  output("closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            //output("closing:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            //   output("failure:" + t.getMessage());
        }
    }
}
