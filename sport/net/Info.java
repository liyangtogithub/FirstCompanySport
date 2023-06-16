package com.desay.sport.net;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.dateAdapter_dialog;
import com.desay.utils.ContentWatcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Info extends Activity implements OnClickListener{
	private static final String TAG = "Info";
	private TextView tv_title;
	private ImageView iv_return;
	private ImageView iv_save;
	private ImageView iv_photo;
	private ImageView rl_image;
	private TextView tv_user;
	private TextView tv_sex;
	private TextView tv_birthday;
	private EditText et_height;
	private EditText et_weight;	
	private TextView tv_city;
	private TextView tv_load;
	private Context context;
	private Handler handle;
	private entity_userinfo info;
	private sportDB db = null;
//	private ContentWatcher cwatcher1 = null;
	private ContentWatcher cwatcher2 = null;
	private ContentWatcher cwatcher3 = null;
	public DatePickerDialog datePickerDialog = null;
	private Calendar cal = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.info);	
		context = Info.this;
		db = new sportDB(context);

		init();
		initEditInfo();
		handle = new Handler(){@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case SportData.TOAST_SEX_NULL:
				showToast.normaltoast(context, getString(R.string.sexisnull), showToast.ONE_SECOND);
				break;
			case SportData.TOAST_BIRTHDAY_NULL:
				showToast.normaltoast(context, getString(R.string.birthdayisnull), showToast.ONE_SECOND);
				break;
			case SportData.TOAST_HEIGHT_NULL:
				showToast.normaltoast(context, getString(R.string.heightisnull), showToast.ONE_SECOND);
				break;
			case SportData.TOAST_WEIGHT_NULL:
				showToast.normaltoast(context, getString(R.string.weightisnull), showToast.ONE_SECOND);
				break;
			case SportData.TOAST_CITY_NULL:
				showToast.normaltoast(context, getString(R.string.cityisnull), showToast.ONE_SECOND);
				break;
			}
			super.handleMessage(msg);
		}};
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.tv_city:
			ActivateInputStatus();
			Intent i = new Intent(context,ChooseCityActivity.class);
			startActivityForResult(i,SportData.REQUESTCODE_CITY);	
			break;
		case R.id.iv_return:
			finish();
			break;
		case R.id.tv_sex:
			showDialog(context,SportData.ICON_SEX);
			ActivateInputStatus();
			break;
		case R.id.iv_save:
			if(checkInputValid())
			{
				info.setSex(tv_sex.getText().toString());
				info.setBirthday(tv_birthday.getText().toString().replace("-", ""));
				info.setHeight(et_height.getText().toString());
				info.setWeight(et_weight.getText().toString());
				info.setCity(tv_city.getText().toString());
				info.setUpLoad(SportData.UNUPLOAD);
				db.SaveUserInfo(context,info);
				if(!SportData.getUserName(Info.this).equals(SportData.DEFAULTUSERNAME))
				{
			      //��������ͬ����ݵ�ƽ??
					Info.this.startService(new Intent(Info.this,SendServer.class));
				}
				showToast.normaltoast(context, getString(R.string.save_success), showToast.TWO_SECOND);
				finish();
			}
			
			break;
		case R.id.rl_image:
			showDialog(context,SportData.ICON_PHOTOSELECT);
			ActivateInputStatus();
			break;
		case R.id.tv_load:
			if(!SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
			{//�û�ע��
				SportData.UpdateUserName(context,SportData.DEFAULTUSERNAME);
				Outload();
				resetConnect();
				Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
			  	sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_LOGINOUT);
		    	sendBroadcast(sendintent);
		    	showToast.normaltoast(context, getString(R.string.logout_success), showToast.ONE_SECOND);
		    	return ;
			}
			else
			{
				Intent loadinttent = new Intent(context,Login.class);
			    startActivity(loadinttent);
//			    finish();
			}
			break;
		case R.id.tv_birthday:
			ActivateInputStatus();
			datePickerDialog.show();
			break;
		}		
	}
	
