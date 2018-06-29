package com.mjun.demo.example6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

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

public class Example6DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example6_detail);
        init();
    }

    private void init() {
        final View myContainer = findViewById(R.id.container);
        final CatLottieView animView = findViewById(R.id.anim_view);
        animView.setProgress(1f);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                int width = container.getWidth();
                container.translateX(width, 0);
                MTransitionView toAnimView = transition.toPage().addTransitionView("animView", animView);
                toAnimView.hideDuringTrasition();
                MTransitionView fromAnimView = transition.fromPage().getTransitionView("animView");
                CatLottieView replace = new CatLottieView(Example6DetailActivity.this);
                fromAnimView.replaceBy(replace, new LayoutParams(animView.getWidth(), animView.getHeight()));
                fromAnimView.translateXTo(toAnimView.getSourceView().getLeft() - fromAnimView.getSourceView().getLeft());
                fromAnimView.above(container);
                transition.setDuration(animView.getDuration());
                transition.start();
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example6DetailActivity.this);
                }
            }
        });

        animView.setOnClickListener(new View.OnClickListener() {
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
