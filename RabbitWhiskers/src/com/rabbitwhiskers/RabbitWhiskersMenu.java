package com.rabbitwhiskers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RabbitWhiskersMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rabbit_whiskers_menu);

        // タイトルのフォント設定
        TextView text1 = (TextView)findViewById(R.id.textView1);
        text1.setTypeface(Typeface.createFromAsset(getAssets(), "GlassAntiqua-Regular.ttf"));

        Button btnStart = (Button)findViewById(R.id.start_button);
        btnStart.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// インテントのインスタンス生成
        		Intent intent = new Intent(RabbitWhiskersMenu.this, RabbitWhiskersGame.class);
        		//intent.putExtra("LEVEL", 3);
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
