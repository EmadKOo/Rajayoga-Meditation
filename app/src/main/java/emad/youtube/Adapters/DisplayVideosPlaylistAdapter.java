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

import emad.youtube.Model.VideoList.ItemsListData;
import emad.youtube.PlayVideoActivity;
import emad.youtube.R;

public class DisplayVideosPlaylistAdapter extends RecyclerView.Adapter<DisplayVideosPlaylistAdapter.MyViewHolder>{

    Context context;
    ArrayList<ItemsListData> mVideosList;
    Intent intent;
    public DisplayVideosPlaylistAdapter(Context context, ArrayList<ItemsListData> mPlayLists) {
        this.context = context;
        this.mVideosList = mPlayLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.playlist_item_grid, viewGroup, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        try {
            Picasso.get().load(mVideosList.get(i).getSnippet().getThumbnails().getMedium().getUrl()).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
        }
        holder.namePlaylistItem.setText(mVideosList.get(i).getSnippet().getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("type" , "original");
                intent.putExtra("currentVideo", mVideosList.get(i));
                intent.putExtra("allList", mVideosList);
                context.startActivity(intent);
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaylistItem = itemView.findViewById(R.id.imgPlaylistItem);
            namePlaylistItem = itemView.findViewById(R.id.namePlaylistItem);
        }
    }
}
