package com.example.sc.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    private List<String> datas;
    private List<String> images;

    public RvAdapter(List<String> datas, List<String> images) {
        this.datas = datas;
        this.images = images;
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
                R.layout.rv_item_cardview, parent, false);
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
        if (images.contains("putao")) {
            holder.imageView.setImageResource(R.drawable.putao);
        } else if (images.contains("dapeng")) {
            holder.imageView.setImageResource(R.drawable.dapeng);
        } else {
            holder.imageView.setImageResource(R.drawable.def_img);
        }
        holder.textView.setText(datas.get(position));
        holder.arrow.setImageResource(R.drawable.arrow_right);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, arrow;
        public TextView textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_head);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            arrow = (ImageView) itemView.findViewById(R.id.arrow_right);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getPosition());
                }
            });

            textView.setOnClickListener(
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