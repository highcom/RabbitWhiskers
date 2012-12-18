package com.rabbitwhiskers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class TextureDrawer {
	/**
	 * 	2Dテクスチャを描画する
	 * @param gl
	 * @param x,y 描画する座標
	 * @param width, height 四角形の幅、高さ
	 * @param angle 回転角度
	 * @param scale_x,scale_y 拡大率
	 */
	static void drawTexture(GL10 gl, int tex_id, float x, float y, int width, int height, float angle, float scale_x, float scale_y)
	{
		// 固定小数点値で1.0
		int one = 0x10000;

		// 頂点座標
		int vertices[] = {
				-width*one/2, -height*one/2, 0,
				width*one/2, -height*one/2, 0,
				-width*one/2, height*one/2, 0,
				width*one/2, height*one/2, 0,
		};

		// テクスチャ座標配列
		int texCoords[] = {
				0, one, one, one, 0, 0, one, 0,
		};

		// 頂点配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// テクスチャ座標配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// ブレンディングを有効化
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// 2Dテクスチャを有効に
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// テクスチャユニット0番をアクティブに
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		// テクスチャIDに対応するテクスチャをバインド
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id);

		// モデルビュー行列を選択
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// 現在選択されている行列(モデルビュー行列)に、単位行をセット
		gl.glLoadIdentity();

		// 行列スタックに現在の行列をプッシュ
		gl.glPushMatrix();

		// モデルを平行移動する行列を掛け合わせる
		gl.glTranslatef(x, y, 0);
		// モデルをZ軸を中心に回転する行列を掛け合わせる
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		// モデルを拡大縮小する行列を掛け合わせる
		gl.glScalef(scale_x, scale_y, 1.0f);

		// 色をセット
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		// 頂点座標配列をセット
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, makeIntBuffer(vertices));

		// 色情報配列をセット
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, makeIntBuffer(texCoords));

		// セットした配列を元に描画
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// 先ほどプッシュした状態に行列スタックを戻す
		gl.glPopMatrix();

		// 有効にしたものを無効化
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	static void drawTexture(GL10 gl, int tex_id, float x, float y, int width, int height, float angle, float scale_x, float scale_y, int number)
	{
		float tex_h = 0.33f;
		float tex_w = 0.25f;
		float u = (float)(number % 4) * 0.25f;
		float v = (float)(number / 4) * 0.33f;

		// 固定小数点値で1.0
		int one = 0x10000;

		// 頂点座標
		int vertices[] = {
				-width*one/2, -height*one/2, 0,
				width*one/2, -height*one/2, 0,
				-width*one/2, height*one/2, 0,
				width*one/2, height*one/2, 0,
		};

		// テクスチャ座標配列
		float coords[] = {
		        u,				 v + tex_h,
		        u + tex_w, v + tex_h,
		        u,				 v,
		        u + tex_w, v,
		};

		// 頂点配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// テクスチャ座標配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// ブレンディングを有効化
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// 2Dテクスチャを有効に
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// テクスチャユニット0番をアクティブに
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		// テクスチャIDに対応するテクスチャをバインド
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id);

		// モデルビュー行列を選択
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// 現在選択されている行列(モデルビュー行列)に、単位行をセット
		gl.glLoadIdentity();

		// 行列スタックに現在の行列をプッシュ
		gl.glPushMatrix();

		// モデルを平行移動する行列を掛け合わせる
		gl.glTranslatef(x, y, 0);
		// モデルをZ軸を中心に回転する行列を掛け合わせる
		gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
		// モデルを拡大縮小する行列を掛け合わせる
		gl.glScalef(scale_x, scale_y, 1.0f);

		// 色をセット
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		// 頂点座標配列をセット
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, makeIntBuffer(vertices));

		// 色情報配列をセット
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, makeFloatBuffer(coords));

		// セットした配列を元に描画
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// 先ほどプッシュした状態に行列スタックを戻す
		gl.glPopMatrix();

		// 有効にしたものを無効化
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	static void drawTextureRabbit(GL10 gl, int tex_id, float x, float y, int width, int height, float angle, float scale_x, float scale_y)
	{
		float tex_h = 800f/1024f;
		float tex_w = 480f/1024f;

		// 固定小数点値で1.0
		int one = 0x10000;

		// 頂点座標
		int vertices[] = {
				-width*one/2, -height*one/2, 0,
				width*one/2, -height*one/2, 0,
				-width*one/2, height*one/2, 0,
				width*one/2, height*one/2, 0,
		};

		// テクスチャ座標配列
		float coords[] = {
		        0,			tex_h,
		        tex_w,	tex_h,
		        0,			0,
		        tex_w,	0,
		};

		// 頂点配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// テクスチャ座標配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// ブレンディングを有効化
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// 2Dテクスチャを有効に
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// テクスチャユニット0番をアクティブに
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		// テクスチャIDに対応するテクスチャをバインド
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id);

		// モデルビュー行列を選択
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// 現在選択されている行列(モデルビュー行列)に、単位行をセット
		gl.glLoadIdentity();

		// 行列スタックに現在の行列をプッシュ
		gl.glPushMatrix();

		// モデルを平行移動する行列を掛け合わせる
		gl.glTranslatef(x, y, 0);
		// モデルをZ軸を中心に回転する行列を掛け合わせる
		gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
		// モデルを拡大縮小する行列を掛け合わせる
		gl.glScalef(scale_x, scale_y, 1.0f);

		// 色をセット
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		// 頂点座標配列をセット
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, makeIntBuffer(vertices));

		// 色情報配列をセット
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, makeFloatBuffer(coords));

		// セットした配列を元に描画
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// 先ほどプッシュした状態に行列スタックを戻す
		gl.glPopMatrix();

		// 有効にしたものを無効化
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	static void drawTextureWhiskers(GL10 gl, int tex_id, float x, float y, int width, int height, float angle, float scale_x, float scale_y)
	{
		float tex_h = 60f/256f;
		float tex_w = 160f/256f;

		// 固定小数点値で1.0
		int one = 0x10000;

		// 頂点座標
		int vertices[] = {
				-width*one/2, -height*one/2, 0,
				width*one/2, -height*one/2, 0,
				-width*one/2, height*one/2, 0,
				width*one/2, height*one/2, 0,
		};

		// テクスチャ座標配列
		float coords[] = {
		        0,			tex_h,
		        tex_w,	tex_h,
		        0,			0,
		        tex_w,	0,
		};

		// 頂点配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// テクスチャ座標配列を使う事を宣言
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// ブレンディングを無効化
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// 2Dテクスチャを有効に
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// テクスチャユニット0番をアクティブに
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		// テクスチャIDに対応するテクスチャをバインド
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id);

		// モデルビュー行列を選択
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// 現在選択されている行列(モデルビュー行列)に、単位行をセット
		gl.glLoadIdentity();

		// 行列スタックに現在の行列をプッシュ
		gl.glPushMatrix();

		// モデルを平行移動する行列を掛け合わせる
		gl.glTranslatef(x, y, 0);
		// モデルをZ軸を中心に回転する行列を掛け合わせる
		gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
		// モデルを拡大縮小する行列を掛け合わせる
		gl.glScalef(scale_x, scale_y, 1.0f);

		// 色をセット
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		// 頂点座標配列をセット
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, makeIntBuffer(vertices));

		// 色情報配列をセット
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, makeFloatBuffer(coords));

		// セットした配列を元に描画
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// 先ほどプッシュした状態に行列スタックを戻す
		gl.glPopMatrix();

		// 有効にしたものを無効化
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	private static FloatBuffer makeFloatBuffer(float[] coords) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer fb = byteBuffer.asFloatBuffer();
		fb.put(coords);
		fb.position(0);
		return fb;
	}

	private static IntBuffer makeIntBuffer(int vertices[]) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length*4);
		byteBuffer.order(ByteOrder.nativeOrder());
		IntBuffer ib = byteBuffer.asIntBuffer();
		ib.put(vertices);
		ib.position(0);
		return ib;
	}
}
