package com.abdelmun3m.backingapp.RecipeDetails;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Step;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abdelmun3m on 22/10/17.
 */

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.StepsViewHolder> {


    List<Step> mStepsList = new ArrayList<>();
    stepListListeners mFragmentStepListener = null;
   // videoPlayerClickListener mVideoPlayerListener ;
    Boolean isImage = false;


    public AdapterSteps(stepListListeners listener){
        mFragmentStepListener = listener;
    }


    public void UpdateStepsList(List<Step> newStepList){
        this.mStepsList = newStepList;
        notifyDataSetChanged();
    }

    public void setmFragmentStepListener(stepListListeners lis){
     mFragmentStepListener = lis;
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


        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mRecipetDescription.setOnClickListener(this);
        }

        public void OnBind(int position){
            s = mStepsList.get(position);
            mRecipeShortDescription.setText(s.shortDescription.trim());
            mRecipetDescription.setText(s.description);

            //Function To Hide the the video Play Button if both videoURL Ond thumbnailURL is empty
            if(s.videoURL.equals("") || s.videoURL == null){
                   // mPlayerButton.setVisibility(View.INVISIBLE);
                if(s.thumbnailURL.equals("") || s.thumbnailURL == null){
                   mPlayerButton.setVisibility(View.INVISIBLE);
                }else  if(s.thumbnailURL.endsWith(".mp4") || s.thumbnailURL.endsWith(".mp3")) {
                    mPlayerButton.setVisibility(View.INVISIBLE);
                }else {

                    isImage = true;
                }
            }

            /*if(s.thumbnailURL.equals("") || s.thumbnailURL == null){
                mPlayerButton.setVisibility(View.INVISIBLE);
            }else if(s.thumbnailURL.endsWith(".mp4") || s.thumbnailURL.endsWith(".mp3")) {
                mPlayerButton.setVisibility(View.INVISIBLE);
            }else {
                mPlayerButton.setVisibility(View.VISIBLE);
            }*/


        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mRecipetDescription.getId()){
                mFragmentStepListener.onStepClick(mStepsList.get(getAdapterPosition()));
            }
        }

        @OnClick(R.id.btn_play)
        public void LoadStepVideo(View v){

            //Function To load the the video URI to ExoPlayer from Recipe  videoURL Or thumbnailURL
            String uri = null;

           if(!s.videoURL.equals("") && s.videoURL != null){
                uri =s.videoURL;
                isImage = false;
            }else if(!s.thumbnailURL.equals("") && s.thumbnailURL != null && isImage){
                uri = s.thumbnailURL;
            }
            mFragmentStepListener.onVideoLoadClick(uri,isImage);
          //  mVideoPlayerListener.onVideoLoadClick(uri,isImage);
        }

    }



    public interface stepListListeners {
        //on step click create Toast
        void onStepClick(Step s);
        void onVideoLoadClick(String uri, Boolean isImage);

    }

}
