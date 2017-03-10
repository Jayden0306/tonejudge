package group6.tcss450.uw.edu.tonejudge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends NavDrawerActivity implements OnChartValueSelectedListener {
    /**
     * the message that user enter
     */
    private String myText = "";
    
    /**
     * the flag to check whether adding data to the database
     */
    private String mID;
    private ElementTone mElementTone;
    /**
     * the button allow user publish data into the database
     */
    
    private Button mPublishButton;
    /**
     * the list stores scores value
     */
    private ArrayList<String> mScoreList;

    private List<String> mEmotionLabels;
    private List<String> mSocialLabels;
    private List<String> mLanguageLabels;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myText = getIntent().getStringExtra("text");
        String analysisJson = getIntent().getStringExtra("analysis");
        mElementTone = new GsonBuilder().create().fromJson(analysisJson, ElementTone.class);
        //get the id from the judge activity or history activity
        mID = getIntent().getStringExtra("id");
        mPublishButton = (Button) findViewById(R.id.results_publish);
        mScoreList = new ArrayList<>();
        onCreateDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            HorizontalBarChart emotionChart = (HorizontalBarChart) findViewById(R.id.emotion_chart);
            HorizontalBarChart socialChart = (HorizontalBarChart) findViewById(R.id.social_chart);
            HorizontalBarChart languageChart = (HorizontalBarChart) findViewById(R.id.language_chart);

            mEmotionLabels = new ArrayList<>();
            mSocialLabels = new ArrayList<>();
            mLanguageLabels = new ArrayList<>();

            ArrayList<BarEntry> emotionToneSet = new ArrayList<>();
            ArrayList<BarEntry> socialToneSet = new ArrayList<>();
            ArrayList<BarEntry> languageToneSet = new ArrayList<>();

            JSONArray jar = new JSONObject(mElementTone.toString()).getJSONArray("tone_categories");
            JSONArray emotionTones = new JSONObject(jar.get(0).toString()).getJSONArray("tones");
            JSONArray socialTones = new JSONObject(jar.get(1).toString()).getJSONArray("tones");
            JSONArray languageTones = new JSONObject(jar.get(2).toString()).getJSONArray("tones");

            addData(emotionToneSet, emotionTones, mEmotionLabels, .001f);
            addData(socialToneSet, socialTones, mSocialLabels, .002f);
            addData(languageToneSet, languageTones, mLanguageLabels, .003f);

            
            int[] emotionColors = {
                    ContextCompat.getColor(getApplicationContext(), Tone.anger.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.disgust.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.fear.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.joy.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.sadness.getColorId())
            };

            int[] socialColors = {
                    ContextCompat.getColor(getApplicationContext(), Tone.analytical.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.confident.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.tentative.getColorId())
            };

            int[] colors = {
                    ContextCompat.getColor(getApplicationContext(), Tone.openness_big5.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.conscientiousness_big5.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.extraversion_big5.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.agreeableness_big5.getColorId()),
                    ContextCompat.getColor(getApplicationContext(), Tone.emotional_range_big5.getColorId())
            };
            
            updateChart(emotionChart, emotionToneSet, mEmotionLabels, emotionColors);
            updateChart(socialChart, socialToneSet, mSocialLabels, socialColors);
            updateChart(languageChart, languageToneSet, mLanguageLabels, colors);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
            }

        //checking the id is avoid adding duplicate data into the database
        //if the id number not equal 0, scores will not add to the database
        if(mID.equals("0")) {
                DataBaseHelper db = new DataBaseHelper(this);
                ToneModel tone = new ToneModel(
                        System.currentTimeMillis(),
                        myText,
                        mScoreList.get(0),
                        mScoreList.get(1),
                        mScoreList.get(2),
                        mScoreList.get(3),
                        mScoreList.get(4),
                        mScoreList.get(5),
                        mScoreList.get(6),
                        mScoreList.get(7),
                        mScoreList.get(8),
                        mScoreList.get(9),
                        mScoreList.get(10),
                        mScoreList.get(11),
                        mScoreList.get(12)
                );
                db.addResult(tone);
            }
        }


    private void addData(List<BarEntry> theToneSet, JSONArray theTones, List<String> theLabels, float theVal) throws JSONException {
        for (int i = 0; i < theTones.length(); i++) {
            JSONObject tmp_tone = new JSONObject(theTones.get(i).toString());
            BigDecimal bd = new BigDecimal(tmp_tone.getString("score"));
            bd = bd.multiply(BigDecimal.valueOf(100));
            bd = bd.setScale(1, BigDecimal.ROUND_DOWN);
            try {
                bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
            } catch (ArithmeticException e) {}
            theToneSet.add(new BarEntry(i + theVal , bd.floatValue()));
            theLabels.add(tmp_tone.get("tone_name").toString());
            mScoreList.add(tmp_tone.get("score").toString());
        }
    }

    private void updateChart(BarChart theChart, ArrayList<BarEntry> theToneSet, List<String> theLabels,
                             int[] theColors) {
        BarDataSet dataset = new BarDataSet(theToneSet, "Tones");
        dataset.setValueFormatter(new MyValueFormatter());
        dataset.setValueTextSize(12f);
        Legend legend = theChart.getLegend();
        List<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < theLabels.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = theColors[i];
            entry.label = theLabels.get(i);
            entries.add(entry);
        }
        legend.setCustom(entries);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(14f);

        YAxis leftAxis = theChart.getAxisLeft();
        YAxis rightAxis = theChart.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        theChart.getXAxis().setDrawLabels(false);
        theChart.setDescription(new Description());

        dataset.setColors(theColors);

        theChart.setData(new BarData(dataset));
        theChart.getDescription().setEnabled(false);

        theChart.getXAxis().setDrawGridLines(false);
        theChart.getAxisLeft().setDrawGridLines(false);
        theChart.setOnChartValueSelectedListener(this);
        theChart.setPinchZoom(false);
        theChart.setDoubleTapToZoomEnabled(false);
        theChart.setHighlightPerDragEnabled(false);
        theChart.invalidate();

        String displayResult = getString(R.string.your_text) + "\n" + myText;
        ((TextView)findViewById(R.id.original_text)).setText(displayResult);
    }


    public void onPublishClick(View view) {
        JSONObject request = ElementTones.elementToneToDbJson(mElementTone);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.email), null);
        try {
            request.put("email", email);
            request.put("text", myText);
            request.put("action", PublishTask.ACTION);
            new PublishTask().execute(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        double d = e.getX() % 1;
        // Emotion Tones
        if (d < 0.0015) {
            Tone tone = Tone.valueOf(mEmotionLabels.get(Math.round(e.getX())).toLowerCase());
            new AlertDialog.Builder(this)
                    .setMessage(tone.getDescription(this))
                    .setTitle(tone.getName())
                    .show();

        // Social Tones
        } else if (d < 0.0025) {
            Tone tone = Tone.valueOf(mSocialLabels.get(Math.round(e.getX())).toLowerCase());
            new AlertDialog.Builder(this)
                    .setMessage(tone.getDescription(this))
                    .setTitle(tone.getName())
                    .show();

        // Language Tones
        } else {
            Tone tone = Tone.valueOf(mLanguageLabels.get(Math.round(e.getX())).toLowerCase()
                    .replace(" ", "_") + "_big5");
            new AlertDialog.Builder(this)
                    .setMessage(tone.getDescription(this))
                    .setTitle(tone.getName())
                    .show();
        }
    }

    @Override
    public void onNothingSelected() {

    }

    private class PublishTask extends JsonPostErrorTask {

        public static final String ACTION = "publish";

        private ProgressDialog progressDialog;

        public PublishTask() {
            super("https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/results");
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ResultActivity.this);
            progressDialog.setMessage("Publishing...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onFinish(String errorMessage) {
            progressDialog.dismiss();
            if (errorMessage == null) {
                Toast.makeText(getApplicationContext(), "Published successfully", Toast.LENGTH_LONG).show();
                mPublishButton.setClickable(false);
            } else {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class MyValueFormatter implements com.github.mikephil.charting.formatter.IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00"); // use two decimals
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + "%"; // e.g. append a dollar-sign
        }
    }
}
