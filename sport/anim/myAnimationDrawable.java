package com.desay.sport.anim;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

public abstract class myAnimationDrawable extends AnimationDrawable {

    protected static final String TAG = "myAnimationDrawable";
	Handler finishHandler;      // 判断结束的Handler
    public myAnimationDrawable(AnimationDrawable ad) {
        // 这里得自己把每一帧加进去
        for (int i = 0; i < ad.getNumberOfFrames(); i++) {
            this.addFrame(ad.getFrame(i), ad.getDuration(i));
        }
        
    }


	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		 finishHandler = new Handler();
	        finishHandler.postDelayed(
	            new Runnable() {
	                public void run() {
	                    onAnimationEnd();
	                }
	            }, getTotalDuration());
	}
	
    public int getTotalDuration() {
        int durationTime = 0;
        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            durationTime += this.getDuration(i);
        }
        return durationTime;
    }

    
   public abstract void onAnimationEnd();

}
