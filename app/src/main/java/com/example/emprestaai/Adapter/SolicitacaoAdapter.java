package com.example.emprestaai.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;

import java.util.ArrayList;

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder>{
    private ArrayList<Pedido> solicitacoes;
    PedidoClicado activity;

    public interface PedidoClicado{
        void onPedidoClicked(int posicao, ArrayList<Pedido> pedidos);
    }

    public SolicitacaoAdapter(Context context, ArrayList<Pedido> lista) {
        solicitacoes = lista;
        activity = (PedidoClicado)  context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro,tvSolicitante;
        ImageView dImagem;
        ImageButton btnAceitar, btnRecusar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvPeriodo = itemView.findViewById(R.id.tvPeriodo);
            dImagem = itemView.findViewById(R.id.ivObjeto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocalEncontro = itemView.findViewById(R.id.tvLocalEncontro);
            tvSolicitante = itemView.findViewById(R.id.tvSolicitante);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onPedidoClicked(solicitacoes.indexOf((Pedido) itemView.getTag()),solicitacoes);
                }
            });
        }
    }

    @NonNull
    @Override
    public SolicitacaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_solicitacoes, parent, false);
        return new SolicitacaoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(solicitacoes.get(position));
        holder.tvNome.setText(solicitacoes.get(position).getObjeto().getNome());
        holder.tvStatus.setText(solicitacoes.get(position).getStatus());
        holder.tvLocalEncontro.setText(solicitacoes.get(position).getLocal());
        holder.tvPeriodo.setText(solicitacoes.get(position).getPeriodo());
        holder.tvSolicitante.setText(solicitacoes.get(position).getSolicitante());
        holder.dImagem.setImageBitmap(solicitacoes.get(position).getObjeto().getImagem());
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }
}
