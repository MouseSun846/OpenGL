package com.example.opengl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexArray {
    private final FloatBuffer floatBuffer ;
    public VertexArray(float[] vertexData){
        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder())
        .asFloatBuffer().put(vertexData);
    }
    public void setVertexAttribPointer(int dataOffset,int attributeLocation,int commentCount,int stride){
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation,commentCount,GL_FLOAT,false,stride,floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }
}
