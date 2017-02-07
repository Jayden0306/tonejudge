package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    private String myText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myText = getIntent().getStringExtra("json");
        Log.d("json", myText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setUsernameAndPassword("676b4490-b97c-4d56-a087-585e19daa351", "SXKJN7YuyIHn");
        ToneAnalysis tone = service.getTone(myText, null).execute();
        Log.d("Service Return", tone.toString());
        StringBuilder sb = new StringBuilder();
        try {
            JSONArray jar = new JSONObject(tone.toString()).getJSONObject("document_tone").getJSONArray("tone_categories");
            for (int i = 0; i < jar.length(); i++) {
                JSONObject temp_j = new JSONObject(jar.get(i).toString());
//                Log.d("Category Name", temp_j.get("category_name").toString());
                sb.append("Category Name: " + temp_j.get("category_name").toString() + "\n");
                JSONArray tones = temp_j.getJSONArray("tones");
                for (int j = 0; j < tones.length(); j++) {
                    JSONObject tmp_tone = new JSONObject(tones.get(j).toString());
                    sb.append("\t Tone: " + tmp_tone.get("tone_name").toString() + "\n");
                    sb.append("\t Score: " + tmp_tone.get("score").toString() + "\n");
//                    Log.d("Tone", tmp_tone.get("tone_name").toString());
//                    Log.d("Tone Score", tmp_tone.get("score").toString());
                }
//                Log.d("Jar output", jar.get(i).toString());
            }
            ((TextView)findViewById(R.id.results_text)).setText(sb.toString());
        } catch (JSONException e) {

        }
    }
}
