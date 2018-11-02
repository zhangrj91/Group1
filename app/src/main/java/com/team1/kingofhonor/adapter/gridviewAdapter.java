package com.team1.kingofhonor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team1.kingofhonor.R;
import com.team1.kingofhonor.model.Equipment;

import java.util.ArrayList;

public class gridviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Equipment> list;//数据源
    private int mIndex;// 页数下标，标示第几页，从0开始
    private int mPagerSize;// 每页显示的最大的数量
    private TextView equip_property;
    private TextView equip_process;

    public gridviewAdapter(Context context, ArrayList<Equipment> list, int mIndex, int mPagerSize){
        this.context = context;
        this.list = list;
        this.mIndex = mIndex;
        this.mPagerSize = mPagerSize;
    }


    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        return list.size() > (mIndex + 1) * mPagerSize ? mPagerSize : (list.size() - mIndex * mPagerSize);
    }

    @Override
    public Object getItem(int i) {
        return list.get(i + mPagerSize*mIndex);
    }

    @Override
    public long getItemId(int i) {
        return i + mIndex*mPagerSize;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.equipment_view, null);
            holder.equip_name = (TextView) view.findViewById(R.id.equip_name);
            holder.equip_photo = (ImageView) view.findViewById(R.id.imgUrl);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final int pos = i + mIndex*mPagerSize;
        holder.equip_photo.setImageResource(list.get(pos).getImage());
        holder.equip_name.setText(list.get(pos).getName());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                equip_property = (TextView) view.findViewById(R.id.equip_property);
//                equip_process = (TextView) view.findViewById(R.id.equip_process);
//                Equipment equipment = list.get(pos);
//                Log.e("aaa",equipment.getProperty());
//
//            }
//        });
        return view;
    }

    class ViewHolder{
        private TextView equip_name;
        private ImageView equip_photo;
    }
}
