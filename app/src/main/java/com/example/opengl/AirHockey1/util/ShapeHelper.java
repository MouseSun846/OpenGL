package com.example.opengl.AirHockey1.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShapeHelper {
    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }
    public static int  compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }
    private static int compileShader(int type, String shaderCode) {
        //创建新的着色器对象
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0){
            if (LoggerConfig.ON){
                Log.w("mouse","Could not create new  shader.");
            }
            return 0;
        }
        //将着色器代码上传到着色器对象
        glShaderSource(shaderObjectId,shaderCode);
        //编译着色器
        glCompileShader(shaderObjectId);
        //获取编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        //取出着色器信息日志
        if (LoggerConfig.ON){
            Log.v("mouse","Results of compiling source: "+ "\n" +shaderCode + "\n:"
            + glGetShaderInfoLog(shaderObjectId));
        }
        //验证编译状态并返回着色器对象ID
        if (compileStatus[0] == 0){
            glDeleteShader(shaderObjectId);
            if(LoggerConfig.ON){
                Log.w("mouse","Compilation of shader failed.");
            }
            return 0;
        }
        //返回新的着色器对象ID
        return  shaderObjectId;

    }
    public static int linkProgram(int vertexShaderID,int fragmentShaderID){
        final int programObjectiID = glCreateProgram();
        if (programObjectiID == 0){
            if (LoggerConfig.ON){
                Log.w("mouse","Could not create new program");
            }
            return 0;
        }
        //把顶点着色器和片段着色器附加到程序对象上
        glAttachShader(programObjectiID,vertexShaderID);
        glAttachShader(programObjectiID,fragmentShaderID);
        //链接程序
        glLinkProgram(programObjectiID);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectiID,GL_LINK_STATUS,linkStatus,0);
        if (LoggerConfig.ON){
            Log.v("mosue","Results of linking program:\n" + glGetProgramInfoLog(programObjectiID));
        }
        //验证连接状态并返回程序对象ID
        if (linkStatus[0] == 0){
            glDeleteProgram(programObjectiID);
            if (LoggerConfig.ON){
                Log.w("mouse","Linking of program failed.");
            }
            return 0;
        }
        return programObjectiID;

    }
    //验证OpenGL程序对象
    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
        Log.v("mouse","Results of validate program: "+validateStatus[0] + "\nlog:" + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
    public static int buildProgram(String vertexShaderSource,String fragmentShaderSource){
        int program;
        //编译
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        //连接
        program = linkProgram(vertexShader,fragmentShader);
        if (LoggerConfig.ON){
            validateProgram(program);
        }
        return program;
    }

}
