package com.example.anshit.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karan on 04-05-2015.
 */
public class Signup extends ActionBarActivity implements View.OnClickListener{

    public static final String PREFS_NAME = "LoginFile";
    private static String url_signup = Login.ip_address+"signupapp.php";
    public static String emailid, password, name, age, gender, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button button=(Button)findViewById(R.id.signupbutton2);
        button.setOnClickListener(Signup.this);
    }

    public void onClick(View v) {
        EditText editText = (EditText) findViewById(R.id.signupidtext);
        emailid = editText.getText().toString();
        editText = (EditText) findViewById(R.id.signuppasswordtext);
        password = editText.getText().toString();
        editText = (EditText) findViewById(R.id.nametext);
        name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.agetext);
        age = editText.getText().toString();
        editText = (EditText) findViewById(R.id.gendertext);
        gender = editText.getText().toString();
        if(gender.charAt(0)=='M'||gender.charAt(0)=='m')
            gender="M";
        else
            gender="F";
        editText = (EditText) findViewById(R.id.citytext);
        city = editText.getText().toString();

        new signup().execute();
    }

    JSONParser jParser = new JSONParser();

    class signup extends AsyncTask<String, String, String> {

        String error;
        private ProgressDialog pDialog;
        boolean signupFlag,errorFlag=false;

        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.signupactvityid);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signup.this);
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
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("age", age));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("city", city));

            JSONObject json = jParser.makeHttpRequest(url_signup, "GET", params);
            try {
                int success=json.getInt("success");
                if(success==1){
                    signupFlag=true;
                }
                else if(success==2){
                    error="Already registered with this email address";
                    signupFlag=false;
                }
                else{
                    signupFlag=false;
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
                        AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                        alertDialog.setTitle("Sign Up Failed");
                        alertDialog.setMessage(error);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        if (signupFlag) {
                            SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("id", emailid);
                            Login.emailid = emailid;
                            editor.commit();
                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                            alertDialog.setTitle("Sign Up Failed");
                            alertDialog.setMessage(error);
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
