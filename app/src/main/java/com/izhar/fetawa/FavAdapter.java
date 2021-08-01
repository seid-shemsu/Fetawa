package com.izhar.fetawa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {
    List<Answer> answerList;
    Context context;
    Fav myFav;

    public FavAdapter(List<Answer> answerList, Context context) {
        this.answerList = answerList;
        this.context = context;
        myFav = new Fav(context);
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_question, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Answer current = answerList.get(position);
        holder.question.setText(current.getQuestion());
        holder.roll.setText(Integer.toString(position + 1));
        holder.fav.setImageResource(R.drawable.fav);

    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        TextView question, roll;
        ImageView fav;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            fav = itemView.findViewById(R.id.fav_image);
            roll = itemView.findViewById(R.id.roll);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myFav.deleteFav(answerList.get(getAdapterPosition()).getId());
                    deleteItem(getAdapterPosition());
                }
            });
            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, Detail.class)
                            .putExtra("id", answerList.get(getAdapterPosition()).getId())
                            .putExtra("question", answerList.get(getAdapterPosition()).getQuestion())
                            .putExtra("answer", answerList.get(getAdapterPosition()).getAnswer()));
                }
            });
        }

        private void deleteItem(int adapterPosition) {
            answerList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(0, answerList.size());
        }
    }
}
