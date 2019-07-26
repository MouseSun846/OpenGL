package com.example.opengl.AirHockey2;

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

import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class AirHockey2 implements GLSurfaceView.Renderer {
    private static final String U_MATRIX = "u_Matrix";
    //顶点数组存储矩阵
    private final float[] projectionMatrix = new  float[16];

    //模型矩阵
    private final float[] modelMatrix = new float[16];
    private int uMatrixLocation;
    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int BYTES_PER_FLOAT = 4;
    //用来在本地内存中存储数据
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    //跨距
    private static final int STRIDE = (POSITION_COMPONENT_COUNT +COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;
    public AirHockey2(Context context){
        this.context = context;
        //两个三角形
        float[] tableVerticesWithTriangles = {
                // Order of coordinates: X, Y, R, G, B

//                // Triangle Fan
//                0f,    0f,   1f,   1f,   1f,
//                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//                0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
//                -0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
//                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//
//                // Line 1
//                -0.5f, 0f, 1f, 0f, 0f,
//                0.5f, 0f, 1f, 0f, 0f,
//
//                // Mallets
//                0f, -0.4f, 0f, 0f, 1f,
//                0f,  0.4f, 1f, 0f, 0f
                // Order of coordinates: X, Y, Z, W, R, G, B

                // Triangle Fan
                0f,    0f, 0f, 1.5f,   1f,   1f,   1f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
                0f,  0.4f, 0f, 1.75f, 1f, 0f, 0f
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
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        //获取属性的位置
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
        //设置视图窗口
//        glViewport(100,100,1000,1000);
        //关联属性到顶点数据的数组
        //从数据开头处读取
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        //使能顶点数组
        glEnableVertexAttribArray(aPositionLocation);
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
//        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
//        //给正交投影矩阵赋值
//        if (width >  height){
//            orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
//        }else {
//            orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
//        }
        perspectiveM(projectionMatrix,0,45,(float)width/(float)height,1f,10f);
        //把模型矩阵设置为单位矩阵
        setIdentityM(modelMatrix,0);
        //在沿Z轴负方向平移2个单位
        translateM(modelMatrix,0,0f,0f,-3f);
        //加入旋转矩阵,X Y Z转动45度
        rotateM(modelMatrix,0,180f,1f,0f,0f);
        final float[] temp = new float[16];
        //将投影矩阵与模型矩阵相乘
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        //拷贝其结果
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //设置unifrom值
        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);
        //绘制，含有6个顶点
        glDrawArrays(GL_TRIANGLE_FAN,0,6);

        // Draw the center dividing line.
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet.
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet.
        glDrawArrays(GL_POINTS, 9, 1);


//        //绘制分割线
//        glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
//        //从第6个顶点后的第一个顶点开始读入；两个顶点
//        glDrawArrays(GL_LINES,6,2);
//
//        //把木槌绘制为点--蓝色
//        glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
//        glDrawArrays(GL_POINTS,8,1);
//        //把木槌绘制为点--红色
//        glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
//        glDrawArrays(GL_POINTS,9,1);

    }
}