//	@Override
//	public void afterTextChanged(Editable s) {
//		// TODO Auto-generated method stub
//	}
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//		// TODO Auto-generated method stub
//	}
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		// TODO Auto-generated method stub
//		if(!iffirstlogin)
//		ActivateInputStatus();
//	} 
	
	private void resetConnect()
	{// reset connect
		SportData.sendBroadCast(Info.this,MainTab.REMOVECONNECT);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		cwatcher1.setLoginStatus(false);
		cwatcher2.setLoginStatus(false);
		cwatcher3.setLoginStatus(false);
		super.onResume();
	}
	protected void onDestroy() {
		 context.unregisterReceiver(infoLoadReceiver);
		 super.onDestroy();
	 };
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	    String resultData; 
		Bundle extras;
		Log.d(TAG, "requestCode=="+requestCode+"resultCode=="+resultCode+"data=="+data);
		if(SportData.REQUESTCODE_CITY==requestCode)
		{
				try{
					resultData = data.getExtras().getString("updatecity");
				}catch(Exception e)
				{
					resultData="";
				}
		    	if(resultData!=null&&!resultData.equals("")) {
		    		tv_city.setText(resultData);
		    	}
		}
		else
		{
			if (resultCode == SportData.REQUESTCODE_NONE)
				return;
			if (data == null)
				return;
			if (requestCode == SportData.REQUESTCODE_PHOTOGRAPH) {
				
				Bundle bundle = data.getExtras();  
	            Bitmap bitmap = (Bitmap) bundle.get("data");
				startPhotoZoomNew(bitmap);  
			}  

			if (requestCode == SportData.REQUESTCODE_PHOTOZOOM) {
				startPhotoZoomOld(data.getData());  
			}  
			if (requestCode == SportData.REQUESTCODE_PHOTOSAVE) {
				extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
					info.setHeadPhoto(SportData.bytesToHexString(stream.toByteArray()));
					if(photo!=null)
					{
						iv_photo.setImageBitmap(SportData.toRoundBitmap(photo));
					    iv_photo.setVisibility(View.VISIBLE);
					}
				}  
			}
		}	
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void init()
	{
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_save = (ImageView) findViewById(R.id.iv_save);
		tv_user = (TextView) findViewById(R.id.tv_user);
		tv_load = (TextView) findViewById(R.id.tv_load);
	
		tv_title.setText(SportData.GetSlidePageTitle(context, SportData.PAGE_INFO));
		iv_return.setOnClickListener(this);
		iv_save.setOnClickListener(this);
		tv_load.setOnClickListener(this);
		
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		et_height = (EditText) findViewById(R.id.et_height);
		et_weight = (EditText) findViewById(R.id.et_weight);	
		tv_city = (TextView) findViewById(R.id.tv_city);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		rl_image = (ImageView) findViewById(R.id.rl_image);

		rl_image.setOnClickListener(this);
		tv_sex.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_birthday.setOnClickListener(this);
//		cwatcher1 = new ContentWatcher(context,tv_birthday,tv_user,iv_save,true);
		cwatcher2 = new ContentWatcher(context,et_height,tv_user,iv_save,true);
		cwatcher3 = new ContentWatcher(context,et_weight,tv_user,iv_save,true);		

//		et_age.addTextChangedListener(cwatcher1);
		et_height.addTextChangedListener(cwatcher2);		
		et_weight.addTextChangedListener(cwatcher3);	
	
		IntentFilter intentFilter = new IntentFilter(); 
	    intentFilter.addAction(SportData.RECEIVER_LOAD);
	    context.registerReceiver(infoLoadReceiver , intentFilter);
	}
	public void initEditInfo()
	{
		cal = Calendar.getInstance();
		info = db.GetUserInfo(context);
	    int year  = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day   = cal.get(Calendar.DAY_OF_MONTH);
		if(SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
		{
			   tv_user.setText(getString(R.string.unknow_str));
			   tv_load.setText(getString(R.string.userlogin));
		 }
		else
		{
			  tv_user.setText(info.getShowname());
			  tv_load.setText(getString(R.string.userlogout));
		}
		if(info.getSex()!=null && !info.getSex().equals(""))tv_sex.setText(info.getSex());
		if(info.getBirthday()!=null && !info.getBirthday().equals(""))
		{
			if(info.getBirthday().length()==8)
			{
				System.out.println("��������");
			    year = Integer.valueOf(info.getBirthday().substring(0, 4));
			    month = Integer.valueOf(info.getBirthday().substring(4, 6));
			    day =  Integer.valueOf(info.getBirthday().substring(6));   
			    tv_birthday.setText(year+"-"+info.getBirthday().substring(4, 6)+"-"+info.getBirthday().substring(6));
			}
			else//���������
			{
				System.out.println("����");
				year  = year-Integer.valueOf(info.getBirthday());
				month = 1;
				day   = 1;
				HashMap<String, String> cv = new HashMap<String, String>();
				cv.put(sportDB.ID_AGE,year+"0101");
				cv.put(sportDB.ID_IFUPLOAD,SportData.UNUPLOAD);
				db.refreshTable_UserInfo(SportData.getUserName(context), cv);
				tv_birthday.setText(year+"-01-01");
			}
			month -- ;
		}
		if(info.getHeight()!=null && !info.getHeight().equals(""))et_height.setText(info.getHeight());
		if(info.getWeight()!=null && !info.getWeight().equals(""))et_weight.setText(info.getWeight());		
		if(info.getCity()!=null && !info.getCity().equals(""))tv_city.setText(info.getCity());
		if(info.getHeadPhoto()!=null && !info.getHeadPhoto().equals(""))
		{
			byte[] photo = SportData.hexStringToBytes(info.getHeadPhoto());
			Bitmap output=BitmapFactory.decodeByteArray(photo, 0,photo.length);
//			iv_photo.setImageBitmap(output);
			iv_photo.setImageBitmap(SportData.toRoundBitmap(output));
			iv_photo.setVisibility(View.VISIBLE);
		}
		datePickerDialog = new DatePickerDialog(context,dateSet,year,month,day);	
	}

	private void ActivateInputStatus()
	{
//		tv_user.setText(getString(R.string.changeheadphoto));
		iv_save.setVisibility(View.VISIBLE);
	}
	private void Outload()
	{
		tv_user.setText(getString(R.string.unknow_str));
	    tv_load.setText(getString(R.string.userlogin));
	    tv_sex.setText("");
	    tv_birthday.setText("");
		et_height.setText("");
		et_weight.setText("");		
		tv_city.setText("");
		iv_photo.setVisibility(View.GONE);  
		iv_save.setVisibility(View.GONE);
		info.setHeadPhoto("");
		datePickerDialog = new DatePickerDialog(context,dateSet,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
	}
	
	private boolean checkInputValid()
	{
		String sex = tv_sex.getText().toString();
		String birthday = tv_birthday.getText().toString();
		String height = et_height.getText().toString();
		String weight = et_weight.getText().toString();
		String city = tv_city.getText().toString();	
		boolean retValue = false;
		Message msg = new Message();
		msg.what = -1;
	    if(sex.equals(""))
	    {
	    	msg.what = SportData.TOAST_SEX_NULL;
	    	tv_sex.requestFocus();
	    }
	    else if(birthday.equals(""))
	    {
	    	msg.what = SportData.TOAST_BIRTHDAY_NULL;	
	    	tv_birthday.requestFocus();
	    }
	    else if(height.equals(""))
	    {
	    	msg.what = SportData.TOAST_HEIGHT_NULL;	
	    	et_height.requestFocus();
	    }
	    else if(weight.equals(""))
	    {
	    	msg.what = SportData.TOAST_WEIGHT_NULL;	
	    	et_weight.requestFocus();
	    }
	    else if(city.equals(""))
	    {
	    	msg.what = SportData.TOAST_CITY_NULL;	
	    	tv_city.requestFocus();
	    }
	    else
	    {
	    	retValue = true;
	    }
	    if(msg.what!=-1)handle.sendMessage(msg); 
		return retValue;
	}
	
	
	private void showDialog(Context tcontext,final int icontype)
	{
		int arrayID = -1;
		int titleID = -1;
		if(icontype==SportData.ICON_SEX)
		{
			arrayID = R.array.array_sex;
			titleID = R.string.selectsex;
		}	
		else if(icontype==SportData.ICON_PHOTOSELECT)
		{
			arrayID = R.array.array_setphoto;
			titleID = R.string.selectheadphoto;	
		}	
		final String [] data = tcontext.getResources().getStringArray(arrayID);
		AlertDialog.Builder builder = new AlertDialog.Builder(tcontext);
		final Dialog ad = builder.create();
	    LayoutInflater inflater;
	    View layout=null;	  
        if (!ad.isShowing())
        {
                ad.show();
        	    WindowManager m = getWindowManager();  
        	    Display d = m.getDefaultDisplay(); // ��ȡ��Ļ�?��??  
        	    WindowManager.LayoutParams p = ad.getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���??   
        	    p.width = (int) (d.getWidth() * 0.8); // �������Ϊ��Ļ��0.8  
        	    ad.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL); 
        	    ad.getWindow().setAttributes(p);   
                inflater=  LayoutInflater.from(tcontext);
        		layout = inflater.inflate(R.layout.dialog, null);
                ad.getWindow().setContentView(layout);
                TextView tv_content = (TextView)layout.findViewById(R.id.tv_content);
                ListView list  = (ListView)layout.findViewById(R.id.list);
                tv_content.setText(tcontext.getString(titleID));
        		dateAdapter_dialog adapter = new dateAdapter_dialog(tcontext,data,icontype);
        		list.setAdapter(adapter);
        		list.setOnItemClickListener(new OnItemClickListener(){

        			@Override
        			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        					long arg3) {
        				// TODO Auto-generated method stub
        				switch(icontype)
        				{
        				case SportData.ICON_SEX:
            				tv_sex.setText(data[arg2]);	
        					break;
        				case SportData.ICON_PHOTOSELECT:
        					setHeadPhoto(arg2);
        					break;	
        				}

        				ad.dismiss();
        			}});
        }	
	}
	
	private void setHeadPhoto(int index)
	{
		Intent i = null;
	    switch(index)
	    {
	    case 0:
			i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, SportData.REQUESTCODE_PHOTOGRAPH);
	    	break;
	    case 1:
			i = new Intent(Intent.ACTION_PICK, null);
			i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					SportData.IMAGE_UNSPECIFIED);
			startActivityForResult(i, SportData.REQUESTCODE_PHOTOZOOM);
	    	break;
	    }
	}
	private BroadcastReceiver infoLoadReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
          int flag = intent.getIntExtra(SportData.LOAD, -1);
          Log.d(TAG, "flag==="+flag);
          if(SportData.LOADSTATUS_LOGIN==flag)
          {
        	 // initEditInfo();
        	  finish();
          }
         
	    }
	 };


