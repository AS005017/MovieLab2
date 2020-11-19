package com.labs.movielab2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.labs.movielab2.R;
import com.labs.movielab2.models.Movie;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    MovieItemClickListener movieItemClickListener;
    private Context mContext ;
    private List<Movie> mList ;

    public SliderPagerAdapter(Context mContext, List<Movie> mList, MovieItemClickListener movieItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.movieItemClickListener = movieItemClickListener;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item,null);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);
        ImageView slideAdult = slideLayout.findViewById(R.id.adultslide);
        TextView slideRate = slideLayout.findViewById(R.id.sliderate);

        Glide.with(mContext)
                .load(mList.get(position).getBackdropPath())
                .placeholder(R.drawable.loading)
                .into(slideImg);
        slideText.setText(mList.get(position).getTitle());
        if (mList.get(position).isAdult()) {
            slideAdult.setVisibility(View.VISIBLE);
        } else  {
            slideAdult.setVisibility(View.INVISIBLE);
        }
        double voteaver = mList.get(position).getVoteAverage()*10;
        slideRate.setText(Math.round(voteaver)+ "%");

        container.addView(slideLayout);
        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            movieItemClickListener.onMovieClick(mList.get(position),slideImg);
            }
        });
        return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
