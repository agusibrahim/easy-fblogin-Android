package ai.agusibrahim.fbloginx;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.*;
import com.loopj.android.image.SmartImageView;
import com.loopj.android.http.*;

public class MainActivity extends AppCompatActivity {
   Toolbar toolbar;
   EditText statusfield, apifield;
   Button postbtn, querybtn, loginbtn;
   TextView nama, output;
   SmartImageView pp;
   LinearLayout fbinteract;
   FBHackr fb;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main_activity);
       toolbar = (Toolbar) findViewById(R.id.toolbar);
	   statusfield=(EditText) findViewById(R.id.postfield);
	   apifield=(EditText) findViewById(R.id.apiquery);
	   postbtn=(Button) findViewById(R.id.postbtn);
	   querybtn=(Button) findViewById(R.id.getbtn);
	   loginbtn=(Button) findViewById(R.id.loginbtn);
	   nama=(TextView) findViewById(R.id.txtname);
	   output=(TextView) findViewById(R.id.output);
	   pp=(SmartImageView) findViewById(R.id.pp);
	   fbinteract=(LinearLayout) findViewById(R.id.fbinteract);
       setSupportActionBar(toolbar);
	   fb=new FBHackr(this);
	   
	   final String token=fb.getAccessToken();
	   if(token!=null){
		   loginbtn.setText("Logout");
	   }
	   fb.validate(new FBHackr.ValidateListener(){
			   @Override
			   public void onValidate(JSONObject data) {
				   fbinteract.setVisibility(View.VISIBLE);
				   try {
					   nama.setText(data.getString("name"));
					   pp.setImageUrl("https://graph.facebook.com/"+data.getString("id")+"/picture?type=large");
				   }
				   catch(Exception e) {}
			   }
			   @Override
			   public void onReject() {
				   // TODO: Implement this method
			   }
	   });
	   loginbtn.setOnClickListener(new View.OnClickListener(){
			   @Override
			   public void onClick(final View p1) {
				   if(token!=null){
					   fb.logout();
					   fbinteract.setVisibility(View.GONE);
					   loginbtn.setText("Login With Facebook");
					   return;
				   }
				   fb.Auth(new FBHackr.LoginListener(){
						   @Override
						   public void onLoginSuccess(String token) {
							   fbinteract.setVisibility(View.VISIBLE);
						   }

						   @Override
						   public void onLoginFailed() {
							   Toast.makeText(p1.getContext(), "Login gagal", Toast.LENGTH_LONG).show();
						   }

						   @Override
						   public void onLoginCancel() {
							   // TODO: Implement this method
						   }
					   });
			   }
		   });
	   postbtn.setOnClickListener(new View.OnClickListener(){
			   @Override
			   public void onClick(final View p1) {
				   String statustxt=statusfield.getText().toString();
				   if(statustxt.length()<1) return;
				   RequestParams param=new RequestParams();
				   param.put("message", statustxt);
				   fb.api("/me/feed", param, fb.METHOD_POST, new FBHackr.RequestListener(){

						   @Override
						   public void onSuccess(JSONObject data) {
							   output.setText("OK: "+data.toString());
						   }


						   @Override
						   public void onFailure(String msg) {
							   output.setText("Error: "+msg);
						   }
					   });
			   }
		   });
	   querybtn.setOnClickListener(new View.OnClickListener(){
			   @Override
			   public void onClick(View p1) {
				   String query=apifield.getText().toString();
				   if(query.length()<1) return;
				   fb.api(query, null, fb.METHOD_GET, new FBHackr.RequestListener(){
						   @Override
						   public void onSuccess(JSONObject data) {
							   output.setText(data.toString());
						   }

						   @Override
						   public void onFailure(String msg) {
							   output.setText("FAILED, "+msg);
						   }
					   });
			   }
		   });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	   getMenuInflater().inflate(R.menu.main_menu,menu);
	   return super.onCreateOptionsMenu(menu);
   }
   
}
