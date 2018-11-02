package com.team1.kingofhonor.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team1.kingofhonor.HeroDetail;
import com.team1.kingofhonor.R;
import com.team1.kingofhonor.adapter.HeroAdapter;
import com.team1.kingofhonor.model.Hero;
import com.team1.kingofhonor.sqlite.HeroSQLiteHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class Fragment1 extends Fragment {
    private View view;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private HeroAdapter mAdapter;
    private int clickPosition;
    //搜索提示消息
    private ArrayAdapter<String> searchAdapter;
    //picture pager
    private ViewPager imageViewPager;
    private int[] imageArr={R.mipmap.juyoujing,R.mipmap.libai, R.mipmap.kai, R.mipmap.yingzheng};
    private ArrayList<ImageView> imgList;
    private int lastPointPosition;//上一个页面的位置索引
    private LinearLayout pointGroup;//滑动的指示
    //database
    private HeroSQLiteHelper mySQLiteHelper;
    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Hero h) {
        Toast.makeText(getActivity(),h.getName(),Toast.LENGTH_SHORT).show();
        if(mAdapter != null) {
            mAdapter.deleteItem(clickPosition);
            searchAdapter.remove(h.getName());
            searchAdapter.notifyDataSetChanged();
        }
        mySQLiteHelper.deleteHero(h);
        Log.d("delete", "onMessageEvent: delete" + h.getName());
    };

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, container, false);
        mySQLiteHelper= new HeroSQLiteHelper(getContext());

        //view pager
        pointGroup = view.findViewById(R.id.point_group);
        imageViewPager = view.findViewById(R.id.hero_upper_pager);
        imgList= new ArrayList<>();
        for (int i=0;i<imageArr.length;i++) {
            //初始化图片
            ImageView image=new ImageView(getActivity());
            image.setBackgroundResource(imageArr[i]);
            imgList.add(image);
            //添加图片的指示点
            ImageView point=new ImageView(getActivity());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(30,30);//布局参数,point的布局宽与高
            params.rightMargin = 40;//右边距
            point.setLayoutParams(params);//设置布局参数
            point.setBackgroundResource(R.drawable.point_bg);//point_bg是根据setEnabled的值来确定形状的
            if(i==0){
                point.setEnabled(true);//初始化的时候设置第一张图片的形状
            }else{
                point.setEnabled(false);//根据该属性来确定这个图片的显示形状
            }
            pointGroup.addView(point);//将该指示的图片添加到布局中
        }
        imageViewPager.setAdapter(new ImagePagerAdapter());
        //为viewPager设置监听
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面改变的时候调用(稳定),positon表示被选中的索引
            @Override
            public void onPageSelected(int position) {
                //改变指示点的状态
                pointGroup.getChildAt(position).setEnabled(true);//将当前点enbale设置为true
                pointGroup.getChildAt(lastPointPosition).setEnabled(false);//将上一个点设置为false
                lastPointPosition=position;
            }
            //页面正在滑动的时候调用,position指的是左侧页面的索引,positionOffset代表偏移量[0,1]的范围,positionOffsetPixels也是偏移量,不过是像素点的偏移量 范围[0,显示的控件的绝对长度]
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            //页面滚动状态发送改变的时候回调
            public void onPageScrollStateChanged(int state) {
                //当手指点击屏幕滚动的时状态码为1,当手指离开viewpager自动滚动的状态码为2,自动滚动选中了显示了页面的时候状态码为0
            }
        });

        //hero list
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mAdapter = new HeroAdapter(mySQLiteHelper.getAllHeroes());
        mRecyclerView = view.findViewById(R.id.hero_recyclerview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(40));
        mAdapter.setOnItemClickListener(new HeroAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HeroDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Click_Hero", mAdapter.getItem(position));
                intent.putExtras(bundle);
                clickPosition = position;
                startActivityForResult(intent, 0);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        //search
//        final SearchView searchView = view.findViewById(R.id.hero_edit_search);
//        AutoCompleteTextView completeText = searchView.findViewById(R.id.search_src_text) ;
//        completeText.setThreshold(0);
//        searchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mAdapter.getAllNames());
//        completeText.setAdapter(searchAdapter);
//        completeText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,int position,long id){
//                searchView.setQuery(searchAdapter.getItem(position),true);
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query){
//                Toast.makeText(getActivity(),query + clickPosition ,Toast.LENGTH_SHORT).show();
//                Hero temp_hero = mAdapter.getItemByName(query);
//                if(temp_hero != null){
//                    Intent intent = new Intent(getActivity(), HeroDetail.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("Click_Hero", temp_hero);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, 0);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText){
//                return false;
//            }
//        });
//
//        //展开状态
//        searchView.setIconified(false);
//        //清除焦点
//        searchView.clearFocus();
//        //键盘上显示搜索图标
//        searchView.isSubmitButtonEnabled();
//
//        //hero category
//        //静态实现的下拉框，数据写在cityInfo.xml文件中
//        Spinner hero_spinner = view.findViewById(R.id.hero_category);
//        hero_spinner.setOnItemSelectedListener(new HeroCategorySelected());
        return view;
    }

    //图片轮换的适配器
    private class ImagePagerAdapter extends PagerAdapter {
        /**
         * 获得页面的总数
         */
        @Override
        public int getCount() {
            return imageArr.length;
        }
        /**
         * 判断view和object的对应关系,如果当前要显示的控件是来之于instantiateItem方法创建的就显示,否则不显示
         * object 为instantiateItem方法返回的对象
         * 如果为false就不会显示该视图
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        /**
         * 实例化下一个要显示的子条目,获取相应位置上的view,这个为当前显示的视图的下一个需要显示的控件
         * container  view的容器,其实就是viewager自身
         * position   ViewPager相应的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imgList.get(position));
            return imgList.get(position);
        }
        /**
         * 销毁一个子条目,object就为instantiateItem方法创建的返回的对象,也是滑出去需要销毁了的视图对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object=null;
        }
    }
    //recyclerview居中的间距设置
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) %3==0) {
                outRect.left = 0;
            }
        }

    }
    //下拉框选择事件
    private class HeroCategorySelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            mAdapter.updateWithCategory(mySQLiteHelper.getAllHeroes(),parent.getItemAtPosition(position).toString());


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }

    }
    //一定要记得取消注册，不然经常创建页面会显示重复注册
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


}