package com.example.hancher.myopengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liaohaicong on 2018/8/3.
 * OpenGl渲染器
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    public static final String TAG = "OpenGL";

    public static final int COMPONENT_COUNT_PER_FLOAT = 4;
    public static final int VERTEX_POSITION_COMPONENT_COUNT = 2;  //X,Y两个分量
    public static final int TEXTURE_POSITION_COMPONENT_COUNT = 2; //S,T分量
    public static final int STRIDE = (VERTEX_POSITION_COMPONENT_COUNT + TEXTURE_POSITION_COMPONENT_COUNT) * COMPONENT_COUNT_PER_FLOAT;

    /**
     * 着色器程序
     */
    private Shader mShader;
    /**
     * 纹理贴图
     */
    private Texture mTexture;
    /**
     * 顶点坐标引用（句柄）
     */
    private int mVertexPosLocation;
    /**
     * \纹理坐标引用（句柄）
     */
    private int mTexturePosLocation;
    /**
     * 纹理数据引用（句柄）
     */
    private int mTexUnitLocation;
    /**
     * 顶点数据（使用FloatBuffer作为CPU和GPU传递的载体）
     * 包含两个属性：顶点坐标和纹理坐标
     */
    private FloatBuffer mVertexBuffer;

    private Context mContext;

    private static final float[] VERTEX_DATA = new float[]{
            //x,y,s,t(纹理坐标t要反方向)
            -0.5f, -0.5f, 0.2f, 0.6f,
            0.5f, 0.5f, 0.6f, 0.2f,
            -0.5f, 0.5f, 0.2f, 0.2f,
            -0.5f, -0.5f, 0.2f, 0.6f,
            0.5f, -0.5f, 0.6f, 0.6f,
            0.5f, 0.5f, 0.6f, 0.2f
    };


    public MyRenderer(Context context) {
        mContext = context;
        mShader = new Shader();
        //此时还不能任何OpenGL的操作，需要在Renderer的onSurfaceCreated回调之后，绑定到EGLContext才能调用GLES20;
    }

    private void initVertexBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * COMPONENT_COUNT_PER_FLOAT).
                order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(VERTEX_DATA);
        mVertexBuffer.position(0);  //必须调用，否则回调IndexOutOfBoundsException
    }

    private void initLocation() {
        mVertexPosLocation = GLES20.glGetAttribLocation(mShader.mProgramId, Shader.A_POSITION);
        mTexturePosLocation = GLES20.glGetAttribLocation(mShader.mProgramId, Shader.A_TEXPOSITION);
        mTexUnitLocation = GLES20.glGetUniformLocation(mShader.mProgramId, Shader.U_TEXTURE);
    }


    private void startDraw() {
        //激活纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);  //激活纹理单元0
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.mTextureId);  //绑定纹理
        GLES20.glUniform1i(mTexUnitLocation, 0);  //传递纹理数据到纹理单元0

        //传递顶点数据
        GLES20.glEnableVertexAttribArray(mVertexPosLocation);
        GLES20.glEnableVertexAttribArray(mTexturePosLocation);
        //顶点数据传递到GPU中引用指向的变量
        GLES20.glVertexAttribPointer(mVertexPosLocation, VERTEX_POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mVertexBuffer);
        mVertexBuffer.position(VERTEX_POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(mTexturePosLocation, TEXTURE_POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mVertexBuffer);

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);  //第二个参数 偏移量  第三个参数：顶点个数

        //结束绘制
        GLES20.glDisableVertexAttribArray(mVertexPosLocation);
        GLES20.glDisableVertexAttribArray(mTexturePosLocation);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);  //解除纹理绑定
        GLES20.glFlush();
        GLES20.glFinish();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");
        //指定清屏颜色RGBA
        GLES20.glClearColor(1, 1, 1, 1);
        //激活着色器程序
        mShader.initShaderProgram();
        //获取着色器程序中的变量引用
        initLocation();
        //初始化顶点数据
        initVertexBuffer();
        //加载纹理贴图
        mTexture = Texture.loadTexture(mContext, R.drawable.test);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame");
        //刷新屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        startDraw();
    }
}
