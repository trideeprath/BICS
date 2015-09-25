package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class reportDisplay extends ActionBarActivity implements View.OnClickListener{

    TextView finalReportTextView;
    TextView reportDate;
    ArrayList<String> reportScore;
    StringBuilder reportStringBuilder;
    String moduleName;
    String moduleScore;
    String reportString="";
    Button emailReport;
    String final_points;
    Intent intent ;
    final String Prefs_Pre_Post_Injury = "Prefs_Pre_Post";
    SharedPreferences prePostInjurySharedPreference;
    String prePostString;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_display);
        finalReportTextView = (TextView) findViewById(R.id.report_textview);
        emailReport = (Button) findViewById(R.id.email_report);
        emailReport.setOnClickListener(this);

        exit = (Button) findViewById(R.id.email_report_exit);
        exit.setOnClickListener(this);

        ArrayList<String> reportScore = reportScoreSave.scoreSave;
        intent = getIntent();
        prePostInjurySharedPreference = getSharedPreferences(Prefs_Pre_Post_Injury,0);
        prePostString = prePostInjurySharedPreference.getString("pre_post_injury","0");
        Log.d("pre post: ", prePostString);

        final_points = intent.getStringExtra("final_point");
        if(final_points.length() >4){
            final_points = final_points.substring(0,4);
        }
        reportDate = (TextView) findViewById(R.id.report_date);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        Log.d("date: ",dateString );

        reportDate.setText(prePostString + "\n" +dateString);



        for(int i=0 ; i<reportScore.size() ; i ++){
            moduleName = reportScore.get(i).split(":")[0];
            moduleScore = reportScore.get(i).split(":")[1];
            Log.d("ModuleName" , moduleName);
            Log.d("ModuleScore" , moduleScore);
            if(moduleScore.length() >3){
                if(i==0 || i==4) {
                    if(moduleScore.length() >6) {
                        moduleScore = moduleScore.substring(0,7);
                    }
                    else{
                        moduleScore = moduleScore.substring(0,6);
                    }
                }
                else{
                    moduleScore = moduleScore.substring(0, 3);
                }
            }
            reportString = reportString + moduleName + " : " +moduleScore +"\n";
        }

        reportString = reportString + "Total Score: " + final_points;

        finalReportTextView.setText(reportString);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.email_report){
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"neuroheadachecenter@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "BICS Report");
            email.putExtra(Intent.EXTRA_TEXT, reportDate.getText().toString()+"\n" +finalReportTextView.getText().toString());
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        if(v.getId() == R.id.email_report_exit){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
