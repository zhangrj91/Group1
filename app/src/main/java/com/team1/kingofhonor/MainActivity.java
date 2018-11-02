package com.team1.kingofhonor;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.idescout.sql.SqlScoutServer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.team1.kingofhonor.fragment.Fragment1;
import com.team1.kingofhonor.fragment.Fragment2;
import com.team1.kingofhonor.fragment.Fragment3;
import com.team1.kingofhonor.model.Equipment;
import com.team1.kingofhonor.model.Hero;
import com.team1.kingofhonor.sqlite.HeroSQLiteHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private HeroSQLiteHelper heroSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SqlScoutServer.create(this, getPackageName());
        //添加数据库
        heroSQLiteHelper = new HeroSQLiteHelper(this);
        heroSQLiteHelper.deleteAllHeroes();
        //下面用来添加数据，数据库是永久的，所以不需要再添加了
        heroSQLiteHelper.addHero(new Hero("橘右京",R.mipmap.juyoujing, "神梦一刀", "刺客"));
        heroSQLiteHelper.addHero(new Hero("李白",R.mipmap.libai,"青莲剑仙", "刺客"));
        heroSQLiteHelper.addHero(new Hero("凯",R.mipmap.kai,"破灭刀锋", "战士"));
        heroSQLiteHelper.addHero(new Hero("嬴政",R.mipmap.yingzheng,"王者独尊", "法师"));
        //添加各个页面
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.title1, Fragment1.class)
                .add(R.string.title2, Fragment2.class)
                .add(R.string.title3, Fragment3.class)
                .create());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        //添加装备数据
        heroSQLiteHelper.deleteAllEquipments();
        for(int i = 0; i < 20; i++)
        {
            if (i != 8)
                heroSQLiteHelper.addEquipment(new Equipment(String.valueOf(i), "攻击速度10%,物理攻击40点,唯一被动：破甲：+10%物理穿透（远程英雄使用时效果翻倍）", "穿云弓", R.mipmap.chuanyungong));
            else
                heroSQLiteHelper.addEquipment(new Equipment(String.valueOf(i), "!!!!!!!!", "短刃", R.mipmap.chuanyungong));
        }

        ArrayList<Equipment> list = heroSQLiteHelper.getAllEquipment();
        Log.e("aeaefaef",String.valueOf(list.size()));
    }

}
