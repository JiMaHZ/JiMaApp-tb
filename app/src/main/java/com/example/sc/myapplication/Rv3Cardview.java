package com.example.sc.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sc.parse.DeviceLocation;
import com.example.sc.parse.Item;
import com.example.sc.parse.IniStatus;


import java.util.ArrayList;
import java.util.List;


public class Rv3Cardview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<String> datas;
    private List<String> title;
    public List<String> button_values;
    //    private List<String> control_addrtw;
//    private List<String> control_addrte;
//    private  String Id;
//    private  String token;
    private List<Item> icardList;
    private List<IniStatus> statuskeyList;
    private List<DeviceLocation> DeviceLocationList = new ArrayList<>();
    private int te;
    private int tw;



    private OnSwitchClickListener mOnSwitchClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private List<Type> typeList = new ArrayList<>();
    private List<Type> bt1List = new ArrayList<>();
    private List<Type> bt2List = new ArrayList<>();
    private List<Type> bt3List = new ArrayList<>();

    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    //标记下选中和未选中这两种状态
    public enum Type{
        Checked, UnCheck
    }


    public Rv3Cardview(List<Item> icardList, List<String> datas, List<String> title, int tw, int te, List<IniStatus> statuskeyList,List<DeviceLocation>DeviceLocationList) {
        this.title = title;
        this.datas = datas;
        this.icardList = icardList;
        this.tw = tw;
        this.statuskeyList = statuskeyList;
        this.te = te;
        this.DeviceLocationList = DeviceLocationList;
//        this.control_addrtw=control_addrtw;
//        this.control_addrte=control_addrte;
//        this.Id =Id;
//        this.token =token;
         initData();
    }

    //初始化checkbox数据  暂时假设均为关闭
    private void initData() {
        for (int i = 0; i < tw+te; i++) {
//            if (i<tw) {
                Type type = Type.UnCheck;
                typeList.add(type);
//            } else  if (i<te+tw ){
                bt1List.add(Type.UnCheck);
                bt2List.add(Type.UnCheck);
                bt3List.add(Type.UnCheck);
//            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        viewType == ITEM_TYPE.ITEM1.ordinal()
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.rvc1_item_cardview, parent, false);
            return new Item1ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.rvc2_item_cardview, parent, false);
            return new Item2ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        holder.itemView.setTag(position);
        if (holder instanceof Item1ViewHolder) {
            ((Item1ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item1ViewHolder) holder).mTextView2.setText(datas.get(position));
            ((Item1ViewHolder) holder).mTextView3.setText(DeviceLocationList.get(position).getLocation());
            ((Item1ViewHolder) holder).aSwitch.setOnCheckedChangeListener(null);
                if ((statuskeyList.size() != 0) &&((statuskeyList.get(position).getStatuskey().equals("0001"))||(statuskeyList.get(position).getStatuskey().equals("0005")))){
//                    ((Item1ViewHolder) holder).aSwitch.setChecked(true);
                    typeList.set(position, Type.Checked);
                    statuskeyList.get(position).setStatuskey("INI");
//                    typeList.add(type);
                } else if ((statuskeyList.size() != 0) &&((statuskeyList.get(position).getStatuskey().equals("0000"))||(statuskeyList.get(position).getStatuskey().equals("0004")))) {
//                    ((Item1ViewHolder) holder).aSwitch.setChecked(false);
                    typeList.set(position, Type.UnCheck);
                    statuskeyList.get(position).setStatuskey("INI");
//                    typeList.add(type);
                }
//                else {
////                    ((Item1ViewHolder) holder).aSwitch.setChecked(false);
//                    typeList.set(position, Type.UnCheck);
////                    typeList.add(type);
//                }

            ((Item1ViewHolder) holder).aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (buttonView.isPressed()) {

                        if (mOnSwitchClickListener != null) {
                            mOnSwitchClickListener.onClick(icardList.get(position), position, isChecked);
                            if (isChecked){
                                typeList.set(position, Type.Checked);
                            }
                            else{
                                typeList.set(position, Type.UnCheck);
                            }
                        }
                    }
                    else {
                        return;
//                        if ((statuskeyList.size() != 0) &&(statuskeyList.get(position).getStatuskey().equals("0001"))){
////                    ((Item1ViewHolder) holder).aSwitch.setChecked(true);
//                            typeList.set(position, Type.Checked);
////                    typeList.add(type);
//                        } else if ((statuskeyList.size() != 0) &&(statuskeyList.get(position).getStatuskey().equals("0000"))) {
////                    ((Item1ViewHolder) holder).aSwitch.setChecked(false);
//                            typeList.set(position, Type.UnCheck);
////                    typeList.add(type);
//                        }else {
////                    ((Item1ViewHolder) holder).aSwitch.setChecked(false);
//                            typeList.set(position, Type.UnCheck);
////                    typeList.add(type);
//                        }
                    }
                }
            });

            //保留原有位置
            if (typeList.get(position).equals(Type.Checked) ) {
            	((Item1ViewHolder) holder).aSwitch.setChecked(true);
            } else if (typeList.get(position).equals(Type.UnCheck)) {
            	((Item1ViewHolder) holder).aSwitch.setChecked(false);
            } else {
            	((Item1ViewHolder) holder).aSwitch.setChecked(false);
            }
        } else if (holder instanceof Item2ViewHolder) {
            ((Item2ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item2ViewHolder) holder).mTextView2.setText(datas.get(position));
            ((Item2ViewHolder) holder).mTextView3.setText(DeviceLocationList.get(position).getLocation());
            if ((statuskeyList.size() != 0) &&((statuskeyList.get(position).getStatuskey().equals("0008")) ||(statuskeyList.get(position).getStatuskey().equals("0000")))) {
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.Checked);
                bt3List.set(position, Type.UnCheck);
                statuskeyList.get(position).setStatuskey("INI");
            } else if((statuskeyList.size() != 0) &&((statuskeyList.get(position).getStatuskey().equals("0009"))||(statuskeyList.get(position).getStatuskey().equals("0003")))) {
                bt1List.set(position, Type.Checked);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.UnCheck);
                statuskeyList.get(position).setStatuskey("INI");
            } else if((statuskeyList.size() != 0) &&((statuskeyList.get(position).getStatuskey().equals("000B"))||(statuskeyList.get(position).getStatuskey().equals("0004")))) {
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.Checked);
                statuskeyList.get(position).setStatuskey("INI");
            }
            ((Item2ViewHolder) holder).button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonClickListener != null) {
                        mOnButtonClickListener.onClick(icardList.get(position), position, v);

                        bt1List.set(position, Type.Checked);
                        bt2List.set(position, Type.UnCheck);
                        bt3List.set(position, Type.UnCheck);
//                        ((Item2ViewHolder) holder).button3.setEnabled(true);
                    }
                }
            });
            ((Item2ViewHolder) holder).button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonClickListener != null) {
                        mOnButtonClickListener.onClick(icardList.get(position), position, v);
                        bt1List.set(position, Type.UnCheck);
                        bt2List.set(position, Type.Checked);
                        bt3List.set(position, Type.UnCheck);
                    }
                }
            });
            ((Item2ViewHolder) holder).button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonClickListener != null) {
                        mOnButtonClickListener.onClick(icardList.get(position), position, v);
                        bt1List.set(position, Type.UnCheck);
                        bt2List.set(position, Type.UnCheck);
                        bt3List.set(position, Type.Checked);
                    }
                }
            });

            if ((bt1List.size() !=0) && (bt2List.size() !=0) &&(bt3List.size() !=0) && (bt1List.get(position).equals(Type.Checked))&&(bt2List.get(position).equals(Type.UnCheck))&&(bt3List.get(position).equals(Type.UnCheck))) {
                ((Item2ViewHolder) holder).button1.setEnabled(false);
                ((Item2ViewHolder) holder).button2.setEnabled(true);
                ((Item2ViewHolder) holder).button3.setEnabled(true);
            } else if ((bt1List.size() !=0) && (bt2List.size() !=0) &&(bt3List.size() !=0) && (bt1List.get(position).equals(Type.UnCheck))&&(bt2List.get(position).equals(Type.Checked))&&(bt3List.get(position).equals(Type.UnCheck))) {
                ((Item2ViewHolder) holder).button1.setEnabled(true);
                ((Item2ViewHolder) holder).button2.setEnabled(false);
                ((Item2ViewHolder) holder).button3.setEnabled(true);
            } else if ((bt1List.size() !=0) && (bt2List.size() !=0) &&(bt3List.size() !=0) && (bt1List.get(position).equals(Type.UnCheck))&&(bt2List.get(position).equals(Type.UnCheck))&&(bt3List.get(position).equals(Type.Checked))) {
                ((Item2ViewHolder) holder).button1.setEnabled(true);
                ((Item2ViewHolder) holder).button2.setEnabled(true);
                ((Item2ViewHolder) holder).button3.setEnabled(false);
            } else {
                ((Item2ViewHolder) holder).button1.setEnabled(true);
                ((Item2ViewHolder) holder).button2.setEnabled(true);
                ((Item2ViewHolder) holder).button3.setEnabled(true);
            }


        }
    }


    @Override
    public int getItemViewType(int i) {

        if (icardList.size() !=0 &&(icardList.get(i).getLocation().equals("1"))) {
            return 1;
        }
//          else if(
// icardList.get(i).equals("1")){return ITEM_TYPE.ITEM2.ordinal();}
        else {
//            return ITEM_TYPE.ITEM2.ordinal();
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public OnItemClickListener itemClickListener;

    /**
     * 设置接口
     *
     * @param itemClickListener
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 点击事件接口
     */
    public interface OnItemClickListener {
        //item的点击事件
        void onItemClick(View view, int position);

        //item中文字的点击事件
        void onTextClick(View view, int position);
    }

    /**
     * 按钮点击事件接口
     */
    public interface OnButtonClickListener {
        void onClick(Item item, int position, View view);
    }

    /**
     * 开关点击事件接口
     */
    public interface OnSwitchClickListener {

        void onClick(Item item, int position, boolean isChecked);
    }

    public static class Item1ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView1;
        TextView mTextView2;
        TextView mTextView3;
        Switch aSwitch;

        public Item1ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data1);
            mTextView3 = (TextView) itemView.findViewById(R.id.devices_location);
            aSwitch = (Switch) itemView.findViewById(R.id.switch1);

        }
    }

    public class Item2ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView1;
        TextView mTextView2;
        TextView mTextView3;
        Button button1, button2, button3;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data1);
            mTextView3 = (TextView) itemView.findViewById(R.id.devices_location);
            button1 = (Button) itemView.findViewById(R.id.bt_zhengzhuan);
            button2 = (Button) itemView.findViewById(R.id.bt_stop);
            button3 = (Button) itemView.findViewById(R.id.bt_fanzhuan);


//            button1.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    switch (v.getId()) {
//                        case R.id.bt_zhengzhuan:
//                            int position;
//                            Toast.makeText(v.getContext(),"正转",Toast.LENGTH_SHORT).show();
////                            HttpUtil.sendOkHttpPost("http://140.143.23.199:8080/api/plugins/rpc/twoway/"+Id,token,control_addrte,"0008",new okhttp3.Callback(){
////
////                            });
//                            break;
//                        default:
//                            break;
//
//                    }
//                }
//            });
        }
    }


    public void setOnSwitchClickListener(OnSwitchClickListener onSwitchClickListener) {
        mOnSwitchClickListener = onSwitchClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        mOnButtonClickListener = onButtonClickListener;
    }
}
