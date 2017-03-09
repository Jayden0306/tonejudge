package group6.tcss450.uw.edu.tonejudge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ResultActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private String myText = "";
    private String mID;
    private ElementTone mElementTone;
    private Button mPublishButton;
    private ArrayList<String> mScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myText = getIntent().getStringExtra("text");
        String analysisJson = getIntent().getStringExtra("analysis");
        mElementTone = new GsonBuilder().create().fromJson(analysisJson, ElementTone.class);
                Log.d("result: ", mElementTone.toString());
        mID = getIntent().getStringExtra("id");
        mPublishButton = (Button) findViewById(R.id.results_publish);
        mScoreList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            HorizontalBarChart emotionChart = (HorizontalBarChart) findViewById(R.id.emotion_chart);
            HorizontalBarChart socialChart = (HorizontalBarChart) findViewById(R.id.social_chart);
            HorizontalBarChart languageChart = (HorizontalBarChart) findViewById(R.id.language_chart);

            List<String> emotionLabels = new ArrayList();
            List<String> socialLabels = new ArrayList();
            List<String> languageLabels = new ArrayList();

            ArrayList<BarEntry> emotionToneSet = new ArrayList();
            ArrayList<BarEntry> socialToneSet = new ArrayList<>();
            ArrayList<BarEntry> languageToneSet = new ArrayList<>();

            JSONArray jar = new JSONObject(mElementTone.toString()).getJSONArray("tone_categories");
            JSONArray emotionTones = new JSONObject(jar.get(0).toString()).getJSONArray("tones");
            JSONArray socialTones = new JSONObject(jar.get(1).toString()).getJSONArray("tones");
            JSONArray languageTones = new JSONObject(jar.get(2).toString()).getJSONArray("tones");

            addData(emotionToneSet, emotionTones, emotionLabels);
            addData(socialToneSet, socialTones, socialLabels);
            addData(languageToneSet, languageTones, languageLabels);

            
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
            
            updateChart(emotionChart, emotionToneSet, emotionLabels, emotionColors);
            updateChart(socialChart, socialToneSet, socialLabels, socialColors);
            updateChart(languageChart, languageToneSet, languageLabels, colors);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
            }


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


    private void addData(List<BarEntry> theToneSet, JSONArray theTones, List<String> theLabels) throws JSONException {
        for (int i = 0; i < theTones.length(); i++) {
            JSONObject tmp_tone = new JSONObject(theTones.get(i).toString());
            BigDecimal bd = new BigDecimal(tmp_tone.getString("score"));
            bd = bd.multiply(BigDecimal.valueOf(100));
            bd = bd.setScale(1, BigDecimal.ROUND_DOWN);
            try {
                bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
            } catch (ArithmeticException e) {}
            theToneSet.add(new BarEntry(i, bd.floatValue()));
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
        theChart.invalidate();
        String displayResult = getString(R.string.your_text) + myText;
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
        Log.d("Highlighted", e.toString());
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

    public static final String jsonResult = "{\n" +
                "\t\"document_tone\": {\n" +
                "\t\t\"tone_categories\": [ \n" +
                "\t\t{\n" +
                "\t\t\t\"category_id\": \"emotion_tone\",\n" +
                "            \"category_name\": \"Emotion Tone\",\n" +
                "            \"tones\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"tone_id\": \"anger\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Anger\",\n" +
                "\t\t\t\t\t\"score\": 0.156427\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"disgust\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Disgust\",\n" +
                "\t\t\t\t\t\"score\": 0.183589\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"fear\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Fear\",\n" +
                "\t\t\t\t\t\"score\": 0.277895\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"joy\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Joy\",\n" +
                "\t\t\t\t\t\"score\": 0.304628\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"sadness\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Sadness\",\n" +
                "\t\t\t\t\t\"score\": 0.145423\n" +
                "                }\n" +
                "            ]\n" +
                "            },\n" +
                "            {\n" +
                "            \"category_id\": \"language_tone\",\n" +
                "            \"category_name\": \"Language Tone\",\n" +
                "            \"tones\": [\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"analytical\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Analytical\",\n" +
                "\t\t\t\t\t\"score\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"confident\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Confident\",\n" +
                "\t\t\t\t\t\"score\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"tentative\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Tentative\",\n" +
                "\t\t\t\t\t\"score\": 0.0\n" +
                "                }\n" +
                "            ]\n" +
                "            },\n" +
                "            {\n" +
                "            \"category_id\": \"social_tone\",\n" +
                "            \"category_name\": \"Social Tone\",\n" +
                "            \"tones\": [\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"openness_big5\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Openness\",\n" +
                "\t\t\t\t\t\"score\": 0.30311\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"conscientiousness_big5\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Conscientiousness\",\n" +
                "\t\t\t\t\t\"score\": 0.864621\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"extraversion_big5\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Extraversion\",\n" +
                "\t\t\t\t\t\"score\": 0.136726\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"agreeableness_big5\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Agreeableness\",\n" +
                "\t\t\t\t\t\"score\": 0.176844\n" +
                "                },\n" +
                "                {\n" +
                "\t\t\t\t\t\"tone_id\": \"emotional_range_big5\",\n" +
                "\t\t\t\t\t\"tone_name\": \"Emotional Range\",\n" +
                "\t\t\t\t\t\"score\": 0.777629\n" +
                "                }\n" +
                "            ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

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
