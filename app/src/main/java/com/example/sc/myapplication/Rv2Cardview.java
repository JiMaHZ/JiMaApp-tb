package com.example.sc.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sc.parse.DeviceLocation;

import java.util.ArrayList;
import java.util.List;

public class Rv2Cardview extends RecyclerView.Adapter<Rv2Cardview.ViewHolder> {

    private List<String> datas;
    private List<String> title;
    private List<String> times;
    private List<DeviceLocation> DeviceLocationList = new ArrayList<>();

    public Rv2Cardview(List<String> datas, List<String> title, List<String> times,List<DeviceLocation> DeviceLocationList ) {
        this.datas = datas;
        this.title = title;
        this.times = times;
        this.DeviceLocationList=DeviceLocationList;
    }

    /**
     * 将布局转换成view 并传递给RecyclerView 封装好的 ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_rv2_cardview, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 建立ViewHolder中视图与数据的关联
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView1.setText(title.get(position));
        holder.textView2.setText(datas.get(position));
        holder.textView3.setText(times.get(position));
        holder.textView4.setText(DeviceLocationList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;

        public ViewHolder(final View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.tv_title);
            textView1 = (TextView) itemView.findViewById(R.id.tv_data);
            textView3 = (TextView) itemView.findViewById(R.id.tv_time);
            textView4 = (TextView) itemView.findViewById(R.id.devices_location);

            textView1.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onTextClick(v, getPosition());
                            }
                        }
                    });
            textView2.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onTextClick(v, getPosition());
                            }
                        }
                    });
            textView3.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onTextClick(v, getPosition());
                            }
                        }
                    });
            textView4.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onTextClick(v, getPosition());
                            }
                        }
                    });

        }
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


}