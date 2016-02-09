package com.nl.lotterynl.net;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;

public class HttpClientUtil {
	
	private HttpClient client;
	private HttpPost post;
	private HttpGet get;
	
	
	public HttpClientUtil(){
		client = new DefaultHttpClient();
		//判断是否需要设置代理信息
		if(StringUtils.isNotBlank(GlobalParams.PROXY)){
			//设置代理信息
			HttpHost host = new HttpHost(GlobalParams.PROXY, GlobalParams.PORT);
			client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, host);
			
		}
	}
	/**
	 * 向指定的链接发送xml文件
	 * @param uri
	 * @param xml
	 * @return 
	 */
	public InputStream sendXml(String uri, String xml){
		post = new HttpPost(uri);
		try {
			StringEntity entity = new StringEntity(xml, ConstantValues.ENCODING);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode()==200){
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
