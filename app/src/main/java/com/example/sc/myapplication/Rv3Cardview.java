package com.example.sc.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sc.util.HttpUtil;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class Rv3Cardview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<String> datas;
    private List<String> title;
    private List<String> control_addrtw;
    private List<String> control_addrte;
    private String Id;
    private String token;
    int i;

    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public Rv3Cardview(int i, List<String> datas, List<String> title, List<String> control_addrtw, List<String> control_addrte, String Id, String token) {
        this.title = title;
        this.datas = datas;
        this.i = i;
        this.control_addrtw = control_addrtw;
        this.control_addrte = control_addrte;
        this.Id = Id;
        this.token = token;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.rvc1_item_cardview, parent, false);
            return new Item1ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.rvc2_item_cardview, parent, false);
            return new Item1ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Item1ViewHolder) {
            ((Item1ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item1ViewHolder) holder).mTextView2.setText(datas.get(position));
        } else if (holder instanceof Item2ViewHolder) {
            ((Item2ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item2ViewHolder) holder).mTextView2.setText(datas.get(position));
        }
    }


    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return ITEM_TYPE.ITEM1.ordinal();
        } else if (i == 1) {
            return ITEM_TYPE.ITEM2.ordinal();
        } else {
            return Integer.parseInt(null);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public static class Item1ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView1;
        TextView mTextView2;
        Switch aSwitch;

        public Item1ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_title);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data);
            aSwitch = (Switch) itemView.findViewById(R.id.switch1);
        }
    }

    public class Item2ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView1;
        TextView mTextView2;
        Button button1, button2, button3;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_title);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data);
            button1 = (Button) itemView.findViewById(R.id.bt_zhengzhuan);
            button2 = (Button) itemView.findViewById(R.id.bt_stop);
            button3 = (Button) itemView.findViewById(R.id.bt_fanzhuan);
            //button1.setOnClickListener(new View.OnClickListener(){
/*
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.bt_zhengzhuan:
                            int position;
                            HttpUtil.sendOkHttpPost("http://140.143.23.199:8080/api/plugins/rpc/twoway/"+Id,token,control_addrte,"0008",new okhttp3.Callback(){

                            });
                            break;
                        default:
                            break;

                    }
                }*/
            // });
        }
    }
}
