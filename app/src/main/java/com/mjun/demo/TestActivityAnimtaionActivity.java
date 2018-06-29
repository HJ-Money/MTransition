package com.mjun.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;


/**
 * Created by huijun on 2018/6/8.
 */

public class TestActivityAnimtaionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.test_tip));
        setContentView(textView);
    }
}
