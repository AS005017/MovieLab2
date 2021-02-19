package com.labs.movielab2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.labs.movielab2.R;
import com.labs.movielab2.models.MovieDetail;

import java.util.List;

public class FavoritesAdapter  extends RecyclerView.Adapter<FavoritesAdapter.MyNewViewHolder> {

    private Context context ;
    private List<MovieDetail> mList;
    private FavoriteMovieClickListener movieItemClickListener;


    public FavoritesAdapter(Context context, List<MovieDetail> mList, FavoriteMovieClickListener movieItemClickListener) {
        this.context = context;
        this.mList = mList;
        this.movieItemClickListener = movieItemClickListener;
    }


    @NonNull
    @Override
    public MyNewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slide_item,viewGroup,false);
        return new MyNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.MyNewViewHolder holder, int position) {
        if (mList.get(position).isAdult()) {
            holder.adultslide.setVisibility(View.VISIBLE);
        } else  {
            holder.adultslide.setVisibility(View.INVISIBLE);
        }
        double voteaver = mList.get(position).getVoteAverage()*10;
        holder.tvRate.setText(Math.round(voteaver)+ "%");

        holder.tvTitle.setText(mList.get(position).getTitle());
        Glide.with(context)
                .load(mList.get(position).getBackdropPath())
                .placeholder(R.drawable.loading)
                .into(holder.imgMovie);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyNewViewHolder extends RecyclerView.ViewHolder {


        private TextView tvTitle, tvRate;
        private ImageView imgMovie,adultslide;


        public MyNewViewHolder(@NonNull View itemView) {

            super(itemView);
            tvTitle = itemView.findViewById(R.id.slide_title);
            tvRate = itemView.findViewById(R.id.sliderate);
            imgMovie = itemView.findViewById(R.id.slide_img);
            adultslide = itemView.findViewById(R.id.adultslide);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movieItemClickListener.onMovieClick(mList.get(getAdapterPosition()), imgMovie);


                }
            });

        }
    }
}
