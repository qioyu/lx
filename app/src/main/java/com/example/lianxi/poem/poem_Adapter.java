package com.example.lianxi.poem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lianxi.R;

import java.util.ArrayList;
import java.util.List;

public class poem_Adapter extends RecyclerView.Adapter<poem_Adapter.Viewholder> {

    List<poemdata.ResultBean> result = new ArrayList<>();
    public void refresh(List<poemdata.ResultBean> result){
        this.result = result;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.poem_item, viewGroup, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        viewholder.poem_item_title.setText(result.get(i).getTitle());
        viewholder.poem_item_content.setText(result.get(i).getContent());
        viewholder.poem_item_authors.setText(result.get(i).getAuthors());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView poem_item_title;
        TextView poem_item_content;
        TextView poem_item_authors;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            poem_item_title = itemView.findViewById(R.id.poem_item_title);
            poem_item_content = itemView.findViewById(R.id.poem_item_content);
            poem_item_authors = itemView.findViewById(R.id.poem_item_authors);

        }
    }
}
