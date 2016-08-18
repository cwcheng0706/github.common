/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月18日 下午9:38:52
 */
package com.zy.springboot_1.controller;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月18日 下午9:38:52
 */
@Controller
public class JfreeChartController {

	@RequestMapping(value = "/getColumnChart")
	public String columnChart(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		// 1. 获得数据集合
//		CategoryDataset dataset = getDataSet();
		CategoryDataset dataset = createDataset();
		// 2. 创建柱状图
		JFreeChart chart = ChartFactory.createBarChart3D("学生对教师授课满意度", // 图表标题
				"课程名", // 目录轴的显示标签
				"百分比", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				false, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
		);
		// 3. 设置整个柱状图的颜色和文字（char对象的设置是针对整个图形的设置）
		chart.setBackgroundPaint(ChartColor.WHITE); // 设置总的背景颜色

		// 4. 获得图形对象，并通过此对象对图形的颜色文字进行设置
		CategoryPlot p = chart.getCategoryPlot();// 获得图表对象
		p.setBackgroundPaint(ChartColor.lightGray);// 图形背景颜色
		p.setRangeGridlinePaint(ChartColor.WHITE);// 图形表格颜色

		// 5. 设置柱子宽度
		BarRenderer renderer = (BarRenderer) p.getRenderer();
//		BarRenderer3D renderer = (BarRenderer3D) p.getRenderer();
		renderer.setMaximumBarWidth(0.06);
		//6.设置柱子颜色
		renderer.setSeriesPaint(0, Color.decode("#24F4DB")); // 给series1 Bar
		renderer.setSeriesPaint(1, Color.decode("#7979FF")); // 给series2 Bar
		renderer.setSeriesPaint(2, Color.decode("#FF5555")); // 给series3 Bar
		renderer.setSeriesPaint(3, Color.decode("#F8D661")); // 给series4 Bar
		renderer.setSeriesPaint(4, Color.decode("#F284DC")); // 给series5 Bar
		renderer.setSeriesPaint(5, Color.decode("#00BF00")); // 给series6 Bar
		renderer.setSeriesOutlinePaint(0,Color.BLACK);//边框为黑色
		renderer.setSeriesOutlinePaint(1,Color.BLACK);//边框为黑色
		renderer.setSeriesOutlinePaint(2,Color.BLACK); //边框为黑色
		renderer.setSeriesOutlinePaint(3,Color.BLACK);//边框为黑色
		renderer.setSeriesOutlinePaint(4,Color.BLACK);//边框为黑色
		renderer.setSeriesOutlinePaint(5,Color.BLACK); //边框为黑色
		
		
		
		//这一部分很重要。当柱体太小时候。数值不能显示，那么只有改边显示的位置
//		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("1111", new DecimalFormat()));//自定义显示每个柱的数值
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//默认显示每个柱的数值
		renderer.setBaseItemLabelsVisible(true); 
		//设置不能在柱子上正常显示的那些数值的显示方式，将这些数值显示在柱子外面
	    ItemLabelPosition itemLabelPositionFallback = new ItemLabelPosition(
	    ItemLabelAnchor.OUTSIDE12,TextAnchor.BASELINE_LEFT,TextAnchor.HALF_ASCENT_LEFT,-0.0D);
	    //设置当不能正常显示柱状值的时候
	    renderer.setPositiveItemLabelPositionFallback(itemLabelPositionFallback);
	    renderer.setNegativeItemLabelPositionFallback(itemLabelPositionFallback);
	    
	    p.setRenderer(renderer);
	    
	    

		// 解决乱码问题
		getChartByFont(chart);
		
		
		// 6. 将图形转换为图片，传到前台
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FileOutputStream fos_jpg = null;
		try {
			fos_jpg=new FileOutputStream("f:\\项目状态分布.jpg");
			ChartUtilities.writeChartAsJPEG(bos,100,chart,640,480);
			
			//fos_jpg.close();
		} catch (Exception e) {
		}

		// 6. 将图形转换为图片，传到前台
		String fileName = ServletUtilities.saveChartAsJPEG(chart, 700, 400, null, request.getSession());
		String chartURL = request.getContextPath() + "/chart?filename=" + fileName;
		model.addAttribute("chartURL", chartURL);
		return "/jfreechart/columnChart";
	}

	// 设置文字样式
	private static void getChartByFont(JFreeChart chart) {
		// 1. 图形标题文字设置
		TextTitle textTitle = chart.getTitle();
		String DATE_FORMAT_LONG="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_LONG);
		TextTitle source = new TextTitle(sdf.format(new Date()));
		textTitle.setFont(new Font("宋体", Font.BOLD, 20));
		chart.addSubtitle(source);

		// 2. 图形X轴坐标文字的设置
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = plot.getDomainAxis();
		axis.setLabelFont(new Font("宋体", Font.BOLD, 22)); // 设置X轴坐标上标题的文字
		axis.setTickLabelFont(new Font("宋体", Font.BOLD, 15)); // 设置X轴坐标上的文字

		// 2. 图形Y轴坐标文字的设置
		ValueAxis valueAxis = plot.getRangeAxis();
		valueAxis.setLabelFont(new Font("宋体", Font.BOLD, 15)); // 设置Y轴坐标上标题的文字
		valueAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD, 12));// 设置Y轴坐标上的文字
	}

	// 获取一个演示用的组合数据集对象
	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(300000, "", "普通动物学");
		dataset.addValue(500000, "", "生物学");
		dataset.addValue(600000, "", "动物解剖学");
		dataset.addValue(700000, "", "生物理论课");
		dataset.addValue(800000, "", "动物理论课");
		
		return dataset;
	}
	
	private static CategoryDataset createDataset() {  
        // 时间维度  
        String[] category1 = { "第一季度", "第二季度", "第三季度", "第四季度" };  
        // 分类维度  
        String[] category2 = { "JAVA", "C/C++", "PHP" };  
        DefaultCategoryDataset defaultdataset = new DefaultCategoryDataset();  
        for (int i = 0; i < category1.length; i++) {  
            String category = category1[i];  
            for (int j = 0; j < category2.length; j++) {  
                String cat = category2[j];  
                // 模拟添加数据  
                defaultdataset.addValue(DataUtils.getRandomData(), cat, category);  
            }  
        }  
        return defaultdataset;  
    }  
}
class DataUtils {  
    private static Random random = new Random();  
    private static final int MAX_NUMBER = 100;  
  
    /** 
     *  随机在0到100间取数 
     * @return  
     */  
    public static int getRandomData() {  
        return random.nextInt(MAX_NUMBER);  
    }  
}  
