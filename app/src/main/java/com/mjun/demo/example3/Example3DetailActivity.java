package com.mjun.demo.example3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;
import com.mjun.demo.example2.AppBean;
import com.mjun.demo.example2.AppDetailPage;

public class Example3DetailActivity extends Activity {

    AppDetailPage mAppDetailPage;
    private int mDownX = 0;
    private int mClickItemPos = 0;
    private int mItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDetailPage = new AppDetailPage(this);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        AppBean bean = (AppBean) transition.getBundle().getObject("bean");
        mAppDetailPage.setImageId(bean.mIconId);
        mAppDetailPage.setName(bean.mName);
        mClickItemPos = transition.getBundle().getInt("clickItemPos", 0);
        mItemCount = transition.getBundle().getInt("itemCount", 0);
        setContentView(mAppDetailPage);
        initTranstion();
    }

    private void initTranstion() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(mAppDetailPage, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                MTransitionView fromContainer = transition.fromPage().getContainer();
                fromContainer.setBgColor(Color.TRANSPARENT);
                for (int i = 0; i < mItemCount; i++) {
                    MTransitionView child = transition.fromPage().getTransitionView("item" + i);
                    if (i <= mClickItemPos) {
                        child.translateY(0, -container.getHeight()).translateYInterpolator(new AccelerateInterpolator());
                    } else if (i > mClickItemPos) {
                        child.translateY(0, container.getHeight()).translateYInterpolator(new AccelerateInterpolator());
                    }
                }
                MTransitionView header = transition.fromPage().getTransitionView("header");
                header.translateY(0, -header.getHeight()).setDuration(500);
                container.below(fromContainer);
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example3DetailActivity.this);
                }
            }
        });

        transition.setDuration(800);
        transition.start();
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
