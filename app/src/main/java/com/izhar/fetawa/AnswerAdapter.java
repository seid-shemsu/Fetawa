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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    List<Answer> answerList;
    Context context;
    List<String> ids;
    Fav myFav;

    public AnswerAdapter(List<Answer> answerList, Context context, List<String> ids) {
        this.answerList = answerList;
        this.context = context;
        this.ids = ids;
        myFav = new Fav(context);
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_question, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer current = answerList.get(position);
        holder.question.setText(current.getQuestion());
        holder.roll.setText(Integer.toString(position + 1));
        if (myFav.getFav(ids.get(position)).moveToFirst()){
            holder.fav.setImageResource(R.drawable.fav);
        }
        else {
            holder.fav.setImageResource(R.drawable.fav_empty);
        }
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView question, roll;
        ImageView fav;
        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            fav = itemView.findViewById(R.id.fav_image);
            roll = itemView.findViewById(R.id.roll);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myFav.getFav(ids.get(getAdapterPosition())).moveToFirst()){
                        fav.setImageResource(R.drawable.fav_empty);
                        myFav.deleteFav(ids.get(getAdapterPosition()));
                    }
                    else {
                        fav.setImageResource(R.drawable.fav);
                        myFav.insert_new_fav(answerList.get(getAdapterPosition()));
                        Toast.makeText(context, answerList.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
                        //myFav.insert_new_fav(ids.get(getAdapterPosition()), answerList.get(getAdapterPosition()).getQuestion(), answerList.get(getAdapterPosition()).getAnswer());
                    }
                }
            });
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
