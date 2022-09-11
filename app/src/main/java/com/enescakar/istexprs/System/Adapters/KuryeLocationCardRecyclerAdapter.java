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

import com.enescakar.istexprs.R;
import com.enescakar.istexprs.UI.AdminKuryeDetailsScreen;
import com.enescakar.istexprs.UI.AdminKuryeMapDetailsScreen;

import java.util.ArrayList;

public class KuryeLocationCardRecyclerAdapter extends RecyclerView.Adapter<KuryeLocationCardRecyclerAdapter.Holder> {

    private Context context;
    private ArrayList<String> dateList;
    private String kuryeId;


    public KuryeLocationCardRecyclerAdapter(Context context, ArrayList<String> dateList, String kuryeId) {
        this.context = context;
        this.dateList = dateList;
        this.kuryeId = kuryeId;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KuryeLocationCardRecyclerAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.kurye_location_item, parent, false));

    }

    @Override
    public int getItemCount() {
        if (dateList.size() != 0){
            return dateList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.dateText_RecyclerItem.setText(dateList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AdminKuryeMapDetailsScreen.class).putExtra("kuryeId",kuryeId).putExtra("date", dateList.get(position)));
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView dateText_RecyclerItem;
        private CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            dateText_RecyclerItem = itemView.findViewById(R.id.dateText_RecyclerItem);
            cardView = itemView.findViewById(R.id.kuryeDateCard);
        }
    }
}
