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

public class RecyclerAdapter extends FirebaseRecyclerAdapter<ModelList, RecyclerAdapter.MyViewHolder> {

    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<ModelList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ModelList model) {
        String subject = model.getSub();
        subject = subject.replace("[","");
        subject = subject.replace("]","");

        holder.name.setText("Student Name : "+model.getName());
        holder.subject.setText("Subjects : "+subject);
        holder.amount.setText("Amount Paid : "+model.getAmount());
        holder.date.setText("Date Of Payment : "+model.getDate());
        holder.note.setText("Transaction Note : "+model.getTransactionNote());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(view);

    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,subject,amount,date,note;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerName);
            subject = itemView.findViewById(R.id.recyclerSubject);
            amount = itemView.findViewById(R.id.recyclerAmount);
            date = itemView.findViewById(R.id.recyclerDate);
            note = itemView.findViewById(R.id.recyclerTranNote);
        }
    }
}
