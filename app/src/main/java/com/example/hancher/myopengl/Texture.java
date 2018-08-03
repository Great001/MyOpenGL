package com.example.hancher.myopengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by liaohaicong on 2018/8/3.
 * 图片纹理
 */

public class Texture {
    public int mTextureId;
    public int mTextureWidth;
    public int mTextureHeight;

    private Texture(int textureId,int width,int height){
        this.mTextureId = textureId;
        this.mTextureWidth = width;
        this.mTextureHeight = height;
    }

    public static Texture loadTexture(Context context,int resId){
        Bitmap bitmap = createBitmap(context,resId);

        int[] textureHandlers = new int[1];
        GLES20.glGenTextures(1,textureHandlers,0);
        if(textureHandlers[0] == 0){
            return null;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureHandlers[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        int realWidth = nextPowerOfTwo(bitmap.getWidth());
        int realHeight = nextPowerOfTwo(bitmap.getHeight());

        Bitmap bitmapToLoad = bitmap;
        if(realWidth != bitmap.getWidth() || realHeight != bitmap.getHeight()){
            bitmapToLoad = Bitmap.createBitmap(realWidth,realHeight,Bitmap.Config.ARGB_8888);
            new Canvas(bitmapToLoad).drawBitmap(bitmap,0,0,null);
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapToLoad,0);

        return new Texture(textureHandlers[0],realWidth,realHeight);
    }

    public static Bitmap createBitmap(Context context,int resId){
        return BitmapFactory.decodeResource(context.getResources(),resId);
    }

    /**
     * 获取下个2次幂：如1→2;5→8;16→16
     *
     * @param n 原本的值
     */
    public static int nextPowerOfTwo(int n) {
        n--;
        int shift = 1;
        while (((n + 1) & n) != 0) {
            n |= n >> shift;
            shift <<= 1;
        }
        return n + 1;
    }

}
