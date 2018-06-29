package com.mjun.demo.example9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.demo.example2.AppBean;
import com.mjun.demo.example2.AppDetailPage;

/**
 * Created by huijun on 2018/4/16.
 */

public class Page2 extends FrameLayout {

    AppDetailPage mAppDetailPage;

    public Page2(@NonNull Context context) {
        super(context);
        mAppDetailPage = new AppDetailPage(context);
        AppBean bean = (AppBean) MTransitionManager.getInstance().getTransition("example").getBundle().getObject("bean");
        mAppDetailPage.setImageId(bean.mIconId);
        mAppDetailPage.setName(bean.mName);
        addView(mAppDetailPage);
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
        transition.setDuration(500);
        transition.start();
    }

}
