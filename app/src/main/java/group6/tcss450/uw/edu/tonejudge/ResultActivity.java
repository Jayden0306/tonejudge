package group6.tcss450.uw.edu.tonejudge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private String myText = "";

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
        mPublishButton = (Button) findViewById(R.id.results_publish);
        mScoreList = new ArrayList<String>();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d("Service Return", myAnalysis.toString());
        try {
            HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.chart);
            JSONArray jar = new JSONObject(mElementTone.toString()).getJSONArray("tone_categories");
            List<String> labels = new ArrayList();
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();

            int dataset_ctr = 0;
            for (int i = 0; i < jar.length(); i++) {
                ArrayList<BarEntry> toneSet = new ArrayList();
                JSONObject temp_j = new JSONObject(jar.get(i).toString());
//                Log.d("Category Name", temp_j.get("category_name").toString());
//                sb.append("Category Name: " + temp_j.get("category_name").toString() + "\n");
                JSONArray tones = temp_j.getJSONArray("tones");
                for (int j = 0; j < tones.length(); j++) {
                    JSONObject tmp_tone = new JSONObject(tones.get(j).toString());
                    toneSet.add(new BarEntry(dataset_ctr++, Float.parseFloat(tmp_tone.getString("score"))));
                    labels.add(tmp_tone.get("tone_name").toString());
                    mScoreList.add(tmp_tone.get("score").toString());
//                    sb.append("\t Tone: " + tmp_tone.get("tone_name").toString() + "\n");
//                    sb.append("\t Score: " + tmp_tone.get("score").toString() + "\n");
                    Log.d("Tone", tmp_tone.get("tone_name").toString());
                    Log.d("Tone Score", tmp_tone.get("score").toString());
                }
//                Log.d("Jar output", jar.get(i).toString());
                BarDataSet bds = new BarDataSet(toneSet, temp_j.get("category_name").toString());
                switch (temp_j.getString("category_id")) {
                    case ("emotion_tone"): {
//                        int[] colors = {Tone.anger.getColorId(), Tone.disgust.getColorId(),
//                                Tone.fear.getColorId(), Tone.joy.getColorId(), Tone.sadness.getColorId()};
//                        bds.setColors(colors);
                        bds.setColor(R.color.btn_red);
                    }
                    case ("language_tone"): {
//                        int[] colors = {Tone.analytical.getColorId(), Tone.confident.getColorId(),
//                                Tone.tentative.getColorId()};
//                        bds.setColors(colors);
                        bds.setColor(R.color.very_light_green);
                    }
                    case ("social_tone"): {
//                        int[] colors = {Tone.openness_big5.getColorId(),
//                                Tone.conscientiousness_big5.getColorId(),
//                                Tone.extraversion_big5.getColorId(),
//                                Tone.agreeableness_big5.getColorId(),
//                                Tone.emotional_range_big5.getColorId()};
//                        bds.setColors(colors);
                        bds.setColor(R.color.very_light_blue);
                    }
                }
                dataSets.add(bds);
            }
            ((TextView)findViewById(R.id.original_text)).setText("Your Text: \n" + myText);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.9f);
            chart.getXAxis().setDrawGridLines(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.setData(data);
            chart.setFitBars(true);
            XAxis xaxis = chart.getXAxis();
            xaxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xaxis.setLabelCount(200000000);
            chart.setTouchEnabled(false);
            chart.invalidate();

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

        } catch (JSONException e) {

        }
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
}
