package emad.youtube.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.comix.rounded.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emad.youtube.Iterfaces.ChangeVideo;
import emad.youtube.Model.VideoList.ItemsListData;
import emad.youtube.R;

public class RestVideosAdapter extends RecyclerView.Adapter<RestVideosAdapter.MyViewHolder>{
    Context context;
    ArrayList<ItemsListData> mVideosList;
    ChangeVideo changeVideo;
    public RestVideosAdapter(Context context, ArrayList<ItemsListData> mPlayLists, ChangeVideo changeVideo) {
        this.context = context;
        this.mVideosList = mPlayLists;
        this.changeVideo = changeVideo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.next_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
     try {   Picasso.get().load(mVideosList.get(i).getSnippet().getThumbnails().getMedium().getUrl()).into(holder.imgPlaylistItem);
    }catch (Exception ex){
        Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
    }
     holder.namePlaylistItem.setText(mVideosList.get(i).getSnippet().getTitle());
     holder.publishedAt.setText(mVideosList.get(i).getSnippet().getPublishedAt().substring(0,10 ));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVideo.videoChanged(mVideosList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedCornerImageView imgPlaylistItem;
        TextView namePlaylistItem;
        TextView publishedAt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaylistItem = itemView.findViewById(R.id.imgPlaylistItem);
            namePlaylistItem = itemView.findViewById(R.id.namePlaylistItem);
            publishedAt = itemView.findViewById(R.id.publishedAt);
        }
    }
}
