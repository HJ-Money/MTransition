package com.mjun.demo.example8;

import java.util.*;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.mjun.demo.R;

/**
 * Created by huijun on 2018/4/12.
 */

public class Example8Activity extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example8);
        init();
    }

    private void init() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, Fragment1.newInstance(), "f1")
                .commit();
    }

    public void enterOtherFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, Fragment2.newInstance(), "f2").addToBackStack("f2")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!handleBackPress()) {
            super.onBackPressed();
        }
    }

    private boolean handleBackPress() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments == null) return false;

        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);

            if (child instanceof IHandleBack &&
                    ((IHandleBack) child).onBackPressed(getSupportFragmentManager())) {
                return true;
            }
        }

        return false;
    }

    public interface IHandleBack {
        boolean onBackPressed(FragmentManager manager);
    }
}
