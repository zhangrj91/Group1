package com.team1.kingofhonor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team1.kingofhonor.R;
import com.team1.kingofhonor.adapter.MyViewPagerAdapter;
import com.team1.kingofhonor.adapter.gridviewAdapter;
import com.team1.kingofhonor.model.Equipment;
import com.team1.kingofhonor.sqlite.HeroSQLiteHelper;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    private View view;
    private android.support.v7.widget.SearchView searchView;
    private ViewPager viewPager;
    private ViewGroup group;
    private ImageView[] ivPoints;
    private int totalPage;
    private int mPageSize = 8;
    private int currentPage;
    private ArrayList<Equipment> equipmentArrayList;
    private ArrayList<View> viewPagerList;
    private HeroSQLiteHelper heroSQLiteHelper;
    private TextView equip_property;
    private TextView equip_process;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment2, container, false);
        Log.d("aaa","abc");
        searchView = (android.support.v7.widget.SearchView) view.findViewById(R.id.search_equipment);
        initView();
        initData();
        return view;
    }

    public void initView(){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        group = (LinearLayout) view.findViewById(R.id.points);
        equip_property = (TextView) view.findViewById(R.id.equip_property);
        equip_process = (TextView) view.findViewById(R.id.equip_process);
    }

    public void initData(){
        equipmentArrayList = new ArrayList<Equipment>();
        heroSQLiteHelper = new HeroSQLiteHelper(getContext());
        equipmentArrayList = heroSQLiteHelper.getAllEquipment();
        Log.e("aaaaaa",String.valueOf(equipmentArrayList.size()));

        LayoutInflater myinflater = LayoutInflater.from(getContext());
        totalPage = (int) Math.ceil(equipmentArrayList.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < totalPage; i++){
            final GridView gridView = (GridView) myinflater.inflate(R.layout.gridview,viewPager,false);
            gridView.setAdapter(new gridviewAdapter(getContext(),equipmentArrayList, i, mPageSize));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("aaaaaa","nnnnnnn");
                    Equipment equipment = (Equipment)equipmentArrayList.get(i + currentPage*mPageSize);
                    equip_property.setText(equipment.getProperty());
                    equip_process.setText(equipment.getProcess());
                    Toast.makeText(getActivity(),"aaaaaaaaa",Toast.LENGTH_SHORT).show();

                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //设置小圆点
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < totalPage; i++){
            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(30,30);//布局参数,point的布局宽与高
            params.rightMargin = 40;//右边距
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.point_bg);
            if (i == 0){
                imageView.setEnabled(true);
            }
            else {
                imageView.setEnabled(false);
            }
            ivPoints[i] = imageView;
            group.addView(imageView);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setImageBackground(i);
                currentPage = i;
                Log.e("aaa",equipmentArrayList.get(currentPage*mPageSize).getProperty());
                equip_property.setText(equipmentArrayList.get(currentPage*mPageSize).getProperty());
                equip_process.setText(equipmentArrayList.get(currentPage*mPageSize).getProcess());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.drawable.point_focus);
            } else {
                ivPoints[i].setBackgroundResource(R.drawable.point_normal);
            }
        }
    }
}
