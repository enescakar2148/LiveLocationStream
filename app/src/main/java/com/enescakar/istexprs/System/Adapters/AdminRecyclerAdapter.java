package com.enescakar.istexprs.System.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.enescakar.istexprs.R;
import com.enescakar.istexprs.UI.AdminKuryeDetailsScreen;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.Holder> {
    private Context context;

    public AdminRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.admin_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AdminKuryeDetailsScreen.class).putExtra("position",holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private CardView cardView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.kuryeCard);
        }
    }
}
