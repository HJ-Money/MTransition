package com.mjun.demo.example41;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;

public class Example41EntryActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example41_1);
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.text)).setText(getString(R.string.example41_tip11));
        ((Button) findViewById(R.id.button)).setText(getString(R.string.example41_tip12));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MTransition transition = MTransitionManager.getInstance().createTransition("Activity1");
                transition.fromPage().setContainer(findViewById(R.id.wrapper), new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        // donothing
                    }
                });
                Intent intent = new Intent(Example41EntryActivity1.this, Example41EntryActivity2.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example41EntryActivity1.this);
            }
        });
    }

}
