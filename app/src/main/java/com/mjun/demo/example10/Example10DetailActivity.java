package com.mjun.demo.example10;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;

import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;
import jameson.io.library.util.ScreenUtil;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example10DetailActivity extends Activity {

    private int mDownY = 0;
    private int mDownScrollY = 0;
    private boolean mBeginDrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example10_detail);
        init();
    }

    private void init() {
        ImageView headView = findViewById(R.id.image);
        int width = ScreenUtil.getScreenWidth(this);
        headView.getLayoutParams().height = (int) (width * 1.5f);
        headView.setImageResource(MTransitionManager.getInstance().getTransition("example").getBundle().getInt("imgId"));
        initTransition();
        // 增加滑动返回
        backByMove();
    }

    private void initTransition() {
        final View myContainer = findViewById(R.id.container);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                container.alpha(0f, 1f);

                MTransitionView content = transition.toPage().addTransitionView("content", findViewById(R.id.content));
                content.alpha(0f, 1f).scaleX(0.7f, 1f).scaleY(0.7f, 1f).translateY(-300, 0);

                MTransitionView cardBg = transition.fromPage().getTransitionView("card_bg");
                MTransitionView contentBg = transition.toPage().addTransitionView("content_bg", findViewById(R.id.content_bg));
                cardBg.transitTo(contentBg).below(content);

                MTransitionView fromImage = transition.fromPage().getTransitionView("image");
                MTransitionView toImage = transition.toPage().addTransitionView("image", findViewById(R.id.image));
                fromImage.transitTo(toImage).above(content);

                MTransitionView fromTitle = transition.fromPage().getTransitionView("title");
                MTransitionView toTitle = transition.toPage().addTransitionView("title", findViewById(R.id.title));
                fromTitle.transitTo(toTitle).above(content);

                MTransitionView fromNumber = transition.fromPage().getTransitionView("number");
                MTransitionView toNumber = transition.toPage().addTransitionView("number", findViewById(R.id.number));
                fromNumber.transitTo(toNumber).above(content);

                MTransitionView fromStars = transition.fromPage().getTransitionView("stars");
                MTransitionView toStars = transition.toPage().addTransitionView("stars", findViewById(R.id.stars));
                fromStars.transitTo(toStars).above(content);

                MTransitionView fromHead0 = transition.fromPage().getTransitionView("head0");
                MTransitionView toHead0 = transition.toPage().addTransitionView("head0", findViewById(R.id.head0));
                fromHead0.transitTo(toHead0).above(content);

                MTransitionView fromHead1 = transition.fromPage().getTransitionView("head1");
                MTransitionView toHead1 = transition.toPage().addTransitionView("head1", findViewById(R.id.head1));
                fromHead1.transitTo(toHead1).above(content);

                MTransitionView fromHead2 = transition.fromPage().getTransitionView("head2");
                MTransitionView toHead2 = transition.toPage().addTransitionView("head2", findViewById(R.id.head2));
                fromHead2.transitTo(toHead2).above(content);

                MTransitionView fromHead3 = transition.fromPage().getTransitionView("head3");
                MTransitionView toHead3 = transition.toPage().addTransitionView("head3", findViewById(R.id.head3));
                fromHead3.transitTo(toHead3).above(content);

                MTransitionView header = transition.fromPage().getTransitionView("header");
                header.translateY(0, -500);
                MTransitionView footer = transition.fromPage().getTransitionView("footer");
                footer.translateY(0, 500);
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {

            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example10DetailActivity.this);
                }
            }
        });

        transition.setDuration(1000);
        transition.start();
    }

    private void backByMove() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        // 滑动返回
        final ScrollView scrollView = findViewById(R.id.container);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = false;
                int scrolly = scrollView.getScrollY();
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mBeginDrag = false;
                    mDownY = (int) event.getY();
                    mDownScrollY = scrolly;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int delta = (int) (event.getY() - mDownY);
                    if (mDownScrollY <= 0 && delta > 0) {
                        // 下拉
                        transition.onBeginDrag();
                        mBeginDrag = true;
                    }
                    if (mBeginDrag) {
                        transition.setProgress(1f - delta / (float) scrollView.getMeasuredHeight());
                        result = true;
                    }
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    if (mBeginDrag) {
                        int delta = (int) (event.getY() - mDownY);
                        float progress = 1f - delta / (float) scrollView.getMeasuredHeight();
                        if (progress < 0.7f) {
                            transition.gotoCeil();
                        } else {
                            transition.gotoFloor();
                        }
                        result = true;
                    }
                }
                return result;
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
}
