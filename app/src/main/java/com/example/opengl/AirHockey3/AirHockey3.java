package com.example.opengl.AirHockey3;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.opengl.Objects.Mallet;
import com.example.opengl.Objects.Table;
import com.example.opengl.R;
import com.example.opengl.programs.ColorShaderProgram;
import com.example.opengl.programs.TextureShaderProgram;
import com.example.opengl.wenli.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class AirHockey3 implements GLSurfaceView.Renderer {
    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgrm;
    private int texture;
    public AirHockey3(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        table = new Table();
        mallet = new Mallet();
        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgrm = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
        perspectiveM(projectionMatrix,0,45,(float)width/(float)height,1f,10f);
        //把模型矩阵设置为单位矩阵
        setIdentityM(modelMatrix,0);
        //在沿Z轴负方向平移2个单位
        translateM(modelMatrix,0,0f,0f,-2f);
        //加入旋转矩阵,X Y Z转动45度
        rotateM(modelMatrix,0,-60f,1f,0f,0f);
        final float[] temp = new float[16];
        //将投影矩阵与模型矩阵相乘
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        //拷贝其结果
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //画桌子
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();
        //画木槌
        colorShaderProgrm.useProgram();
        colorShaderProgrm.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgrm);
        mallet.draw();
    }
}
