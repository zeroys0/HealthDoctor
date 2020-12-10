package net.leelink.healthdoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import net.leelink.healthdoctor.R;
import net.leelink.healthdoctor.activity.FeedBackActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.ViewHolder> {
    OnItemJoinClickListener onItemClickListener;
    Context context;
    private int maxImgCount;
    private List<ImageItem> mData;
    private boolean isAdded;   //是否额外添加了最后一个图片

    public void setImages(List<ImageItem> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new ImageItem());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public List<ImageItem> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public JoinAdapter(List<ImageItem> list, Context context, OnItemJoinClickListener onItemClickListener, int maxImgCount) {
//        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.maxImgCount = maxImgCount;
        setImages(list);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // 实例化展示的view

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_item, parent, false); // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.onItemClick(v);
//            }
//        });
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(JoinAdapter.ViewHolder holder, int position) {
        final int clickPosition;
        if (isAdded && position == getItemCount() - 1) {
            holder.img_add.setImageResource(R.drawable.icon_add);
            clickPosition = FeedBackActivity.IMAGE_ITEM_ADD;
        } else {
            ImagePicker.getInstance().getImageLoader().displayImage((Activity) context, mData.get(position).path, holder.img_add, 0, 0);
            clickPosition = position;
        }
        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemAdd(v, clickPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return  mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_add;

        public ViewHolder(View itemView) {
            super(itemView);
            img_add = (ImageView) itemView.findViewById(R.id.img_add);
        }

    }

}
