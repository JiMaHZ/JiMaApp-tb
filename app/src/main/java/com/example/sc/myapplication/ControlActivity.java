package com.example.sc.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sc.parse.App;
import com.example.sc.parse.Device;
import com.example.sc.parse.DeviceLocation;
import com.example.sc.parse.IniStatus;
import com.example.sc.parse.Item;
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
import java.util.ArrayList;
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


public class ControlActivity extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Rv3Cardview rvAdapter;
    private List<String> datas = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private List<String> control_addrtw = new ArrayList<>();
    private List<String> control_addrte = new ArrayList<>();
//    public List<String> check_values = new ArrayList<>();
    public List<String> button_values = new ArrayList<>();
    private List<DeviceLocation> DeviceLocationList = new ArrayList<>();

    public int[] i0 = new int[700];
    public List<Item> icardList = new ArrayList<>();
    public List<IniStatus> statuskeyList = new ArrayList<>();

    // public Item icard =new Item();
    public int[] i012 = new int[700];
    public String Id0;
    public String token;
    public String[] keyI = new String[700];
    public String[] unitI = new String[700];
    //    public String[] ref_item = new String[700];
    public String temp;
    public int t;
    public int tw = 0, te = 0;

    private String[] name = new String[700];
    private String[] type = new String[700];
    private String[] unit = new String[700];
    private String[] key = new String[700];
    private String[] ts = new String[700];
    private String[] time = new String[700];
    private String[] deviceid = new String[700];
    private String[] key12 = new String[700];
    private String[] name12 = new String[700];
    private String[] type12 = new String[700];
    private String[] deviceid12 = new String[700];
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
    public String[] strings2 = new String[700];
    public String[] strings3 = new String[700];
    public String[] strings4 = new String[700];
    public String[] cur_keytw = new String[700];
    public String[] max_cur_keytw = new String[700];
    public String[] on_cur_keyte = new String[700];
    public String[] off_cur_keyte = new String[700];
    public String[] max_on_cur_keyte = new String[700];
    public String[] max_off_cur_keyte = new String[700];

    public String[] cur_key = new String[700];
    public String[] max_cur_key = new String[700];
    public String[] on_cur_key = new String[700];
    public String[] off_cur_key = new String[700];
    public String[] max_on_cur_key = new String[700];
    public String[] max_off_cur_key = new String[700];

    public String[] button_value = new String[700];



    private Button btn1=null;
    private Button btn2=null;
    private Button btn3=null;

    public boolean b1 = true;
    public boolean b2 = true;
    public boolean b3 = true;


    public String region1, region2;
    int i1 = 1, i = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0, i6 = 0, i7 = 0, i8 = 0;
    int iI = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] Ids = getActivity().getIntent().getStringExtra("Id").split(",");
        Log.e("Ids[0]", Ids[0]);
        region1 = getActivity().getIntent().getStringExtra("region1");
        region2 = getActivity().getIntent().getStringExtra("region2");
        Id[0] = Ids[0].substring(1);
        Log.e("id[0]", Id[0]);
        for (int i = 1; i < 10; i++) {
            if (Ids[i].contains("null")) {

            } else {
                Id[i1] = Ids[i].substring(1);
//                Log.e("id[i]",Id[i1]);
                i1++;       //确定DEVICE的个数
            }

        }
        Log.e("DeviceActivity", Id[0]);
        int e = getActivity().getIntent().getIntExtra("e", 0);
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        token = pref.getString("token", "");
//        token0 = token;
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
                        Log.e("DeviceActivity", device.getValue());
                        value[i] = device.getValue();
                        Gson gson1 = new Gson();
                        Value value1 = gson1.fromJson(value[i], Value.class);
                        category[i] = value1.getCategory();
                        String[] rg = value1.getRegion().split("[|]");
                        String rg1 = rg[0];
                        String rg2 = new String();
                        if (!value1.getRegion().contains("|")) {
                            rg2 = "default";                                //什么意思？
                        } else {
                            rg2 = rg[1];
                        }
                        if (rg1.equals(region1) && rg2.equals(region2) && category[i].equals("controller")) {
                            String add = String.valueOf(call.request().url());        //???
                            String[] devId = add.split("/");
                            Log.e("add", add);
                            Log.e("devId[7]:", devId[7]);
                            deviceid[i2] = devId[7];    //之后用于控制命令（未排序）
                            for (int i = 0, ii = 0; i < i3; i++) {
                                if (devId[7].equals(Idss[i])) {
                                    i = i3;
                                } else {
                                    ii++;
                                }
                                if (ii == i3) {
                                    Idss[i3] = devId[7];
                                    i3++;                  //不同deviceId的个数
                                }
//                                Log.e("Idss[i]",Idss[i3]);
                            }
                            if (i3 == 0) {
                                i3++;
                                Idss[0] = devId[7];       //deviceId的数组
                                Log.e("Idss[i]", Idss[0]);
                            }
                            key[i2] = device.getKey().substring(1);
                            Log.e("KEY", key[i2]);
                            name[i2] = value1.getName();
//                            Log.e("NAME",name[i2]);
                            type[i2] = value1.getConfig().getType();
//                            Log.e("TYPE",type[i2]);
                            //unit[i2] = value1.getConfig().getUnit();
                            if (type[i2].equals("两态可控")) {
                                i0[i2] = 0;
                                tw++;       //记录风机个数
                                cur_keytw[i2] = value1.getConfig().getCur_key().substring(1);
                                max_cur_keytw[i2] = value1.getConfig().getMax_cur_key().substring(1);
                            } else if (type[i2].equals("三态可控")) {
                                i0[i2] = 1;
                                te++;       //记录卷膜个数
                                on_cur_keyte[i2] = value1.getConfig().getOn_cur_key().substring(1);
                                off_cur_keyte[i2] = value1.getConfig().getOff_cur_key().substring(1);
                                max_on_cur_keyte[i2] = value1.getConfig().getMax_on_cur_key().substring(1);
                                max_off_cur_keyte[i2] = value1.getConfig().getMax_off_cur_key().substring(1);
                            }  //取得控制器下对应电机的key值

//                            int key10= Integer.parseInt(key[i2],16);
//
//                            int i111=(key10-256)/32;
//                            int i222=((key10-256)%32)/4;
//                            i5=i111*8+i222;
//                            if(i5>511){
//                                i5=i5-256;
//                            }
//                            name12[i5]=name[i2];
//                            type12[i5]=type[i2];
//                            unit12[i5]=unit[i2];
//                            if (key[i2].equals("2100")){   //在起始位置为"2100"时，才有序排列
//                                i7=1;
//                            }                    //排序
                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put(devId[7], key[i2]);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            Log.e("jsonObject", String.valueOf(jsonObject));
                            jsonArray1.put(jsonObject);
                            Log.e("jsonar", String.valueOf(jsonArray1));
                            //Log.e("111", name[i2] + " " + type[i2] + " " + unit[i2]);
                            i2++;   //记录控制器个数
                        }
                        //获取电机key值和单位
