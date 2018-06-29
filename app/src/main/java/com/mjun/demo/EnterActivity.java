package com.mjun.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.mjun.demo.example0.Example0EntryActivity;
import com.mjun.demo.example1.Example1EntryActivity;
import com.mjun.demo.example10.Example10EntryActivity;
import com.mjun.demo.example2.Example2EntryActivity;
import com.mjun.demo.example3.Example3EntryActivity;
import com.mjun.demo.example4.Example4EntryActivity1;
import com.mjun.demo.example41.Example41EntryActivity1;
import com.mjun.demo.example51.Example51EntryActivity;
import com.mjun.demo.example6.Example6EntryActivity;
import com.mjun.demo.example7.Example7EntryActivity;
import com.mjun.demo.example8.Example8Activity;
import com.mjun.demo.example9.Example9Activity;

public class EnterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_activity);

        findViewById(R.id.example0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example0EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example1EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example2EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example3EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example4EntryActivity1.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example41).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example41EntryActivity1.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example51EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example6EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example7EntryActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example8Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example9Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.example10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, Example10EntryActivity.class);
                startActivity(intent);
            }
        });
    }
}
