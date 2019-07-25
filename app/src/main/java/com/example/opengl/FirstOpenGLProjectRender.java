package com.example.opengl;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glViewport;

public class FirstOpenGLProjectRender implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("mouse","onSurfaceCreated");
        //填充颜色
        glClearColor(1.0f,1.0f,0.0f,1.0f);
    }

    //横竖屏切换时候
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //视图尺寸
        glViewport(0,0,width,height);
        Log.i("mouse","onSurfaceChanged");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除屏幕所有颜色
        glClear(GL_COLOR_BUFFER_BIT);
//        Log.i("mouse","onDrawFrame");

    }
}
