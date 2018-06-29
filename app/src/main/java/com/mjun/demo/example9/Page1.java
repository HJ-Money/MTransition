package com.mjun.demo.example9;

import java.util.*;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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

/**
 * Created by huijun on 2018/4/16.
 */

public class Page1 extends FrameLayout {


    private AppListPage mPage;
    // 数据
    private List<AppBean> mBeans = new ArrayList<>();

    public Page1(@NonNull Context context) {
        super(context);
        mPage = new AppListPage(context);
        addView(mPage);
        init();
    }

    private void init() {
        genDatas();
        mPage.mListView.setAdapter(new Page1.DemoListAdapter());
        mPage.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final MTransition transition = MTransitionManager.getInstance().createTransition("example");
                transition.fromPage().setContainer(mPage, new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        transition.fromPage().addTransitionView("icon", view.findViewById(R.id.item_icon));
                        transition.fromPage().addTransitionView("name", view.findViewById(R.id.item_name));
                        transition.fromPage().addTransitionView("snapshot", view.findViewById(R.id.item_snapshot));
                        transition.fromPage().addTransitionView("container", mPage);
                    }
                });
                transition.getBundle().putObject("bean", mBeans.get(position));
                ((Example9Activity) getContext()).enterOtherPage();
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
            bean.mName = AppModel.getInstance().getTitleArray(getContext())[i];
            bean.mSnapshot = AppModel.getInstance().getCategoryArray(getContext())[i];
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
            return convertView;
        }
    }
}
