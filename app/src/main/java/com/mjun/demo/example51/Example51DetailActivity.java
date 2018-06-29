package com.mjun.demo.example51;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example51DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example51_detail);
        init();
    }

    private void init() {
        final View myContainer = findViewById(R.id.container);
        final ActionBtn btn = (ActionBtn) findViewById(R.id.exit_btn);
        btn.setProgress(1f);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                int width = container.getWidth();
                container.above(transition.fromPage().getContainer());
                container.alpha(0f, 1f);
                MTransitionView toBtn = transition.toPage().addTransitionView("btn", btn);
                toBtn.hideDuringTrasition();
                MTransitionView fromBtn = transition.fromPage().getTransitionView("btn");
                ActionBtn replace = new ActionBtn(Example51DetailActivity.this);
                fromBtn.replaceBy(replace);
                fromBtn.translateYTo(toBtn.getSourceView().getBottom() - fromBtn.getSourceView().getBottom());
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example51DetailActivity.this);
                }
            }
        });

        transition.setDuration(500);
        transition.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverse();
            }
        });
    }

    private void reverse() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.reverse();
    }

    @Override
    public void onBackPressed() {
        reverse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("example");
    }
}
