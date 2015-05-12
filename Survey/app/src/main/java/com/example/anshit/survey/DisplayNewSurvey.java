package com.example.anshit.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayNewSurvey extends ActionBarActivity implements OnClickListener{

    String sno,surveynos,stitle,object,answer;
    int optionIdCounter,arraylength;
    public static final String SAVED_SURVEYS = "SavedSurveys";
    JSONObject jsonObject;
    JSONArray jsonArray;
    boolean saved,found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        sno = intent.getExtras().getString("surveyno");
        stitle = intent.getExtras().getString("surveytitle");
        object=intent.getExtras().getString("object");

        if(!object.equalsIgnoreCase("none")) {
            saved=true;
            try {
                jsonObject = new JSONObject(object);
                jsonArray = jsonObject.getJSONArray("objects");
                arraylength = jsonObject.getInt("noofobjects");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        else {
            saved=false;
        }
        surveyList = new ArrayList<HashMap<String, String>>();

        optionIdCounter=1;

        new LoadSurvey().execute();
    }

    String surveyno, qno, questiontitle, type, noofoptions, options, image, help;

    private static String url_insert = Login.ip_address+"insert.php";
    private static String url_survey = Login.ip_address+"get_survey.php";

    @Override
    public void onClick(View v){

        Button button=(Button)findViewById(v.getId());
        String buttontext= (String) button.getText();
        if(buttontext.equalsIgnoreCase("Submit")){
            new SubmitSurvey().execute();
        }
        else{
            optionIdCounter=1;

            JSONArray jsonArray = new JSONArray();
            int noofobjects=0;
            for (int i = 0; i < surveyList.size(); i++) {

                JSONObject jsonObject = new JSONObject();
                String selectedoptions = "";

                surveyno = surveyList.get(i).get(TAG_SURVEYNO);
                qno = surveyList.get(i).get(TAG_QNO);
                type = surveyList.get(i).get(TAG_TYPE);
                noofoptions = surveyList.get(i).get(TAG_NOOFOPTIONS);
                int noOfOptions = Integer.valueOf(noofoptions);

                if (type.equalsIgnoreCase("checkbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                } else if (type.equalsIgnoreCase("radio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    if(radioGroup.getCheckedRadioButtonId()!=-1)
                        selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                } else if (type.equalsIgnoreCase("othercheckbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    if (selectedoptions.length() == 0)
                        selectedoptions = editText.getText().toString();
                    else {
                        selectedoptions += "#";
                        selectedoptions += editText.getText().toString();
                    }
                } else if (type.equalsIgnoreCase("otherradio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions += "#";
                    selectedoptions+= editText.getText().toString();
                } else {
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions = editText.getText().toString();
                }
                if(selectedoptions.length()>0){
                    try {
                        jsonObject.put("qno",qno);
                        jsonObject.put("answer",selectedoptions);
                        noofobjects++;
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Could not save survey");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
            JSONObject jsonObject2=new JSONObject();
            try {
                jsonObject2.put("noofobjects",noofobjects);
                jsonObject2.put("objects",jsonArray);
                SharedPreferences settings = getSharedPreferences(SAVED_SURVEYS, 0);
                SharedPreferences.Editor editor = settings.edit();
                surveynos=settings.getString("surveys","");
                surveyno = surveyList.get(0).get(TAG_SURVEYNO);
                if(surveynos.indexOf(surveyno+",")==-1)
                    surveynos=surveynos.concat(surveyno+",");
                editor.putString("surveys",surveynos);
                editor.putString("object"+surveyno,jsonObject2.toString());
                editor.putString("surveytitle"+surveyno,stitle);
                editor.commit();
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                alertDialog.setTitle("Congratulations");
                alertDialog.setMessage("Survey saved");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            } catch (JSONException e) {
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Could not save survey");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }

        }

    }

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SURVEY = "survey";
    private static final String TAG_SURVEYNO = "surveyno";
    private static final String TAG_QNO = "qno";
    private static final String TAG_QUESTIONTITLE = "questiontitle";
    private static final String TAG_HELP = "help";
    private static final String TAG_TYPE = "type";
    private static final String TAG_NOOFOPTIONS = "noofoptions";
    private static final String TAG_OPTIONS = "options";
    private static final String TAG_IMAGE= "image";

    // products JSONArray
    JSONArray surveys = null;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> surveyList;

    class LoadSurvey extends AsyncTask<String, String, String> {

        String str,error;
        private ProgressDialog pDialog;
        boolean questionsFoundFlag=true,errorFlag=false;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.displaysurveyid);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayNewSurvey.this);
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

            JSONObject json = jParser.makeHttpRequest(url_survey, "GET", params);

            // Check your log cat for JSON reponse
            //Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    // products found
                    // Getting Array of Products

                    surveys = json.getJSONArray(TAG_SURVEY);

                    // looping through All Products
                    for (int i = 0; i < surveys.length(); i++) {
                        JSONObject c = surveys.getJSONObject(i);
                        // Storing each json item in variable
                        surveyno = c.getString(TAG_SURVEYNO);
                        qno = c.getString(TAG_QNO);
                        questiontitle = c.getString(TAG_QUESTIONTITLE);
                        help = c.getString(TAG_HELP);
                        type = c.getString(TAG_TYPE);
                        noofoptions = c.getString(TAG_NOOFOPTIONS);
                        options = c.getString(TAG_OPTIONS);
                        image = c.getString(TAG_IMAGE);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SURVEYNO, surveyno);
                        map.put(TAG_QNO, qno);
                        map.put(TAG_QUESTIONTITLE, questiontitle);
                        map.put(TAG_HELP,help);
                        map.put(TAG_TYPE, type);
                        map.put(TAG_NOOFOPTIONS, noofoptions);
                        map.put(TAG_OPTIONS, options);
                        map.put(TAG_IMAGE,image);
                        // adding HashList to ArrayList
                        surveyList.add(map);

                    }
                } else {
                    // no products found
                    questionsFoundFlag=false;
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
                        AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Could not load survey.\n"+error);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which){}
                        });
                        alertDialog.show();
                    }
                    else {
                        if (questionsFoundFlag) {

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

                                TextView textView = new TextView(DisplayNewSurvey.this);
                                textView.setTextSize(20);
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setLayoutParams(layoutParams);
                                textView.setTextColor(Color.parseColor("#333333"));

                                textView.setText(qno + ". " + questiontitle);
                                linearLayout.addView(textView);

                                if(help.length()>0) {
                                    textView = new TextView(DisplayNewSurvey.this);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.parseColor("#bbbbbb"));
                                    textView.setText(help);
                                    linearLayout.addView(textView);
                                }

                                String imagebase64 = new String(image.substring(image.indexOf(",") + 1));
                                byte[] byteArray = Base64.decode(imagebase64, Base64.DEFAULT);
                                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                ImageView imageView = new ImageView(DisplayNewSurvey.this);
                                imageView.setImageBitmap(image);
                                linearLayout.addView(imageView);

                                if(saved){
                                    found=false;
                                    for(int j=0;j<jsonArray.length();j++){
                                        try {
                                            JSONObject temp=jsonArray.getJSONObject(j);
                                            if(temp.getString("qno").equalsIgnoreCase(qno)){
                                                answer=temp.getString("answer");
                                                found=true;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if (type.equalsIgnoreCase("checkbox")) {
                                    for (int j = 1; j <= noOfOptions; j++) {
                                        CheckBox checkBox = new CheckBox(DisplayNewSurvey.this);
                                        String option;
                                        int indexOfHash = options.indexOf('#');
                                        if (indexOfHash != -1) {
                                            option = options.substring(0, indexOfHash);
                                            options = options.substring(indexOfHash + 1);
                                        } else {
                                            option = options;
                                        }
                                        checkBox.setLayoutParams(layoutParams);
                                        checkBox.setId(optionIdCounter++);
                                        checkBox.setText(option);

                                        if(found) {
                                            String tempoption = answer;
                                            String checkoption;
                                            while (tempoption.indexOf("#") != -1) {
                                                checkoption = tempoption.substring(0, tempoption.indexOf("#"));
                                                tempoption = tempoption.substring(tempoption.indexOf("#") + 1);
                                                if (checkoption.equalsIgnoreCase(option)) {
                                                    checkBox.setChecked(true);
                                                }
                                            }
                                            checkoption = tempoption;
                                            if (checkoption.equalsIgnoreCase(option)) {
                                                checkBox.setChecked(true);
                                            }
                                        }

                                        linearLayout.addView(checkBox);
                                    }
                                } else if (type.equalsIgnoreCase("radio")) {
                                    RadioGroup radioGroup = new RadioGroup(DisplayNewSurvey.this);
                                    radioGroup.setId(optionIdCounter++);
                                    radioGroup.setLayoutParams(layoutParams);
                                    for (int j = 1; j <= noOfOptions; j++) {
                                        RadioButton radioButton = new RadioButton(DisplayNewSurvey.this);
                                        radioButton.setId(optionIdCounter++);
                                        String option;
                                        int indexOfHash = options.indexOf('#');
                                        if (indexOfHash != -1) {
                                            option = options.substring(0, indexOfHash);
                                            options = options.substring(indexOfHash + 1);
                                        } else {
                                            option = options;
                                        }
                                        radioButton.setText(option);
                                        radioGroup.addView(radioButton);

                                        if(found) {
                                            if(answer.equalsIgnoreCase(option)){
                                                radioButton.setChecked(true);
                                            }
                                        }

                                    }
                                    linearLayout.addView(radioGroup);
                                } else if (type.equalsIgnoreCase("othercheckbox")) {
                                    boolean noother=false;
                                    for (int j = 1; j <= noOfOptions; j++) {
                                        CheckBox checkBox = new CheckBox(DisplayNewSurvey.this);
                                        String option;
                                        int indexOfHash = options.indexOf('#');
                                        if (indexOfHash != -1) {
                                            option = options.substring(0, indexOfHash);
                                            options = options.substring(indexOfHash + 1);
                                        } else {
                                            option = options;
                                        }
                                        checkBox.setLayoutParams(layoutParams);
                                        checkBox.setId(optionIdCounter++);
                                        checkBox.setText(option);
                                        if(found) {
                                            String tempoption = answer;
                                            String checkoption;
                                            while (tempoption.indexOf("#") != -1) {
                                                checkoption = tempoption.substring(0, tempoption.indexOf("#"));
                                                tempoption = tempoption.substring(tempoption.indexOf("#") + 1);
                                                if (checkoption.equalsIgnoreCase(option)) {
                                                    checkBox.setChecked(true);
                                                }
                                            }
                                            checkoption = tempoption;
                                            if (checkoption.equalsIgnoreCase(option)) {
                                                checkBox.setChecked(true);
                                                noother=true;
                                            }
                                        }
                                        linearLayout.addView(checkBox);
                                    }
                                    EditText editText = new EditText(DisplayNewSurvey.this);
                                    editText.setId(optionIdCounter++);
                                    editText.setHint("Other option");
                                    editText.setLayoutParams(layoutParams);
                                    if(found && !noother){
                                        String tempoption = answer;
                                        String checkoption;
                                        while (tempoption.indexOf("#") != -1) {
                                            checkoption = tempoption.substring(0, tempoption.indexOf("#"));
                                            tempoption = tempoption.substring(tempoption.indexOf("#") + 1);

                                        }
                                        checkoption = tempoption;
                                        editText.setText(checkoption);
                                    }
                                    linearLayout.addView(editText);
                                } else if (type.equalsIgnoreCase("otherradio")) {
                                    boolean noother=false;
                                    RadioGroup radioGroup = new RadioGroup(DisplayNewSurvey.this);
                                    radioGroup.setId(optionIdCounter++);
                                    radioGroup.setLayoutParams(layoutParams);
                                    for (int j = 1; j <= noOfOptions; j++) {
                                        RadioButton radioButton = new RadioButton(DisplayNewSurvey.this);
                                        radioButton.setId(optionIdCounter++);
                                        String option;
                                        int indexOfHash = options.indexOf('#');
                                        if (indexOfHash != -1) {
                                            option = options.substring(0, indexOfHash);
                                            options = options.substring(indexOfHash + 1);
                                        } else {
                                            option = options;
                                        }
                                        radioButton.setText(option);

                                        if(found) {
                                            String tempoption = answer;
                                            String checkoption;
                                            while (tempoption.indexOf("#") != -1) {
                                                checkoption = tempoption.substring(0, tempoption.indexOf("#"));
                                                tempoption = tempoption.substring(tempoption.indexOf("#") + 1);
                                                if (checkoption.equalsIgnoreCase(option)) {
                                                    radioButton.setChecked(true);
                                                }
                                            }
                                            checkoption = tempoption;
                                            if (checkoption.equalsIgnoreCase(option)) {
                                                radioButton.setChecked(true);
                                                noother=true;
                                            }
                                        }

                                        radioGroup.addView(radioButton);
                                    }
                                    linearLayout.addView(radioGroup);
                                    EditText editText = new EditText(DisplayNewSurvey.this);
                                    editText.setId(optionIdCounter++);
                                    editText.setHint("Other option");
                                    editText.setLayoutParams(layoutParams);
                                    if(found && !noother){
                                        String tempoption = answer;
                                        String checkoption;
                                        while (tempoption.indexOf("#") != -1) {
                                            checkoption = tempoption.substring(0, tempoption.indexOf("#"));
                                            tempoption = tempoption.substring(tempoption.indexOf("#") + 1);
                                        }
                                        checkoption = tempoption;
                                        editText.setText(checkoption);
                                    }
                                    linearLayout.addView(editText);
                                } else {
                                    EditText editText = new EditText(DisplayNewSurvey.this);
                                    editText.setId(optionIdCounter++);
                                    editText.setHint("Answer here");
                                    editText.setLayoutParams(layoutParams);
                                    if(found) {
                                        editText.setText(answer);
                                    }
                                    linearLayout.addView(editText);
                                }
                            }
                            if (surveyList.size() > 0) {
                                Button button = new Button(DisplayNewSurvey.this);
                                button.setId(optionIdCounter++);
                                button.setText("Submit");
                                button.setOnClickListener(DisplayNewSurvey.this);
                                button.setLayoutParams(layoutParams);
                                button.setTextColor(Color.parseColor("#fafafa"));
                                button.setBackgroundResource(R.drawable.button_background);
                                linearLayout.addView(button);
                                button = new Button(DisplayNewSurvey.this);
                                button.setId(optionIdCounter++);
                                button.setText("Save");
                                button.setOnClickListener(DisplayNewSurvey.this);
                                button.setLayoutParams(layoutParams);
                                button.setTextColor(Color.parseColor("#fafafa"));
                                button.setBackgroundResource(R.drawable.button_background);
                                linearLayout.addView(button);
                            }
                        } else {
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 30, 0, 0);
                            TextView textView = new TextView(DisplayNewSurvey.this);
                            textView.setLayoutParams(layoutParams);
                            textView.setTextSize(20);
                            textView.setTextColor(Color.parseColor("#333333"));
                            textView.setGravity(Gravity.CENTER);
                            textView.setText("No Question Found");
                            linearLayout.addView(textView);
                        }
                    }
                }
            });
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }
    }

    /**
     * Background Async Task to Create new product
     * */
    class SubmitSurvey extends AsyncTask<String, String, String> {
        String error;
        boolean errorFlag=false;
        private ProgressDialog pDialog;
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.displaysurveyid);

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayNewSurvey.this);
            pDialog.setMessage("Submitting response");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        boolean filledFlag=true;
        protected String doInBackground(String... args) {
            optionIdCounter=1;
            for (int i = 0; i < surveyList.size(); i++) {

                String selectedoptions = "";

                surveyno = surveyList.get(i).get(TAG_SURVEYNO);
                qno = surveyList.get(i).get(TAG_QNO);
                type = surveyList.get(i).get(TAG_TYPE);
                noofoptions = surveyList.get(i).get(TAG_NOOFOPTIONS);
                int noOfOptions = Integer.valueOf(noofoptions);

                if (type.equalsIgnoreCase("checkbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                    if(selectedoptions.length()==0){
                        filledFlag=false;
                        return null;
                    }
                } else if (type.equalsIgnoreCase("radio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    if(radioGroup.getCheckedRadioButtonId()==-1){
                        filledFlag=false;
                        return null;
                    }
                    selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                } else if (type.equalsIgnoreCase("othercheckbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    if (selectedoptions.length() == 0)
                        selectedoptions = editText.getText().toString();
                    else {
                        selectedoptions += "#";
                        selectedoptions += editText.getText().toString();
                    }
                    if(selectedoptions.length()==0){
                        filledFlag=false;
                        return null;
                    }
                } else if (type.equalsIgnoreCase("otherradio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    if(radioGroup.getCheckedRadioButtonId()!=-1)
                        selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions += "#";
                    selectedoptions+= editText.getText().toString();
                    if(selectedoptions.length()==0){
                        filledFlag=false;
                        return null;
                    }
                } else {
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions = editText.getText().toString();
                    if(selectedoptions.length()==0){
                        filledFlag=false;
                        return null;
                    }
                }

            }
            optionIdCounter=1;
            for (int i = 0; i < surveyList.size(); i++) {

                String selectedoptions = "";

                surveyno = surveyList.get(i).get(TAG_SURVEYNO);
                qno = surveyList.get(i).get(TAG_QNO);
                type = surveyList.get(i).get(TAG_TYPE);
                noofoptions = surveyList.get(i).get(TAG_NOOFOPTIONS);
                int noOfOptions = Integer.valueOf(noofoptions);

                if (type.equalsIgnoreCase("checkbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                } else if (type.equalsIgnoreCase("radio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                } else if (type.equalsIgnoreCase("othercheckbox")) {
                    for (int j = 1; j <= noOfOptions; j++) {
                        CheckBox checkBox = (CheckBox) findViewById(optionIdCounter++);
                        if (checkBox.isChecked()) {
                            if (selectedoptions.length() == 0)
                                selectedoptions = checkBox.getText().toString();
                            else {
                                selectedoptions += "#";
                                selectedoptions += checkBox.getText().toString();
                            }
                        }
                    }
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    if (selectedoptions.length() == 0)
                        selectedoptions = editText.getText().toString();
                    else {
                        selectedoptions += "#";
                        selectedoptions += editText.getText().toString();
                    }
                } else if (type.equalsIgnoreCase("otherradio")) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(optionIdCounter++);
                    optionIdCounter+=noOfOptions;
                    if(radioGroup.getCheckedRadioButtonId()!=-1)
                        selectedoptions = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions += "#";
                    selectedoptions+= editText.getText().toString();
                } else {
                    EditText editText = (EditText) findViewById(optionIdCounter++);
                    selectedoptions = editText.getText().toString();
                }

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("emailid", Login.emailid));
                params.add(new BasicNameValuePair("surveyno", surveyno));
                params.add(new BasicNameValuePair("qno", qno));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("option", selectedoptions));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jParser.makeHttpRequest(url_insert, "GET", params);

                // check log cat fro response
                //Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1 && !errorFlag) {
                        // successfully created product
                        errorFlag=false;
                        // closing this screen
                    } else {
                        errorFlag=true;
                        error=json.getString("message");
                        // failed to create product
                    }
                } catch (JSONException e) {
                    error=e.toString();
                    errorFlag=true;
                }
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            runOnUiThread(new Runnable() {
                public void run() {
                    if(errorFlag){
                        AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Could not submit response.\n"+error);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which){}
                        });
                        alertDialog.show();
                    }
                    else{
                        if(filledFlag) {
                            AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                            alertDialog.setTitle("Congratulations");
                            alertDialog.setMessage("Your response is recorded");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                    SharedPreferences settings = getSharedPreferences(SAVED_SURVEYS, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    surveynos = settings.getString("surveys", "");
                                    surveynos = surveynos.replace(sno + ",", "");
                                    editor.putString("surveys", surveynos);
                                    settings.edit().remove("surveytitle" + sno).commit();
                                    settings.edit().remove("object" + sno).commit();
                                    editor.commit();
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(DisplayNewSurvey.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("All questions must be answered");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
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
