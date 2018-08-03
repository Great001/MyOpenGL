package com.example.hancher.myopengl;

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

    private int mPositionLocation;
    private int mColorLocation;

    private FloatBuffer mVertexBuffer;

    private static final float[] VERTEX_DATA = new float[]{0f, 0f};

    public MyRenderer() {
        mShader = new MyShader();
    }

    private void initVertexBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(VERTEX_DATA);
        mVertexBuffer.position(0);
    }

    public void drawPoint() {
        GLES20.glEnableVertexAttribArray(mPositionLocation);
        GLES20.glVertexAttribPointer(mPositionLocation, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glUniform4f(mColorLocation, 1f, 0f, 0f, 1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        GLES20.glFlush();
        GLES20.glFinish();
    }

    private void initLocation() {
        mPositionLocation = GLES20.glGetAttribLocation(mShader.mProgramId, MyShader.A_POSITION);
        mColorLocation = GLES20.glGetUniformLocation(mShader.mProgramId, MyShader.U_COLOR);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG,"onSurfaceCreated");
        GLES20.glClearColor(1, 1, 1, 1);
        mShader.initShaderProgram();
        GLES20.glUseProgram(mShader.mProgramId);
        initLocation();
        initVertexBuffer();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG,"onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG,"onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawPoint();
    }
}
