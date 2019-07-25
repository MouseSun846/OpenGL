package com.example.opengl.AirHockey1;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.opengl.AirHockey1.util.LoggerConfig;
import com.example.opengl.AirHockey1.util.ShapeHelper;
import com.example.opengl.AirHockey1.util.TextResourceReader;
import com.example.opengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class AirHockey1 implements GLSurfaceView.Renderer {
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private int uColorLocation;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    //用来在本地内存中存储数据
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    public AirHockey1(Context context){
        this.context = context;
        //两个三角形
        float[] tableVerticesWithTriangles = {
          //triangle1
          -0.5f,-0.5f,
          0.5f,0.5f,
          -0.5f,0.5f,
          //triangle2
          -0.5f,-0.5f,
          0.5f,-0.5f,
          0.5f,0.5f,
          //line1
          -0.5f,0f,
          0.5f,0f,
          //Mallets (木槌)
          0f,-0.25f,
          0f,0.25f
        };
        //分配本地内存，使用相同的排序，在转换成FloatBuffer实例
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        //读取着色器代码
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);
        //在Renderer类中编译着色器
        int vertecShader = ShapeHelper.compileVertexShader(vertexShaderSource);
        int framentShader = ShapeHelper.compileFragmentShader(fragmentShaderSource);
        //链接储存程序ID
        program = ShapeHelper.linkProgram(vertecShader,framentShader);
        if (LoggerConfig.ON){
            ShapeHelper.validateProgram(program);
        }
        //告诉OpenGL在绘制任何东西到屏幕上的时候要使用这里定义的程序
        glUseProgram(program);
        //获取一个uniform位置
        uColorLocation = glGetUniformLocation(program,U_COLOR);
        //获取属性的位置
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        //关联属性到顶点数据的数组
        //从数据开头处读取
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,0,vertexData);
        //使能顶点数组
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //更新着色器中u_Color的值
        glUniform4f(uColorLocation,1.0f,1.0f,0.0f,1.0f);
        //绘制，含有6个顶点
        glDrawArrays(GL_TRIANGLES,0,6);

        //绘制分割线
        glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        //从第6个顶点后的第一个顶点开始读入；两个顶点
        glDrawArrays(GL_LINES,6,2);

        //把木槌绘制为点--蓝色
        glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
        glDrawArrays(GL_POINTS,8,1);
        //把木槌绘制为点--红色
        glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        glDrawArrays(GL_POINTS,9,1);

    }
}
