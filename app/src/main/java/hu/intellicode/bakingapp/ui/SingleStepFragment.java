package hu.intellicode.bakingapp.ui;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.helper.RecipeData;
import hu.intellicode.bakingapp.models.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleStepFragment extends Fragment {

    private static final String TAG = "SingleStepFragment";
    private ArrayList<Step> steps;
    private Bundle args;
    private TextView stepDescription;
    private Step step = new Step();
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private View rootView;
    private ImageView thumbnailView;
    Uri videoUri;
    boolean playWhenReady;
    long videoPosition = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public SingleStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_single_step, container, false);
        stepDescription = rootView.findViewById(R.id.tv_step_description);
        exoPlayerView = rootView.findViewById(R.id.exoplayer_view);
        thumbnailView = rootView.findViewById(R.id.video_imageview);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getLong("VIDEO_POSITION");
            playWhenReady = savedInstanceState.getBoolean("PLAY_WHEN_READY");
        }

        args = getArguments();
        if (getArguments() != null) {
            steps = args.getParcelableArrayList("STEPS");
            updateStepView(RecipeData.stepIndex);
        }

        return rootView;
    }

    public void updateStepView(int position) {

        step = steps.get(position);
        stepDescription.setText(step.getDescription());

        if (step.getVideoURL().equals("")) {
            exoPlayerView.setVisibility(View.GONE);
            thumbnailView.setVisibility(View.VISIBLE);
            Context context = rootView.getContext();
            if (step.getThumbnailURL().equals("")) {
                thumbnailView.setImageDrawable(context.getResources().getDrawable(R.drawable.cupcake));
            } else {
                Picasso.with(context)
                        .load(step.getThumbnailURL())
                        .placeholder(context.getResources().getDrawable(R.drawable.cupcake))
                        .error(context.getResources().getDrawable(R.drawable.cupcake))
                        .into(thumbnailView);
            }
        } else {
            exoPlayerView.setVisibility(View.VISIBLE);
            thumbnailView.setVisibility(View.GONE);
//            initializeMediaSession();
            videoUri = Uri.parse(step.getVideoURL());
            initializePlayer(videoUri);
        }
        RecipeData.stepIndex = position;
    }

    public void setListIndex(int index) {
        RecipeData.stepIndex = index;
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the video to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(rootView.getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(rootView.getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    rootView.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(videoPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) releasePlayer();
    }
    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        if (mExoPlayer != null) {
            currentState.putLong("VIDEO_POSITION", mExoPlayer.getCurrentPosition());
            currentState.putBoolean("PLAY_WHEN_READY", mExoPlayer.getPlayWhenReady());
        }
    }
}