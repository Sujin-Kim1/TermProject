package com.example.kimsujin.termproject;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Pick1Activity extends AppCompatActivity {

    TextView text;
    ImageButton prevButton, nextButton;
    ImageView imageView;

    final int Pick1[] = {R.drawable.pick00, R.drawable.pick01, R.drawable.pick02, R.drawable.pick03};
    private int index = 0;
    private int maxIndex = Pick1.length;
    private ToggleButton toggleButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick1);
        setTitle("감성 넘치는 카페");
        toggleButton = (ToggleButton)findViewById(R.id.toggle2);

        // 가게 이름과 정보 입력할 text 변수 생성
        text = (TextView)findViewById(R.id.details);

        prevButton = (ImageButton)findViewById(R.id.prevButton);
        nextButton = (ImageButton)findViewById(R.id.nextButton);
        imageView = (ImageView)findViewById(R.id.pick1_image);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (--index < 0) {
                    index += maxIndex;
                }
                imageView.setImageResource(Pick1[index]);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++index >= maxIndex) {
                    index -= maxIndex;
                }
                imageView.setImageResource(Pick1[index]);
            }
        });


    }

    public void btnMethod(View view) {
        if (!toggleButton.isChecked()) {
            NetworkThread thread = new NetworkThread();
            thread.start();
            toggleButton.setChecked(false);
        } else {
            text.setText("");
            toggleButton.setChecked(true);
        }
    }

    class NetworkThread extends Thread {
        @Override
        public void run() {
            try {
                String site = "http://10.0.2.2:9001/api/pick";
                URL siteUrl = new URL(site);

                URLConnection conn = siteUrl.openConnection();

                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                do {
                    str = br.readLine();
                    if (str != null) {
                        buf.append(str);
                    }
                } while (str != null);

                String data = buf.toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("");
                    }
                });

                // JSON 배열 객체 생성
                JSONArray root = new JSONArray(data);

                for (int i = 0; i < 4; i++) {
                    // i 번째 객체 추출
                    JSONObject object = root.getJSONObject(i);
                    // 추출한 JSON 객체로부터 데이터 추출
                    final String name = object.getString("name");
                    final String info = object.getString("info");
                    final String url = object.getString("url");
                    final String address = object.getString("address");
                    final String time = object.getString("time");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.append(name + "\n\n");
                            text.append(info + '\n');
                            text.append("홈페이지: " + url + '\n');
                            text.append("주소: " + address + '\n');
                            text.append("time : " + time + "\n\n");
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
