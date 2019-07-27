package emad.youtube.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comix.rounded.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emad.youtube.Model.Latest.Latest;
import emad.youtube.PlayVideoActivity;
import emad.youtube.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    ArrayList<Latest> searchList;
    Context context;
    Intent intent;

    public SearchAdapter(ArrayList<Latest> searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.next_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        try{
            Picasso.get().load(searchList.get(i).getUrl()).into(holder.imgPlaylistItem);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgPlaylistItem);
        }

        holder.namePlaylistItem.setText(searchList.get(i).getTitle());
        holder.publishedAt.setText(searchList.get(i).getPublishedAt().substring(0,10));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("type" , "latest");
                intent.putExtra("currentVideo", searchList.get(i));
                intent.putExtra("allList", searchList);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RoundedCornerImageView imgPlaylistItem;
        public TextView namePlaylistItem;
        public TextView publishedAt;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgPlaylistItem = itemView.findViewById(R.id.imgPlaylistItem);
            namePlaylistItem = itemView.findViewById(R.id.namePlaylistItem);
            publishedAt = itemView.findViewById(R.id.publishedAt);
        }
    }
}
