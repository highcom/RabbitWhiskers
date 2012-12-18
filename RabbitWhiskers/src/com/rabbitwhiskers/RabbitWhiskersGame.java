package com.rabbitwhiskers;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
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
	static float touchX;
	static float touchY;
	static float moveX1;
	static float moveY1;
	static float moveX2;
	static float moveY2;
	SoundPool soundPool;
	static int volume;
	static int senyaa;
	static int seita;
	static int width;
	static int height;
	static int backgroundID;
	private int readyID;
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
	static int shuID;
	// 基準位置
	static float x_left, x_right, y_top, y_under;

	static final int READY_TIME = 2000;
	static final int RAP_TIME = 700;
	private int gameState;
	private long startTime;
	private long rapTime;

	private float readyScale;
	static float scale;

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

	private final int READY = 1;
	private final int PLAYING = 2;
	private final int END = 3;
	// うさぎの基準のサイズ
	static final int R_B_HEIGHT = 800;
	static final int R_B_WIDTH = 480;
	// ひげの基準位置
	static final int W_BASE_X = 150;
	// うさぎの表示位置調整
	static final float POSITION = 180f;

	public RabbitWhiskersGame() {
		readyScale = 0.5f;
	}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // サウンドプレイヤーの生成
        Context context = this.getApplicationContext();
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = audio.getStreamVolume(AudioManager.STREAM_RING);
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        senyaa = soundPool.load(context, R.raw.rabbit_nyaa, 1);
        seita = soundPool.load(context, R.raw.rabbit_ita, 1);

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

        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // GLSurfaceViewを作成
        gLSurfaceView = new GLSurfaceView(this);
        // レンダラーを生成してセット
        gLSurfaceView.setRenderer(this);

        // レイアウトのリソース参照は渡さず直接Viewオブジェクトを渡す
        setContentView(gLSurfaceView);
    }

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

    public void onDrawFrame(GL10 gl) {
    	// 描画用バッファをクリア
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	// 背景を描画
    	TextureDrawer.drawTexture(gl, backgroundID, width/2, height/2, width, height, 0.0f, 1.0f, 1.0f);
    	rapTime = System.currentTimeMillis();

    	if (rapTime - startTime <= READY_TIME) { // readyを描画
    		gameState = READY;
    		readyScale -= 0.02f;
    		if (readyScale < 0.0f) {
    			readyScale = 0.0f;
    		}
    		TextureDrawer.drawTexture(gl, readyID, width/2, height/2, width, width, 0.0f, scale+readyScale, scale+readyScale);
    	} else if (rapTime - startTime <= READY_TIME + RAP_TIME) { // 1を描画
    		TextureDrawer.drawTexture(gl, oneID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else if (READY_TIME + RAP_TIME < rapTime - startTime && rapTime - startTime <= READY_TIME + RAP_TIME*2) { // 2を描画
    		TextureDrawer.drawTexture(gl, twoID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else if (READY_TIME + RAP_TIME*2 < rapTime - startTime && rapTime - startTime <= READY_TIME + RAP_TIME*3) { // startを描画
    		TextureDrawer.drawTexture(gl, startID, width/2, height/2, width, width, 0.0f, scale, scale);
    	} else {
    		gameState = PLAYING;
    		RabbitDrawer.cycleTime(gl);
    		RabbitDrawer.countSuccess(gl);
    		RabbitDrawer.drawScoreborad(gl);
        	RabbitDrawer.judgmentTiming(gl, soundPool, rabbitBase1, rabbitBase2);
        	 // うさぎを描画
    		RabbitDrawer.rabbitDraw(gl, rabbitBase1);
    		RabbitDrawer.rabbitDraw(gl, rabbitBase2);
    	}
    }

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

    // サーフェイスが生成される際、または再生成される際に呼び出される
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
    	shuID = TextureLoader.loadTexture(gl, this, R.drawable.shu);

    	// 開始時間を取得
    	startTime = System.currentTimeMillis();
    }
}