package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 22/10/17.
 *
 *
 *
 * class that mange and display the steps of a recipe in the steps RecyclerView
 * impalement the @LayoutManager and  @StepsAdapter
 *
 *
 * it will be called from the class @FragmentDetails
 * will take  a list of @ingredient that will be passed to the adapter
 * and
 * exoPlayer object that will be used to play the 'step' video
 *
 *
 *
 */

public class FragmentStep extends Fragment implements StepsAdapter.StepsClickListener{

    Context mContext;
    List<Step> mStepsList = new ArrayList<>();
    SimpleExoPlayer mExoPlayer;

    @BindView(R.id.rv_step)
    RecyclerView mStepsView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step,container,false);
        mContext = rootView.getContext();
        ButterKnife.bind(this,rootView);
        setLayoutManger();
        return rootView;
    }

    public FragmentStep(){

    }


    public FragmentStep(List<Step> s ,SimpleExoPlayer player){
        this.mStepsList =s;
        this.mExoPlayer=player;
    }

    private void setLayoutManger() {
        /*
        *set layoutManager and steps Adapter
        *
        * and update adapter list with the passed date from RecipeDetailsContainer
        *
        * */
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        StepsAdapter adapter = new StepsAdapter(this);
        mStepsView.setAdapter(adapter);
        mStepsView.setLayoutManager(manager);
        mStepsView.setHasFixedSize(true);
        adapter.UpdateStepsList(mStepsList);
    }

    @Override
    public void onStepClick(Step s) {
        /*
        *
        * override from the @StepsAdapter.StepsClickListener
        * to create a toast that will display the recipe description in case that
        * the content was not completely available, in it's textView
        *
        * */

        Toast.makeText(mContext,""+s.description, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onVideoLoadClick(Uri uri) {
        /*
        * override from the @StepsAdapter.StepsClickListener
        *
        * to handle video Icon click to load the step video into the ExoPlayer Object
        *
        * */
        if(uri != null) {
            setMediaSource(uri);
        }


    }

    private void setMediaSource(Uri uri){

        /*
        *
        * change the media Source in the ExoPlayer Object
        *
        * **/

        String userAgent = Util.getUserAgent(mContext,"BackingApp");
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
        MediaSource mediaSource =
                new ExtractorMediaSource(
                        uri,
                        defaultDataSourceFactory,
                        new DefaultExtractorsFactory(),
                        null,
                        null);
        mExoPlayer.prepare(mediaSource);
    }


}
