package com.projectx.itor;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
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
import com.projectx.api.ISipService;
import com.projectx.api.SipCallSession;
import com.projectx.api.SipManager;
import com.projectx.api.SipProfile;
import com.projectx.api.SipUri;
import com.projectx.ui.SipHome;
import com.projectx.ui.SipHome.ViewPagerVisibilityListener;
import com.projectx.ui.dialpad.DialerFragment;
import com.projectx.utils.CallHandlerPlugin;
import com.projectx.utils.DialingFeedback;
import com.projectx.utils.Log;
import com.projectx.utils.PreferencesWrapper;
import com.projectx.utils.CallHandlerPlugin.OnLoadListener;
import com.projectx.widgets.AccountChooserButton;
import com.projectx.widgets.DialerCallBar.OnDialActionListener;
import com.projectx.wizards.WizardIface;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class WebView1 extends SherlockFragment implements ViewPagerVisibilityListener, OnDialActionListener{
	
	public interface OnDialActionListener {
        /**
         * The make call button has been pressed
         */
        void placeCall();

        /**
         * The video button has been pressed
         */
        void placeVideoCall();
        /**
         * The delete button has been pressed
         */
        void deleteChar();
        /**
         * The delete button has been long pressed
         */
        void deleteAll();
    }
	
	private OnDialActionListener actionListener;
	
	public void setOnDialActionListener(OnDialActionListener l) {
        actionListener = l;
    }

	private WebView webView1;
	private final static String myBlogAddr = "http://119.46.211.105/app/SlideShow/slide.html";
	//private final static String myBlogAddr = "http://iptv.downloads-mediafire.com/android.php";
    private String myUrl;
    private ImageView imDial, imChat, imVideo;
    
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String KEY_DOMAIN = "domain";
    private static String KEY_PROXY = "proxy";
    private static String KEY_TCP = "tcp";
    
    private DialingFeedback dialFeedback;
    private PreferencesWrapper prefsWrapper;
    private AccountChooserButton accountChooserButton;
    private ISipService service;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            service = ISipService.Stub.asInterface(arg1);
            /*
             * timings.addSplit("Service connected"); if(configurationService !=
             * null) { timings.dumpToLog(); }
             */
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(SipManager.INTENT_SIP_SERVICE), connection,
                Context.BIND_AUTO_CREATE);
        // timings.addSplit("Bind asked for two");
        if (prefsWrapper == null) {
            prefsWrapper = new PreferencesWrapper(getActivity());
        }
        if (dialFeedback == null) {
            dialFeedback = new DialingFeedback(getActivity(), false);
        }

        dialFeedback.resume();
        
    }

    @Override
    public void onDetach() {
        try {
            getActivity().unbindService(connection);
        } catch (Exception e) {
            // Just ignore that
            Log.w("WebView1", "Unable to un bind", e);
        }
        dialFeedback.pause();
        super.onDetach();
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
        StrictMode.setThreadPolicy(policy); 
		View v = inflater.inflate(R.layout.web_view_1, container, false);
		webView1 = (WebView) v.findViewById(R.id.webView1);
		
		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView1.setWebViewClient(new MyWebViewClient());
		
        if(myUrl == null){
            myUrl = myBlogAddr;
        }
        webView1.loadUrl(myUrl);
        
        imDial = (ImageView) v.findViewById(R.id.imDial);
        imDial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "Dial!!", Toast.LENGTH_SHORT).show();
				/*SipProfile account = new SipProfile();
				ContentValues cv = new ContentValues();
                cv.put(SipProfile.FIELD_ACTIVE, true);
                getActivity().getContentResolver().update(ContentUris.withAppendedId(SipProfile.ACCOUNT_ID_URI_BASE, 1), cv, null, null);*/
                
				Intent i = new Intent(getActivity(), CallcenterList.class);
				startActivity(i);
                
				//placeCallWithOption(null);
                
				//DialerFragment dialerFragment = new DialerFragment();
				//dialerFragment.placeCall();
				
				/*String call = "1113";
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.extensionTemp(call);
 
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS); 
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            //DatabaseHandler db = new DatabaseHandler(getActivity());
                            JSONObject json_extension = json.getJSONObject("extension");
                            String username = json_extension.getString(KEY_USERNAME);
                            String password = json_extension.getString(KEY_PASSWORD);
                            String domain = json_extension.getString(KEY_DOMAIN);
                            String proxy = json_extension.getString(KEY_PROXY);
                            Boolean tcp = json_extension.getBoolean(KEY_TCP);
                            
                           
                            Toast.makeText(getActivity(), "Extension recieved!!", Toast.LENGTH_SHORT).show();

                        }else{
                        	Toast.makeText(getActivity(), json.getString(KEY_ERROR_MSG), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
			}
		});
		imVideo = (ImageView) v.findViewById(R.id.imVideo);
		imVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "Video!!", Toast.LENGTH_SHORT).show();
				Bundle b = new Bundle();
		        b.putBoolean(SipCallSession.OPT_CALL_VIDEO, true);
		        placeCallWithOption(b );
			}
		});
		imChat = (ImageView) v.findViewById(R.id.imChat);
		imChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "Chat!!", Toast.LENGTH_SHORT).show();
				SipProfile account = new SipProfile();
				ContentValues cv = new ContentValues();
                cv.put(SipProfile.FIELD_ACTIVE, ! account.active);
                getActivity().getContentResolver().update(ContentUris.withAppendedId(SipProfile.ACCOUNT_ID_URI_BASE, 1), cv, null, null);
			}
		});
        
		return v;
	}

	private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	myUrl = url;
            view.loadUrl(url);
            return true;
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
		}
        
    }
	
	@Override
	public void onVisibilityChanged(boolean visible) {
		
	}

	@Override
	public void placeCall() {
		
	}

	@Override
	public void placeVideoCall() {
		
	}

	@Override
	public void deleteChar() {
		
	}

	@Override
	public void deleteAll() {
		
	}
	
	public void placeCallWithOption(Bundle b) {
        if (service == null) {
            return;
        }
        String toCall = "1113";
        Long accountToUse = SipProfile.INVALID_ID;
        // Find account to use
        //accountChooserButton.setChangeable(false);
        /*try {
        	SipProfile acc = accountChooserButton.getSelectedAccount();
            if (acc != null) {
                accountToUse = acc.id;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        

        // -- MAKE THE CALL --//
        if (accountToUse >= 0) {
            // It is a SIP account, try to call service for that
            try {
                service.makeCallWithOptions(toCall, accountToUse.intValue(), b);
            } catch (RemoteException e) {
                Log.e("WebView1", "Service can't be called to make the call");
            }
        } else if (accountToUse != SipProfile.INVALID_ID) {
            // It's an external account, find correct external account
            CallHandlerPlugin ch = new CallHandlerPlugin(getActivity());
            ch.loadFrom(accountToUse, toCall, new OnLoadListener() {
                @Override
                public void onLoad(CallHandlerPlugin ch) {
                    placePluginCall(ch);
                }
            });
        }*/
        
        try {
			service.makeCallWithOptions(toCall, 3, b);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void placePluginCall(CallHandlerPlugin ch) {
        try {
            String nextExclude = ch.getNextExcludeTelNumber();
            if (service != null && nextExclude != null) {
                try {
                    service.ignoreNextOutgoingCallFor(nextExclude);
                } catch (RemoteException e) {
                    Log.e("WebView1", "Impossible to ignore next outgoing call", e);
                }
            }
            ch.getIntent().send();
        } catch (CanceledException e) {
            Log.e("WebView1", "Pending intent cancelled", e);
        }
    }
	
	
	/*private void addAccount(String user, String password, String domain, String proxy, Boolean tcp) {
		boolean needRestart = false;
		account.display_name = user;
 		account.acc_id = " <sip:" + user + "@" + domain + ">";
 		
 		account.reg_uri = "sip:" + domain;

 		account.realm = "*";
 		
        account.username = user;
        if (TextUtils.isEmpty(account.username)) {
            account.username = user;
        }
 		account.data = password;
 		account.scheme = SipProfile.CRED_SCHEME_DIGEST;
 		account.datatype = SipProfile.CRED_DATA_PLAIN_PASSWD;

 		if (tcp) {
 			account.transport = SipProfile.TRANSPORT_TCP;
			} else {
				account.transport = SipProfile.TRANSPORT_AUTO;
			}
 		if (proxy != null) {
 			account.proxies = new String[] { "sip:" + proxy };
 		} else {
 			account.proxies = null;
 		}
 		//account = wizard.buildAccount(account);
 		wizard.canSave();
 		needRestart = wizard.needRestart();
			if (needRestart) {
				Intent intent = new Intent(SipManager.ACTION_SIP_REQUEST_RESTART);
				getActivity().sendBroadcast(intent);
			}
	}
*/
}
