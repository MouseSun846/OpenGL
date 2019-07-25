//顶点着色器
//精度默认为 highp
//attribute 关键词 attribute变量是全局且只读的,它只能在vertex shader中使用,
//只能与浮点数,向量或矩阵变量组合, 一般attribute变量用来放置程序传递来的模型顶点,
//法线,颜色,纹理等数据它可以访问数据缓冲区
//vec4 包含4个分量的向量
attribute vec4 a_Position;
void main(){
    //赋值给指定输出变量
    gl_Position = a_Position;
    gl_PointSize = 10.0;
}