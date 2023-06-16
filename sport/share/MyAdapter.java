/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */

package com.desay.sport.share;

import java.util.HashMap;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

/**
 * 一个用于演示{@link AuthorizeAdapter}的例子。
 * <p>
 * 本demo将在授权页面底部显示一个“关注官方微博”的提示框，
 *用户可以在授权期间对这个提示进行控制，选择关注或者不关
 *注，如果用户最后确定关注此平台官方微博，会在授权结束以
 *后执行关注的方法。
 */
public class MyAdapter extends AuthorizeAdapter{
	private CheckedTextView ctvFollow;
	private PlatformActionListener backListener;
    private Context context;
	public void onCreate() {
		context = getActivity();
		TitleLayout llTitle = getTitleLayout();
		int dp_10 = cn.sharesdk.framework.utils.R.dipToPx(getActivity(), 10);
		int dp_50 = cn.sharesdk.framework.utils.R.dipToPx(getActivity(), 50);
		ImageView iv_back =llTitle.getBtnBack();
		TextView  tv_title = llTitle.getTvTitle();
		llTitle.setBackgroundColor(Color.rgb(0xbb, 0x26, 0x5e));
		iv_back.setImageResource(R.drawable.btn_return);
		iv_back.setBackgroundResource(R.drawable.bg_button);
		iv_back.setPadding(dp_10, dp_10, dp_10, dp_10);
		tv_title.setText(context.getResources().getString(R.string.title_third_login));
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX  ,context.getResources().getDimension(R.dimen.fontsize_40));;
		tv_title.setTextColor(Color.WHITE);	
		tv_title.setGravity(Gravity.CENTER);
		tv_title.setPadding(0, 0, dp_50, 0);
		tv_title.setBackgroundColor(Color.rgb(0xbb, 0x26, 0x5e));
		llTitle.getChildAt(1).setVisibility(View.GONE);
		llTitle.getChildAt(3).setVisibility(View.GONE);
		llTitle.getChildAt(4).setVisibility(View.GONE);
		llTitle.getChildAt(5).setVisibility(View.GONE);	
	}
}
