package com.flyingh.newsmanager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText titleText;
	private EditText viewCountText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		titleText = (EditText) findViewById(R.id.title);
		viewCountText = (EditText) findViewById(R.id.viewCount);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
		}
	}

	public void save(View view) throws MalformedURLException, NotFoundException, IOException {
		String title = titleText.getText().toString();
		Integer viewCount = Integer.valueOf(viewCountText.getText().toString());
		if (saveByPost(title, viewCount)) {
			Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), R.string.failture, Toast.LENGTH_SHORT).show();
		}
	}

	private boolean saveByPost(String title, Integer viewCount) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL("http://10.1.79.29:8080/News/ManageNewsServlet").openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		String str = "title=" + title + "&viewCount=" + viewCount;
		conn.setRequestProperty("Content-Length", String.valueOf(str.getBytes().length));
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(str.getBytes());
		return conn.getResponseCode() == 200;
	}

	@SuppressWarnings("unused")
	private boolean saveByGet(String title, Integer viewCount) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(makePath(title, viewCount)).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		return conn.getResponseCode() == 200;
	}

	private String makePath(String title, Integer viewCount) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("http://10.1.79.29:8080/News/ManageNewsServlet").append("?title=").append(URLEncoder.encode(title, "utf-8")).append("&viewCount=")
				.append(viewCount);
		return sb.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
