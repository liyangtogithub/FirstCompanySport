package com.desay.sport.multimedia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;


public class AlbumImageLoader {
	private HashMap<String, SoftReference<Bitmap>> caches;
	Context mContext;
	private ArrayList<Task> taskQueue;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Task task = (Task)msg.obj;
			task.callback.loadedImage(task.albumkey, task.bitmap);
		}
		
	};
	
	public Thread getThread(){
		
		Thread thread = new Thread(){
			@Override
			public void run() {
					while(taskQueue.size()>0){
						Task task = taskQueue.remove(0);
						try {
							Bitmap bitmap=BitmapTool.getbitBmBykey(mContext, task.albumkey);
							if(bitmap==null){
								return ;
							}
							task.bitmap=compressImage(bitmap);
							caches.put(task.albumkey, new SoftReference<Bitmap>(task.bitmap));
							if(handler!=null){
								Message msg = handler.obtainMessage();
								msg.obj = task;
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			}
			
		};
		return thread;
	}
	
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while ( baos.toByteArray().length / 1024>40) {		
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap compressBitmap = BitmapFactory.decodeStream(isBm, null, null);
		return compressBitmap;
	}

	
	public AlbumImageLoader(Context context){
		caches =new  HashMap<String, SoftReference<Bitmap>>();
		mContext=context;
		taskQueue = new ArrayList<AlbumImageLoader.Task>();
		//thread.start();
	}
	
	public Bitmap loadImage(final String albumkey,final ImageCallback callback){
		if(caches.containsKey(albumkey)){
			SoftReference<Bitmap> rf = caches.get(albumkey);
			Bitmap bitmap = rf.get();
			if(bitmap==null){
				caches.remove(albumkey);				
			}else{
				return bitmap;
			}
		}
		if(!caches.containsKey(albumkey)){
			Task task = new Task();
			task.albumkey=albumkey;
			task.callback = callback;
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				Thread thread=getThread();
				synchronized(thread){
						thread.start();
				}	
			}
		}
		return null;
	}
	
	public interface ImageCallback{
		void loadedImage(String albumkey,Bitmap bitmap);
	}
	class Task{
		String albumkey;
		Bitmap bitmap;
		ImageCallback callback;
		
		
	}
	
}
