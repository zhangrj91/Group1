package com.team1.kingofhonor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.team1.kingofhonor.model.Hero;
import org.greenrobot.eventbus.EventBus;

public class HeroDetail extends AppCompatActivity {
    EditText heroName, heroAlias;
    ImageView heroImage;
    private Hero displayHero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        displayHero = (Hero)getIntent().getSerializableExtra("Click_Hero");
        heroName = findViewById(R.id.hero_detail_name);
        heroAlias = findViewById(R.id.hero_detail_alias);
        heroImage = findViewById(R.id.hero_detail_image);

        heroName.setText(displayHero.getName());
        heroAlias.setText(displayHero.getAlias());
        heroImage.setImageResource(displayHero.getImage());

        ImageButton imageButton = findViewById(R.id.hero_detail_delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HeroDetail.this);
                alertDialog.setTitle("提示").setMessage("是否确定删除英雄: " + displayHero.getName() + "？").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(displayHero);
                                startActivity(new Intent(HeroDetail.this, MainActivity.class));

                            }
                        }).setNegativeButton("取消",null).create().show();
            }
        });

    }
}
