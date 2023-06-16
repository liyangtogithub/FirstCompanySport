package com.desay.sport.slidepage;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class MoveBg {
	public static final int ANIMATION_DURATION = 300;

	public static TranslateAnimation moveFrontBg(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(ANIMATION_DURATION);
		anim.setFillAfter(true);
		v.startAnimation(anim);
        return anim;
	}
}
