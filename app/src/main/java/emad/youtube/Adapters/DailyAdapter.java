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

import emad.youtube.DisplayImageActivity;
import emad.youtube.R;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder>{

    Context context;
    ArrayList allURLS;
    Intent intent;

    public DailyAdapter(Context context, ArrayList allURLS) {
        this.context = context;
        this.allURLS = allURLS;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.daily_item_layout, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        try{
            Picasso.get().load(allURLS.get(i).toString()).into(holder.imgDaily);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.imgDaily);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, DisplayImageActivity.class);
                intent.putExtra("image", allURLS.get(i).toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allURLS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RoundedCornerImageView imgDaily;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgDaily = itemView.findViewById(R.id.imgDaily);

        }
    }

}
