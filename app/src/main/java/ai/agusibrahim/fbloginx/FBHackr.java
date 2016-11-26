package ai.agusibrahim.fbloginx;

import android.content.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import android.graphics.*;
import android.preference.*;
import com.loopj.android.http.RequestParams;
import org.json.*;
import com.loopj.android.http.AsyncHttpClient;
import org.apache.http.*;
import com.loopj.android.http.*;

/*
	Original script by Agus Ibrahim
	http://fb.me/mynameisagoes
	USE WITH YOUR OWN RISK
*/
public class FBHackr
{
	Context mContext;
	SharedPreferences sett;
	public static int METHOD_GET = 1;
	public static int METHOD_POST = 2;
	public static int METHOD_DELETE = 3;
	AsyncHttpClient client;
	String GRAPH_URL="https://graph.facebook.com";
	public interface LoginListener{
		void onLoginSuccess(String token);
		void onLoginFailed();
		void onLoginCancel();
	}
	public interface RequestListener{
		void onSuccess(JSONObject data);
		void onFailure(String msg);
	}
	public interface ValidateListener{
		void onValidate(JSONObject data);
		void onReject();
	}
	public FBHackr(Context ctx){
		this.mContext=ctx;
		sett= ctx.getSharedPreferences("0x00123fbHackr", 1);
		client = new AsyncHttpClient();
	}
	public void Auth(final LoginListener listener){
		final Dialog dlg = new Dialog(mContext);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View v = inflater.inflate(R.layout.weblogin, null);
		final ImageView closebtn=(ImageView) v.findViewById(R.id.close_btn);
		closebtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					dlg.dismiss();
					listener.onLoginCancel();
				}
			});
		dlg.setCancelable(false);
		final WebView webview = (WebView) v.findViewById(R.id.web);
		webview.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					webview.setVisibility(View.GONE);
					LinearLayout ll=(LinearLayout) v.findViewById(R.id.loading);
					ll.setVisibility(View.VISIBLE);
					closebtn.setVisibility(View.GONE);
					super.onPageStarted(view, url, favicon);
				}
				@Override
				public void onPageFinished(WebView web, String url){
					web.setVisibility(View.VISIBLE);
					LinearLayout ll=(LinearLayout) v.findViewById(R.id.loading);
					ll.setVisibility(View.GONE);
					//Toast.makeText(mContext, "URL: "+url, Toast.LENGTH_LONG).show();
					if(web.getTitle().length()<1){
						dlg.dismiss();
						listener.onLoginFailed();
					}
					closebtn.setVisibility(View.VISIBLE);
					if(web.getTitle().contains("#session=")){
						dlg.hide();
						String token=web.getTitle().split("#session=")[1].split("&exp")[0];
						SharedPreferences.Editor edit= sett.edit();
						edit.putString("session", toHexString(token.getBytes()));
						edit.commit();
						listener.onLoginSuccess(token);
					}else if(url.contains("#access_token=")){
						dlg.hide();
						String token=url.split("#access_token=")[1].split("&exp")[0];
						SharedPreferences.Editor edit= sett.edit();
						edit.putString("session", toHexString(token.getBytes()));
						edit.commit();
						listener.onLoginSuccess(token);
					}
					super.onPageFinished(web, url);
				}
			});
		EditText edit = (EditText) v.findViewById(R.id.edit);
		edit.setFocusable(true);
		edit.requestFocus();
		String nokia="https://mobile.facebook.com/v2.0/dialog/oauth?redirect_uri=http://account.nokia.com/acct/account/_COPY_All_URL_ACCESS_TOKEN_FROM_ADDRESS_BAR_&scope=email,publish_actions,user_about_me,user_actions.books,user_actions.music,user_actions.news,user_actions.video,user_activities,user_birthday,user_education_history,user_events,user_games_activity,user_groups,user_hometown,user_interests,user_likes,user_location,user_notes,user_photos,user_questions,user_relationship_details,user_relationships,user_religion_politics,user_status,user_subscriptions,user_videos,user_website,user_work_history,friends_about_me,friends_actions.books,friends_actions.music,friends_actions.news,friends_actions.video,friends_activities,friends_birthday,friends_education_history,friends_events,friends_games_activity,friends_groups,friends_hometown,friends_interests,friends_likes,friends_location,friends_notes,friends_photos,friends_questions,friends_relationship_details,friends_relationships,friends_religion_politics,friends_status,friends_subscriptions,friends_videos,friends_website,friends_work_history,ads_management,create_event,create_note,export_stream,friends_online_presence,manage_friendlists,manage_notifications,manage_pages,photo_upload,publish_stream,read_friendlists,read_insights,read_mailbox,read_page_mailboxes,read_requests,read_stream,rsvp_event,share_item,sms,status_update,user_online_presence,video_upload,xmpp_login&response_type=token,code&client_id=200758583311692";
		String ig="https://m.facebook.com/v1.0/dialog/oauth?redirect_uri=https://www.instagram.com/accounts/signup/index/Copy_this_whole_url_www.facebook.com/&scope=email,publish_actions,user_about_me,user_actions.books,user_actions.music,user_actions.news,user_actions.video,user_activities,user_birthday,user_education_history,user_events,user_games_activity,user_groups,user_hometown,user_interests,user_likes,user_location,user_notes,user_photos,user_questions,user_relationship_details,user_relationships,user_religion_politics,user_status,user_subscriptions,user_videos,user_website,user_work_history,friends_about_me,friends_actions.books,friends_actions.music,friends_actions.news,friends_actions.video,friends_activities,friends_birthday,friends_education_history,friends_events,friends_games_activity,friends_groups,friends_hometown,friends_interests,friends_likes,friends_location,friends_notes,friends_photos,friends_questions,friends_relationship_details,friends_relationships,friends_religion_politics,friends_status,friends_subscriptions,friends_videos,friends_website,friends_work_history,ads_management,create_event,create_note,export_stream,friends_online_presence,manage_friendlists,manage_notifications,manage_pages,photo_upload,publish_stream,read_friendlists,read_insights,read_mailbox,read_page_mailboxes,read_requests,read_stream,rsvp_event,share_item,sms,status_update,user_online_presence,video_upload,xmpp_login&response_type=token+code&client_id=124024574287414";
		webview.loadUrl(nokia);
		dlg.setContentView(v);
		dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dlg.show();
	}
	public void logout(){
		SharedPreferences.Editor edit= sett.edit();
		edit.remove("session");
		edit.commit();
		new WebView(mContext).clearCache(true);
		/*
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "locale=");
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "datr=");
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "s=");
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "csm=");
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "fr=");
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "lu=");*/
		android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "c_user=");
		//android.webkit.CookieManager.getInstance().setCookie(".facebook.com", "xs=");
	}
	public void validate(final ValidateListener listener){
		if(getAccessToken()==null){
			listener.onReject();
			return;
		}
		api("/me?fields=name", null, METHOD_GET, new RequestListener(){
				@Override
				public void onSuccess(JSONObject data) {
					if(data.isNull("error")) listener.onValidate(data);
					else listener.onReject();
				}

				@Override
				public void onFailure(String msg) {
					listener.onReject();
				}
			});
	}
	public String getAccessToken(){
		String token=sett.getString("session", null);
		if(token!=null) token=fromHexString(token);
		return token;
	}
	public void api(String path, RequestParams param, int method, final RequestListener listener){
		String token=getAccessToken();
		if(token==null) return;
		if(param==null){
			param=new RequestParams();
		}
		param.put("access_token", token);
		if(!path.startsWith("/")) path="/"+path;
		JsonHttpResponseHandler handler= new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int p1, Header[] p2, String data, Throwable p4) {
				listener.onFailure(data);
			}
			@Override
			public void onSuccess(int p1, Header[] p2, JSONObject data) {
				listener.onSuccess(data);
			}
		};
		if(method==METHOD_GET) client.get(GRAPH_URL+path, param, handler);
		if(method==METHOD_POST) client.post(GRAPH_URL+path, param, handler);
		if(method==METHOD_DELETE) client.delete(GRAPH_URL+path, param, handler);
	}
	public static String toHexString(byte[] ba) {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < ba.length; i++)
			str.append(String.format("%x", ba[i]));
		return str.toString();
	}

	public static String fromHexString(String hex) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < hex.length(); i+=2) {
			str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		return str.toString();
	}
}
