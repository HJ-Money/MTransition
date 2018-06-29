package com.mjun.demo.example6;

import android.content.Context;
import android.util.AttributeSet;
import com.airbnb.lottie.LottieAnimationView;
import com.mjun.mtransition.ITransitional;

/**
 * Created by huijun on 2018/4/9.
 */

public class CatLottieView extends LottieAnimationView implements ITransitional {

    public CatLottieView(Context context) {
        super(context);
        init();
    }

    public CatLottieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        useHardwareAcceleration();
        setAnimation("anubis.json");
        setProgress(0f);
    }

    @Override
    public void onTransitProgress(long playTime, float progress) {
        setProgress(progress);
    }

    @Override
    public void onTransitStart() {

    }

    @Override
    public void onTransitEnd() {

    }
}
