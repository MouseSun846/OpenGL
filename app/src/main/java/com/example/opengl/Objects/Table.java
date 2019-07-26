package com.example.opengl.Objects;

import com.example.opengl.data.Constants;
import com.example.opengl.data.VertexArray;
import com.example.opengl.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

public class Table {
    //位置分量
    private static final int  POSITION_COMPONENT_COUNT = 2;
    //纹理分量
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    //跨距
    private static final int STRIDE = (POSITION_COMPONENT_COUNT +TEXTURE_COORDINATES_COMPONENT_COUNT)* Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //X Y S T
        0f,0f,0.5f,0.5f,
        -0.5f,-0.8f,0f,0.9f,
        0.5f,-0.8f,1f,0.9f,
        0.5f,0.8f,1f,0.1f,
        -0.5f,0.8f,0f,0.1f,
        -0.5f,-0.8f,0f,0.9f

    };
    private final VertexArray vertexArray;
    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    //把顶点数组绑定到着色器程序上
    public void bindData(TextureShaderProgram textureProgram){
        vertexArray.setVertexAttribPointer(0,textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,STRIDE);
    }
    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
    }

}
