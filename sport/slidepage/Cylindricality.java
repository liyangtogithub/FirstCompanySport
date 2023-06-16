package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.desay.sport.data.SportData;
import com.desay.sport.data.SwitchDayNum;
import com.desay.sport.main.R;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class Cylindricality
{
	double yMax ;
	double yTem ;
	int x = 0;
	int left = 35;
	String xLable = null;
	/** 
	 * 根据当前的页数，决定显示每天的柱形图，还是每月的柱形图
	 */
	public View execute(Context context, List<Integer> values1, int pageNum ,int year ,int month)
	{		
		List<Integer> values = values1;
		int[] colors = new int[]
		{ Color.WHITE };
		//don't change place before ask for liyang
		XYMultipleSeriesDataset seriesDataset =  buildBarDataset(values,pageNum);
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		
		if (pageNum == SportData.TAB_STEP_ONE)
			 x = 24;
		else 
			 x = SwitchDayNum.getDayNum(year, month+1);
		setChartSettings(renderer,null, null,null,0, x, 0, yMax>0 ? (yMax+0.5):7, Color.GRAY, Color.WHITE);
		return ChartFactory.getBarChartView(context,seriesDataset,
				renderer, Type.DEFAULT);
	}

	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors)
	{
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setBarSpacing(0);
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApplication.getInstance().getResources().getDisplayMetrics();
		int	screenWidth = dm.widthPixels;	
		int	screenHeight = dm.heightPixels;	
		renderer.setShowAxes(true);//坐标轴
		if (yMax>=1000)
			x=11;
		else if (yMax>=10000) {
			x=22;
		}else {
			x=0;
		}
		if ( screenWidth==320 && screenHeight==480 )    //928竖屏
		{
			renderer.setChartTitleTextSize(22);
			renderer.setAxisTitleTextSize(16);//单位
			renderer.setLabelsTextSize(18); //坐标
			renderer.setMargins(new int[] { 35, 30+x, -12, 33 });// top,left,bottom,right
		}
		else if (screenWidth==480 && screenHeight==320) {//928横屏
			renderer.setAxisTitleTextSize(17);
			renderer.setLabelsTextSize(20); 
			renderer.setMargins(new int[] { 40, 35+x, -13, 38 });// top,left,bottom,right
		}
		else if(screenWidth>=1080 && screenHeight<=1920){                                           //其他手机
			renderer.setAxisTitleTextSize(40);
			renderer.setLabelsTextSize(40); 
			renderer.setChartTitleTextSize(30);			
			renderer.setMargins(new int[] {100,100+x, -20, 100});//top,left,bottom,right
		}else{
			renderer.setAxisTitleTextSize(25);
			renderer.setLabelsTextSize(25); 
			renderer.setChartTitleTextSize(30);			
			renderer.setMargins(new int[] {40,50+x, -20, 50});//top,left,bottom,right
		}
			
		renderer.setLegendTextSize(15);

		int length = colors.length;
		for (int i = 0; i < length; i++)
		{
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor)
	{
		renderer.setChartTitle(title);
		renderer.setXTitle("");
		renderer.setYTitle("");
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setApplyBackgroundColor(true);
	  	renderer.setBackgroundColor(Color.argb(0xff,0xc2, 0x3c, 0x6e));//设置坐标内部背景颜色
		
	  	renderer.setShowGrid(true);	
		renderer.setShowGridX(true);
        renderer.setShowGridY(false);//只显示X轴线
		renderer.setXLabels(10);
		renderer.setYLabels(6);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPanEnabled(false, false);//可否移动
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(false,false);//放大
		renderer.setBarSpacing(0.5f);//间距
		renderer.setShowLegend(false); //最下面的文字注释
		
		renderer.setShowAxes(true);//坐标轴
		
		renderer.setMarginsColor(Color.argb(0x00,0xbc, 0x26, 0x5f));// 坐标轴外的背景色
	}

	protected XYMultipleSeriesDataset buildBarDataset(List<Integer> values,int mode)
	{
		yMax = 0 ;
		yTem = 0 ;
		double unit = 1;
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		CategorySeries series = new CategorySeries("");
		switch(mode)
		{
		case SportData.TAB_STAGE_MIDDLE:
			 unit = 1000.0;
			 break;
		case SportData.TAB_STEP_ONE:
		case SportData.TAB_STEP_TWO:
			unit = 1.0;
			break;
		}
		for (int k = 0; k < values.size(); k++)
		{	
			yTem = values.get(k)/unit;		
			if ( yTem > yMax )
			{
				yMax = yTem;
			}
			series.add(yTem);
		}
		dataset.addSeries(series.toXYSeries());
		return dataset;
	}
}
