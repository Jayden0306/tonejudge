package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends NavDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        onCreateDrawer();

        DataBaseHelper db = new DataBaseHelper(this);
        Log.d("Reading", "Reading all contacts...");
        List<ToneModel> toneList = db.getAllScores();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_history);
        TextView textView = (TextView) findViewById(R.id.display_DB);
        String display = "";

        for(ToneModel tone : toneList) {
            display += tone.getmMessage() + "\n";
            display += tone.getAnger() + "\n";
            display += tone.getDisgust() + "\n";
            display += tone.getFear() + "\n";
            display += tone.getSadness() + "\n";
            display += tone.getAnalytical() + "\n";
            display += tone.getConfident() + "\n";
            display += tone.getTentative() + "\n";
            display += tone.getOpenness() + "\n";
            display += tone.getConscientiousness() + "\n";
            display += tone.getExtraversion() + "\n";
            display += tone.getAgreeableness() + "\n";
            display += tone.getEmotionalRange() + "\n";
        }
        textView.setText(display);

    }
}
