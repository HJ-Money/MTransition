package com.mjun.demo.example4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

public class Example4EntryActivity4 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example4_1);
        init();
        startAnim();
    }

    private void init() {
        ((TextView) findViewById(R.id.text)).setText(getString(R.string.example4_tip41));
        ((Button) findViewById(R.id.button)).setText("不要点啦");
        ((Button) findViewById(R.id.button)).setVisibility(View.GONE);
    }

    private void startAnim() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Example4EntryActivity3");
        transition.toPage().setContainer(findViewById(R.id.container), new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                container.translateY(container.getHeight(), 0);
            }
        });
        transition.setDuration(500);
        transition.start();
        findViewById(R.id.container).setBackgroundColor(0xffe9ae6a);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("Example4EntryActivity3");
    }

    @Override
    public void onBackPressed() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Example4EntryActivity3");
        transition.reverse();
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example4EntryActivity4.this);
                }
            }
        });
    }
}
