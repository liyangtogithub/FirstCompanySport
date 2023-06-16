package com.desay.sport.multimedia;

import java.util.List;
import com.desay.sport.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter {

	private Context myCon;
	private static int pos=-1;
    private  List<Mp3Info> list;
	LayoutInflater inflate_item;
	public MusicListAdapter(Context con, List<Mp3Info> list) {		
		this.list=list;
		this.myCon=con;
	//inflate_item = LayoutInflater.from(con);
}



	public int getCount() {
		return list.size();
	}

	
	public Object getItem(int position) {
		return position;
	}


	public long getItemId(int position) {
		return position;
	}
	public void setItemIcon(int position){
		pos = position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		Mp3Info m=list.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView =  LayoutInflater.from(myCon).inflate(R.layout.musiclist_item,null);		
		holder.tv_music = (TextView) convertView.findViewById(R.id.music);
		holder.tv_singer = (TextView) convertView.findViewById(R.id.singer);
		holder.tv_time = (TextView) convertView.findViewById(R.id.time);
		holder.img = (ImageView)convertView.findViewById(R.id.listitem);
		convertView.setTag(holder);}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		if (m.getMusicName().length()>24){
			try {
				String musicTitle = bSubstring(m.getMusicName().trim(),24);
				holder.tv_music.setText(musicTitle);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else {
			holder.tv_music.setText(m.getMusicName().trim());
		}
		
		if (m.getSinger().equals("<unknown>")){
			holder.tv_singer.setText(myCon.getString(R.string.unknown));
		}else{
			holder.tv_singer.setText(m.getSinger());
		}
		if (position == pos){
			holder.img.setImageResource(R.drawable.isplaying);
		}else{
			holder.img.setImageResource(R.drawable.item);
		}
		holder.tv_time.setText(toTime(m.getTime()));
		convertView.setBackgroundResource(R.drawable.list_rect_selector);
		
		return convertView;
	}
	
	public static class ViewHolder{
		TextView tv_music;
		TextView tv_singer;
		TextView tv_time;
		ImageView img;
		
	}
	/**
	 * ʱ���ʽת��
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
	
	/**
	 * 字符串裁剪
	 * @param s
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static String bSubstring(String s, int length) throws Exception  
	{  
	 
	    byte[] bytes = s.getBytes("Unicode");  
	    int n = 0; // 表示当前的字节数  
	    int i = 2; // 要截取的字节数，从第3个字节开始  
	    for (; i < bytes.length && n < length; i++)  
	    {  
	        // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节  
	        if (i % 2 == 1)  
	        {  
	            n++; // 在UCS2第二个字节时n加1  
	        }  
	        else 
	        {  
	            // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节  
	            if (bytes[i] != 0)  
	            {  
	                n++;  
	            }  
	        }  
	    }  
	    // 如果i为奇数时，处理成偶数  
	    if (i % 2 == 1)  
	 
	    {  
	        // 该UCS2字符是汉字时，去掉这个截一半的汉字  
	        if (bytes[i - 1] != 0)  
	            i = i - 1;  
	        // 该UCS2字符是字母或数字，则保留该字符  
	        else 
	            i = i + 1;  
	    }  
	 
	    return new String(bytes, 0, i, "Unicode");  
	}  

}