public void startPhotoZoomNew(Bitmap data) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	//intent.setDataAndType(uri, SportData.IMAGE_UNSPECIFIED);  
	intent.setType("image/*");  
    intent.putExtra("data", data); 
	intent.putExtra("crop", "true");
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	intent.putExtra("outputX", 100);
	intent.putExtra("outputY", 100);
	intent.putExtra("return-data", true);
	intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	startActivityForResult(intent, SportData.REQUESTCODE_PHOTOSAVE);  
}

public void startPhotoZoomOld(Uri uri) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, SportData.IMAGE_UNSPECIFIED);  
	//intent.setType("image/*");  
   // intent.putExtra("data", data); 
	intent.putExtra("crop", "true");
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	intent.putExtra("outputX", 100);
	intent.putExtra("outputY", 100);
	intent.putExtra("return-data", true);
	intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	startActivityForResult(intent, SportData.REQUESTCODE_PHOTOSAVE);  
}

DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener()
{

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		if(year<1900||(year>=Calendar.getInstance().get(Calendar.YEAR)))
		{
			showToast.normaltoast(context, getString(R.string.birthdayiserror), showToast.TWO_SECOND);
		}
		else
		{
			DecimalFormat decimalFormat = new DecimalFormat("00");
			tv_birthday.setText(year + "-"
					+ decimalFormat.format(monthOfYear + 1) + "-"
					+ decimalFormat.format(dayOfMonth));
		}
	}};
}
