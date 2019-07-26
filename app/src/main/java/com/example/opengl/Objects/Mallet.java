package com.example.opengl.Objects;

import com.example.opengl.data.Constants;
import com.example.opengl.data.VertexArray;
import com.example.opengl.programs.ColorShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;

//木槌
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPNENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+COLOR_COMPNENT_COUNT)* Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA ={
            //X Y R G B
            0f,-0.4f,0f,0f,1f,
            0f,0.4f,1f,0f,0f
    };
    private final VertexArray vertexArray;
    public Mallet(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,colorProgram.getColorAttributeLocation(),
                COLOR_COMPNENT_COUNT,STRIDE);
    }
    public void draw(){
        glDrawArrays(GL_POINTS,0,2);
    }

}
