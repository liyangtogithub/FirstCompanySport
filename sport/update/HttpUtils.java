package com.desay.sport.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {
	private final static String TAG = "HttpUtils";
	/**
	* 获取网络文件
	* @param url
	* @return
	* @throws Exception
	* @{@link Deprecated}
	*/
	public static InputStream getContent(String url) throws Exception{
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
//	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpResponse response = client.execute(new HttpGet(url)); 
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
//		    Log.d(TAG, "1111111111");
	    	return entity.getContent();
	    }
//	    Log.d(TAG, "22222222222222");
	    return null;
	} 
	
	/**
	 * 获取网络资源 POST方式
	 * @param url 地址
	 * @param jsonstr json数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] getContentByPost(String url,String jsonstr,boolean iszip) throws Exception{
		byte[] data=null;
		HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
//	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpPost request = new HttpPost(url);
	    HttpEntity postentity;
		 // 绑定到请求 Entry
	    postentity = new StringEntity(jsonstr);
		request.setEntity(postentity);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if(entity!=null)
			data = EntityUtils.toByteArray(entity);
		return data;
	}
}
