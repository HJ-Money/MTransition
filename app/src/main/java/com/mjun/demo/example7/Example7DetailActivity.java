package com.mjun.demo.example7;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example7DetailActivity extends Activity {

    private int mDownX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example7_detail);
        init();
    }

    private void init() {
        final View myContainer = findViewById(R.id.container);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                MTransitionView fromContainer = transition.fromPage().getContainer();
                final CurtainView curtainView = new CurtainView(Example7DetailActivity.this);
                curtainView.setDirection(CurtainView.DIRECTION_LEFT);
                curtainView.setTexture(snapshot(fromContainer.getSourceView()));
                fromContainer
                        .replaceBy(curtainView)
                        .above(container);
//                MTransitionView fromImage = transition.fromPage().getTransitionView("image");
//                MTransitionView toImage = transition.toPage().addTransitionView("image", findViewById(R.id.image));
//                toImage.hideDuringTrasition();
//                fromImage
//                        .above(fromContainer)
//                        .translateYTo(toImage.getSourceView().getTop() - fromImage.getSourceView().getTop());
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example7DetailActivity.this);
                }
            }
        });

        transition.setDuration(700);
        transition.start();

        final Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverse();
            }
        });

        // 滑动返回
        myContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    transition.onBeginDrag();
                    mDownX = (int) event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int delta = (int) (mDownX - event.getX());
                    transition.setProgress(1f - delta / (float) myContainer.getMeasuredWidth());
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    int delta = (int) (mDownX - event.getX());
                    float progress = 1f - delta / (float) myContainer.getMeasuredWidth();
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

    private void reverse() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.reverse();
    }

    @Override
    public void onBackPressed() {
        reverse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("example");
    }

    private Bitmap snapshot(View v) {

        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Drawable bg = v.getBackground();
        if(bg != null){
            bg.setBounds(0, 0, v.getWidth(), v.getHeight());
            bg.draw(c);
        }else{
            paint.setColor(0xFFFFFFFF);
            c.drawRect(0, 0, v.getWidth(), v.getHeight(), paint);
        }
        v.draw(c);
        //make sure the color on the top will continue to the status bar area
//        paint.setColor(b.getPixel(0, getStatusBarHeight() + 1));
//        c.drawRect(0, 0, getContext().getResources().getDisplayMetrics().widthPixels, getStatusBarHeight(), paint);
        return b;
    }
}
