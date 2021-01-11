package com.amitthakare.sanskarschool.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amitthakare.sanskarschool.Model.ModelList;
import com.amitthakare.sanskarschool.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerAdapter2 extends FirebaseRecyclerAdapter<ModelList, RecyclerAdapter2.MyViewHolder> {

    public RecyclerAdapter2(@NonNull FirebaseRecyclerOptions<ModelList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ModelList model) {

        holder.name.setText(model.getName());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row2,parent,false);
        return new MyViewHolder(view);

    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerName2);
        }
    }
}
