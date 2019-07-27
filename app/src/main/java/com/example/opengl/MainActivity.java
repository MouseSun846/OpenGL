package com.example.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.opengl.AirHockey1.AirHockey1;
import com.example.opengl.AirHockey2.AirHockey2;
import com.example.opengl.AirHockey3.AirHockey3;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        //检查系统是否 支持OpenGL2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 =
                (configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                 &&(Build.FINGERPRINT.startsWith("generic")
                  || Build.FINGERPRINT.startsWith("unknown")
                  || Build.MODEL.contains("google_sdk")
                  || Build.MODEL.contains("Emulator")
                  || Build.MODEL.contains("Android SDK built for x86")));
        //配置渲染表面
        if (supportsEs2){
            //配置Surface视图
            glSurfaceView.setEGLContextClientVersion(2);
//            glSurfaceView.setEGLConfigChooser(8,8,8,8,16,1);
//            glSurfaceView.setRenderer(new FirstOpenGLProjectRender());
            //实现render接口方法，渲染类
            glSurfaceView.setRenderer(new AirHockey3(this));
            rendererSet = true;
            Toast.makeText(MainActivity.this,"支持OpenGLES2.0",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,"不支持OpenGLES2.0",Toast.LENGTH_LONG).show();
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet){
            glSurfaceView.onResume();
        }
    }
}
