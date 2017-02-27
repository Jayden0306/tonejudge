package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TopRanksToneActivity extends NavDrawerActivity {

    private Tone mTone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranks_tone);
        mTone = (Tone) getIntent().getSerializableExtra("tone");
        ((Toolbar) findViewById(R.id.toolbar)).setTitle("Top: " + mTone.getName());
        onCreateDrawer();
    }
}
