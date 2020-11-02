package com.example.ulakdemo.ui.auth.main.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ulakdemo.R;
import com.example.ulakdemo.model.Comment;


import java.util.ArrayList;
import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Comment> comments = new ArrayList<>();
    private OnItemClickListener mListener;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder)holder).bind(comments.get(position));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView email;
        TextView body;
        TextView id;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.comment_name);
            email = itemView.findViewById(R.id.comment_email);
            body = itemView.findViewById(R.id.comment_body);
            id = itemView.findViewById(R.id.comment_id);

            itemView.setOnClickListener(view -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }

        public void bind(Comment comment){
            name.setText(comment.getName());
            email.setText(comment.getEmail());
            body.setText(comment.getBody());
            id.setText(Integer.toString(comment.getId()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
