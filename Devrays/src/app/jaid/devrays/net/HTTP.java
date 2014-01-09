package app.jaid.devrays.net;

import java.util.HashMap;

import app.jaid.devrays.debug.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;

public class HTTP {

	public static void request(String url, HttpResponseListener listener, String... params)
	{
		Log.m("Sending HTTP Request with " + params.length + " parameter" + (params.length == 1 ? "" : "s") + " to " + url);

		HttpRequest post = new HttpRequest(HttpMethods.POST);
		post.setUrl(url);
		post.setTimeOut(3000);

		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i != params.length; i++)
			map.put(String.valueOf(i), params[i]);

		post.setContent(HttpParametersUtils.convertHttpParameters(map));

		Gdx.net.sendHttpRequest(post, listener);
	}
}
