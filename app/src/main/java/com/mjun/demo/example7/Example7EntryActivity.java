package com.mjun.demo.example7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example7EntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example7_entry);
        init();
    }

    private void init() {
        final View myContainer = findViewById(R.id.container);
        final Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MTransition transition = MTransitionManager.getInstance().createTransition("example");
                transition.fromPage().setContainer(myContainer, new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        transition.fromPage().addTransitionView("image", findViewById(R.id.image));
                    }
                });
                Intent intent = new Intent(Example7EntryActivity.this, Example7DetailActivity.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example7EntryActivity.this);
            }
        });

    }
}
