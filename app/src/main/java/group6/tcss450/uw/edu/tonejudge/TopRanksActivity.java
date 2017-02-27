package group6.tcss450.uw.edu.tonejudge;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class TopRanksActivity extends NavDrawerActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    private ExpandableListView mListView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranks);
        mListView = (ExpandableListView) findViewById(R.id.top_ranks_expandable_list);
        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(this);
        mListView.setOnChildClickListener(this);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        onCreateDrawer();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return true;
    }

    private class Adapter extends BaseExpandableListAdapter {

        private Tone.Category[] groups = Tone.Category.values();

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups[groupPosition].getTones().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups[groupPosition].getTones().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            } else {
                TextView tv = new TextView(TopRanksActivity.this);
                tv.setText(groups[groupPosition].getName());
                tv.setGravity(Gravity.END);
                return tv;
            }
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            } else {
                TextView tv = new TextView(TopRanksActivity.this);
                Tone tone = groups[groupPosition].getTones().get(childPosition);
                tv.setText(tone.getName());
                tv.setBackgroundColor(getColor(tone.getColorId()));
                tv.setHeight(100);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(19.0f);
                return tv;
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
