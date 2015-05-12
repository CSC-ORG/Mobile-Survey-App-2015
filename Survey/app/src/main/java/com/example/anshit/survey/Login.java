package com.example.anshit.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends ActionBarActivity implements View.OnClickListener{

    static final String ip_address = "http://surveyapp.netai.net/";
    //static final String ip_address = "http://172.16.101.219/PHP/Surveylocal/";
    public static final String PREFS_NAME = "LoginFile";
    private static String url_login = ip_address+"login.php";
    public static String emailid,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        String id=settings.getString("id","none");
        if(!id.equalsIgnoreCase("none"))
        {
            emailid=id;
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button button1=(Button)findViewById(R.id.loginbutton);
        Button button2=(Button)findViewById(R.id.signupbutton1);
        button1.setOnClickListener(Login.this);
        button2.setOnClickListener(Login.this);
    }

    @Override
    public void onClick(View v) {
        EditText editText;
        switch (v.getId())
        {
            case R.id.signupbutton1:
                Intent intent=new Intent(this, Signup.class);
                startActivity(intent);
                finish();
                break;
            case R.id.loginbutton:
                editText = (EditText) findViewById(R.id.loginidtext);
                emailid = editText.getText().toString();
                editText = (EditText) findViewById(R.id.loginpasswordtext);
                password = editText.getText().toString();
                new login().execute();
                break;
        }
    }

    JSONParser jParser = new JSONParser();

    class login extends AsyncTask<String, String, String> {

        String error;
        private ProgressDialog pDialog;
        boolean loginFlag,errorFlag=false;

        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.loginactivityid);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Checking details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            params.add(new BasicNameValuePair("emailid", emailid));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);

            try {
                int success = json.getInt("success");
                if (success == 1) {
                        loginFlag=true;
                } else {
                    loginFlag=false;
                }
            } catch (JSONException e) {
                error=e.toString();
                errorFlag=true;
            }
            return null;
        }


        protected void onPostExecute(String file_url) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if(errorFlag){
                        AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                        alertDialog.setTitle("Sign Up Failed");
                        alertDialog.setMessage(error);//"Already registered with this email address.");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        if (loginFlag) {
                            SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("id", emailid);
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                            alertDialog.setTitle("Login Failed");
                            alertDialog.setMessage("Email/Password do not match");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                        }
                    }
                }
            });
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }

}
