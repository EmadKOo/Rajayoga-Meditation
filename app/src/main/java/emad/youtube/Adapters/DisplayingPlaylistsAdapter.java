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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import emad.youtube.Model.PlayLists.PlayListItems;
import emad.youtube.R;
import emad.youtube.ViewPlaylistVideosActivity;

public class DisplayingPlaylistsAdapter extends RecyclerView.Adapter<DisplayingPlaylistsAdapter.MyViewHolder>{
    Context context;
    ArrayList<PlayListItems> mPlayLists;
    Intent intent;
    public DisplayingPlaylistsAdapter(Context context, ArrayList<PlayListItems> mPlayLists) {
        this.context = context;
        this.mPlayLists = mPlayLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.playlist_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        try{
            Picasso.get().load(mPlayLists.get(i).getSnippet().getThumbnails().getHigh().getUrl()).into(holder.imgPlaylistItem);
         }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
        }
        holder.namePlaylistItem.setText(mPlayLists.get(i).getSnippet().getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, ViewPlaylistVideosActivity.class);
                intent.putExtra("playlist", mPlayLists.get(i));
                intent.putExtra("type", "usual");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlaylistItem;
        TextView namePlaylistItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaylistItem = itemView.findViewById(R.id.imgPlaylistItem);
            namePlaylistItem = itemView.findViewById(R.id.namePlaylistItem);
        }
    }
}
