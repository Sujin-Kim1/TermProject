package com.example.kimsujin.termproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Restaurant implements Comparable<Restaurant> {
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;

    public int getRank() {
        return this.rank;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAddress() {
        return this.address;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int compareTo(Restaurant restaurant) {
        if (this.rank > restaurant.rank) {
            return 1;
        } else if (this.rank == restaurant.rank) {
            return 0;
        } else {
            return -1;
        }
    }
}

public class RankingActivity extends AppCompatActivity {

    final int TYPE_KOREAN = 0;
    final int TYPE_JAPANESE = 1;
    final int TYPE_CHINESE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("맛집랭킹");

        TabHost th = (TabHost) findViewById(R.id.tapHost);
        th.setup();

        TabHost.TabSpec tab1 = th.newTabSpec("Tab1"); // 객체 생성
        tab1.setIndicator("한식"); // 화면에 표시할 부분
        tab1.setContent(R.id.tab1); // 매핑할 컨탠츠 지정
        th.addTab(tab1); // 1번 탭 생성

        TabHost.TabSpec tab2 = th.newTabSpec("Tab2").setIndicator("중식").setContent(R.id.tab2);
        th.addTab(tab2); // 2번 탭 생성

        TabHost.TabSpec tab3 = th.newTabSpec("Tab3").setIndicator("일식").setContent(R.id.tab3);
        th.addTab(tab3); // 3번 탭 생성

        th.setCurrentTab(0); // 현재 Tab 설정

        if (isNetworkAvailable()) {
            RankingActivity.DownloadTask downloadTask1 = new RankingActivity.DownloadTask(TYPE_KOREAN);
            downloadTask1.execute("http://10.0.2.2:9001/api/korean");
            RankingActivity.DownloadTask downloadTask2 = new RankingActivity.DownloadTask(TYPE_JAPANESE);
            downloadTask2.execute("http://10.0.2.2:9001/api/japanese");
            RankingActivity.DownloadTask downloadTask3 = new RankingActivity.DownloadTask(TYPE_CHINESE);
            downloadTask3.execute("http://10.0.2.2:9001/api/chinese");
        } else {
            Toast.makeText(getBaseContext(),
                    "Network is not Available", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private boolean isNetworkAvailable() {
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            available = true;

        return available;
    }

    private String downloadUrl(String strUrl) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.d("Exception download", e.toString());
        } finally {

        }
        return "{}";
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String s = null;
        int type = -1;

        @Override
        protected String doInBackground(String... url) {
            try {
                s = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return s;
        }

        DownloadTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Restaurant[] restaurants = gson.fromJson(result, Restaurant[].class);
            Arrays.sort(restaurants);

            TextView[] name = null;
            TextView[] address = null;
            TextView[] phone = null;

            switch (type) {
                case TYPE_KOREAN:
                    // korean tab 의 내용을 변경
                    name = new TextView[]{findViewById(R.id.name21), findViewById(R.id.name22), findViewById(R.id.name23)};
                    address = new TextView[]{findViewById(R.id.address21), findViewById(R.id.address22), findViewById(R.id.address23)};
                    phone = new TextView[]{findViewById(R.id.phone21), findViewById(R.id.phone22), findViewById(R.id.phone23)};
                    break;
                case TYPE_CHINESE:
                    // chinese tab 의 내용을 변경
                    name = new TextView[]{findViewById(R.id.name11), findViewById(R.id.name12), findViewById(R.id.name13)};
                    address = new TextView[]{findViewById(R.id.address11), findViewById(R.id.address12), findViewById(R.id.address13)};
                    phone = new TextView[]{findViewById(R.id.phone11), findViewById(R.id.phone12), findViewById(R.id.phone13)};
                    break;
                case TYPE_JAPANESE:
                    // japanese tab 의 내용을 변경
                    name = new TextView[]{findViewById(R.id.name31), findViewById(R.id.name32), findViewById(R.id.name33)};
                    address = new TextView[]{findViewById(R.id.address31), findViewById(R.id.address32), findViewById(R.id.address33)};
                    phone = new TextView[]{findViewById(R.id.phone31), findViewById(R.id.phone32), findViewById(R.id.phone33)};
                    break;
            }
            if (name != null) {
                for (int i = 0; i < name.length; i++) {
                    name[i].setText(restaurants[i].getName());
                    address[i].setText(restaurants[i].getAddress());
                    phone[i].setText(restaurants[i].getPhone());
                }
            }
        }
    }

}