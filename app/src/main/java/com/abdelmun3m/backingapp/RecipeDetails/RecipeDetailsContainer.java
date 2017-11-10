package com.abdelmun3m.backingapp.RecipeDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.abdelmun3m.backingapp.MainActivity;
import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;

import butterknife.ButterKnife;


/*
*
* an Activity that display the content of a recipe
* contains and handle the fragment that manage Ingredient and Step Recycler View
* handle ExoPlayer
* handle Media session
*
*
*
* */
public class RecipeDetailsContainer extends AppCompatActivity {


    private static final java.lang.String TAG = "RecipeDetailsContainer" ;
  /*  @Nullable
    @BindView(R.id.tv_detail_recipe_name)
    TextView mRecipeName;
    @Nullable
    @BindView(R.id.iv_details_recipe_img)
    ImageView mRecipeImage;
    @Nullable
    @BindView(R.id.player_view)
    SimpleExoPlayerView myExoPlayerView;
    @Nullable
    @BindView(R.id.pb_player_progress)
    ProgressBar mPlayerProgress;

    @Nullable
    @BindView(R.id.img_favorit_button)
    ImageView mFavoriteButton;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    ;

    SimpleExoPlayer mExoPlayer;*/
  Recipe mRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);




        //get the recipe object sellected from the user in the main page
        Intent in = getIntent();
        mRecipe = in.getParcelableExtra(MainActivity.RECIPE_INTENT_KEY);

        if(mRecipe != null){

            FragmentDetails mdetails = new FragmentDetails(mRecipe);
            getFragmentManager().beginTransaction().add(R.id.mainDetails,mdetails).commit();

        }else {
            Toast.makeText(this, "Null intent", Toast.LENGTH_SHORT).show();
        }
/*

       String mFavoriteRecipeId = NewAppWidget.getMyFavoriteRecipeId();
        if(mFavoriteRecipeId != null && mFavoriteRecipeId.equals(mRecipe.Id)){

          setFavoriteOn(true);

        }else{
            setFavoriteOn(false);
        }

        mRecipeName.setText(mRecipe.name);
        mRecipeImage.setImageResource(mRecipe.imageId);
        FragmentManager fragmentManager = getFragmentManager();


        //create fragment ingredient and pass the ingredient of the current recipe
        FragmentIngredient ingredient = new FragmentIngredient(mRecipe.ingredients);
        fragmentManager.beginTransaction().add(R.id.container_ingredient,ingredient).commit();


        //intiate and handle videos
        initializeMediaSession();
        initializeMediaPlayer(this);

        //set the default image of the Exoplayer with the recipe image displayed in the main avtivity
        myExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), mRecipe.imageId));

        //create fragment ingredient and pass the steps of the current recipe and pass exoplayer
        FragmentStep steps = new FragmentStep(mRecipe.steps,this.mExoPlayer);
        fragmentManager.beginTransaction().add(R.id.container_steps,steps).commit();
*/
    }

/*
    private void initializeMediaPlayer(Context context) {


        if(mExoPlayer == null){
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context,selector,control);
            myExoPlayerView.setPlayer(mExoPlayer);


        }

    }

   private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

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
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
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
    public void setAsFavorite(View view){
        setFavoriteOn(true);
        NewAppWidget.setMyfavoriteRecipe(mRecipe);

    }

    private void setFavoriteOn(boolean favorite){

        if(favorite){
            mFavoriteButton.setImageResource(android.R.drawable.star_big_on);
        }else {
            mFavoriteButton.setImageResource(android.R.drawable.star_big_off);
        }

    }
**/
}
