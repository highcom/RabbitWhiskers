package com.rabbitwhiskers;

import javax.microedition.khronos.opengles.GL10;

import android.media.SoundPool;

public class RabbitDrawer extends RabbitWhiskersGame {

	private static final int OK = 1;
	private static final int NG = 0;
	private static final float NUM_H = 120f;
	private static final float NUM_W = 60f;

	private static int longPos;
	private static int[] whiskersID = {0, 0, 0, 0};
	private static int[] speedup_cnt = {10, 30, 50, 100, -1};
	private static long startTime;
	private static long touchTime;
	private static long moveTime;
	private static long rapTime;
	private static int judge1;
	private static int judge2;
	private static int judgeflg;

	/*
	 * うさぎの状態を初期化するメソッド
	 */
	static void initRabbitDrawer(RabbitBase rabbitBase) {
		longPos = 0;
		startTime = 0;
		touchTime = 0;
		moveTime = 0;
		rapTime = 0;
		judge1 = NG;
		judge2 = NG;
		judgeflg = 0;
		rabbitBase.x1 = x_left;
		rabbitBase.y1 = y_top;
		rabbitBase.x2 = x_right;
		rabbitBase.y2 = y_top;
		rabbitBase.x3 = x_left;
		rabbitBase.y3 = y_under;
		rabbitBase.x4 = x_right;
		rabbitBase.y4 = y_under;
		rabbitBase.rabbitBaseID = nomalRabbitID[rabbitBase.rabbitKind];
		tempo0 = 0;
		tempo1 = 0;
		tempo2 = 0;
		tempo3 = 0;
	}
	/*
	 * うさぎを描画するメソッド
	 */
    static void rabbitDraw(GL10 gl, RabbitBase rabbitBase) {
    	int num;
    	// うさぎを描画
    	TextureDrawer.drawTextureRabbit(gl, rabbitBase.rabbitBaseID, width/2+rabbitBase.movePosX, height/2-POSITION*scale, R_B_WIDTH, R_B_HEIGHT, 0.0f, scale, scale);
    	// ひげを描画
    	if (rabbitBase.startPosX == 0) {
    		// 画面に表示されている方だけひげを移動
    		if (x_left-80*scale < touchX && touchX < x_left+80*scale && y_top-30*scale < touchY && touchY < y_top+30*scale) {
    			// 左上のひげ
    			rabbitBase.x1 += (moveX2 - moveX1);
    			rabbitBase.y1 += (moveY2 - moveY1);
    			rabbitBase.x2 = x_right;
    			rabbitBase.y2 = y_top;
    			rabbitBase.x3 = x_left;
    			rabbitBase.y3 = y_under;
    			rabbitBase.x4 = x_right;
    			rabbitBase.y4 = y_under;
    			moveX1 = moveX2;
    			moveY1 = moveY2;
    		} else if (x_right-80*scale < touchX && touchX < x_right+80*scale && y_top-30*scale < touchY && touchY < y_top+30*scale) {
    			// 右上のひげ
    			rabbitBase.x1 = x_left;
    			rabbitBase.y1 = y_top;
    			rabbitBase.x2 += (moveX2 - moveX1);
    			rabbitBase.y2 += (moveY2 - moveY1);
    			rabbitBase.x3 = x_left;
    			rabbitBase.y3 = y_under;
    			rabbitBase.x4 = x_right;
    			rabbitBase.y4 = y_under;
    			moveX1 = moveX2;
    			moveY1 = moveY2;
    		}else if (x_left-80*scale < touchX && touchX < x_left+80*scale && y_under-30*scale < touchY && touchY < y_under+30*scale) {
    			// 左下のひげ
    			rabbitBase.x1 = x_left;
    			rabbitBase.y1 = y_top;
    			rabbitBase.x2 = x_right;
    			rabbitBase.y2 = y_top;
    			rabbitBase.x3 += (moveX2 - moveX1);
    			rabbitBase.y3 += (moveY2 - moveY1);
    			rabbitBase.x4 = x_right;
    			rabbitBase.y4 = y_under;
    			moveX1 = moveX2;
    			moveY1 = moveY2;
    		} else if (x_right-80*scale < touchX && touchX < x_right+80*scale && y_under-30*scale < touchY && touchY < y_under+30*scale) {
    			// 右下のひげ
    			rabbitBase.x1 = x_left;
    			rabbitBase.y1 = y_top;
    			rabbitBase.x2 = x_right;
    			rabbitBase.y2 = y_top;
    			rabbitBase.x3 = x_left;
    			rabbitBase.y3 = y_under;
    			rabbitBase.x4 += (moveX2 - moveX1);
    			rabbitBase.y4 += (moveY2 - moveY1);
    			moveX1 = moveX2;
    			moveY1 = moveY2;
    		}
    	} else {
    		// 画面に表示されていない方は基準位置のまま
    		rabbitBase.x1 = x_left;
    		rabbitBase.y1 = y_top;
    		rabbitBase.x2 = x_right;
    		rabbitBase.y2 = y_top;
    		rabbitBase.x3 = x_left;
    		rabbitBase.y3 = y_under;
    		rabbitBase.x4 = x_right;
    		rabbitBase.y4 = y_under;
    	}
    	// ひげを描画
    	TextureDrawer.drawTextureWhiskers(gl, whiskersID[0], rabbitBase.x1+rabbitBase.movePosX, rabbitBase.y1-POSITION*scale, 160, 60, 0.0f, scale, scale);
    	TextureDrawer.drawTextureWhiskers(gl, whiskersID[1], rabbitBase.x2+rabbitBase.movePosX, rabbitBase.y2-POSITION*scale, 160, 60, 0.0f, scale, scale);
    	TextureDrawer.drawTextureWhiskers(gl, whiskersID[2], rabbitBase.x3+rabbitBase.movePosX, rabbitBase.y3-POSITION*scale, 160, 60, 0.0f, scale, scale);
    	TextureDrawer.drawTextureWhiskers(gl, whiskersID[3], rabbitBase.x4+rabbitBase.movePosX, rabbitBase.y4-POSITION*scale, 160, 60, 0.0f, scale, scale);

    	// スライドする
    	if (rabbitBase.nextFlg == 1) {
    		rabbitBase.movePosX -= MOVE_SPEED;
    		if (rabbitBase.startPosX - rabbitBase.movePosX >= width) {
    			// 画面に表示されてない方は、うさぎの種類を変える
    			if (rabbitBase.movePosX  != 0) {
    				num = (int)(Math.random()*100.0d)%R_KIND;
    				rabbitBase.rabbitKind = num;
    			}
    			rabbitBase.movePosX = rabbitBase.startPosX - width;
    			rabbitBase.startPosX = Math.abs(rabbitBase.movePosX);
    			rabbitBase.movePosX = rabbitBase.startPosX;
    			rabbitBase.nextFlg = 0;
    			rabbitBase.rabbitBaseID = nomalRabbitID[rabbitBase.rabbitKind];
    			tempo1 = 0;
    			tempo2 = 0;
    			tempo3 = 0;
    		}
    	}
    }

