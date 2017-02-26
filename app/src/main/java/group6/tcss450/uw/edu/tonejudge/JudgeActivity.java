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
    }
}
