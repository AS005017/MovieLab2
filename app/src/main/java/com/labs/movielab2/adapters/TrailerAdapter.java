package com.labs.movielab2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.movielab2.R;
import com.labs.movielab2.models.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter <TrailerAdapter.MyViewHolder> {

    private TrailerItemClickListener trailerItemClickListener;
    private Context context ;
    private List<Trailer> mList;

    public TrailerAdapter(Context context, List<Trailer> mList, TrailerItemClickListener trailerItemClickListener) {
        this.trailerItemClickListener = trailerItemClickListener;
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.MyViewHolder holder, int position) {
        holder.typeVideo.setText(mList.get(position).getType());
        holder.sizeVideo.setText(String.valueOf(mList.get(position).getSize()));
        holder.nameVideo.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView typeVideo;
        private TextView nameVideo;
        private TextView sizeVideo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            typeVideo = itemView.findViewById(R.id.type_video);
            nameVideo = itemView.findViewById(R.id.video_name);
            sizeVideo = itemView.findViewById(R.id.size_video);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailerItemClickListener.onTrailerClick(mList.get(getAdapterPosition()));
                }
            });
        }
    }
}
