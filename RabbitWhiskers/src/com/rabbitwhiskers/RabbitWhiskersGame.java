package com.rabbitwhiskers;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class RabbitWhiskersGame extends Activity implements GLSurfaceView.Renderer {
	// GLSurfaceView
	private GLSurfaceView gLSurfaceView;
	// 成功と失敗のカウント
	static int okcnt;
	static int ngcnt;
	// スピードアップ回数
	static int up;
	// タッチ座標
	static float touchX;
	static float touchY;
	static float moveX1;
	static float moveY1;
	static float moveX2;
	static float moveY2;
	// サウンドＩＤ
	static SoundPool soundPool;
	static int volume;
	static int senyaa;
	static int seita;
	static int setempo;
	static int sesyu;
	static int sespeedup;
	// ディスプレイサイズ
	static int width;
	static int height;
	// グラフィックＩＤ
	static int backgroundID;
	private int readyID;
	private int speedupID;
	static int oneID;
	static int twoID;
	static int startID;
	static int boardID;
	static int nomalRabbitID;
	static int okRabbitID;
	static int ngRabbitID;
	static int longWhiskersID;
	static int shortWhiskersID;
	static int numberID;
	static int ngID;
	static int shuID;
	// ゲームの状態
	static int gameState;
	// 基準位置
	static float x_left, x_right, y_top, y_under;
	// テンポの効果音フラグ
	static int tempo0, tempo1, tempo2, tempo3;
	// ゲームの時間
	static long gameStartTime;
	static long gameRapTime;
	// ディスプレイサイズに合わせたスケール
	private float readyScale;
	static float scale;

	// うさぎの設定情報を保持するクラス
	class RabbitBase {
		// ひげの初期位置を設定
		public RabbitBase() {
    		x1 = x_left;
    		y1 = y_top;
    		x2 = x_right;
    		y2 = y_top;
    		x3 = x_left;
    		y3 = y_under;
    		x4 = x_right;
    		y4 = y_under;
		}
		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;
		int rabbitBaseID;
		int startPosX;
		int movePosX;
		int nextFlg;
	}
	private RabbitBase rabbitBase1;
	private RabbitBase rabbitBase2;

	// テンポ
	static final int READY_TIME = 2000;
	static int RAP_TIME;
	static int MARGIN_TIME;
	static int SPEEDUP_CNT;
	static int MOVE_SPEED;
	// ゲームのステータス
	static final int READY = 1;
	static final int PLAYING = 2;
	static final int GAMEOVER = 3;
	// うさぎの基準のサイズ
	static final int R_B_HEIGHT = 800;
	static final int R_B_WIDTH = 480;
	// ひげの基準位置
	static final int W_BASE_X = 150;
	// うさぎの表示位置調整
	static final float POSITION = 180f;

	/*
	 * コンストラクタ
	 */
	public RabbitWhiskersGame() {
		RAP_TIME = 700;
		MARGIN_TIME = 200;
		SPEEDUP_CNT = 3;
		MOVE_SPEED = 30;
		up = 0;
		okcnt = 0;
		ngcnt = 0;
		tempo0 = 0;
		tempo1 = 0;
		tempo2 = 0;
		tempo3 = 0;
		readyScale = 0.5f;
	}

	/*
	 * 画面の生成時(非 Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // サウンドプレイヤーの生成
        Context context = this.getApplicationContext();
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = audio.getStreamVolume(AudioManager.STREAM_RING);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        senyaa = soundPool.load(context, R.raw.rabbit_nyaa, 1);
        seita = soundPool.load(context, R.raw.rabbit_ita, 1);
        setempo = soundPool.load(context, R.raw.tempo, 1);
        sesyu = soundPool.load(context, R.raw.tempo_syu, 1);
        sespeedup = soundPool.load(context, R.raw.tempo_speedup, 1);

        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display disp = wm.getDefaultDisplay();
        // ディスプレイのサイズ取得
        width = disp.getWidth();
        height = disp.getHeight();

        // 画面の解像度に応じてスケールを変更
        if (0 < height - R_B_HEIGHT && height - R_B_HEIGHT < width - R_B_WIDTH) {
        	// 拡大
        	scale = (float)height / (float)R_B_HEIGHT;
        } else if (0 < width - R_B_WIDTH && height - R_B_HEIGHT > width - R_B_WIDTH){
        	// 拡大
        	scale = (float)width / (float)R_B_WIDTH;
        } else if (0 > height - R_B_HEIGHT && height - R_B_HEIGHT < width - R_B_WIDTH) {
        	// 縮小
        	scale = (float)height / (float)R_B_HEIGHT;
        } else {
        	// 縮小
        	scale = (float)width / (float)R_B_WIDTH;
        }
        // ひげの基準位置を設定
		x_left = width/2-W_BASE_X*scale;
		y_top = height/2;
		x_right = width/2+W_BASE_X*scale;
		y_under = height/2-60*scale;
        // うさぎの初期位置を設定
        rabbitBase1 = new RabbitBase();
        rabbitBase2 = new RabbitBase();
        rabbitBase2.startPosX = width;
        rabbitBase2.movePosX = width;
        // ステータスの初期化
        RabbitDrawer.initRabbitDrawer(rabbitBase1);
        RabbitDrawer.initRabbitDrawer(rabbitBase2);

        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // GLSurfaceViewを作成
        gLSurfaceView = new GLSurfaceView(this);
        // レンダラーを生成してセット
        gLSurfaceView.setRenderer(this);

        // レイアウトのリソース参照は渡さず直接Viewオブジェクトを渡す
        setContentView(gLSurfaceView);
    }

    /*
     * ひげをタッチしたときのイベント(非 Javadoc)
     * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
     */
    public boolean onTouchEvent(MotionEvent event) {
    	if (gameState == PLAYING) {
    		switch (event.getAction()) {
    			// タッチされた瞬間
    			case MotionEvent.ACTION_DOWN:
    				touchX = (float)event.getX();
    				touchY = (float)(height - event.getY()+POSITION*scale);
    				moveX1 =touchX;
        			moveX2 = touchX;
        			moveY1 = touchY;
        			moveY2 = touchY;
        			RabbitDrawer.setTouchTime();
        			break;
        		// ひげを抜く
        		case MotionEvent.ACTION_MOVE:
        			moveX2 = (float)event.getX();
        			moveY2 = (float)(height - event.getY()+POSITION*scale);
        			// ある程度移動したら、移動したと判定する
        			if (Math.abs(moveX2 - touchX) > 10 || Math.abs(moveY2 - touchY) > 10) {
        				if (tempo3 == 0) {
        					soundPool.play(sesyu, (float)volume, (float)volume, 0, 0, 1.0f);
        					tempo3 = 1;
        				}
        			}
        			RabbitDrawer.setMoveTime();
        			break;
    		}
    	} else {
    		touchX = 0;
    		touchY = 0;
    		moveX1 = 0;
    		moveY1 = 0;
    		moveX2 = 0;
    		moveY2 = 0;
    	}
        return true;
    }

    /*
     * フレーム毎に呼び出される(非 Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
     */
    public void onDrawFrame(GL10 gl) {
    	// 描画用バッファをクリア
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	// 背景を描画
    	TextureDrawer.drawTexture(gl, backgroundID, width/2, height/2, width, height, 0.0f, 1.0f, 1.0f);
    	// スコアボードを描画
    	RabbitDrawer.drawSuccessFailure(gl);
    	RabbitDrawer.drawScoreborad(gl);
    	gameRapTime = System.currentTimeMillis();

    	if (gameRapTime - gameStartTime <= READY_TIME) { // readyを描画
    		gameState = READY;
    		readyScale -= 0.02f;
    		if (readyScale < 0.0f) {
    			readyScale = 0.0f;
    		}
    		if (tempo0 == 0) {
    			soundPool.play(sespeedup, (float)volume, (float)volume, 0, 0, 1.0f);
    			tempo0 = 1;
    		}
    		if (up == 0) {
    			TextureDrawer.drawTexture(gl, readyID, width/2, height/2, width, width, 0.0f, scale+readyScale, scale+readyScale);
    		} else {
    			TextureDrawer.drawTexture(gl, speedupID, width/2, height/2, width, width, 0.0f, scale+readyScale, scale+readyScale);
    		}
    	} else if (gameRapTime - gameStartTime <= READY_TIME + RAP_TIME) { // 1を描画
    		if (tempo1 == 0) {
    			soundPool.play(setempo, (float)volume, (float)volume, 0, 0, 1.0f);
    			tempo1 = 1;
    		}
    		TextureDrawer.drawTexture(gl, oneID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else if (READY_TIME + RAP_TIME < gameRapTime - gameStartTime && gameRapTime - gameStartTime <= READY_TIME + RAP_TIME*2) { // 2を描画
    		if (tempo2 == 0) {
    			soundPool.play(setempo, (float)volume, (float)volume, 0, 0, 1.0f);
    			tempo2 = 1;
    		}
    		TextureDrawer.drawTexture(gl, twoID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else if (READY_TIME + RAP_TIME*2 < gameRapTime - gameStartTime && gameRapTime - gameStartTime <= READY_TIME + RAP_TIME*3) { // startを描画
    		if (tempo3 == 0) {
    			soundPool.play(sesyu, (float)volume, (float)volume, 0, 0, 1.0f);
    			tempo3 = 1;
    		}
    		TextureDrawer.drawTexture(gl, startID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else {
    		gameState = PLAYING;
    		RabbitDrawer.cycleTime(gl);
        	RabbitDrawer.judgmentTiming(gl, soundPool, rabbitBase1, rabbitBase2);
        	 // うさぎを描画
    		RabbitDrawer.rabbitDraw(gl, rabbitBase1);
    		RabbitDrawer.rabbitDraw(gl, rabbitBase2);
    		if (gameState == GAMEOVER) {
    			gameOver();
    		}
    	}
    }

    /*
     * 画面が縦または横になった場合に呼び出される(非 Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	// ビューボートをサイズに合わせてセットしなおす
    	gl.glViewport(0, 0, width, height);
        // カメラ位置をセット
        GLU.gluLookAt(gl, 2.0f, 2.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

    	// 射影行列を選択
    	gl.glMatrixMode(GL10.GL_PROJECTION);
    	// 現在選択されている行列(射影行列)に、単位行列をセット
    	gl.glLoadIdentity();
    	// 平行投影用のパラメータをセット
    	GLU.gluOrtho2D(gl, 0.0f, width, 0.0f, height);
    }

    /*
     * サーフェイスが生成される際、または再生成される際に呼び出される(非 Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	// ディザを無効化
    	gl.glDisable(GL10.GL_DITHER);
    	// カラーとテクスチャ座標の補完精度を最も効率的なものに指定
    	gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
    	// バッファ初期化時のカラー情報をセット
    	gl.glClearColor(0, 0, 0, 1);
    	// 片面表示を有効に
    	gl.glEnable(GL10.GL_CULL_FACE);
    	// カリング設定をCCWに
    	gl.glFrontFace(GL10.GL_CCW);
    	// 深度テストを有効に
    	gl.glDisable(GL10.GL_DEPTH_TEST);
    	// スムースシェーディングにセット
    	gl.glShadeModel(GL10.GL_FLAT);

    	// リソース読み込み
    	backgroundID = TextureLoader.loadTexture(gl, this, R.drawable.background_game);
    	readyID = TextureLoader.loadTexture(gl, this, R.drawable.ready);
    	speedupID = TextureLoader.loadTexture(gl, this, R.drawable.speedup);
    	oneID = TextureLoader.loadTexture(gl, this, R.drawable.one);
    	twoID = TextureLoader.loadTexture(gl, this, R.drawable.two);
    	startID = TextureLoader.loadTexture(gl, this, R.drawable.start);
    	boardID = TextureLoader.loadTexture(gl, this, R.drawable.board);
    	nomalRabbitID = TextureLoader.loadTexture(gl, this, R.drawable.rabbit_base);
    	rabbitBase1.rabbitBaseID = nomalRabbitID;
    	rabbitBase2.rabbitBaseID = nomalRabbitID;
    	okRabbitID = TextureLoader.loadTexture(gl, this, R.drawable.rabbit_base_ok);
    	ngRabbitID = TextureLoader.loadTexture(gl, this, R.drawable.rabbit_base_ng);
    	longWhiskersID = TextureLoader.loadTexture(gl, this, R.drawable.long_whiskers);
    	shortWhiskersID = TextureLoader.loadTexture(gl, this, R.drawable.short_whiskers);
    	numberID = TextureLoader.loadTexture(gl, this, R.drawable.number);
    	ngID = TextureLoader.loadTexture(gl, this, R.drawable.ng);
    	shuID = TextureLoader.loadTexture(gl, this, R.drawable.shu);

    	// 開始時間を取得
    	gameStartTime = System.currentTimeMillis();
    }

    public void gameOver() {
		// インテントのインスタンス生成
		Intent intent = new Intent(RabbitWhiskersGame.this, RabbitRanking.class);
		intent.putExtra("SCORE", okcnt);
		// 次画面のアクティビティ起動
		startActivity(intent);
		finish();
    }

	// ポーズ状態からの復旧時やアクティビティ生成時などに呼び出される
	protected void inResume() {
		super.onResume();
		gLSurfaceView.onResume();
	}

	// アクティビティ一時停止や終了時に呼び出される
	protected void onPause() {
		super.onPause();
		gLSurfaceView.onPause();
		// サウンドプールを開放する
		soundPool.release();
		// 一時停止した場合はタイトル画面に戻る
		finish();
	}

}