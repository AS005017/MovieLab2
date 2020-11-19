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
import com.labs.movielab2.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    Context context ;
    List<Movie> mList;
    MovieItemClickListener movieItemClickListener;


    public MovieAdapter(Context context, List<Movie> mList, MovieItemClickListener movieItemClickListener) {
        this.context = context;
        this.mList = mList;
        this.movieItemClickListener = movieItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie,viewGroup,false);
        return new MyViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {


        myViewHolder.tvTitle.setText(mList.get(position).getTitle());
        Glide.with(context)
                .load(mList.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .into(myViewHolder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView tvTitle;
        private ImageView imgMovie;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_movie_title);
            imgMovie = itemView.findViewById(R.id.item_movie_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movieItemClickListener.onMovieClick(mList.get(getAdapterPosition()), imgMovie);


                }
            });

        }
    }

    public class MyNewViewHolder {
    }
}
