package com.rabbitwhiskers;

import javax.microedition.khronos.opengles.GL10;

import android.media.SoundPool;

public class RabbitDrawer extends RabbitWhiskersGame {
	// 判定
	private static final int OK = 1;
	private static final int NG = 0;

	private static int cnt;
	private static int longPos;
	private static int[] whiskersID = {0, 0, 0, 0};
	private static final float NUM_H = 120f;
	private static final float NUM_W = 60f;
	private static long startTime = 0;
	private static long touchTime = 0;
	private static long moveTime = 0;
	private static long rapTime = 0;
	private static int judge1 = NG;
	private static int judge2 = NG;
	private static int judgeflg = 0;

    static void rabbitDraw(GL10 gl, RabbitBase rabbitBase) {
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
    		rabbitBase.movePosX -= 30;
    		if (rabbitBase.startPosX - rabbitBase.movePosX >= width) {
    			rabbitBase.movePosX = rabbitBase.startPosX - width;
    			rabbitBase.startPosX = Math.abs(rabbitBase.movePosX);
    			rabbitBase.movePosX = rabbitBase.startPosX;
    			rabbitBase.nextFlg = 0;
    			rabbitBase.rabbitBaseID = nomalRabbitID;
    		}
    	}
    }

    static void countSuccess(GL10 gl)
    {
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*7/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, cnt/1000);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*5/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, (cnt%1000)/100);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W*3/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, (cnt%100)/10);
    	TextureDrawer.drawTexture(gl, numberID, width-(NUM_W/4+25)*scale, height-(NUM_H/2+50)*scale, (int)NUM_W/2, (int)NUM_H/2, 0.0f, scale, scale, cnt%10);
    }

    static void drawScoreborad(GL10 gl)
    {
    	// スコアボートを描画
    	TextureDrawer.drawTexture(gl, boardID, width/2, height-(NUM_H+30)*scale, width, width, 0.0f, 1.0f, 1.0f);
    }
    static void cycleTime(GL10 gl)
    {
    	if (startTime == 0) {
    		startTime = System.currentTimeMillis();
    		whiskersSetting();
    	} else {
    		rapTime = System.currentTimeMillis();
    	}
    	if (RAP_TIME < rapTime - startTime) {
    		TextureDrawer.drawTexture(gl, numberID, NUM_W/2*scale, height-(NUM_H/2+30)*scale, (int)NUM_W, (int)NUM_H, 0.0f, scale, scale, 1);
    		if (RAP_TIME*2 < rapTime - startTime) {
    			TextureDrawer.drawTexture(gl, numberID, NUM_W*3/2*scale, height-(NUM_H/2+30)*scale, (int)NUM_W, (int)NUM_H, 0.0f, scale, scale, 2);
    			if (RAP_TIME*3 < rapTime - startTime) {
        			TextureDrawer.drawTexture(gl, shuID, NUM_W*3*scale, height-(NUM_H/2+30)*scale, (int)NUM_W*2, (int)NUM_H, 0.0f, scale, scale);
    			}
    		}
    	}
    	if (RAP_TIME*4 < rapTime - startTime) {
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
    }

    static void whiskersSetting()
    {
    	for (int i =0; i < 4; i++) {
    		whiskersID[i] = shortWhiskersID;
    	}
    	longPos = (int)(Math.random()*10.0d)%4;
    	whiskersID[longPos] = longWhiskersID;
    }

    static void setTouchTime()
    {
    	touchTime = System.currentTimeMillis();
    }

    static void setMoveTime()
    {
    	moveTime = System.currentTimeMillis();
    }

    static void judgmentTiming(GL10 gl, SoundPool soundPool, RabbitBase rabbitBase1, RabbitBase rabbitBase2)
    {
    	// タッチした瞬間のタイミングを判定
    	if (RAP_TIME*3-200 < touchTime - startTime && touchTime - startTime < RAP_TIME*3+200) {
    		switch (longPos) {
    		case 0: // 左上
    			if (x_left-40*scale < moveX2 && moveX2 < x_left+80*scale && y_top-30*scale < moveY2 && moveY2 < y_top+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 1: // 右上
    			if (x_right-80*scale < moveX2 && moveX2 < x_right+40*scale && y_top-30*scale < moveY2 && moveY2 < y_top+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 2: // 左下
    			if (x_left-40*scale < moveX2 && moveX2 < x_left+80*scale && y_under-30*scale < moveY2 && moveY2 < y_under+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		case 3: // 右上
    			if (x_right-80*scale < moveX2 && moveX2 < x_right+40*scale && y_under-30*scale < moveY2 && moveY2 < y_under+30*scale) {
    				judge1 = OK;
    			}
    			break;
    		}
    	}
    	// ひげを抜いた時のタイミングの判定
    	if (RAP_TIME*3-200 < moveTime - startTime && moveTime - startTime < RAP_TIME*3+400) {
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

    	if (RAP_TIME*3+400 < rapTime - startTime) {
        	// 判定によって、笑顔か不満顔かを描画
    		if(judge1 == OK && judge2 == OK) {
    			// 笑顔
        		TextureDrawer.drawTexture(gl, numberID, 32, 64, 128, 128, 0.0f, 1.0f, 1.0f, 1);
        	} else {
        		// 不満顔
        		TextureDrawer.drawTexture(gl, numberID, 32, 64, 128, 128, 0.0f, 1.0f, 1.0f, 2);
        	}

        	if(judgeflg == 0) {
    			if (judge1 == OK && judge2 == OK) {
        			cnt++;
        			// 画面に表示されているうさぎだけ笑顔にする。
        			if (rabbitBase1.startPosX == 0) {
            			rabbitBase1.rabbitBaseID = okRabbitID;
        			}
        			if (rabbitBase2.startPosX == 0) {
            			rabbitBase2.rabbitBaseID = okRabbitID;
        			}
    				soundPool.play(senyaa, (float)volume, (float)volume, 0, 0, 1.0f);
    			} else {
        			// 画面に表示されているうさぎだけうーんにする。
        			if (rabbitBase1.startPosX == 0) {
            			rabbitBase1.rabbitBaseID = ngRabbitID;
        			}
        			if (rabbitBase2.startPosX == 0) {
            			rabbitBase2.rabbitBaseID = ngRabbitID;
        			}
    				soundPool.play(seita, (float)volume, (float)volume, 0, 0, 1.0f);
    			}
    			judgeflg = 1;
    		}
    	}

    	if (RAP_TIME*3+500 < rapTime - startTime) {
    		rabbitBase1.nextFlg = 1;
    		rabbitBase2.nextFlg = 1;
    	}
    }
}

