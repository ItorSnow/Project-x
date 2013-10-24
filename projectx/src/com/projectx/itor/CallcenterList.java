package com.projectx.itor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.projectx.R;

public class CallcenterList extends ListActivity{

	// Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> inboxList;
 
    // products JSONArray
    JSONArray callList = null;
 
    // Inbox JSON url
    //private static final String INBOX_URL = "http://api.androidhive.info/mail/inbox.json";
     
    // ALL JSON node names
    private static final String TAG_CALLCENTER = "callcenter";
    private static final String TAG_CALLCENTERS = "callcenters";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_NUMBER = "number";
     
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callcenter_list);
         
        // Hashmap for ListView
        inboxList = new ArrayList<HashMap<String, String>>();
  
        // Loading INBOX in Background Thread
        new LoadInbox().execute();
    }
 
    /**
     * Background Async Task to Load all INBOX messages by making HTTP Request
     * */
    class LoadInbox extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CallcenterList.this);
            pDialog.setMessage("Loading List ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting Inbox JSON
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
             
            // getting JSON string from URL
            //JSONObject json = jsonParser.makeHttpRequest(INBOX_URL, "GET", params);
 
            
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.CallcenterList(TAG_CALLCENTER);
            
            // Check your log cat for JSON reponse
            Log.d("Inbox JSON: ", json.toString());
 
            try {
                callList = json.getJSONArray(TAG_CALLCENTERS);
                // looping through All messages
                for (int i = 0; i < callList.length(); i++) {
                    JSONObject c = callList.getJSONObject(i);
 
                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String description = c.getString(TAG_DESCRIPTION);
                    String number = c.getString(TAG_NUMBER);
                    String image = c.getString(TAG_IMAGE);
 
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
 
                    // adding each child node to HashMap key => value
                    map.put(TAG_ID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_DESCRIPTION, description);
                    map.put(TAG_NUMBER, number);
                    map.put(TAG_IMAGE, image);
 
                    // adding HashList to ArrayList
                    inboxList.add(map);
                }
 
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            CallcenterList.this, inboxList,
                            R.layout.callcenter_list_item, new String[] { TAG_NAME, TAG_DESCRIPTION ,TAG_IMAGE},
                            new int[] { R.id.ColName, R.id.ColDescription, R.id.list_image });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}
