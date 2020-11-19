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
import com.labs.movielab2.models.Cast;

import java.util.List;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.MyViewHolder> {

    Context context ;
    List<Cast> mList;

    public CreditsAdapter(Context context, List<Cast> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.creditName.setText(mList.get(position).getName());
        if (!mList.get(position).getProfile_path().equals("https://image.tmdb.org/t/p/w500null")) {
            Glide.with(context)
                    .load(mList.get(position).getProfile_path())
                    .placeholder(R.drawable.empty_profile)
                    .into(holder.creditPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView creditName;
        private ImageView creditPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            creditName = itemView.findViewById(R.id.item_movie_title);
            creditPhoto = itemView.findViewById(R.id.item_movie_img);
        }
    }
}
