package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 22/10/17.
 * <p>
 * <p>
 * <p>
 * class that mange and display the steps of a recipe in the steps RecyclerView
 * impalement the @LayoutManager and  @AdapterSteps
 * <p>
 * <p>
 * it will be called from the class @FragmentDetails
 * will take  a list of @ingredient that will be passed to the adapter
 * and
 * exoPlayer object that will be used to play the 'step' video
 */

public class FragmentStep extends Fragment implements AdapterSteps.stepListListeners {

    Context mContext;
    List<Step> mStepsList = new ArrayList<>();
    AdapterSteps adapter;
    RecyclerView.LayoutManager manager;
    Parcelable StepState;
    mFragmentDetailListeners detailListeners;


    @BindView(R.id.rv_step)
    RecyclerView mStepsView;

    public FragmentStep() {

    }


    public FragmentStep(List<Step> s, mFragmentDetailListeners lis) {
        this.mStepsList = s;
        detailListeners = lis;
    }

    public void setDetailListeners(mFragmentDetailListeners lis) {
        detailListeners = lis;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mContext = rootView.getContext();
        ButterKnife.bind(this, rootView);
        setLayoutManger();
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            StepState = savedInstanceState.getParcelable(getString(R.string.step_state));
            mStepsList = savedInstanceState.getParcelableArrayList(getString(R.string.step_list));
        }
        adapter.UpdateStepsList(mStepsList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (manager != null) {
            manager.onRestoreInstanceState(StepState);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        StepState = manager.onSaveInstanceState();
        outState.putParcelable(getString(R.string.step_state), StepState);
        outState.putParcelableArrayList(getString(R.string.step_list), (ArrayList<? extends Parcelable>) mStepsList);
    }

    private void setLayoutManger() {
        /*
        *set layoutManager and steps Adapter
        *
        * and update adapter list with the passed date from RecipeDetailsContainer
        *
        * */

        manager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        if (StepState != null) {
            manager.onRestoreInstanceState(StepState);
        }
        adapter = new AdapterSteps(this);
        mStepsView.setAdapter(adapter);
        mStepsView.setLayoutManager(manager);
        mStepsView.setHasFixedSize(true);
        adapter.UpdateStepsList(mStepsList);
    }

    @Override
    public void onStepClick(Step s) {
        /*
        *
        * override from the @AdapterSteps.StepsClickListener
        * to create a toast that will display the recipe description in case that
        * the content was not completely available, in it's textView
        *
        * */
        Toast.makeText(mContext, "" + s.description, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onVideoLoadClick(String uri, Boolean isImage) {
        detailListeners.onChangePlayerVideo(uri, isImage);
    }

    public interface mFragmentDetailListeners {
        void onChangePlayerVideo(String uri, Boolean isImage);
    }
}
