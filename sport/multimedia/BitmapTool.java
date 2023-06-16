package com.desay.sport.multimedia;

import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

public class BitmapTool {
	
	public static Bitmap getbitBmBykey(Context context,String albumkey){
		String path=gealbumby(context, albumkey);
		if(path==null){
			return null;
		}else{
		return getbitmap(path);
		}
	}

	public static String gealbumby(Context context, String albumkey) {
		// 取得歌曲对应的专辑Key 这里由于专辑图片太占内存 就不在播放列表上显示了
		String[] argArr = { albumkey };
		Cursor albumCursor = context.getContentResolver().query(
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
				MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?", argArr, null);
		if (null != albumCursor && albumCursor.getCount() > 0) {
			albumCursor.moveToFirst();
			int albumArtIndex = albumCursor
					.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
			String musicAlbumArtPath = albumCursor.getString(albumArtIndex);
			albumCursor.close();
			if (null != musicAlbumArtPath) {
				return musicAlbumArtPath;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public static Bitmap getbitmap(String albumpath){
		FileInputStream is = null;
		try {
			is = new FileInputStream(albumpath);
			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				if (is!=null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	} 
}
