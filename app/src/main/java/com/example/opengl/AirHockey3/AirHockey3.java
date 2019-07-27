package com.example.opengl.AirHockey3;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.opengl.Objects.Mallet_New;
import com.example.opengl.Objects.Puck;
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
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

public class AirHockey3 implements GLSurfaceView.Renderer {
    private final Context context;
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private Table table;
    private Mallet_New mallet;
    private Puck puck;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgrm;
    private int texture;

    public AirHockey3(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        table = new Table();
//        mallet = new Mallet();
        mallet = new Mallet_New(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgrm = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        //创建投影矩阵
        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);
        //创建一个特殊类型的视图矩阵
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
//        //把模型矩阵设置为单位矩阵
//        setIdentityM(modelMatrix,0);
//        //在沿Z轴负方向平移2个单位
//        translateM(modelMatrix,0,0f,0f,-2f);
//        //加入旋转矩阵,X Y Z转动60度
//        rotateM(modelMatrix,0,-60f,1f,0f,0f);
//        final float[] temp = new float[16];
//        //将投影矩阵与模型矩阵相乘
//        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
//        //拷贝其结果
//        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //将投影矩阵和视图矩阵缓存到viewProjectionMatrix
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //画桌子
        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, texture);
        table.bindData(textureShaderProgram);
        table.draw();
        //画木槌
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorShaderProgrm.useProgram();
        colorShaderProgrm.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorShaderProgrm);
        mallet.draw();
        positionObjectInScene(0f,mallet.height/2f,0.4f);
        colorShaderProgrm.setUniforms(modelViewProjectionMatrix,0f,0f,1f);
        mallet.draw();

        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorShaderProgrm.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorShaderProgrm);
        puck.draw();
    }

    //绘制物体前更新模型矩阵
    private void positionTableInScene(){
        setIdentityM(modelMatrix,0);
        rotateM(modelMatrix,0,-90f,1f,0f,0f);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    private void positionObjectInScene(float x,float y,float z){
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,x,y,z);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);

    }

}
