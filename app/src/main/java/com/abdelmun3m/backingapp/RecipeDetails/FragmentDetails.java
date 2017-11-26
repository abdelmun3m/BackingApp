package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.RecipeList.FragmentRecipeList;
import com.abdelmun3m.backingapp.Utils.Recipe;
import com.abdelmun3m.backingapp.widget.WidgetIntentService;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abdelmun3m on 07/11/17.
 * <p>
 * FragmentDetails Class is a class for the Details fragment
 * which displayed in the Details Activity
 * <p>
 * it accepts a Recipe Object as parameter to display its data
 * <p>
 * it can be initiated  from IngredientDetailsContainer when click on recipe.
 * <p>
 * it can be intiated from MainActivity in tablet screen size
 * <p>
 * it can be initiated from RecipeWidgetProvider when widget click.
 */

public class FragmentDetails extends Fragment implements ExoPlayer.EventListener,
        FragmentStep.mFragmentDetailListeners {


    private final java.lang.String TAG = "RecipeDetailsContainer";

    @BindView(R.id.tv_detail_recipe_name)
    TextView mRecipeName;

    @BindView(R.id.iv_details_recipe_img)
    ImageView mRecipeImage;

    @BindView(R.id.player_view)
    SimpleExoPlayerView myExoPlayerView;

    @BindView(R.id.img_favorit_button)
    ImageView mFavoriteButton;


    @BindView(R.id.image_view)
    ImageView imageView;


    //defined static couse it is used in static class MediaReceiver
    private static MediaSessionCompat mMediaSession;

    private PlaybackStateCompat.Builder mStateBuilder;

    private Recipe mRecipe;

    private View rootView;
    private Context mContext;

    long position = 0;
    String videoUri = "";
    Parcelable state;
    FragmentIngredient ingredient;
    FragmentStep steps;


    private SimpleExoPlayer mExoPlayer;

    public FragmentDetails(Recipe m) {
        /**
         * Constructor to intiate the fragment ant set Recipe
         * */
        this.mRecipe = m;
    }


    public FragmentDetails() {
        // default instructor
    }

    public FragmentDetails(Recipe mRecipe, Parcelable parcelable) {
        this.mRecipe = mRecipe;
        this.state = parcelable;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the recipe object sellected from the user in the main page

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mContext = rootView.getContext();
        FragmentManager fragmentManager = getFragmentManager();
        ButterKnife.bind(this, rootView);

        //intiate and handle videos

        if (savedInstanceState != null) {
            videoUri = savedInstanceState.getString(getString(R.string.uri));
            position = savedInstanceState.getLong(getString(R.string.position));
            ingredient = (FragmentIngredient) getFragmentManager().findFragmentByTag(getString(R.string.ingredient_fragment));
            steps = (FragmentStep) getFragmentManager().findFragmentByTag(getString(R.string.step_fragment));
            steps.setDetailListeners(this);
            //  setMediaSource(videoUri);
        } else {
            if (mRecipe == null) {
                // if the object intiated with null object it will return an empty view of the fragment_details.xml
                return rootView;
            }

            //create fragment ingredient and pass the ingredient of the current recipe
            ingredient = new FragmentIngredient(mRecipe.ingredients);
            fragmentManager.beginTransaction().replace(R.id.container_ingredient, ingredient, getString(R.string.ingredient_fragment)).commit();

            //create fragment ingredient and pass the steps of the current recipe and pass exoplayer
            steps = new FragmentStep(mRecipe.steps, this);
            fragmentManager.beginTransaction().replace(R.id.container_steps, steps, getString(R.string.step_fragment)).commit();

            setFavoriteStar();

        }
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            videoUri = savedInstanceState.getString(getString(R.string.uri));
            position = savedInstanceState.getLong(getString(R.string.position), 0);
            mRecipe = (Recipe) savedInstanceState.getParcelable(getString(R.string.recipe));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mRecipeName.setText(mRecipe.name);
        if (mRecipe.imageUrl == null || mRecipe.imageUrl.equals("")) {
            mRecipeImage.setImageResource(mRecipe.imageId);
        } else {

            mRecipe.loadMovieImage(mRecipe.imageUrl, mRecipeImage);
        }
        initializeMediaSession();
        initializeMediaPlayer(mContext);
        //initializeMediaPlayer(mContext);
        setMediaSource(videoUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.uri), videoUri);
        outState.putLong(getString(R.string.position), position);
        outState.putParcelable(getString(R.string.recipe), mRecipe);
    }

    @OnClick(R.id.img_favorit_button)
    public void setAsFavorite(View view) {
        setFavoriteOn(true);
        WidgetIntentService.UpdateWidgetRecipe(mContext, mRecipe);
    }

    private void setFavoriteStar() {
        /**
         *
         * this function check the Recipe Used in the Widget to
         * mark the current displayed recipe as favorite
         * */


        String mFavoriteRecipeId = WidgetIntentService.curRecipe != null ?
                WidgetIntentService.curRecipe.Id : "";
        if (mFavoriteRecipeId != null && mFavoriteRecipeId.equals(mRecipe.Id)) {
            setFavoriteOn(true);
        } else {
            setFavoriteOn(false);
        }

    }


    private void setFavoriteOn(boolean favorite) {

        mFavoriteButton.setImageResource(favorite ? android.R.drawable.star_big_on : android.R.drawable.star_big_off);

    }

    private void initializeMediaPlayer(Context context) {
        // set Exoplayer Controllers
        if (mExoPlayer == null) {
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, selector, control);
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
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

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

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else {

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
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }


    private void setMediaSource(String uri) {
        /*
        *
        * change the media Source in the ExoPlayer Object
        *
        * **/
        if (mExoPlayer != null) {
            Uri mURi = Uri.parse(uri).buildUpon().build();
            String userAgent = Util.getUserAgent(mContext, "BackingApp");
            DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource =
                    new ExtractorMediaSource(
                            mURi,
                            defaultDataSourceFactory,
                            new DefaultExtractorsFactory(),
                            null,
                            null);
            mExoPlayer.seekTo(position);
            mExoPlayer.prepare(mediaSource);
        }
    }

    @Override
    public void onChangePlayerVideo(String uri, Boolean isImage) {
        if (uri != null && !isImage) {

            myExoPlayerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            videoUri = uri;
            setMediaSource(uri);
        } else if (uri != null && isImage) {
            myExoPlayerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            mRecipe.loadMovieImage(uri, imageView);
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


        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
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
