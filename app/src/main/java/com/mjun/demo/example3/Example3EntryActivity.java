package com.mjun.demo.example3;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;

import com.mjun.demo.example2.AppBean;
import com.mjun.demo.example2.AppListPage;
import com.mjun.demo.model.AppModel;
import com.mjun.mtransition.MTranstionUtil;

public class Example3EntryActivity extends Activity{


    private AppListPage mPage;
    // 数据
    private List<AppBean> mBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = new AppListPage(this);
        mPage.setBackgroundColor(Color.TRANSPARENT);
        setContentView(mPage);
        init();
    }

    private void init() {
        genDatas();
        mPage.mListView.setDivider(null);
        mPage.mListView.setAdapter(new Example3EntryActivity.DemoListAdapter());
        mPage.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                final int firstItemPos = mPage.mListView.getFirstVisiblePosition();
                final int lastItemPos = mPage.mListView.getLastVisiblePosition();
                final MTransition transition = MTransitionManager.getInstance().createTransition("example");
                transition.fromPage().setContainer(mPage, new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        int count = lastItemPos - firstItemPos + 1;
                        for (int i = 0; i < count; i++) {
                            View child = parent.getChildAt(i);
                            transition.fromPage().addTransitionView("item" + i, child);
                        }
                        transition.fromPage().addTransitionView("header", mPage.mHeader);
                    }
                });
                transition.getBundle().putObject("bean", mBeans.get(position));
                transition.getBundle().putInt("clickItemPos", position - firstItemPos);
                transition.getBundle().putObject("itemCount", lastItemPos - firstItemPos + 1);
                Intent intent = new Intent(mPage.getContext(), Example3DetailActivity.class);
                mPage.getContext().startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example3EntryActivity.this);
            }
        });
    }

    private void genDatas() {
        for (int i = 0 ; i < 20; i++) {
            AppBean bean = new AppBean();
            String iconId = "icon_" + (i + 1);
            int identify = getResources().getIdentifier(iconId, "mipmap", "com.mjun.mtransition");
            if (identify == 0) {
                identify = R.mipmap.icon_1;
            }
            bean.mIconId = identify;
            bean.mName = AppModel.getInstance().getTitleArray(this)[i];
            bean.mSnapshot = AppModel.getInstance().getCategoryArray(this)[i];
            mBeans.add(bean);
        }
    }

    private class DemoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mPage.getContext()).inflate(R.layout.app_item_view, parent, false);
            }
            AppBean bean = mBeans.get(position);
            ((ImageView) convertView.findViewById(R.id.item_icon)).setImageResource(bean.mIconId);
            ((TextView) convertView.findViewById(R.id.item_name)).setText(bean.mName);
            ((TextView) convertView.findViewById(R.id.item_snapshot)).setText(bean.mSnapshot);
            if (position % 2 == 0) {
                convertView.setBackgroundColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(0xffeeeeee);
            }
            return convertView;
        }
    }
}
