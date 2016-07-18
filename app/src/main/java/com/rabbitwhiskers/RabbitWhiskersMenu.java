package com.rabbitwhiskers;

//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RabbitWhiskersMenu extends Activity {

	private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rabbit_whiskers_menu);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
		//LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
		// adView を作成する
		//adView = new AdView(this, AdSize.BANNER, "ca-app-pub-3217012767112748/9825611110");
		//adView = new AdView(this, AdSize.BANNER, "a151012a8bf229d");
		//layout.addView(adView);
		//AdRequest request = new AdRequest();

		//adView.loadAd(request);

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

	@Override
	public void onDestroy() {
        mAdView.destroy();
		super.onDestroy();
     }
}
