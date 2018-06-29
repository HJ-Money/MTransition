package com.mjun.demo.example4;

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

public class Example4EntryActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example4_1);
        init();
        startAnim();
    }

    private void init() {
        ((TextView) findViewById(R.id.text)).setText(getString(R.string.example4_tip11));
        ((Button) findViewById(R.id.button)).setText(getString(R.string.example4_tip12));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTransition transition = MTransitionManager.getInstance().createTransition("Example4EntryActivity1");
                transition.fromPage().setContainer(findViewById(R.id.container), new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {

                    }
                });
                Intent intent = new Intent(Example4EntryActivity1.this, Example4EntryActivity2.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example4EntryActivity1.this);
            }
        });
    }

    private void startAnim() {

    }

}
