package com.example.anshit.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayFilledSurvey extends ActionBarActivity{

    String sno;
    int optionIdCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        sno = intent.getExtras().getString("surveyno");

        surveyList = new ArrayList<HashMap<String, String>>();

        optionIdCounter=1;

        new LoadSurvey().execute();
    }

    String surveyno, qno, questiontitle, type, noofoptions, options, image, help;

    private static String url_survey = Login.ip_address+"get_survey.php";
    private static String url_filled_survey = Login.ip_address+"get_filled_survey.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SURVEY = "survey";
    private static final String TAG_SURVEYNO = "surveyno";
    private static final String TAG_QNO = "qno";
    private static final String TAG_QUESTIONTITLE = "questiontitle";
    private static final String TAG_HELP = "help";
    private static final String TAG_TYPE = "type";
    private static final String TAG_NOOFOPTIONS = "noofoptions";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_OPTIONS = "options";

    // products JSONArray
    JSONArray surveys = null, surveys2 = null;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> surveyList;

    class LoadSurvey extends AsyncTask<String, String, String> {

        String str,error;
        private ProgressDialog pDialog;
        boolean surveyFoundFlag=true,errorFlag=false;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.displaysurveyid);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayFilledSurvey.this);
            pDialog.setMessage("Loading Survey. Please wait...");
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
            params.add(new BasicNameValuePair("surveyno", sno));
            params.add(new BasicNameValuePair("emailid", Login.emailid));

            JSONObject json = jParser.makeHttpRequest(url_survey, "GET", params);
            JSONObject json2 = jParser.makeHttpRequest(url_filled_survey, "GET", params);

            // Check your log cat for JSON reponse
            //Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json2.getInt(TAG_SUCCESS);
                if (success == 1) {

                    // products found
                    // Getting Array of Products

                    surveys = json.getJSONArray(TAG_SURVEY);
                    surveys2 = json2.getJSONArray(TAG_SURVEY);

                    // looping through All Products
                    for (int i = 0; i < surveys.length(); i++) {
                        JSONObject c = surveys.getJSONObject(i);
                        JSONObject c2 = surveys2.getJSONObject(i);
                        // Storing each json item in variable
                        surveyno = c.getString(TAG_SURVEYNO);
                        qno = c.getString(TAG_QNO);
                        questiontitle = c.getString(TAG_QUESTIONTITLE);
                        help = c.getString(TAG_HELP);
                        type = c.getString(TAG_TYPE);
                        noofoptions = c.getString(TAG_NOOFOPTIONS);
                        image = c.getString(TAG_IMAGE);
                        options = c2.getString(TAG_OPTIONS);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SURVEYNO, surveyno);
                        map.put(TAG_QNO, qno);
                        map.put(TAG_QUESTIONTITLE, questiontitle);
                        map.put(TAG_HELP, help);
                        map.put(TAG_TYPE, type);
                        map.put(TAG_NOOFOPTIONS, noofoptions);
                        map.put(TAG_OPTIONS, options);
                        map.put(TAG_IMAGE, image);
                        // adding HashList to ArrayList
                        surveyList.add(map);

                    }
                } else {
                    // no products found
                    surveyFoundFlag=false;
                }
            } catch (JSONException e) {
                error=e.toString();
                errorFlag=true;
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            optionIdCounter=1;

            final LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,15,0,15);

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if(errorFlag){
                        AlertDialog alertDialog = new AlertDialog.Builder(DisplayFilledSurvey.this).create();
                        alertDialog.setTitle("Sign Up Failed");
                        alertDialog.setMessage(error);//"Already registered with this email address.");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        if (surveyFoundFlag) {
                            for (int i = 0; i < surveyList.size(); i++) {
                                // Storing each json item in variable
                                surveyno = surveyList.get(i).get(TAG_SURVEYNO);
                                qno = surveyList.get(i).get(TAG_QNO);
                                questiontitle = surveyList.get(i).get(TAG_QUESTIONTITLE);
                                help = surveyList.get(i).get(TAG_HELP);
                                type = surveyList.get(i).get(TAG_TYPE);
                                noofoptions = surveyList.get(i).get(TAG_NOOFOPTIONS);
                                options = surveyList.get(i).get(TAG_OPTIONS);
                                image = surveyList.get(i).get(TAG_IMAGE);
                                int noOfOptions = Integer.valueOf(noofoptions);

                                TextView textView = new TextView(DisplayFilledSurvey.this);
                                textView.setTextSize(20);
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setLayoutParams(layoutParams);
                                textView.setTextColor(Color.parseColor("#333333"));

                                textView.setText(qno+". "+questiontitle);
                                linearLayout.addView(textView);

                                if(help.length()>0) {
                                    textView = new TextView(DisplayFilledSurvey.this);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#bbbbbb"));

                                    textView.setText(help);
                                    linearLayout.addView(textView);
                                }

                                String imagebase64 = image.substring(image.indexOf(",") + 1);
                                byte[] byteArray = Base64.decode(imagebase64, Base64.DEFAULT);
                                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                ImageView imageView = new ImageView(DisplayFilledSurvey.this);
                                imageView.setImageBitmap(image);
                                linearLayout.addView(imageView);

                                if (type.equalsIgnoreCase("checkbox")) {
                                    options = options.replaceAll("#", ", ");
                                    textView = new TextView(DisplayFilledSurvey.this);
                                    textView.setTextSize(17);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#333333"));
                                    textView.setText("Answer: " + options);
                                    linearLayout.addView(textView);
                                } else if (type.equalsIgnoreCase("othercheckbox")) {
                                    options = options.replaceAll("#", ", ");
                                    textView = new TextView(DisplayFilledSurvey.this);
                                    textView.setTextSize(17);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#333333"));
                                    textView.setText("Answer: " + options);
                                    linearLayout.addView(textView);
                                } else if (type.equalsIgnoreCase("otherradio")) {
                                    options = options.replaceAll("#", ", ");
                                    textView = new TextView(DisplayFilledSurvey.this);
                                    textView.setTextSize(17);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#333333"));
                                    textView.setText("Answer: " + options);
                                    linearLayout.addView(textView);
                                } else {
                                    textView = new TextView(DisplayFilledSurvey.this);
                                    textView.setTextSize(17);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#333333"));
                                    textView.setText("Answer: " + options);
                                    linearLayout.addView(textView);
                                }
                            }
                        } else {
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 30, 0, 0);
                            TextView textView = new TextView(DisplayFilledSurvey.this);
                            textView.setLayoutParams(layoutParams);
                            textView.setTextSize(20);
                            textView.setTextColor(Color.parseColor("#333333"));
                            textView.setGravity(Gravity.CENTER);
                            textView.setText("No Response Found");
                            linearLayout.addView(textView);
                        }
                    }
                }
            });
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }
    }

}
