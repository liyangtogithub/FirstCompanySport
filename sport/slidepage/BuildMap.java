package com.desay.sport.slidepage;

import java.util.List;

import android.content.Context;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.desay.sport.main.R;


public class BuildMap {
	//public static MapView mMapView = null; 
	static MapView mMapView = null;
	private MapController mMapController = null;
		
		public MapView creatmap(Context context,MapView mMapView,List<GeoPoint> points,List<Float> speeds) {
			// TODO Auto-generated method stub
			this.mMapView=mMapView;
			   mMapController = mMapView.getController();
		        mMapView.getController().setZoom(18);
		        mMapView.getController().enableClick(true);
				for (int i = 0; i < points.size()-1; i++) {
					GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
					mMapView.getOverlays().add(graphicsOverlay);
						graphicsOverlay.setData(drawLine(points.get(i), points.get(i+1),
								speeds.get(i+1)));
			    }
			    mMapView.refresh();
			    ItemizedOverlay ov = new ItemizedOverlay<OverlayItem>(context.getResources().getDrawable(R.drawable.map_start), mMapView);
			    OverlayItem item= new OverlayItem(points.get(0),"item","item");
			    OverlayItem item2= new OverlayItem(points.get(points.size()-1),"item","item");
			   	item.setMarker(context.getResources().getDrawable(R.drawable.map_start));
			   	item2.setMarker(context.getResources().getDrawable(R.drawable.map_end));
			    ov.addItem(item);
			    ov.addItem(item2);
			    mMapView.getOverlays().add(ov);
			    mMapView.refresh();
			    mMapView.getController().setCenter(points.get(0));
			    return mMapView;
		}
		
		public Graphic drawLine(GeoPoint pt1, GeoPoint pt2 ,float speed)
		{	
			Geometry lineGeometry = new Geometry();
			GeoPoint[] linePoints = new GeoPoint[2];
			linePoints[0] = pt1;
			linePoints[1] = pt2;
			lineGeometry.setPolyLine(linePoints);
			Symbol lineSymbol = new Symbol();
			Symbol.Color lineColor =null;
			if(speed<=2.0){
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xcc;
			lineColor.green = 0x73;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}else if(2.0<speed&&speed<=4.0){
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xcc;
			lineColor.green = 0x73;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}else if(4.0<speed&&speed<6.0){
			lineColor = lineSymbol.new Color();
			lineColor.red = 0x74;
			lineColor.green = 0x8b;
			lineColor.blue = 0x82;
			lineColor.alpha = 255;
		}else if(6.0<speed&&speed<8.0){
			lineColor = lineSymbol.new Color();
			lineColor.red = 0x87;
			lineColor.green = 0x74;
			lineColor.blue = 0x9b;
			lineColor.alpha = 255;
		}else {
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xe1;
			lineColor.green = 0x3a;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}
			lineSymbol.setLineSymbol(lineColor, 8);
			Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
			return lineGraphic;
		}
	
}
