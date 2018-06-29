package com.mjun.demo.example2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mjun.demo.R;

public class AppDetailPage extends ScrollView {

    public ImageView mImage;
    public TextView mName;
    public TextView mContent;
    public int mImgId;

    public AppDetailPage(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.appdetail_page, this, true);
        mImage = (ImageView) findViewById(R.id.app_detail_icon);
        mName = (TextView) findViewById(R.id.app_detail_name);
        mContent = (TextView) findViewById(R.id.app_detail_content);
        setBackgroundColor(Color.WHITE);
    }

    public void setImageId(int imgId) {
        mImgId = imgId;
        mImage.setImageResource(imgId);
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void setContent(String name) {
        mContent.setText(name);
    }
}
