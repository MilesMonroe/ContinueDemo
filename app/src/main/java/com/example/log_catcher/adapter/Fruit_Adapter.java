package com.example.log_catcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.log_catcher.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Fruit_Adapter extends ArrayAdapter<Fruit> {
    private int resourceId;
    private Context mContext;

    public Fruit_Adapter(Context context, int resourceId, List<Fruit> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        Fruit fruit = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            //加载list的基布局
            view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        //填充组件内容
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getImageId());

        return view;
    }

    class ViewHolder {
         ImageView fruitImage;
         TextView  fruitName;
    }
}
