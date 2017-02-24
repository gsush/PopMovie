package com.example.android.popmovie.Fragment;

/**
 * Created by user on 2/21/2017.
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovie.Extras;
import com.example.android.popmovie.MovieList;
import com.example.android.popmovie.Network.FetchReviewsTask;
import com.example.android.popmovie.Network.FetchTrailersTask;
import com.example.android.popmovie.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment implements FetchTrailersTask.Event,FetchReviewsTask.Event,View.OnClickListener {
    View.OnClickListener monClickListener;
    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    private static final boolean DEBUG = false; // Set this to false to disable logs.
    private Extras extras;

    public DetailMovieFragment() {
        setHasOptionsMenu(true);
    }

    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolBar;
    @BindView(R.id.title) TextView mMovieTitle;
    @BindView(R.id.poster) ImageView mMoviePoster;
    @BindView(R.id.backposter) ImageView mBackPoster;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.rating) TextView mRatingAverage;
    @BindView(R.id.synopsis) TextView mSynopsis;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.trailers_header)     TextView txtViewTrailersHeader;
    @BindView(R.id.trailers_container) HorizontalScrollView scrollViewTrailers;
    @BindView(R.id.trailers)            ViewGroup viewTrailers;
    @BindView(R.id.reviews_header) TextView textViewReviewHeader;
    @BindView(R.id.reviews) ViewGroup viewReviews;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final MovieList movie = getActivity().getIntent().getParcelableExtra(MovieFragment.EXTRA_MOVIE_DATA);


        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());
        ButterKnife.bind(this, rootView);

        mMovieTitle.setText(movie.getTitle());
        Picasso.with(getContext())
                .load(movie.getImageurl())
                .placeholder(R.drawable.square_placeholder)
                .error(R.drawable.image_error)
                .into(mMoviePoster);
        Picasso.with(getContext())
                .load((movie.getBackPoster()))
                .placeholder(R.drawable.square_placeholder)
                .error(R.drawable.image_error)
                .into(mBackPoster);
//            Glide.with(getActivity())
//                    .load(movie.getImageurl())
//                    //.asBitmap()
//                    .crossFade()
//                    .placeholder(R.drawable.image)
//                    .error(R.drawable.image_error)
//                    .into(mMoviePoster);
        mReleaseDate.setText("Released:\n" + movie.getRelease_date());
        mRatingAverage.setText(String.valueOf("Rating:\n" + movie.getRating() + "/10"));
        mSynopsis.setText("SYNOPSIS:\n" + movie.getSynopsis());//https://www.youtube.com/watch?v=wRaV4SIQY8A
        FetchTrailersTask fetchTrailersTask= new FetchTrailersTask(getContext(),this);
        FetchReviewsTask fetchReviewsTask= new FetchReviewsTask(getActivity(),this);
        fetchTrailersTask.execute(movie.getId());
        fetchReviewsTask.execute(movie.getId());

        return rootView;
    }

    public void onTrailersFetch(Extras extras) {
        showTrailers(extras);
    }
    public void onReviewsFetch(Extras extras) {
        showReviews(extras);
    }

    private void showTrailers(Extras extras) {
        int numTrailers = extras.getTrailersNum();
        boolean hasTrailers = numTrailers>0;
        txtViewTrailersHeader.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        scrollViewTrailers.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        if (hasTrailers) {
            addTrailers(extras);
        }
    }

    private void addTrailers(Extras extras) {
        this.extras=extras;
        int numTrailers = extras.getTrailersNum();
        viewTrailers.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Picasso picasso = Picasso.with(getActivity());
        for(int i=0; i<numTrailers;i++) {
            String TRAILER_THUMB_BASE_URL= getActivity().getString(R.string.trailer_thumb_base_url);
            String TRAILER_BASE_URL=getActivity().getString(R.string.trailer_base_url);
            String trailer_key=extras.getTrailerAtIndex((int) i).getSource();
            String trailer_url= TRAILER_BASE_URL + trailer_key;
            ViewGroup thumbContainer = (ViewGroup) inflater.inflate(R.layout.video, viewTrailers,
                    false);
            ImageView thumbView = (ImageView) thumbContainer.findViewById(R.id.video_thumb);
            thumbView.setTag(trailer_url);
            thumbView.setOnClickListener(this);

            picasso
                    .load(TRAILER_THUMB_BASE_URL + trailer_key + "/0.jpg")
                    .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                    .placeholder(R.drawable.square_placeholder)
                    .centerCrop()
                    .into(thumbView);
            viewTrailers.addView(thumbContainer);
            //favShare.setOnClickListener(this);
        }
    }
    private void showReviews(Extras extras){
        int numReviews = extras.getReviewsNum();
        boolean hasReviews = numReviews>0;
        textViewReviewHeader.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        viewReviews.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        if (hasReviews) {
            addReviews(extras);
        }
    }
    private void addReviews(final Extras extras) {
        int numReviews = extras.getReviewsNum();
        viewReviews.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i = 0; i < numReviews; i++) {
            ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review_card, viewReviews,
                    false);
            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.review_author);
            TextView reviewContent = (TextView) reviewContainer.findViewById(R.id.review_content);
            reviewAuthor.setText(extras.getReviewAtIndex(i).getAuthor());
            reviewContent.setText(extras.getReviewAtIndex(i).getBody().replace("\n\n", " ").replace("\n", " "));
            reviewContainer.setOnClickListener(this);
            reviewContainer.setTag(extras.getReviewAtIndex(i));
            viewReviews.addView(reviewContainer);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//       unbinder.unbind();
    }

    //FOLLOWING FUNCTIONS FOR DEBUGGING PURPOSE ONLY.
    @Override
    public void onStart() {
        if (DEBUG) Log.i(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.i(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.i(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.i(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.video_thumb){
            String videoUrl = (String) view.getTag();
            try{
                Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(playVideoIntent);
            }catch (ActivityNotFoundException ex){
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(intent);
            }
        }
        else return;
    }
}

