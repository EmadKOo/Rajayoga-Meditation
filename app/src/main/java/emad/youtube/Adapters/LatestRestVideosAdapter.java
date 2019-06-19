package emad.youtube.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emad.youtube.Iterfaces.ChangeVideo;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.R;

public class LatestRestVideosAdapter extends RecyclerView.Adapter<LatestRestVideosAdapter.MyViewHolder>{

    Context context;
    ArrayList<Latest> latestArrayList;
    ChangeVideo changeVideo;

    public LatestRestVideosAdapter(Context context, ArrayList<Latest> latestArrayList, ChangeVideo changeVideo) {
        this.context = context;
        this.latestArrayList = latestArrayList;
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
        try {
            Picasso.get().load(latestArrayList.get(i).getUrl()).into(holder.imgPlaylistItem);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
        }
        holder.namePlaylistItem.setText(latestArrayList.get(i).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVideo.latestVideoChanged(latestArrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return latestArrayList.size();
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
