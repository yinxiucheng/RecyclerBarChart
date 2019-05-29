package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @date 2019-05-10
 */
public class BaseChartAttrs {

    public float contentPaddingBottom;//底部内容绘制高度
    public float contentPaddingTop;//顶部内容绘制

    public double ratioVelocity;//recyclerView 惯性滑动的 加速度 比率。
    public double ratioSpeed;//LinearLayoutManager 速度的 比率。

    public int layoutManagerOrientation;//layout  orientation
    public boolean layoutManagerReverseLayout;//layout horizontal layout from right to left, default is true;

    public boolean enableYAxisZero;// 控制是否显示 Y轴中的 0 刻度线
    public boolean enableYAxisGridLine;// 控制是否显示 y轴对应的横轴网格线
    public boolean enableRightYAxisLabel;//控制是否显示 Y轴右刻度
    public boolean enableLeftYAxisLabel;// 控制是否显示 Y轴左刻度
    public boolean enableXAxisGridLine;//控制X对应的纵轴的网格线
    public boolean enableXAxisFirstGridLine;//控制X对应的First_line网格线
    public boolean enableXAxisSecondGridLine;//控制X对应的Second_line网格线
    public boolean enableXAxisThirdGridLine;//控制X对应的Third_line网格线
    public boolean enableXAxisLabel;//控制X轴刻度的绘制
    public boolean enableXAxisDisplayLabel;//控制无规格的X轴刻度，供首页显示用。
    public boolean enableXAxisLineCircle;//控制 折线图 value处的圈
    public boolean enableBarBorder;//控制是否显示边框

    public float yAxisMaximum;//y轴刻度默认的最大刻度
    public float yAxisMinimum;//y轴刻度默认的最小刻度
    public float yAxisLabelTxtSize;//y轴刻度字体大小
    public int yAxisLabelTxtColor;//y轴字体颜色
    public int yAxisLabelSize;//y轴刻度的格数
    public int yAxisLineColor;//y轴对应的网格线的颜色
    public float yAxisLabelHorizontalPadding;//刻度字跟边框的间距
    public float yAxisLabelVerticalPadding;//刻度 字跟刻度线的位置对齐的调整
    public boolean yAxisReverse;//y坐标 reverse，value从上到下增大。

    public float xAxisTxtSize;//x轴刻度字体大小
    public int xAxisTxtColor;//x轴刻度字体颜色
    public int xAxisFirstDividerColor;//x轴对应纵轴 第一种网格线颜色
    public int xAxisSecondDividerColor;//x轴对应纵轴 第二种网格线颜色
    public int xAxisThirdDividerColor;//x轴对应纵轴 第三种网格线颜色
    public float xAxisLabelTxtPadding;//x轴刻度跟 坐标线之间的间距（不居中的情况下)
    public int xAxisScaleDistance;//x轴刻度 文字的间距


}
