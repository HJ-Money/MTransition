package com.mjun.demo.example6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example6EntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example6_entry);
        init();
    }

    private void init() {
        final View myContainer = findViewById(R.id.container);
        final CatLottieView animView = findViewById(R.id.anim_view);
        animView.setProgress(0f);
        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MTransition transition = MTransitionManager.getInstance().createTransition("example");
                transition.fromPage().setContainer(myContainer, new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        transition.fromPage().addTransitionView("animView", animView);
                    }
                });
                Intent intent = new Intent(Example6EntryActivity.this, Example6DetailActivity.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example6EntryActivity.this);
            }
        });
    }
}
