package com.mjun.demo.example2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.mjun.demo.R;

public class AppListPage extends FrameLayout {

    public ListView mListView = null;
    public View mHeader = null;

    public AppListPage(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.applist_page, this, true);
        mHeader = findViewById(R.id.header);
        mListView = (ListView) findViewById(R.id.listview);
        setBackgroundColor(Color.WHITE);
    }
}
