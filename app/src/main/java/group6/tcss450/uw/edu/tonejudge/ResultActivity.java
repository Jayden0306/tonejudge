package group6.tcss450.uw.edu.tonejudge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;

public class ResultActivity extends AppCompatActivity {

    private String myText = "";

    private ElementTone elementTone;
    private Button mPublishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myText = getIntent().getStringExtra("json");
        mPublishButton = (Button) findViewById(R.id.results_publish);
//        Log.d("json", myText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setUsernameAndPassword("676b4490-b97c-4d56-a087-585e19daa351", "SXKJN7YuyIHn");
        ToneAnalysis tone = service.getTone(myText, null).execute();
        Log.d("Service Return", tone.toString());
        elementTone = tone.getDocumentTone();
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

    public void onPublishClick(View view) {
        JSONObject request = ElementTones.elementToneToSimpleJson(elementTone);
        try {
            request.put("email", "EMAILLLLLLL");
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
}
