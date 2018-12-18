package com.example.kimsujin.termproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;


public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("맛집랭킹");

        TabHost th = (TabHost)findViewById(R.id.tapHost);
        th.setup();

        TabHost.TabSpec tab1 = th.newTabSpec("Tab1"); // 객체 생성
        tab1.setIndicator("한식");  // 화면에 표시할 부분
        tab1.setContent(R.id.tab1);  // 매핑할 컨탠츠 지정
        th.addTab(tab1); // 1번 탭 생성

        TabHost.TabSpec tab2 = th.newTabSpec("Tab2").setIndicator("중식").setContent(R.id.tab2);
        th.addTab(tab2); // 2번 탭 생성

        TabHost.TabSpec tab3 = th.newTabSpec("Tab3").setIndicator("일식").setContent(R.id.tab3);
        th.addTab(tab3); // 3번 탭 생성

        th.setCurrentTab(0);  // 현재 Tab 설정
    }



}
