package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HistoryActivity extends NavDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        onCreateDrawer();
    }
}
