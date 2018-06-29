package com.mjun.demo.example41;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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

public class Example41EntryActivity2 extends Activity {

    private int mDownX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example41_1);
        init();
        startAnim();
    }

    private void init() {
        ((TextView) findViewById(R.id.text)).setText(getString(R.string.example41_tip21));
        ((Button) findViewById(R.id.button)).setText(getString(R.string.example41_tip22));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTransition transition = MTransitionManager.getInstance().createTransition("Activity2");
                transition.fromPage().setContainer(findViewById(R.id.wrapper), new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {

                    }
                });
                Intent intent = new Intent(Example41EntryActivity2.this, Example41EntryActivity3.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example41EntryActivity2.this);
            }
        });
        findViewById(R.id.content).setBackgroundColor(0xfffffea0);
    }

    private void startAnim() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Activity1");
        transition.toPage().setContainer(findViewById(R.id.wrapper), new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                final MTransitionView content = transition.toPage().addTransitionView("content", findViewById(R.id.content));
                int width = container.getWidth();
                transition.fromPage().getContainer().translateX(0, -width / 4);
                content.translateX(container.getWidth(), 0);
                container.alpha(0f, 1f);
            }
        });
        transition.setDuration(500);
        transition.start();

        // 滑动返回
        findViewById(R.id.wrapper).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    transition.onBeginDrag();
                    mDownX = (int) event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int delta = (int) (event.getX() - mDownX);
                    transition.setProgress(1f - delta / (float) findViewById(R.id.wrapper).getMeasuredWidth());
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    int delta = (int) (event.getX() - mDownX);
                    float progress = 1f - delta / (float) findViewById(R.id.wrapper).getMeasuredWidth();
                    if (progress < 0.5f) {
                        transition.gotoCeil();
                    } else {
                        transition.gotoFloor();
                    }
                }
                return true;
            }
        });
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example41EntryActivity2.this);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("Activity1");
    }

    @Override
    public void onBackPressed() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("Activity1");
        transition.reverse();
    }

}
