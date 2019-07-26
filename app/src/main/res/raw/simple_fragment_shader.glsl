//uniform变量是全局且只读的,在整个shader执行完毕前其值不会改变,他可以和任意基本类型变量组合,
//一般我们使用uniform变量来放置外部程序传递来的环境数据(如点光源位置,模型的变换矩阵等等)
//这些数据在运行中显然是不需要被改变的.
//定义默认精度 lowp mediump highp
precision mediump float;
//颜色混合
varying vec4 v_Color;
void main(){
    //赋值 到指定输出变量
    gl_FragColor = v_Color;
}