//                        if  (rg1.equals(region1) && rg2.equals(region2) && category[i].equals("amperemeter")){
//                            keyI[iI] = device.getKey();
////                            Log.e("KEYI",keyI[iI]);
//                            unitI[iI] = value1.getConfig().getUnit();
////                            Log.e("UNITI",unitI[iI]);
//                            iI++;    //记录电机个数
//                        }

                    }
                    //复制数组，每组i2个数据
                    System.arraycopy(key, 0, key12, 0, i2);
                    System.arraycopy(deviceid, 0, deviceid12, 0, i2);
                    System.arraycopy(name, 0, name12, 0, i2);
                    System.arraycopy(type, 0, type12, 0, i2);
                    System.arraycopy(i0, 0, i012, 0, i2);
                    System.arraycopy(cur_keytw, 0, cur_key, 0, i2);
                    System.arraycopy(max_cur_keytw, 0, max_cur_key, 0, i2);
                    System.arraycopy(on_cur_keyte, 0, on_cur_key, 0, i2);
                    System.arraycopy(off_cur_keyte, 0, off_cur_key, 0, i2);
                    System.arraycopy(max_on_cur_keyte, 0, max_on_cur_key, 0, i2);
                    System.arraycopy(max_off_cur_keyte, 0, max_off_cur_key, 0, i2);
