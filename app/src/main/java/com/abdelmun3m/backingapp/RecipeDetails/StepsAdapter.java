package com.abdelmun3m.backingapp.RecipeDetails;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Step;

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
                if(s.thumbnailURL.equals("") || s.thumbnailURL ==null){
                    mPlayerButton.setVisibility(View.INVISIBLE);
                }
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

            //Function To load the the video URI to ExoPlayer from Recipe  videoURL Or thumbnailURL
            Uri uri = null;
            if(!s.videoURL.equals("") && s.videoURL != null){
                uri = Uri.parse(s.videoURL).buildUpon().build();
            }else if(!s.thumbnailURL.equals("") && s.thumbnailURL != null){
                uri = Uri.parse(s.thumbnailURL).buildUpon().build();
            }
            mClickListener.onVideoLoadClick(uri);
        }

    }



    public interface StepsClickListener {
        //on step click create Toast
        void onStepClick(Step s);

        //on Play Button click Load Step video
        void onVideoLoadClick(Uri uri);
    }


}
