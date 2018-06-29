package com.mjun.demo.example4;

import android.app.Activity;
import android.content.Intent;
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

public class Example4EntryActivity3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example4_1);
        init();
        startAnim();
    }

    private void init() {
        ((TextView) findViewById(R.id.text)).setText(getString(R.string.example4_tip31));
        ((Button) findViewById(R.id.button)).setText(getString(R.string.example4_tip32));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTransition transition = MTransitionManager.getInstance().createTransition("Example4EntryActivity3");
                transition.fromPage().setContainer(findViewById(R.id.container), new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {

                    }
                });
                Intent intent = new Intent(Example4EntryActivity3.this, Example4EntryActivity4.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example4EntryActivity3.this);
            }
        });
        findViewById(R.id.container).setBackgroundColor(0xfffee388);
    }

    private void startAnim() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Example4EntryActivity2");
        transition.toPage().setContainer(findViewById(R.id.container), new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                container.alpha(0f, 1f);
            }
        });
        transition.setDuration(500);
        transition.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("Example4EntryActivity2");
    }

    @Override
    public void onBackPressed() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Example4EntryActivity2");
        transition.reverse();
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example4EntryActivity3.this);
                }
            }
        });
    }
}