//                    for (int i = 0; i < i2; i++) {
//                        Log.e("key", key12[i]);
//                        Log.e("name", name12[i]);
//                        Log.e("type", type12[i]);
//                        if (i0[i] == 0) {
//                            Log.e("cur_keytw[i]", cur_keytw[i]);
//                        } else if (i0[i] == 1) {
//                            Log.e("on_cur_keyte[i]", on_cur_keyte[i]);
//                        }
//                    }

                    // 进行冒泡从小到大排列 key12、name12、type12、i012
                    for (int i = 0; i < i2 - 1; i++) {
                        for (int j = i; j < i2; j++) {
                            if (Integer.parseInt(key12[i], 16) > Integer.parseInt(key12[j], 16)) {
                                temp = key12[i];
                                key12[i] = key12[j];
                                key12[j] = temp;
                                temp = name12[i];
                                name12[i] = name12[j];
                                name12[j] = temp;
                                temp = type12[i];
                                type12[i] = type12[j];
                                type12[j] = temp;
                                temp = deviceid12[i];
                                deviceid12[i] = deviceid12[j];
                                deviceid12[j] = temp;

                                if (i012[i] + i012[j] == 1 && i012[i] == 0) {
                                    cur_key[j] = cur_key[i];
                                    max_cur_key[j] = max_cur_key[i];
                                    on_cur_key[i] = on_cur_key[j];
                                    off_cur_key[i] = off_cur_key[j];
                                    max_on_cur_key[i] = max_on_cur_key[j];
                                    max_off_cur_key[i] = max_off_cur_key[j];
                                    cur_key[i] = "";
                                    max_cur_key[i] = "";
                                    on_cur_key[j] = "";
                                    off_cur_key[j] = "";
                                    max_on_cur_key[j] = "";
                                    max_off_cur_key[j] = "";
                                } else if (i012[i] + i012[j] == 1 && i012[i] == 1) {
                                    cur_key[i] = cur_key[j];
                                    max_cur_key[i] = max_cur_key[j];
                                    on_cur_key[j] = on_cur_key[i];
                                    off_cur_key[j] = off_cur_key[i];
                                    max_on_cur_key[j] = max_on_cur_key[i];
                                    max_off_cur_key[j] = max_off_cur_key[i];
                                    cur_key[j] = "";
                                    max_cur_key[j] = "";
                                    on_cur_key[i] = "";
                                    off_cur_key[i] = "";
                                    max_on_cur_key[i] = "";
                                    max_off_cur_key[i] = "";
                                } else if (i012[i] + i012[j] == 0) {
                                    temp = cur_key[i];
                                    cur_key[i] = cur_key[j];
                                    cur_key[j] = temp;
                                    temp = max_cur_key[i];
                                    max_cur_key[i] = max_cur_key[j];
                                    max_cur_key[j] = temp;
                                } else if (i012[i] + i012[j] == 2) {
                                    temp = on_cur_key[i];
                                    on_cur_key[i] = on_cur_key[j];
                                    on_cur_key[j] = temp;
                                    temp = off_cur_key[i];
                                    off_cur_key[i] = off_cur_key[j];
                                    off_cur_key[j] = temp;
                                    temp = max_on_cur_key[i];
                                    max_on_cur_key[i] = max_on_cur_key[j];
                                    max_on_cur_key[j] = temp;
                                    temp = max_off_cur_key[i];
                                    max_off_cur_key[i] = max_off_cur_key[j];
                                    max_off_cur_key[j] = temp;
                                }
                                t = i012[i];
                                i012[i] = i012[j];
                                i012[j] = t;
//                                exchange(cur_keytw[i],cur_keytw[j]);

                                //采用交换数组 全部替换
                            }
                        }
                    }
                    for (int i = 0; i < i2; i++) {
                        Item icard = new Item();
                        if (i012[i] == 0) {
                            icard.setLocation("0");
                        } else if (i012[i] == 1) {
                            icard.setLocation("1");
                        }
                        icardList.add(icard);
                        int key10= Integer.parseInt(key12[i],16);
//
                        int i111=(key10-8448)/16;
                        int i222=((key10-8448)%16)/4;
                        DeviceLocation DeviceLocation =new DeviceLocation();

                        DeviceLocation.setLocation("#"+String.valueOf(i111+1)+"-"+String.valueOf(i222+1));

                        DeviceLocationList.add(DeviceLocation);

//                        Log.e("icardList", String.valueOf(icardList));
//                        Log.e("icardList|||||||", String.valueOf(icardList.get(i).getLocation()));
//                        Log.e("key12", key12[i]);
//                        Log.e("name12", name12[i]);
//                        Log.e("type12", type12[i]);
//                        Log.e("i012", String.valueOf(i012[i]));
                        if (i012[i] == 0) {
                            Log.e("cur_key[i]", cur_key[i]);
                        } else if (i012[i] == 1) {
                            Log.e("on_cur_key[i]", on_cur_key[i]);
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
        if (ControlActivity.this.getActivity().isDestroyed()) {
            mOkHttpClient.dispatcher().executorService().shutdown();
        }

    }

//    public void exchange(String a,String b){
//        String temp;
//        temp = a;
//        a = b;
//        b = temp;
//    }


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_control, container,
                false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
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

        //目的？
        for (int i = 0; i < i2; i++) {
            title.add(" " + i);
            datas.add(" " + i);
            button_values.add(" " +i);
            //times.add(" " + i);
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
        rvAdapter = new Rv3Cardview(icardList, datas, title,button_values, tw, te, statuskeyList,DeviceLocationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(ControlActivity.this.getActivity()));
        recyclerView.setAdapter(rvAdapter);
        rvAdapter.setItemClickListener(new Rv3Cardview.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(view.getContext(),"正转",Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onTextClick(View view, int position) {
            }
        });

        rvAdapter.setOnSwitchClickListener(new Rv3Cardview.OnSwitchClickListener() {
            @Override
            public void onClick(Item item, int position, boolean isChecked) {

                String t = ControlActivity.this.title.get(position);
                String b = isChecked ? "打开" : "关闭";
//                Log.e("Idss********",Id[position]);
                Log.e("token********",token);
                Log.e("key12********",key12[position]);
                if (isChecked){
                    HttpUtil.sendOkHttpPost(App.baseURL + "/api/plugins/rpc/twoway/"+deviceid12[position],token,key12[position],"0001",new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException{
                            String responseData = response.body().string();
                            if ((responseData != null)&&(responseData !="Device with requested id wasn't found!")){
                                Log.e("Respond*********",  "已打开");
                            }
                        }
                    });
                } else {
                    HttpUtil.sendOkHttpPost(App.baseURL + "/api/plugins/rpc/twoway/"+deviceid12[position],token,key12[position],"0000",new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException{
                            String responseData = response.body().string();
                            if ((responseData != null)&&(responseData !="Device with requested id wasn't found!")){
                                Log.e("Respond*********",  "已关闭");
                            }
                        }
                    });

                }

//                Toast.makeText(getActivity(), b + "了" + t + "的开关", Toast.LENGTH_SHORT).show();
            }
        });


        rvAdapter.setOnButtonClickListener(new Rv3Cardview.OnButtonClickListener() {
            @Override
            public void onClick(Item item, int position, View view) {


//                btn1=(Button)view.findViewById(R.id.bt_zhengzhuan);
//                btn2=(Button)view.findViewById(R.id.bt_stop);
//                btn3=(Button)view.findViewById(R.id.bt_fanzhuan);

                String b = "";
                if (view instanceof Button) {

                    b = ((Button) view).getText().toString();
                    Log.e("b*************", b);
                    Log.e("***************", String.valueOf(((Button) view).getId()));   //正转2131230760   反转2131230759    关2131230757
                }
                String t = ControlActivity.this.title.get(position);
                if ( b.equals("正转")){
                    HttpUtil.sendOkHttpPost(App.baseURL + "/api/plugins/rpc/twoway/"+deviceid12[position],token,key12[position],"0009",new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException{
                            String responseData = response.body().string();
                            Log.e("responseData*******",responseData);
                            if ((responseData != null)&&(responseData !="Device with requested id wasn't found!")){
                                Log.e("Respond*********",  "已正转");
//                                Toast.makeText(getActivity(), "已正转", Toast.LENGTH_SHORT).show();
//                                    b1 = false;
//                                    b2 = true;
//                                    b3 = true;
                            }
                        }
                    });//6b1c7100-46ae-11e8-8280-65869ac1d365
                } else if ( b.equals("停止")) {
                    HttpUtil.sendOkHttpPost(App.baseURL + "/api/plugins/rpc/twoway/"+deviceid12[position], token, key12[position], "0008", new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.e("responseData*******",responseData);
                            if ((responseData != null) && (responseData != "Device with requested id wasn't found!")) {
                                Log.e("Respond*********", "已停止");
//                                Toast.makeText(getActivity(), "已停止", Toast.LENGTH_SHORT).show();
//                                b1 = true;
//                                b2 = false;
//                                b3 = true;
                            }
                        }
                    });
                } else if ( b.equals("反转")) {
                    HttpUtil.sendOkHttpPost(App.baseURL + "/api/plugins/rpc/twoway/"+deviceid12[position], token, key12[position], "000B", new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.e("responseData*******",responseData);
                            if ((responseData != null) && (responseData != "Device with requested id wasn't found!")) {
                                Log.e("Respond*********", "已反转");
//                                Toast.makeText(getActivity(), "已反转", Toast.LENGTH_SHORT).show();
//                                b1 = true;
//                                b2 = false;
//                                b3 = true;
                            }
                        }
                    });
                }
