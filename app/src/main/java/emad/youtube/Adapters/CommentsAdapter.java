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

import de.hdodenhof.circleimageview.CircleImageView;
import emad.youtube.Iterfaces.CommentsInterface;
import emad.youtube.Model.Comment.CoreSnippet;
import emad.youtube.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    ArrayList<CoreSnippet> snippetsList;
    Context context;
    public CommentsAdapter(ArrayList<CoreSnippet> snippetsList, Context context) {
        this.snippetsList = snippetsList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.comment_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        try {
            Picasso.get().load(snippetsList.get(i).getAuthorProfileImageUrl()).into(holder.commentAuthorImg);
        }catch (Exception ex){
            Picasso.get().load(context.getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(holder.commentAuthorImg);
        }
        holder.authorComment.setText(snippetsList.get(i).getTextDisplay());
        holder.authorName.setText(snippetsList.get(i).getAuthorDisplayName());
        holder.commentTime.setText(snippetsList.get(i).getPublishedAt().substring(0,10));

    }

    @Override
    public int getItemCount() {
        return snippetsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView commentAuthorImg;
        TextView authorName;
        TextView commentTime;
        TextView authorComment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthorImg = itemView.findViewById(R.id.commentAuthorImg);
            authorName = itemView.findViewById(R.id.authorName);
            commentTime = itemView.findViewById(R.id.commentTime);
            authorComment = itemView.findViewById(R.id.authorComment);
        }
    }
}
