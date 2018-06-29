package com.mjun.demo.example10;

import java.util.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import com.mjun.demo.R;
import com.mjun.demo.example10.com.view.jameson.library.CardScaleHelper;
import com.mjun.demo.example10.com.view.jameson.util.BlurBitmapUtils;
import com.mjun.demo.example10.com.view.jameson.util.ViewSwitchUtils;


/**
 * Created by huijun on 2018/4/8.
 */

public class Example10EntryActivity extends Activity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private List<Integer> mImgIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example10_entry);
        init();
    }

    private void init() {
        mImgIds.add(R.mipmap.img0);
        mImgIds.add(R.mipmap.img1);
        mImgIds.add(R.mipmap.img2);
        mImgIds.add(R.mipmap.img3);
        mImgIds.add(R.mipmap.img4);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new CardAdapter(this, mImgIds));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);

        initBlurBackground();
    }

    private void initBlurBackground() {
        mBlurView = (ImageView) findViewById(R.id.blurView);
        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        mLastPos++;
        mLastPos = mLastPos % mImgIds.size();
        final int resId = mImgIds.get(mLastPos);
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
            }
        };
        mBlurView.post(mBlurRunnable);
    }
}
