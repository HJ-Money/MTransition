package com.mjun.demo.example2;

import android.app.Activity;
import android.os.Bundle;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

public class Example2DetailActivity extends Activity {

    AppDetailPage mAppDetailPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDetailPage = new AppDetailPage(this);
        AppBean bean = (AppBean) MTransitionManager.getInstance().getTransition("example").getBundle().getObject("bean");
        mAppDetailPage.setImageId(bean.mIconId);
        mAppDetailPage.setName(bean.mName);
        setContentView(mAppDetailPage);
        initTranstion();
    }

    private void initTranstion() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(mAppDetailPage, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                container.alpha(0f, 1f);
                MTransitionView icon = transition.toPage().addTransitionView("icon", mAppDetailPage.mImage);
                MTransitionView name = transition.toPage().addTransitionView("name", mAppDetailPage.mName);
                transition.fromPage().getTransitionView("icon").above(icon).transitTo(icon, true);
                transition.fromPage().getTransitionView("name").above(name).transitTo(name)/*.setDuration(1000).setStartDelay(500)*/;
            }
        });
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
               if (reverse) {
                   finish();
                   MTranstionUtil.removeActivityAnimation(Example2DetailActivity.this);
               }
            }
        });

        transition.setDuration(500);
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
