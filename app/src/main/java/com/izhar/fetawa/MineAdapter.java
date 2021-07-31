package com.izhar.fetawa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.MineViewHolder> {
    List<Answer> answerList;
    Context context;
    public MineAdapter(List<Answer> answerList, Context context) {
        this.answerList = answerList;
        this.context = context;
    }

    @NonNull
    @Override
    public MineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_question, parent, false);
        return new MineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineViewHolder holder, int position) {
        Answer current = answerList.get(position);
        holder.question.setText(current.getQuestion());
        holder.roll.setText(Integer.toString(position + 1));
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class MineViewHolder extends RecyclerView.ViewHolder {
        TextView question, roll;
        ImageView fav;
        public MineViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            fav = itemView.findViewById(R.id.fav_image);
            fav.setVisibility(View.GONE);
            roll = itemView.findViewById(R.id.roll);
            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Detail.class)
                            .putExtra("id", answerList.get(getAdapterPosition()).getId())
                            .putExtra("question",answerList.get(getAdapterPosition()).getQuestion())
                            .putExtra("answer",answerList.get(getAdapterPosition()).getAnswer()));
                }
            });
        }
    }
}