//                btn1.setEnabled(b1);
//                btn2.setEnabled(b2);
//                btn3.setEnabled(b3);

//                ((Button) view).setEnabled(false);

//                Toast.makeText(getActivity(), "点击了" + t + "的" + b+String.valueOf(position), Toast.LENGTH_SHORT).show();

                //也可以通过view.getId()来判断是点击了哪一个button，然后进行对应的处理
            }
        });

        return view;
    }


    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            for (int ii = 0; ii < i3; ii++) {
//                Log.e("i1", "i1 " + i3);
//                Log.e("i1", "IDss " + Idss[ii]);
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
            Log.e("message|||", text);
            try {
                final String message = "{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[0] + "\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}]," +
                        "\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"" + Idss[0] + "\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}]}";

                if (text.contains("{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"")) {
                    String[] tx = text.split("\"");
                    devId = tx[9];
                    Log.e("devId", devId);
                }
                if (!text.contains("{\"tsSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"") && !text.contains("\"errorCode\":0,\"errorMsg\":null,\"data\":{},\"latestValues\":{}")
                        && !text.contains("{\"entityType\":\"DEVICE\",\"entityId\":\"null\",\"scope\":\"LATEST_TELEMETRY\",\"cmdId\":2}],\"historyCmds\":[],\"attrSubCmds\":[{\"entityType\":\"DEVICE\",\"entityId\":\"null\",\"scope\":\"CLIENT_SCOPE\",\"cmdId\":1}")) {
                    Log.e("Data", "receive text:" + text);
                    //收到服务器端发送来的信息后，每隔60秒发送一次心跳包
                    Log.e("i2", "i2 " + i2);
                    ParseJSONWithJSONObject(text);
                    for (int i = 0; i < i2; i++) {
                        IniStatus statuskey = new IniStatus();
                        //Log.e("Data",data.getData().getKey1()[i]);
                        //strings=data.getData().getKey();
                        //Log.e("Data", name[i] + " " + type[i] + " " + strings[i] + " " + unit[i]);
                        if (i012[i] == 0) {
//                            IniStatus statuskey = new IniStatus();
                            statuskey.setStatuskey(button_value[i]);
                            statuskeyList.add(statuskey);
                            button_values.set(i, (button_value[i]));
//                            Log.e("|||||||||******",button_values.get(i));
//                            icard.setLocation("0");
                            title.set(i, (name12[i]));
                            //type[i] + ":\n"
                            datas.set(i, ("当前电流 " + strings[i] + " " + "A"  + "\n"+ "最大电流 " + strings2[i] + " " + "A"));
//                                times.set(i,(time[i]));
                        } else if (i012[i] == 1) {
//                            icard.setLocation("1");
                            statuskey.setStatuskey(button_value[i]);
                            statuskeyList.add(statuskey);
                            button_values.set(i, (button_value[i]));
                            title.set(i, (name12[i]));
                            //type[i] + ":\n"
                            datas.set(i, ("正转当前电流 " + strings[i] + " " + "A" + "\n" + "正转最大电流 " + strings2[i] + " " + "A" + "\n"
                                    + "反转当前电流 " + strings3[i] + " " + "A" + "\n" + "反转最大电流 " + strings4[i] + " " + "A"));
                        }
//                        icardList.add(icard);
//                        statuskeyList.add(statuskey);

                        Message message1 = new Message();
                        message1.what = UPDATE_TEXT;
                        handler.sendMessage(message1);

                    }
//                    rvAdapter.notifyDataSetChanged();
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

        private void ParseJSONWithJSONObject(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                JSONObject data = json.getJSONObject("data");
                String[] key1 = new String[700];
                Log.e("Control", data.toString());
//                for(int i=0;i<jsonArray1.length();i++){
//                    JSONObject jsonObject=jsonArray1.getJSONObject(i);
//                    Log.e("jsonob", String.valueOf(jsonObject));
//                    Log.e("jsonob", devId);
//                    try {
//                        key1[i] = jsonObject.getString(devId);
//                        int key10= Integer.parseInt(key1[i],16);
//                        int i111=(key10-256)/32;
//                        int i222=((key10-256)%32)/4;
//                        i6=i111*8+i222;
//                        if(i6>511){
//                            i6=i6-256;
//                        }
//                        key12[i6]=key1[i];
//                        Log.e("key1[i]",key1[i]);
//                        Log.e("ii","i="+i);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//                if (data.length() == 1) {
                if (i4 == 1) {
                    for (int i = 0; i < i2; i++) {
                        try {
                            String[] sdcur = new String[700];
                            String[] sdmax_cur = new String[700];
                            String[] sdon_cur = new String[700];
                            String[] sdon_max_cur = new String[700];
                            String[] sdoff_cur = new String[700];
                            String[] sdoff_max_cur = new String[700];
                            String[] buttonkey = new String[700];
                            Log.e("||||||||||||||", "0000000000000");
                            if (i012[i] == 0) {
                                Log.e("||||||||||||||", "0000000000001");
//                                button_value[i] = data.getString("H"+key12[i]);

                                if (data.has("H"+cur_key[i])) {
                                    sdcur[i] = data.getString("H" + cur_key[i]);
                                    String[] sd1 = sdcur[i].split("\"");
                                    strings[i] = sd1[1];
                                    Log.e("cur_key|||", sd1[1]);
                                }
                                if (data.has("H"+max_cur_key[i])) {
                                    sdmax_cur[i] = data.getString("H" + max_cur_key[i]);
                                    String[] sd2 = sdmax_cur[i].split("\"");
                                    strings2[i] = sd2[1];
                                }
                                if (data.has("H" + key12[i])) {
                                    buttonkey[i] = data.getString("H" + key12[i]);
                                    String[] sd3 = buttonkey[i].split("\"");
                                    button_value[i] = sd3[1];
                                }
//                                String[] sd1 = sdcur[i].split(",");
//                                String[] sd2 = sdmax_cur[i].split(",");
//                                strings[i] = sd1[1];
//                                strings2[i] = sd2[1];

//                                Log.e("DATA", sd2[1]);       //如果是 ，首先下载什么
                            } else if (i012[i] == 1) {
                                if (data.has("H"+on_cur_key[i])) {
                                    sdon_cur[i] = data.getString("H"+on_cur_key[i]);
                                    String[] sd1 = sdon_cur[i].split("\"");
                                    strings[i] = sd1[1];
                                }
                                if (data.has("H"+max_on_cur_key[i])) {
                                    sdon_max_cur[i] = data.getString("H"+max_on_cur_key[i]);
                                    String[] sd2 = sdon_max_cur[i].split("\"");
                                    strings2[i] = sd2[1];
                                }
                                if (data.has("H"+off_cur_key[i])) {
                                    sdoff_cur[i] = data.getString("H"+off_cur_key[i]);
                                    String[] sd3 = sdoff_cur[i].split("\"");
                                    strings3[i] = sd3[1];
                                }
                                if (data.has("H"+max_off_cur_key[i])) {
                                    sdoff_cur[i] = data.getString("H"+max_off_cur_key[i]);
                                    String[] sd4 = sdoff_max_cur[i].split("\"");
                                    strings4[i] = sd4[1];
                                }
                                if (data.has("H" + key12[i])) {
                                    buttonkey[i] = data.getString("H" + key12[i]);
                                    String[] sd5 = buttonkey[i].split("\"");
                                    button_value[i] = sd5[1];
                                }
//                                Log.e("DATA", sd1[1]);
//                                Log.e("DATA", sd2[1]);
//                                Log.e("DATA", sd3[1]);
//                                Log.e("DATA", sd4[1]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                final int l = data.length();

                if (i4 ==0) {
                    i4 = 1;
                    for (int i = 0; i < i2; i++) {
                        try {
                            String[] sdcur = new String[700];
                            String[] sdmax_cur = new String[700];
                            String[] sdon_cur = new String[700];
                            String[] sdon_max_cur = new String[700];
                            String[] sdoff_cur = new String[700];
                            String[] sdoff_max_cur = new String[700];
                            String[] buttonkey = new String[700];
//                          Log.e("||||||||||||||", "11111111111");
                            if (i012[i] == 0) {
//                            Log.e("||||||||||||||", "33333");
//                            Log.e("|||||||33333", String.valueOf(i));
//                            Log.e("|||||||i012", String.valueOf(i012[i]));
//                            Log.e("||||data", data.toString());
//                            Log.e("||||cur_key[i]", cur_key[i]);
                                buttonkey[i] = data.getString("H" + key12[i]);
                                sdcur[i] = data.getString("H" + cur_key[i]);
                                sdmax_cur[i] = data.getString("H" + max_cur_key[i]);
                                String[] sd1 = sdcur[i].split("\"");
                                String[] sd2 = sdmax_cur[i].split("\"");
                                String[] sd3 = buttonkey[i].split("\"");
                                strings[i] = sd1[1];
                                strings2[i] = sd2[1];
                                button_value[i] = sd3[1];
//                            Log.e("key*********", key12[i]);
//                            Log.e("sd3*********", sd3[1]);
//                            Log.e("button_value********", button_value[i]);
                            } else if (i012[i] == 1) {
//                              Log.e("||||||||||||||", "44444");
//                              Log.e("|||||||44444", String.valueOf(i));
//                              Log.e("|||||||i012", String.valueOf(i012[i]));
//                               Log.e("||on_cur_key[i]", on_cur_key[i]);
                                buttonkey[i] = data.getString("H" + key12[i]);
                                sdon_cur[i] = data.getString("H" + on_cur_key[i]);
                                sdon_max_cur[i] = data.getString("H" + max_on_cur_key[i]);
                                sdoff_cur[i] = data.getString("H" + off_cur_key[i]);
                                sdoff_max_cur[i] = data.getString("H" + max_off_cur_key[i]);
                                String[] sd1 = sdon_cur[i].split("\"");
                                String[] sd2 = sdon_max_cur[i].split("\"");
                                String[] sd3 = sdoff_cur[i].split("\"");
                                String[] sd4 = sdoff_max_cur[i].split("\"");
                                String[] sd5 = buttonkey[i].split("\"");
                                strings[i] = sd1[1];
                                strings2[i] = sd2[1];
                                strings3[i] = sd3[1];
                                strings4[i] = sd4[1];
                                button_value[i] = sd5[1];
//                                Log.e("DATA",sd1[1]);
//                                Log.e("DATA",sd3[1]);
//                                Log.e("DATA",sd4[1]);
//                                Log.e("key*********", key12[i]);
//                                Log.e("sd5*********", sd5[1]);
//                                Log.e("button_value********", button_value[i]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            i4++;


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




