package com.abdelmun3m.backingapp.RecipeDetails;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abdelmun3m on 22/10/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {


    List<Step> mStepsList = new ArrayList<>();
    StepsClickListener mClickListener = null;

   /* SimpleExoPlayer mExoPlayer;*/

    public StepsAdapter(StepsClickListener listener){
        mClickListener = listener;
    }


    public void UpdateStepsList(List<Step> newStepList){
        this.mStepsList = newStepList;
        notifyDataSetChanged();
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false);
        StepsViewHolder mViewHolder = new StepsViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.OnBind(position);
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {

        Step s=null;
        @BindView(R.id.tv_step_short_description)
        TextView mRecipeShortDescription;
        @BindView(R.id.tv_step_description)
        TextView mRecipetDescription;
        @BindView(R.id.btn_play)
        ImageView mPlayerButton;
       /* @BindView(R.id.player_view)
        SimpleExoPlayerView myExoPlayerView;*/
        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            //itemView.setOnClickListener(this);
            mRecipetDescription.setOnClickListener(this);
        }

        public void OnBind(int position){
            s = mStepsList.get(position);
            mRecipeShortDescription.setText(s.shortDescription.trim());
            mRecipetDescription.setText(s.description);
            if(s.videoURL.equals("") && s.videoURL == null){
                mPlayerButton.setVisibility(View.GONE);
            }if(s.thumbnailURL.equals("") && s.thumbnailURL ==null){
                mPlayerButton.setVisibility(View.GONE);
            }else {
                mPlayerButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mRecipetDescription.getId()){
                mClickListener.onStepClick(mStepsList.get(getAdapterPosition()));
            }
        }
        @OnClick(R.id.btn_play)
        public void LoadStepVideo(View v){
            if(s.videoURL.equals("") && s.videoURL == null){
                return;
            }else if(s.thumbnailURL.equals("") && s.thumbnailURL == null){
                return;
            }
            Uri uri = Uri.parse(s.videoURL).buildUpon().build();
            mClickListener.onVideoLoadClick(uri);
        }

    }



    public interface StepsClickListener {
        void onStepClick(Step s);
        void onVideoLoadClick(Uri uri);
    }


}
