package com.rabbitwhiskers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class RabbitWhiskersMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rabbit_whiskers_menu);

        // フォントを取得
        Typeface tf = Typeface.createFromAsset(getAssets(), "GlassAntiqua-Regular.ttf");
        // タイトルのフォント設定
        TextView text1 = (TextView)findViewById(R.id.textView1);
        text1.setTypeface(tf);

        Button btnStart = (Button)findViewById(R.id.start_button);
        btnStart.setTypeface(tf);
        btnStart.setTextSize(20.0f);
        btnStart.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// インテントのインスタンス生成
        		Intent intent = new Intent(RabbitWhiskersMenu.this, RabbitWhiskersGame.class);
        		// 次画面のアクティビティ起動
        		startActivity(intent);
        	}
        });

        Button btnRanking = (Button)findViewById(R.id.ranking_button);
        btnRanking.setTypeface(tf);
        btnRanking.setTextSize(20.0f);
        btnRanking.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// インテントのインスタンス生成
        		Intent intent = new Intent(RabbitWhiskersMenu.this, RabbitRanking.class);
        		// 次画面のアクティビティ起動
        		startActivity(intent);
        	}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rabbit_whiskers_menu, menu);
        return true;
    }
}
