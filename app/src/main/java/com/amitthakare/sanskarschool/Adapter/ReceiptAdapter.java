package com.amitthakare.sanskarschool.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amitthakare.sanskarschool.Model.ReceiptModel;
import com.amitthakare.sanskarschool.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ReceiptAdapter extends FirebaseRecyclerAdapter<ReceiptModel,ReceiptAdapter.MyViewHolder> {

    public ReceiptAdapter(@NonNull FirebaseRecyclerOptions<ReceiptModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ReceiptModel model) {

        holder.receipt_studentname.setText(model.getName());
        Glide.with(holder.receipt_img.getContext()).load(model.getImg()).into(holder.receipt_img);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_receipt,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView receipt_img;
        TextView receipt_studentname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            receipt_img = itemView.findViewById(R.id.receipt_img);
            receipt_studentname = itemView.findViewById(R.id.receipt_studentname);

        }
    }
}
