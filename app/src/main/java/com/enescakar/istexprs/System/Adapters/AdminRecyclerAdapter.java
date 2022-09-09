package com.enescakar.istexprs.System.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.enescakar.istexprs.Model.Kurye;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.UI.AdminKuryeDetailsScreen;

import java.util.ArrayList;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.Holder> {
    private Context context;
    private ArrayList<Kurye> kuryeler;

    public AdminRecyclerAdapter(Context context, ArrayList<Kurye> kuryeler) {
        this.context = context;
        this.kuryeler = kuryeler;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.admin_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.kuryeNo.setText(kuryeler.get(position).getKuryeNo());
        holder.plaka.setText(kuryeler.get(position).getPlaka());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AdminKuryeDetailsScreen.class).putExtra("kuryeId",kuryeler.get(position).getKuryeId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (kuryeler.size() != 0){
            return kuryeler.size();
        }
        return 0;
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView plaka, kuryeNo;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.kuryeCard);
            plaka = itemView.findViewById(R.id.plakaText);
            kuryeNo = itemView.findViewById(R.id.kuryeNoText);
        }
    }
}
