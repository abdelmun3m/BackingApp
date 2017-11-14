package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;
import com.abdelmun3m.backingapp.widget.WidgetIntentService;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abdelmun3m on 07/11/17.
 *
 * FragmentDetails Class is a class for the Details fragment
 * which displayed in the Details Activity
 *
 * it accepts a Recipe Object as parameter to display its data
 *
 * it can be initiated  from IngredientDetailsContainer when click on recipe.
 *
 * it can be intiated from MainActivity in tablet screen size
 *
 * it can be initiated from NewAppWidget when widget click.
 *
 *
 */

public class FragmentDetails extends Fragment implements ExoPlayer.EventListener {



    private final java.lang.String TAG = "RecipeDetailsContainer" ;

    @BindView(R.id.tv_detail_recipe_name)
    TextView mRecipeName;

    @BindView(R.id.iv_details_recipe_img)
    ImageView mRecipeImage;

    @BindView(R.id.player_view)
    SimpleExoPlayerView myExoPlayerView;

    @BindView(R.id.pb_player_progress)
    ProgressBar mPlayerProgress;

    @BindView(R.id.img_favorit_button)
    ImageView mFavoriteButton;


    //defined static couse it is used in static class MediaReceiver
    private static MediaSessionCompat mMediaSession;

    private PlaybackStateCompat.Builder mStateBuilder;

    private Recipe mRecipe;

    private View rootView;
    private Context mContext;


    private SimpleExoPlayer mExoPlayer;

    public FragmentDetails(Recipe m) {
        /**
         * Constructor to intiate the fragment ant set Recipe
         * */
        this.mRecipe = m ;
    }

    public  FragmentDetails(){
    // default instructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_details,container,false);
        mContext = rootView.getContext();

        //using ButterKnife Library and bind view
        ButterKnife.bind(this,rootView);

        if(mRecipe ==null){

            // if the object intiated with null object it will return an empty view of the fragment_details.xml
            return rootView;
        }


        setFavoriteStar();


        //set the detail Header Part
        mRecipeName.setText(mRecipe.name);
        mRecipeImage.setImageResource(mRecipe.imageId);



        FragmentManager fragmentManager = getFragmentManager();



        //ToDO Test this new Update of .add ---> .replace
        //create fragment ingredient and pass the ingredient of the current recipe
        FragmentIngredient ingredient = new FragmentIngredient(mRecipe.ingredients);
        fragmentManager.beginTransaction().replace(R.id.container_ingredient,ingredient).commit();


        //create fragment ingredient and pass the steps of the current recipe and pass exoplayer
        FragmentStep steps = new FragmentStep(mRecipe.steps,this.mExoPlayer);
        fragmentManager.beginTransaction().replace(R.id.container_steps,steps).commit();


        //intiate and handle videos
        initializeMediaSession();
        initializeMediaPlayer(mContext);

        //set the default image of the Exoplayer with the recipe image displayed in the main avtivity
        myExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), mRecipe.imageId));



        return rootView;
    }


    private void setFavoriteStar(){
        /**
         *
         * this function check the Recipe Used in the Widget to
         * mark the current displayed recipe as favorite
         * */


        String mFavoriteRecipeId = WidgetIntentService.curRecipe != null ?
                WidgetIntentService.curRecipe.Id:"";
        if(mFavoriteRecipeId != null && mFavoriteRecipeId.equals(mRecipe.Id)){
            setFavoriteOn(true);
        }else{
            setFavoriteOn(false);
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the recipe object sellected from the user in the main page

    }


    private void initializeMediaPlayer(Context context) {

        // set Exoplayer Controllers
        if(mExoPlayer == null){
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context,selector,control);
            myExoPlayerView.setPlayer(mExoPlayer);
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        //TODO Controle ProgressPar in Playing Videos
        if(!isLoading){
            mPlayerProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(mMediaSession != null){
            mMediaSession.setActive(false);
        }

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @OnClick(R.id.img_favorit_button)
    public void setAsFavorite(View view) {
        setFavoriteOn(true);

        WidgetIntentService.UpdateWidgetRecipe(mContext, mRecipe);
    }


    private void setFavoriteOn(boolean favorite){


        //change the star image according to widget recipe and current recipe
        if(favorite){
            mFavoriteButton.setImageResource(android.R.drawable.star_big_on);
        }else {
            mFavoriteButton.setImageResource(android.R.drawable.star_big_off);
        }

    }


    public static class MediaReceiver extends BroadcastReceiver {


        /*
        *
        * BreadCast Receiver to handel media controls
        *
        * **/
        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

}
