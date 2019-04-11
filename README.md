# RecyclerBarChart
基于Recyclerview实现 动态X/Y轴的运动步数 柱状图

"RecyclerView柱状图：1.动态无线滑动；2、根据显示中的柱状图动态改变Y坐标  3.通过RecyclerView的Decoration画纵坐标、横坐标、柱状图、value值;"

1.动态无线滑动， 接口动态添加数据  ;
2、根据显示中的柱状图动态改变Y坐标；（已完成）
3.通过RecyclerView的Decoration画纵坐标、横坐标、柱状图、value值;（已完成）

目前需要完成的：
	4. 需要动态添加 边框，左Y轴、右Y轴，顶部的value的文字显示、柱状图颜色、DisplayNumbers， Y坐标、X坐标的自定义。（已完成）
	5. Y轴变化的时候，添加动画。
	6. Canvas画仅顶部为圆角的 RoundRect。
	7. GridLine的控制是否显示 （已完成）
	8. Item的 触摸 事件。
	9. 封装 View。
	10. 画  周、日的 X 轴 （已完成）
 12.   参照 MPAndroidChart 自定义 X 坐标、Y坐标。（已完成 X 轴，完成一半）
 13.   Item 中柱状图所占的 比率，也就是 Space的宽度。（已完成）
	14. X轴刻度坐标左右的显示跟 iOS不一样。（已处理，itemDecoration 靠左。）
	15. 底部 X轴左边的 边界显示问题。（已修复）
	16. 周、月、日、年的统计问题，获取当前显示中的 柱状图的 日期跨度  在顶部显示。（已完成）
	17. Recyclerview左右预留空的，并带滑动 阻尼效果
	18. 左滑、右滑 ScrollToPosition是不一样的。
	19. 判断惯性滑动的速度超过某一个值的时候就 进行微调、否则就不调整。（方案调整）
	20. Override Recyclerview 的 fling 方法，降低惯性速度。（已修改）
	21. 超出一个 child width的 value文字显示的时候，有bug ，边界右滑有问题。(已修改，分离画柱状图跟文字的函数，各自单独处理)
	22. Y轴坐标变换，在临界点来回的变动，导致无法准确的改变 ，偶现的。（需要微调后，在继续计算Y轴刻度）
	23. 左右边界进入，颜色渐变动画，丝滑般的进入显示页面。需要处理的，Barchart柱体本身，X轴刻度文字，BarChart 的value文字。（待优化）
	24. 保留原始 RecyclerView的 paddingLeft， paddingRight。todo这里注意Padding是否会因为重绘而累加， 自定义padding属性，动态加入。（已完成）
	25. 自定义 CustomerRecyclerView的 xml attribute列表，解析为 Attrs 类中的静态值，对应代码中的 BarChartConfig，传给ItemDecoration使用。（已完成）
	26. 控制是否 显示的一些元素的 switch， 转移到BarChartConfig，到时候也可以直接用 attribute xml来控制。（完成）
	27. 调整滑动策略，滑动停止处于idle状态时，位置处于右边 1/3 往靠近右边刻度滑，处于左边 1/3往 靠近左边刻度滑动，处于中间 1/3 的保持不动，进行微调（22点处的方案微调）， 所有的滑动用 scrollToPosition，不用 smoothScrollToPosition。
	28. 控制是否 scrollToPosition的参数，customerRecyclerView的参数 放入到 attribute xml中？（完成）
	29. 如何自定义 Y轴，参照MPAndroidChart.
	30. 边界处的 lastVisiblePosition跟  FirstVisiblePosition需要 细心处理，目前存在bug。（完成）
	31. 抽取BarChartRecycler的Attrs, 修改部分属性为float，添加工具类 进行 float的 == 比较。（完成）
	32. YAxis、Xaxis 中的属性也要到 Attrs中设置。（已完成）
	33. 右边的 barChart 滑入有问题，没有渐渐消失。（已完成）
	34. 显示顶部的时间间断, 统计步数。(已完成)
	35. 底部有个浮动的 Date的标志， ItemDecoration添加。


微调方案：试图在ItemDecoration中进行微调，根据child的getRight、getLeft跟parentLeft、parentRight的位置来主动判断，
真正的显示边界(completeDisplayVisibleItemPosition),取到displayEntries； 
通过 XAxis传递给 ItemDecoration 进行微调。
存在难点：需要在 ItemDecoration本身的onDraw、onDrawOver中的 recycleView 用getChildAt（index）拿到的 child才不为null。

