package com.desay.sport.slidepage;

import java.util.HashMap;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;
import com.desay.sport.multimedia.MusicService;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;


public class SpeakTool  {
	
	private SoundPool   pool;
	private HashMap<Integer, Integer> soundPoolMap;
	private Context context;
    public SpeakTool(Context context) {
		// TODO Auto-generated constructor stub
    	this.context=context;
    	initSound();
    }

	
		
	public void playMinite(int pT){
		if(pT==0){
			return;
		}

		int i=pT;
		int i1;
		int i10;
		int i100;
		i100=i/100;
		i=i-i100*100;
		i10=i/10;
		i1=i-i10*10;
		if(i100>0){
			i=(Integer) soundPoolMap.get(i100);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
			i=(Integer) soundPoolMap.get(12);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
		}
		if(i10>0){
				i=(Integer) soundPoolMap.get(i10);
				pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(350);	
				i=(Integer) soundPoolMap.get(11);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
		}		
		if(i1>0){
			i=(Integer) soundPoolMap.get(i1);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);
		}
		if(i10==0&&i1==0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);
		}
		i=(Integer) soundPoolMap.get(16);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(500);
	}
	
	
	public void playSound(String pT){
		String s[] =pT.split("\\.");
		int i=Integer.parseInt(s[0]);
		int j=Integer.parseInt(s[1]);
		int j100=j/100;
		int j10=(j-j100*100)/10;
		int j1=j%10;
		int i1;
		int i10;
		int i100;
		i100=i/100;
		i=i-i100*100;
		i10=i/10;
		i1=i-i10*10;
		if(i100>0){
			i=(Integer) soundPoolMap.get(i100);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
			i=(Integer) soundPoolMap.get(12);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
		}
		if(i10>0){			
				i=(Integer) soundPoolMap.get(i10);
				pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(350);	
				i=(Integer) soundPoolMap.get(11);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
		}
		if(i10==0&&i100!=0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(350);	
		}
		if(i1>0){
			i=(Integer) soundPoolMap.get(i1);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(450);
		}if(i1==0&&i100==0&&i10==0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(450);
		}
		if(j100!=0||j10!=0||j1!=0){
		i=(Integer) soundPoolMap.get(15);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(400);
		}
		if(j100>0){
		i=(Integer) soundPoolMap.get(j100);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(400);
		}
		if((j100!=0||j10!=0||j1!=0)&&j100==0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(350);	
		}
		if(j10>0){
		i=(Integer) soundPoolMap.get(j10);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(400);
		}
		if((j1!=0)&&j10==0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(350);	
		}
		if(j1>0){
		i=(Integer) soundPoolMap.get(j1);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(400);
		}
		i=(Integer) soundPoolMap.get(0);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(800);
		
	}
	
	public  void playSecond(int pT){

		int i=pT;
		int i1;
		int i10;
		int i100;
		i100=i/100;
		i=i-i100*100;
		i10=i/10;
		i1=i-i10*10;
		if(i10>0){
				i=(Integer) soundPoolMap.get(i10);
				pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(350);	
				i=(Integer) soundPoolMap.get(11);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(400);	
		}
		if(i1>0){
			i=(Integer) soundPoolMap.get(i1);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(450);
		}
		if(i10==0&&i1==0){
			i=(Integer) soundPoolMap.get(10);
			pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
			delay(450);
		}
		i=(Integer) soundPoolMap.get(17);
		pool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(400);
	}
	
	private void initSound(){
		pool = new SoundPool(30, AudioManager.STREAM_MUSIC,5);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(0, pool.load(context, R.raw.skm, 1));
		soundPoolMap.put(1, pool.load(context, R.raw.s1, 1));
		soundPoolMap.put(2, pool.load(context, R.raw.s2, 1));
		soundPoolMap.put(3, pool.load(context, R.raw.s3, 1));
		soundPoolMap.put(4, pool.load(context, R.raw.s4, 1));
		soundPoolMap.put(5, pool.load(context, R.raw.s5, 1));
		soundPoolMap.put(6, pool.load(context, R.raw.s6, 1));
		soundPoolMap.put(7, pool.load(context, R.raw.s7, 1));
		soundPoolMap.put(8, pool.load(context, R.raw.s8, 1));
		soundPoolMap.put(9, pool.load(context, R.raw.s9, 1));
		soundPoolMap.put(10, pool.load(context, R.raw.s0, 1));
		soundPoolMap.put(11, pool.load(context, R.raw.ten, 1));
		soundPoolMap.put(12, pool.load(context, R.raw.hundred, 1));
		soundPoolMap.put(13, pool.load(context, R.raw.thousand, 1));
		soundPoolMap.put(14, pool.load(context, R.raw.wan, 1));
		soundPoolMap.put(15, pool.load(context, R.raw.dot, 1));
		soundPoolMap.put(16, pool.load(context, R.raw.minute, 1));
		soundPoolMap.put(17, pool.load(context, R.raw.second, 1));

		soundPoolMap.put(20, pool.load(context, R.raw.go, 1));
		soundPoolMap.put(21, pool.load(context, R.raw.great, 1));
		soundPoolMap.put(22, pool.load(context, R.raw.already, 1));
		soundPoolMap.put(23, pool.load(context, R.raw.time, 1));
		soundPoolMap.put(24, pool.load(context, R.raw.sports, 1));
		soundPoolMap.put(25, pool.load(context, R.raw.relax, 1));
		
		soundPoolMap.put(26, pool.load(context, R.raw.walk, 1));
		soundPoolMap.put(27, pool.load(context, R.raw.run, 1));		
		soundPoolMap.put(28, pool.load(context, R.raw.cycle, 1));
		
	}
	
	public void go(final Handler mHandler){
		int j=(Integer) soundPoolMap.get(20);
		pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
		delay(2000);
		Message msg = mHandler.obtainMessage(0);
        mHandler.sendMessage(msg);
	}
	
	public void readInfo(final Handler mHandler,final int min,final int second,final String distance,final int type){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int j=0;
				if(min>=10){
				j=(Integer) soundPoolMap.get(21);
				pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(1000);}
				
				j=(Integer) soundPoolMap.get(22);
				pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(500);
				if(type==0){
					j=(Integer) soundPoolMap.get(26);
					pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);				
				}else if(type==1){
					j=(Integer) soundPoolMap.get(27);
					pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				}else if(type==2){
					j=(Integer) soundPoolMap.get(28);
					pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				}
				delay(500);
				j=(Integer) soundPoolMap.get(23);
				pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(500);
				playMinite(min);
				playSecond(second);
				j=(Integer) soundPoolMap.get(24);
				pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);
				delay(500);
				playSound(distance);
				j=(Integer) soundPoolMap.get(25);
				pool.play(j, 1.0f, 1.0f, 0, 0, 1.0f);	
				delay(1000);
				Message msg = mHandler.obtainMessage(0);
		        mHandler.sendMessage(msg);
			}
		}).start();		
	}
	
	private void delay(long pt){
		long i=SystemClock.elapsedRealtime();
		while(SystemClock.elapsedRealtime()-i<pt);

	};
}