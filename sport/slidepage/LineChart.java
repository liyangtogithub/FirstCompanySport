
package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class LineChart extends AbstractChart
{
	public static List<Double> x=new ArrayList<Double>();
	public static List<Double> values=new ArrayList<Double>();
	private static double maxY=0;
	String yTitle = null;
	public static int heartMax = 0;
	public static int heartAvg = 0;
	public static int heartNormal = 0;
	
	public View execute(Context context, double x1, double y1)
	{
		x.add(x1);
		values.add(y1);
		if(y1>maxY){
			maxY=y1;
		}
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setBarSpacing(0);
		renderer.setAxisTitleTextSize(25);
		renderer.setLabelsTextSize(25); 
		renderer.setChartTitleTextSize(30);
		renderer.setMargins(new int[] {  50, 43, -20, 57 });// top,left,bottom,right
		
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();			
			XYSeries series = new XYSeries("");
			List<Double> xV = x;
			List<Double> yV = values;
			int seriesLength = xV.size();
			for (int k = 0; k < seriesLength; k++)
			{
				series.add(xV.get(k), yV.get(k));
			}
			dataset.addSeries(0,series);				  
		  	XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor( Color.WHITE);
			r.setLineWidth(6.0f); 
			renderer.addSeriesRenderer(r);
			View view=ChartFactory.getCubeLineChartView(context, dataset, renderer, 0.3f);					 
		double xValue = 0;
		if ((xValue = x.get(x.size()-1)) > 5)
		{				
			setChartSettings(renderer,context.getString(R.string.sportrount), context.getString(R.string.xlable), 
				context.getString(R.string.ylable),(xValue - 5), xValue + 2, 0, maxY>10?maxY+2:10, Color.WHITE, Color.WHITE);
		}
		else
		{
			setChartSettings(renderer, context.getString(R.string.sportrount), context.getString(R.string.xlable), 
					context.getString(R.string.ylable), 0.1, 7,0,  maxY>10?maxY+2:10,
					Color.WHITE, Color.WHITE);
		}

		return view;
	}
	/**
	 *  画速度曲线图表
	 */
	public View execute(Context context, List<Double> x1, List<Double> y)
	{
		double yMax = 0;
		double yTem = 0;
		double xMax = 0;
		double xTem = 0;

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setBarSpacing(0);
		renderer.setAxisTitleTextSize(25);
		renderer.setLabelsTextSize(25);
		renderer.setChartTitleTextSize(30);
		renderer.setMargins(new int[] { 60, 43, -20, 55 });// top,left,bottom,right

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		int seriesLength = x1.size();
		for (int k = 0; k < seriesLength; k++)
		{
			yTem = y.get(k);
			if (yTem > yMax)
			{
				yMax = yTem;
			}
			xTem = x1.get(k);
			if (xTem > xMax)
			{
				xMax = xTem;
			}
			series.add(xTem, yTem);
		}
		dataset.addSeries(0, series);
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);
		r.setLineWidth(6.0f);
		renderer.addSeriesRenderer(r);

		View view = null;
		yTitle = context.getString(R.string.ylable);
		view = ChartFactory.getCubeLineChartView(context, dataset, renderer,
				0.3f);

		setChartSettings(renderer, context.getString(R.string.sportrount),
				context.getString(R.string.xlable), yTitle, 0,
				xMax > 7 ? (xMax + 2) : 7, 0, yMax > 10 ? (yMax + 2) : 10,
				Color.WHITE, Color.WHITE);
		return view;
	}

	/**
	 *  画心率折线图表
	 */
//	public View execute(Context context, List<Double> x1, List<Double> y,int heart)
//	{
//		double yMax = 0;
//		double yTem = 0;
//		double xMax = 0;
//		double xTem = 0;
//		double ySum = 0;
//
//		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//		renderer.setBarSpacing(0);
//		renderer.setAxisTitleTextSize(25);
//		renderer.setLabelsTextSize(25);
//		renderer.setChartTitleTextSize(30);
//		renderer.setMargins(new int[] {60, 70, 0, 40 });// top,left,bottom,right
//
//		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
//		XYSeries series = new XYSeries("");
//
//		XYSeries seriesHeart = new XYSeries("");
//		entity_userinfo info = new sportDB(context).GetUserInfo(context);
//		String Age = info.getAge();
//		int yHeart = 200;
//		if (Age != null )
//		{
//			yHeart = 220 - Integer.parseInt(Age);
//		}
//
//		int seriesLength = x1.size();
//		for (int k = 0; k < seriesLength; k++)
//		{
//			yTem = y.get(k);
//            ySum = ySum + yTem;
//			if (yTem > yMax)
//			{
//				yMax = yTem;
//			}
//			xTem = x1.get(k);
//			if (xTem > xMax)
//			{
//				xMax = xTem;
//			}
//			series.add(xTem, yTem);
//			seriesHeart.add(xTem, yHeart);
//		}
//		heartMax = (int) yMax;
//		heartNormal = yHeart;
//		heartAvg = (int) (ySum/y.size());
//		dataset.addSeries(series);
//		dataset.addSeries(seriesHeart);
//
//		XYSeriesRenderer r = new XYSeriesRenderer();
//		r.setColor(Color.WHITE);
//		r.setLineWidth(4.0f);
//		renderer.addSeriesRenderer(r);
//
//		XYSeriesRenderer rHeart = new XYSeriesRenderer();
//		rHeart.setColor(Color.GREEN);
//		rHeart.setLineWidth(3.0f);
//		renderer.addSeriesRenderer(rHeart);
//
//		View view = null;
//		yTitle = context.getString(R.string.ylable2);
//		view = ChartFactory.getLineChartView(context, dataset, renderer);
//
//		setChartSettings(renderer, context.getString(R.string.sportrount),
//				context.getString(R.string.xlable), yTitle, 0,
//				xMax > 7 ? (xMax + 2) : 7, 0, yMax > 10 ? (yMax + 2) : 10,
//				Color.WHITE, Color.WHITE);
//		return view;
//	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDesc()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
