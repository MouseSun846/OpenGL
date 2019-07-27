package com.example.opengl.Objects;

import com.example.opengl.AirHockey1.util.Geometry;
import com.example.opengl.data.VertexArray;
import com.example.opengl.programs.ColorShaderProgram;

import java.util.List;

public class Puck {
    private static final int POSITION_COUNT = 3;
    public final float radius,height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand>  drawList;
    public Puck(float radius,float height,int numPointsAroundPuck){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(new Geometry.Cylinder(
                new Geometry.Point(0f,0f,0f),radius,height),numPointsAroundPuck);
        this.radius = radius;
        this.height = height;
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawlist;
    }
    public void bindData(ColorShaderProgram colorShaderProgram){
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COUNT,0);
    }
    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand:
             drawList) {
            drawCommand.draw();
        }
    }
}
