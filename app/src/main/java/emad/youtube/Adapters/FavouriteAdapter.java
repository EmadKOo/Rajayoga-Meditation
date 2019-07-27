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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.PlayingVideoActivity;
import emad.youtube.R;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>{

    ArrayList<Favourite> favouriteList;
    Context context;
    Intent intent;
    DatabaseReference favRef;
    FirebaseAuth mAuth;

    public FavouriteAdapter(ArrayList<Favourite> favouriteList, Context context) {
        this.favouriteList = favouriteList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.favourite_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        try{

            Picasso.get().load(favouriteList.get(i).getUrl().toString()).into(holder.imgFav);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgFav);
        }
        holder.titleFav.setText(favouriteList.get(i).getTitle());

        holder.favIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove node from firebase and refresh adapter also you can use interface
                favRef = FirebaseDatabase.getInstance().getReference().child("fav").child(mAuth.getCurrentUser().getUid()).child(favouriteList.get(i).getVideoID());
                favRef.removeValue();
                favRef = FirebaseDatabase.getInstance().getReference().child("fav").child(mAuth.getCurrentUser().getUid()).child(favouriteList.get(i).getVideoID());
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, PlayingVideoActivity.class);
                intent.putExtra("type" , "favourites");
                intent.putExtra("CurrentVideo" , favouriteList.get(i));
                intent.putExtra("favList", favouriteList);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RoundedCornerImageView imgFav;
        public ImageView favIco;
        public TextView titleFav;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgFav = itemView.findViewById(R.id.imgFav);
            titleFav = itemView.findViewById(R.id.titleFav);
            favIco = itemView.findViewById(R.id.favIco);
        }
    }
}
