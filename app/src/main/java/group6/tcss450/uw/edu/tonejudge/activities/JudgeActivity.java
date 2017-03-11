package group6.tcss450.uw.edu.tonejudge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import group6.tcss450.uw.edu.tonejudge.R;

/**
 * Activity where the user can input text and then press the judge button to analyze it and change
 * to the ResultsActivity.
 *
 * @author Hunter Bennett, Travis Stinebaugh
 */
public class JudgeActivity extends NavDrawerActivity implements View.OnClickListener {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);
        ImageButton b = (ImageButton) findViewById(R.id.subButton);
        mEditText = (EditText) findViewById(R.id.judgeText);
        b.setOnClickListener(this);

        // if this activity is started by sharing selected text from outside of the app
        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            mEditText.setText(sharedText.trim());
        }
        onCreateDrawer();
    }

    @Override
    public void onClick(View v) {
        String text = mEditText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Input Some Text To Be Judged First!",
                    Toast.LENGTH_SHORT).show();
        } else {
            new AnalyzeTask(text).execute();
        }
    }

    /**
     * An AsyncTask that analyzes the user inputted text using Watson Tone Analyzer and switches to
     * the ResultActivity.
     */
    private class AnalyzeTask extends AsyncTask<Void, Void, ToneAnalysis> {

        private ProgressDialog myProgressDialog;
        private String mText;

        private AnalyzeTask(String text) {
            this.mText = text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgressDialog = new ProgressDialog(JudgeActivity.this);
            myProgressDialog.setMessage("Judging...");
            myProgressDialog.show();
        }

        @Override
        protected ToneAnalysis doInBackground(Void... params) {
            ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
            service.setUsernameAndPassword("d8b44841-8e53-44a7-921f-13d31f3c0a04", "dMEdf3fORAkZ");
            return service.getTone(mText, null).execute();
        }

        @Override
        protected void onPostExecute(ToneAnalysis analysis) {
            super.onPostExecute(analysis);
            myProgressDialog.dismiss();
            Intent intent = new android.content.Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("text", mText);
            intent.putExtra("analysis", analysis.getDocumentTone().toString());
            intent.putExtra("id", "0");
            startActivity(intent);
        }
    }
}
