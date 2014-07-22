package controllers;

import play.*;
import play.mvc.*;

import java.io.IOException;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import models.*;

public class Application extends Controller {
	private static final String baseurl = "http://app.simsimi.com/app/aicr/request.p";
    private static final ObjectMapper mapper = new ObjectMapper();
	public static void index() {
        render();
    }
    
    public static void talk(String send){
    	HttpClient client = new DefaultHttpClient();
    	String body = "";
    	try {
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("av", "6.2.9");
    		map.put("ft", "0.0");
    		map.put("lc", "ch");
    		map.put("os", "i");
    		map.put("req", send);
    		map.put("tz", "Asia%2FShanghai");
    		map.put("uid", "61977624");
    		String url = addParams(baseurl, map);
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = client.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			body = EntityUtils.toString(entity);
			String str = mapper.readTree(body).findValue("sentence_resp").asText();
			
			String videoUrl = "http://translate.google.cn/translate_tts?ie=UTF-8&q=" + str + "&tl=zh-CN&total=2&idx=1&textlen=34&client=t";
			
			
			renderJSON("{\"answer\": \""+ str +"\", \"videoUrl\":\""+ videoUrl +"\"}");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static String addParams(String url, Map<String, String> paras){
    	Set<Map.Entry<String, String>> set = paras.entrySet();
    	if(!url.contains("?")){
			url = url + "?";
		}else{
			if(url.endsWith("&")){
				url = url.substring(0, url.length() - 1);
			}
		}
    	for(Map.Entry<String, String> entry : set){
    		url = url + "&" + entry.getKey() + "=" + entry.getValue();
    	}
    	return url;
    }
    
    public static void main(String[] args) {
    	for(int i = 0 ; i < 10; i++){
    		talk("");
    	}
	}
}