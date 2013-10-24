package com.projectx.itor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.projectx.R;
import com.projectx.ui.SipHome.ViewPagerVisibilityListener;

public class WebView2 extends SherlockFragment implements ViewPagerVisibilityListener{

	private WebView webView2;
	private final static String myBlogAddr = "http://www.tgs-enterprise.com";
    private String myUrl;
    private ImageView imDial, imChat, imVideo;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.web_view_2, container, false);
		webView2 = (WebView) v.findViewById(R.id.webView2);
		
		WebSettings webSettings = webView2.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView2.setWebViewClient(new MyWebViewClient());
		
        if(myUrl == null){
            myUrl = myBlogAddr;
        }
        webView2.loadUrl(myUrl);
        
        /*imDial = (ImageView) v.findViewById(R.id.imDial2);
        imDial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Dial!!", Toast.LENGTH_SHORT).show();
			}
		});
		imVideo = (ImageView) v.findViewById(R.id.imVideo2);
		imVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Video!!", Toast.LENGTH_SHORT).show();
			}
		});
		imChat = (ImageView) v.findViewById(R.id.imChat2);
		imChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Chat!!", Toast.LENGTH_SHORT).show();
			}
		});*/
        
		return v;
	}

	private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            myUrl = url;
            view.loadUrl(url);
            return true;
        }
    }
	
	@Override
	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}