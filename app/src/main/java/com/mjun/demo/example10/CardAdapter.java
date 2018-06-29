package com.mjun.demo.example10;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mjun.demo.R;
import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;

/**
 * Created by huijun on 2018/4/16.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Activity mActivity;
    private List<Integer> mImgIds = new ArrayList<>();

    public CardAdapter(Activity mActivity, List<Integer> imgIds) {
        this.mActivity = mActivity;
        mImgIds = imgIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.example10_card_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImage.setImageResource(mImgIds.get(position));
        holder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTransition(v);
                MTransitionManager.getInstance().getTransition("example").getBundle().putInt("imgId", mImgIds.get(position));
                Intent intent = new Intent(mActivity, Example10DetailActivity.class);
                mActivity.startActivity(intent);
                MTranstionUtil.removeActivityAnimation(mActivity);
            }
        });
    }


    private void initTransition(final View card) {
        View myContainer = mActivity.findViewById(R.id.container);
        final MTransition transition = MTransitionManager.getInstance().createTransition("example");
        transition.fromPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                transition.fromPage().addTransitionView("header", mActivity.findViewById(R.id.header));
                transition.fromPage().addTransitionView("footer", mActivity.findViewById(R.id.footer));
                transition.fromPage().addTransitionView("image", card.findViewById(R.id.image));
                transition.fromPage().addTransitionView("title", card.findViewById(R.id.title));
                transition.fromPage().addTransitionView("number", card.findViewById(R.id.number));
                transition.fromPage().addTransitionView("stars", card.findViewById(R.id.stars));
                transition.fromPage().addTransitionView("head0", card.findViewById(R.id.head0));
                transition.fromPage().addTransitionView("head1", card.findViewById(R.id.head1));
                transition.fromPage().addTransitionView("head2", card.findViewById(R.id.head2));
                transition.fromPage().addTransitionView("head3", card.findViewById(R.id.head3));
                transition.fromPage().addTransitionView("card_bg", card.findViewById(R.id.card_bg));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImgIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mContent;
        private ImageView mImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            mContent = itemView;
            mImage = itemView.findViewById(R.id.image);
        }

    }
}
