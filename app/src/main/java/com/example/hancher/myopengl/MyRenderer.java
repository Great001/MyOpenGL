package com.example.hancher.myopengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by liaohaicong on 2018/8/3.
 * OpenGl渲染器
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    public static final String TAG = "OpenGL";

    private MyShader mShader;
    private Texture mTexture;

    private int mPositionLocation;
    private int mTexPositionLocation;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexPosBuffer;

    private Context mContext;

    private static final float[] VERTEX_DATA = new float[]{
            -1f, -0.5f,
            1f,-0.5f,
            0f,0.5f
    };

    private static final float[] TEX_DATA = new float[]{
            0.2f, 0.2f,
            1f,0.2f,
            1f,1f
    };

    public MyRenderer(Context context) {
        mShader = new MyShader();
        mContext = context;
    }

    private void initVertexBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(VERTEX_DATA);
        mVertexBuffer.position(0);

        ByteBuffer byteBufferTex = ByteBuffer.allocateDirect(6 * 4);
        byteBufferTex.order(ByteOrder.nativeOrder());
        mTexPosBuffer = byteBufferTex.asFloatBuffer();
        mTexPosBuffer.put(TEX_DATA);
        mTexPosBuffer.position(0);
    }

    public void drawPoint() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.mTextureId);
        GLES20.glUniform1i(mTexPositionLocation, 0);


        GLES20.glEnableVertexAttribArray(mPositionLocation);
        GLES20.glEnableVertexAttribArray(mTexPositionLocation);
        GLES20.glVertexAttribPointer(mPositionLocation, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glVertexAttribPointer(mTexPositionLocation, 2, GLES20.GL_FLOAT, false, 0, mTexPosBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(mPositionLocation);
        GLES20.glDisableVertexAttribArray(mTexPositionLocation);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glFlush();
        GLES20.glFinish();
    }

    private void initLocation() {
        mPositionLocation = GLES20.glGetAttribLocation(mShader.mProgramId, MyShader.A_POSITION);
        mTexPositionLocation = GLES20.glGetAttribLocation(mShader.mProgramId, MyShader.A_TEXPOSITION);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");
        GLES20.glClearColor(1, 1, 1, 1);
        mShader.initShaderProgram();
        GLES20.glUseProgram(mShader.mProgramId);
        initLocation();
        initVertexBuffer();
        mTexture = Texture.loadTexture(mContext, R.drawable.bucket);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawPoint();
    }
}
