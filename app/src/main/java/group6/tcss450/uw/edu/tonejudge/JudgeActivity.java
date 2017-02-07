package group6.tcss450.uw.edu.tonejudge;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

public class JudgeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);
        Button b = (Button) findViewById(R.id.subButton);
        b.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View v) {
        EditText editText = (EditText) findViewById(R.id.judgeText);
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Input Some Text To Be Judged First!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new android.content.Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("json", editText.getText().toString());
            startActivity(intent);
        }
//        String JsonResult = "{\n" +
//                "\t\"document_tone\": {\n" +
//                "\t\t\"tone_categories\": [ \n" +
//                "\t\t{\n" +
//                "\t\t\t\"category_id\": \"emotion_tone\",\n" +
//                "            \"category_name\": \"Emotion Tone\",\n" +
//                "            \"tones\": [\n" +
//                "\t\t\t\t{\n" +
//                "\t\t\t\t\t\"tone_id\": \"anger\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Anger\",\n" +
//                "\t\t\t\t\t\"score\": 0.156427\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"disgust\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Disgust\",\n" +
//                "\t\t\t\t\t\"score\": 0.183589\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"fear\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Fear\",\n" +
//                "\t\t\t\t\t\"score\": 0.277895\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"joy\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Joy\",\n" +
//                "\t\t\t\t\t\"score\": 0.304628\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"sadness\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Sadness\",\n" +
//                "\t\t\t\t\t\"score\": 0.145423\n" +
//                "                }\n" +
//                "            ]\n" +
//                "            },\n" +
//                "            {\n" +
//                "            \"category_id\": \"language_tone\",\n" +
//                "            \"category_name\": \"Language Tone\",\n" +
//                "            \"tones\": [\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"analytical\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Analytical\",\n" +
//                "\t\t\t\t\t\"score\": 0.0\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"confident\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Confident\",\n" +
//                "\t\t\t\t\t\"score\": 0.0\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"tentative\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Tentative\",\n" +
//                "\t\t\t\t\t\"score\": 0.0\n" +
//                "                }\n" +
//                "            ]\n" +
//                "            },\n" +
//                "            {\n" +
//                "            \"category_id\": \"social_tone\",\n" +
//                "            \"category_name\": \"Social Tone\",\n" +
//                "            \"tones\": [\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"openness_big5\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Openness\",\n" +
//                "\t\t\t\t\t\"score\": 0.30311\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"conscientiousness_big5\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Conscientiousness\",\n" +
//                "\t\t\t\t\t\"score\": 0.864621\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"extraversion_big5\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Extraversion\",\n" +
//                "\t\t\t\t\t\"score\": 0.136726\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"agreeableness_big5\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Agreeableness\",\n" +
//                "\t\t\t\t\t\"score\": 0.176844\n" +
//                "                },\n" +
//                "                {\n" +
//                "\t\t\t\t\t\"tone_id\": \"emotional_range_big5\",\n" +
//                "\t\t\t\t\t\"tone_name\": \"Emotional Range\",\n" +
//                "\t\t\t\t\t\"score\": 0.777629\n" +
//                "                }\n" +
//                "            ]\n" +
//                "            }\n" +
//                "        ]\n" +
//                "    }\n" +
//                "}";



    }
}
