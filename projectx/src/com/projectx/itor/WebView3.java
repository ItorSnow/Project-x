package com.projectx.itor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.projectx.R;
import com.projectx.ui.SipHome.ViewPagerVisibilityListener;

public class WebView3 extends SherlockFragment implements ViewPagerVisibilityListener{

/*	private WebView webView2;
	private final static String myBlogAddr = "http://www.tgs-enterprise.com";
    private String myUrl;
    private ImageView imDial, imChat, imVideo;*/
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.web_view_3, container, false);
		
		return v;
	}
	
	@Override
	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}