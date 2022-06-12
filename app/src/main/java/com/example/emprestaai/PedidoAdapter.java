package com.example.emprestaai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    private ArrayList<Pedido> pedidos;
    Context activity;
    public PedidoAdapter(Context context,ArrayList<Pedido> pedidos) {
        activity = context;
        this.pedidos = pedidos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro;
        ImageView dImagem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvPeriodo = itemView.findViewById(R.id.tvPeriodo);
            dImagem = itemView.findViewById(R.id.ivObjeto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocalEncontro = itemView.findViewById(R.id.tvLocalEncontro);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_pedidos, parent, false);
        return new PedidoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNome.setText(pedidos.get(position).getObjeto().getNome());
        holder.tvStatus.setText("Solicitado");
        holder.tvLocalEncontro.setText(pedidos.get(position).getLocal());
        holder.tvPeriodo.setText(pedidos.get(position).getPeriodo());
        holder.dImagem.setImageDrawable(pedidos.get(position).getObjeto().getImagem());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
}