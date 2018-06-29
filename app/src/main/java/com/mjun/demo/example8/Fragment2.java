package com.mjun.demo.example8;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.TransitListenerAdapter;
import com.mjun.demo.example2.AppBean;
import com.mjun.demo.example2.AppDetailPage;

/**
 * Created by huijun on 2018/4/12.
 */

public class Fragment2 extends Fragment implements Example8Activity.IHandleBack {

    private Activity mActivity;
    AppDetailPage mAppDetailPage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAppDetailPage = new AppDetailPage(mActivity);
        AppBean bean = (AppBean) MTransitionManager.getInstance().getTransition("example").getBundle().getObject("bean");
        mAppDetailPage.setImageId(bean.mIconId);
        mAppDetailPage.setName(bean.mName);
        initTranstion();
        return mAppDetailPage;
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

    public static Fragment2 newInstance() {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MTransitionManager.getInstance().destoryTransition("example");
    }

    @Override
    public boolean onBackPressed(final FragmentManager manager) {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.reverse();
        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    manager.popBackStackImmediate();
                }
            }
        });
        return true;
    }
}
