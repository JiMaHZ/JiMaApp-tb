package com.example.sc.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sc.parse.CheckBoxList;
import com.example.sc.parse.DeviceLocation;
import com.example.sc.parse.Item;
import com.example.sc.parse.IniStatus;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


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
    private String sort_types;


    private OnCheckBoxClickListener mOnCheckBoxClickListener;
    private OnSwitchClickListener mOnSwitchClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private List<Type> typeList = new ArrayList<>();
    private List<Type> bt1List = new ArrayList<>();
    private List<Type> bt2List = new ArrayList<>();
    private List<Type> bt3List = new ArrayList<>();
    private List<CheckBoxList> mCheckBoxList;
    private boolean isShowOrNot;

    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    //标记下选中和未选中这两种状态
    public enum Type {
        Checked, UnCheck
    }


    public Rv3Cardview(List<Item> icardList, List<String> datas, List<String> title, List<String> button_values, int tw, int te, String sort_types, List<IniStatus> statuskeyList, List<DeviceLocation> DeviceLocationList) {
        this.title = title;
        this.datas = datas;
        this.icardList = icardList;
        this.tw = tw;
        this.statuskeyList = statuskeyList;
        this.te = te;
        this.sort_types = sort_types;
        this.DeviceLocationList = DeviceLocationList;
        this.button_values = button_values;
//        this.control_addrtw=control_addrtw;
//        this.control_addrte=control_addrte;
//        this.Id =Id;
//        this.token =token;
        initData();
    }

    //初始化checkbox数据  暂时假设均为关闭
    private void initData() {
        for (int i = 0; i < tw + te; i++) {
            Type type = Type.UnCheck;
            typeList.add(type);
            bt1List.add(Type.UnCheck);
            bt2List.add(Type.UnCheck);
            bt3List.add(Type.UnCheck);
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

        //由spinner来实现筛选
        if ((sort_types != null) && (sort_types.equals("全部"))) {
            if (holder instanceof Item1ViewHolder) {
                ((Item1ViewHolder) holder).setVisibility(true);
//                ((Item1ViewHolder) holder).choose.setChecked(isCheckedAll);
            } else if (holder instanceof Item2ViewHolder) {
                ((Item2ViewHolder) holder).setVisibility(true);
//                checkBoxList.set(position,(isCheckedAll.equals("all")? Type.Checked:Type.UnCheck));
            }
        } else if ((sort_types != null) && (sort_types.equals("风机"))) {
            if (holder instanceof Item1ViewHolder) {
                ((Item1ViewHolder) holder).setVisibility(true);
//                checkBoxList.set(position,(isCheckedAll.equals("all")? Type.Checked:Type.UnCheck));
            } else if (holder instanceof Item2ViewHolder) {
                ((Item2ViewHolder) holder).setVisibility(false);
            }
        } else if ((sort_types != null) && (sort_types.equals("卷膜"))) {
            if (holder instanceof Item1ViewHolder) {
                ((Item1ViewHolder) holder).setVisibility(false);
            } else if (holder instanceof Item2ViewHolder) {
                ((Item2ViewHolder) holder).setVisibility(true);
//                checkBoxList.set(position,(isCheckedAll.equals("all")? Type.Checked:Type.UnCheck));
            }
        }

        if (holder instanceof Item1ViewHolder) {

            //全选与取消
            if ((sort_types != null) && (sort_types.equals("风机")||sort_types.equals("全部"))) {
                if (mCheckBoxList != null && mCheckBoxList.get(position).isSelect){
                    ((Item1ViewHolder) holder).choose.setChecked(true);

                }else {
                    ((Item1ViewHolder) holder).choose.setChecked(false);
                }
            }

            //实现checkBox的显示与隐藏
            ((Item1ViewHolder) holder).choose.setVisibility(isShowOrNot ? View.VISIBLE : View.GONE);


            //指示灯的状态
            if ((button_values.size() != 0) && ((button_values.get(position).equals("0001")) || (button_values.get(position).equals("0005")))) {
                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_green);
//                typeList.set(position, Type.Checked);
            } else if ((button_values.size() != 0) && ((button_values.get(position).equals("0000")) || (button_values.get(position).equals("0004")))) {
                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_gray);
//                typeList.set(position, Type.UnCheck);
            } else {
                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_gray);
            }

            ((Item1ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item1ViewHolder) holder).mTextView2.setText(datas.get(position));
            ((Item1ViewHolder) holder).mTextView3.setText(DeviceLocationList.get(position).getLocation());

            //状态的初始化显示--button
            ((Item1ViewHolder) holder).aSwitch.setOnCheckedChangeListener(null);
            if ((statuskeyList.size() != 0) && ((statuskeyList.get(position).getStatuskey().equals("0001")) || (statuskeyList.get(position).getStatuskey().equals("0005")))) {
//                    ((Item1ViewHolder) holder).aSwitch.setChecked(true);
                typeList.set(position, Type.Checked);
                statuskeyList.get(position).setStatuskey("INI");
//                    typeList.add(type);
            } else if ((statuskeyList.size() != 0) && ((statuskeyList.get(position).getStatuskey().equals("0000")) || (statuskeyList.get(position).getStatuskey().equals("0004")))) {
//                    ((Item1ViewHolder) holder).aSwitch.setChecked(false);
                typeList.set(position, Type.UnCheck);
                statuskeyList.get(position).setStatuskey("INI");
            }

                ((Item1ViewHolder) holder).choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    }
                });

            ((Item1ViewHolder) holder).choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCheckBoxClickListener != null) {
                        mOnCheckBoxClickListener.onClick(icardList.get(position), position, !mCheckBoxList.get(position).isSelect);
                        if (!mCheckBoxList.get(position).isSelect) {
                            mCheckBoxList.get(position).setSelect(true);
                        } else {
                            mCheckBoxList.get(position).setSelect(false);
                        }

                        Log.e("checkBox",String.valueOf(mOnCheckBoxClickListener));
                        Log.e("INI","======================");
                    }
                }
            });

                ((Item1ViewHolder) holder).aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (buttonView.isPressed()) {

                        if (mOnSwitchClickListener != null) {
                            mOnSwitchClickListener.onClick(icardList.get(position), position, isChecked);
                            if (isChecked) {
                                typeList.set(position, Type.Checked);
                            } else {
                                typeList.set(position, Type.UnCheck);
                            }
                        }
                    } else {
                        return;
                    }
                }
            });

            //保留原有位置
            if (typeList.get(position).equals(Type.Checked)) {
                ((Item1ViewHolder) holder).aSwitch.setChecked(true);
//                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_green);
            } else if (typeList.get(position).equals(Type.UnCheck)) {
                ((Item1ViewHolder) holder).aSwitch.setChecked(false);
//                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_gray);
            } else {
                ((Item1ViewHolder) holder).aSwitch.setChecked(false);
//                ((Item1ViewHolder) holder).lamp1.setImageResource(R.drawable.lamp_gray);
            }
        } else if (holder instanceof Item2ViewHolder) {

            //全选与取消
            if ((sort_types != null) && (sort_types.equals("卷膜")||sort_types.equals("全部"))) {
                if (mCheckBoxList != null && mCheckBoxList.get(position).isSelect){
                    ((Item2ViewHolder) holder).choose.setChecked(true);
                }else {
                    ((Item2ViewHolder) holder).choose.setChecked(false);
                }
            }

            //实现checkBox的显示与隐藏
            ((Item2ViewHolder) holder).choose.setVisibility(isShowOrNot ? View.VISIBLE : View.GONE);

            ((Item2ViewHolder) holder).mTextView1.setText(title.get(position));
            ((Item2ViewHolder) holder).mTextView2.setText(datas.get(position));
            ((Item2ViewHolder) holder).mTextView3.setText(DeviceLocationList.get(position).getLocation());
            if ((button_values.size() != 0) && ((button_values.get(position).equals("0009")) || (button_values.get(position).equals("0003")))) {
                ((Item2ViewHolder) holder).lamp2.setImageResource(R.drawable.lamp_green);
                bt1List.set(position, Type.Checked);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.UnCheck);
            } else if ((button_values.size() != 0) && ((button_values.get(position).equals("0008")) || (button_values.get(position).equals("0000")))) {
                ((Item2ViewHolder) holder).lamp2.setImageResource(R.drawable.lamp_gray);
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.Checked);
                bt3List.set(position, Type.UnCheck);
            } else if ((button_values.size() != 0) && ((button_values.get(position).equals("000B")) || (button_values.get(position).equals("0004")))) {
                ((Item2ViewHolder) holder).lamp2.setImageResource(R.drawable.lamp_blue);
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.Checked);

            } else {
                ((Item2ViewHolder) holder).lamp2.setImageResource(R.drawable.lamp_gray);
            }

            if ((statuskeyList.size() != 0) && ((statuskeyList.get(position).getStatuskey().equals("0008")) || (statuskeyList.get(position).getStatuskey().equals("0000")))) {
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.Checked);
                bt3List.set(position, Type.UnCheck);
                statuskeyList.get(position).setStatuskey("INI");
            } else if ((statuskeyList.size() != 0) && ((statuskeyList.get(position).getStatuskey().equals("0009")) || (statuskeyList.get(position).getStatuskey().equals("0003")))) {
                bt1List.set(position, Type.Checked);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.UnCheck);
                statuskeyList.get(position).setStatuskey("INI");
            } else if ((statuskeyList.size() != 0) && ((statuskeyList.get(position).getStatuskey().equals("000B")) || (statuskeyList.get(position).getStatuskey().equals("0004")))) {
                bt1List.set(position, Type.UnCheck);
                bt2List.set(position, Type.UnCheck);
                bt3List.set(position, Type.Checked);
                statuskeyList.get(position).setStatuskey("INI");
            }

            ((Item2ViewHolder) holder).choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
            ((Item2ViewHolder) holder).choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCheckBoxClickListener != null) {
                        mOnCheckBoxClickListener.onClick(icardList.get(position), position, !mCheckBoxList.get(position).isSelect);
                        if (!mCheckBoxList.get(position).isSelect) {
                            mCheckBoxList.get(position).setSelect(true);
                        } else {
                            mCheckBoxList.get(position).setSelect(false);
                        }
                        Log.e("checkBox",String.valueOf(mOnCheckBoxClickListener));
                        Log.e("INI","======================");
                    }
                }
            });



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

            if ((bt1List.size() != 0) && (bt2List.size() != 0) && (bt3List.size() != 0) && (bt1List.get(position).equals(Type.Checked)) && (bt2List.get(position).equals(Type.UnCheck)) && (bt3List.get(position).equals(Type.UnCheck))) {
                ((Item2ViewHolder) holder).button1.setEnabled(false);
                ((Item2ViewHolder) holder).button2.setEnabled(true);
                ((Item2ViewHolder) holder).button3.setEnabled(true);
            } else if ((bt1List.size() != 0) && (bt2List.size() != 0) && (bt3List.size() != 0) && (bt1List.get(position).equals(Type.UnCheck)) && (bt2List.get(position).equals(Type.Checked)) && (bt3List.get(position).equals(Type.UnCheck))) {
                ((Item2ViewHolder) holder).button1.setEnabled(true);
                ((Item2ViewHolder) holder).button2.setEnabled(false);
                ((Item2ViewHolder) holder).button3.setEnabled(true);
            } else if ((bt1List.size() != 0) && (bt2List.size() != 0) && (bt3List.size() != 0) && (bt1List.get(position).equals(Type.UnCheck)) && (bt2List.get(position).equals(Type.UnCheck)) && (bt3List.get(position).equals(Type.Checked))) {
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

        if (icardList.size() != 0 && (icardList.get(i).getLocation().equals("1"))) {
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

    /**
     * CheckBox点击事件接口
     */
    public interface OnCheckBoxClickListener {

        void onClick(Item item, int position, boolean isChecked);
    }


    public static class Item1ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView1;
        TextView mTextView2;
        TextView mTextView3;
        Switch aSwitch;
        public ImageView lamp1;
        CheckBox choose;

        public Item1ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data1);
            mTextView3 = (TextView) itemView.findViewById(R.id.devices_location);
            aSwitch = (Switch) itemView.findViewById(R.id.switch1);
            lamp1 = (ImageView) itemView.findViewById(R.id.lamp);
            choose = itemView.findViewById(R.id.choose);

        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

    }

    public class Item2ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView1;
        TextView mTextView2;
        TextView mTextView3;
        Button button1, button2, button3;
        public ImageView lamp2;
        CheckBox choose;

        public Item2ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.tv_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.tv_data1);
            mTextView3 = (TextView) itemView.findViewById(R.id.devices_location);
            button1 = (Button) itemView.findViewById(R.id.bt_zhengzhuan);
            button2 = (Button) itemView.findViewById(R.id.bt_stop);
            button3 = (Button) itemView.findViewById(R.id.bt_fanzhuan);
            lamp2 = (ImageView) itemView.findViewById(R.id.lamp);
            choose = itemView.findViewById(R.id.choose);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }


    public void setOnSwitchClickListener(OnSwitchClickListener onSwitchClickListener) {
        mOnSwitchClickListener = onSwitchClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        mOnButtonClickListener = onButtonClickListener;
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        mOnCheckBoxClickListener = onCheckBoxClickListener;
    }

    /**
     * 筛选窗口接口
     * @return
     */
 	public String getSort_types() {
        return sort_types;
    }

    public void setSort_types(String sort_types) {
        this.sort_types = sort_types;
        notifyDataSetChanged();
    }

    /**
     * CheckBox隐藏与显示接口
      * @return
     */
    public boolean isShowOrNot() {
        return isShowOrNot;
    }

    public void setShowOrNot(boolean showOrNot) {
        isShowOrNot = showOrNot;
        notifyDataSetChanged();
    }

    /**
     * 全选与取消接口
     * @return
     */
    public void notifyAdapter(List<CheckBoxList> checkBoxList, boolean isAdd) {
        if (!isAdd) {
            this.mCheckBoxList = checkBoxList;
        } else {
            this.mCheckBoxList.addAll(checkBoxList);
        }
        notifyDataSetChanged();
    }

    public List<CheckBoxList> getCheckBoxList() {
        if (mCheckBoxList == null) {
            mCheckBoxList = new ArrayList<>();
        }
        return mCheckBoxList;
    }


}
