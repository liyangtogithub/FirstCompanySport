package com.desay.sport.slidepage;

import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Log;

public abstract class AbstractChart implements IDemoChart
{
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
	  	renderer.setBackgroundColor( Color.rgb( 194,60,110 ) );	  	
	  	renderer.setXLabels(10);
		renderer.setYLabels(6);
		renderer.setShowGridX(true);
        renderer.setShowGridY(false);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(false);
	  	renderer.setZoomEnabled(false, false);
	  	renderer.setPanEnabled(false,false);//可否移动
		renderer.setShowAxes(false);//坐标轴线
		renderer.setShowCustomTextGrid(true);
		renderer.setShowLegend(false); 
		renderer.setMarginsColor(Color.argb(0xff,0xbb, 0x26, 0x5e));
		
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApplication.getInstance().getResources().getDisplayMetrics();
		int	screenWidth = dm.widthPixels;	
		int	screenHeight = dm.heightPixels;	
		
		if ( screenWidth==320 && screenHeight==480 )
		{
			renderer.setChartTitleTextSize(22);//最上面的标题
			renderer.setAxisTitleTextSize(18);
			renderer.setLabelsTextSize(18); 
			renderer.setMargins(new int[] { 37, 30, -14, 38 });// top,left,bottom,right
			//Log.i("AbstractChart",  "111111111111111111  screenWidth "+screenWidth+"  screenHeight "+screenHeight);
		}
	}
}
