package com.mjun.demo.model;

import android.content.Context;
import com.mjun.demo.R;

public class AppModel {
    private static AppModel sInstance = new AppModel();

    private String[] mTitleArray;
    private String[] mCategoryArray;

    private AppModel() {
    }

    public static AppModel getInstance() {
        return sInstance;
    }

    public String[] getTitleArray(Context context) {
        if (mTitleArray == null) {
            int resID = R.array.app_titles;
            mTitleArray = context.getResources().getStringArray(resID);
        }
        return mTitleArray;
    }

    public String[] getCategoryArray(Context context) {
        if (mCategoryArray == null) {
            int resID = R.array.app_categorys;
            mCategoryArray = context.getResources().getStringArray(resID);
        }
        return mCategoryArray;
    }
}