    /*
     * スコアを表示するメソッド
     */
    static void drawSuccessFailure(GL10 gl)
    {
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*7/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, okcnt/1000);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*5/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, (okcnt%1000)/100);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*3/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, (okcnt%100)/10);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, okcnt%10);
    	for (int i = 0; i < ngcnt; i++) {
    		TextureDrawer.drawTexture(gl, ngID, width-(NUM_W*(7-i*2)/4+25)*scale, height-(NUM_H/2+100)*scale, (int)NUM_W/2, (int)NUM_W/2, 0.0f, scale, scale);
    	}
    }

    /*
     * スコアボードを描画するメソッド
     */
    static void drawScoreborad(GL10 gl)
    {
    	// スコアボートを描画
    	TextureDrawer.drawTexture(gl, boardID, width/2, height-(NUM_H+30)*scale, width, width, 0.0f, 1.0f, 1.0f);
    }

    /*
     * タイムサイクルを管理するメソッド
     */
    static void cycleTime(GL10 gl)
    {
    	if (startTime == 0) {
    		tempo1 = 0;
    		tempo2 = 0;
    		tempo3 = 0;
    		startTime = System.currentTimeMillis();
    		whiskersSetting();
    	} else {
    		rapTime = System.currentTimeMillis();
    	}
    	if (RAP_TIME < rapTime - startTime) {
    		if (tempo1 == 0) {
    			soundPool.play(setempo, (float)volume, (float)volume, 0, 0, 1.0f);
    			tempo1 = 1;
    		}
    		TextureDrawer.drawTexture(gl, numberID, NUM_W/2*scale, height-(NUM_H/2+30)*scale, (int)NUM_W, (int)NUM_H, 0.0f, scale, scale, 1);
    		if (RAP_TIME*2 < rapTime - startTime) {
        		if (tempo2 == 0) {
        			soundPool.play(setempo, (float)volume, (float)volume, 0, 0, 1.0f);
        			tempo2 = 1;
        		}
    			TextureDrawer.drawTexture(gl, numberID, NUM_W*3/2*scale, height-(NUM_H/2+30)*scale, (int)NUM_W, (int)NUM_H, 0.0f, scale, scale, 2);
    			if (RAP_TIME*3 < rapTime - startTime) {
        			TextureDrawer.drawTexture(gl, shuID, NUM_W*3*scale, height-(NUM_H/2+30)*scale, (int)NUM_W*2, (int)NUM_H, 0.0f, scale, scale);
    			}
    		}
    	}
    	if (RAP_TIME*4 < rapTime - startTime && ngcnt < 3 && okcnt != SPEEDUP_CNT) { // XXX okcntの判定は無理やりなので後で調整
    		// 状態をリセットする
    		touchX = 0;
    		touchY = 0;
    		startTime = 0;
    		touchTime = 0;
    		rapTime = 0;
    		judge1 = NG;
    		judge2 = NG;
    		judgeflg = 0;
    	}
    	// ゲームオーバー
    	if (RAP_TIME*6 < rapTime - startTime && ngcnt >= 3) {
    		gameState = GAMEOVER;
    	}
    }

    /*
     * 長いひげを設定するメソッド
     */
    static void whiskersSetting()
    {
    	for (int i =0; i < 4; i++) {
    		whiskersID[i] = shortWhiskersID;
    	}
    	longPos = (int)(Math.random()*100.0d)%4;
    	whiskersID[longPos] = longWhiskersID;
    }

    /*
     * タッチしたタイミングを設定するメソッド
     */
    static void setTouchTime()
    {
    	touchTime = System.currentTimeMillis();
    }

    /*
     * ひげを動かしたタイミングを設定するメソッド
     */
    static void setMoveTime()
    {
    	moveTime = System.currentTimeMillis();
    }

    /*
     * ひげを抜いたタイミングを判定するメソッド
     */
    static void judgmentTiming(GL10 gl, SoundPool soundPool, RabbitBase rabbitBase1, RabbitBase rabbitBase2)
    {
    	// タッチした瞬間のタイミングを判定
    	if (RAP_TIME*3-MARGIN_TIME < touchTime - startTime && touchTime - startTime < RAP_TIME*3+MARGIN_TIME) {
    		switch (longPos) {
    		case 0: // 左上
    			if (x_left-40*scale < touchX && touchX < x_left+80*scale && y_top-30*scale < touchY && touchY < y_top+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 1: // 右上
    			if (x_right-80*scale < touchX && touchX < x_right+40*scale && y_top-30*scale < touchY && touchY < y_top+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 2: // 左下
    			if (x_left-40*scale < touchX && touchX < x_left+80*scale && y_under-30*scale < touchY && touchY < y_under+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 3: // 右上
    			if (x_right-80*scale < touchX && touchX < x_right+40*scale && y_under-30*scale < touchY && touchY < y_under+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		}
    	}
    	// ひげを抜いた時のタイミングの判定
    	if (RAP_TIME*3-MARGIN_TIME < moveTime - startTime && moveTime - startTime < RAP_TIME*3+MARGIN_TIME*2) {
    		switch (longPos) {
    		case 0: // 左上
    			if (x_left-80*scale < moveX2 && moveX2 < x_left+40*scale && y_top-30*scale < moveY2 && moveY2 < y_top+30*scale) {
    				judge2 = OK;
    			}
    			break;
    		case 1: // 右上
    			if (x_right-40*scale < moveX2 && moveX2 < x_right+80*scale && y_top-30*scale < moveY2 && moveY2 < y_top+30*scale) {
    				judge2 = OK;
    			}
    			break;
    		case 2: // 左下
    			if (x_left-80*scale < moveX2 && moveX2 < x_left+40*scale && y_under-30*scale < moveY2 && moveY2 < y_under+30*scale) {
    				judge2 = OK;
    			}
    			break;
    		case 3: // 右上
    			if (x_right-40*scale < moveX2 && moveX2 < x_right+80*scale && y_under-30*scale < moveY2 && moveY2 < y_under+30*scale) {
    				judge2 = OK;
    			}
    			break;
    		}
    	}

    	if (RAP_TIME*3+MARGIN_TIME*2 < rapTime - startTime) {
        	// 判定によって、笑顔か不満顔かを描画
        	if(judgeflg == 0) {
    			if (judge1 == OK && judge2 == OK) {
        			okcnt++;
        			// 画面に表示されているうさぎだけ笑顔にする。
        			if (rabbitBase1.startPosX == 0) {
            			rabbitBase1.rabbitBaseID = okRabbitID[rabbitBase1.rabbitKind];
        			}
        			if (rabbitBase2.startPosX == 0) {
            			rabbitBase2.rabbitBaseID = okRabbitID[rabbitBase2.rabbitKind];
        			}
    				soundPool.play(senyaa, (float)volume, (float)volume, 0, 0, 1.0f);
    			} else {
    				ngcnt++;
        			// 画面に表示されているうさぎだけうーんにする。
        			if (rabbitBase1.startPosX == 0) {
            			rabbitBase1.rabbitBaseID = ngRabbitID[rabbitBase1.rabbitKind];
        			}
        			if (rabbitBase2.startPosX == 0) {
            			rabbitBase2.rabbitBaseID = ngRabbitID[rabbitBase2.rabbitKind];
        			}
    				soundPool.play(seita, (float)volume, (float)volume, 0, 0, 1.0f);
    			}
    			judgeflg = 1;
    		}
    	}

    	if (RAP_TIME*6 < rapTime - startTime && okcnt == SPEEDUP_CNT) { // XXX okcntの判定は無理やりなので後で調整
        	gameStartTime = System.currentTimeMillis();
        	RabbitDrawer.initRabbitDrawer(rabbitBase1);
        	RabbitDrawer.initRabbitDrawer(rabbitBase2);
        	touchX = 0;
        	touchY = 0;
    		up++;
    		SPEEDUP_CNT = speedup_cnt[up];
    		RAP_TIME -= 100;
    		MARGIN_TIME -= 30;
    		MOVE_SPEED += 10;
    	}

    	if (RAP_TIME*3+MARGIN_TIME*2+100 < rapTime - startTime && ngcnt < 3 && okcnt != SPEEDUP_CNT) { // XXX okcntの判定は無理やりなので後で調整
    		rabbitBase1.nextFlg = 1;
    		rabbitBase2.nextFlg = 1;
    	}
    }
}

