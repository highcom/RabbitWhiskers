package com.rabbitwhiskers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class RabbitRanking extends Activity implements AdListener{
	// ランキングのファイル
	private final String RANK_FILE = "ranking.dat";
	private final int RANK_MAX = 3;

	private String unitID = "ca-app-pub-3217012767112748/6007027511";
	private InterstitialAd interstitialAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Context context = this;

		int  score;
		int score_flg = 0;
		int[] rank;
		rank = new int[4];

		super.onCreate(savedInstanceState);
		// タイトルバーを消す
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rabbit_whiskers_ranking);

		// インタースティシャルを作成する。
		interstitialAd = new InterstitialAd(this, unitID);
	    // 広告リクエストを作成する。
	    AdRequest adRequest = new AdRequest();
	    // インタースティシャルの読み込みを開始する。
	    interstitialAd.loadAd(adRequest);
	    // Ad Listener を設定して下のコールバックを使用する
	    interstitialAd.setAdListener((AdListener) this);

		// ラインを引く
		TextView lineView1 = (TextView) findViewById(R.id.lineView1);
		lineView1.setBackgroundResource(R.layout.line);
		TextView lineView2 = (TextView) findViewById(R.id.lineView2);
		lineView2.setBackgroundResource(R.layout.line);
		TextView lineView3 = (TextView) findViewById(R.id.lineView3);
		lineView3.setBackgroundResource(R.layout.line);
		TextView lineView4 = (TextView) findViewById(R.id.lineView4);
		lineView4.setBackgroundResource(R.layout.line);

		// フォントを取得
		Typeface tf = Typeface.createFromAsset(getAssets(), "GlassAntiqua-Regular.ttf");

		TextView text1 = (TextView)findViewById(R.id.RankView);
		text1.setTypeface(tf);

		// タイムを取得
		Intent intent = getIntent();
		score = intent.getIntExtra("SCORE", -1);
		// 今回のスコアを表示
		if (score != -1) {
			TextView text = (TextView)findViewById(R.id.NowView);
			text.setText("This score is " + score);
			text.setTypeface(tf);
		}

		// ファイルから今までの記録を読み込み
		DataInputStream in = null;
		try {
			FileInputStream file = context.openFileInput(RANK_FILE);
			in = new DataInputStream(file);
			for(int i = RANK_MAX-1; i >=0; i--) {
				rank[i] = in.readInt();
			}
			in.close();
		}  catch(final FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}

		// 今回のタイムを追加
		rank[RANK_MAX] = score;
		// ソートしてランキング形式で表示
		Arrays.sort(rank);
		// ランキングを表示
		TextView head_1st = (TextView)findViewById(R.id.HeadView_1st);
		head_1st.setTypeface(tf);
		TextView text_1st = (TextView)findViewById(R.id.RankView_1st);
		text_1st.setTypeface(tf);
		text_1st.setText("" + rank[RANK_MAX]);
		if (rank[RANK_MAX] == score && score_flg == 0) {
			text_1st.setTextColor(0xffff0000);
			score_flg = 1;
		}
		TextView head_2nd = (TextView)findViewById(R.id.HeadView_2nd);
		head_2nd.setTypeface(tf);
		TextView text_2nd = (TextView)findViewById(R.id.RankView_2nd);
		text_2nd.setTypeface(tf);
		text_2nd.setText("" + rank[RANK_MAX-1]);
		if (rank[RANK_MAX-1] == score && score_flg == 0) {
			text_2nd.setTextColor(0xffff0000);
			score_flg = 1;
		}
		TextView head_3rd = (TextView)findViewById(R.id.HeadView_3rd);
		head_3rd.setTypeface(tf);
		TextView text_3rd = (TextView)findViewById(R.id.RankView_3rd);
		text_3rd.setTypeface(tf);
		text_3rd.setText("" + rank[RANK_MAX-2]);
		if (rank[RANK_MAX-2] == score && score_flg == 0) {
			text_3rd.setTextColor(0xffff0000);
			score_flg = 1;
		}

		// ゲーム終了後の場合だけ書き込みを行う
		if (score != 0) {
			// 更新したランキングをファイルに書き込み
			DataOutputStream out = null;
			try {
				FileOutputStream file = context.openFileOutput(RANK_FILE, Context.MODE_PRIVATE);
				out = new DataOutputStream(file);
				// 4個目はランク外として書かない。
				for (int i = RANK_MAX; i > 0; i--) {
					out.writeInt(rank[i]);
				}
				out.flush();
				file.close();
			}  catch (final FileNotFoundException e) {
				System.out.println("ファイルが見つかりません。");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Button btnBack = (Button)findViewById(R.id.back_button);
		btnBack.setTypeface(tf);
		btnBack.setTextSize(20.0f);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
     }

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onReceiveAd(Ad ad) {
	    if (ad == interstitialAd) {
	    	// 25%の確立で広告を表示させる
	    	if ((int)(Math.random()*100.0d)%4 == 0)
	    	{
	    		interstitialAd.show();
	    	}
	    }
	}
}