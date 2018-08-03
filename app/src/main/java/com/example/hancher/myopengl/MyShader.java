package com.example.hancher.myopengl;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by liaohaicong on 2018/8/3.
 * 着色器程序
 */

public class MyShader {

    public static final String TAG = "OpenGL";

    private static final String VERTEX_SHADER = "attribute vec4 a_position;\n" +
            "uniform vec2 a_texPosition;\n" +
            "varying vec2 v_texPosition;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = a_position;\n" +
            "    gl_PointSize = 1000.0;\n" +
            "    v_texPosition = a_texPosition;\n" +
            "}";

    private static final String FRAGMENT_SHADER = "precision mediump float;\n" +
            "uniform sampler2D u_texture;\n" +
            "varying vec2 v_texPosition;\n" +
            "void main(){\n" +
            "    gl_FragColor = texture2D(u_texture,v_texPosition);\n" +
            "}";

    public static final String A_POSITION = "a_position";
    public static final String A_TEXPOSITION = "a_texPosition";
    public static final String U_COLOR = "u_color";

    public int mProgramId;


    public void initShaderProgram() {
        int vertexShader = loadVertexShader();
        int fragmentShader = loadFragmentShader();

        mProgramId = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramId, vertexShader);
        GLES20.glAttachShader(mProgramId, fragmentShader);
        GLES20.glLinkProgram(mProgramId);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgramId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.d(TAG, "link program error");
        }
    }

    public int loadVertexShader() {
        return loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
    }

    public int loadFragmentShader() {
        return loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
    }

    public int loadVertexShader(String shaderStr) {
        return loadShader(GLES20.GL_SHADER_TYPE, shaderStr);
    }

    public int loadFragmentShader(String shaderStr) {
        return loadShader(GLES20.GL_FRAGMENT_SHADER, shaderStr);
    }


    private int loadShader(int shaderType, String shaderSource) {
        int shaderObjectId = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shaderObjectId, shaderSource);
        GLES20.glCompileShader(shaderObjectId);
        int[] shaderStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, shaderStatus, 0);
        if (shaderStatus[0] == 0) {
            Log.d(TAG, GLES20.glGetShaderInfoLog(shaderObjectId));
            return 0;
        }
        return shaderObjectId;
    }


}
