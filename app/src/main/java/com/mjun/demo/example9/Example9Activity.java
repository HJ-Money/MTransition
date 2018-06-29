package com.mjun.demo.example9;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.mjun.demo.R;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.TransitListenerAdapter;

/**
 * Created by huijun on 2018/4/12.
 */

public class Example9Activity extends Activity {

    private Page1 mPage1;
    private Page2 mPage2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example9);
        init();
    }

    private void init() {
        mPage1 = new Page1(this);
        FrameLayout cotainer = findViewById(R.id.container);
        cotainer.addView(mPage1);
    }

    public void enterOtherPage() {
        mPage2 = new Page2(this);
        FrameLayout cotainer = findViewById(R.id.container);
        cotainer.addView(mPage2);
    }

    @Override
    public void onBackPressed() {
        if (mPage2 != null) {
            final MTransition transition = MTransitionManager.getInstance().getTransition("example");
            transition.reverse();
            transition.setOnTransitListener(new TransitListenerAdapter() {
                @Override
                public void onTransitEnd(MTransition transition, boolean reverse) {
                    if (reverse) {
                        if (mPage2.getParent() != null) {
                            FrameLayout cotainer = findViewById(R.id.container);
                            cotainer.removeView(mPage2);
                            mPage2 = null;
                        }
                    }
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("example");
    }
}
