package com.desay.sport.multimedia;

import java.io.IOException;

import com.desay.sport.main.R;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends Activity implements OnTouchListener{

	//private 
	MediaController mController;
	VideoView viv;
	int progress=0;
	private AudioManager mAudioManager;
	/** ������� */
	private int mMaxVolume;
	/** ��ǰ���� */
	private int mVolume = -1;
	private ImageView mOperationBg;
	private GestureDetector mGestureDetector;
	private View mVolumeBrightnessLayout;
	private ImageView mOperationPercent;
	private ImageButton playButton;
	private MediaPlayer mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.player);
//	ActionBar bar =  getActionBar();
//	bar.hide();
	viv=(VideoView)findViewById(R.id.videoView);
	
	mOperationBg = (ImageView) findViewById(R.id.operation_bg);
	mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
	playButton=(ImageButton) findViewById(R.id.button);
	mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
	mController=new MediaController(this);
	mController.setVisibility(View.INVISIBLE);
	viv.setMediaController(mController);
	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	mMaxVolume = mAudioManager
			.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	int videoname=getIntent().getIntExtra("videoname", -1);
	Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" +videoname); 
	
	if (videoname == -1 ) 
	{
       return;
	}
	viv.setVideoURI(uri);
	mediaPlayer=MediaPlayer.create(this, uri);
	int width=mediaPlayer.getVideoWidth();
	int height=mediaPlayer.getVideoHeight();
	System.out.println(width+" "+height);
	if(width>height){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}else{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	playButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viv.start();
			playButton.setVisibility(View.GONE);
		}
	});
	viv.requestFocus();
	mGestureDetector = new GestureDetector(this, new MyGestureListener());
	viv.start();
	viv.setOnCompletionListener(new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			playButton.setVisibility(View.VISIBLE);
			
		}
	});
	
	}
	
	private class MyGestureListener extends SimpleOnGestureListener {
		

		/** ���� */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			System.out.println("���»���-----------");
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			//if (mOldX > windowWidth * 4.0 / 5)// �ұ߻���
				onVolumeSlide((mOldY - y) / windowHeight);			
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// �������ƽ���
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}
	/** ���ƽ��� */
	private void endGesture() {
		mVolume = -1;

		// ����
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}
	/** ��ʱ���� */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// ��ʾ
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// �������
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// �������
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}
    @Override
    protected void onPause() 
    {
    	// TODO Auto-generated method stub
    	super.onPause();
    	progress=viv.getCurrentPosition();
    }
    @Override
    protected void onResume() 
    {
    	// TODO Auto-generated method stub
    	super.onResume();
    	viv.seekTo(progress);
    	viv.start();
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
		
	} 
}
