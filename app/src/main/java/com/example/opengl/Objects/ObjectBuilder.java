package com.example.opengl.Objects;

import com.example.opengl.AirHockey1.util.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

//物体构建器
public class ObjectBuilder {
    private static final int FLOAT_PER_VERTEX = 3;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;

    static class GeneratedData{
        final float[]  vertexData;
        final List<DrawCommand> drawlist;
        GeneratedData(float[] vertexData,List<DrawCommand> drawlist){
            this.vertexData = vertexData;
            this.drawlist = drawlist;
        }

    }
    private GeneratedData build(){
        return new GeneratedData(vertexData,drawList);
    }
    private ObjectBuilder(int sizeInVertices){
        vertexData = new float[sizeInVertices * FLOAT_PER_VERTEX];
    }
    //计算圆柱体顶点数量
    public static int sizeOfCircleInVertices(int numPoints){
        //由三角形扇构成 有一个顶点在圆心 围绕圆的第一个顶点需要重合两次才能使圆闭合
        return 1 + (numPoints + 1);
    }
    //计算圆柱体侧面顶点数量
    private static int sizeOfOpenCylinderInVertices(int numPoints){
        //由三角形带构成 有一个顶点在圆心 围着顶部圆的每个点都需要两个顶点，并且前两个顶点需要重复两次才能使闭合
        return (numPoints + 1) * 2;
    }
    //用圆柱体创建冰球--桌子中间扁平的圆柱体
    static GeneratedData createPuck(Geometry.Cylinder puck,int numPoints){
        //所有顶点数目
        int size = sizeOfCircleInVertices(numPoints)+sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height/2f),puck.radius);
        builder.appendCircle(puckTop,numPoints);
        builder.appendOpenCylinder(puck,numPoints);
        return builder.build();
    }
    //构造木槌
    static GeneratedData createMallet(Geometry.Point center,float radius,float height,int numPoints){
        int size = sizeOfCircleInVertices(numPoints) * 2 +sizeOfOpenCylinderInVertices(numPoints) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);
        float baseHeight = height * 0.25f;
        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight),radius);
        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f),radius,baseHeight);
        builder.appendCircle(baseCircle,numPoints);
        builder.appendOpenCylinder(baseCylinder,numPoints);
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;
        Geometry.Circle handleCircle = new Geometry.Circle(center.translateY(height * 0.5f),handleRadius);
        Geometry.Cylinder handleCyinder = new Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2f),handleRadius,handleHeight);
        builder.appendCircle(handleCircle,numPoints);
        builder.appendOpenCylinder(handleCyinder,numPoints);
        return builder.build();
    }
    //用三角形扇构造圆---X--Z平面的圆
    private void appendCircle(Geometry.Circle circle,int numPoints){
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);
        //三角形扇的中心
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;
        for (int i = 0; i < numPoints; i++) {
            float angleInRadius = (((float) i/(float) numPoints)*((float) Math.PI*2f));
            vertexData[offset++] = circle.center.x + circle.radius * (float) Math.cos(angleInRadius);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float) Math.sin(angleInRadius);
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN,startVertex,numVertices);
            }
        });
    }
    //用三角形带构造圆柱体侧面
    private void appendOpenCylinder(Geometry.Cylinder cylinder, final int numPoints){
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);
        for(int i = 0 ; i <= numPoints;i++){
            float angleInRadius = ((float) i/(float) numPoints)*(float)Math.PI * 2f;
            float xPosition = cylinder.center.x + cylinder.radius * (float) Math.cos(angleInRadius);
            float zPosition = cylinder.center.z + cylinder.radius * (float) Math.sin(angleInRadius);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                //绘制三角形带
                glDrawArrays(GL_TRIANGLE_STRIP,startVertex,numVertices);
            }
        });
    }
    //绘画命令接口
    static interface DrawCommand{
        void draw();
    }

}
