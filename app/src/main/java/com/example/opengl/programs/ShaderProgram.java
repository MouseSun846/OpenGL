package com.example.opengl.programs;

import android.content.Context;

import com.example.opengl.AirHockey1.util.ShapeHelper;
import com.example.opengl.AirHockey1.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    //Unfirom constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    //shader program
    protected final int program;
    protected ShaderProgram(Context context,int vertexShaderResourceId,int fragmentShaderResourceId){
        //编译 连接程序
        program = ShapeHelper.buildProgram(TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }
    public void useProgram(){
        glUseProgram(program);
    }


}
