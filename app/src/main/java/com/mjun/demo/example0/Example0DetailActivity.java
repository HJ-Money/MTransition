package com.mjun.demo.example0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;

import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;
import com.mjun.demo.TestActivityAnimtaionActivity;
import com.mjun.demo.example2.AppBean;
import com.mjun.demo.example2.AppDetailPage;

public class Example0DetailActivity extends Activity {

    AppDetailPage mAppDetailPage;
    private int mDownX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDetailPage = new AppDetailPage(this);
        AppBean bean = (AppBean) MTransitionManager.getInstance().getTransition("example").getBundle().getObject("bean");
        mAppDetailPage.setImageId(bean.mIconId);
        mAppDetailPage.setName(bean.mName);
        setContentView(mAppDetailPage);
        mAppDetailPage.setContent(getString(R.string.example0_tip));
        initTranstion();
    }

    private void initTranstion() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(mAppDetailPage, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                int width = container.getWidth();
                transition.fromPage().getTransitionView("container").translateX(0, -width / 4);
                container.translateX(width, 0);
            }
        });
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example0DetailActivity.this);
                }
            }
        });
        transition.setDuration(500);
        transition.start();

        mAppDetailPage.mImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Example0DetailActivity.this, TestActivityAnimtaionActivity.class);
                startActivity(intent);
            }
        });

        // 滑动返回
        mAppDetailPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    transition.onBeginDrag();
                    mDownX = (int) event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int delta = (int) (event.getX() - mDownX);
                    transition.setProgress(1f - delta / (float) mAppDetailPage.getMeasuredWidth());
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    int delta = (int) (event.getX() - mDownX);
                    float progress = 1f - delta / (float) mAppDetailPage.getMeasuredWidth();
                    if (progress < 0.5f) {
                        transition.gotoCeil();
                    } else {
                        transition.gotoFloor();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.reverse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("example");
    }
}
