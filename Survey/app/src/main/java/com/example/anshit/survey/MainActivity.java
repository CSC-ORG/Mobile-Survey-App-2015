package com.example.anshit.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener {

    String sno,stitle,surveynos;
    public static boolean noconnection;
    // url to get all products list
    private static String url_survey_list;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    public void onRestart(){
        super.onRestart();
        if(mTitle == getString(R.string.title_section1)) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainactivityid);
            linearLayout.removeAllViews();
            surveyList.clear();
            url_survey_list = Login.ip_address + "get_new_survey_list.php";
            new LoadSurveys().execute();
        }
        else if(mTitle == getString(R.string.title_section3)) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainactivityid);
            linearLayout.removeAllViews();
            loadSavedSurveys();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        surveyList = new ArrayList<HashMap<String, String>>();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.mainactivityid);
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                linearLayout.removeAllViews();
                surveyList.clear();
                url_survey_list = Login.ip_address+"get_new_survey_list.php";
                new LoadSurveys().execute();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                linearLayout.removeAllViews();
                surveyList.clear();
                url_survey_list = Login.ip_address+"get_filled_survey_list.php";
                new LoadSurveys().execute();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                linearLayout.removeAllViews();
                loadSavedSurveys();
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                linearLayout.removeAllViews();
                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(20);
                textView.setTextColor(Color.parseColor("#333333"));
                textView.setText("User Manual\n" +
                        "\n" +
                        "•\tThe login/signup page of android application is a onetime login/signup required when you install the application for the first time.\n" +
                        " \n" +
                        "•\tThe main page of the application contains a navigation drawer which gives us various options.\n" +
                        " \n" +
                        "•\tThe navigation drawer gives us access to\n" +
                        "\n" +
                        "o\tNew Surveys\n" +
                        "o\tPreviously Filled Surveys\n" +
                        "o\tSaved Surveys\n" +
                        "o\tHelp\n" +
                        "o\tExit\n" +
                        "\n" +
                        "•\tThe New Surveys are downloaded directly from the server to the android application. The user can choose to fill the survey by clicking on the survey.\n" +
                        " \n" +
                        "•\tAll the questions and options along with images are displayed in the application. The user is required to fill the answers and press the submit button.\n" +
                        " \n" +
                        "•\tThe responses are uploaded to the cloud database. \n" +
                        "\n" +
                        "•\tThe option, Previously Filled Surveys, allows the user to view the questions and the responses to all the surveys filled by him previously\n" +
                        "\n" +
                        "•\tThe Help button opens the help.\n" +
                        "\n" +
                        "•\tThe exit option is used to shut down the currently running instance of the application.");
                linearLayout.addView(textView);
                break;
            case 5:
                System.exit(0);
                break;
        }
    }

    private void loadSavedSurveys() {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.mainactivityid);
        final SharedPreferences settings = getSharedPreferences(DisplayNewSurvey.SAVED_SURVEYS, 0);
        final SharedPreferences.Editor editor = settings.edit();
        surveynos=settings.getString("surveys","");
        if(surveynos.indexOf(",")==-1){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 0);
            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(20);
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setGravity(Gravity.CENTER);
            textView.setText("No Saved Surveys");
            linearLayout.addView(textView);
        }
        else {
            int buttonid=1;
            while(surveynos.indexOf(",")!=-1){
                sno=surveynos.substring(0,surveynos.indexOf(","));
                surveynos=surveynos.substring(surveynos.indexOf(",")+1);
                stitle=settings.getString("surveytitle"+sno,"");

                Button btn = new Button(MainActivity.this);
                btn.setOnClickListener(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                btn.setLayoutParams(lp);
                btn.setGravity(Gravity.CENTER);
                btn.setId(buttonid++);
                btn.setText(stitle);
                btn.setHint(sno);
                btn.setTextColor(Color.parseColor("#444444"));
                btn.setBackgroundResource(R.drawable.survey_background);
                linearLayout.addView(btn);
            }
            Button btn = new Button(MainActivity.this);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getSharedPreferences(DisplayNewSurvey.SAVED_SURVEYS, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent=getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(lp);
            btn.setGravity(Gravity.CENTER);
            btn.setText("Clear Saved Surveys");
            btn.setTextColor(Color.parseColor("#fafafa"));
            btn.setBackgroundResource(R.drawable.button_background);
            linearLayout.addView(btn);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(mTitle==getString(R.string.title_section1))
        {
            String sno=surveyList.get(v.getId()).get(TAG_SURVEYNO);
            String stitle=surveyList.get(v.getId()).get(TAG_SURVEYTITLE);
            Intent intent=new Intent(this, DisplayNewSurvey.class);
            intent.putExtra(TAG_SURVEYNO,sno);
            intent.putExtra(TAG_SURVEYTITLE,stitle);
            intent.putExtra("object", "none");
            startActivity(intent);
        }
        else if (mTitle==getString(R.string.title_section2))
        {
            String sno=surveyList.get(v.getId()).get(TAG_SURVEYNO);
            Intent intent=new Intent(this, DisplayFilledSurvey.class);
            intent.putExtra(TAG_SURVEYNO,sno);
            startActivity(intent);
        }
        else if(mTitle==getString(R.string.title_section3)){
            Button button=(Button)findViewById(v.getId());
            sno = (String) button.getHint();
            stitle = (String) button.getText();
            Intent intent=new Intent(this, DisplayNewSurvey.class);
            SharedPreferences settings = getSharedPreferences(DisplayNewSurvey.SAVED_SURVEYS, 0);
            String str=settings.getString("object"+sno,"");
            intent.putExtra(TAG_SURVEYNO, sno);
            intent.putExtra(TAG_SURVEYTITLE,stitle);
            intent.putExtra("object", str);
            startActivity(intent);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SURVEY = "survey";
    private static final String TAG_SURVEYNO = "surveyno";
    private static final String TAG_SURVEYTITLE = "surveytitle";

    // products JSONArray
    JSONArray surveys = null;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> surveyList;

    class LoadSurveys extends AsyncTask<String, String, String> {

        String error;
        private ProgressDialog pDialog;
        boolean surveyFoundFlag=true,errorFlag=false;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainactivityid);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading surveys. Please wait...");
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
            params.add(new BasicNameValuePair("emailid", Login.emailid));
            noconnection=false;
            JSONObject json = jParser.makeHttpRequest(url_survey_list, "GET", params);

            if(noconnection){
                return null;
            }
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
                        String surveyno = c.getString(TAG_SURVEYNO);
                        String surveytitle = c.getString(TAG_SURVEYTITLE);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SURVEYNO, surveyno);
                        map.put(TAG_SURVEYTITLE, surveytitle);

                        // adding HashList to ArrayList
                        surveyList.add(map);

                    }
                } else {
                    // no survey found
                    surveyFoundFlag=false;
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
                        if(noconnection)
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("No internet connection.");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 30, 0, 0);
                            TextView textView = new TextView(MainActivity.this);
                            textView.setLayoutParams(layoutParams);
                            textView.setTextSize(20);
                            textView.setTextColor(Color.parseColor("#333333"));
                            textView.setGravity(Gravity.CENTER);
                            textView.setText("No Survey Found");
                            linearLayout.addView(textView);
                        }
                        else if(errorFlag){
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Could not load surveys.\n" + error);
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
                                    String surveyno = surveyList.get(i).get(TAG_SURVEYNO);
                                    String surveytitle = surveyList.get(i).get(TAG_SURVEYTITLE);

                                    Button btn = new Button(MainActivity.this);
                                    btn.setOnClickListener(MainActivity.this);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    btn.setLayoutParams(lp);
                                    btn.setId(i);
                                    btn.setGravity(Gravity.CENTER);
                                    btn.setText(surveytitle);
                                    btn.setTextColor(Color.parseColor("#444444"));
                                    btn.setBackgroundResource(R.drawable.survey_background);
                                    linearLayout.addView(btn);
                                }
                            } else {
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(0, 30, 0, 0);
                                TextView textView = new TextView(MainActivity.this);
                                textView.setLayoutParams(layoutParams);
                                textView.setTextSize(20);
                                textView.setTextColor(Color.parseColor("#333333"));
                                textView.setGravity(Gravity.CENTER);
                                textView.setText("No Survey Found");
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
