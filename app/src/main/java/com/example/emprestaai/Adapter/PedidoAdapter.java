package com.example.emprestaai.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    private ArrayList<Pedido> pedidos;
    Context activity;
    public PedidoAdapter(Context context,ArrayList<Pedido> pedidos) {
        activity = context;
        this.pedidos = pedidos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro,tvDono;
        ImageView dImagem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvPeriodo = itemView.findViewById(R.id.tvPeriodo);
            dImagem = itemView.findViewById(R.id.ivObjeto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocalEncontro = itemView.findViewById(R.id.tvLocalEncontro);
            tvDono = itemView.findViewById(R.id.tvDono);

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
        holder.tvStatus.setText(pedidos.get(position).getObjeto().getStatus());
        holder.tvLocalEncontro.setText(pedidos.get(position).getLocal());
        holder.tvPeriodo.setText(pedidos.get(position).getPeriodo());
        holder.tvDono.setText(pedidos.get(position).getObjeto().getDono());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
}
